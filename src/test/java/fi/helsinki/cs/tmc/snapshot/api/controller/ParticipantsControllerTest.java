package fi.helsinki.cs.tmc.snapshot.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import fi.helsinki.cs.tmc.snapshot.api.app.App;
import fi.helsinki.cs.tmc.snapshot.api.model.Participant;
import fi.helsinki.cs.tmc.snapshot.api.model.Snapshot;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotFile;
import fi.helsinki.cs.tmc.snapshot.api.service.SnapshotService;
import fi.helsinki.cs.tmc.snapshot.api.service.TmcService;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = App.class)
@WebAppConfiguration
@IntegrationTest
@ActiveProfiles("test")
public class ParticipantsControllerTest {

    @Mock
    private TmcService tmcDataService;

    @Mock
    private SnapshotService snapshotService;

    @InjectMocks
    private ParticipantController participantController;

    private MockMvc mockMvc;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(participantController).build();
    }

    @Test
    public void shouldReturnParticipantInformation() throws Exception {

        final ObjectMapper mapper = new ObjectMapper();

        final List<Snapshot> snapshots = new ArrayList<>();

        for (int i = 0; i < 5; ++i) {
            snapshots.add(new Snapshot((long) i, new ArrayList<SnapshotFile>()));
        }

        when(tmcDataService.findUsername("", 2064)).thenReturn("hiphei");
        when(snapshotService.findAll("/hy/", "hiphei")).thenReturn(snapshots);

        final MvcResult result = mockMvc.perform(get("/participants/2064")).andReturn();
        final String participantJson = result.getResponse().getContentAsString();

        final Participant participant = mapper.readValue(participantJson, Participant.class);

        assertEquals(2064, (long) participant.getId());
        assertEquals(5, participant.getSnapshots().size());
    }
}
