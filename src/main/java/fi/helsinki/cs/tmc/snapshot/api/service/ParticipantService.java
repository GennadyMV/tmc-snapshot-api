package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.model.Participant;

import java.io.IOException;

public interface ParticipantService {

    Participant findByInstanceAndId(String instance, String username) throws IOException;

}
