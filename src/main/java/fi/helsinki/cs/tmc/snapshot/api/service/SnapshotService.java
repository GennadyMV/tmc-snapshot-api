package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.app.ApiException;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotEvent;

import java.util.Collection;

public interface SnapshotService {

    Collection<SnapshotEvent> findAll(String instance, String username) throws ApiException;
    SnapshotEvent find(String instance, String username, Long id) throws ApiException;

}
