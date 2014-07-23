package fi.helsinki.cs.tmc.snapshot.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Snapshot {

    private final Long id;
    private final Map<String, SnapshotFile> files;
    private final Date timestamp;

    @JsonCreator
    public Snapshot(@JsonProperty("id") final Long id, @JsonProperty("files") final List<SnapshotFile> files) {

        this.id = id;
        this.timestamp = new Date(id);

        this.files = new HashMap<>();
        for (SnapshotFile file : files) {
            this.files.put(file.getPath(), file);
        }
    }

    public Snapshot(final Long id, final Map<String, SnapshotFile> files) {

        this.id = id;
        this.timestamp = new Date(id);
        this.files = files;
    }

    public Long getId() {

        return id;
    }

    public Collection<SnapshotFile> getFiles() {

        return files.values();
    }

    public SnapshotFile getFile(final String path) {

        return files.get(path);
    }

    public Date getTimestamp() {

        return timestamp;
    }
}
