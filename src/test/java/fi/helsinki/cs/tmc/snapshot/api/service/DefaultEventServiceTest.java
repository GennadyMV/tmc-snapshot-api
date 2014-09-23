package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.exception.NotFoundException;
import fi.helsinki.cs.tmc.snapshot.api.model.Event;
import fi.helsinki.cs.tmc.snapshot.api.model.Exercise;
import fi.helsinki.cs.tmc.snapshot.api.spyware.model.SnapshotEvent;

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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Exercise.class)
public final class DefaultEventServiceTest {

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
                                      final String eventType,
                                      final String metadata) {

        final SnapshotEvent event = new SnapshotEvent();

        event.setCourseName(courseName);
        event.setExerciseName(exerciseName);
        event.setHappenedAt(happenedAt);
        event.setSystemNanotime(systemNanotime);
        event.setEventType(eventType);
        event.setMetadata(metadata);

        return event;
    }

    private void configure(final String courseName,
                           final String exerciseName,
                           final Long happenedAt,
                           final Long systemNanotime,
                           final String eventType,
                           final String metadata) throws IOException {

        final List<SnapshotEvent> events = new ArrayList<>();

        final SnapshotEvent event = createEvent(courseName, exerciseName, happenedAt, systemNanotime, eventType, metadata);

        events.add(event);

        when(exerciseService.find(INSTANCE, USER, COURSE, EXERCISE)).thenReturn(exercise);
        when(exercise.getSnapshotEvents()).thenReturn(events);

    }

    @Test
    public void shouldFindEvent() throws IOException {

        configure("mooc", "ex1", 100L, 102L, "text_insert", "{\"metadata\":\"data\"}");

        final Event result = eventService.find(INSTANCE, USER, COURSE, EXERCISE, "100102");

        assertNotNull(result);
        assertEquals("100102", result.getId());
        assertEquals("text_insert", result.getEventType());
        assertFalse(result.getMetadata().isEmpty());
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowExceptionOnIncorrectId() throws IOException {

        configure("mooc", "ex1", 100L, 102L, "text_insert", null);

        eventService.find(INSTANCE, USER, COURSE, EXERCISE, "404");
    }

    @Test
    public void shouldFindAllEvents() throws IOException {

        configure("hy", "ex10", 100L, 102L, "text_remove", null);

        final List<Event> events = eventService.findAll(INSTANCE, USER, COURSE, EXERCISE);

        assertEquals(1, events.size());

        assertEquals("100102", events.get(0).getId());
        assertEquals("text_remove", events.get(0).getEventType());
    }

    @Test
    public void shouldReturnEmptyMapOnInvalidMetadata() throws IOException {

        final List<SnapshotEvent> snapshotEvents = new ArrayList<>();

        snapshotEvents.add(createEvent("event", "metadata", 50L, 55L, "invalid", "falseData"));

        when(exerciseService.find(INSTANCE, USER, COURSE, EXERCISE)).thenReturn(exercise);
        when(exercise.getSnapshotEvents()).thenReturn(snapshotEvents);

        final Event event = eventService.find(INSTANCE, USER, COURSE, EXERCISE, "5055");

        assertTrue(event.getMetadata().isEmpty());
    }
}
