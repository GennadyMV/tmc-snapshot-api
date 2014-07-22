package fi.helsinki.cs.tmc.snapshot.api.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class SnapshotFileTest {

    @Test
    public void constructorSetsValues() {

        final SnapshotFile file = new SnapshotFile("myPath", "myContent");

        assertEquals("myPath", file.getPath());
        assertEquals("myContent", file.getContent());
    }

}
