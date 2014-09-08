package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.exception.NotFoundException;
import fi.helsinki.cs.tmc.snapshot.api.model.Course;

import java.io.IOException;
import java.util.Collection;

import org.apache.commons.codec.binary.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class DefaultCourseService implements CourseService {

    @Autowired
    private ParticipantService participantService;

    @Override
    public Collection<Course> findAll(final String instanceId, final String id) throws IOException {

        return participantService.find(instanceId, id).getCourses();
    }

    @Override
    public Course find(final String instanceId, final String participantId, final String courseId) throws IOException {

        final String courseName = new String(Base64.decodeBase64(courseId));
        final Course course = participantService.find(instanceId, participantId)
                                                .getCourse(courseName);

        if (course == null) {
            throw new NotFoundException();
        }

        return course;
    }
}
