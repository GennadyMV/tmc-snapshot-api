package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotEvent;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;

@Service
public final class SpywareSnapshotService implements SnapshotService {

    @Autowired
    private SnapshotPatchService patchService;

    @Value("${url.spyware}")
    private String spywareUrl;

    @Value("${spyware.username}")
    private String spywareUsername;

    @Value("${spyware.password}")
    private String spywarePassword;

    private final HttpComponentsClientHttpRequestFactory httpFactory = new HttpComponentsClientHttpRequestFactory();

    @PostConstruct
    private void initialise() {

        setCredentials();
    }

    private void setCredentials() {

        // Basic authentication

        final AuthCache cache = new BasicAuthCache();
        final AuthScheme scheme = new BasicScheme();
        final HttpHost host = new HttpHost(spywareUrl, 80, "http");

        cache.put(host, scheme);

        // Set credentials

        final UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(spywareUsername, spywarePassword);
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();

        credentialsProvider.setCredentials(new AuthScope(host), credentials);

        httpFactory.setHttpClient(HttpClients.custom().setDefaultCredentialsProvider(credentialsProvider).build());
    }

    private InputStream fetchFile(final ClientHttpRequest request) throws IOException {

        final ClientHttpResponse response = request.execute();

        // Response body
        return response.getBody();
    }

    private ClientHttpRequest createRequest(final String instance,
                                            final String username,
                                            final String extension) throws IOException, URISyntaxException {

        final StringBuilder builder = new StringBuilder();

        builder.append(instance)
               .append(username)
               .append(extension);

        final URI url = new URL("http", spywareUrl, builder.toString()).toURI();
        return httpFactory.createRequest(url, HttpMethod.GET);
    }

    private List<byte[]> findRange(final InputStream index,
                                   final String instance,
                                   final String username) throws IOException, URISyntaxException {

        final List<byte[]> byteData = new ArrayList<>();

        // Convert to string
        final String indexData = IOUtils.toString(index);

        // Split on newlines
        for (String event : indexData.split("\\n")) {

            final String[] indexes = event.split("\\s+");
            final int start = Integer.parseInt(indexes[0]);
            final int length = Integer.parseInt(indexes[1]);

            final ClientHttpRequest request = createRequest(instance, username, ".dat");
            request.getHeaders().add("Range", String.format("bytes=%d-%d", start, start + length));

            final InputStream content = fetchFile(request);
            byteData.add(IOUtils.toByteArray(content));
        }

        return byteData;
    }

    @Override
    public Collection<SnapshotEvent> findAll(final String instance, final String username) throws IOException,
                                                                                                  URISyntaxException {

        // Fetch index and data file
        final InputStream index = fetchFile(createRequest(instance, username, ".idx"));

        // Find the byte range for reading .dat file
        final List<byte[]> content = findRange(index, instance, username);

        return patchService.patch(content);
    }
}
