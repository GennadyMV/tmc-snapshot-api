package fi.helsinki.cs.tmc.snapshot.api.controller;

import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotFile;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotLevel;
import fi.helsinki.cs.tmc.snapshot.api.service.SnapshotFileService;

import java.io.IOException;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "{instanceId}/participants/{participantId}/courses/{courseId}/exercises/{exerciseId}/snapshots/{snapshotId}/files", produces = "application/json")
public final class SnapshotFileController {

    @Autowired
    private SnapshotFileService snapshotFileService;

    @RequestMapping(method = RequestMethod.GET)
    public Collection<SnapshotFile> list(@PathVariable final String instanceId,
                                         @PathVariable final String participantId,
                                         @PathVariable final String courseId,
                                         @PathVariable final String exerciseId,
                                         @PathVariable final String snapshotId,
                                         @RequestParam(value = "level", defaultValue = "KEY", required = false) final String level) throws IOException {

        return snapshotFileService.findAll(instanceId, participantId, courseId, exerciseId, snapshotId, SnapshotLevel.fromString(level));
    }

    @RequestMapping(method = RequestMethod.GET, value = "{fileId}")
    public SnapshotFile read(@PathVariable final String instanceId,
                             @PathVariable final String participantId,
                             @PathVariable final String courseId,
                             @PathVariable final String exerciseId,
                             @PathVariable final String snapshotId,
                             @PathVariable final String fileId,
                             @RequestParam(value = "level", defaultValue = "KEY", required = false) final String level) throws IOException {

        return snapshotFileService.find(instanceId, participantId, courseId, exerciseId, snapshotId, fileId, SnapshotLevel.fromString(level));
    }

    @RequestMapping(method = RequestMethod.GET,
                    value = "{fileId}/content",
                    produces = "text/plain")
    public String readContent(@PathVariable final String instanceId,
                              @PathVariable final String participantId,
                              @PathVariable final String courseId,
                              @PathVariable final String exerciseId,
                              @PathVariable final String snapshotId,
                              @PathVariable final String fileId,
                              @RequestParam(value = "level", defaultValue = "KEY", required = false) final String level) throws IOException {

        return snapshotFileService.findContent(instanceId, participantId, courseId, exerciseId, snapshotId, fileId, SnapshotLevel.fromString(level));
    }
}
