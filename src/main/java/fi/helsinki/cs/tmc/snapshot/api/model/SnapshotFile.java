package fi.helsinki.cs.tmc.snapshot.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.tomcat.util.codec.binary.Base64;

public final class SnapshotFile {

    private final String id;
    private final String path;
    private final String name;
    private final String content;

    @JsonCreator
    public SnapshotFile(@JsonProperty("path") final String path, @JsonProperty("content") final String content) {

        this.path = path;
        this.content = content;

        name = path.substring(path.lastIndexOf("/") + 1);
        id = Base64.encodeBase64URLSafeString(path.getBytes());
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
