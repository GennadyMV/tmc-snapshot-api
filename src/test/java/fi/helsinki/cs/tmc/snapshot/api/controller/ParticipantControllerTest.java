package fi.helsinki.cs.tmc.snapshot.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fi.helsinki.cs.tmc.snapshot.api.app.App;
import fi.helsinki.cs.tmc.snapshot.api.model.Participant;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = App.class)
@WebAppConfiguration
@IntegrationTest
public class ParticipantControllerTest {

    private final TestRestTemplate template = new TestRestTemplate();

    @Test
    public void shouldReturnlistOfParticipants() throws IOException {

        final ObjectMapper mapper = new ObjectMapper();

        final String participantsJson = template.getForObject("http://localhost:8080/participants", String.class);

        final List<Participant> participants = Arrays.asList(mapper.readValue(participantsJson, Participant[].class));

        assertEquals(1, participants.size());
        assertTrue(participants.get(0).getSnapshots().isEmpty());
        assertEquals(1L, (long) participants.get(0).getId());
    }
}
