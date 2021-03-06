package fi.helsinki.cs.tmc.snapshot.api.http;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClients;

import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

public final class HttpRequestBuilder extends HttpComponentsClientHttpRequestFactory {

    private final String host;
    private final int port;
    private final String protocol;
    private final Map<String, String> parameters = new HashMap<>();

    private String path;

    public HttpRequestBuilder(final String host, final int port, final String protocol) {

        this.host = host;
        this.port = port;
        this.protocol = protocol;
    }

    private void initialiseBasicAuthentication(final String username, final String password) {

        final AuthCache cache = new BasicAuthCache();
        final AuthScheme scheme = new BasicScheme();
        final HttpHost httpHost = new HttpHost(host, port, protocol);

        cache.put(httpHost, scheme);

        final UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();

        credentialsProvider.setCredentials(new AuthScope(httpHost), credentials);
        setHttpClient(HttpClients.custom().setDefaultCredentialsProvider(credentialsProvider).build());
    }

    public URI buildURI() {

        final URIBuilder builder = new URIBuilder();

        builder.setHost(host)
               .setPath(path)
               .setPort(port)
               .setScheme(protocol);

        for (Entry<String, String> parameter : parameters.entrySet()) {
            builder.addParameter(parameter.getKey(), parameter.getValue());
        }

        try {
            return builder.build();
        } catch (URISyntaxException exception) {
            return null;
        }
    }

    public HttpRequestBuilder authenticate(final String username, final String password) {

        initialiseBasicAuthentication(username, password);
        return this;
    }

    public HttpRequestBuilder addParameter(final String key, final String value) {

        parameters.put(key, value);
        return this;
    }

    public HttpRequestBuilder setPath(final String path) {

        this.path = path;
        return this;
    }

    public ClientHttpRequest get() throws IOException {

        final URI uri = buildURI();
        final ClientHttpRequest request = createRequest(uri, HttpMethod.GET);

        return request;
    }
}
