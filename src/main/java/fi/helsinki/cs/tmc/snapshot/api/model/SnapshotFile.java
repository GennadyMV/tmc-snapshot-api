package fi.helsinki.cs.tmc.snapshot.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.tomcat.util.codec.binary.Base64;

public final class SnapshotFile {

    private final String id;
    private final String name;
    private final String path;
    private final String content;

    @JsonCreator
    public SnapshotFile(@JsonProperty("path") final String path, @JsonProperty("content") final String content) {

        this.path = path;
        this.content = content;
        this.name = path.substring(path.lastIndexOf("/") + 1);
        this.id = Base64.encodeBase64URLSafeString(name.getBytes());
    }

    public String getId() {

        return id;
    }

    public String getName() {

        return name;
    }

    public String getPath() {

        return path;
    }

    @JsonIgnore
    public String getContent() {

        return content;
    }

    @Override
    public String toString() {

        return path;
    }
}
