package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.model.Course;
import fi.helsinki.cs.tmc.snapshot.api.model.Exercise;
import fi.helsinki.cs.tmc.snapshot.api.model.Participant;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotEvent;

import java.util.Collection;

import org.springframework.stereotype.Service;

@Service
public final class DefaultSnapshotOrganiserService implements SnapshotOrganiserService {

    @Override
    public void organise(final Participant participant, final Collection<SnapshotEvent> snapshotEvents) {

        for (SnapshotEvent event : snapshotEvents) {

            // Course

            Course course = participant.getCourse(event.getCourseName());

            if (course == null) {
                course = new Course(event.getCourseName());
                participant.addCourse(course);
            }

            // Exercise

            Exercise exercise = course.getExercise(event.getExerciseName());

            if (exercise == null) {
                exercise = new Exercise(event.getExerciseName());
                course.addExercise(exercise);
            }

            exercise.addSnapshotEvent(event);
        }
    }
}
