package fi.helsinki.cs.tmc.snapshot.api.service;

import java.io.IOException;

public interface SpywareService {

    String fetchIndex(final String instance, final String username) throws IOException;
    byte[] fetchChunkByRange(String instance, String username, int start, int end) throws IOException;

}
