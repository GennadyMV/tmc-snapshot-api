package fi.helsinki.cs.tmc.snapshot.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.DiffMatchPatch;

import fi.helsinki.cs.tmc.snapshot.api.model.Event;
import fi.helsinki.cs.tmc.snapshot.api.model.Metadata;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.IOUtils;

import org.springframework.stereotype.Service;

@Service
public final class SnapshotDiffMatchPatchService implements SnapshotPatchService {

    private DiffMatchPatch patcher = new DiffMatchPatch();

    private byte[] decompressGzip(final byte[] content) {

        final ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            IOUtils.copy(new GZIPInputStream(new ByteArrayInputStream(content)), output);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        return output.toByteArray();
    }

    private Collection<Event> getEventsFromString(final String eventsJson) throws UnsupportedEncodingException {

        try {

            final ObjectMapper mapper = new ObjectMapper();
            final Event[] events = mapper.readValue(eventsJson, Event[].class);

            for (Event event : events) {

                // Snapshot
                if (event.isCodeSnapshotEvent()) {
                    event.setMetadata(mapper.readValue(event.getMetadataAsString(), Metadata.class));
                }
            }

            return Arrays.asList(events);

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
            final byte[] decompressed = decompressGzip(compressed);

            // Generate events from decompressed string data
            final Collection<Event> generatedEvents = getEventsFromString(new String(decompressed, "UTF-8"));

            if (generatedEvents != null) {
                events.addAll(generatedEvents);
            }
        }

        return events;
    }

    @Override
    public Collection<Event> patch() throws IOException {

        final Collection<Event> events = readEvents("test-data", "perusoskari");

        return events;
    }
}
