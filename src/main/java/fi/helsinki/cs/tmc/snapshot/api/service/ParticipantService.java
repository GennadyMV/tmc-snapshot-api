package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.model.Participant;

import java.io.IOException;
import java.util.Collection;

public interface ParticipantService {

    Collection<Participant> findAll(String instanceId) throws IOException;
    Participant find(String instanceId, String id) throws IOException;

}
