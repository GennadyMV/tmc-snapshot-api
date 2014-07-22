package fi.helsinki.cs.tmc.snapshot.api.controller;

import fi.helsinki.cs.tmc.snapshot.api.exception.NotFoundException;
import fi.helsinki.cs.tmc.snapshot.api.model.Participant;
import fi.helsinki.cs.tmc.snapshot.api.model.TmcParticipant;
import fi.helsinki.cs.tmc.snapshot.api.service.SnapshotService;
import fi.helsinki.cs.tmc.snapshot.api.service.TmcService;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "{instance}/participants", produces = "application/json")
public final class ParticipantController {

    @Autowired
    private TmcService tmcService;

    @Autowired
    private SnapshotService snapshotService;

    @RequestMapping(method = RequestMethod.GET)
    public List<TmcParticipant> list(@PathVariable final String instance) throws IOException {

        return tmcService.findAll(instance);
    }

    @RequestMapping(method = RequestMethod.GET, value = "{participant}")
    public Participant read(@PathVariable final String instance, @PathVariable final Long participant) throws IOException {

        final String username = tmcService.findUsernameById(instance, participant);

        if (username == null) {
            throw new NotFoundException();
        }

        return new Participant(participant, snapshotService.findAll(instance, username));
    }
}
