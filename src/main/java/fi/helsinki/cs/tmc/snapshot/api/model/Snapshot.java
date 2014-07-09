package fi.helsinki.cs.tmc.snapshot.api.model;

import java.util.Map;

public final class Snapshot {

    private final Long id;
    private final Map<String, SnapshotFile> files;

    public Snapshot(final Long id, final Map<String, SnapshotFile> files) {

        this.id = id;
        this.files = files;
    }
}
