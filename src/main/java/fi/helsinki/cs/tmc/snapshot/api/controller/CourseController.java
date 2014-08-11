package fi.helsinki.cs.tmc.snapshot.api.controller;

import fi.helsinki.cs.tmc.snapshot.api.model.Course;
import fi.helsinki.cs.tmc.snapshot.api.service.CourseService;

import java.io.IOException;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "{instance}/participants/{userId}/courses", produces = "application/json")
public final class CourseController {

    @Autowired
    private CourseService courseService;

    @RequestMapping(method = RequestMethod.GET)
    public Collection<Course> list(@PathVariable final String instance, @PathVariable final String userId) throws IOException {

        return courseService.findAll(instance, userId);
    }

    @RequestMapping(method = RequestMethod.GET, value = "{courseId}")
    public Course read(@PathVariable final String instance,
                       @PathVariable final String userId,
                       @PathVariable final String courseId) throws IOException {

        return courseService.find(instance, userId, courseId);
    }
}