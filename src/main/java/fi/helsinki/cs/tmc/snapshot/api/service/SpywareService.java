package fi.helsinki.cs.tmc.snapshot.api.service;

import java.io.IOException;
import java.io.InputStream;

public interface SpywareService {

    byte[] fetchData(final String event, final String instance, final String username) throws IOException;
    InputStream fetchIndex(final String instance, final String username) throws IOException;

}
