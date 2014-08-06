package fi.helsinki.cs.tmc.snapshot.api.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class SnapshotTest {

    @Test
    public void constructorSetsValuesCorrectly() {

        final List<SnapshotFile> files = new ArrayList<>();

        final SnapshotFile file = new SnapshotFile("path", "content");
        files.add(file);

        final Snapshot snapshot = new Snapshot(13L, files);

        assertEquals(Long.valueOf(13L), snapshot.getId());
        assertEquals(file, snapshot.getFiles().iterator().next());
        assertEquals(new Date(13L), snapshot.getTimestamp());
    }

    @Test
    public void constructorSetsValuesCorrectly2() {

        final Map<String, SnapshotFile> files = new HashMap<>();

        final SnapshotFile file = new SnapshotFile("path", "content");
        files.put(file.getPath(), file);

        final Snapshot snapshot = new Snapshot(13L, files);

        assertEquals(Long.valueOf(13L), snapshot.getId());
        assertEquals(file, snapshot.getFiles().iterator().next());
        assertEquals(new Date(13L), snapshot.getTimestamp());
    }

    @Test
    public void shouldAddFileToSnapshot() {

        final Snapshot snapshot = new Snapshot(12L, new ArrayList<SnapshotFile>());

        snapshot.addFile(new SnapshotFile("example", "test"));

        final SnapshotFile file = snapshot.getFiles().iterator().next();

        assertFalse(snapshot.getFiles().isEmpty());
        assertEquals("example", file.getPath());
        assertEquals("test", file.getContent());
    }

    @Test
    public void isFromCompleteSnapshotReturnCorrectStatus() {

        final Snapshot s1 = new Snapshot(1L, new HashMap<String, SnapshotFile>(), false);
        final Snapshot s2 = new Snapshot(1L, new HashMap<String, SnapshotFile>(), true);

        assertFalse(s1.isFromCompleteSnapshot());
        assertTrue(s2.isFromCompleteSnapshot());
    }

    @Test
    public void compareToOrdersChronologically() {

        final Snapshot s1 = new Snapshot(1L, new HashMap<String, SnapshotFile>(), false);
        final Snapshot s2 = new Snapshot(2L, new HashMap<String, SnapshotFile>(), true);

        assertTrue(s1.compareTo(s2) < 0);
        assertTrue(s2.compareTo(s1) > 0);
    }

    @Test
    public void compareToReturnsZeroForSnapshotsWithSameTimestamp() {

        final Snapshot s1 = new Snapshot(1L, new HashMap<String, SnapshotFile>(), false);
        final Snapshot s2 = new Snapshot(1L, new HashMap<String, SnapshotFile>(), true);

        assertTrue(s1.compareTo(s2) == 0);
        assertTrue(s2.compareTo(s1) == 0);
    }
}
