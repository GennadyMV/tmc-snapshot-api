package fi.helsinki.cs.tmc.snapshot.api.model;

import java.util.Collections;
import java.util.HashMap;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EventTest {

    private Event event;

    @Before
    public void setUp() {
        event = new Event("id", "eventType", new HashMap<String, Object>());
    }

    @Test
    public void testGetId() {

        assertEquals("id", event.getId());
    }

    @Test
    public void testEventType() {

        assertEquals("eventType", event.getId());
    }

    @Test
    public void testMetaData() {

        assertEquals(Collections.EMPTY_MAP, event.getId());
    }
}
