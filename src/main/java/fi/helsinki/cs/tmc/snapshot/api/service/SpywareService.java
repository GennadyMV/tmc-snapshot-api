package fi.helsinki.cs.tmc.snapshot.api.service;

import java.io.IOException;

public interface SpywareService {

    String fetchParticipants(String instance) throws IOException;
    String fetchIndex(String instance, String username) throws IOException;
    byte[] fetchChunkByRange(String instance, String username, int start, int end) throws IOException;

}
