package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotEvent;

import java.util.Collection;

public interface SnapshotService {

    Collection<SnapshotEvent> findAll(String directory, String username) throws Exception;

}
