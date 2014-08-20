package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.exception.NotFoundException;
import fi.helsinki.cs.tmc.snapshot.api.http.HttpRequestBuilder;
import fi.helsinki.cs.tmc.snapshot.api.model.Participant;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;

@Service
public final class DefaultParticipantService implements ParticipantService {

    @Autowired
    private SnapshotEventService snapshotEventService;

    @Autowired
    private SnapshotOrganiserService snapshotOrganiser;

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

    private Collection<Participant> parseParticipants(final String data) {

        final String[] content = data.split("\n");
        final List participants = new ArrayList();

        for (String username : content) {
            participants.add(new Participant(username));
        }

        return participants;
    }

    @Override
    public Collection<Participant> findAll(final String instance) throws IOException {

        final ClientHttpResponse response = requestBuilder.setPath(String.format("/%s/index.txt", instance))
                                                          .get()
                                                          .execute();

        if (response.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
            response.close();
            throw new NotFoundException();
        }

        if (response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError()) {
            response.close();
            throw new IOException("Remote server returned status " + response.getRawStatusCode());
        }

        final String index = IOUtils.toString(response.getBody());
        response.close();

        return parseParticipants(index);
    }

    @Override
    public Participant find(final String instance, final String id) throws IOException {

        final String username = new String(Base64.decodeBase64(id));
        final Participant participant = new Participant(username);
        final Collection<SnapshotEvent> events = snapshotEventService.findAll(instance, username);

        snapshotOrganiser.organise(participant, events);

        return participant;
    }
}
