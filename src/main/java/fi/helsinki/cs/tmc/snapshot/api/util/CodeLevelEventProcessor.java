package fi.helsinki.cs.tmc.snapshot.api.util;

import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotEvent;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

@Component
public final class CodeLevelEventProcessor implements EventProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(KeyLevelEventProcessor.class);

    private void processSnapshot(final SnapshotEvent previousEvent, final SnapshotEvent event) throws IOException {

        final byte[] decodedData = Base64.decodeBase64(event.getData());
        final Map<String, byte[]> data;

        try {
            data = Zip.decompress(decodedData);
        } catch (IOException exception) {
            return;
        }

        boolean hasChanges = false;

        for (String filename : data.keySet()) {

            final String fileKey = filename.replaceAll(event.getExerciseName(), "");

            if (fileKey.endsWith("/")) {
                continue;
            }

            final String fileContent = new String(data.get(filename), "UTF-8");
            event.getFiles().put(fileKey, fileContent);

            if (hasChanges) {
                continue;
            }

            if (previousEvent == null) {
                hasChanges = true;
                continue;
            }

            if (!fileContent.equals(previousEvent.getFiles().get(fileKey))) {
                hasChanges = true;
            }
        }

        // This is somehow pretty ugly
        if (event.getFiles().isEmpty() ||
            (previousEvent != null && !hasChanges && previousEvent.getFiles().size() == event.getFiles().size())) {
            throw new IOException("Nothing new in ZIP.");
        }
    }

    @Override
    public void process(final Collection<SnapshotEvent> events) {

        LOG.info("Processing {} events...", events.size());

        final Iterator<SnapshotEvent> iterator = events.iterator();

        SnapshotEvent previous = null;

        while (iterator.hasNext()) {

            final SnapshotEvent event = iterator.next();

            if (!event.isCodeSnapshot()) {
                iterator.remove();
                continue;
            }

            try {

                processSnapshot(previous, event);
                previous = event;

            } catch (IOException exception) {

                iterator.remove();

                LOG.warn("Filtering snapshot due to: {}. Duplicate content for course {} exercise {} snapshot {}{}.",
                         exception.getMessage(),
                         event.getCourseName(),
                         event.getExerciseName(),
                         event.getHappenedAt(),
                         event.getSystemNanotime());
            }

        }

        LOG.info("Events processed.");
    }
}
