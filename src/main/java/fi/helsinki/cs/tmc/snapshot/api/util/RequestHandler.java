package fi.helsinki.cs.tmc.snapshot.api.util;

import fi.helsinki.cs.tmc.snapshot.api.exception.NotFoundException;
import fi.helsinki.cs.tmc.snapshot.api.http.HttpRequestBuilder;

import java.io.IOException;

import org.apache.commons.io.IOUtils;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;

public final class RequestHandler {

    private RequestHandler() { }

    public static String fetch(final HttpRequestBuilder requestBuilder) throws IOException {

        final ClientHttpResponse response = requestBuilder.get()
                                                          .execute();

        if (response.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
            response.close();
            throw new NotFoundException();
        }

        if (response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError()) {
            response.close();
            throw new IOException("Remote server returned status " + response.getRawStatusCode());
        }

        final String index = IOUtils.toString(response.getBody());
        response.close();

        return index;
    }
}
