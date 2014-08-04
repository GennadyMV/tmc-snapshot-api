package fi.helsinki.cs.tmc.snapshot.api.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.IOUtils;

public final class GZip {

    private GZip() {}

    public static byte[] decompress(final byte[] content) throws IOException {

        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        IOUtils.copy(new GZIPInputStream(new ByteArrayInputStream(content)), output);

        return output.toByteArray();
    }
}
