package fi.helsinki.cs.tmc.snapshot.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

public final class Snapshot {

    private final Long id;
    private final List<SnapshotFile> files;
    private final Date timestamp;

    @JsonCreator
    public Snapshot(@JsonProperty("id") final Long id, @JsonProperty("files") final List<SnapshotFile> files) {

        this.id = id;
        this.files = files;
        this.timestamp = new Date(id);
    }

    public Long getId() {

        return id;
    }

    public List<SnapshotFile> getFiles() {

        return files;
    }

    public Date getTimestamp() {

        return timestamp;
    }
}
