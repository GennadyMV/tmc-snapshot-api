package fi.helsinki.cs.tmc.snapshot.api.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class TmcParticipantTest {

    private TmcParticipant participant;

    @Before
    public void setUp() {

        participant = new TmcParticipant();
    }

    @Test
    public void canSetId() {

        participant.setId(123L);

        assertEquals(Long.valueOf(123L), participant.getId());
    }

    @Test
    public void canSetUsername() {

        participant.setUsername("user123");

        assertEquals("user123", participant.getUsername());
    }

    @Test
    public void canSortUsersWithId() {

        assertEquals(-1, participant.compareTo(null));

        participant.setId(10L);
        final TmcParticipant participant2 = new TmcParticipant();
        assertEquals(-1, participant.compareTo(participant2));

        participant2.setId(10L);
        assertEquals(0, participant.compareTo(participant2));

        participant2.setId(0L);
        assertEquals(1, participant.compareTo(participant2));
        assertEquals(-1, participant2.compareTo(participant));
        assertEquals(-1, participant.compareTo(null));
    }
}
