package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.model.Participant;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotEvent;

import java.util.Collection;

public interface SnapshotOrganiserService {

    void organise(final Participant participant, final Collection<SnapshotEvent> snapshotEvents);

}
