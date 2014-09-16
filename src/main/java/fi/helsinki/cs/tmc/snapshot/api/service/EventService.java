package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.model.Event;

import java.io.IOException;
import java.util.List;

public interface EventService {

    List<Event> findAll(String instanceId, String participantId, String courseId, String exerciseId) throws IOException;
    Event find(String instanceId, String participantId, String courseId, String exerciseId, String eventId) throws IOException;

}
