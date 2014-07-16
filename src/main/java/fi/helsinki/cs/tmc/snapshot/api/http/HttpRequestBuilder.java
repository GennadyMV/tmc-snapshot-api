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

public class HttpRequestBuilder extends HttpComponentsClientHttpRequestFactory {

    private String host;
    private String path;
    private int port;
    private String protocol;

    private String username;
    private String password;

    private Map<String, String> extraHeaders;
    private Map<String, String> parameters;

    public HttpRequestBuilder(final String host, final int port, final String protocol) {

        this.host = host;
        this.port = port;
        this.protocol = protocol;
    }

    public HttpRequestBuilder auth(final String user, final String pass) {

        this.username = user;
        this.password = pass;

        setupBasicAuth();
        return this;
    }

    public HttpRequestBuilder setHost(final String newHost) {

        this.host = newHost;
        return this;
    }

    public HttpRequestBuilder setPath(final String newPath) {

        this.path = newPath;
        return this;
    }

    public HttpRequestBuilder setPort(final int newPort) {

        this.port = newPort;
        return this;
    }

    public HttpRequestBuilder setProtocol(final String newProtocol) {

        this.protocol = newProtocol;
        return this;
    }

    public HttpRequestBuilder setHeaders(final Map<String, String> headers) {

        this.extraHeaders = headers;
        return this;
    }

    public HttpRequestBuilder addHeader(final String key, final String value) {

        if (extraHeaders == null) {
            extraHeaders = new HashMap<>();
        }

        extraHeaders.put(key, value);
        return this;
    }

    public HttpRequestBuilder setParameters(final Map<String, String> params) {

        this.parameters = params;
        return this;
    }

    public HttpRequestBuilder addParameter(final String key, final String value) {

        if (parameters == null) {
            parameters = new HashMap<>();
        }

        parameters.put(key, value);
        return this;
    }

    public ClientHttpRequest build() throws IOException, URISyntaxException {

        final URI uri = buildURI();

        final ClientHttpRequest request = createRequest(uri, HttpMethod.GET);

        if (extraHeaders != null) {
            for (Entry<String, String> header : extraHeaders.entrySet()) {
                request.getHeaders().add(header.getKey(), header.getValue());
            }
        }

        return request;
    }

    private void setupBasicAuth() {

        final AuthCache cache = new BasicAuthCache();
        final AuthScheme scheme = new BasicScheme();
        final HttpHost httpHost = new HttpHost(this.host, this.port, this.protocol);

        cache.put(httpHost, scheme);

        final UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();

        credentialsProvider.setCredentials(new AuthScope(httpHost), credentials);
        setHttpClient(HttpClients.custom().setDefaultCredentialsProvider(credentialsProvider).build());
    }

    private URI buildURI() throws URISyntaxException {

        final URIBuilder builder = new URIBuilder();

        builder.setHost(host)
                .setPath(path)
                .setPort(port)
                .setScheme(protocol);

        if (parameters != null) {
            for (Entry<String, String> parameter : parameters.entrySet()) {
                builder.addParameter(parameter.getKey(), parameter.getValue());
            }
        }

        return builder.build();
    }

}
