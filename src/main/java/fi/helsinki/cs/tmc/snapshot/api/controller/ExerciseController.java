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
@RequestMapping(value = "{instance}/participants/{username}/courses/{course}/exercises", produces = "application/json")
public class ExerciseController {

    @Autowired
    private ExerciseService exerciseService;

    @RequestMapping(method = RequestMethod.GET)
    public Collection<Exercise> list(@PathVariable final String instance,
                                     @PathVariable final String username,
                                     @PathVariable final String course) throws IOException {

        return exerciseService.find(instance, username, course);
    }

    @RequestMapping(method = RequestMethod.GET, value = "{exercise}")
    public Exercise read(@PathVariable final String instance,
                         @PathVariable final String username,
                         @PathVariable final String course,
                         @PathVariable final String exercise) throws IOException {

        return exerciseService.find(instance, username, course, exercise);
    }
}
