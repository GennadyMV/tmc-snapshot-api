package fi.helsinki.cs.tmc.snapshot.api.controller;

import fi.helsinki.cs.tmc.snapshot.api.app.App;
import fi.helsinki.cs.tmc.snapshot.api.exception.NotFoundException;
import fi.helsinki.cs.tmc.snapshot.api.model.Snapshot;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotFile;
import fi.helsinki.cs.tmc.snapshot.api.service.SnapshotService;

import java.util.ArrayList;
import java.util.Arrays;
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
                                          new ArrayList<SnapshotFile>()));
        }

        when(snapshotService.findAll(INSTANCE, USER, COURSE, EXERCISE)).thenReturn(snapshotData);

        mockMvc.perform(get(SNAPSHOT_BASE_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(5)));

        verify(snapshotService).findAll(INSTANCE, USER, COURSE, EXERCISE);
        verifyNoMoreInteractions(snapshotService);
    }

    @Test
    public void shouldReturnSnapshot() throws Exception {

        final SnapshotFile file = new SnapshotFile("/src/HeiMaailma.java", "public class HeiMaailma { }");
        final Snapshot snapshotData = new Snapshot("1", 1L, Arrays.asList(file));

        when(snapshotService.find(INSTANCE, USER, COURSE, EXERCISE, "1")).thenReturn(snapshotData);

        mockMvc.perform(get(SNAPSHOT_BASE_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is("1")))
                .andExpect(jsonPath("$.files", hasSize(1)))
                .andExpect(jsonPath("$.files[0].path", is("/src/HeiMaailma.java")))
                .andExpect(jsonPath("$.files[0].name", is("HeiMaailma.java")))
                .andExpect(jsonPath("$.files[0].id", is("SGVpTWFhaWxtYS5qYXZh")));

        verify(snapshotService).find(INSTANCE, USER, COURSE, EXERCISE, "1");
        verifyNoMoreInteractions(snapshotService);
    }

    @Test
    public void listHandlesNotFoundException() throws Exception {

        when(snapshotService.findAll(INSTANCE, USER, COURSE, EXERCISE)).thenThrow(new NotFoundException());

        mockMvc.perform(get(SNAPSHOT_BASE_URL))
                .andExpect(status().is(404));
    }

    @Test
    public void readHandlesNotFoundException() throws Exception {

        when(snapshotService.find(INSTANCE, USER, COURSE, EXERCISE, "1")).thenThrow(new NotFoundException());

        mockMvc.perform(get(SNAPSHOT_BASE_URL + "/1"))
                .andExpect(status().is(404));
    }
}
