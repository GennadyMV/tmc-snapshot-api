package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.exception.NotFoundException;
import fi.helsinki.cs.tmc.snapshot.api.model.Snapshot;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotFile;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotLevel;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class DefaultSnapshotFileService implements SnapshotFileService {

    @Autowired
    private SnapshotService snapshotService;

    @Override
    public Collection<SnapshotFile> findAll(final String instanceId,
                                            final String participantId,
                                            final String courseId,
                                            final String exerciseId,
                                            final String snapshotId,
                                            final SnapshotLevel level) throws IOException {

        final Collection<SnapshotFile> snapshotFiles = snapshotService.find(instanceId, participantId, courseId, exerciseId, snapshotId, level)
                                                                      .getFiles();

        if (snapshotFiles == null) {
            return Collections.EMPTY_LIST;
        }

        return snapshotFiles;
    }

    @Override
    public SnapshotFile find(final String instanceId,
                             final String participantId,
                             final String courseId,
                             final String exerciseId,
                             final String snapshotId,
                             final String fileId,
                             final SnapshotLevel level) throws IOException {

        final Snapshot snapshot = snapshotService.find(instanceId, participantId, courseId, exerciseId, snapshotId, level);

        final SnapshotFile file = snapshot.getFileForId(fileId);

        if (file == null) {
            throw new NotFoundException();
        }

        return file;
    }

    @Override
    public String findContent(final String instanceId,
                              final String participantId,
                              final String courseId,
                              final String exerciseId,
                              final String snapshotId,
                              final String fileId,
                              final SnapshotLevel level) throws IOException {

        final SnapshotFile file = find(instanceId, participantId, courseId, exerciseId, snapshotId, fileId, level);

        final String content = file.getContent();

        if (content == null) {
            throw new NotFoundException();
        }

        return content;
    }
}
