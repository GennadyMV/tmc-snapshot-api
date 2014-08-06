package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotEvent;
import fi.helsinki.cs.tmc.snapshot.api.util.EventReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${spyware.chunkSize}")
    private int spywareChunkSize;

    private List<byte[]> fetchData(final String index, final String instance, final String username) throws IOException {


        final String[] indexLines = index.split("\\n");

        final int[] lastRange = indexRange(indexLines[indexLines.length - 1]);
        final int endIndex = lastRange[0] + lastRange[1];

        final byte[] byteData = new byte[endIndex];

        int i = 0;

        LOG.info("Fetching Spyware-data for {} from instance {} with length {}...",
                    username,
                    instance,
                    endIndex);

        while (i < endIndex) {

            final byte[] bytes = spywareServer.fetchChunk(instance, username, i, Math.min(i + spywareChunkSize, endIndex));

            for (byte b : bytes) {
                byteData[i++] = b;
            }
        }

        final List<byte[]> chunks = new ArrayList<>();

        for (String line : indexLines) {
            final int[] range = indexRange(line);
            chunks.add(Arrays.copyOfRange(byteData, range[0], range[0] + range[1]));
        }

        return chunks;
    }

    private int[] indexRange(final String indexLine) {

        final String[] indexes = indexLine.split("\\s+");

        return new int[] { Integer.parseInt(indexes[0]), Integer.parseInt(indexes[1]) };
    }

    @Override
    public Collection<SnapshotEvent> find(final String instance, final String username) throws IOException {

        LOG.info("Finding snapshots for {} from instance {}...", username, instance);

        // Fetch index
        final String index = spywareServer.fetchIndex(instance, username);

        // Fetch data
        final List<byte[]> content = fetchData(index, instance, username);

        // Read events from bytes
        final Collection<SnapshotEvent> events = eventReader.readEvents(content);

        LOG.info("Found " + events.size() + " events ...");

        return events;
    }

}
