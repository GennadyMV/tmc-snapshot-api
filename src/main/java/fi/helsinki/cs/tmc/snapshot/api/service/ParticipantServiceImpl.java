package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.model.Participant;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotEvent;

import java.io.IOException;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class ParticipantServiceImpl implements ParticipantService {

    private static final Logger LOG = LoggerFactory.getLogger(ParticipantServiceImpl.class);

    @Autowired
    private SnapshotEventService eventService;

    @Autowired
    private SnapshotOrganiserService snapshotOrganizer;

    @Override
    public Participant find(final String instance, final String username) throws IOException {

        final Participant participant = new Participant(username);

        final Collection<SnapshotEvent> events = eventService.find(instance, username);

        snapshotOrganizer.organise(participant, events);

        return participant;
    }
}
