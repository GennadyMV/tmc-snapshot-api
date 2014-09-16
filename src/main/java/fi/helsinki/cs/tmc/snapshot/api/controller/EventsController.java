package fi.helsinki.cs.tmc.snapshot.api.controller;

import fi.helsinki.cs.tmc.snapshot.api.model.Event;
import fi.helsinki.cs.tmc.snapshot.api.service.EventService;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "{instanceId}/participants/{participantId}/courses/{courseId}/exercises/{exerciseId}/events",
                produces = "application/json")
public final class EventsController {

    @Autowired
    private EventService eventService;

    @RequestMapping(method = RequestMethod.GET)
    public List<Event> list(@PathVariable final String instanceId,
                            @PathVariable final String participantId,
                            @PathVariable final String courseId,
                            @PathVariable final String exerciseId) throws IOException {

        return eventService.findAll(instanceId, participantId, courseId, exerciseId);
    }

    @RequestMapping(method = RequestMethod.GET, value = "{eventId}")
    public Event read(@PathVariable final String instanceId,
                      @PathVariable final String participantId,
                      @PathVariable final String courseId,
                      @PathVariable final String exerciseId,
                      @PathVariable final String eventId) throws IOException {

        return eventService.find(instanceId, participantId, courseId, exerciseId, eventId);
    }
}
