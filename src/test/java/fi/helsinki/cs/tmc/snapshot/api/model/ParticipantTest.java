package fi.helsinki.cs.tmc.snapshot.api.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class ParticipantTest {

    @Test
    public void constructorSetsValues() {

        final Participant participant = new Participant("admin");

        final Course course = new Course("mooc");
        participant.addCourse(course);
        participant.addCourse(course);

        assertEquals("admin", participant.getUsername());
        assertEquals("YWRtaW4=", participant.getId());
        assertEquals(course, participant.getCourse("mooc"));
        assertEquals(1, participant.getCourses().size());
    }
}
