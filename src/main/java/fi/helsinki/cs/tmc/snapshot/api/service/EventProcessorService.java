package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotEvent;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotLevel;
import java.util.Collection;

public interface EventProcessorService {

    void processEvents(Collection<SnapshotEvent> events, SnapshotLevel level);

}
