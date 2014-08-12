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
    public String find(final String instance,
                       final String userId,
                       final String courseId,
                       final String exerciseId,
                       final String snapshotId,
                       final String path) throws IOException {

        final Snapshot snapshot = snapshotService.find(instance, userId, courseId, exerciseId, snapshotId);

        if (snapshot.getFile(path) == null) {
            throw new NotFoundException();
        }

        final String content = snapshot.getFile(path).getContent();

        if (content == null) {
            throw new NotFoundException();
        }

        return content;
    }
}
