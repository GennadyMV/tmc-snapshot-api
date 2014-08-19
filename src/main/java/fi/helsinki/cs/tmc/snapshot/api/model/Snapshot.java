package fi.helsinki.cs.tmc.snapshot.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Snapshot {

    private final String id;
    private final Date timestamp;
    private final Map<String, SnapshotFile> files;

    @JsonIgnore
    private final boolean fromCompleteSnapshot;

    public Snapshot(final String id, final Long timestamp, final List<SnapshotFile> files) {

        this.id = id;
        this.timestamp = new Date(timestamp);
        fromCompleteSnapshot = false;

        this.files = new HashMap<>();

        for (SnapshotFile file : files) {
            this.files.put(file.getId(), file);
        }
    }

    public Snapshot(final String id, final Long timestamp, final Map<String, SnapshotFile> files) {

        this(id, timestamp, files, false);
    }

    public Snapshot(final String id,
                    final Long timestamp,
                    final Map<String, SnapshotFile> files,
                    final boolean fromCompleteSnapshot) {

        this.id = id;
        this.timestamp = new Date(timestamp);
        this.files = files;
        this.fromCompleteSnapshot = fromCompleteSnapshot;
    }

    public String getId() {

        return id;
    }

    public Date getTimestamp() {

        return timestamp;
    }

    public void addFile(final SnapshotFile file) {

        files.put(file.getId(), file);
    }

    public SnapshotFile getFile(final String id) {

        return files.get(id);
    }

    public Collection<SnapshotFile> getFiles() {

        return files.values();
    }

    public boolean isFromCompleteSnapshot() {

        return fromCompleteSnapshot;
    }
}
