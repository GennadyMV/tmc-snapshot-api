package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.model.Course;

import java.io.IOException;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class DefaultCourseService implements CourseService {

    @Autowired
    private ParticipantService participantService;

    @Override
    public Collection<Course> findAll(final String instance, final String id) throws IOException {

        return participantService.find(instance, id).getCourses();
    }

    @Override
    public Course find(final String instance, final String userId, final String courseId) throws IOException {

        return participantService.find(instance, userId).getCourse(courseId);
    }
}
