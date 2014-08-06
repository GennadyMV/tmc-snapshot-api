package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.model.Participant;

import java.io.IOException;

public interface SnapshotService {

    Participant find(String instance, String username) throws IOException;
    //Snapshot find(String instance, String username, Long id) throws IOException;

}
