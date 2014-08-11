package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.model.Participant;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotEvent;

import java.io.IOException;
import java.util.Collection;

import org.apache.commons.codec.binary.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class DefaultParticipantService implements ParticipantService {

    @Autowired
    private SnapshotEventService snapshotEventService;

    @Autowired
    private SnapshotOrganiserService snapshotOrganiser;

    @Override
    public Participant find(final String instance, final String id) throws IOException {

        final String username = new String(Base64.decodeBase64(id));
        final Participant participant = new Participant(username);
        final Collection<SnapshotEvent> events = snapshotEventService.findAll(instance, username);

        snapshotOrganiser.organise(participant, events);

        return participant;
    }
}
