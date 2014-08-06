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

        exercise = new Exercise("test");
    }

    @Test
    public void constructorSetsValues() {

        assertEquals("test", exercise.getName());
        assertEquals("dGVzdA", exercise.getId());
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

        final Exercise other = new Exercise("toast");

        assertFalse(exercise.getId().equals(other.getId()));
        assertFalse(exercise.equals(other));
    }

    @Test
    public void equalsShouldReturnTrueOnSameExerciseId() {

        final Exercise other = new Exercise("test");

        assertTrue(exercise.getId().equals(other.getId()));
        assertTrue(exercise.equals(other));
    }
}
