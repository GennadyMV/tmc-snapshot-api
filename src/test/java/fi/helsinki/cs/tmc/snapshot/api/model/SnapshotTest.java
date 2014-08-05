package fi.helsinki.cs.tmc.snapshot.api.model;

import java.util.ArrayList;
import java.util.Collections;
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

        final Snapshot snapshot = new Snapshot(13L, new Course(1L, "course"), new Exercise(1L, "exercise"), files);

        assertEquals(Long.valueOf(13L), snapshot.getId());
        assertEquals(file, snapshot.getFiles().iterator().next());
        assertEquals(new Date(13L), snapshot.getTimestamp());
        assertEquals("course", snapshot.getCourse().getName());
        assertEquals("exercise", snapshot.getExercise().getName());
    }

    @Test
    public void constructorSetsValuesCorrectly2() {

        final Map<String, SnapshotFile> files = new HashMap<>();

        final SnapshotFile file = new SnapshotFile("path", "content");
        files.put(file.getPath(), file);

        final Snapshot snapshot = new Snapshot(13L, new Course(1L, "course"), new Exercise(1L, "exercise"), files);

        assertEquals(Long.valueOf(13L), snapshot.getId());
        assertEquals(file, snapshot.getFiles().iterator().next());
        assertEquals(new Date(13L), snapshot.getTimestamp());
        assertEquals("course", snapshot.getCourse().getName());
        assertEquals("exercise", snapshot.getExercise().getName());
    }

    @Test
    public void shouldAddFileToSnapshot() {

        final Snapshot snapshot = new Snapshot(12L,
                                               new Course(1L, "test"),
                                               new Exercise(1L, "name"),
                                               new ArrayList<SnapshotFile>());

        snapshot.addFile(new SnapshotFile("example", "test"));

        final SnapshotFile file = snapshot.getFiles().iterator().next();

        assertFalse(snapshot.getFiles().isEmpty());
        assertEquals("example", file.getPath());
        assertEquals("test", file.getContent());
    }

    @Test
    public void shouldCompareSnapshotsCorrectly() {

        final List<Snapshot> snapshots = new ArrayList<>();

        snapshots.add(new Snapshot(200L,
                                   new Course(1L, "after"),
                                   new Exercise(1L, "snapshot"),
                                   new ArrayList<SnapshotFile>()));
        snapshots.add(new Snapshot(100L,
                                   new Course(1L, "before"),
                                   new Exercise(1L, "snapshot"),
                                   new ArrayList<SnapshotFile>()));

        assertEquals("after", snapshots.get(0).getCourse().getName());
        assertEquals("before", snapshots.get(1).getCourse().getName());

        Collections.sort(snapshots);

        assertEquals("before", snapshots.get(0).getCourse().getName());
        assertEquals("after", snapshots.get(1).getCourse().getName());
    }

    @Test
    public void isFromCompleteSnapshotReturnCorrectStatus() {

        final Snapshot s1 = new Snapshot(1L,
                                         new Course(1L, "11"),
                                         new Exercise(1L, "11"),
                                         new HashMap<String, SnapshotFile>(),
                                        false);
        final Snapshot s2 = new Snapshot(1L,
                                         new Course(1L, "22"),
                                         new Exercise(1L, "22"),
                                         new HashMap<String, SnapshotFile>(),
                                         true);

        assertFalse(s1.isFromCompleteSnapshot());
        assertTrue(s2.isFromCompleteSnapshot());
    }
}
