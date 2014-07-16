package fi.helsinki.cs.tmc.snapshot.api.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;

@Component
public class SpywareServerDataService implements ServerDataService {

    @Value("${spyware.url}")
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

    private ClientHttpResponse fetchFile(final ClientHttpRequest request) throws IOException {

        return request.execute();
    }

    @Cacheable("RawSpywareData")
    @Override
    public byte[] getData(final String event, final String instance, final String username) throws IOException, URISyntaxException {

        final String[] indexes = event.split("\\s+");
        final int start = Integer.parseInt(indexes[0]);
        final int length = Integer.parseInt(indexes[1]);

        final ClientHttpRequest request = createRequest(instance, username, ".dat");
        request.getHeaders().add("Range", String.format("bytes=%d-%d", start, start + length));

        final byte[] bytes;
        try (ClientHttpResponse response = fetchFile(request)) {
            bytes = IOUtils.toByteArray(response.getBody());
        }
        return bytes;
    }

    @Override
    public InputStream getIndex(final String instance, final String username) throws IOException, URISyntaxException {

        final ClientHttpResponse response = fetchFile(createRequest(instance, username, ".idx"));
        return response.getBody();
    }
}
