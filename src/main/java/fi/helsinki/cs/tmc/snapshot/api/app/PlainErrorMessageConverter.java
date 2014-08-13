package fi.helsinki.cs.tmc.snapshot.api.app;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;

public final class PlainErrorMessageConverter extends AbstractHttpMessageConverter<fi.helsinki.cs.tmc.snapshot.api.model.Error> {

    public PlainErrorMessageConverter() {

        super(MediaType.TEXT_PLAIN);
    }

    @Override
    public boolean supports(final Class<?> clazz) {

        return fi.helsinki.cs.tmc.snapshot.api.model.Error.class.equals(clazz);
    }

    @Override
    protected fi.helsinki.cs.tmc.snapshot.api.model.Error readInternal(final Class<? extends fi.helsinki.cs.tmc.snapshot.api.model.Error> error,
                                                                       final HttpInputMessage inputMessage) throws IOException {

        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    protected void writeInternal(final fi.helsinki.cs.tmc.snapshot.api.model.Error error,
                                 final HttpOutputMessage outputMessage) throws IOException {

        try (PrintWriter writer = new PrintWriter(outputMessage.getBody())) {
            writer.print(error.getError());
        }
    }
}
