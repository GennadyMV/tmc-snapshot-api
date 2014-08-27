package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.model.Snapshot;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotLevel;
import java.io.IOException;
import java.util.List;

public interface SnapshotService {

    List<Snapshot> findAll(String instance, String userId, String courseId, String exerciseId, SnapshotLevel level) throws IOException;
    List<Snapshot> findAll(String instance, String userId, String courseId, String exerciseId) throws IOException;
    Snapshot find(String instance, String userId, String courseId, String exerciseId, String snapshotId, SnapshotLevel level) throws IOException;
    Snapshot find(String instance, String userId, String courseId, String exerciseId, String snapshotId) throws IOException;
    byte[] findAllFilesAsZip(String instance, String userId, String courseId, String exerciseId, SnapshotLevel level) throws IOException;
    byte[] findAllFilesAsZip(String instance, String userId, String courseId, String exerciseId) throws IOException;

}
