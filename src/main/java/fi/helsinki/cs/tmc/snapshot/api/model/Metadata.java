package fi.helsinki.cs.tmc.snapshot.api.model;

public final class Metadata {

    private String cause;
    private String file;

    public String getCause() {

        return cause;
    }

    public void setCause(final String cause) {

        this.cause = cause;
    }

    public String getFile() {

        return file;
    }

    public void setFile(final String file) {

        this.file = file;
    }
}
