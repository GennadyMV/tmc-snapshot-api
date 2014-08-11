package fi.helsinki.cs.tmc.snapshot.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Snapshot implements Comparable<Snapshot> {

    private final Long id;
    private final Date timestamp;
    private final Map<String, SnapshotFile> files;

    @JsonIgnore
    private final boolean fromCompleteSnapshot;

    public Snapshot(final Long id, final List<SnapshotFile> files) {

        this.id = id;
        timestamp = new Date(id);
        fromCompleteSnapshot = false;

        this.files = new HashMap<>();

        for (SnapshotFile file : files) {
            this.files.put(file.getPath(), file);
        }
    }

    public Snapshot(final Long id, final Map<String, SnapshotFile> files) {

        this(id, files, false);
    }

    public Snapshot(final Long id,
                    final Map<String, SnapshotFile> files,
                    final boolean fromCompleteSnapshot) {

        this.id = id;
        timestamp = new Date(id);
        this.files = files;
        this.fromCompleteSnapshot = fromCompleteSnapshot;
    }

    public Long getId() {

        return id;
    }

    public Date getTimestamp() {

        return timestamp;
    }

    public void addFile(final SnapshotFile file) {

        files.put(file.getPath(), file);
    }

    public SnapshotFile getFile(final String path) {

        return files.get(path);
    }

    public Collection<SnapshotFile> getFiles() {

        return files.values();
    }

    public boolean isFromCompleteSnapshot() {

        return fromCompleteSnapshot;
    }

    @Override
    public int compareTo(final Snapshot other) {

        return timestamp.compareTo(other.timestamp);
    }
}
