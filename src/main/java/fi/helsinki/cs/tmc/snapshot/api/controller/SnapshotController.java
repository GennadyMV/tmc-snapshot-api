package fi.helsinki.cs.tmc.snapshot.api.controller;

import fi.helsinki.cs.tmc.snapshot.api.app.ApiException;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotEvent;
import fi.helsinki.cs.tmc.snapshot.api.model.view.View;
import fi.helsinki.cs.tmc.snapshot.api.service.SnapshotService;
import fi.helsinki.cs.tmc.snapshot.api.service.TmcDataService;
import fi.helsinki.cs.tmc.snapshot.api.util.JsonViewWriter;

import java.util.Collection;
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
    private SnapshotService spywareService;

    @Autowired
    private TmcDataService tmcService;

    @RequestMapping(method = RequestMethod.GET, value = "{participant}/snapshots")
    public String list(@PathVariable final Long participant) {

        final Collection<SnapshotEvent> events;

        try {
            final String username = tmcService.findUsername("", participant);
            events = spywareService.findAll("/hy/", username);
        } catch (ApiException ex) {
            Logger.getLogger(SnapshotController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        return JsonViewWriter.getView(events, View.IdOnly.class);
    }

    @RequestMapping(method = RequestMethod.GET, value = "{participant}/snapshots/{snapshot}")
    public String read(@PathVariable final Long participant, @PathVariable final Long snapshot) {

        final SnapshotEvent event;

        try {
            final String username = tmcService.findUsername("", participant);
            event = spywareService.find("/hy/", username, snapshot);
        } catch (ApiException ex) {
            Logger.getLogger(SnapshotController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        return JsonViewWriter.getView(event, View.Complete.class);
    }
}
