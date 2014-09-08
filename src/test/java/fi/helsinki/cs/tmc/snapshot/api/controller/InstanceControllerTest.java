package fi.helsinki.cs.tmc.snapshot.api.controller;

import fi.helsinki.cs.tmc.snapshot.api.app.App;
import fi.helsinki.cs.tmc.snapshot.api.exception.NotFoundException;
import fi.helsinki.cs.tmc.snapshot.api.model.Instance;
import fi.helsinki.cs.tmc.snapshot.api.service.InstanceService;

import java.util.ArrayList;
import java.util.List;
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
public class InstanceControllerTest {

    private static final String INSTANCE_1 = "mooc";
    private static final String INSTANCE_2 = "hy";
    private static final String BASE_URL = "/";

    @Mock
    private InstanceService instanceService;

    @InjectMocks
    private InstanceController instanceController;


    private MockMvc mockMvc;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(instanceController).build();
    }

    @Test
    public void listReturnsAllInstances() throws Exception {

        final List<Instance> instances = new ArrayList<>();
        instances.add(new Instance(INSTANCE_1));
        instances.add(new Instance(INSTANCE_2));

        when(instanceService.findAll()).thenReturn(instances);

        mockMvc.perform(get(BASE_URL))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$", hasSize(2)))
               .andExpect(jsonPath("$[0].id", is(INSTANCE_1)))
               .andExpect(jsonPath("$[1].id", is(INSTANCE_2)));

        verify(instanceService).findAll();
        verifyNoMoreInteractions(instanceService);
    }

    @Test
    public void listHandlesNotFound() throws Exception {

        when(instanceService.findAll()).thenThrow(new NotFoundException());

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isNotFound());

        verify(instanceService).findAll();
        verifyNoMoreInteractions(instanceService);
    }

    @Test
    public void readReturnsCorrectInstance() throws Exception {

        when(instanceService.find(INSTANCE_1)).thenReturn(new Instance(INSTANCE_1));

        mockMvc.perform(get(BASE_URL + INSTANCE_1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(INSTANCE_1)));

        verify(instanceService).find(INSTANCE_1);
        verifyNoMoreInteractions(instanceService);
    }

    @Test
    public void readHandlesNotFound() throws Exception {

        when(instanceService.find(INSTANCE_2)).thenThrow(new NotFoundException());

        mockMvc.perform(get(BASE_URL + INSTANCE_2))
                .andExpect(status().isNotFound());

        verify(instanceService).find(INSTANCE_2);
        verifyNoMoreInteractions(instanceService);
    }
}
