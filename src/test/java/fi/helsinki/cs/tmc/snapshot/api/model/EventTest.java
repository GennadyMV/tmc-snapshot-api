package fi.helsinki.cs.tmc.snapshot.api.model;

import java.util.Collections;
import java.util.HashMap;

import org.junit.Test;

import static org.junit.Assert.*;

public final class EventTest {

    private final Event eventA;
    private final Event eventB;

    public EventTest() {

        eventA = new Event("id", "eventType", 0L, new HashMap<String, Object>());
        eventB = new Event("id2", "eventType2", 1L, null);
    }

    @Test
    public void testGetId() {

        assertEquals("id", eventA.getId());
    }

    @Test
    public void testEventType() {

        assertEquals("eventType", eventA.getEventType());
    }

    @Test
    public void testHappendAt() {

        assertEquals(0L, eventA.getTimestamp());
    }

    @Test
    public void testMetaData() {

        assertEquals(Collections.EMPTY_MAP, eventA.getMetadata());
    }

    @Test
    public void testGetIdForEvent2() {

        assertEquals("id2", eventB.getId());
    }

    @Test
    public void testEventTypeForEvent2() {

        assertEquals("eventType2", eventB.getEventType());
    }

    @Test
    public void testHappendAtForEvent2() {

        assertEquals(1L, eventB.getTimestamp());
    }

    @Test
    public void testMetaDataForEvent2() {

        assertNull(eventB.getMetadata());
    }
}
