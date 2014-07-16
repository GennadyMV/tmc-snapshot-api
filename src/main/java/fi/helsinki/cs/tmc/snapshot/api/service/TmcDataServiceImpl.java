package fi.helsinki.cs.tmc.snapshot.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fi.helsinki.cs.tmc.snapshot.api.app.ApiException;
import fi.helsinki.cs.tmc.snapshot.api.http.HttpRequestBuilder;
import fi.helsinki.cs.tmc.snapshot.api.model.TmcParticipant;
import java.io.IOException;
import java.net.URISyntaxException;
import javax.annotation.PostConstruct;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;

@Service
public class TmcDataServiceImpl implements TmcDataService {

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
                      .auth(tmcUsername, tmcPassword)
                      .addParameter("api_version", tmcVersion);
    }

    private String fetchJson(final String instance) throws IOException, URISyntaxException {

        final ClientHttpResponse response = requestBuilder.setPath(instance + "/participants.json")
                                                          .build()
                                                          .execute();

        final String responseBody = IOUtils.toString(response.getBody(), "UTF-8");

        response.close();

        // Response body
        return responseBody;
    }

    @Override
    public String findUsername(final String instance, final long userId) throws ApiException {

        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        final JsonNode rootNode;
        try {
            rootNode = mapper.readTree(fetchJson(instance));
        } catch (IOException | URISyntaxException ex) {
            throw new ApiException(ex);
        }

        try {
            for (TmcParticipant participant : mapper.treeToValue(rootNode.path("participants"), TmcParticipant[].class)) {
                if (participant.getId() == userId) {
                    return participant.getUsername();
                }
            }
        } catch (JsonProcessingException ex) {
            throw new ApiException(ex);
        }

        throw new ApiException("User with id " + userId + " not found");
    }
}
