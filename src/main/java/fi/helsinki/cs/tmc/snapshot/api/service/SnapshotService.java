package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotEvent;

import java.util.Collection;

public interface SnapshotService {

    Collection<SnapshotEvent> findWithRange(String instance, String username) throws Exception;
    Collection<SnapshotEvent> findAll(String instance, String username) throws Exception;

}
