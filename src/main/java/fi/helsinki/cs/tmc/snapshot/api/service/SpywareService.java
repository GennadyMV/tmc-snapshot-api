package fi.helsinki.cs.tmc.snapshot.api.service;

import java.io.IOException;

public interface SpywareService {

    byte[] fetchData(final String event, final String instance, final String username) throws IOException;
    String fetchIndex(final String instance, final String username) throws IOException;

}
