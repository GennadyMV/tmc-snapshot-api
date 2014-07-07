package fi.helsinki.cs.tmc.snapshot.api.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.DiffMatchPatch;
import com.google.DiffMatchPatch.Patch;

import fi.helsinki.cs.tmc.snapshot.api.model.Event;
import fi.helsinki.cs.tmc.snapshot.api.model.EventInformation;
import fi.helsinki.cs.tmc.snapshot.api.utilities.GZip;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import org.springframework.stereotype.Service;

@Service
public final class SnapshotDiffMatchPatchService implements SnapshotPatchService {

    private final DiffMatchPatch patcher = new DiffMatchPatch();

    private Collection<Event> getEventsFromString(final String eventsJson) throws UnsupportedEncodingException {

        try {

            final List<Event> eventsList = new ArrayList<Event>();

            final ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            final Event[] events = mapper.readValue(eventsJson, Event[].class);

            for (Event event : events) {

                if (!event.isProjectActionEvent()) {
                    eventsList.add(event);
                }
            }

            return eventsList;

        } catch (IOException exception) {
            return null;
        }
    }

    private Collection<Event> readEvents(final String directory, final String studentName) throws IOException {

        final Set<Event> events = new TreeSet<Event>();
        final File indexFile = new File(directory, studentName + ".idx");

        // Index file not found
        if (!indexFile.exists()) {
            throw new RuntimeException("No entries for student " + studentName);
        }

        // Event content
        final String indexData = IOUtils.toString(new FileInputStream(indexFile));
        final byte[] content = IOUtils.toByteArray(new FileInputStream(new File(directory, studentName + ".dat")));

        for (String event : indexData.split("\\n")) {

            // Split on whitespace characters
            final String[] indexes = event.split("\\s+");
            final int start = Integer.parseInt(indexes[0]);
            final int length = Integer.parseInt(indexes[1]);

            // Copy content from .dat file with range
            final byte[] compressed = Arrays.copyOfRange(content, start, start + length);

            // Decompress .dat content
            final byte[] decompressed = GZip.decompress(compressed);

            // Generate events from decompressed string data
            final Collection<Event> generatedEvents = getEventsFromString(new String(decompressed, "UTF-8"));

            if (generatedEvents != null) {
                events.addAll(generatedEvents);
            }
        }

        return events;
    }

    private void patchFile(final Event event, final Map<String, String> files) throws UnsupportedEncodingException {

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
        List<Patch> patches = patcher.patch_fromText(information.getPatches());

        // Current file content
        String currentContent = files.containsKey(information.getFile()) ? files.get(information.getFile()) : "";

        // Apply patch to content
        String updatedContent = (String) patcher.patch_apply(new LinkedList(patches), currentContent)[0];
        files.put(information.getFile(), updatedContent);
    }

    @Override
    public Collection<Event> patch() throws IOException {

        final Collection<Event> events = readEvents("test-data", "012608144");
        final Map<String, String> files = new TreeMap<String, String>();

        for (Event event : events) {
            patchFile(event, files);
        }

        return events;
    }
}
