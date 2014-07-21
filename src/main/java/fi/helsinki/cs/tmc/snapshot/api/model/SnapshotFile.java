package fi.helsinki.cs.tmc.snapshot.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class SnapshotFile {

    private final String path;

    @JsonIgnore
    private final String content;

    @JsonCreator
    public SnapshotFile(@JsonProperty("path") final String path, @JsonProperty("content") final String content) {

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
