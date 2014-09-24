package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.model.Participant;
import fi.helsinki.cs.tmc.snapshot.api.model.SimpleParticipant;
import fi.helsinki.cs.tmc.snapshot.api.spyware.model.SnapshotEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.codec.binary.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class DefaultParticipantService implements ParticipantService {

    @Autowired
    private SnapshotEventService snapshotEventService;

    @Autowired
    private SnapshotOrganiserService snapshotOrganiser;

    @Autowired
    private SpywareService spywareService;

    private Collection<SimpleParticipant> parseParticipants(final String data) {

        final String[] content = data.split("\n");
        final List participants = new ArrayList();

        for (String username : content) {
            participants.add(new SimpleParticipant(username));
        }

        return participants;
    }

    @Override
    public List<SimpleParticipant> findAll(final String instanceId) throws IOException {

        final List<SimpleParticipant> sortedParticipants = new ArrayList<>(parseParticipants(spywareService.fetchParticipants(instanceId)));
        Collections.sort(sortedParticipants);

        return sortedParticipants;
    }

    @Override
    public Participant find(final String instanceId, final String participantId) throws IOException {

        final String username = new String(Base64.decodeBase64(participantId));
        final Participant participant = new Participant(username);
        final Collection<SnapshotEvent> events = snapshotEventService.findAll(instanceId, username);

        snapshotOrganiser.organise(participant, events);

        return participant;
    }
}
