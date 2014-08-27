package fi.helsinki.cs.tmc.snapshot.api.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

public final class Participant implements Comparable<Participant> {

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

    @Override
    public int compareTo(final Participant other) {

        return id.compareTo(other.getId());
    }
}
