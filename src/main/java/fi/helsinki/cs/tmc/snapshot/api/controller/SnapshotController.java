package fi.helsinki.cs.tmc.snapshot.api.controller;

import fi.helsinki.cs.tmc.snapshot.api.model.Snapshot;
import fi.helsinki.cs.tmc.snapshot.api.service.SnapshotService;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "{instance}/participants/{username}/courses/{course}/exercises/{exercise}/snapshots", produces = "application/json")
public final class SnapshotController {

    @Autowired
    private SnapshotService snapshotService;

    @RequestMapping(method = RequestMethod.GET)
    public List<Snapshot> list(@PathVariable final String instance,
                               @PathVariable final String username,
                               @PathVariable final String course,
                               @PathVariable final String exercise) throws IOException {

        return snapshotService.find(instance, username, course, exercise);
    }

    @RequestMapping(method = RequestMethod.GET, value = "{snapshot}")
    public Snapshot read(@PathVariable final String instance,
                         @PathVariable final String username,
                         @PathVariable final String course,
                         @PathVariable final String exercise,
                         @PathVariable final Long snapshot) throws IOException {

        return snapshotService.find(instance, username, course, exercise, snapshot);
    }
}
