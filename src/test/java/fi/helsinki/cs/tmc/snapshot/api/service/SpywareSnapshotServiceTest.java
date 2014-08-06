package fi.helsinki.cs.tmc.snapshot.api.service;

import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ DefaultSpywareService.class, DefaultSnapshotEventService.class })
public final class SpywareSnapshotServiceTest {

//    @Mock
//    private DefaultSpywareService spywareService;
//
//    @Mock
//    private EventReader eventReader;
//
//    @InjectMocks
//    private DefaultSnapshotEventService injectedSnapshotEventService;
//
//    @Before
//    public void setUp() {
//
//        MockitoAnnotations.initMocks(this);
//    }
//
//    @Test
//    public void shouldFindAllSnapshots() throws Exception {
//
//        final File indexFile = new File("test-data/test.idx");
//        final String indexFileContent = FileUtils.readFileToString(indexFile);
//
//        final File dataFile = new File("test-data/test.dat");
//
//        final byte[] bytes = FileUtils.readFileToByteArray(dataFile);
//
//        when(spywareService.fetchIndex("hy", "karpo")).thenReturn(indexFileContent);
//        when(spywareService.fetchChunkByRange(any(String.class), any(String.class), any(Integer.class), any(Integer.class)))
//                            .thenReturn(Arrays.copyOfRange(bytes, 0, 11683))
//                            .thenReturn(Arrays.copyOfRange(bytes, 11683, 12945))
//                            .thenReturn(Arrays.copyOfRange(bytes, 12945, 14557));
//
//        final Collection<SnapshotEvent> participant = injectedSnapshotEventService.findAll("hy", "karpo");
//
//        assertNotNull(participant);
//    }

//    @Test(expected = IOException.class)
//    public void closesInputStream() throws IOException {
//
//        final FileInputStream indexInputStream = new FileInputStream(new File("test-data/test.idx"));
//
//        when(spywareService.fetchIndex("mooc", "coom")).thenReturn(indexInputStream);
//        injectedSpywareSnapshotService.findAll("mooc", "coom");
//
//        indexInputStream.available();
//    }
//
//    @Test
//    public void shouldNotFailForCorruptedJsonData() throws Exception {
//
//        final File indexFile = new File("test-data/error.idx");
//        final FileInputStream indexInputStream = new FileInputStream(indexFile);
//
//        final File dataFile = new File("test-data/error.dat");
//
//        final byte[] bytes = FileUtils.readFileToByteArray(dataFile);
//
//        when(spywareService.fetchIndex("hy", "karpo")).thenReturn(indexInputStream);
//        when(spywareService.fetchData(any(String.class), any(String.class), any(String.class)))
//                            .thenReturn(Arrays.copyOfRange(bytes, 0, 11741));
//
//        when(patchService.patch(any(List.class))).thenCallRealMethod();
//
//        final List<Snapshot> snapshots = injectedParticipantService.findAll("hy", "karpo");
//
//        assertNotNull(snapshots);
//        assertEquals(0, snapshots.size());
//    }
//
//    @Test
//    public void shouldNotFailForCorruptedData() throws Exception {
//
//        final File indexFile = new File("test-data/content-error.idx");
//        final FileInputStream indexInputStream = new FileInputStream(indexFile);
//
//        final File dataFile = new File("test-data/content-error.dat");
//
//        final byte[] bytes = FileUtils.readFileToByteArray(dataFile);
//
//        when(spywareService.fetchIndex("mooc", "pekka")).thenReturn(indexInputStream);
//        when(spywareService.fetchData(any(String.class), any(String.class), any(String.class)))
//                            .thenReturn(Arrays.copyOfRange(bytes, 0, 710));
//
//        when(patchService.patch(any(List.class))).thenCallRealMethod();
//
//        final List<Snapshot> snapshots = injectedParticipantService.findAll("mooc", "pekka");
//
//        assertNotNull(snapshots);
//        assertEquals(2, snapshots.size());
//    }
//
//    @Test
//    public void shouldNotFailForCorruptedPatchData() throws Exception {
//
//        final File indexFile = new File("test-data/patch-data.idx");
//        final FileInputStream indexInputStream = new FileInputStream(indexFile);
//
//        final File dataFile = new File("test-data/patch-data.dat");
//
//        final byte[] bytes = FileUtils.readFileToByteArray(dataFile);
//
//        when(spywareService.fetchIndex("peliohjelmointi", "pekka")).thenReturn(indexInputStream);
//        when(spywareService.fetchData(any(String.class), any(String.class), any(String.class)))
//                            .thenReturn(Arrays.copyOfRange(bytes, 0, 710));
//
//        when(patchService.patch(any(List.class))).thenCallRealMethod();
//
//        final List<Snapshot> snapshots = injectedParticipantService.findAll("peliohjelmointi", "pekka");
//
//        assertNotNull(snapshots);
//        assertEquals(5, snapshots.size());
//    }
//
//    @Test
//    public void shouldFindSnapshotWithCorrectId() throws IOException {
//
//        final List<Snapshot> snapshots = new ArrayList<>();
//
//        for (int i = 0; i < 3; i++) {
//            snapshots.add(new Snapshot((long) i,
//                                       new Course(1L, "course"),
//                                       new Exercise(1L, "exercise"),
//                                       new ArrayList<SnapshotFile>()));
//        }
//
//        when(participantService.findAll("test", "jack")).thenReturn(snapshots);
//        when(participantService.find("test", "jack", 2L)).thenCallRealMethod();
//
//        final Snapshot snapshot = participantService.find("test", "jack", 2L);
//
//        assertNotNull(snapshot);
//        assertEquals(2, (long) snapshot.getId());
//    }
//
//    @Test
//    public void shouldReturnNullOnNonExistantId() throws IOException {
//
//        final List<Snapshot> snapshots = new ArrayList<>();
//
//        for (int i = 0; i < 3; i++) {
//            snapshots.add(new Snapshot((long) i,
//                                       new Course(1L, "course"),
//                                       new Exercise(1L, "exercise"),
//                                       new ArrayList<SnapshotFile>()));
//        }
//
//        when(participantService.findAll("data", "user")).thenReturn(snapshots);
//        when(participantService.find("data", "user", 404L)).thenCallRealMethod();
//
//        final Snapshot snapshot = participantService.find("data", "user", 404L);
//
//        assertNull(snapshot);
//    }
}
