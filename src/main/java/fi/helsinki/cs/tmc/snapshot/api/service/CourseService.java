package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.model.Course;

import java.io.IOException;
import java.util.Collection;

public interface CourseService {

    Collection<Course> findAll(String instanceId, String id) throws IOException;
    Course find(String instanceId, String participantId, String courseId) throws IOException;

}
