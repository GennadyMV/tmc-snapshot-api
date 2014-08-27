package fi.helsinki.cs.tmc.snapshot.api.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.DiffMatchPatch;

import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotEvent;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotEventInformation;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotEventMetadata;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

@Component
public final class KeyLevelEventProcessor implements EventProsessor {

    private static final Logger LOG = LoggerFactory.getLogger(KeyLevelEventProcessor.class);

    private Map<String, String> fileCache;
    private final ObjectMapper mapper = new ObjectMapper();
    private final DiffMatchPatch patcher = new DiffMatchPatch();

    private boolean processData(final SnapshotEvent event) throws UnsupportedEncodingException {

        final byte[] decodedData = Base64.decodeBase64(event.getData());
        final Map<String, byte[]> data;

        try {
            data = Zip.decompress(decodedData);
        } catch (IOException exception) {
            return false;
        }

        boolean hasChanged = false;

        for (String filename : data.keySet()) {

            final String fileKey = filename.replaceAll(event.getExerciseName(), "");
            final String fileContent = new String(data.get(filename), "UTF-8");

            if (!fileKey.endsWith("/") && fileCache.containsKey(fileKey)) {

                if (!fileContent.equals(fileCache.get(fileKey))) {
                    hasChanged = true;
                    fileCache.put(fileKey, fileContent);
                }

                event.getFiles().put(fileKey, fileContent);
            }
        }

        return hasChanged;
    }

    private boolean processMetadata(final SnapshotEvent event) {

        final SnapshotEventMetadata metadata;

        try {
            metadata = mapper.readValue(event.getMetadata(), SnapshotEventMetadata.class);
        } catch (IOException | NullPointerException exception) {
            LOG.info("Unable to parse metadata for event {}: {}.", event.getHappenedAt(), exception.getMessage());
            return false;
        }

        if (metadata == null) {
            return false;
        }

        final String file = metadata.getFile();

        if (metadata.getCause().equals("file_delete")) {
            fileCache.remove(file);
            return true;
        }
        return false;
    }

    private void processCompleteSnapshot(final SnapshotEvent event) throws IOException {

        if (!processData(event) && !processMetadata(event)) {
            throw new IOException("Nothing new in zip");
        }
    }

    private void patchFile(final SnapshotEvent event) throws IOException {

        final byte[] decodedData = Base64.decodeBase64(event.getData());
        SnapshotEventInformation information;

        try {
            information = mapper.readValue(new String(decodedData, "UTF-8"), SnapshotEventInformation.class);
        } catch (IOException exception) {
            throw new IOException("Unreadable data");
        }

        // No patches to apply
        if (information == null || information.getPatches() == null || information.getPatches().isEmpty()) {
            throw new IOException("No patch data available");
        }

        // Parse patches
        final List<DiffMatchPatch.Patch> patches = patcher.patch_fromText(information.getPatches());

        // Current file content from cache
        final String currentContent = fileCache.containsKey(information.getFile()) ? fileCache.
                get(information.getFile()) : "";

        // Apply patches to content
        final String updatedContent = (String) patcher.patch_apply(new LinkedList(patches), currentContent)[0];

        if (currentContent.equals(updatedContent)) {
            throw new IOException("Corrupted patch data");
        }

        fileCache.put(information.getFile(), updatedContent);

        // Save content to specific event
        event.getFiles().put(information.getFile(), updatedContent);
    }

    private void processSnapshotEvent(final SnapshotEvent event) throws IOException {

        if (event.getEventType().equals("code_snapshot")) {
            processCompleteSnapshot(event);
        } else {
            patchFile(event);
        }
    }

    @Override
    public void process(final Collection<SnapshotEvent> events) {

        LOG.info("Processing {} events...", events.size());

        fileCache = new HashMap<>();

        final Iterator<SnapshotEvent> iterator = events.iterator();

        while (iterator.hasNext()) {

            final SnapshotEvent event = iterator.next();

            try {

                processSnapshotEvent(event);
            } catch (IOException exception) {

                iterator.remove();

                LOG.warn(
                        "Filtering snapshot due to {}. Duplicate content for course {} exercise {} snapshot {}{}.",
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
