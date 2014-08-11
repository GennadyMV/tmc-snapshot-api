package fi.helsinki.cs.tmc.snapshot.api.util;

import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotEvent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class EventReaderTest {

    private final EventReader eventReader = new EventReader();

    @Test
    public void shouldReadEvents() throws IOException {

        final File dataFile = new File("test-data/patch-data.dat");

        final byte[] bytes = FileUtils.readFileToByteArray(dataFile);

        final List<byte[]> data = new ArrayList<>();
        data.add(bytes);

        final Collection<SnapshotEvent> snapshotEvents = eventReader.readEvents(data);
        final List<SnapshotEvent> events = new ArrayList(snapshotEvents);

        assertEquals(5, events.size());

        for (SnapshotEvent snapshotEvent : events) {
            assertEquals("XXX-ohja-kertaus", snapshotEvent.getCourseName());
            assertEquals("setti1-01.TavaraMatkalaukkuJaLastiruuma", snapshotEvent.getExerciseName());
        }
    }

    @Test
    public void shouldNotReadInvalidEvents() throws IOException {

        // Contains 6 events, 2 of them are invalid
        final File dataFile = new File("test-data/patch-null-data.dat");

        final byte[] bytes = FileUtils.readFileToByteArray(dataFile);

        final List<byte[]> data = new ArrayList<>();
        data.add(bytes);

        final Collection<SnapshotEvent> snapshotEvents = eventReader.readEvents(data);
        final List<SnapshotEvent> events = new ArrayList(snapshotEvents);

        assertEquals(4, events.size());

        for (SnapshotEvent snapshotEvent : events) {
            assertNotEquals(0x1478673a498L, (long) snapshotEvent.getHappenedAt());
            assertNotEquals(1406710010431L, (long) snapshotEvent.getHappenedAt());
        }
    }
}
