package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.model.Participant;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotEvent;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.IOUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class SpywareSnapshotService implements SnapshotService {

    private static final Logger LOG = LoggerFactory.getLogger(SpywareSnapshotService.class);

    @Autowired
    private SnapshotDiffPatchService patchService;

    @Autowired
    private EventReader eventReader;

    @Autowired
    private SpywareService spywareServer;

    private List<byte[]> findWithRange(final InputStream index,
                                       final String instance,
                                       final String username) throws IOException  {

        final List<byte[]> byteData = new ArrayList<>();

        // Convert to string
        final String indexData;

        indexData = IOUtils.toString(index);
        index.close();

        // Split on newlines
        for (String event : indexData.split("\\n")) {
            byteData.add(spywareServer.fetchData(event, instance, username));
        }

        return byteData;
    }

    @Override
    public Participant find(final String instance, final String username) throws IOException {

        LOG.info("Finding snapshots for {} from instance {}...", username, instance);

        final Participant participant = new Participant(username);

        // Fetch index
        final InputStream index = spywareServer.fetchIndex(instance, username);

        // Fetch data
        final List<byte[]> content = findWithRange(index, instance, username);

        // Read events from bytes
        final Collection<SnapshotEvent> events = eventReader.readEvents(content);

        LOG.info("Found " + events.size() + " events ...");

        new SnapshotOrganizer().organize(participant, events);

        return participant;
    }
    /*
    @Override
    public Snapshot find(final String instance, final String username, final Long id) throws IOException {

        LOG.info("Finding snapshot for {} with id {} from instance {}...", username, id, instance);

        final Collection<Snapshot> events = findAll(instance, username);

        for (Snapshot event : events) {
            if (event.getId().equals(id)) {
                return event;
            }
        }

        return null;
    }
    */
}
