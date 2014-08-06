package fi.helsinki.cs.tmc.snapshot.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.util.DigestUtils;

public final class Course {

    private final String id;
    private final String name;

    @JsonIgnore
    private final Map<String, Exercise> exercises;

    public Course(final String name) {

        id = DigestUtils.md5DigestAsHex(name.getBytes());
        this.name = name;
        exercises = new HashMap<>();
    }

    public String getId() {

        return id;
    }

    public String getName() {

        return name;
    }

    public Collection<Exercise> getExercises() {

        return exercises.values();
    }

    public void addExercise(final Exercise exercise) {

        if (!exercises.containsKey(exercise.getName())) {
            exercises.put(exercise.getName(), exercise);
        }
    }

    public Exercise getExercise(final String exerciseName) {

        return exercises.get(exerciseName);
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

        return id.equals(other.getId());
    }
}
