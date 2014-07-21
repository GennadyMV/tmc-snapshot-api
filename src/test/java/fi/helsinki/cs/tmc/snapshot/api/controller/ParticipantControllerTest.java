package fi.helsinki.cs.tmc.snapshot.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import fi.helsinki.cs.tmc.snapshot.api.app.App;
import fi.helsinki.cs.tmc.snapshot.api.model.Participant;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = App.class)
@WebAppConfiguration
@IntegrationTest
public class ParticipantControllerTest {

    @Autowired
    private ParticipantController participantController;

    private MockMvc mockMvc;

    @Before
    public void setUp() {

        mockMvc = MockMvcBuilders.standaloneSetup(participantController).build();
    }

    @Test
    public void shouldReturnlistOfParticipants() throws Exception {

        final ObjectMapper mapper = new ObjectMapper();

        final MvcResult result = mockMvc.perform(get("/participants")).andReturn();

        final String participantsJson = result.getResponse().getContentAsString();

        final List<Participant> participants = Arrays.asList(mapper.readValue(participantsJson, Participant[].class));

        assertEquals(1, participants.size());
        assertTrue(participants.get(0).getSnapshots().isEmpty());
        assertEquals(1L, (long) participants.get(0).getId());
    }
}
