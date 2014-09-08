package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotFile;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotLevel;

import java.io.IOException;
import java.util.Collection;

public interface SnapshotFileService {

    Collection<SnapshotFile> findAll(String instanceId, String participantId, String courseId, String exerciseId, String snapshotId, SnapshotLevel level) throws IOException;
    SnapshotFile find(String instanceId, String participantId, String courseId, String exerciseId, String snapshotId, String fileId, SnapshotLevel level) throws IOException;
    String findContent(String instanceId, String participantId, String courseId, String exerciseId, String snapshotId, String fileId, SnapshotLevel level) throws IOException;

}
