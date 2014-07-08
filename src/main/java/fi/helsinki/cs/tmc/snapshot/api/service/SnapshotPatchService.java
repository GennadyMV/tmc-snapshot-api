package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotEvent;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

public interface SnapshotPatchService {

    Collection<SnapshotEvent> patch(InputStream indexData, InputStream content) throws IOException;

}
