package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.model.Exercise;

import java.io.IOException;
import java.util.Collection;

public interface ExerciseService {

    Collection<Exercise> findAll(String instanceId, String participantId, String courseId) throws IOException;
    Exercise find(String instanceId, String participantId, String courseId, String exerciseId) throws IOException;

}
