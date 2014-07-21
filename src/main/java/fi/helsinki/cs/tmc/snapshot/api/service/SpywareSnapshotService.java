package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.app.ApiException;
import fi.helsinki.cs.tmc.snapshot.api.model.Snapshot;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.IOUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class SpywareSnapshotService implements SnapshotService {

    @Autowired
    private SnapshotDiffPatchService patchService;

    @Autowired
    private SpywareService spywareServer;

    private List<byte[]> findWithRange(final InputStream index,
                                       final String instance,
                                       final String username) throws ApiException {

        final List<byte[]> byteData = new ArrayList<>();

        // Convert to string
        final String indexData;

        try {
            indexData = IOUtils.toString(index);
            index.close();
        } catch (IOException ex) {
            throw new ApiException(ex);
        }


        // Split on newlines
        for (String event : indexData.split("\\n")) {
            byteData.add(spywareServer.getData(event, instance, username));
        }

        return byteData;
    }

    @Override
    public List<Snapshot> findAll(final String instance, final String username) throws ApiException {

        // Fetch index
        final InputStream index = spywareServer.getIndex(instance, username);

        // Fetch data
        final List<byte[]> content = findWithRange(index, instance, username);

        try {
            return patchService.patch(content);
        } catch (IOException exception) {
            throw new ApiException(exception);
        }
    }

    @Override
    public Snapshot find(final String instance, final String username, final Long id) throws ApiException {

        final Collection<Snapshot> events = findAll(instance, username);

        for (Snapshot event : events) {
            if (event.getId().equals(id)) {
                return event;
            }
        }

        return null;
    }
}
