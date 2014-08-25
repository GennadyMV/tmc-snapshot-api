package fi.helsinki.cs.tmc.snapshot.api.util;

import fi.helsinki.cs.tmc.snapshot.api.model.Snapshot;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotEvent;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public final class EventTransformerTest {

    private final EventTransformer eventTransformer = new EventTransformer();

    private SnapshotEvent createEvent(final String courseName,
                                      final String exerciseName,
                                      final Long happenedAt,
                                      final Long systemNanotime,
                                      final String eventType) {

        final SnapshotEvent event = new SnapshotEvent();

        event.setCourseName(courseName);
        event.setExerciseName(exerciseName);
        event.setHappenedAt(happenedAt);
        event.setSystemNanotime(systemNanotime);
        event.setEventType(eventType);

        return event;
    }

    @Test
    public void shouldConvertEventsToSnapshots() {

        final List<SnapshotEvent> events = new ArrayList<>();

        final SnapshotEvent event1 = createEvent("mooc", "exercise1", 100L, 100L, "text_insert");

        event1.getFiles().put("example.java", "public class Example { }");
        event1.getFiles().put("test.java", "public class Test { }");

        final SnapshotEvent event2 = createEvent("mooc", "exercise2", 101L, 101L, "text_remove");

        event2.getFiles().put("experiment.java", "public class Experiment { }");
        event2.getFiles().put("trial.java", "public class Trial { }");

        events.add(event1);
        events.add(event2);

        final List<Snapshot> snapshots = eventTransformer.toSnapshotList(events);

        assertEquals(2, snapshots.size());

        assertNull(snapshots.get(1).getFile("example.java"));
        assertNotNull(snapshots.get(1).getFile("ZXhwZXJpbWVudC5qYXZhMTAx"));

        assertNull(snapshots.get(1).getFile("test.java"));
        assertNotNull(snapshots.get(1).getFile("dGVzdC5qYXZhMTAw"));

        assertNull(snapshots.get(1).getFile("experiment.java"));
        assertNotNull(snapshots.get(1).getFile("ZXhhbXBsZS5qYXZhMTAw"));

        assertNull(snapshots.get(1).getFile("trial.java"));
        assertNotNull(snapshots.get(1).getFile("dHJpYWwuamF2YTEwMQ"));
    }

    @Test
    public void shouldReturnEmptySnapshotListIfGivenNullEvents() {

        final List<Snapshot> snapshots = eventTransformer.toSnapshotList(null);

        assertNotNull(snapshots);
    }

    @Test
    public void shouldNotProcessCompleteSnapshotsIfNotTypeFileDelete() {

        final List<SnapshotEvent> events = new ArrayList<>();

        final SnapshotEvent event1 = createEvent("hy", "ex", 50L, 50L, "code_snapshot");
        event1.setMetadata("file_create");

        final SnapshotEvent event2 = createEvent("hy", "ex", 55L, 55L, "code_snapshot");
        event2.setMetadata("file_delete");

        events.add(event1);
        events.add(event2);

        final List<Snapshot> snapshots = eventTransformer.toSnapshotList(events);

        assertEquals(1, snapshots.size());
        assertEquals("5555", snapshots.get(0).getId());
    }

    @Test
    public void shouldBuildExerciseContinuum() {

        final List<SnapshotEvent> events = new ArrayList<>();

        final SnapshotEvent event1 = createEvent("mooc", "continuum", 100L, 100L, "code_snapshot");

        event1.getFiles().put("example.java", "public class Example { }");
        event1.getFiles().put("test.java", "public class Test { }");

        final SnapshotEvent event2 = createEvent("mooc", "continuum", 102L, 102L, "text_insert");

        event2.getFiles().put("experiment.java", "public class Experiment { }");
        event2.getFiles().put("trial.java", "public class Trial { }");

        events.add(event1);
        events.add(event2);

        final List<Snapshot> snapshots = eventTransformer.toSnapshotList(events);

        assertEquals(2, snapshots.get(0).getFiles().size());
        assertEquals(4, snapshots.get(1).getFiles().size());

        assertNull(snapshots.get(1).getFile("example.java"));
        assertNotNull(snapshots.get(1).getFile("ZXhwZXJpbWVudC5qYXZhMTAy"));

        assertNull(snapshots.get(1).getFile("test.java"));
        assertNotNull(snapshots.get(1).getFile("dGVzdC5qYXZhMTAw"));

        assertNull(snapshots.get(1).getFile("experiment.java"));
        assertNotNull(snapshots.get(1).getFile("ZXhhbXBsZS5qYXZhMTAw"));

        assertNull(snapshots.get(1).getFile("trial.java"));
        assertNotNull(snapshots.get(1).getFile("dHJpYWwuamF2YTEwMg"));
    }
}
