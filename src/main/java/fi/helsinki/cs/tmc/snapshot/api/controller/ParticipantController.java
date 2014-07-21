package fi.helsinki.cs.tmc.snapshot.api.controller;

import fi.helsinki.cs.tmc.snapshot.api.app.ApiException;
import fi.helsinki.cs.tmc.snapshot.api.model.Participant;
import fi.helsinki.cs.tmc.snapshot.api.service.SnapshotService;
import fi.helsinki.cs.tmc.snapshot.api.service.TmcService;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/participants", produces = "application/json")
public final class ParticipantController {

    @Autowired
    private TmcService tmcDataService;

    @Autowired
    private SnapshotService snapshotService;

    @RequestMapping(method = RequestMethod.GET, value = "{participant}")
    public Participant read(@PathVariable final Long participant) {

        try {
            final String username = tmcDataService.findUsername("", participant);
            return new Participant(participant, snapshotService.findAll("/hy/", username));
        } catch (ApiException ex) {
            Logger.getLogger(SnapshotController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
