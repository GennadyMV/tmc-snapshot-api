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
    public Collection<Course> findAll(final String instance, final String username) throws IOException {

        final Collection<Course> courses = participantService.findByInstanceAndId(instance, username).getCourses();

        if (courses == null) {
            throw new NotFoundException();
        }

        return courses;
    }

    @Override
    public Course findById(final String instance, final String username, final String courseId) throws IOException {

        final Course course = participantService.findByInstanceAndId(instance, username).getCourse(courseId);

        if (course == null) {
            throw new NotFoundException();
        }

        return course;
    }
}
