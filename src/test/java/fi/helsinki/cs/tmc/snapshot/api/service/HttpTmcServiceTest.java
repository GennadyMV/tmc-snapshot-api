package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.http.HttpRequestBuilder;
import fi.helsinki.cs.tmc.snapshot.api.model.TmcParticipant;
import fi.helsinki.cs.tmc.snapshot.api.util.CacheHelper;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.*;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ HttpRequestBuilder.class, HttpTmcService.class, LoggerFactory.class })
public final class HttpTmcServiceTest {

    private Logger logger;

    @Mock
    private HttpTmcService mockTmcService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private HttpRequestBuilder requestBuilder;

    @InjectMocks
    private HttpTmcService tmcService;

    private CacheHelper cacheHelper;

    @Before
    public void setUp() {

        mockStatic(LoggerFactory.class);
        logger = mock(Logger.class);
        when(LoggerFactory.getLogger(any(Class.class))).thenReturn(logger);

        MockitoAnnotations.initMocks(this);

        this.cacheHelper = mock(CacheHelper.class);

        Whitebox.setInternalState(mockTmcService, this.cacheHelper);
    }

    @Test
    public void shouldFindUsernameWithValidUserId() throws IOException {

        when(mockTmcService.fetchJson("")).thenReturn("{\"api_version\":7,\"participants\":[{\"id\":42,\"username\":\"admin\",\"email\":\"jannebackman@live.fi\"}]}");
        when(mockTmcService.findUsernameById("", 42)).thenCallRealMethod();

        final String username = mockTmcService.findUsernameById("", 42);

        verify(mockTmcService).fetchJson("");
        assertEquals("admin", username);
    }

    @Test
    public void shouldFindAllTmcParticipants() throws IOException {

        when(mockTmcService.fetchJson("")).thenReturn("{\"api_version\":7,\"participants\":[{\"id\":1948,\"username\":\"who\",\"email\":\"anonymous@cs.com\"},{\"id\":726,\"username\":\"am\",\"email\":\"anonymous@cs.com\"},{\"id\":343,\"username\":\"i\",\"email\":\"anonymous@cs.com\"}]}");
        when(mockTmcService.findAll("")).thenCallRealMethod();

        final List<TmcParticipant> participants = mockTmcService.findAll("");

        assertEquals(3, participants.size());
        assertEquals(343, (long) participants.get(0).getId());
        assertEquals("i", participants.get(0).getUsername());
        assertEquals(726, (long) participants.get(1).getId());
        assertEquals("am", participants.get(1).getUsername());
        assertEquals(1948, (long) participants.get(2).getId());
        assertEquals("who", participants.get(2).getUsername());
    }

    @Test
    public void shouldCacheAllParticipantsRetrievedFromFindAll() throws IOException {

        when(mockTmcService.fetchJson("")).thenReturn("{\"api_version\":7,\"participants\":[{\"id\":1948,\"username\":\"who\",\"email\":\"anonymous@cs.com\"},{\"id\":726,\"username\":\"am\",\"email\":\"anonymous@cs.com\"},{\"id\":343,\"username\":\"i\",\"email\":\"anonymous@cs.com\"}]}");
        when(mockTmcService.findAll("")).thenCallRealMethod();

        mockTmcService.findAll("");

        verify(cacheHelper, times(1)).cacheUsername("", 343L, "i");
        verify(cacheHelper, times(1)).cacheUsername("", 726L, "am");
        verify(cacheHelper, times(1)).cacheUsername("", 1948L, "who");
        verifyNoMoreInteractions(cacheHelper);
    }

    @Test
    public void shouldReturnNullOnNonExistantUserId() throws IOException {

        when(mockTmcService.fetchJson("")).thenReturn("{\"api_version\":7,\"participants\":[{\"id\":42,\"username\":\"admin\",\"email\":\"jannebackman@live.fi\"}]}");
        when(mockTmcService.findUsernameById("", 404)).thenCallRealMethod();

        final String username = mockTmcService.findUsernameById("", 404);

        verify(mockTmcService).fetchJson("");
        assertNull(username);
    }

    @Test
    public void shouldFetchParticipantsJson() throws IOException {

        when(restTemplate.getForObject(any(URI.class), eq(String.class))).thenReturn("json");

        assertEquals("json", tmcService.fetchJson("test"));
        verify(requestBuilder).setPath("/participants.json");
    }
}
