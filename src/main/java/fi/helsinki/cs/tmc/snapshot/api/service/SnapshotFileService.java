package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotFile;

import java.io.IOException;
import java.util.Collection;

public interface SnapshotFileService {

    Collection<SnapshotFile> findAll(String instance, String username, String course, String exercise, Long snapshot) throws IOException;
    String find(String instance, String username, String course, String exercise, Long snapshot, String path) throws IOException;

}
