package fi.helsinki.cs.tmc.snapshot.api.service;

import java.io.IOException;

public interface SpywareService {

    byte[] fetchChunkByInstanceAndId(String instance, String username, int i, int min) throws IOException;
    String fetchIndexByInstanceAndId(final String instance, final String username) throws IOException;

}
