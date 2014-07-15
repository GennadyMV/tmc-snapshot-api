package fi.helsinki.cs.tmc.snapshot.api.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import fi.helsinki.cs.tmc.snapshot.api.model.TmcParticipant;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;

@Service
public class TmcService implements TmcServiceInterface {

    @Value("${tmc.url}")
    private String tmcUrl;

    @Value("${tmc.username}")
    private String tmcUsername;

    @Value("${tmc.password}")
    private String tmcPassword;

    @Value("${tmc.version}")
    private String tmcVersion;

    private final HttpComponentsClientHttpRequestFactory httpFactory = new HttpComponentsClientHttpRequestFactory();

    @PostConstruct
    private void setCredentials() {

        // Basic authentication

        final AuthCache cache = new BasicAuthCache();
        final AuthScheme scheme = new BasicScheme();
        final HttpHost host = new HttpHost(tmcUrl, 80, "http");

        cache.put(host, scheme);

        // Set credentials

        final UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(tmcUsername, tmcPassword);
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();

        credentialsProvider.setCredentials(new AuthScope(host), credentials);

        httpFactory.setHttpClient(HttpClients.custom().setDefaultCredentialsProvider(credentialsProvider).build());
    }

    private String fetchJson(final String instance) throws IOException, URISyntaxException {

        final StringBuilder builder = new StringBuilder();

        builder.append(instance)
               .append("/participants.json");

        final URI url = new URIBuilder().setScheme("http")
                                        .setHost(tmcUrl)
                                        .setPort(80)
                                        .setPath(builder.toString())
                                        .setParameter("api_version", tmcVersion).build();

        final ClientHttpRequest request = httpFactory.createRequest(url, HttpMethod.GET);
        final ClientHttpResponse response = request.execute();

        final String responseBody = IOUtils.toString(response.getBody(), "UTF-8");

        response.close();

        // Response body
        return responseBody;
    }

    @Override
    public String findUsername(final String instance, final int userId) throws Exception {

        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        final JsonNode rootNode = mapper.readTree(fetchJson(instance));

        for (TmcParticipant participant : mapper.treeToValue(rootNode.path("participants"), TmcParticipant[].class)) {
            if (participant.getId() == userId) {
                return participant.getUsername();
            }
        }

        throw new Exception("User with id " + userId + " not found");
    }
}
