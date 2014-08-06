package fi.helsinki.cs.tmc.snapshot.api.service;

import java.io.IOException;

public interface SpywareService {

    byte[] fetchChunk(String instance, String username, int i, int min) throws IOException;
    String fetchIndex(final String instance, final String username) throws IOException;

}
