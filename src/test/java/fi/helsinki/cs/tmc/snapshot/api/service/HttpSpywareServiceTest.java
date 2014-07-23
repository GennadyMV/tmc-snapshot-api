package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.exception.NotFoundException;
import fi.helsinki.cs.tmc.snapshot.api.http.HttpRequestBuilder;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import org.springframework.http.HttpStatus;
import org.springframework.mock.http.client.MockClientHttpRequest;
import org.springframework.mock.http.client.MockClientHttpResponse;

import static org.junit.Assert.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ HttpRequestBuilder.class, HttpSpywareService.class })
public final class HttpSpywareServiceTest {

    @Mock
    private MockClientHttpRequest request;

    @Spy
    private final HttpRequestBuilder requestBuilder = new HttpRequestBuilder("host", 80, "http");

    @InjectMocks
    private HttpSpywareService spywareService;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowExceptionWhenIndexFileIsNotFound() throws IOException {

        final MockClientHttpResponse response = new MockClientHttpResponse((byte[]) null, HttpStatus.NOT_FOUND);
        spy(response);

        when(requestBuilder.build()).thenReturn(request);
        when(request.execute()).thenReturn(response);

        spywareService.fetchIndex("not", "found");
    }

    @Test(expected = IOException.class)
    public void shouldThrowExceptionOnServerError() throws IOException {

        final MockClientHttpResponse response = new MockClientHttpResponse((byte[]) null, HttpStatus.INTERNAL_SERVER_ERROR);
        spy(response);

        when(requestBuilder.build()).thenReturn(request);
        when(request.execute()).thenReturn(response);

        spywareService.fetchIndex("server", "error");
    }

    @Test
    public void shouldReturnIndexFile() throws IOException {

        final byte[] content = {(byte) 0x1f, (byte) 0x8b, (byte) 0x08, (byte) 0x00};

        final MockClientHttpResponse response = new MockClientHttpResponse(content, HttpStatus.OK);
        spy(response);

        when(requestBuilder.build()).thenReturn(request);
        when(request.execute()).thenReturn(response);

        final InputStream data = spywareService.fetchIndex("server", "ok");

        final byte[] byteData = IOUtils.toByteArray(data);

        assertNotNull(data);
        assertEquals(4, byteData.length);
    }
}
