package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.model.Exercise;

import java.io.IOException;
import java.util.Collection;

public interface ExerciseService {

    Collection<Exercise> findAll(String instance, String username, String course) throws IOException;
    Exercise find(String instance, String username, String course, String exercise) throws IOException;

}
