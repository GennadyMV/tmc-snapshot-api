package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.exception.NotFoundException;
import fi.helsinki.cs.tmc.snapshot.api.model.Snapshot;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotFile;

import java.io.IOException;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class DefaultSnapshotFileService implements SnapshotFileService {

    @Autowired
    private SnapshotService snapshotService;

    @Override
    public Collection<SnapshotFile> findAll(final String instance,
                                            final String userId,
                                            final String courseId,
                                            final String exerciseId,
                                            final String snapshotId) throws IOException {

        final Collection<SnapshotFile> snapshotFiles = snapshotService.find(instance, userId, courseId, exerciseId, snapshotId).getFiles();

        if (snapshotFiles == null) {
            throw new NotFoundException();
        }

        return snapshotFiles;
    }

    @Override
    public SnapshotFile find(final String instance,
                             final String userId,
                             final String courseId,
                             final String exerciseId,
                             final String snapshotId,
                             final String fileId) throws IOException {

        final Snapshot snapshot = snapshotService.find(instance, userId, courseId, exerciseId, snapshotId);

        if (snapshot.getFile(fileId) == null) {
            throw new NotFoundException();
        }

        return snapshot.getFile(fileId);
    }

    @Override
    public String findContent(final String instance,
                              final String userId,
                              final String courseId,
                              final String exerciseId,
                              final String snapshotId,
                              final String fileId) throws IOException {

        final SnapshotFile file = find(instance, userId, courseId, exerciseId, snapshotId, fileId);

        final String content = file.getContent();

        if (content == null) {
            throw new NotFoundException();
        }

        return content;
    }
}
