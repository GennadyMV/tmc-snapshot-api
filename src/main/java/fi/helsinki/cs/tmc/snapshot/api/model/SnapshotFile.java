package fi.helsinki.cs.tmc.snapshot.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public final class SnapshotFile {

    private final String path;
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

    @JsonValue
    @Override
    public String toString() {

        return path;
    }
}
