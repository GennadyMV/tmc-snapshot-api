package fi.helsinki.cs.tmc.snapshot.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class SnapshotEventInformation {

    private String file;
    private String patches;

    @JsonProperty("full_document")
    private Boolean fullDocument;

    public void setFile(final String file) {

        this.file = file;
    }

    public String getFile() {

        return file;
    }

    public void setPatches(final String patches) {

        this.patches = patches;
    }

    public String getPatches() {

        return patches;
    }

    public void setFullDocument(final Boolean fullDocument) {

        this.fullDocument = fullDocument;
    }

    public Boolean isFullDocument() {

        return fullDocument;
    }
}
