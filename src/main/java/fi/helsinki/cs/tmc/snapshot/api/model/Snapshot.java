package fi.helsinki.cs.tmc.snapshot.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public final class Snapshot {

    private final String id;
    private final Date timestamp;

    private final Map<String, SnapshotFile> idsToFiles = new HashMap<>();
    private final Map<String, SnapshotFile> pathsToFiles;

    @JsonIgnore
    private final boolean fromCompleteSnapshot;

    public Snapshot(final String id,
                    final Long timestamp,
                    final Map<String, SnapshotFile> files,
                    final boolean fromCompleteSnapshot) {

        this.id = id;
        this.timestamp = new Date(timestamp);
        this.pathsToFiles = files;
        this.fromCompleteSnapshot = fromCompleteSnapshot;

        for (SnapshotFile file : pathsToFiles.values()) {
            idsToFiles.put(file.getId(), file);
        }
    }

    public String getId() {

        return id;
    }

    public Date getTimestamp() {

        return timestamp;
    }

    public void addFile(final SnapshotFile file) {

        idsToFiles.put(file.getId(), file);
        pathsToFiles.put(file.getPath(), file);
    }

    public SnapshotFile getFileForId(final String id) {

        return idsToFiles.get(id);
    }

    public SnapshotFile getFileForPath(final String path) {

        return pathsToFiles.get(path);
    }

    public Collection<SnapshotFile> getFiles() {

        return idsToFiles.values();
    }

    public Collection<SnapshotFile> getF() {

        return pathsToFiles.values();
    }

    public boolean isFromCompleteSnapshot() {

        return fromCompleteSnapshot;
    }
}
