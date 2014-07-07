package fi.helsinki.cs.tmc.snapshot.api.utilities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.IOUtils;

public class GZip {

    public static byte[] decompress(final byte[] content) {

        final ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            IOUtils.copy(new GZIPInputStream(new ByteArrayInputStream(content)), output);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        return output.toByteArray();
    }
}
