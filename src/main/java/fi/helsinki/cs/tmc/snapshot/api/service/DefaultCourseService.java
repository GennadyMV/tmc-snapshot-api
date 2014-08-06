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
    public Collection<Course> findAll(final String instance, final String username) throws IOException {

        return participantService.find(instance, username).getCourses();
    }

    @Override
    public Course find(final String instance, final String username, final String course) throws IOException {

        return participantService.find(instance, username).getCourse(course);
    }
}
