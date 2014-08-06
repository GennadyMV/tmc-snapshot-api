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
                                            final String username,
                                            final String course,
                                            final String exercise,
                                            final Long snapshotId) throws IOException {

        final Collection<SnapshotFile> snapshotFiles = snapshotService.find(instance, username, course, exercise, snapshotId).getFiles();

        if (snapshotFiles == null) {
            throw new NotFoundException();
        }

        return snapshotFiles;
    }

    @Override
    public String find(final String instance,
                       final String username,
                       final String course,
                       final String exercise,
                       final Long snapshotId,
                       final String path) throws IOException {

        final Snapshot snapshot = snapshotService.find(instance, username, course, exercise, snapshotId);

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
