package fi.helsinki.cs.tmc.snapshot.api.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import fi.helsinki.cs.tmc.snapshot.api.http.HttpRequestBuilder;
import fi.helsinki.cs.tmc.snapshot.api.model.TmcParticipant;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;

@Service
public final class HttpTmcService implements TmcService {

    private final Logger logger = LoggerFactory.getLogger(HttpTmcService.class);

    @Value("${tmc.url}")
    private String tmcUrl;

    @Value("${tmc.username}")
    private String tmcUsername;

    @Value("${tmc.password}")
    private String tmcPassword;

    @Value("${tmc.version}")
    private String tmcVersion;

    private HttpRequestBuilder requestBuilder;

    @PostConstruct
    private void initialise() {

        requestBuilder = new HttpRequestBuilder(tmcUrl, 80, "http")
                        .authenticate(tmcUsername, tmcPassword)
                        .addParameter("api_version", tmcVersion);
    }

    private String fetchJson(final String instance) throws IOException {

        logger.info("Fetching TMC-participants as JSON from instance {}...", instance);

        final ClientHttpResponse response = requestBuilder.setPath(instance + "/participants.json")
                                                          .build()
                                                          .execute();

        final String responseBody = IOUtils.toString(response.getBody(), "UTF-8");

        response.close();

        logger.info("Fetched TMC-participants.");

        // Response body
        return responseBody;
    }

    @Override
    public List<TmcParticipant> findAll(final String instance) throws IOException {

        logger.info("Finding participants from instance {}...", instance);

        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        final String json = fetchJson(instance);
        final JsonNode rootNode = mapper.readTree(json);
        final TmcParticipant[] participants = mapper.treeToValue(rootNode.path("participants"), TmcParticipant[].class);

        return Arrays.asList(participants);
    }

    @Cacheable("TmcUsername")
    @Override
    public String findUsernameById(final String instance, final long id) throws IOException {

        logger.info("Finding username for id {} from instance {}...", id, instance);

        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        final JsonNode rootNode = mapper.readTree(fetchJson(instance));

        for (TmcParticipant participant : mapper.treeToValue(rootNode.path("participants"), TmcParticipant[].class)) {

            logger.info("Found username {} for id {}.", participant.getUsername(), id);

            if (participant.getId() == id) {
                return participant.getUsername();
            }
        }

        logger.info("No username found for id {}.", id);

        return null;
    }
}
