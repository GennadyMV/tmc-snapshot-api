package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotEvent;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.IOUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class SpywareSnapshotService implements SnapshotService {

    @Autowired
    private SnapshotPatchService patchService;

    @Autowired
    private ServerDataService spywareServer;

    private List<byte[]> findWithRange(final InputStream index,
                                       final String instance,
                                       final String username) throws IOException, URISyntaxException {

        final List<byte[]> byteData = new ArrayList<>();

        // Convert to string
        final String indexData = IOUtils.toString(index);
        index.close();

        // Split on newlines
        for (String event : indexData.split("\\n")) {
            byteData.add(spywareServer.getData(event, instance, username));
        }

        return byteData;
    }

    @Override
    public Collection<SnapshotEvent> findWithRange(final String instance, final String username) throws IOException,
                                                                                                        URISyntaxException {

        // Fetch index
        final InputStream index = spywareServer.getIndex(instance, username);

        // Fetch data
        final List<byte[]> content = findWithRange(index, instance, username);

        return patchService.patch(content);
    }
}
