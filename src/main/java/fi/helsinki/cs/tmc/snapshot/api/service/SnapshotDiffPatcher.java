package fi.helsinki.cs.tmc.snapshot.api.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.DiffMatchPatch;
import com.google.DiffMatchPatch.Patch;

import fi.helsinki.cs.tmc.snapshot.api.model.Metadata;
import fi.helsinki.cs.tmc.snapshot.api.model.Snapshot;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotEvent;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotEventInformation;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotFile;
import fi.helsinki.cs.tmc.snapshot.api.util.GZip;
import fi.helsinki.cs.tmc.snapshot.api.util.Zip;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.codec.binary.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

@Service
public final class SnapshotDiffPatcher implements SnapshotDiffPatchService {

    private static final Logger LOG = LoggerFactory.getLogger(SnapshotDiffPatcher.class);

    private final DiffMatchPatch patcher = new DiffMatchPatch();
    private Map<String, String> fileCache;

    @Override
    public List<Snapshot> patch(final List<byte[]> content) throws IOException {

        // Reset file cache
        fileCache = new TreeMap<>();

        // Read events from bytes
        final Collection<SnapshotEvent> events = readEvents(content);

        // Build files from patches
        processEvents(events);

        // Transform to snapshots
        final List<Snapshot> snapshots = asSnapshotList(events);

        // Build file continuums for exercises
        buildExerciseContinuum(snapshots);

        return snapshots;
    }

    private Collection<SnapshotEvent> readEvents(final List<byte[]> data) throws IOException {

        LOG.info("Building events from {} chunks of raw data", data.size());

        final Set<SnapshotEvent> events = new TreeSet<>();

        for (byte[] compressed : data) {

            // Decompress .dat content
            final byte[] decompressed = GZip.decompress(compressed);

            // Generate events from decompressed string data
            final Collection<SnapshotEvent> generatedEvents = getEventsFromString(new String(decompressed, "UTF-8"));

            if (generatedEvents != null) {
                events.addAll(generatedEvents);
            }
        }

        LOG.info("Done building events.");

        return events;
    }

    private List<SnapshotEvent> getEventsFromString(final String eventsJson) throws UnsupportedEncodingException {

        LOG.info("Parsing events from JSON...");

        try {

            final List<SnapshotEvent> eventsList = new ArrayList<>();

            final ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            final SnapshotEvent[] events = mapper.readValue(eventsJson, SnapshotEvent[].class);

            for (SnapshotEvent event : events) {

                if (!event.isProjectActionEvent()) {
                    eventsList.add(event);
                }
            }

            LOG.info("Done parsing events.");

            return eventsList;

        } catch (IOException exception) {
            return null;
        }
    }

    private void processEvents(final Collection<SnapshotEvent> events) throws UnsupportedEncodingException {

        LOG.info("Processing {} events", events.size());

        for (SnapshotEvent event : events) {
            if (event.getEventType().equals("code_snapshot")) {
                processCompleteSnapshot(event);
            } else {
                patchFile(event);
            }
        }

        LOG.info("Events processed.");
    }

    private void patchFile(final SnapshotEvent event) throws UnsupportedEncodingException {

        final ObjectMapper mapper = new ObjectMapper();
        final byte[] decodedData = Base64.decodeBase64(event.getData());
        SnapshotEventInformation information;

        try {
            information = mapper.readValue(new String(decodedData, "UTF-8"), SnapshotEventInformation.class);
        } catch (IOException exception) {
            return;
        }

        // No patches to apply
        if (information.getPatches() == null || information.getPatches().isEmpty()) {
            return;
        }

        // Parse patches
        final List<Patch> patches = patcher.patch_fromText(information.getPatches());

        // Current file content from cache
        final String currentContent = fileCache.containsKey(information.getFile()) ? fileCache.get(information.getFile()) : "";

        // Apply patches to content
        final String updatedContent = (String) patcher.patch_apply(new LinkedList(patches), currentContent)[0];
        fileCache.put(information.getFile(), updatedContent);

        // Save content to specific event
        event.getFiles().put(information.getFile(), updatedContent);
    }

    private void processCompleteSnapshot(final SnapshotEvent event) throws UnsupportedEncodingException {

        processData(event);
        processMetadata(event);
    }

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

        final ObjectMapper mapper = new ObjectMapper();
        final Metadata metadata;

        try {
            metadata = mapper.readValue(event.getMetadata(), Metadata.class);
        } catch (IOException ex) {
            LOG.info("Unable to parse metadata for event {}:  {}.", event.getHappenedAt(), ex.getMessage());
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

    private List<Snapshot> asSnapshotList(final Collection<SnapshotEvent> events) {

        LOG.info("Converting events to snapshots.");

        final List<Snapshot> snapshots = new ArrayList<>();

        for (SnapshotEvent event : events) {

            // Only process complete snapshots of type file_delete
            if (event.getEventType().equals("code_snapshot")) {
                if (!event.getMetadata().contains("file_delete")) {

                    continue;
                }
            }

            final Map<String, SnapshotFile> files = new HashMap<>();

            for (Entry<String, String> entry : event.getFiles().entrySet()) {
                files.put(entry.getKey(), new SnapshotFile(entry.getKey(), entry.getValue()));
            }

            final boolean isComplete = event.getEventType().equals("code_snapshot");
            snapshots.add(new Snapshot(Long.parseLong(event.getHappenedAt()), event.getCourseName(), event.getExerciseName(), files, isComplete));
        }

        LOG.info("Done converting events.");

        return snapshots;
    }

    private void buildExerciseContinuum(final List<Snapshot> snapshots) {

        LOG.info("Building exercise continuums...");

        final Map<String, Snapshot> cache = new HashMap<>();

        for (Snapshot current : snapshots) {

            final String key = current.getCourse() + "-" + current.getExercise();

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
    }
}
