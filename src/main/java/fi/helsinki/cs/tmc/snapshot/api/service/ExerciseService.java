package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.model.Exercise;

import java.io.IOException;
import java.util.Collection;

public interface ExerciseService {

    Collection<Exercise> findAll(String instance, String userId, String courseId) throws IOException;
    Exercise find(String instance, String userId, String courseId, String exerciseId) throws IOException;

}
