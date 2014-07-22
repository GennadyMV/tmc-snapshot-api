package fi.helsinki.cs.tmc.snapshot.api.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class SnapshotEventInformationTest {

    private SnapshotEventInformation info;

    @Before
    public void setUp() {

        info = new SnapshotEventInformation();
    }

    @Test
    public void canSetFile() {

        info.setFile("myFile");

        assertEquals("myFile", info.getFile());
    }

    @Test
    public void canSetPatches() {

        info.setPatches("myPatches");

        assertEquals("myPatches", info.getPatches());
    }

    @Test
    public void canSetAsFullDocument() {

        info.setFullDocument(true);

        assertTrue(info.isFullDocument());
    }

}
