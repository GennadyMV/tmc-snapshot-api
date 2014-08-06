package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.model.Participant;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotEvent;
import java.util.Collection;

public interface SnapshotOrganizerService {

    void organize(final Participant participant, final Collection<SnapshotEvent> snapshotEvents);
}
