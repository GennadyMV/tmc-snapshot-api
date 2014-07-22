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
public class SnapshotFileControllerTest {

    @Mock
    private TmcService tmcService;

    @Mock
    private SnapshotService snapshotService;

    @InjectMocks
    private SnapshotFileController snapshotFileController;

    private MockMvc mockMvc;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(snapshotFileController).build();
    }

    @Test
    public void shouldReturnSnapshotFiles() throws Exception {

        final ObjectMapper mapper = new ObjectMapper();

        final SnapshotFile file = new SnapshotFile("/src/HeiMaailma.java", "public class HeiMaailma { }");

        final Snapshot snapshotData = new Snapshot(1L, new ArrayList<>(Arrays.asList(file)));

        when(tmcService.findUsername("", 2064)).thenReturn("jones");
        when(snapshotService.find("/hy/", "jones", 1L)).thenReturn(snapshotData);

        final MvcResult result = mockMvc.perform(get("/participants/2064/snapshots/1/files")).andReturn();
        final String snapshotFilesJson = result.getResponse().getContentAsString();

        final List<SnapshotFile> snapshotFiles = Arrays.asList(mapper.readValue(snapshotFilesJson, SnapshotFile[].class));

        assertEquals(1, snapshotFiles.size());
        assertEquals("/src/HeiMaailma.java", snapshotFiles.get(0).getPath());
    }

    @Test
    public void shouldReturnSnapshotFile() throws Exception {

        final SnapshotFile file = new SnapshotFile("/src/HeiMaailma.java", "public class HeiMaailma { }");

        final Snapshot snapshotData = new Snapshot(1L, new ArrayList<>(Arrays.asList(file)));

        when(tmcService.findUsername("", 2064)).thenReturn("jones");
        when(snapshotService.find("/hy/", "jones", 1L)).thenReturn(snapshotData);

        final MvcResult result = mockMvc.perform(get("/participants/2064/snapshots/1/files/src/HeiMaailma.java")).andReturn();
        final String fileContent = result.getResponse().getContentAsString();

        assertEquals("text/plain", result.getResponse().getContentType());
        assertEquals("public class HeiMaailma { }", fileContent);
    }

    @Test
    public void shouldReturn404OnNonExistantParticipantId() throws Exception {

        when(tmcService.findUsername("", 0)).thenReturn(null);

        mockMvc.perform(get("/participants/0/snapshots/1/files")).andExpect(status().is(404));
    }

    @Test
    public void shouldReturn404OnNonExistantParticipantIdForSpecificFile() throws Exception {

        when(tmcService.findUsername("", 0)).thenReturn(null);

        mockMvc.perform(get("/participants/0/snapshots/1/files/src/Legit.java")).andExpect(status().is(404));
    }

    @Test
    public void shouldReturn404OnNonExistantSnapshotFile() throws Exception {

        when(tmcService.findUsername("", 1)).thenReturn("jack");
        when(snapshotService.find("/hy/", "jack", 1L)).thenReturn(new Snapshot(1L, new ArrayList<SnapshotFile>()));

        mockMvc.perform(get("/participants/1/snapshots/1/files/src/404.java")).andExpect(status().is(404));
    }
}
