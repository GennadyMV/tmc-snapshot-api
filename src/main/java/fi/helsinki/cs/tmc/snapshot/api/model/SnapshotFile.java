package fi.helsinki.cs.tmc.snapshot.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class SnapshotFile {

    private final String id;
    private final String path;
    private final String name;
    private final String content;

    @JsonCreator
    public SnapshotFile(@JsonProperty("id") final String id,
                        @JsonProperty("path") final String path,
                        @JsonProperty("content") final String content) {

        this.id = id;
        this.path = path;
        this.content = content;

        this.name = path.substring(path.lastIndexOf("/") + 1);
    }

    public String getId() {

        return id;
    }

    public String getPath() {

        return path;
    }

    public String getName() {

        return name;
    }

    @JsonIgnore
    public String getContent() {

        return content;
    }
}
