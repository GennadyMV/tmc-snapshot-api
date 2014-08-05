package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.model.Snapshot;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotEvent;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class EventTransformerTest {

    private final EventTransformer eventTransformer = new EventTransformer();

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
    public void shouldConvertEventsToSnapshots() {

        final List<SnapshotEvent> events = new ArrayList<>();

        final SnapshotEvent event1 = createEvent("mooc", "exercise1", 100L, "text_insert");

        event1.getFiles().put("example.java", "public class Example { }");
        event1.getFiles().put("test.java", "public class Test { }");

        final SnapshotEvent event2 = createEvent("mooc", "exercise2", 101L, "text_remove");

        event2.getFiles().put("experiment.java", "public class Experiment { }");
        event2.getFiles().put("trial.java", "public class Trial { }");

        events.add(event1);
        events.add(event2);

        final List<Snapshot> snapshots = eventTransformer.toSnapshotList(events);

        assertEquals(2, snapshots.size());

        assertEquals("exercise1", snapshots.get(0).getExercise().getName());
        assertEquals("exercise2", snapshots.get(1).getExercise().getName());

        assertNotNull(snapshots.get(0).getFile("example.java"));
        assertNotNull(snapshots.get(0).getFile("test.java"));

        assertNotNull(snapshots.get(1).getFile("experiment.java"));
        assertNotNull(snapshots.get(1).getFile("trial.java"));
    }
}
