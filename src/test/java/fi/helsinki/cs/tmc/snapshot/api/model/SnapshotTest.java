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

public final class SnapshotTest {

    @Test
    public void constructorSetsValuesCorrectly() {

        final List<SnapshotFile> files = new ArrayList<>();

        final SnapshotFile file = new SnapshotFile("path", "content");
        files.add(file);

        final Snapshot snapshot = new Snapshot(13L, "course", "exercise", files);

        assertEquals(Long.valueOf(13L), snapshot.getId());
        assertEquals(file, snapshot.getFiles().iterator().next());
        assertEquals(new Date(13L), snapshot.getTimestamp());
        assertEquals("course", snapshot.getCourse());
        assertEquals("exercise", snapshot.getExercise());
    }

    @Test
    public void constructorSetsValuesCorrectly2() {

        final Map<String, SnapshotFile> files = new HashMap<>();

        final SnapshotFile file = new SnapshotFile("path", "content");
        files.put(file.getPath(), file);

        final Snapshot snapshot = new Snapshot(13L, "course", "exercise", files);

        assertEquals(Long.valueOf(13L), snapshot.getId());
        assertEquals(file, snapshot.getFiles().iterator().next());
        assertEquals(new Date(13L), snapshot.getTimestamp());
        assertEquals("course", snapshot.getCourse());
        assertEquals("exercise", snapshot.getExercise());
    }

    @Test
    public void shouldAddFileToSnapshot() {

        final Snapshot snapshot = new Snapshot(12L, "test", "name", new ArrayList<SnapshotFile>());

        snapshot.addFile(new SnapshotFile("example", "test"));

        final SnapshotFile file = snapshot.getFiles().iterator().next();

        assertFalse(snapshot.getFiles().isEmpty());
        assertEquals("example", file.getPath());
        assertEquals("test", file.getContent());
    }

    @Test
    public void shouldCompareSnapshotsCorrectly() {

        final List<Snapshot> snapshots = new ArrayList<>();

        snapshots.add(new Snapshot(200L, "after", "snapshot", new ArrayList<SnapshotFile>()));
        snapshots.add(new Snapshot(100L, "before", "snapshot", new ArrayList<SnapshotFile>()));

        assertEquals("after", snapshots.get(0).getCourse());
        assertEquals("before", snapshots.get(1).getCourse());

        Collections.sort(snapshots);

        assertEquals("before", snapshots.get(0).getCourse());
        assertEquals("after", snapshots.get(1).getCourse());
    }
}
