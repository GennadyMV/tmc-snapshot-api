package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.exception.NotFoundException;
import fi.helsinki.cs.tmc.snapshot.api.model.Course;
import fi.helsinki.cs.tmc.snapshot.api.model.Participant;

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

public final class DefaultCourseServiceTest {

    private static final String INSTANCE = "testInstance";
    private static final String USERNAME = "testUsername";

    @Mock
    private ParticipantService participantService;

    @InjectMocks
    private DefaultCourseService courseService;

    private Participant participant;
    private Course course1;
    private Course course2;
    private Course course3;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);

        participant = new Participant(USERNAME);

        course1 = new Course("1");
        course2 = new Course("2");
        course3 = new Course("3");

        participant.addCourse(course1);
        participant.addCourse(course2);
        participant.addCourse(course3);
    }

    @Test
    public void findAllReturnsAllTheCoursesFromTheParticipant() throws IOException {

        when(participantService.find(INSTANCE, USERNAME)).thenReturn(participant);

        final Collection<Course> courses = courseService.findAll(INSTANCE, USERNAME);

        assertEquals(3, courses.size());
        assertTrue(courses.contains(course1));
        assertTrue(courses.contains(course2));
        assertTrue(courses.contains(course3));
    }

    @Test(expected = NotFoundException.class)
    public void findAllPassesOnNotFoundException() throws IOException {

        when(participantService.find(INSTANCE, USERNAME)).thenThrow(new NotFoundException());
        courseService.findAll(INSTANCE, USERNAME);
    }

    @Test
    public void findReturnsCorrectCourse() throws IOException {

        when(participantService.find(INSTANCE, USERNAME)).thenReturn(participant);

        final Course course = courseService.find(INSTANCE, USERNAME, course1.getId());
        assertEquals(course1, course);
    }

    @Test(expected = NotFoundException.class)
    public void findHandlesNullCourse() throws IOException {

        when(participantService.find(INSTANCE, USERNAME)).thenReturn(participant);

        courseService.find(INSTANCE, USERNAME, "NoSuchId");
    }

    @Test(expected = NotFoundException.class)
    public void findPassesOnNotFoundException() throws IOException {

        when(participantService.find(INSTANCE, USERNAME)).thenThrow(new NotFoundException());

        courseService.find(INSTANCE, USERNAME, "NosuchId");
    }
}
