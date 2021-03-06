package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.exception.NotFoundException;
import fi.helsinki.cs.tmc.snapshot.api.model.Exercise;

import java.io.IOException;
import java.util.Collection;

import org.apache.commons.codec.binary.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class DefaultExerciseService implements ExerciseService {

    @Autowired
    private CourseService courseService;

    @Override
    public Collection<Exercise> findAll(final String instanceId,
                                        final String participantId,
                                        final String courseId) throws IOException {

        return courseService.find(instanceId, participantId, courseId)
                            .getExercises();
    }

    @Override
    public Exercise find(final String instanceId,
                         final String participantId,
                         final String courseId,
                         final String exerciseId) throws IOException {

        final String exerciseName = new String(Base64.decodeBase64(exerciseId));
        final Exercise exercise = courseService.find(instanceId, participantId, courseId)
                                               .getExercise(exerciseName);

        if (exercise == null) {
            throw new NotFoundException();
        }

        return exercise;
    }
}
