package fi.helsinki.cs.tmc.snapshot.api.controller;

import fi.helsinki.cs.tmc.snapshot.api.app.App;
import fi.helsinki.cs.tmc.snapshot.api.exception.NotFoundException;
import fi.helsinki.cs.tmc.snapshot.api.model.Exercise;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotEvent;
import fi.helsinki.cs.tmc.snapshot.api.service.ExerciseService;

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
public final class ExerciseControllerTest {

    private static final String INSTANCE = "hy";
    private static final String USERNAME = "testUsername";
    private static final String COURSE = "testCourse";
    private static final String EXERCISE = "testExercise";
    private static final String EXERCISE_BASE_URL = "/hy/participants/testUsername/courses/testCourse/exercises";

    @Mock
    private ExerciseService exerciseService;

    @InjectMocks
    private ExerciseController exerciseController;

    private MockMvc mockMvc;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(exerciseController).build();
    }

    @Test
    public void listShouldReturnExercises() throws Exception {

        final List<Exercise> exercises = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            exercises.add(new Exercise("ex" + i));
        }

        when(exerciseService.findAll(INSTANCE, USERNAME, COURSE)).thenReturn(exercises);

        mockMvc.perform(get(EXERCISE_BASE_URL))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$", hasSize(5)))
               .andExpect(jsonPath("$[0].name", is("ex0")))
               .andExpect(jsonPath("$[1].name", is("ex1")))
               .andExpect(jsonPath("$[2].name", is("ex2")))
               .andExpect(jsonPath("$[3].name", is("ex3")))
               .andExpect(jsonPath("$[4].name", is("ex4")));

        verify(exerciseService).findAll(INSTANCE, USERNAME, COURSE);
        verifyNoMoreInteractions(exerciseService);
    }

    @Test
    public void listHandlesNotFoundException() throws Exception {

        when(exerciseService.findAll(INSTANCE, USERNAME, COURSE)).thenThrow(new NotFoundException());

        mockMvc.perform(get(EXERCISE_BASE_URL))
               .andExpect(status().isNotFound());
    }

    @Test
    public void readShouldReturnExercise() throws Exception {

        final Exercise exercise = new Exercise(EXERCISE);
        for (int i = 0; i < 5; i++) {
            exercise.addSnapshotEvent(new SnapshotEvent());
        }

        when(exerciseService.find(INSTANCE, USERNAME, COURSE, EXERCISE)).thenReturn(exercise);

        mockMvc.perform(get(EXERCISE_BASE_URL + "/" + EXERCISE))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.name", is(exercise.getName())))
               .andExpect(jsonPath("$.id", is(exercise.getId())));

        verify(exerciseService).find(INSTANCE, USERNAME, COURSE, EXERCISE);
        verifyNoMoreInteractions(exerciseService);
    }

    @Test
    public void readHandlesNotFoundException() throws Exception {

        when(exerciseService.find(INSTANCE, USERNAME, COURSE, EXERCISE)).thenThrow(new NotFoundException());

        mockMvc.perform(get(EXERCISE_BASE_URL + "/" + EXERCISE))
               .andExpect(status().isNotFound());
    }
}
