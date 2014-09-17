package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.exception.NotFoundException;
import fi.helsinki.cs.tmc.snapshot.api.model.Event;
import fi.helsinki.cs.tmc.snapshot.api.model.Exercise;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Exercise.class)
public class DefaultEventServiceTest {

    private static final String INSTANCE = "hy";
    private static final String USER = "testUsername";
    private static final String COURSE = "testCourseName";
    private static final String EXERCISE = "testExerciseName";

    @Mock
    private final Exercise exercise = new Exercise("exercise");

    @Mock
    private ExerciseService exerciseService;

    @InjectMocks
    private DefaultEventService eventService;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);
    }

    private SnapshotEvent createEvent(final String courseName,
                                      final String exerciseName,
                                      final Long happenedAt,
                                      final Long systemNanotime,
                                      final String eventType) {

        final SnapshotEvent event = new SnapshotEvent();

        event.setCourseName(courseName);
        event.setExerciseName(exerciseName);
        event.setHappenedAt(happenedAt);
        event.setSystemNanotime(systemNanotime);
        event.setEventType(eventType);

        return event;
    }

    @Test
    public void shouldFindEvent() throws IOException {

        final List<SnapshotEvent> events = new ArrayList<>();

        final SnapshotEvent event1 = createEvent("mooc", "ex1", 100L, 102L, "text_insert");
        final SnapshotEvent event2 = createEvent("mooc", "ex2", 104L, 106L, "file_create");

        events.add(event1);
        events.add(event2);

        when(exerciseService.find(INSTANCE, USER, COURSE, EXERCISE)).thenReturn(exercise);
        when(exercise.getSnapshotEvents()).thenReturn(events);

        final Event result = eventService.find(INSTANCE, USER, COURSE, EXERCISE, "104106");

        assertNotNull(result);
        assertEquals("104106", result.getId());
        assertEquals("file_create", result.getEventType());
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowExceptionOnIncorrectId() throws IOException {

        final List<SnapshotEvent> events = new ArrayList<>();

        final SnapshotEvent event1 = createEvent("mooc", "ex1", 100L, 102L, "text_insert");
        final SnapshotEvent event2 = createEvent("mooc", "ex2", 104L, 106L, "file_create");

        events.add(event1);
        events.add(event2);

        when(exerciseService.find(INSTANCE, USER, COURSE, EXERCISE)).thenReturn(exercise);
        when(exercise.getSnapshotEvents()).thenReturn(events);

        eventService.find(INSTANCE, USER, COURSE, EXERCISE, "404");
    }

    @Test
    public void shouldFindAllEvents() throws IOException {

        final List<SnapshotEvent> snapshotEvents = new ArrayList<>();

        final SnapshotEvent event1 = createEvent("hy", "ex10", 100L, 102L, "text_remove");
        final SnapshotEvent event2 = createEvent("hy", "ex20", 104L, 106L, "file_delete");

        snapshotEvents.add(event1);
        snapshotEvents.add(event2);

        when(exerciseService.find(INSTANCE, USER, COURSE, EXERCISE)).thenReturn(exercise);
        when(exercise.getSnapshotEvents()).thenReturn(snapshotEvents);

        final List<Event> events = eventService.findAll(INSTANCE, USER, COURSE, EXERCISE);

        assertEquals(2, events.size());

        assertEquals("100102", events.get(0).getId());
        assertEquals("text_remove", events.get(0).getEventType());

        assertEquals("104106", events.get(1).getId());
        assertEquals("file_delete", events.get(1).getEventType());
    }
}
