package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.exception.NotFoundException;
import fi.helsinki.cs.tmc.snapshot.api.http.HttpRequestBuilder;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;

@Service
public final class HttpSpywareService implements SpywareService {

    private final Logger logger = LoggerFactory.getLogger(HttpSpywareService.class);

    @Value("${spyware.url}")
    private String spywareUrl;

    @Value("${spyware.username}")
    private String spywareUsername;

    @Value("${spyware.password}")
    private String spywarePassword;

    private HttpRequestBuilder requestBuilder;

    @PostConstruct
    private void initialise() {

        requestBuilder = new HttpRequestBuilder(spywareUrl, 80, "http")
                             .authenticate(spywareUsername, spywarePassword);
    }

    @Cacheable("RawSpywareData")
    @Override
    public byte[] fetchData(final String event, final String instance, final String username) throws IOException {

        final String[] indexes = event.split("\\s+");

        final int start = Integer.parseInt(indexes[0]);
        final int length = Integer.parseInt(indexes[1]);

        logger.info("Fetching Spyware-data for {} from instance {} with range {}–{}...",
                    username,
                    instance,
                    start,
                    start + length);

        final ClientHttpRequest request = requestBuilder.setPath(instance + username + ".dat").build();
        request.getHeaders().set("Range", String.format("bytes=%d-%d", start, start + length));

        final ClientHttpResponse response = request.execute();

        if (response.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
            throw new NotFoundException();
        }

        if (response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError()) {
            throw new IOException("Remote server returned status " + response.getRawStatusCode());
        }

        final byte[] bytes = IOUtils.toByteArray(response.getBody());
        response.close();

        logger.info("Spyware-data fetched.");

        return bytes;
    }

    @Override
    public InputStream fetchIndex(final String instance, final String username) throws IOException {

        logger.info("Fetching Spyware-index for {} from instance {}...",
                    username,
                    instance);

        final ClientHttpResponse response = requestBuilder.setPath(instance + username + ".idx")
                                                          .build()
                                                          .execute();

        if (response.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
            throw new NotFoundException();
        }

        if (response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError()) {
            throw new IOException("Remote server returned status " + response.getRawStatusCode());
        }

        logger.info("Spyware-index fetched.");

        return response.getBody();
    }
}
