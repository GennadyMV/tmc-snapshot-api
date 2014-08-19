package fi.helsinki.cs.tmc.snapshot.api.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class SnapshotFileTest {

    @Test
    public void constructorSetsValues() {

        final SnapshotFile file = new SnapshotFile("src/myPath", "myContent");

        assertEquals("src/myPath", file.getPath());
        assertEquals("myContent", file.getContent());
        assertEquals("myPath", file.getName());
        assertEquals("bXlQYXRo", file.getId());
        assertEquals("src/myPath", file.toString());
    }
}
