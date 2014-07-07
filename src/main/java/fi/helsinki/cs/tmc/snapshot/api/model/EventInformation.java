package fi.helsinki.cs.tmc.snapshot.api.model;

public final class EventInformation {

    private String file;
    private String patches;
    private Boolean fullDocument;

    public String getFile() {

        return file;
    }

    public void setFile(final String file) {

        this.file = file;
    }

    public String getPatches() {

        return patches;
    }

    public void setPatches(final String patches) {

        this.patches = patches;
    }

    public Boolean isFullDocument() {

        return fullDocument;
    }

    public void setFullDocument(final Boolean fullDocument) {

        this.fullDocument = fullDocument;
    }
}
