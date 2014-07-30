package fi.helsinki.cs.tmc.snapshot.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Metadata {

    private final String cause;
    private final String file;

    @JsonCreator
    public Metadata(@JsonProperty("cause") final String cause, @JsonProperty("file") final String file) {

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
