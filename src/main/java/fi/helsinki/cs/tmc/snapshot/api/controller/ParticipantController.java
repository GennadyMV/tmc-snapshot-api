package fi.helsinki.cs.tmc.snapshot.api.controller;

import fi.helsinki.cs.tmc.snapshot.api.model.Participant;
import fi.helsinki.cs.tmc.snapshot.api.model.Snapshot;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/participants", produces = "application/json")
public final class ParticipantController {

    @RequestMapping(method = RequestMethod.GET)
    public List<Participant> list() {

        final List<Participant> participants = new ArrayList<>();
        participants.add(new Participant(1L, new ArrayList<Snapshot>()));

        return participants;
    }

    @RequestMapping(method = RequestMethod.GET, value = "{participant}")
    public Participant read(@PathVariable final Long participant) {

        return new Participant(1L, new ArrayList<Snapshot>());
    }
}
