package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.exception.NotFoundException;
import fi.helsinki.cs.tmc.snapshot.api.model.Snapshot;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotFile;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotLevel;
import fi.helsinki.cs.tmc.snapshot.api.spyware.model.SnapshotEvent;
import fi.helsinki.cs.tmc.snapshot.api.util.EventTransformer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class DefaultSnapshotService implements SnapshotService {

    @Autowired
    private EventProcessorService eventProcessorService;

    @Autowired
    private ExerciseService exerciseService;

    @Autowired
    private EventTransformer eventTransformer;

    private List<Snapshot> limitSnapshots(final List<Snapshot> snapshots, final String from, final int count) {

        if (from.isEmpty() && count == 0) {
            return snapshots;
        }

        final List<Snapshot> limited = new ArrayList<>();

        for (int i = 0; i < snapshots.size(); i++) {

            if (!from.isEmpty() && !snapshots.get(i).getId().equals(from)) {
                continue;
            }

            for (int j = i; j < Math.min(i + count, snapshots.size()); j++) {
                limited.add(snapshots.get(j));
            }

            break;
        }

        return limited;
    }

    @Override
    public List<Snapshot> findAll(final String instanceId,
                                  final String participantId,
                                  final String courseId,
                                  final String exerciseId,
                                  final SnapshotLevel level) throws IOException {

        final Collection<SnapshotEvent> events = exerciseService.find(instanceId, participantId, courseId, exerciseId)
                                                                .getSnapshotEvents();

        eventProcessorService.processEvents(events, level);

        final List<Snapshot> snapshots = eventTransformer.toSnapshotList(events);

        if (snapshots == null) {
            return Collections.EMPTY_LIST;
        }

        return snapshots;
    }

    @Override
    public Snapshot find(final String instanceId,
                         final String participantId,
                         final String courseId,
                         final String exerciseId,
                         final String snapshotId,
                         final SnapshotLevel level) throws IOException {

        final List<Snapshot> snapshots = findAll(instanceId, participantId, courseId, exerciseId, level);

        for (Snapshot snapshot : snapshots) {
            if (snapshot.getId().equals(snapshotId)) {
                return snapshot;
            }
        }

        throw new NotFoundException();
    }

    @Override
    public byte[] findFilesAsZip(final String instanceId,
                                 final String participantId,
                                 final String courseId,
                                 final String exerciseId,
                                 final SnapshotLevel level,
                                 final String from,
                                 final int count) throws IOException {

        final List<Snapshot> snapshots = findAll(instanceId, participantId, courseId, exerciseId, level);
        final List<Snapshot> limitedList = limitSnapshots(snapshots, from, count);

        final ByteArrayOutputStream files = new ByteArrayOutputStream();
        final ZipOutputStream zip = new ZipOutputStream(files);

        for (Snapshot snapshot : limitedList) {

            zip.putNextEntry(new ZipEntry(snapshot.getId() + "/"));

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
