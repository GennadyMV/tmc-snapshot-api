package fi.helsinki.cs.tmc.snapshot.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

public final class Participant {

    private final String id;
    private final String username;
    private final Map<String, Course> courses;

    public Participant(final String username) {

        id = Base64.encodeBase64URLSafeString(username.getBytes());
        this.username = username;
        courses = new HashMap<>();
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

        courses.put(course.getId(), course);
    }

    public Course getCourse(final String id) {

        return courses.get(id);
    }
}
