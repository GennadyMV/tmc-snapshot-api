package fi.helsinki.cs.tmc.snapshot.api.model;

import java.util.Objects;
import org.junit.Before;
import org.junit.Test;

import org.springframework.util.DigestUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CourseTest {

    private Course course;

    @Before
    public void setUp() {

        course = new Course("mooc");
    }

    @Test
    public void constructorSetsValues() {

        assertEquals("mooc", course.getName());
        assertEquals(DigestUtils.md5DigestAsHex("mooc".getBytes()), course.getId());
        assertEquals(59 * 7 + Objects.hashCode(course.getName()), course.hashCode());
    }

    @Test
    public void hasNoExercisesAfterConstruction() {

        assertNotNull(course.getExercises());
        assertEquals(0, course.getExercises().size());
    }

    @Test
    public void addedExercisesCanBeFetched() {

        final Exercise e = new Exercise("ex1");
        course.addExercise(e);

        assertEquals(1, course.getExercises().size());
        assertEquals(e, course.getExercises().iterator().next());
        assertEquals(e, course.getExercise("ex1"));
    }

    @Test
    public void canNotAddSameExerciseMultipleTimes() {

        final Exercise e = new Exercise("ex1");
        course.addExercise(e);
        course.addExercise(e);

        assertEquals(1, course.getExercises().size());

    }

    @Test
    public void equalsShouldReturnFalseOnNullObject() {

        assertFalse(course.equals(null));
    }

    @Test
    public void equalsShouldReturnFalseOnDifferentClassObject() {

        assertFalse(course.equals("string"));
    }

    @Test
    public void equalsShouldReturnFalseOnDifferentCourseName() {

        final Course other = new Course("mooc2");

        assertFalse(course.equals(other));
    }

    @Test
    public void equalsShouldReturnTrueOnSameCourseName() {

        final Course other = new Course("mooc");

        assertTrue(course.equals(other));
    }
}
