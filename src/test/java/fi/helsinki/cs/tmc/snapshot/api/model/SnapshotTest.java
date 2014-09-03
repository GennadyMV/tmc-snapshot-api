package fi.helsinki.cs.tmc.snapshot.api.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class SnapshotTest {

    @Test
    public void constructorSetsValuesCorrectly() {

        final Map<String, SnapshotFile> files = new HashMap<>();

        final SnapshotFile file = new SnapshotFile("id", "path", "content");
        files.put(file.getId(), file);

        final Snapshot snapshot = new Snapshot("13", 13L, files, false);

        assertEquals("13", snapshot.getId());
        assertEquals(file, snapshot.getFiles().iterator().next());
        assertEquals(new Date(13L), snapshot.getTimestamp());
    }

    @Test
    public void shouldAddFileToSnapshot() {

        final Snapshot snapshot = new Snapshot("12", 12L, new HashMap<String, SnapshotFile>(), false);
        snapshot.addFile(new SnapshotFile("id", "example", "test"));

        final SnapshotFile file = snapshot.getFiles().iterator().next();

        assertFalse(snapshot.getFiles().isEmpty());
        assertEquals("example", file.getPath());
        assertEquals("test", file.getContent());
    }

    @Test
    public void isFromCompleteSnapshotReturnCorrectStatus() {

        final Snapshot s1 = new Snapshot("1", 1L, new HashMap<String, SnapshotFile>(), false);
        final Snapshot s2 = new Snapshot("1", 1L, new HashMap<String, SnapshotFile>(), true);

        assertFalse(s1.isFromCompleteSnapshot());
        assertTrue(s2.isFromCompleteSnapshot());
    }
}
