package fi.helsinki.cs.tmc.snapshot.api.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/participants", produces = "application/json")
public class SnapshotController {

    @RequestMapping(method = RequestMethod.GET, value = "/{participant}/snapshots")
    public String list(@PathVariable final Long participant) {

        return "Participant " + participant + "'s snapshots";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{participant}/snapshots/{snapshot}")
    public String read(@PathVariable final Long participant, @PathVariable final Long snapshot) {

        return "Participant " + participant + "'s snapshot " + snapshot;
    }
}
