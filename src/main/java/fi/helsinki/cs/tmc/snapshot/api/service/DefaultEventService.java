package fi.helsinki.cs.tmc.snapshot.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import fi.helsinki.cs.tmc.snapshot.api.exception.NotFoundException;
import fi.helsinki.cs.tmc.snapshot.api.model.Event;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotEvent;
import fi.helsinki.cs.tmc.snapshot.api.util.EventReader;

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

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(EventReader.class);

    @Autowired
    private ExerciseService exerciseService;

    private final ObjectMapper mapper = new ObjectMapper();

    private Event snapshotEventToEvent(final SnapshotEvent snapshotEvent) {

        final String id = snapshotEvent.getHappenedAt() + "" + snapshotEvent.getSystemNanotime();

        if (snapshotEvent.getMetadata() == null) {
            return new Event(id, snapshotEvent.getEventType(), null);
        }

        Map<String, Object> metadata = null;

        try {
            metadata = mapper.readValue(snapshotEvent.getMetadata(), Map.class);
        } catch (IOException ex) {
            LOG.warn("Invalid metadata for event {}. Metadata: {}", id, snapshotEvent.getMetadata());
        }

        return new Event(id, snapshotEvent.getEventType(), metadata);
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

        final List<Event> events = findAll(instanceId, participantId, courseId, exerciseId);

        for (Event event : events) {

            if (event.getId().equals(eventId)) {
                return event;
            }
        }

        throw new NotFoundException();
    }
}
