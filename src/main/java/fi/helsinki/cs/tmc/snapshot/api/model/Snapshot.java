package fi.helsinki.cs.tmc.snapshot.api.model;

import java.util.List;

public final class Snapshot {

    private final Long id;
    private final List<SnapshotFile> files;

    public Snapshot(final Long id, final List<SnapshotFile> files) {

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
