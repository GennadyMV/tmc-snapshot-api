package fi.helsinki.cs.tmc.snapshot.api.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.DiffMatchPatch;
import com.google.DiffMatchPatch.Patch;

import fi.helsinki.cs.tmc.snapshot.api.model.EventInformation;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotEvent;
import fi.helsinki.cs.tmc.snapshot.api.utilities.GZip;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.codec.binary.Base64;

import org.springframework.stereotype.Service;

@Service
public final class SnapshotDiffMatchPatchService implements SnapshotPatchService {

    private final DiffMatchPatch patcher = new DiffMatchPatch();
    private final Map<String, String> fileCache = new TreeMap<>();

    private Collection<SnapshotEvent> getEventsFromString(final String eventsJson) throws UnsupportedEncodingException {

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

            return eventsList;

        } catch (IOException exception) {
            return null;
        }
    }

    private Collection<SnapshotEvent> readEvents(final List<byte[]> data) throws IOException {

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

        return events;
    }

    private void patchFile(final SnapshotEvent event) throws UnsupportedEncodingException {

        final ObjectMapper mapper = new ObjectMapper();
        final byte[] decodedData = Base64.decodeBase64(event.getData());
        EventInformation information;

        try {
            information = mapper.readValue(new String(decodedData, "UTF-8"), EventInformation.class);
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

    @Override
    public Collection<SnapshotEvent> patch(final List<byte[]> content) throws IOException {

        // Get events
        final Collection<SnapshotEvent> events = readEvents(content);

        // Patch files
        for (SnapshotEvent event : events) {
            patchFile(event);
        }

        return events;
    }
}
