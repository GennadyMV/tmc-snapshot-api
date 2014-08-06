package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.exception.NotFoundException;
import fi.helsinki.cs.tmc.snapshot.api.model.Course;

import java.io.IOException;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private ParticipantService participantService;

    @Override
    public Collection<Course> find(final String instance, final String username) throws IOException {

        final Collection<Course> courses = participantService.find(instance, username).getCourses();

        if (courses == null) {
            throw new NotFoundException();
        }

        return courses;
    }

    @Override
    public Course find(final String instance, final String username, final String courseId) throws IOException {

        final Course course = participantService.find(instance, username).getCourse(courseId);

        if (course == null) {
            throw new NotFoundException();
        }

        return course;
    }
}
