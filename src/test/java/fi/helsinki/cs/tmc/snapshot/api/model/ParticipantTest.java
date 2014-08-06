package fi.helsinki.cs.tmc.snapshot.api.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public final class ParticipantTest {

    @Test
    public void constructorSetsValues() {

        final Participant participant = new Participant("admin");

        final Course course = new Course("mooc");
        participant.addCourse(course);
        participant.addCourse(course);

        assertEquals("admin", participant.getUsername());
        assertEquals("YWRtaW4", participant.getId());

        assertNull(participant.getCourse("mooc"));

        assertEquals(course, participant.getCourse("bW9vYw"));
        assertEquals(1, participant.getCourses().size());
    }
}
