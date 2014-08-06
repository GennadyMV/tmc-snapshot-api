package fi.helsinki.cs.tmc.snapshot.api.model;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Snapshot implements Comparable<Snapshot> {

    private final Long id;
    private final Date timestamp;
    private final boolean fromCompleteSnapshot;
    private final Map<String, SnapshotFile> files;

    public Snapshot(final Long id, final List<SnapshotFile> files) {

        this.id = id;
        this.timestamp = new Date(id);
        this.fromCompleteSnapshot = false;

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
        this.timestamp = new Date(id);
        this.fromCompleteSnapshot = fromCompleteSnapshot;
        this.files = files;
    }

    public Long getId() {

        return id;
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

    public Date getTimestamp() {

        return timestamp;
    }

    public boolean isFromCompleteSnapshot() {

        return fromCompleteSnapshot;
    }

    @Override
    public int compareTo(final Snapshot other) {

        return this.timestamp.compareTo(other.timestamp);
    }
}
