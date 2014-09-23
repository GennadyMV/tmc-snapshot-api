package fi.helsinki.cs.tmc.snapshot.api.spyware.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class SnapshotEventMetadataTest {

    @Test
    public void constructorSetsValuesCorrectly() {

        final SnapshotEventMetadata metadata = new SnapshotEventMetadata("myCause", "myFile");

        assertEquals("myCause", metadata.getCause());
        assertEquals("myFile", metadata.getFile());
    }
}
