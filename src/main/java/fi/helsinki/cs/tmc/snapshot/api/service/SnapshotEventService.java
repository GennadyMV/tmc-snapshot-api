package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotEvent;

import java.io.IOException;
import java.util.Collection;

public interface SnapshotEventService {

    Collection<SnapshotEvent> findAll(final String instance, final String username) throws IOException;

}
