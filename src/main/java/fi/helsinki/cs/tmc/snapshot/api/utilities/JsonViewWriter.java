package fi.helsinki.cs.tmc.snapshot.api.utilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.util.logging.Level;
import java.util.logging.Logger;

public class JsonViewWriter {

    public static String getView(final Object object, final Object view) {

        try {

            final ObjectWriter objectWriter = new ObjectMapper().writerWithView((Class<?>) view);
            return objectWriter.writeValueAsString(object);

        } catch (JsonProcessingException ex) {

            Logger.getLogger(JsonViewWriter.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
