package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.model.Snapshot;

import java.io.IOException;
import java.util.List;

public interface SnapshotService {

    List<Snapshot> findAll(final String instance, final String username, final String course, final String exercise) throws IOException;
    Snapshot find(final String instance, final String username, final String course, final String exercise, final Long snapshot) throws IOException;

}
