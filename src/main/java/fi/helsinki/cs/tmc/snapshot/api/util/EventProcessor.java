package fi.helsinki.cs.tmc.snapshot.api.util;

import fi.helsinki.cs.tmc.snapshot.api.spyware.model.SnapshotEvent;

import java.util.Collection;

public interface EventProcessor {

    void process(Collection<SnapshotEvent> events);

}
