package fi.helsinki.cs.tmc.snapshot.api.service;

import java.io.IOException;

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
    public void shouldReturnNullOnNonExistantUserId() throws IOException {

        when(tmcService.fetchJson("")).thenReturn("{\"api_version\":7,\"participants\":[{\"id\":42,\"username\":\"admin\",\"email\":\"jannebackman@live.fi\"}]}");
        when(tmcService.findUsernameById("", 404)).thenCallRealMethod();

        final String username = tmcService.findUsernameById("", 404);

        verify(tmcService).fetchJson("");
        assertNull(username);
    }
}
