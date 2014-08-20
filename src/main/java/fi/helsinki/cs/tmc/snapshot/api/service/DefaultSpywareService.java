package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.http.HttpRequestBuilder;
import fi.helsinki.cs.tmc.snapshot.api.util.RequestHandler;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.stereotype.Service;

@Service
public final class DefaultSpywareService implements SpywareService {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultSpywareService.class);

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

    @Override
    public String fetchIndex(final String instance, final String username) throws IOException {

        LOG.info("Fetching Spyware-index for {} from instance {}...",
                 username,
                 instance);

        requestBuilder.setPath(String.format("/%s/%s.idx", instance, username));

        final String data = RequestHandler.fetchAsString(requestBuilder);

        LOG.info("Spyware-index fetched.");

        return data;
    }

    @Cacheable("spyware")
    @Override
    public byte[] fetchChunkByRange(final String instance, final String username, final int start, final int end) throws IOException {

        LOG.info("Fetching Spyware-data for {} from instance {} with range {}–{}...",
                 username,
                 instance,
                 start,
                 end);

        final ClientHttpRequest request = requestBuilder.setPath(String.format("/%s/%s.dat", instance, username)).get();
        request.getHeaders().set("Range", String.format("bytes=%d-%d", start, end));

        final byte[] bytes = RequestHandler.fetchAsByteArray(request);

        LOG.info("Spyware-data fetched.");

        return bytes;
    }

    @Override
    public String fetchParticipants(final String instance) throws IOException {

        requestBuilder.setPath(String.format("/%s/index.txt", instance));

        return RequestHandler.fetchAsString(requestBuilder);
    }
}
