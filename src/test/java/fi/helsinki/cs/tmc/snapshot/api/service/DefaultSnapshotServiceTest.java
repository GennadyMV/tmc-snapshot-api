package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.exception.NotFoundException;
import fi.helsinki.cs.tmc.snapshot.api.model.Exercise;
import fi.helsinki.cs.tmc.snapshot.api.model.Snapshot;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotFile;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotLevel;
import fi.helsinki.cs.tmc.snapshot.api.spyware.model.SnapshotEvent;
import fi.helsinki.cs.tmc.snapshot.api.util.EventTransformer;
import fi.helsinki.cs.tmc.snapshot.api.util.KeyLevelEventProcessor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ KeyLevelEventProcessor.class, EventTransformer.class })
public final class DefaultSnapshotServiceTest {

    private static final String INSTANCE = "testInstance";
    private static final String USERNAME = "testUsername";
    private static final String COURSE = "testCourse";
    private static final String EXERCISE = "testExercise";

    @Mock
    private ExerciseService exerciseService;

    @Mock
    private EventProcessorService eventProcessorService;

    @Mock
    private EventTransformer eventTransformer;

    @InjectMocks
    private DefaultSnapshotService snapshotService;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);
    }

    private List<Snapshot> generateSnapshots(final boolean codeLevel, final String fileContent, final int[] ids) {

        final List<Snapshot> snapshots = new ArrayList<>();

        for (int id : ids) {
            final String idString = Integer.toString(id);
            final Snapshot snapshot = new Snapshot(idString, 0L, new HashMap<String, SnapshotFile>(), codeLevel);
            snapshot.addFile(new SnapshotFile(idString, idString, fileContent));
            snapshots.add(snapshot);
        }

        return snapshots;
    }

    private List<Snapshot> generateSnapshots(final int... ids) {

        return generateSnapshots(false, "key", ids);
    }

    private List<Snapshot> generateCodeSnapshots(final int... ids) {

        return generateSnapshots(true, "code", ids);
    }

    @Test
    public void findAllReturnsProcessedSnapshots() throws IOException {

        final Exercise exercise = new Exercise(EXERCISE);

        exercise.addSnapshotEvent(new SnapshotEvent());
        exercise.addSnapshotEvent(new SnapshotEvent());

        final Collection<SnapshotEvent> events = exercise.getSnapshotEvents();

        final List<Snapshot> snapshots = new ArrayList<>();

        snapshots.add(new Snapshot("1", 1L, new HashMap<String, SnapshotFile>(), false));
        snapshots.add(new Snapshot("2", 2L, new HashMap<String, SnapshotFile>(), false));
        snapshots.add(new Snapshot("3", 3L, new HashMap<String, SnapshotFile>(), false));

        when(exerciseService.find(INSTANCE, USERNAME, COURSE, EXERCISE)).thenReturn(exercise);
        when(eventTransformer.toSnapshotList(events)).thenReturn(snapshots);

        final List<Snapshot> returnedSnapshots = snapshotService.findAll(INSTANCE, USERNAME, COURSE, EXERCISE, SnapshotLevel.KEY);

        verify(exerciseService).find(INSTANCE, USERNAME, COURSE, EXERCISE);
        verify(eventTransformer).toSnapshotList(events);
        verify(eventProcessorService).processEvents(events, SnapshotLevel.KEY);

        assertEquals(returnedSnapshots, snapshots);
    }

    @Test
    public void findAllReturnsEmptyListWhenNullBuiltSnapshots() throws IOException {

        final Exercise exercise = new Exercise(EXERCISE);

        when(exerciseService.find(INSTANCE, USERNAME, COURSE, EXERCISE)).thenReturn(exercise);
        when(eventTransformer.toSnapshotList(exercise.getSnapshotEvents())).thenReturn(null);

        final List<Snapshot> result = snapshotService.findAll(INSTANCE, USERNAME, COURSE, EXERCISE, SnapshotLevel.KEY);

        assertEquals(0, result.size());
    }

    @Test
    public void findReturnsCorrectSnapshot() throws IOException {

        final List<Snapshot> snapshots = new ArrayList<>();

        final Snapshot snapshot = new Snapshot("0", 0L, new HashMap<String, SnapshotFile>(), false);
        snapshots.add(snapshot);

        snapshots.add(new Snapshot("1", 1L, new HashMap<String, SnapshotFile>(), false));
        snapshots.add(new Snapshot("2", 2L, new HashMap<String, SnapshotFile>(), false));
        snapshots.add(new Snapshot("3", 3L, new HashMap<String, SnapshotFile>(), false));

        when(exerciseService.find(INSTANCE, USERNAME, COURSE, EXERCISE)).thenReturn(new Exercise(EXERCISE));
        when(eventTransformer.toSnapshotList(any(List.class))).thenReturn(snapshots);

        final Snapshot retrieved = snapshotService.find(INSTANCE, USERNAME, COURSE, EXERCISE, "0", SnapshotLevel.KEY);
        assertEquals(snapshot, retrieved);
    }

    @Test(expected = NotFoundException.class)
    public void findThrowsNotFoundExceptionIfNoSnapshotIsFound() throws IOException {

        final List<Snapshot> snapshots = new ArrayList<>();

        snapshots.add(new Snapshot("1", 1L, new HashMap<String, SnapshotFile>(), false));
        snapshots.add(new Snapshot("2", 2L, new HashMap<String, SnapshotFile>(), false));
        snapshots.add(new Snapshot("3", 3L, new HashMap<String, SnapshotFile>(), false));

        when(exerciseService.find(INSTANCE, USERNAME, COURSE, EXERCISE)).thenReturn(new Exercise(EXERCISE));
        when(eventTransformer.toSnapshotList(any(List.class))).thenReturn(snapshots);

        snapshotService.find(INSTANCE, USERNAME, COURSE, EXERCISE, "0", SnapshotLevel.KEY);
    }

    @Test
    public void findFilesAsZipReturnsCorrectBytes() throws IOException {

        final Exercise exercise = new Exercise(EXERCISE);

        exercise.addSnapshotEvent(new SnapshotEvent());
        exercise.addSnapshotEvent(new SnapshotEvent());

        final Collection<SnapshotEvent> events = exercise.getSnapshotEvents();

        final List<Snapshot> snapshots = generateSnapshots(1, 2, 3);

        when(exerciseService.find(INSTANCE, USERNAME, COURSE, EXERCISE)).thenReturn(exercise);
        when(eventTransformer.toSnapshotList(events)).thenReturn(snapshots);

        final byte[] bytes = snapshotService.findFilesAsZip(INSTANCE, USERNAME, COURSE, EXERCISE, SnapshotLevel.KEY, "", 0);

        assertEquals(625, bytes.length);
    }

    @Test
    public void findFilesAsZipReturnsCorrectBytesWhenCountSet() throws IOException {

        final Exercise exercise = new Exercise(EXERCISE);

        exercise.addSnapshotEvent(new SnapshotEvent());
        exercise.addSnapshotEvent(new SnapshotEvent());

        final Collection<SnapshotEvent> events = exercise.getSnapshotEvents();

        final List<Snapshot> snapshots = generateSnapshots(2, 3, 4, 5, 6, 7);

        when(exerciseService.find(INSTANCE, USERNAME, COURSE, EXERCISE)).thenReturn(exercise);
        when(eventTransformer.toSnapshotList(events)).thenReturn(snapshots);

        final byte[] bytes = snapshotService.findFilesAsZip(INSTANCE, USERNAME, COURSE, EXERCISE, SnapshotLevel.KEY, "", 2);

        assertEquals(424, bytes.length);
    }

    @Test
    public void findFilesAsZipReturnsCorrectBytesWhenFromIdSetAndCount() throws IOException {

        final Exercise exercise = new Exercise(EXERCISE);

        exercise.addSnapshotEvent(new SnapshotEvent());
        exercise.addSnapshotEvent(new SnapshotEvent());

        final Collection<SnapshotEvent> events = exercise.getSnapshotEvents();

        final List<Snapshot> snapshots = generateSnapshots(2, 3, 4, 5, 6, 7);

        when(exerciseService.find(INSTANCE, USERNAME, COURSE, EXERCISE)).thenReturn(exercise);
        when(eventTransformer.toSnapshotList(events)).thenReturn(snapshots);

        final byte[] bytes = snapshotService.findFilesAsZip(INSTANCE, USERNAME, COURSE, EXERCISE, SnapshotLevel.KEY, "3", 3);

        assertEquals(625, bytes.length);
    }
}
