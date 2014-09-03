package fi.helsinki.cs.tmc.snapshot.api.util;

import fi.helsinki.cs.tmc.snapshot.api.model.Snapshot;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotEvent;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotFile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.tomcat.util.codec.binary.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

@Component
public final class EventTransformer {

    private static final Logger LOG = LoggerFactory.getLogger(EventTransformer.class);

    private void removeEmptySnapshotsFromStart(final List<Snapshot> snapshots) {

        final Iterator<Snapshot> iterator = snapshots.iterator();

        while (iterator.hasNext()) {

            final Snapshot snapshot = iterator.next();

            if (snapshot.getFiles().isEmpty()) {
                LOG.info("Removed snapshot with ID {} for having no files.", snapshot.getId());
                iterator.remove();
            } else {
                return;
            }
        }
    }

    private List<Snapshot> toFileSnapshots(final Collection<SnapshotEvent> events) {

        LOG.info("Converting events to snapshots...");

        final List<Snapshot> snapshots = new ArrayList<>();

        for (SnapshotEvent event : events) {

            final Map<String, SnapshotFile> files = new HashMap<>();

            for (Map.Entry<String, String> entry : event.getFiles().entrySet()) {

                final byte[] byteId = (entry.getKey() +
                                       Long.toString(event.getHappenedAt()) +
                                       Long.toString(event.getSystemNanotime())).getBytes();

                final String id = Base64.encodeBase64URLSafeString(byteId);

                files.put(entry.getKey(), new SnapshotFile(id, entry.getKey(), entry.getValue()));
            }

            final boolean isComplete = event.isCodeSnapshot();

            snapshots.add(new Snapshot(event.getHappenedAt() + Long.toString(event.getSystemNanotime()),
                                       event.getHappenedAt(),
                                       files,
                                       isComplete));
        }

        LOG.info("Converted events.");

        return snapshots;
    }

    private void toExerciseSnapshots(final List<Snapshot> snapshots) {

        LOG.info("Building exercise continuums...");

        Snapshot previous = null;

        for (Snapshot current : snapshots) {

            // Complete snapshots are already complete, no need to parse previous.
            // Skip also if current snapshot is the first from this exercise, because
            // the first "file_save" event contains a complete snapshot of the file with
            // the exercise template, but the first "text_insert" is a patch from an empty
            // string to the exercise template. Not discarding the first complete snapshot
            // for empty files results in file content duplication.
            if (!current.isFromCompleteSnapshot() && previous != null) {

                for (SnapshotFile file : previous.getFiles()) {
                    if (current.getFileForPath(file.getPath()) == null) {
                        current.addFile(file);
                    }
                }
            }
            previous = current;
        }

        LOG.info("Built exercise continuums.");
    }

    public List<Snapshot> toSnapshotList(final Collection<SnapshotEvent> events) {

        if (events == null) {
            return new ArrayList<>();
        }

        final List<Snapshot> snapshots = toFileSnapshots(events);
        toExerciseSnapshots(snapshots);
        removeEmptySnapshotsFromStart(snapshots);

        return snapshots;
    }
}
