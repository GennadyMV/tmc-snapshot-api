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

        final SnapshotEvent eventA = createEvent("mooc", "exerciseA", 1L, "text_insert");
        final SnapshotEvent eventB = createEvent("mooc", "exerciseB", 2L, "text_insert");
        final SnapshotEvent eventC = createEvent("mooc", "exerciseC", 3L, "text_insert");
        final SnapshotEvent eventD = createEvent("mooc", "exerciseD", 4L, "text_insert");

        final Collection<SnapshotEvent> events = new ArrayList<>();

        events.add(eventA);
        events.add(eventB);
        events.add(eventC);
        events.add(eventD);

        organiserService.organise(participant, events);

        assertEquals(1, participant.getCourses().size());
        assertNotNull(participant.getCourse("mooc"));

        final Course course = participant.getCourses().iterator().next();

        assertEquals(3, course.getExercises().size());

        final Collection<Exercise> exerciseCollection = course.getExercises();
        final List<Exercise> exercises = new ArrayList(exerciseCollection);

        for (Exercise exercise : exercises) {
            if (exercise.getName().equals("exerciseC")) {
                assertEquals(2, exercise.getSnapshotEvents().size());
            } else {
                assertEquals(1, exercise.getSnapshotEvents().size());
            }
        }
    }
}
