package fi.helsinki.cs.tmc.snapshot.api.model;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import org.junit.Test;

import static org.junit.Assert.*;

public final class EventTest {

    private final Event eventA;
    private final Event eventB;

    public EventTest() {

        eventA = new Event("id", "eventType", new Date(0L), new HashMap<String, Object>());
        eventB = new Event("idB", "eventTypeB", new Date(1L), null);
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

        assertEquals(new Date(0L), eventA.getTimestamp());
    }

    @Test
    public void testMetaData() {

        assertEquals(Collections.EMPTY_MAP, eventA.getMetadata());
    }

    @Test
    public void testGetIdForEventB() {

        assertEquals("idB", eventB.getId());
    }

    @Test
    public void testEventTypeForEventB() {

        assertEquals("eventTypeB", eventB.getEventType());
    }

    @Test
    public void testHappendAtForEventB() {

        assertEquals(new Date(1L), eventB.getTimestamp());
    }

    @Test
    public void testMetaDataForEventB() {

        assertNull(eventB.getMetadata());
    }
}
