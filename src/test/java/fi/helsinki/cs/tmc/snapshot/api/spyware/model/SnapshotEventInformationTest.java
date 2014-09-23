package fi.helsinki.cs.tmc.snapshot.api.spyware.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class SnapshotEventInformationTest {

    private SnapshotEventInformation eventInformation;

    @Before
    public void setUp() {

        eventInformation = new SnapshotEventInformation();
    }

    @Test
    public void canSetFile() {

        eventInformation.setFile("myFile");

        assertEquals("myFile", eventInformation.getFile());
    }

    @Test
    public void canSetPatches() {

        eventInformation.setPatches("myPatches");

        assertEquals("myPatches", eventInformation.getPatches());
    }

    @Test
    public void canSetAsFullDocument() {

        eventInformation.setFullDocument(true);

        assertTrue(eventInformation.isFullDocument());
    }
}
