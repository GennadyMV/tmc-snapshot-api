package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.model.Snapshot;

import java.io.IOException;
import java.util.List;

public interface SnapshotService {

    List<Snapshot> findAll(final String instance, final String userId, final String courseId, final String exerciseId) throws IOException;
    Snapshot find(final String instance, final String userId, final String courseId, final String exerciseId, final String snapshotId) throws IOException;
    byte[] findAllFilesAsZip(final String instance, final String userId, final String courseId, final String exerciseId) throws IOException;

}
