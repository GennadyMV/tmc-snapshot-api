package fi.helsinki.cs.tmc.snapshot.api.controller;

import fi.helsinki.cs.tmc.snapshot.api.model.Participant;
import fi.helsinki.cs.tmc.snapshot.api.model.SimpleParticipant;
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
    public Collection<SimpleParticipant> list(@PathVariable final String instanceId) throws IOException {

        return participantService.findAll(instanceId);
    }

    @RequestMapping(method = RequestMethod.GET, value = "{participantId}")
    public Participant read(@PathVariable final String instanceId, @PathVariable final String participantId) throws IOException {

        return participantService.find(instanceId, participantId);
    }
}
