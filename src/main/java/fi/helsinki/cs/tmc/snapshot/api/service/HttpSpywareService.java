package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.app.ApiException;
import fi.helsinki.cs.tmc.snapshot.api.http.HttpRequestBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;

@Service
public final class HttpSpywareService implements SpywareService {

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
    public byte[] getData(final String event, final String instance, final String username) throws ApiException {

        final String[] indexes = event.split("\\s+");
        final int start = Integer.parseInt(indexes[0]);
        final int length = Integer.parseInt(indexes[1]);

        try {

            final ClientHttpRequest request = requestBuilder.setPath(instance + username + ".dat").build();
            request.getHeaders().set("Range", String.format("bytes=%d-%d", start, start + length));

            final ClientHttpResponse response = request.execute();

            final byte[] bytes = IOUtils.toByteArray(response.getBody());
            response.close();
            return bytes;

        } catch (IOException | URISyntaxException exception) {
            throw new ApiException(exception);
        }
    }

    @Override
    public InputStream getIndex(final String instance, final String username) throws ApiException {

        try {

            return requestBuilder.setPath(instance + username + ".idx")
                                 .build()
                                 .execute()
                                 .getBody();

        } catch (URISyntaxException | IOException exception) {
            throw new ApiException(exception);
        }
    }
}
