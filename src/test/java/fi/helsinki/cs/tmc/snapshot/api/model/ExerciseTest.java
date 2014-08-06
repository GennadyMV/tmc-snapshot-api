package fi.helsinki.cs.tmc.snapshot.api.model;

import java.util.Objects;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class ExerciseTest {

    private Exercise exercise;

    @Before
    public void setUp() {

        exercise = new Exercise(1L, "test");
    }

    @Test
    public void constructorSetsValues() {

        assertEquals(1L, (long) exercise.getId());
        assertEquals("test", exercise.getName());
        assertEquals(83 * 7 + Objects.hashCode(exercise.getName()), exercise.hashCode());
    }

    @Test
    public void equalsShouldReturnFalseOnNullObject() {

        assertFalse(exercise.equals(null));
    }

    @Test
    public void equalsShouldReturnFalseOnDifferentClassObject() {

        assertFalse(exercise.equals("string"));
    }

    @Test
    public void equalsShouldReturnFalseOnDifferentExerciseId() {

        final Exercise other = new Exercise(2L, "test");

        assertFalse(exercise.equals(other));
    }

    @Test
    public void equalsShouldReturnTrueOnSameExerciseId() {

        final Exercise other = new Exercise(1L, "hy");

        assertTrue(exercise.equals(other));
    }
}
