package fi.helsinki.cs.tmc.snapshot.api.model;

import java.util.Collections;
import java.util.HashMap;
import org.junit.Test;
import static org.junit.Assert.*;

public class EventTest {

    private final Event event;
    private final Event event2;

    public EventTest() {
        event = new Event("id", "eventType", 0L, new HashMap<String, Object>());
        event2 = new Event("id2", "eventType2", 1L, null);
    }


    @Test
    public void testGetId() {

        assertEquals("id", event.getId());
    }

    @Test
    public void testEventType() {

        assertEquals("eventType", event.getEventType());
    }

    @Test
    public void testHappendAt() {

        assertEquals(0L, event.getHappendAt());
    }

    @Test
    public void testMetaData() {

        assertEquals(Collections.EMPTY_MAP, event.getMetadata());
    }

    @Test
    public void testGetIdForEvent2() {

        assertEquals("id2", event2.getId());
    }

    @Test
    public void testEventTypeForEvent2() {

        assertEquals("eventType2", event2.getEventType());
    }

    @Test
    public void testHappendAtForEvent2() {

        assertEquals(1L, event2.getHappendAt());
    }

    @Test
    public void testMetaDataForEvent2() {

        assertNull(event2.getMetadata());
    }
}
