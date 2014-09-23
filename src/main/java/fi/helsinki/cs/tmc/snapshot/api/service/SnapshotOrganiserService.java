package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.model.Participant;
import fi.helsinki.cs.tmc.snapshot.api.spyware.model.SnapshotEvent;

import java.util.Collection;

public interface SnapshotOrganiserService {

    void organise(Participant participant, Collection<SnapshotEvent> snapshotEvents);

}
