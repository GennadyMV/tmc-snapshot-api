package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.exception.NotFoundException;
import fi.helsinki.cs.tmc.snapshot.api.model.Snapshot;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotEvent;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotFile;
import fi.helsinki.cs.tmc.snapshot.api.util.EventTransformer;
import fi.helsinki.cs.tmc.snapshot.api.util.KeyLevelEventProcessor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class DefaultSnapshotService implements SnapshotService {

    @Autowired
    private KeyLevelEventProcessor eventProcessor;

    @Autowired
    private ExerciseService exerciseService;

    @Autowired
    private EventTransformer eventTransformer;

    @Override
    public List<Snapshot> findAll(final String instance,
                                  final String userId,
                                  final String courseId,
                                  final String exerciseId) throws IOException {

        final Collection<SnapshotEvent> events = exerciseService.find(instance, userId, courseId, exerciseId)
                                                                .getSnapshotEvents();

        eventProcessor.process(events);

        final List<Snapshot> snapshots = eventTransformer.toSnapshotList(events);

        if (snapshots == null) {
            throw new NotFoundException();
        }

        return snapshots;
    }

    @Override
    public Snapshot find(final String instance,
                         final String userId,
                         final String courseId,
                         final String exerciseId,
                         final String snapshotId) throws IOException {

        final List<Snapshot> snapshots = findAll(instance, userId, courseId, exerciseId);

        for (Snapshot snapshot : snapshots) {
            if (snapshot.getId().equals(snapshotId)) {
                return snapshot;
            }
        }

        throw new NotFoundException();
    }

    @Override
    public byte[] findAllFilesAsZip(final String instance,
                                     final String userId,
                                     final String courseId,
                                     final String exerciseId) throws IOException {

        final List<Snapshot> snapshots = findAll(instance, userId, courseId, exerciseId);

        final ByteArrayOutputStream files = new ByteArrayOutputStream();
        final ZipOutputStream zip = new ZipOutputStream(files);

        for (Snapshot snapshot : snapshots) {

            for (SnapshotFile file : snapshot.getFiles()) {

                final ZipEntry entry = new ZipEntry(String.format("%s/%s", snapshot.getId(), file.getId()));

                zip.putNextEntry(entry);
                zip.write(file.getContent().getBytes());

                zip.closeEntry();
            }
        }

        zip.close();
        files.close();

        return files.toByteArray();
    }
}
