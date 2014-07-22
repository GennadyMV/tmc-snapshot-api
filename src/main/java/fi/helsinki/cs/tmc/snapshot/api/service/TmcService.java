package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.model.TmcParticipant;

import java.io.IOException;
import java.util.List;

public interface TmcService {

    List<TmcParticipant> findAll(String instance) throws IOException;
    String findByUsername(String instance, long id) throws IOException;

}
