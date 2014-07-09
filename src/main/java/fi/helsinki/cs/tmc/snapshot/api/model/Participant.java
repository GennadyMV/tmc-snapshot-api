package fi.helsinki.cs.tmc.snapshot.api.model;

import java.util.List;

public final class Participant {

    private final Long id;
    private final List<Snapshot> snapshots;

    public Participant(final Long id, final List<Snapshot> snapshots) {

        this.id = id;
        this.snapshots = snapshots;
    }
}
