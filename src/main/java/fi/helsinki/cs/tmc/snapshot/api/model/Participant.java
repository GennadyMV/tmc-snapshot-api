package fi.helsinki.cs.tmc.snapshot.api.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Participant extends SimpleParticipant {

    private final Map<String, Course> courses;

    public Participant(final String username) {

        super(username);
        this.courses = new HashMap<>();
    }

    public List<Course> getCourses() {

        final List sortedCourses = new ArrayList<>(courses.values());
        Collections.sort(sortedCourses);

        return sortedCourses;
    }

    public void addCourse(final Course course) {

        courses.put(course.getName(), course);
    }

    public Course getCourse(final String name) {

        return courses.get(name);
    }
}
