package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.exception.NotFoundException;
import fi.helsinki.cs.tmc.snapshot.api.http.HttpRequestBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.mock.http.client.MockClientHttpRequest;
import org.springframework.mock.http.client.MockClientHttpResponse;

import static org.junit.Assert.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ HttpRequestBuilder.class, DefaultSpywareService.class })
public final class DefaultSpywareServiceTest {

    @Spy
    private final MockClientHttpRequest request = new MockClientHttpRequest(HttpMethod.GET, null);

    @Spy
    private final HttpRequestBuilder requestBuilder = new HttpRequestBuilder("host", 80, "http");

    @InjectMocks
    private DefaultSpywareService spywareService;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);
    }

    private void prepareRequestResponse(final byte[] content, final HttpStatus status) throws IOException {

        final MockClientHttpResponse response = new MockClientHttpResponse(content, status);
        spy(response);

        when(requestBuilder.get()).thenReturn(request);
        when(request.execute()).thenReturn(response);
    }

    private ClientHttpResponse prepareMockResponse(final HttpStatus status) throws IOException {

        final InputStream body = new ByteArrayInputStream("asd".getBytes());
        final ClientHttpResponse mockResponse = mock(ClientHttpResponse.class);

        when(requestBuilder.get()).thenReturn(request);
        when(request.execute()).thenReturn(mockResponse);
        when(mockResponse.getBody()).thenReturn(body);
        when(mockResponse.getStatusCode()).thenReturn(status);

        return mockResponse;
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowExceptionWhenDataFileIsNotfound() throws IOException {

        prepareRequestResponse(null, HttpStatus.NOT_FOUND);

        spywareService.fetchChunkByRange("test", "notfound", 404, 504);
    }

    @Test(expected = IOException.class)
    public void shouldThrowExceptionOnDataServerError() throws IOException {

        prepareRequestResponse(null, HttpStatus.INTERNAL_SERVER_ERROR);

        spywareService.fetchChunkByRange("test", "error", 500, 600);
    }

    @Test
    public void shouldReturnDataFile() throws IOException {

        final byte[] content = { (byte) 0x1f, (byte) 0x8b, (byte) 0x08, (byte) 0x00 };

        prepareRequestResponse(content, HttpStatus.OK);

        final byte[] byteData = spywareService.fetchChunkByRange("test", "ok", 200, 300);

        assertNotNull(byteData);
        assertEquals(4, byteData.length);
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowExceptionWhenIndexFileIsNotFound() throws IOException {

        prepareRequestResponse(null, HttpStatus.NOT_FOUND);

        spywareService.fetchIndex("not", "found");
    }

    @Test(expected = IOException.class)
    public void shouldThrowExceptionOnIndexServerError() throws IOException {

        prepareRequestResponse(null, HttpStatus.INTERNAL_SERVER_ERROR);

        spywareService.fetchIndex("server", "error");
    }

    @Test
    public void shouldReturnIndexFile() throws IOException {

        final byte[] content = { (byte) 0x1f, (byte) 0x8b, (byte) 0x08, (byte) 0x00 };

        prepareRequestResponse(content, HttpStatus.OK);

        final String data = spywareService.fetchIndex("server", "ok");

        assertNotNull(data);
        assertEquals(4, data.length());
    }

    @Test
    public void shouldSetCorrectRangeHeader() throws IOException {

        final byte[] content = { (byte) 0x11 };

        prepareRequestResponse(content, HttpStatus.OK);
        spywareService.fetchChunkByRange("foo", "bar", 0, 12);

        assertEquals("bytes=0-12", request.getHeaders().get("Range").get(0));
    }

    @Test
    public void findWithRangeShouldCloseResponseBodyStreamAfterSuccess() throws IOException {

        final ClientHttpResponse mockResponse = prepareMockResponse(HttpStatus.OK);
        spywareService.fetchChunkByRange("foo", "bar", 1, 2);

        verify(mockResponse).close();
    }

    @Test(expected = NotFoundException.class)
    public void findWithRangeShouldCloseResponseBodyStreamAfterNotFound() throws IOException {

        final ClientHttpResponse mockResponse = prepareMockResponse(HttpStatus.NOT_FOUND);

        try {
            spywareService.fetchChunkByRange("foo", "bar", 1, 2);
        } catch (NotFoundException exception) {
            verify(mockResponse).close();
            throw exception;
        }
    }

    @Test(expected = IOException.class)
    public void findWithRangeShouldCloseResponseBodyStreamAfterServerError() throws IOException {

        final ClientHttpResponse mockResponse = prepareMockResponse(HttpStatus.INTERNAL_SERVER_ERROR);

        try {
            spywareService.fetchChunkByRange("foo", "bar", 1, 2);
        } catch (IOException exception) {
            verify(mockResponse).close();
            throw exception;
        }
    }

    @Test(expected = IOException.class)
    public void findWithRangeShouldCloseResponseBodyStreamAfterClientError() throws IOException {

        final ClientHttpResponse mockResponse = prepareMockResponse(HttpStatus.FORBIDDEN);

        try {
            spywareService.fetchChunkByRange("foo", "bar", 1, 2);
        } catch (IOException exception) {
            verify(mockResponse).close();
            throw exception;
        }
    }

    @Test(expected = NotFoundException.class)
    public void fetchIndexShouldCloseResponseBodyStreamAfterNotFound() throws IOException {

        final ClientHttpResponse mockResponse = prepareMockResponse(HttpStatus.NOT_FOUND);

        try {
            spywareService.fetchIndex("instance", "user");
        } catch (NotFoundException exception) {
            verify(mockResponse).close();
            throw exception;
        }
    }

    @Test(expected = IOException.class)
    public void fetchIndexShouldCloseResponseBodyStreamAfterServerError() throws IOException {

        final ClientHttpResponse mockResponse = prepareMockResponse(HttpStatus.INTERNAL_SERVER_ERROR);

        try {
            spywareService.fetchIndex("instance", "user");
        } catch (IOException exception) {
            verify(mockResponse).close();
            throw exception;
        }
    }

    @Test(expected = IOException.class)
    public void fetchIndexShouldCloseResponseBodyStreamAfterClientError() throws IOException {

        final ClientHttpResponse mockResponse = prepareMockResponse(HttpStatus.FORBIDDEN);

        try {
            spywareService.fetchIndex("instance", "user");
        } catch (IOException exception) {
            verify(mockResponse).close();
            throw exception;
        }
    }

    @Test
    public void fetchIndexShouldCloseResponseBodyStreamAfterSuccess() throws IOException {

        final ClientHttpResponse mockResponse = prepareMockResponse(HttpStatus.OK);

        spywareService.fetchIndex("instance", "user");

        verify(mockResponse).close();
    }
}
