package fi.helsinki.cs.tmc.snapshot.api.service;

import java.io.IOException;

public interface TmcService {

    String findUsername(String instance, long id) throws IOException;

}
