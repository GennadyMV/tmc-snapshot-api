package fi.helsinki.cs.tmc.snapshot.api.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import fi.helsinki.cs.tmc.snapshot.api.http.HttpRequestBuilder;
import fi.helsinki.cs.tmc.snapshot.api.model.TmcParticipant;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public final class HttpTmcService implements TmcService {

    @Value("${tmc.url}")
    private String tmcUrl;

    @Value("${tmc.username}")
    private String tmcUsername;

    @Value("${tmc.password}")
    private String tmcPassword;

    @Value("${tmc.version}")
    private String tmcVersion;

    private HttpRequestBuilder requestBuilder;
    private RestTemplate restTemplate;

    @PostConstruct
    private void initialise() {

        requestBuilder = new HttpRequestBuilder(tmcUrl, 80, "http")
                        .authenticate(tmcUsername, tmcPassword)
                        .addParameter("api_version", tmcVersion);

        restTemplate = new RestTemplate(requestBuilder);
    }

    protected String fetchJson(final String instance) throws IOException {

        requestBuilder.setPath(instance + "/participants.json");

        return restTemplate.getForObject(requestBuilder.buildURI(), String.class);
    }

    @Cacheable("TmcUsername")
    @Override
    public String findUsername(final String instance, final long userId) throws IOException {

        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        final JsonNode rootNode;

        rootNode = mapper.readTree(fetchJson(instance));

        for (TmcParticipant participant : mapper.treeToValue(rootNode.path("participants"), TmcParticipant[].class)) {
            if (participant.getId() == userId) {
                return participant.getUsername();
            }
        }

        return null;
    }
}
