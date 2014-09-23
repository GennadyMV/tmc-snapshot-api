package fi.helsinki.cs.tmc.snapshot.api.controller;

import fi.helsinki.cs.tmc.snapshot.api.service.ExerciseService;
import fi.helsinki.cs.tmc.snapshot.api.spyware.model.SnapshotEvent;

import java.io.IOException;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "{instanceId}/participants/{participantId}/courses/{courseId}/exercises/{exerciseId}/snapshots",
                params = "level=raw",
                produces = "application/json")
public final class RawSnapshotController {

    @Autowired
    private ExerciseService exerciseService;

    @RequestMapping(method = RequestMethod.GET)
    public Collection<SnapshotEvent> list(@PathVariable final String instanceId,
                                          @PathVariable final String participantId,
                                          @PathVariable final String courseId,
                                          @PathVariable final String exerciseId) throws IOException {

        return exerciseService.find(instanceId, participantId, courseId, exerciseId).getSnapshotEvents();
    }
}
