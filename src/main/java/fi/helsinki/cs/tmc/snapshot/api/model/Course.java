package fi.helsinki.cs.tmc.snapshot.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class Course extends AbstractBase64Identifier {

    private final String name;

    @JsonIgnore
    private final Map<String, Exercise> exercises;

    public Course(final String name) {

        super(name);
        this.name = name;
        exercises = new HashMap<>();
    }

    public String getName() {

        return name;
    }

    @JsonProperty
    public List<Exercise> getExercises() {

        final List<Exercise> sortedExercises = new ArrayList<>(exercises.values());
        Collections.sort(sortedExercises);

        return sortedExercises;
    }

    public void addExercise(final Exercise exercise) {

        exercises.put(exercise.getName(), exercise);
    }

    public Exercise getExercise(final String name) {

        return exercises.get(name);
    }

    @Override
    public int hashCode() {

        return 59 * 7 + Objects.hashCode(name);
    }

    @Override
    public boolean equals(final Object object) {

        if (object == null) {
            return false;
        }

        if (getClass() != object.getClass()) {
            return false;
        }

        final Course other = (Course) object;

        return this.getId().equals(other.getId());
    }
}
