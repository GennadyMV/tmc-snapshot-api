package fi.helsinki.cs.tmc.snapshot.api.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/participants", produces = "application/json")
public final class ParticipantController {

    @RequestMapping(method = RequestMethod.GET)
    public String list() {

        return "Participants";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{participant}")
    public String read(@PathVariable final Long participant) {

        return "Participant " + participant;
    }
}
