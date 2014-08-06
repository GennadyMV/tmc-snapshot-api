package fi.helsinki.cs.tmc.snapshot.api.controller;

import fi.helsinki.cs.tmc.snapshot.api.app.App;
import fi.helsinki.cs.tmc.snapshot.api.exception.NotFoundException;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotFile;
import fi.helsinki.cs.tmc.snapshot.api.service.SnapshotFileService;

import java.util.ArrayList;
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
public final class SnapshotFileControllerTest {

    private static final String INSTANCE = "hy";
    private static final String USER = "testUsername";
    private static final String COURSE = "testCourseName";
    private static final String EXERCISE = "testExerciseName";
    private static final Long SNAPSHOT = 1L;
    private static final String FILE_BASE_URL = "/hy/participants/testUsername/courses/testCourseName/exercises/testExerciseName/snapshots/1/files";

    @Mock
    private SnapshotFileService snapshotFileService;

    @InjectMocks
    private SnapshotFileController snapshotFileController;

    private MockMvc mockMvc;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(snapshotFileController).build();
    }

    @Test
    public void shouldReturnSnapshotFiles() throws Exception {

        final List<SnapshotFile> files = new ArrayList<>();
        files.add(new SnapshotFile("path1", "content1"));
        files.add(new SnapshotFile("path2", "content2"));
        files.add(new SnapshotFile("path3", "content3"));


        when(snapshotFileService.findAll(INSTANCE, USER, COURSE, EXERCISE, SNAPSHOT)).thenReturn(files);

        mockMvc.perform(get(FILE_BASE_URL))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$", hasSize(3)))
               .andExpect(jsonPath("$[0]", is("path1")))
               .andExpect(jsonPath("$[1]", is("path2")))
               .andExpect(jsonPath("$[2]", is("path3")));

        verify(snapshotFileService).findAll(INSTANCE, USER, COURSE, EXERCISE, SNAPSHOT);
        verifyNoMoreInteractions(snapshotFileService);

    }

    @Test
    public void shouldReturnSnapshotFile() throws Exception {

        final SnapshotFile file = new SnapshotFile("/src/HeiMaailma.java", "public class HeiMaailma { }");

        when(snapshotFileService.find(INSTANCE, USER, COURSE, EXERCISE, SNAPSHOT, "/path")).thenReturn("testContent");

        mockMvc.perform(get(FILE_BASE_URL + "/path"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.TEXT_PLAIN))
               .andExpect(content().string("testContent"));

        verify(snapshotFileService).find(INSTANCE, USER, COURSE, EXERCISE, SNAPSHOT, "/path");
        verifyNoMoreInteractions(snapshotFileService);
    }

    @Test
    public void listShouldHandleNotFoundException() throws Exception {

        when(snapshotFileService.findAll(INSTANCE, USER, COURSE, EXERCISE, SNAPSHOT)).thenThrow(new NotFoundException());

        mockMvc.perform(get(FILE_BASE_URL))
                .andExpect(status().is(404));
    }

    @Test
    public void readShouldHandleNotFoundException() throws Exception {

        when(snapshotFileService.find(INSTANCE, USER, COURSE, EXERCISE, SNAPSHOT, "/path")).thenThrow(new NotFoundException());

        mockMvc.perform(get(FILE_BASE_URL + "/path"))
                .andExpect(status().is(404));
    }
}
