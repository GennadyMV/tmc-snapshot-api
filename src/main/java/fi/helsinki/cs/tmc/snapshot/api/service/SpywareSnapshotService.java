package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotEvent;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;

import javax.annotation.PostConstruct;

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

    private InputStream fetchFile(final String instance,
                                  final String username,
                                  final String extension) throws IOException, URISyntaxException {

        final StringBuilder builder = new StringBuilder();

        builder.append(instance)
               .append(username)
               .append(extension);

        final URI url = new URL("http", spywareUrl, builder.toString()).toURI();
        final ClientHttpRequest request = httpFactory.createRequest(url, HttpMethod.GET);
        final ClientHttpResponse response = request.execute();

        // Response body
        return response.getBody();
    }

    @Override
    public Collection<SnapshotEvent> findAll(final String instance, final String username) throws IOException,
                                                                                                  URISyntaxException {

        // Fetch index and data file
        final InputStream index = fetchFile(instance, username, ".idx");
        final InputStream content = fetchFile(instance, username, ".dat");

        return patchService.patch(index, content);
    }
}
