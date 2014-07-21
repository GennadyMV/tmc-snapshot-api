package fi.helsinki.cs.tmc.snapshot.api.controller;

import fi.helsinki.cs.tmc.snapshot.api.app.ApiException;
import fi.helsinki.cs.tmc.snapshot.api.model.Snapshot;
import fi.helsinki.cs.tmc.snapshot.api.service.SnapshotService;
import fi.helsinki.cs.tmc.snapshot.api.service.TmcService;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/participants", produces = "application/json")
public final class SnapshotController {

    @Autowired
    private SnapshotService snapshotService;

    @Autowired
    private TmcService tmcService;

    @RequestMapping(method = RequestMethod.GET, value = "{participant}/snapshots")
    public List<Snapshot> list(@PathVariable final Long participant) {

        try {
            final String username = tmcService.findUsername("", participant);
            return snapshotService.findAll("/hy/", username);
        } catch (ApiException exception) {
            Logger.getLogger(SnapshotController.class.getName()).log(Level.SEVERE, null, exception);
            return null;
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "{participant}/snapshots/{snapshot}")
    public Snapshot read(@PathVariable final Long participant, @PathVariable final Long snapshot) {

        try {
            final String username = tmcService.findUsername("", participant);
            return  snapshotService.find("/hy/", username, snapshot);
        } catch (ApiException exception) {
            Logger.getLogger(SnapshotController.class.getName()).log(Level.SEVERE, null, exception);
            return null;
        }
    }
}
