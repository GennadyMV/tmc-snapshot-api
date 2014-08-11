package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.model.Course;
import fi.helsinki.cs.tmc.snapshot.api.model.Exercise;
import fi.helsinki.cs.tmc.snapshot.api.model.Participant;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public final class DefaultSnapshotOrganiserServiceTest {

    private final DefaultSnapshotOrganiserService organiserService = new DefaultSnapshotOrganiserService();

    private SnapshotEvent createEvent(final String courseName,
                                      final String exerciseName,
                                      final Long happenedAt,
                                      final String eventType) {

        final SnapshotEvent event = new SnapshotEvent();

        event.setCourseName(courseName);
        event.setExerciseName(exerciseName);
        event.setHappenedAt(happenedAt);
        event.setEventType(eventType);

        return event;
    }

    @Test
    public void shouldOrganiseSnapshots() {

        final Participant participant = new Participant("user");

        final SnapshotEvent event1 = createEvent("mooc", "ex1", 1L, "text_insert");
        final SnapshotEvent event2 = createEvent("mooc", "ex2", 2L, "text_insert");
        final SnapshotEvent event3 = createEvent("mooc", "ex3", 3L, "text_insert");

        final Collection<SnapshotEvent> events = new ArrayList<>();

        events.add(event1);
        events.add(event2);
        events.add(event3);

        organiserService.organise(participant, events);

        assertEquals(1, participant.getCourses().size());
        assertNotNull(participant.getCourse("bW9vYw"));

        final Course course = participant.getCourses().iterator().next();

        assertEquals(3, course.getExercises().size());

        final Collection<Exercise> exerciseCollection = course.getExercises();
        final List<Exercise> exercises = new ArrayList(exerciseCollection);

        for (Exercise exercise : exercises) {
            assertEquals(1, exercise.getSnapshotEvents().size());
        }
    }
}
