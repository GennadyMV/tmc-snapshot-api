package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotFile;

import java.io.IOException;
import java.util.Collection;

public interface SnapshotFileService {

    Collection<SnapshotFile> findAll(String instance, String userId, String courseId, String exerciseId, String snapshotId) throws IOException;
    SnapshotFile find(String instance, String userId, String courseId, String exerciseId, String snapshotId, String fileId) throws IOException;
    String findContent(String instance, String userId, String courseId, String exerciseId, String snapshotId, String fileId) throws IOException;

}
