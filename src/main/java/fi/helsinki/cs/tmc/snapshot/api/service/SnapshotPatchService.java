package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotEvent;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public interface SnapshotPatchService {

    Collection<SnapshotEvent> patch(List<byte[]> content) throws IOException;

}
