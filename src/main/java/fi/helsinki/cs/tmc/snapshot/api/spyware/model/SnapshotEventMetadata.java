package fi.helsinki.cs.tmc.snapshot.api.spyware.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class SnapshotEventMetadata {

    private final String cause;
    private final String file;

    @JsonCreator
    public SnapshotEventMetadata(@JsonProperty("cause") final String cause, @JsonProperty("file") final String file) {

        this.cause = cause;
        this.file = file;
    }

    public String getCause() {

        return cause;
    }

    public String getFile() {

        return file;
    }
}
