package fi.helsinki.cs.tmc.snapshot.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import fi.helsinki.cs.tmc.snapshot.api.exception.NotFoundException;
import fi.helsinki.cs.tmc.snapshot.api.model.Event;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
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

    private Map<String, Object> readMetadata(final SnapshotEvent event) {

        if (event.getMetadata() != null) {
            try {
                return mapper.readValue(event.getMetadata(), Map.class);
            } catch (IOException exception) {
                LOG.warn("Invalid metadata {}.", event.getMetadata());
            }
        }
        return Collections.EMPTY_MAP;
    }

    private String generateId(final SnapshotEvent event) {

        return event.getHappenedAt() + Long.toString(event.getSystemNanotime());
    }

    private Event snapshotEventToEvent(final SnapshotEvent event) {

        return new Event(generateId(event),
                         event.getEventType(),
                         new Date(event.getHappenedAt()),
                         readMetadata(event));
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

        for (SnapshotEvent event : snapshotEvents) {
            events.add(snapshotEventToEvent(event));
        }

        return events;
    }

    @Override
    public Event find(final String instanceId,
                      final String participantId,
                      final String courseId,
                      final String exerciseId,
                      final String eventId) throws IOException {

        final Collection<SnapshotEvent> events = exerciseService.find(instanceId,
                                                                      participantId,
                                                                      courseId,
                                                                      exerciseId)
                                                                .getSnapshotEvents();

        for (SnapshotEvent event : events) {

            if (generateId(event).equals(eventId)) {
                return snapshotEventToEvent(event);
            }
        }

        throw new NotFoundException();
    }
}
