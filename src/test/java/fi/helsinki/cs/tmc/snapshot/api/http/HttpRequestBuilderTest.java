package fi.helsinki.cs.tmc.snapshot.api.http;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;

import org.springframework.http.client.ClientHttpRequest;

import static org.junit.Assert.*;

public final class HttpRequestBuilderTest {

    private HttpRequestBuilder builder;

    @Before
    public void setUp() {

        builder = new HttpRequestBuilder("host", 80, "http");
    }

    @Test
    public void constructorSetsValues() throws URISyntaxException, IOException {

        final ClientHttpRequest request = builder.build();

        assertEquals("host", request.getURI().getHost());
        assertEquals(80, request.getURI().getPort());
        assertEquals("http", request.getURI().getScheme());
    }

    @Test
    public void setPathSetsPath() throws IOException, URISyntaxException {

        builder.setPath("/path/to/foo");
        final ClientHttpRequest request = builder.build();

        assertEquals("http://host:80/path/to/foo", request.getURI().toString());
    }

    @Test
    public void addParameterAddsParameter() throws IOException, URISyntaxException {

        builder.addParameter("param", "val");
        final ClientHttpRequest request = builder.build();

        assertTrue(request.getURI().toString().contains("param=val"));
    }

    @Test
    public void addParameterDoesNotOverwritePreviouslyAddedParameters() throws IOException, URISyntaxException {

        builder.addParameter("param1", "val1");
        builder.addParameter("param2", "val2");
        final ClientHttpRequest request = builder.build();

        assertTrue(request.getURI().toString().contains("param1=val1"));
        assertTrue(request.getURI().toString().contains("param2=val2"));
    }

    @Test
    public void setPathReturnsCorrectBuilder() {

        assertEquals(builder, builder.setPath("test2"));
    }

    @Test
    public void addParameterReturnsCorrectBuilder() {

        assertEquals(builder, builder.addParameter("foo", "bar"));
    }

    @Test
    public void shouldReturnNullOnFalseURI() {

        builder = new HttpRequestBuilder("C:\"", 404, null);
        final URI uri = builder.buildURI();

        assertNull(uri);
    }
}
