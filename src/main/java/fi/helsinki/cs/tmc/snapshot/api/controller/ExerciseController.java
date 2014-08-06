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
@RequestMapping(value = "{instance}/participants/{userId}/courses/{courseId}/exercises", produces = "application/json")
public final class ExerciseController {

    @Autowired
    private ExerciseService exerciseService;

    @RequestMapping(method = RequestMethod.GET)
    public Collection<Exercise> list(@PathVariable final String instance,
                                     @PathVariable final String userId,
                                     @PathVariable final String courseId) throws IOException {

        return exerciseService.findAll(instance, userId, courseId);
    }

    @RequestMapping(method = RequestMethod.GET, value = "{exerciseId}")
    public Exercise read(@PathVariable final String instance,
                         @PathVariable final String userId,
                         @PathVariable final String courseId,
                         @PathVariable final String exerciseId) throws IOException {

        return exerciseService.find(instance, userId, courseId, exerciseId);
    }
}
