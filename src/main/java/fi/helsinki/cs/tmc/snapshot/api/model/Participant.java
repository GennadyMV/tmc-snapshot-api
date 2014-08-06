package fi.helsinki.cs.tmc.snapshot.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class Participant {

    private final String id;
    private final String username;
    private final Map<String, Course> courses;

    public Participant(final String username) {

        this.id = new String(Base64.getEncoder().encode(username.getBytes()));
        this.username = username;
        this.courses = new HashMap<>();
    }

    public String getId() {

        return id;
    }

    public String getUsername() {

        return username;
    }

    @JsonIgnore
    public Collection<Course> getCourses() {

        return courses.values();
    }

    public void addCourse(final Course course) {

        if (!courses.containsKey(course.getName())) {
            this.courses.put(course.getName(), course);
        }
    }

    public Course getCourse(final String courseName) {

        return courses.get(courseName);
    }
}
