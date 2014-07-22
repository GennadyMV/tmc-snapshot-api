package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.model.TmcParticipant;
import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(HttpTmcService.class)
public class HttpTmcServiceTest {

    @Mock
    private HttpTmcService tmcService;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldFindUsernameWithValidUserId() throws IOException {

        when(tmcService.fetchJson("")).thenReturn("{\"api_version\":7,\"participants\":[{\"id\":42,\"username\":\"admin\",\"email\":\"jannebackman@live.fi\"}]}");
        when(tmcService.findUsernameById("", 42)).thenCallRealMethod();

        final String username = tmcService.findUsernameById("", 42);

        verify(tmcService).fetchJson("");
        assertEquals("admin", username);
    }

    @Test
    public void shouldFindAllTmcParticipants() throws IOException {

        when(tmcService.fetchJson("")).thenReturn("{\"api_version\":7,\"participants\":[{\"id\":1948,\"username\":\"who\",\"email\":\"anonymous@cs.com\"},{\"id\":726,\"username\":\"am\",\"email\":\"anonymous@cs.com\"},{\"id\":343,\"username\":\"i\",\"email\":\"anonymous@cs.com\"}]}");
        when(tmcService.findAll("")).thenCallRealMethod();

        final List<TmcParticipant> participants = tmcService.findAll("");

        assertEquals(3, participants.size());
        assertEquals(1948, (long) participants.get(0).getId());
        assertEquals("who", participants.get(0).getUsername());
        assertEquals(726, (long) participants.get(1).getId());
        assertEquals("am", participants.get(1).getUsername());
        assertEquals(343, (long) participants.get(2).getId());
        assertEquals("i", participants.get(2).getUsername());
    }

    @Test
    public void shouldReturnNullOnNonExistantUserId() throws IOException {

        when(tmcService.fetchJson("")).thenReturn("{\"api_version\":7,\"participants\":[{\"id\":42,\"username\":\"admin\",\"email\":\"jannebackman@live.fi\"}]}");
        when(tmcService.findUsernameById("", 404)).thenCallRealMethod();

        final String username = tmcService.findUsernameById("", 404);

        verify(tmcService).fetchJson("");
        assertNull(username);
    }
}
