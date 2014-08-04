package fi.helsinki.cs.tmc.snapshot.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.DiffMatchPatch;

import fi.helsinki.cs.tmc.snapshot.api.model.Snapshot;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ SnapshotDiffPatcher.class, SpywareSnapshotService.class })
public final class SpywareSnapshotServiceTest {

    @Mock
    private SpywareService spywareService;

    @Mock
    private SnapshotDiffPatcher patchService;

    @Mock
    private SpywareSnapshotService spywareSnapshotService;

    @InjectMocks
    private SpywareSnapshotService injectedSpywareSnapshotService;

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

        Whitebox.setInternalState(patchService, new DiffMatchPatch());
        Whitebox.setInternalState(patchService, new ObjectMapper());
        Whitebox.setInternalState(patchService, new TreeMap<String, String>());

        when(spywareService.fetchIndex("hy", "karpo")).thenReturn(indexInputStream);
        when(spywareService.fetchData(any(String.class), any(String.class), any(String.class)))
                            .thenReturn(Arrays.copyOfRange(bytes, 0, 11683))
                            .thenReturn(Arrays.copyOfRange(bytes, 11683, 12945))
                            .thenReturn(Arrays.copyOfRange(bytes, 12945, 14557));

        when(patchService.patch(any(List.class))).thenCallRealMethod();

        final List<Snapshot> snapshots = injectedSpywareSnapshotService.findAll("hy", "karpo");

        assertNotNull(snapshots);

        //64 total, 44 of which are folder_create, file_change or file_create -> 20
        assertEquals(20, snapshots.size());
    }

    @Test(expected = IOException.class)
    public void closesInputStream() throws IOException {

        final FileInputStream indexInputStream = new FileInputStream(new File("test-data/test.idx"));

        when(spywareService.fetchIndex("mooc", "coom")).thenReturn(indexInputStream);
        injectedSpywareSnapshotService.findAll("mooc", "coom");

        indexInputStream.available();
    }

    @Test
    public void shouldNotFailForCorruptedJsonData() throws Exception {

        final File indexFile = new File("test-data/error.idx");
        final FileInputStream indexInputStream = new FileInputStream(indexFile);

        final File dataFile = new File("test-data/error.dat");

        final byte[] bytes = FileUtils.readFileToByteArray(dataFile);

        Whitebox.setInternalState(patchService, new DiffMatchPatch());
        Whitebox.setInternalState(patchService, new ObjectMapper());
        Whitebox.setInternalState(patchService, new TreeMap<String, String>());

        when(spywareService.fetchIndex("hy", "karpo")).thenReturn(indexInputStream);
        when(spywareService.fetchData(any(String.class), any(String.class), any(String.class)))
                            .thenReturn(Arrays.copyOfRange(bytes, 0, 11741));

        when(patchService.patch(any(List.class))).thenCallRealMethod();

        final List<Snapshot> snapshots = injectedSpywareSnapshotService.findAll("hy", "karpo");

        assertNotNull(snapshots);
        assertEquals(0, snapshots.size());
    }

    @Test
    public void shouldNotFailForCorruptedData() throws Exception {

        final File indexFile = new File("test-data/content-error.idx");
        final FileInputStream indexInputStream = new FileInputStream(indexFile);

        final File dataFile = new File("test-data/content-error.dat");

        final byte[] bytes = FileUtils.readFileToByteArray(dataFile);

        Whitebox.setInternalState(patchService, new DiffMatchPatch());
        Whitebox.setInternalState(patchService, new ObjectMapper());
        Whitebox.setInternalState(patchService, new TreeMap<String, String>());

        when(spywareService.fetchIndex("mooc", "pekka")).thenReturn(indexInputStream);
        when(spywareService.fetchData(any(String.class), any(String.class), any(String.class)))
                            .thenReturn(Arrays.copyOfRange(bytes, 0, 704));

        when(patchService.patch(any(List.class))).thenCallRealMethod();

        final List<Snapshot> snapshots = injectedSpywareSnapshotService.findAll("mooc", "pekka");

        assertNotNull(snapshots);
        assertEquals(2, snapshots.size());
    }

    @Test
    public void shouldFindSnapshotWithCorrectId() throws IOException {

        final List<Snapshot> snapshots = new ArrayList<>();

        for (int i = 0; i < 3; i++) {

            snapshots.add(new Snapshot((long) i, "course", "exercise", new ArrayList<SnapshotFile>()));
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

            snapshots.add(new Snapshot((long) i, "course", "exercise", new ArrayList<SnapshotFile>()));
        }

        when(spywareSnapshotService.findAll("data", "user")).thenReturn(snapshots);
        when(spywareSnapshotService.find("data", "user", 404L)).thenCallRealMethod();

        final Snapshot snapshot = spywareSnapshotService.find("data", "user", 404L);

        assertNull(snapshot);
    }
}
