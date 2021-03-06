package fi.helsinki.cs.tmc.snapshot.api.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.DiffMatchPatch;

import fi.helsinki.cs.tmc.snapshot.api.spyware.model.SnapshotEvent;
import fi.helsinki.cs.tmc.snapshot.api.spyware.model.SnapshotEventInformation;
import fi.helsinki.cs.tmc.snapshot.api.spyware.model.SnapshotEventMetadata;

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
public final class KeyLevelEventProcessor implements EventProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(KeyLevelEventProcessor.class);

    private Map<String, String> fileCache;
    private final ObjectMapper mapper = new ObjectMapper();
    private final DiffMatchPatch patcher = new DiffMatchPatch();

    private boolean processData(final SnapshotEvent event) throws UnsupportedEncodingException {

        final byte[] decodedData = Base64.decodeBase64(event.getData());
        final Map<String, byte[]> data;

        final Map<String, String> zipFileCache = new HashMap<>();

        try {
            data = Zip.decompress(decodedData);
        } catch (IOException exception) {
            return false;
        }

        boolean hasChanged = false;

        for (String filename : data.keySet()) {

            if (filename.endsWith("/")) {
                continue;
            }

            final String fileKey = filename.substring(filename.indexOf("/") + 1);
            final String fileContent = new String(data.get(filename), "UTF-8");

            if (!fileContent.equals(fileCache.get(fileKey))) {
                hasChanged = true;
            }
            zipFileCache.put(fileKey, fileContent);
            event.getFiles().put(fileKey, fileContent);
        }

        if (zipFileCache.size() != fileCache.size()) {
            hasChanged = true;
        }

        // Replace old cache
        fileCache = zipFileCache;

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

        // Always try to process both data and metadata
        if (!processData(event) & !processMetadata(event)) {
            throw new IOException("Nothing new in ZIP");
        }
    }

    private boolean containsNoPatches(final SnapshotEventInformation information) {

        return information == null || information.getPatches() == null || information.getPatches().isEmpty();
    }

    private void patchFile(final SnapshotEvent event) throws IOException {

        final byte[] decodedData = Base64.decodeBase64(event.getData());
        SnapshotEventInformation information;

        try {
            information = mapper.readValue(new String(decodedData, "UTF-8"), SnapshotEventInformation.class);
        } catch (IOException exception) {
            throw new IOException("Unreadable data");
        }

        if (containsNoPatches(information)) {
            throw new IOException("No patch data available");
        }

        List<DiffMatchPatch.Patch> patches = null;

        // Parse patches
        try {
            patches = patcher.patch_fromText(information.getPatches());
        } catch (IllegalArgumentException exception) {
            throw new IOException(exception.getMessage());
        }

        final String fileKey = information.getFile().startsWith("/") ? information.getFile().substring(1) : information.getFile();

        final String currentContent = !information.isFullDocument() && fileCache.containsKey(fileKey) ?
                                      fileCache.get(fileKey) : "";

        // Apply patches to content
        final String updatedContent = (String) patcher.patch_apply(new LinkedList(patches), currentContent)[0];

        if (currentContent.equals(updatedContent)) {
            throw new IOException("Corrupted patch data.");
        }

        fileCache.put(fileKey, updatedContent);

        // Save content to specific event
        event.getFiles().put(fileKey, updatedContent);
    }

    private void processSnapshotEvent(final SnapshotEvent event) throws IOException {

        if (event.isCodeSnapshot()) {
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
