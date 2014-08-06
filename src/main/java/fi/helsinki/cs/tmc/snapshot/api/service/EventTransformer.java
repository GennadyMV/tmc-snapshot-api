package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.model.Snapshot;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotEvent;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotFile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

@Service
public final class EventTransformer {

    private static final Logger LOG = LoggerFactory.getLogger(EventTransformer.class);

    public List<Snapshot> toSnapshotList(final Collection<SnapshotEvent> events) {

        if (events == null) {
            return new ArrayList<>();
        }

        final List<Snapshot> snapshots = toFileSnapshots(events);
        //toExerciseSnapshots(snapshots);

        return snapshots;
    }

    private List<Snapshot> toFileSnapshots(final Collection<SnapshotEvent> events) {

        LOG.info("Converting events to snapshots...");

        final List<Snapshot> snapshots = new ArrayList<>();

        for (SnapshotEvent event : events) {

            // Only process complete snapshots of type file_delete
            if (event.getEventType().equals("code_snapshot")) {
                if (event.getMetadata() != null && !event.getMetadata().contains("file_delete")) {
                    continue;
                }
            }

            final Map<String, SnapshotFile> files = new HashMap<>();

            for (Map.Entry<String, String> entry : event.getFiles().entrySet()) {
                files.put(entry.getKey(), new SnapshotFile(entry.getKey(), entry.getValue()));
            }

            final boolean isComplete = event.getEventType().equals("code_snapshot");

//            snapshots.add(new Snapshot(event.getHappenedAt(),
//                                       course,
//                                       exercise,
//                                       files,
//                                       isComplete));
        }

        LOG.info("Done converting events.");

        return snapshots;
    }
    /*
    private void toExerciseSnapshots(final List<Snapshot> snapshots) {

        LOG.info("Building exercise continuums...");

        final Map<String, Snapshot> cache = new HashMap<>();

        for (Snapshot current : snapshots) {

            final String key = current.;

            // Complete snapshots are already complete, no need to parse previous.
            // Also skip if current snapshot is the first from this exercise.
            if (!current.isFromCompleteSnapshot() && cache.containsKey(key)) {
                final Snapshot previous = cache.get(key);

                for (SnapshotFile file : previous.getFiles()) {
                    if (current.getFile(file.getPath()) == null) {
                        current.addFile(file);
                    }
                }
            }
            cache.put(key, current);
        }

        LOG.info("Done building exercise continuums.");
    }*/
}
