package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.model.Snapshot;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotLevel;

import java.io.IOException;
import java.util.List;

public interface SnapshotService {

    List<Snapshot> findAll(String instanceId, String participantId, String courseId, String exerciseId, SnapshotLevel level) throws IOException;
    Snapshot find(String instanceId, String participantId, String courseId, String exerciseId, String snapshotId, SnapshotLevel level) throws IOException;
    byte[] findFilesAsZip(String instanceId, String participantId, String courseId, String exerciseId, SnapshotLevel level, String from, int count) throws IOException;

}
