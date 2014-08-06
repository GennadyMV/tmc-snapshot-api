package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.exception.NotFoundException;
import fi.helsinki.cs.tmc.snapshot.api.http.HttpRequestBuilder;

import java.io.IOException;

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

    private static final Logger LOG = LoggerFactory.getLogger(HttpSpywareService.class);

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
    public byte[] fetchChunk(final String instance, final String username, final int start, final int end) throws IOException {

        LOG.info("Fetching Spyware-data for {} from instance {} with range {}–{}...",
                    username,
                    instance,
                    start,
                    end);

        final ClientHttpRequest request = requestBuilder.setPath(String.format("/%s/%s.dat", instance, username)).build();
        request.getHeaders().set("Range", String.format("bytes=%d-%d", start, end));

        final ClientHttpResponse response = request.execute();

        if (response.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
            response.close();
            throw new NotFoundException();
        }

        if (response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError()) {
            response.close();
            throw new IOException("Remote server returned status " + response.getRawStatusCode());
        }

        final byte[] bytes = IOUtils.toByteArray(response.getBody());
        response.close();

        LOG.info("Spyware-data fetched.");

        return bytes;
    }


    @Override
    public String fetchIndex(final String instance, final String username) throws IOException {

        LOG.info("Fetching Spyware-index for {} from instance {}...",
                    username,
                    instance);

        final ClientHttpResponse response = requestBuilder.setPath(String.format("/%s/%s.idx", instance, username))
                                                          .build()
                                                          .execute();

        if (response.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
            response.close();
            throw new NotFoundException();
        }

        if (response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError()) {
            response.close();
            throw new IOException("Remote server returned status " + response.getRawStatusCode());
        }

        LOG.info("Spyware-index fetched.");

        final String index = IOUtils.toString(response.getBody());
        response.close();

        return index;
    }
}
