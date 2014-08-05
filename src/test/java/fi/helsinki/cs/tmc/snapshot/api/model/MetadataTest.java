package fi.helsinki.cs.tmc.snapshot.api.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MetadataTest {

    @Test
    public void constructorSetsValuesCorrectly() {

        final Metadata metadata = new Metadata("myCause", "myFile");

        assertEquals("myCause", metadata.getCause());
        assertEquals("myFile", metadata.getFile());
    }
}
