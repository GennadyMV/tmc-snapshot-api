package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.model.Course;
import fi.helsinki.cs.tmc.snapshot.api.model.Exercise;
import fi.helsinki.cs.tmc.snapshot.api.model.Snapshot;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ ParticipantSnapshotService.class })
public final class SpywareSnapshotServiceTest {

    @Mock
    private SpywareService spywareService;

    @Mock
    private ParticipantService participantService;

    @InjectMocks
    private ParticipantService injectedParticipantService;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldFindAllSnapshots() throws Exception {

        final File indexFile = new File("test-data/test.idx");
        final FileInputStream indexInputStream = new FileInputStream(indexFile);

        final File dataFile = new File("test-data/test.dat");

        final byte[] bytes = FileUtils.readFileToByteArray(dataFile);

        when(spywareService.fetchIndexByInstanceAndId("hy", "karpo")).thenReturn(indexInputStream);
        when(spywareService.fetchData(any(String.class), any(String.class), any(String.class)))
                            .thenReturn(Arrays.copyOfRange(bytes, 0, 11683))
                            .thenReturn(Arrays.copyOfRange(bytes, 11683, 12945))
                            .thenReturn(Arrays.copyOfRange(bytes, 12945, 14557));

        when(patchService.patch(any(List.class))).thenCallRealMethod();

        final List<Snapshot> snapshots = injectedParticipantService.findAll("hy", "karpo");

        assertNotNull(snapshots);

        // 64 total, 44 of which are folder_create, file_change or file_create -> 20
        assertEquals(20, snapshots.size());
    }

    @Test(expected = IOException.class)
    public void closesInputStream() throws IOException {

        final FileInputStream indexInputStream = new FileInputStream(new File("test-data/test.idx"));

        when(spywareService.fetchIndexByInstanceAndId("mooc", "coom")).thenReturn(indexInputStream);
        injectedParticipantService.findAll("mooc", "coom");

        indexInputStream.available();
    }

    @Test
    public void shouldNotFailForCorruptedJsonData() throws Exception {

        final File indexFile = new File("test-data/error.idx");
        final FileInputStream indexInputStream = new FileInputStream(indexFile);

        final File dataFile = new File("test-data/error.dat");

        final byte[] bytes = FileUtils.readFileToByteArray(dataFile);

        when(spywareService.fetchIndexByInstanceAndId("hy", "karpo")).thenReturn(indexInputStream);
        when(spywareService.fetchData(any(String.class), any(String.class), any(String.class)))
                            .thenReturn(Arrays.copyOfRange(bytes, 0, 11741));

        when(patchService.patch(any(List.class))).thenCallRealMethod();

        final List<Snapshot> snapshots = injectedParticipantService.findAll("hy", "karpo");

        assertNotNull(snapshots);
        assertEquals(0, snapshots.size());
    }

    @Test
    public void shouldNotFailForCorruptedData() throws Exception {

        final File indexFile = new File("test-data/content-error.idx");
        final FileInputStream indexInputStream = new FileInputStream(indexFile);

        final File dataFile = new File("test-data/content-error.dat");

        final byte[] bytes = FileUtils.readFileToByteArray(dataFile);

        when(spywareService.fetchIndexByInstanceAndId("mooc", "pekka")).thenReturn(indexInputStream);
        when(spywareService.fetchData(any(String.class), any(String.class), any(String.class)))
                            .thenReturn(Arrays.copyOfRange(bytes, 0, 710));

        when(patchService.patch(any(List.class))).thenCallRealMethod();

        final List<Snapshot> snapshots = injectedParticipantService.findAll("mooc", "pekka");

        assertNotNull(snapshots);
        assertEquals(2, snapshots.size());
    }

    @Test
    public void shouldNotFailForCorruptedPatchData() throws Exception {

        final File indexFile = new File("test-data/patch-data.idx");
        final FileInputStream indexInputStream = new FileInputStream(indexFile);

        final File dataFile = new File("test-data/patch-data.dat");

        final byte[] bytes = FileUtils.readFileToByteArray(dataFile);

        when(spywareService.fetchIndexByInstanceAndId("peliohjelmointi", "pekka")).thenReturn(indexInputStream);
        when(spywareService.fetchData(any(String.class), any(String.class), any(String.class)))
                            .thenReturn(Arrays.copyOfRange(bytes, 0, 710));

        when(patchService.patch(any(List.class))).thenCallRealMethod();

        final List<Snapshot> snapshots = injectedParticipantService.findAll("peliohjelmointi", "pekka");

        assertNotNull(snapshots);
        assertEquals(5, snapshots.size());
    }

    @Test
    public void shouldFindSnapshotWithCorrectId() throws IOException {

        final List<Snapshot> snapshots = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            snapshots.add(new Snapshot((long) i,
                                       new Course(1L, "course"),
                                       new Exercise(1L, "exercise"),
                                       new ArrayList<SnapshotFile>()));
        }

        when(participantService.findAll("test", "jack")).thenReturn(snapshots);
        when(participantService.find("test", "jack", 2L)).thenCallRealMethod();

        final Snapshot snapshot = participantService.find("test", "jack", 2L);

        assertNotNull(snapshot);
        assertEquals(2, (long) snapshot.getId());
    }

    @Test
    public void shouldReturnNullOnNonExistantId() throws IOException {

        final List<Snapshot> snapshots = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            snapshots.add(new Snapshot((long) i,
                                       new Course(1L, "course"),
                                       new Exercise(1L, "exercise"),
                                       new ArrayList<SnapshotFile>()));
        }

        when(participantService.findAll("data", "user")).thenReturn(snapshots);
        when(participantService.find("data", "user", 404L)).thenCallRealMethod();

        final Snapshot snapshot = participantService.find("data", "user", 404L);

        assertNull(snapshot);
    }
}
