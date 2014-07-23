package fi.helsinki.cs.tmc.snapshot.api.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class SnapshotTest {

    @Test
    public void constructorSetsValuesCorrectly() {

        final List<SnapshotFile> files = new ArrayList<>();
        final Snapshot snapshot = new Snapshot(13L, files);

        assertEquals(Long.valueOf(13L), snapshot.getId());
        assertEquals(files, snapshot.getFiles());
        assertEquals(new Date(13L), snapshot.getTimestamp());
    }
}
