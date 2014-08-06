package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.model.Exercise;

import java.io.IOException;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExerciseServiceImpl implements ExerciseService {

    @Autowired
    private CourseService courseService;

    @Override
    public Collection<Exercise> find(final String instance,
                                     final String username,
                                     final String course) throws IOException {

        return courseService.find(instance, username, course).getExercises();
    }

    @Override
    public Exercise find(final String instance,
                         final String username,
                         final String course,
                         final String exercise) throws IOException {

        return courseService.find(instance, username, course).getExercise(exercise);
    }
}
