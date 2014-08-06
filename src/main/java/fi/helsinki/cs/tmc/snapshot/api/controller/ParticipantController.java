package fi.helsinki.cs.tmc.snapshot.api.controller;

import fi.helsinki.cs.tmc.snapshot.api.model.Course;
import fi.helsinki.cs.tmc.snapshot.api.model.Exercise;
import fi.helsinki.cs.tmc.snapshot.api.model.Participant;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotEvent;
import fi.helsinki.cs.tmc.snapshot.api.model.TmcParticipant;
import fi.helsinki.cs.tmc.snapshot.api.service.SnapshotService;
import fi.helsinki.cs.tmc.snapshot.api.service.TmcService;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "{instance}/participants", produces = "application/json")
public final class ParticipantController {

    @Autowired
    private TmcService tmcService;

    @Autowired
    private SnapshotService snapshotService;

    @RequestMapping(method = RequestMethod.GET)
    public List<TmcParticipant> list(@PathVariable final String instance) throws IOException {

        return tmcService.findAll(instance);
    }

    @RequestMapping(method = RequestMethod.GET, value = "{username}")
    public Participant read(@PathVariable final String instance, @PathVariable final String username) throws IOException {

        return snapshotService.find(instance, username);
    }

    @RequestMapping(method = RequestMethod.GET, value = "{username}/courses")
    public Collection<Course> listCourses(@PathVariable final String instance, @PathVariable final String username) throws IOException {

        return snapshotService.find(instance, username).getCourses();
    }

    @RequestMapping(method = RequestMethod.GET, value = "{username}/courses/{courseName}/exercises")
    public Collection<Exercise> listCourseExercises(@PathVariable final String instance, @PathVariable final String username, @PathVariable final String courseName) throws IOException {

        return snapshotService.find(instance, username).getCourse(courseName).getExercises();
    }

    @RequestMapping(method = RequestMethod.GET, value = "{username}/courses/{courseName}/exercises/{exerciseName}/snapshots")
    public Collection<SnapshotEvent> listExerciseSnapshotEvents(@PathVariable final String instance, @PathVariable final String username, @PathVariable final String courseName, @PathVariable final String exerciseName) throws IOException {

        return snapshotService.find(instance, username).getCourse(courseName).getExercise(exerciseName).getSnapshotEvents();
    }

}
