package fi.helsinki.cs.tmc.snapshot.api.controller;

import fi.helsinki.cs.tmc.snapshot.api.model.Snapshot;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotLevel;
import fi.helsinki.cs.tmc.snapshot.api.service.SnapshotService;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "{instanceId}/participants/{participantId}/courses/{courseId}/exercises/{exerciseId}/snapshots",
                params = "level!=raw",
                produces = "application/json")
public final class SnapshotController {

    @Autowired
    private SnapshotService snapshotService;

    @RequestMapping(method = RequestMethod.GET)
    public List<Snapshot> list(@PathVariable final String instanceId,
                               @PathVariable final String participantId,
                               @PathVariable final String courseId,
                               @PathVariable final String exerciseId,
                               @RequestParam(value = "level", defaultValue = "KEY", required = false) final String level) throws IOException {

        return snapshotService.findAll(instanceId, participantId, courseId, exerciseId, SnapshotLevel.fromString(level));
    }

    @RequestMapping(method = RequestMethod.GET, value = "{snapshotId}")
    public Snapshot read(@PathVariable final String instanceId,
                         @PathVariable final String participantId,
                         @PathVariable final String courseId,
                         @PathVariable final String exerciseId,
                         @PathVariable final String snapshotId,
                         @RequestParam(value = "level", defaultValue = "KEY", required = false) final String level) throws IOException {

        return snapshotService.find(instanceId, participantId, courseId, exerciseId, snapshotId, SnapshotLevel.fromString(level));
    }

    @RequestMapping(method = RequestMethod.GET,
                    value = "files.zip",
                    produces = "application/zip")
    public HttpEntity<byte[]> readFiles(@PathVariable final String instanceId,
                                        @PathVariable final String participantId,
                                        @PathVariable final String courseId,
                                        @PathVariable final String exerciseId,
                                        @RequestParam(value = "level", defaultValue = "KEY", required = false) final String level) throws IOException {

        final byte[] zip = snapshotService.findAllFilesAsZip(instanceId, participantId, courseId, exerciseId, SnapshotLevel.fromString(level));
        return new HttpEntity<>(zip);
    }
}
