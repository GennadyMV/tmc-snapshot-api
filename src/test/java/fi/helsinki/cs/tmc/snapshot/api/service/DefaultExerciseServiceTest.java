package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.exception.NotFoundException;
import fi.helsinki.cs.tmc.snapshot.api.model.Course;
import fi.helsinki.cs.tmc.snapshot.api.model.Exercise;

import java.io.IOException;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import static org.mockito.Mockito.when;

public class DefaultExerciseServiceTest {

    private static final String INSTANCE = "testInstance";
    private static final String USERNAME = "testUsername";
    private static final String COURSE = "testCourse";

    @Mock
    private CourseService courseService;

    @InjectMocks
    private DefaultExerciseService exerciseService;

    private Course course;
    private Exercise exercise1;
    private Exercise exercise2;
    private Exercise exercise3;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);

        course = new Course(COURSE);

        exercise1 = new Exercise("exercise1");
        exercise2 = new Exercise("exercise2");
        exercise3 = new Exercise("exercise3");

        course.addExercise(exercise1);
        course.addExercise(exercise2);
        course.addExercise(exercise3);
    }

    @Test
    public void findAllReturnsAllTheExercisesFromTheCourse() throws IOException {

        when(courseService.find(INSTANCE, USERNAME, COURSE)).thenReturn(course);

        final Collection<Exercise> exercises = exerciseService.findAll(INSTANCE, USERNAME, COURSE);

        assertEquals(3, exercises.size());
        assertTrue(exercises.contains(exercise1));
        assertTrue(exercises.contains(exercise2));
        assertTrue(exercises.contains(exercise3));
    }

    @Test(expected = NotFoundException.class)
    public void findAllPassesOnNotFoundException() throws IOException {

        when(courseService.find(INSTANCE, USERNAME, COURSE)).thenThrow(new NotFoundException());

        exerciseService.findAll(INSTANCE, USERNAME, COURSE);
    }

    @Test
    public void findReturnsCorrectCourse() throws IOException {

        when(courseService.find(INSTANCE, USERNAME, COURSE)).thenReturn(course);

        final Exercise exercise = exerciseService.find(INSTANCE, USERNAME, COURSE, exercise1.getId());

        assertEquals(exercise1, exercise);
    }

    @Test(expected = NotFoundException.class)
    public void findHandlesNullCourse() throws IOException {

        when(courseService.find(INSTANCE, USERNAME, COURSE)).thenReturn(course);

        exerciseService.find(INSTANCE, USERNAME, COURSE, "NoSuchId");
    }

    @Test(expected = NotFoundException.class)
    public void findPassesOnNotFoundException() throws IOException {

        when(courseService.find(INSTANCE, USERNAME, COURSE)).thenThrow(new NotFoundException());

        exerciseService.find(INSTANCE, USERNAME, COURSE, "NosuchId");
    }
}
