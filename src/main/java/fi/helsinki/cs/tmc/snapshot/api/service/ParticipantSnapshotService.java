package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.model.Participant;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotEvent;

import java.io.IOException;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class ParticipantSnapshotService implements ParticipantService {

    @Autowired
    private SnapshotEventService eventService;

    @Autowired
    private SnapshotOrganiserService snapshotOrganiser;

    @Override
    public Participant findByInstanceAndId(final String instance, final String username) throws IOException {

        final Participant participant = new Participant(username);
        final Collection<SnapshotEvent> events = eventService.findByInstanceAndId(instance, username);

        snapshotOrganiser.organise(participant, events);

        return participant;
    }
}
