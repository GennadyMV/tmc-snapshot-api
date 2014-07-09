package fi.helsinki.cs.tmc.snapshot.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public final class SnapshotFile {

    private final String path;

    @JsonIgnore
    private final String content;

    public SnapshotFile(final String path, final String content) {

        this.path = path;
        this.content = content;
    }

    public String getPath() {

        return path;
    }

    public String getContent() {

        return content;
    }
}
