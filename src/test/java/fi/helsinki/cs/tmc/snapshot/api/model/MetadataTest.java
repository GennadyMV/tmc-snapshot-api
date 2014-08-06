package fi.helsinki.cs.tmc.snapshot.api.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class MetadataTest {

    @Test
    public void constructorSetsValuesCorrectly() {

        final SnapshotEventMetadata metadata = new SnapshotEventMetadata("myCause", "myFile");

        assertEquals("myCause", metadata.getCause());
        assertEquals("myFile", metadata.getFile());
    }
}
