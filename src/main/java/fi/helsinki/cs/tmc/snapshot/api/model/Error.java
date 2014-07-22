package fi.helsinki.cs.tmc.snapshot.api.model;

public final class Error {

    private final String error;

    public Error(final String error) {

        this.error = error;
    }

    public String getError() {

        return error;
    }
}
