package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.model.Event;

import java.io.IOException;
import java.util.Collection;

public interface SnapshotPatchService {

    Collection<Event> patch() throws IOException;

}
