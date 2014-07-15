package fi.helsinki.cs.tmc.snapshot.api.controller;

import fi.helsinki.cs.tmc.snapshot.api.model.Snapshot;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotEvent;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotFile;
import fi.helsinki.cs.tmc.snapshot.api.model.views.Views;
import fi.helsinki.cs.tmc.snapshot.api.service.SnapshotService;
import fi.helsinki.cs.tmc.snapshot.api.utilities.JsonViewWriter;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashMap;
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

    @RequestMapping(method = RequestMethod.GET, value = "{participant}/snapshots")
    public String list(@PathVariable final Long participant) {

        final Collection<SnapshotEvent> events;
        try {
            events = spywareService.findWithRange("/hy/", participant.toString());
        } catch (IOException | URISyntaxException ex) {
            Logger.getLogger(SnapshotController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        return JsonViewWriter.getView(events, Views.Summary.class);
    }

    @RequestMapping(method = RequestMethod.GET, value = "{participant}/snapshots/{snapshot}")
    public Snapshot read(@PathVariable final Long participant, @PathVariable final Long snapshot) {

        return new Snapshot(1L, new HashMap<String, SnapshotFile>());
    }
}
