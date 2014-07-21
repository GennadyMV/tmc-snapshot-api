package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.app.ApiException;
import fi.helsinki.cs.tmc.snapshot.api.model.Snapshot;

import java.util.List;


public interface SnapshotService {

    List<Snapshot> findAll(String instance, String username) throws ApiException;
    Snapshot find(String instance, String username, Long id) throws ApiException;

}
