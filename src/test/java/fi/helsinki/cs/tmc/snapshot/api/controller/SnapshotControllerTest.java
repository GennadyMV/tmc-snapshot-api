package fi.helsinki.cs.tmc.snapshot.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import fi.helsinki.cs.tmc.snapshot.api.app.App;
import fi.helsinki.cs.tmc.snapshot.api.model.Snapshot;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotFile;
import fi.helsinki.cs.tmc.snapshot.api.service.SnapshotService;
import fi.helsinki.cs.tmc.snapshot.api.service.TmcService;

import java.util.ArrayList;
import java.util.Arrays;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = App.class)
@WebAppConfiguration
@IntegrationTest
@ActiveProfiles("test")
public class SnapshotControllerTest {

    @Mock
    private TmcService tmcDataService;

    @Mock
    private SnapshotService snapshotService;

    @InjectMocks
    private SnapshotController snapshotController;

    private MockMvc mockMvc;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(snapshotController).build();
    }

    @Test
    public void shouldReturnSnapshots() throws Exception {

        final ObjectMapper mapper = new ObjectMapper();

        final List<Snapshot> snapshotData = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            snapshotData.add(new Snapshot((long) i, new ArrayList<SnapshotFile>()));
        }

        when(tmcDataService.findByUsername("", 2064)).thenReturn("smith");
        when(snapshotService.findAll("/hy/", "smith")).thenReturn(snapshotData);

        final MvcResult result = mockMvc.perform(get("/participants/2064/snapshots")).andReturn();
        final String snapshotJson = result.getResponse().getContentAsString();

        final List<Snapshot> snapshots = Arrays.asList(mapper.readValue(snapshotJson, Snapshot[].class));

        assertEquals(5, snapshots.size());
    }

    @Test
    public void shouldReturnSnapshot() throws Exception {

        final ObjectMapper mapper = new ObjectMapper();

        final SnapshotFile file = new SnapshotFile("/src/HeiMaailma.java", "public class HeiMaailma { }");

        final Snapshot snapshotData = new Snapshot(1L, new ArrayList<>(Arrays.asList(file)));

        when(tmcDataService.findByUsername("", 2064)).thenReturn("jones");
        when(snapshotService.find("/hy/", "jones", 1L)).thenReturn(snapshotData);

        final MvcResult result = mockMvc.perform(get("/participants/2064/snapshots/1")).andReturn();
        final String snapshotJson = result.getResponse().getContentAsString();

        final Snapshot snapshot = mapper.readValue(snapshotJson, Snapshot.class);

        assertEquals(1, (long) snapshot.getId());
        assertEquals(1, snapshot.getFiles().size());
        assertEquals("/src/HeiMaailma.java", snapshot.getFiles().get(0).getPath());
    }

    @Test
    public void shouldReturnNullOnFalseParticipantId() throws Exception {

        when(tmcDataService.findByUsername("", 0)).thenReturn(null);

        mockMvc.perform(get("/participants/0/snapshots")).andExpect(status().is(404));
    }

    @Test
    public void shouldReturnNullOnInvalidParticipantIdAndValidSnapshotId() throws Exception {

        when(tmcDataService.findByUsername("", 0)).thenReturn(null);

        mockMvc.perform(get("/participants/0/snapshots/1")).andExpect(status().is(404));
    }

    @Test
    public void shouldReturnNullOnFalseSnapshotId() throws Exception {

        when(tmcDataService.findByUsername("", 1)).thenReturn("smith");
        when(snapshotService.find("/hy/", "smith", 1L)).thenReturn(null);

        mockMvc.perform(get("/participants/1/snapshots/0")).andExpect(status().is(404));
    }
}
