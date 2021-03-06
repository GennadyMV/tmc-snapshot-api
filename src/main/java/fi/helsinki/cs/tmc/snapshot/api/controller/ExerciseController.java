package fi.helsinki.cs.tmc.snapshot.api.controller;

import fi.helsinki.cs.tmc.snapshot.api.model.Exercise;
import fi.helsinki.cs.tmc.snapshot.api.service.ExerciseService;

import java.io.IOException;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "{instanceId}/participants/{participantId}/courses/{courseId}/exercises",
                produces = "application/json")
public final class ExerciseController {

    @Autowired
    private ExerciseService exerciseService;

    @RequestMapping(method = RequestMethod.GET)
    public Collection<Exercise> list(@PathVariable final String instanceId,
                                     @PathVariable final String participantId,
                                     @PathVariable final String courseId) throws IOException {

        return exerciseService.findAll(instanceId, participantId, courseId);
    }

    @RequestMapping(method = RequestMethod.GET, value = "{exerciseId}")
    public Exercise read(@PathVariable final String instanceId,
                         @PathVariable final String participantId,
                         @PathVariable final String courseId,
                         @PathVariable final String exerciseId) throws IOException {

        return exerciseService.find(instanceId, participantId, courseId, exerciseId);
    }
}
