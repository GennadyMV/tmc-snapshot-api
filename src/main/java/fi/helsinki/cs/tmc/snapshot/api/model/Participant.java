package fi.helsinki.cs.tmc.snapshot.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public final class Participant {

    private final Long id;
    private final List<Snapshot> snapshots;

    @JsonCreator
    public Participant(@JsonProperty("id") final Long id, @JsonProperty("snapshots") final List<Snapshot> snapshots) {

        this.id = id;
        this.snapshots = snapshots;
    }

    public Long getId() {

        return id;
    }

    public List<Snapshot> getSnapshots() {

        return snapshots;
    }
}
