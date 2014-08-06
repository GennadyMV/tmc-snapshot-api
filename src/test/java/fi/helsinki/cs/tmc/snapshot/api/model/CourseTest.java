package fi.helsinki.cs.tmc.snapshot.api.model;

import java.util.Objects;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
        assertEquals("bW9vYw", course.getId());
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

        assertNull(course.getExercise("ex1"));
        assertEquals(e, course.getExercise("ZXgx"));
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
