package fi.helsinki.cs.tmc.snapshot.api.model;

import java.util.Objects;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class CourseTest {

    private Course course;

    @Before
    public void setUp() {

        course = new Course(1L, "mooc");
    }

    @Test
    public void constructorSetsValues() {

        assertEquals(1L, (long) course.getId());
        assertEquals("mooc", course.getName());
        assertEquals(59 * 7 + Objects.hashCode(course.getName()), course.hashCode());
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
    public void equalsShouldReturnFalseOnDifferentCourseId() {

        final Course other = new Course(2L, "mooc");

        assertFalse(course.equals(other));
    }

    @Test
    public void equalsShouldReturnTrueOnSameCourseId() {

        final Course other = new Course(1L, "hy");

        assertTrue(course.equals(other));
    }
}
