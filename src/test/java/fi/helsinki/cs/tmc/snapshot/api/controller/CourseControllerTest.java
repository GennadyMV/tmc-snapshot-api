package fi.helsinki.cs.tmc.snapshot.api.controller;

import fi.helsinki.cs.tmc.snapshot.api.app.App;
import fi.helsinki.cs.tmc.snapshot.api.exception.NotFoundException;
import fi.helsinki.cs.tmc.snapshot.api.model.Course;
import fi.helsinki.cs.tmc.snapshot.api.model.Exercise;
import fi.helsinki.cs.tmc.snapshot.api.service.CourseService;

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
public class CourseControllerTest {

    private static final String INSTANCE = "hy";
    private static final String USERNAME = "testUsername";
    private static final String COURSE = "testCourse";
    private static final String COURSE_BASE_URL = "/hy/participants/testUsername/courses";

    @Mock
    private CourseService courseService;

    @InjectMocks
    private CourseController courseController;

    private MockMvc mockMvc;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(courseController).build();
    }

    @Test
    public void listShouldReturnExercises() throws Exception {

        final List<Course> courses = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            courses.add(new Course("course" + i));
        }

        when(courseService.findAll(INSTANCE, USERNAME)).thenReturn(courses);

        mockMvc.perform(get(COURSE_BASE_URL))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$", hasSize(5)))
               .andExpect(jsonPath("$[0].name", is("course0")))
               .andExpect(jsonPath("$[1].name", is("course1")))
               .andExpect(jsonPath("$[2].name", is("course2")))
               .andExpect(jsonPath("$[3].name", is("course3")))
               .andExpect(jsonPath("$[4].name", is("course4")));

        verify(courseService).findAll(INSTANCE, USERNAME);
        verifyNoMoreInteractions(courseService);
    }

    @Test
    public void listHandlesNotFoundException() throws Exception {

        when(courseService.findAll(INSTANCE, USERNAME)).thenThrow(new NotFoundException());

        mockMvc.perform(get(COURSE_BASE_URL))
               .andExpect(status().isNotFound());
    }

    @Test
    public void readShouldReturnExercise() throws Exception {

        final Course course = new Course(COURSE);
        for (int i = 0; i < 5; i++) {
            course.addExercise(new Exercise("ex" + i));
        }

        when(courseService.find(INSTANCE, USERNAME, COURSE)).thenReturn(course);

        mockMvc.perform(get(COURSE_BASE_URL + "/" + COURSE))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.name", is(course.getName())))
               .andExpect(jsonPath("$.id", is(course.getId())));

        verify(courseService).find(INSTANCE, USERNAME, COURSE);
        verifyNoMoreInteractions(courseService);
    }

    @Test
    public void readHandlesNotFoundException() throws Exception {

        when(courseService.find(INSTANCE, USERNAME, COURSE)).thenThrow(new NotFoundException());

        mockMvc.perform(get(COURSE_BASE_URL + "/" + COURSE))
               .andExpect(status().isNotFound());
    }
}
