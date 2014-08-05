package fi.helsinki.cs.tmc.snapshot.api.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotEvent;
import fi.helsinki.cs.tmc.snapshot.api.util.GZip;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

@Service
public class EventReader {

    private static final Logger LOG = LoggerFactory.getLogger(EventReader.class);

    private final ObjectMapper mapper = new ObjectMapper();

    @PostConstruct
    private void initialise() {

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public Collection<SnapshotEvent> readEvents(final List<byte[]> data) throws IOException {

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

        try {
            final List<SnapshotEvent> eventsList = new ArrayList<>();
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

}
