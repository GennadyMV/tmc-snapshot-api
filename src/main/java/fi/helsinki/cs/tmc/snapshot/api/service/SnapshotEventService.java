package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.spyware.model.SnapshotEvent;

import java.io.IOException;
import java.util.Collection;

public interface SnapshotEventService {

    Collection<SnapshotEvent> findAll(String instance, String username) throws IOException;

}
