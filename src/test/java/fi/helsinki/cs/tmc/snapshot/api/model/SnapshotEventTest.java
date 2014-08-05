package fi.helsinki.cs.tmc.snapshot.api.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class SnapshotEventTest {

    private SnapshotEvent event;

    @Before
    public void setUp() {

        this.event = new SnapshotEvent();
    }

    @Test
    public void canSetExerciseName() {

        event.setExerciseName("exerciseName");

        assertEquals("exerciseName", event.getExerciseName());
    }

    @Test
    public void canSetCourseName() {

        event.setCourseName("courseName");

        assertEquals("courseName", event.getCourseName());
    }

    @Test
    public void canSetData() {

        event.setData("datadata");

        assertEquals("datadata", event.getData());
    }

    @Test
    public void canSetHappenedAt() {

        event.setHappenedAt(10L);

        assertEquals(10L, (long) event.getHappenedAt());
    }

    @Test
    public void canSetMetadata() {

        event.setMetadata("myMetadata");

        assertEquals("myMetadata", event.getMetadata());
    }

    @Test
    public void isProjectActionIfEventTypeIsCorrect() {

        event.setEventType("project_action");

        assertTrue(event.isProjectActionEvent());
    }

    @Test
    public void isNotProjectActionIfEventTypeIncorrect() {

        event.setEventType("test_event");

        assertFalse(event.isProjectActionEvent());
    }

    @Test
    public void eventWithSmallerHappenedAtIsFirstWhenOrdered() {

        final SnapshotEvent e1 = new SnapshotEvent();
        e1.setHappenedAt(1L);

        final SnapshotEvent e2 = new SnapshotEvent();
        e2.setHappenedAt(2L);

        assertTrue(e1.compareTo(e2) < 0);
        assertTrue(e2.compareTo(e1) > 0);
    }

    @Test
    public void eventsWithSameHappenedAtAreOrderedBasedOnNanotime() {

        final SnapshotEvent e1 = new SnapshotEvent();
        e1.setHappenedAt(1L);
        e1.setSystemNanotime(1L);

        final SnapshotEvent e2 = new SnapshotEvent();
        e2.setHappenedAt(1L);
        e2.setSystemNanotime(2L);

        assertTrue(e1.compareTo(e2) < 0);
        assertTrue(e2.compareTo(e1) > 0);
    }
}
