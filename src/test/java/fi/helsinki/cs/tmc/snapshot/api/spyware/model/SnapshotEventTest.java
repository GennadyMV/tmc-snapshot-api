package fi.helsinki.cs.tmc.snapshot.api.spyware.model;

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

        final SnapshotEvent eventA = new SnapshotEvent();
        eventA.setHappenedAt(1L);

        final SnapshotEvent eventB = new SnapshotEvent();
        eventB.setHappenedAt(2L);

        assertTrue(eventA.compareTo(eventB) < 0);
        assertTrue(eventB.compareTo(eventA) > 0);
    }

    @Test
    public void eventsWithSameHappenedAtAreOrderedBasedOnNanotime() {

        final SnapshotEvent eventA = new SnapshotEvent();
        eventA.setHappenedAt(1L);
        eventA.setSystemNanotime(1L);

        final SnapshotEvent eventB = new SnapshotEvent();
        eventB.setHappenedAt(1L);
        eventB.setSystemNanotime(2L);

        assertTrue(eventA.compareTo(eventB) < 0);
        assertTrue(eventB.compareTo(eventA) > 0);
    }
}
