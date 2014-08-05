package fi.helsinki.cs.tmc.snapshot.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.DiffMatchPatch;

import fi.helsinki.cs.tmc.snapshot.api.model.Metadata;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotEvent;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotEventInformation;
import fi.helsinki.cs.tmc.snapshot.api.util.Zip;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

@Service
public final class EventProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(EventProcessor.class);

    private Map<String, String> fileCache;
    private final ObjectMapper mapper = new ObjectMapper();
    private final DiffMatchPatch patcher = new DiffMatchPatch();

    private void processData(final SnapshotEvent event) throws UnsupportedEncodingException {

        final byte[] decodedData = Base64.decodeBase64(event.getData());
        final Map<String, byte[]> data;

        try {
            data = Zip.decompress(decodedData);
        } catch (IOException ex) {
            return;
        }

        for (String filename : data.keySet()) {

            final String fileKey = filename.replaceAll(event.getExerciseName(), "");
            final String fileContent = new String(data.get(filename), "UTF-8");

            if (!fileKey.endsWith("/") && fileCache.containsKey(fileKey)) {
                fileCache.put(fileKey, fileContent);
                event.getFiles().put(fileKey, fileContent);
            }
        }
    }

    private void processMetadata(final SnapshotEvent event) {

        final Metadata metadata;

        try {
            metadata = mapper.readValue(event.getMetadata(), Metadata.class);
        } catch (IOException | NullPointerException exception) {
            LOG.info("Unable to parse metadata for event {}:  {}.", event.getHappenedAt(), exception.getMessage());
            return;
        }

        if (metadata == null) {
            return;
        }

        final String file = metadata.getFile();

        if (metadata.getCause().equals("file_delete")) {
            fileCache.remove(file);
        }
    }

    private void processCompleteSnapshot(final SnapshotEvent event) throws UnsupportedEncodingException {

        processData(event);
        processMetadata(event);
    }

    private void patchFile(final SnapshotEvent event) throws UnsupportedEncodingException {

        final byte[] decodedData = Base64.decodeBase64(event.getData());
        SnapshotEventInformation information;

        try {
            information = mapper.readValue(new String(decodedData, "UTF-8"), SnapshotEventInformation.class);
        } catch (IOException exception) {
            return;
        }

        // No patches to apply
        if (information == null || information.getPatches() == null || information.getPatches().isEmpty()) {
            return;
        }

        // Parse patches
        final List<DiffMatchPatch.Patch> patches = patcher.patch_fromText(information.getPatches());

        // Current file content from cache
        final String currentContent = fileCache.containsKey(information.getFile()) ? fileCache.get(information.getFile()) : "";

        // Apply patches to content
        final String updatedContent = (String) patcher.patch_apply(new LinkedList(patches), currentContent)[0];
        fileCache.put(information.getFile(), updatedContent);

        // Save content to specific event
        event.getFiles().put(information.getFile(), updatedContent);
    }

    public void process(final Collection<SnapshotEvent> events) throws UnsupportedEncodingException {

        LOG.info("Processing {} events...", events.size());

        fileCache = new HashMap<>();

        for (SnapshotEvent event : events) {
            if (event.getEventType().equals("code_snapshot")) {
                processCompleteSnapshot(event);
            } else {
                patchFile(event);
            }
        }

        LOG.info("Events processed.");
    }
}
