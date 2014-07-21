package fi.helsinki.cs.tmc.snapshot.api.app;

public final class ApiException extends Exception {

    public ApiException(final Throwable throwable) {

        super(throwable);
    }

    public ApiException(final String message) {

        super(message);
    }
}
