package fi.helsinki.cs.tmc.snapshot.api.controller;

import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotFile;
import fi.helsinki.cs.tmc.snapshot.api.service.SnapshotFileService;

import java.io.IOException;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "{instance}/participants/{userId}/courses/{courseId}/exercises/{exerciseId}/snapshots/{snapshotId}/files", produces = "application/json")
public final class SnapshotFileController {

    @Autowired
    private SnapshotFileService snapshotFileService;

    @RequestMapping(method = RequestMethod.GET)
    public Collection<SnapshotFile> list(@PathVariable final String instance,
                               @PathVariable final String userId,
                               @PathVariable final String courseId,
                               @PathVariable final String exerciseId,
                               @PathVariable final String snapshotId) throws IOException {

        return snapshotFileService.findAll(instance, userId, courseId, exerciseId, snapshotId);
    }

    @RequestMapping(method = RequestMethod.GET,
                    value = "{fileId}",
                    produces = "text/plain")
    public String read(@PathVariable final String instance,
                       @PathVariable final String userId,
                       @PathVariable final String courseId,
                       @PathVariable final String exerciseId,
                       @PathVariable final String snapshotId,
                       @PathVariable final String fileId) throws IOException {

        return snapshotFileService.find(instance, userId, courseId, exerciseId, snapshotId, fileId);
    }
}
