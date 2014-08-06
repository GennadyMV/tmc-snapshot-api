package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.model.Exercise;

import java.io.IOException;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class DefaultExerciseService implements ExerciseService {

    @Autowired
    private CourseService courseService;

    @Override
    public Collection<Exercise> findAll(final String instance,
                                        final String userId,
                                        final String courseId) throws IOException {

        return courseService.find(instance, userId, courseId).getExercises();
    }

    @Override
    public Exercise find(final String instance,
                             final String userId,
                             final String courseId,
                             final String exerciseId) throws IOException {

        return courseService.find(instance, userId, courseId).getExercise(exerciseId);
    }
}
