package fi.helsinki.cs.tmc.snapshot.api.model;

public class ErrorMessage {

    private final String error;

    public ErrorMessage(final String error) {
        this.error = error;
    }

    public String getError() {
        return this.error;
    }

}
