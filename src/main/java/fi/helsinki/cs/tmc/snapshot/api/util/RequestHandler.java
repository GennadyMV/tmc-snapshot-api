package fi.helsinki.cs.tmc.snapshot.api.util;

import fi.helsinki.cs.tmc.snapshot.api.exception.NotFoundException;
import fi.helsinki.cs.tmc.snapshot.api.http.HttpRequestBuilder;
import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;

public final class RequestHandler {

    private RequestHandler() { }

    private static ClientHttpResponse fetchData(final ClientHttpRequest request) throws IOException {

        final ClientHttpResponse response = request.execute();

        if (response.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
            response.close();
            throw new NotFoundException();
        }

        if (response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError()) {
            response.close();
            throw new IOException("Remote server returned status " + response.getRawStatusCode());
        }

        return response;
    }

    private static String responseToString(final ClientHttpResponse response) throws IOException {

        final String index = IOUtils.toString(response.getBody());
        response.close();

        return index;
    }

    private static byte[] responseToByteArray(final ClientHttpResponse response) throws IOException {

        final byte[] bytes = IOUtils.toByteArray(response.getBody());
        response.close();

        return bytes;
    }

    public static String fetchAsString(final HttpRequestBuilder requestBuilder) throws IOException {

        return fetchAsString(requestBuilder.get());
    }

    public static String fetchAsString(final ClientHttpRequest request) throws IOException {

        return responseToString(fetchData(request));
    }

    public static byte[] fetchAsByteArray(final HttpRequestBuilder requestBuilder) throws IOException {

        return fetchAsByteArray(requestBuilder.get());
    }

    public static byte[] fetchAsByteArray(final ClientHttpRequest request) throws IOException {

        return responseToByteArray(fetchData(request));
    }
}
