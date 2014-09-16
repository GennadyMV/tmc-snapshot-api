package fi.helsinki.cs.tmc.snapshot.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import fi.helsinki.cs.tmc.snapshot.api.exception.NotFoundException;
import fi.helsinki.cs.tmc.snapshot.api.model.Event;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class DefaultEventService implements EventService {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(DefaultEventService.class);

    @Autowired
    private ExerciseService exerciseService;

    private final ObjectMapper mapper = new ObjectMapper();

    private Map<String, Object> readMetadata(final SnapshotEvent snapshotEvent) {

        if (snapshotEvent.getMetadata() == null) {
            return null;
        }

        try {
            return mapper.readValue(snapshotEvent.getMetadata(), Map.class);
        } catch (IOException exception) {
            LOG.warn("Invalid metadata {}.", snapshotEvent.getMetadata());
        }

        return null;
    }

    private String generateId(final SnapshotEvent snapshotEvent) {

        return snapshotEvent.getHappenedAt() + "" + snapshotEvent.getMetadata();
    }

    private Event snapshotEventToEvent(final SnapshotEvent snapshotEvent) {

        return new Event(generateId(snapshotEvent),
                         snapshotEvent.getEventType(),
                         snapshotEvent.getHappenedAt(),
                         readMetadata(snapshotEvent));
    }

    @Override
    public List<Event> findAll(final String instanceId,
                               final String participantId,
                               final String courseId,
                               final String exerciseId) throws IOException {

        final Collection<SnapshotEvent> snapshotEvents = exerciseService.find(instanceId,
                                                                              participantId,
                                                                              courseId,
                                                                              exerciseId)
                                                                        .getSnapshotEvents();

        final List<Event> events = new ArrayList<>();

        for (SnapshotEvent snapshotEvent : snapshotEvents) {
            events.add(snapshotEventToEvent(snapshotEvent));
        }

        return events;
    }

    @Override
    public Event find(final String instanceId,
                      final String participantId,
                      final String courseId,
                      final String exerciseId,
                      final String eventId) throws IOException {

        final Collection<SnapshotEvent> snapshotEvents = exerciseService.find(instanceId,
                                                                              participantId,
                                                                              courseId,
                                                                              exerciseId)
                                                                        .getSnapshotEvents();

        for (SnapshotEvent snapshotEvent : snapshotEvents) {

            if (generateId(snapshotEvent).equals(eventId)) {
                return snapshotEventToEvent(snapshotEvent);
            }
        }

        throw new NotFoundException();
    }
}
