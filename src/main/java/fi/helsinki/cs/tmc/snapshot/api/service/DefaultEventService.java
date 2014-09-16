package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.exception.NotFoundException;
import fi.helsinki.cs.tmc.snapshot.api.model.Event;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class DefaultEventService implements EventService {

    @Autowired
    private ExerciseService exerciseService;

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

            events.add(new Event(snapshotEvent.getHappenedAt() + "" + snapshotEvent.getSystemNanotime(),
                                 snapshotEvent.getEventType(),
                                 snapshotEvent.getMetadata()));
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
