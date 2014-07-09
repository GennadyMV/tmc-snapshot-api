package fi.helsinki.cs.tmc.snapshot.api.controller;

import fi.helsinki.cs.tmc.snapshot.api.model.Snapshot;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/participants", produces = "application/json")
public final class SnapshotController {

    @RequestMapping(method = RequestMethod.GET, value = "{participant}/snapshots")
    public List<Snapshot> list(@PathVariable final Long participant) {

        final List<Snapshot> snapshots = new ArrayList<>();
        snapshots.add(new Snapshot(1L, new HashMap<String, SnapshotFile>()));

        return snapshots;
    }

    @RequestMapping(method = RequestMethod.GET, value = "{participant}/snapshots/{snapshot}")
    public Snapshot read(@PathVariable final Long participant, @PathVariable final Long snapshot) {

        return new Snapshot(1L, new HashMap<String, SnapshotFile>());
    }
}
