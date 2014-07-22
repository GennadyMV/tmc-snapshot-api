package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.model.TmcParticipant;

import java.io.IOException;
import java.util.List;

public interface TmcService {

    String findUsername(String instance, long id) throws IOException;

    List<TmcParticipant> all(String instance) throws IOException;

}
