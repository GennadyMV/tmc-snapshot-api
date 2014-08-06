package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotFile;

import java.io.IOException;
import java.util.Collection;

public interface SnapshotFileService {

    Collection<SnapshotFile> findAll(String instance, String userId, String courseId, String exerciseId, Long snapshotId) throws IOException;
    String find(String instance, String userId, String courseId, String exerciseId, Long snapshotId, String path) throws IOException;

}
