package fi.helsinki.cs.tmc.snapshot.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public final class Snapshot {

    private final Long id;
    private final List<SnapshotFile> files;

    @JsonCreator
    public Snapshot(@JsonProperty("id") final Long id, @JsonProperty("files") final List<SnapshotFile> files) {

        this.id = id;
        this.files = files;
    }

    public Long getId() {

        return id;
    }

    public List<SnapshotFile> getFiles() {

        return files;
    }
}
