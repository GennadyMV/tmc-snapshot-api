package fi.helsinki.cs.tmc.snapshot.api.model;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class ParticipantTest {

    @Test
    public void constructorSetsValues() {

        final List<Snapshot> snapshots = new ArrayList<>();
        final Participant participant = new Participant(123L, snapshots);

        assertEquals(Long.valueOf(123), participant.getId());
        assertEquals(snapshots, participant.getSnapshots());
    }
}
