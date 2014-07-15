package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotEvent;

import java.io.IOException;
import java.net.URISyntaxException;

import java.util.Collection;

public interface SnapshotService {

    Collection<SnapshotEvent> findWithRange(String instance, String username) throws IOException, URISyntaxException;

}
