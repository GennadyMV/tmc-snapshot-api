package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.model.Snapshot;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


@RunWith(PowerMockRunner.class)
@PrepareForTest(SpywareSnapshotService.class)
public class SpywareSnapshotServiceTest {

    @Mock
    private SpywareSnapshotService spywareSnapshotService;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldFindSnapshotWithCorrectId() throws IOException {

        final List<Snapshot> snapshots = new ArrayList<>();

        for (int i = 0; i < 3; i++) {

            snapshots.add(new Snapshot((long) i, new ArrayList<SnapshotFile>()));
        }

        when(spywareSnapshotService.findAll("test", "jack")).thenReturn(snapshots);
        when(spywareSnapshotService.find("test", "jack", 2L)).thenCallRealMethod();

        final Snapshot snapshot = spywareSnapshotService.find("test", "jack", 2L);

        assertNotNull(snapshot);
        assertEquals(2, (long) snapshot.getId());
    }

    @Test
    public void shouldReturnNullOnNonExistantId() throws IOException {

        final List<Snapshot> snapshots = new ArrayList<>();

        for (int i = 0; i < 3; i++) {

            snapshots.add(new Snapshot((long) i, new ArrayList<SnapshotFile>()));
        }

        when(spywareSnapshotService.findAll("data", "user")).thenReturn(snapshots);
        when(spywareSnapshotService.find("data", "user", 404L)).thenCallRealMethod();

        final Snapshot snapshot = spywareSnapshotService.find("data", "user", 404L);

        assertNull(snapshot);
    }

}
