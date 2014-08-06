package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.model.Exercise;

import java.io.IOException;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class ParticipantExerciseService implements ExerciseService {

    @Autowired
    private CourseService courseService;

    @Override
    public Collection<Exercise> findAll(final String instance,
                                        final String username,
                                        final String course) throws IOException {

        return courseService.findById(instance, username, course).getExercises();
    }

    @Override
    public Exercise findById(final String instance,
                             final String username,
                             final String course,
                             final String exercise) throws IOException {

        return courseService.findById(instance, username, course).getExercise(exercise);
    }
}
