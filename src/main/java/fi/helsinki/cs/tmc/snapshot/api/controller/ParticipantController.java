package fi.helsinki.cs.tmc.snapshot.api.controller;

import com.fasterxml.jackson.annotation.JsonView;
import fi.helsinki.cs.tmc.snapshot.api.model.Participant;
import fi.helsinki.cs.tmc.snapshot.api.service.ParticipantService;

import java.io.IOException;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "{instanceId}/participants", produces = "application/json")
public final class ParticipantController {

    @Autowired
    private ParticipantService participantService;

    @RequestMapping(method = RequestMethod.GET)
    @JsonView(Participant.Default.class)
    public Collection<Participant> list(@PathVariable final String instanceId) throws IOException {

        return participantService.findAll(instanceId);
    }

    @RequestMapping(method = RequestMethod.GET, value = "{participantId}")
    @JsonView(Participant.WithCourses.class)
    public Participant read(@PathVariable final String instanceId, @PathVariable final String participantId) throws IOException {

        return participantService.find(instanceId, participantId);
    }
}
