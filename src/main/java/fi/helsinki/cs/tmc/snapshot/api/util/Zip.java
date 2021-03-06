package fi.helsinki.cs.tmc.snapshot.api.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public final class Zip {

    private Zip() {}

    public static Map<String, byte[]> decompress(final byte[] content) throws IOException {

        final Map<String, byte[]> entries = new TreeMap<>();

        try (ZipInputStream input = new ZipInputStream(new ByteArrayInputStream(content))) {

            ZipEntry entry;

            while ((entry = input.getNextEntry()) != null) {

                final ByteArrayOutputStream output = new ByteArrayOutputStream();

                final byte[] buffer = new byte[2048];
                int bytesRead;

                while ((bytesRead = input.read(buffer, 0, 2048)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }

                entries.put(entry.getName(), output.toByteArray());
            }
        }

        return entries;
    }
}
