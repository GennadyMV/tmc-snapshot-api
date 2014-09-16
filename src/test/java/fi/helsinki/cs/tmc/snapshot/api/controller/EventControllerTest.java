package fi.helsinki.cs.tmc.snapshot.api.controller;

import fi.helsinki.cs.tmc.snapshot.api.app.App;
import fi.helsinki.cs.tmc.snapshot.api.exception.NotFoundException;
import fi.helsinki.cs.tmc.snapshot.api.model.Event;
import fi.helsinki.cs.tmc.snapshot.api.service.EventService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = App.class)
@WebAppConfiguration
@ActiveProfiles("test")
public final class EventControllerTest {

    private static final String INSTANCE = "hy";
    private static final String USER = "testUsername";
    private static final String COURSE = "testCourseName";
    private static final String EXERCISE = "testExerciseName";
    private static final String EVENT_BASE_URL = "/hy/participants/testUsername/courses/testCourseName/exercises/testExerciseName/events";

    @Mock
    private EventService eventService;

    @InjectMocks
    private EventController eventController;

    private MockMvc mockMvc;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(eventController).build();
    }

    @Test
    public void shouldReturnEvents() throws Exception {

        final List<Event> eventData = new ArrayList<>();

        for (int i = 0; i < 5; i++) {

            eventData.add(new Event(Integer.toString(i),
                                    Integer.toString(i),
                                    (long) i,
                                    new HashMap<String, Object>()));
        }

        when(eventService.findAll(INSTANCE, USER, COURSE, EXERCISE)).thenReturn(eventData);

        mockMvc.perform(get(EVENT_BASE_URL))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$", hasSize(5)));

        verify(eventService).findAll(INSTANCE, USER, COURSE, EXERCISE);
        verifyNoMoreInteractions(eventService);
    }

    @Test
    public void shouldReturnSnapshot() throws Exception {

        final Map<String, Object> metadata = new HashMap<>();
        metadata.put("key", "value");
        metadata.put("key2", "value2");

        final Event event = new Event("1", "eventType", 1L, metadata);

        when(eventService.find(INSTANCE, USER, COURSE, EXERCISE, "1")).thenReturn(event);

        mockMvc.perform(get(EVENT_BASE_URL + "/1"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.id", is("1")))
               .andExpect(jsonPath("$.eventType", is("eventType")))
               .andExpect(jsonPath("$.happendAt", is(1)))
               .andExpect(jsonPath("$.metadata.key", is("value")))
               .andExpect(jsonPath("$.metadata.key2", is("value2")));

        verify(eventService).find(INSTANCE, USER, COURSE, EXERCISE, "1");
        verifyNoMoreInteractions(eventService);
    }

    @Test
    public void listHandlesNotFoundException() throws Exception {

        when(eventService.findAll(INSTANCE, USER, COURSE, EXERCISE))
            .thenThrow(new NotFoundException());

        mockMvc.perform(get(EVENT_BASE_URL))
               .andExpect(status().is(404));
    }

    @Test
    public void readHandlesNotFoundException() throws Exception {

        when(eventService.find(INSTANCE, USER, COURSE, EXERCISE, "1"))
            .thenThrow(new NotFoundException());

        mockMvc.perform(get(EVENT_BASE_URL + "/1"))
               .andExpect(status().is(404));
    }
}
