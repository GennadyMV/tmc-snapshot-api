package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.model.Snapshot;
import java.io.IOException;
import java.util.List;

public interface SnapshotService {

    List<Snapshot> findAll(String instance, String username) throws IOException;
    Snapshot find(String instance, String username, Long id) throws IOException;

}
