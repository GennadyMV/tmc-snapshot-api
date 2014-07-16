package fi.helsinki.cs.tmc.snapshot.api.app;

public class ApiException extends Exception {

    public ApiException(final Throwable t) {
        super(t);
    }

    public ApiException(final String msg) {
        super(msg);
    }
}
