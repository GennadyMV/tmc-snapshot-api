package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotEvent;
import fi.helsinki.cs.tmc.snapshot.api.util.EventReader;

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
public class SnapshotEventServiceImpl implements SnapshotEventService {

    private static final Logger LOG = LoggerFactory.getLogger(SnapshotEventServiceImpl.class);

    @Autowired
    private SnapshotDiffPatchService patchService;

    @Autowired
    private EventReader eventReader;

    @Autowired
    private SpywareService spywareServer;

    private List<byte[]> retrieveInChunks(final InputStream index,
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
    public Collection<SnapshotEvent> find(final String instance, final String username) throws IOException {

        LOG.info("Finding snapshots for {} from instance {}...", username, instance);

        // Fetch index
        final InputStream index = spywareServer.fetchIndex(instance, username);

        // Fetch data
        final List<byte[]> content = retrieveInChunks(index, instance, username);

        // Read events from bytes
        final Collection<SnapshotEvent> events = eventReader.readEvents(content);

        LOG.info("Found " + events.size() + " events ...");

        return events;
    }

}
