package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.model.Snapshot;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotEvent;
import fi.helsinki.cs.tmc.snapshot.api.util.EventProcessor;
import fi.helsinki.cs.tmc.snapshot.api.util.EventReader;
import fi.helsinki.cs.tmc.snapshot.api.util.EventTransformer;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class SnapshotDiffPatcher implements SnapshotDiffPatchService {

    private static final Logger LOG = LoggerFactory.getLogger(SnapshotDiffPatcher.class);

    @Autowired
    private EventReader eventReader;

    @Autowired
    private EventProcessor eventProcessor;

    @Autowired
    private EventTransformer eventTransformer;

    @Override
    public List<Snapshot> patch(final List<byte[]> content) throws IOException {

        LOG.info("Start patching events...");

        // Read events from bytes
        final Collection<SnapshotEvent> events = eventReader.readEvents(content);

        // Build files from patches
        eventProcessor.process(events);

        // Transform to snapshots
        final List<Snapshot> snapshots = eventTransformer.toSnapshotList(events);

        LOG.info("Done patching events.");

        return snapshots;
    }
}
