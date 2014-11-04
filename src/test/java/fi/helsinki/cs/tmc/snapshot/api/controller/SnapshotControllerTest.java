package fi.helsinki.cs.tmc.snapshot.api.controller;

import fi.helsinki.cs.tmc.snapshot.api.app.App;
import fi.helsinki.cs.tmc.snapshot.api.exception.NotFoundException;
import fi.helsinki.cs.tmc.snapshot.api.model.Exercise;
import fi.helsinki.cs.tmc.snapshot.api.model.Snapshot;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotFile;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotLevel;
import fi.helsinki.cs.tmc.snapshot.api.service.ExerciseService;
import fi.helsinki.cs.tmc.snapshot.api.service.SnapshotService;
import fi.helsinki.cs.tmc.snapshot.api.spyware.model.SnapshotEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = App.class)
@WebAppConfiguration
@ActiveProfiles("test")
public final class SnapshotControllerTest {

    private static final String INSTANCE = "hy";
    private static final String USER = "testUsername";
    private static final String COURSE = "testCourseName";
    private static final String EXERCISE = "testExerciseName";
    private static final String SNAPSHOT_BASE_URL = "/hy/participants/testUsername/courses/testCourseName/exercises/testExerciseName/snapshots";

    @Mock
    private SnapshotService snapshotService;

    @Mock
    private ExerciseService exerciseService;

    @InjectMocks
    private SnapshotController snapshotController;

