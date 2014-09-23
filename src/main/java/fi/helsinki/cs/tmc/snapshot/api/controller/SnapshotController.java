package fi.helsinki.cs.tmc.snapshot.api.controller;

import fi.helsinki.cs.tmc.snapshot.api.model.Snapshot;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotLevel;
import fi.helsinki.cs.tmc.snapshot.api.service.ExerciseService;
import fi.helsinki.cs.tmc.snapshot.api.service.SnapshotService;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "{instanceId}/participants/{participantId}/courses/{courseId}/exercises/{exerciseId}/snapshots",
                produces = "application/json")
public final class SnapshotController {

    @Autowired
    private SnapshotService snapshotService;

    @Autowired
    private ExerciseService exerciseService;

    @RequestMapping(method = RequestMethod.GET)
    public Object list(@PathVariable final String instanceId,
                               @PathVariable final String participantId,
                               @PathVariable final String courseId,
                               @PathVariable final String exerciseId,
                               @RequestParam(value = "level", defaultValue = "KEY", required = false) final String level) throws IOException {

        final SnapshotLevel snapshotLevel = SnapshotLevel.fromString(level);

        if (snapshotLevel == SnapshotLevel.RAW) {

            return exerciseService.find(instanceId, participantId, courseId, exerciseId).getSnapshotEvents();
        }

        return snapshotService.findAll(instanceId, participantId, courseId, exerciseId, snapshotLevel);
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