    private MockMvc mockMvc;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(snapshotController).build();
    }

    @Test
    public void shouldReturnSnapshots() throws Exception {

        final List<Snapshot> snapshotData = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            snapshotData.add(new Snapshot(Integer.toString(i),
                                          (long) i,
                                          new HashMap<String, SnapshotFile>(),
                                          false));
        }

        when(snapshotService.findAll(INSTANCE, USER, COURSE, EXERCISE, SnapshotLevel.KEY)).thenReturn(snapshotData);

        mockMvc.perform(get(SNAPSHOT_BASE_URL))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$", hasSize(5)));

        verify(snapshotService).findAll(INSTANCE, USER, COURSE, EXERCISE, SnapshotLevel.KEY);
        verifyNoMoreInteractions(snapshotService);
    }

    @Test
    public void shouldReturnRawEvents() throws Exception {

        final SnapshotEvent event = new SnapshotEvent();
        event.setEventType("text_insert");

        final Exercise exercise = new Exercise("exercise");
        exercise.addSnapshotEvent(event);

        when(exerciseService.find(INSTANCE, USER, COURSE, EXERCISE)).thenReturn(exercise);

        mockMvc.perform(get(SNAPSHOT_BASE_URL + "?level=raw"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$", hasSize(1)))
               .andExpect(jsonPath("$[0].eventType", is("text_insert")));

        verify(exerciseService).find(INSTANCE, USER, COURSE, EXERCISE);
        verifyNoMoreInteractions(exerciseService);
    }

    @Test
    public void shouldReturnSnapshot() throws Exception {

        final SnapshotFile file = new SnapshotFile("L3NyYy9IZWlNYWFpbG1hLmphdmE", "/src/HeiMaailma.java", "public class HeiMaailma { }");
        final Snapshot snapshotData = new Snapshot("1", 1L, new HashMap<String, SnapshotFile>(), false);

        snapshotData.addFile(file);

        when(snapshotService.find(INSTANCE, USER, COURSE, EXERCISE, "1", SnapshotLevel.KEY)).thenReturn(snapshotData);

        mockMvc.perform(get(SNAPSHOT_BASE_URL + "/1"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.id", is("1")))
               .andExpect(jsonPath("$.files", hasSize(1)))
               .andExpect(jsonPath("$.files[0].path", is("/src/HeiMaailma.java")))
               .andExpect(jsonPath("$.files[0].name", is("HeiMaailma.java")))
               .andExpect(jsonPath("$.files[0].id", is("L3NyYy9IZWlNYWFpbG1hLmphdmE")));

        verify(snapshotService).find(INSTANCE, USER, COURSE, EXERCISE, "1", SnapshotLevel.KEY);
        verifyNoMoreInteractions(snapshotService);
    }

    @Test
    public void listHandlesNotFoundException() throws Exception {

        when(snapshotService.findAll(INSTANCE, USER, COURSE, EXERCISE, SnapshotLevel.KEY)).thenThrow(new NotFoundException());

        mockMvc.perform(get(SNAPSHOT_BASE_URL))
               .andExpect(status().is(404));
    }

    @Test
    public void readHandlesNotFoundException() throws Exception {

        when(snapshotService.find(INSTANCE, USER, COURSE, EXERCISE, "1", SnapshotLevel.KEY)).thenThrow(new NotFoundException());

        mockMvc.perform(get(SNAPSHOT_BASE_URL + "/1"))
               .andExpect(status().is(404));
    }

    @Test
    public void readFilesReturnsZip() throws Exception {

        final byte[] bytes = { 0x00, 0x01, 0x02 };
        when(snapshotService.findFilesAsZip(INSTANCE, USER, COURSE, EXERCISE, SnapshotLevel.KEY, "", 0)).thenReturn(bytes);

        mockMvc.perform(get(SNAPSHOT_BASE_URL + "/files.zip?level=key"))
               .andExpect(status().isOk())
               .andExpect(content().contentType("application/zip"))
               .andExpect(content().bytes(bytes));

        verify(snapshotService).findFilesAsZip(INSTANCE, USER, COURSE, EXERCISE, SnapshotLevel.KEY, "", 0);
        verifyNoMoreInteractions(snapshotService);
    }

    @Test
    public void readFilesPassesLevelToService() throws Exception {

        mockMvc.perform(get(SNAPSHOT_BASE_URL + "/files.zip?level=code"));

        verify(snapshotService).findFilesAsZip(INSTANCE, USER, COURSE, EXERCISE, SnapshotLevel.CODE, "", 0);
    }

    @Test
    public void readFilesPassesFromToService() throws Exception {

        mockMvc.perform(get(SNAPSHOT_BASE_URL + "/files.zip?from=2"));

        verify(snapshotService).findFilesAsZip(INSTANCE, USER, COURSE, EXERCISE, SnapshotLevel.KEY, "2", 0);
    }

    @Test
    public void readFilesPassesCountToService() throws Exception {

        mockMvc.perform(get(SNAPSHOT_BASE_URL + "/files.zip?count=1"));

        verify(snapshotService).findFilesAsZip(INSTANCE, USER, COURSE, EXERCISE, SnapshotLevel.KEY, "", 1);
    }

    @Test
    public void readFilesPassesFromAndCountToService() throws Exception {

        mockMvc.perform(get(SNAPSHOT_BASE_URL + "/files.zip?from=2&count=1"));

        verify(snapshotService).findFilesAsZip(INSTANCE, USER, COURSE, EXERCISE, SnapshotLevel.KEY, "2", 1);
    }

    @Test
    public void readFilesPassesLevelAndFromAndCountToService() throws Exception {

        mockMvc.perform(get(SNAPSHOT_BASE_URL + "/files.zip?level=code&from=2&count=1"));

        verify(snapshotService).findFilesAsZip(INSTANCE, USER, COURSE, EXERCISE, SnapshotLevel.CODE, "2", 1);
    }

    @Test
    public void readFilesHandlesNotFoundException() throws Exception {

        when(snapshotService.findFilesAsZip(INSTANCE, USER, COURSE, EXERCISE, SnapshotLevel.KEY, "", 0)).thenThrow(new NotFoundException());

        mockMvc.perform(get(SNAPSHOT_BASE_URL + "/files.zip?level=key"))
               .andExpect(status().isNotFound());
    }
}
