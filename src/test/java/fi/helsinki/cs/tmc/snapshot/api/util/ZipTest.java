package fi.helsinki.cs.tmc.snapshot.api.util;

import java.util.Map;

import org.junit.Test;

import static org.junit.Assert.*;

public final class ZipTest {

    @Test
    public void testDecompressZipContainingOneFile() {

        // ZIP Achive containing 'test.java' file with content 'test'
        final byte[] content = {(byte) 0x50, (byte) 0x4b, (byte) 0x03, (byte) 0x04,
                                (byte) 0x14, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                                (byte) 0x08, (byte) 0x00, (byte) 0xf7, (byte) 0x5e,
                                (byte) 0xf6, (byte) 0x44, (byte) 0x0c, (byte) 0x7e,
                                (byte) 0x7f, (byte) 0xd8, (byte) 0x06, (byte) 0x00,
                                (byte) 0x00, (byte) 0x00, (byte) 0x04, (byte) 0x00,
                                (byte) 0x00, (byte) 0x00, (byte) 0x09, (byte) 0x00,
                                (byte) 0x00, (byte) 0x00, (byte) 0x74, (byte) 0x65,
                                (byte) 0x73, (byte) 0x74, (byte) 0x2e, (byte) 0x6a,
                                (byte) 0x61, (byte) 0x76, (byte) 0x61, (byte) 0x2b,
                                (byte) 0x49, (byte) 0x2d, (byte) 0x2e, (byte) 0x01,
                                (byte) 0x00, (byte) 0x50, (byte) 0x4b, (byte) 0x01,
                                (byte) 0x02, (byte) 0x00, (byte) 0x00, (byte) 0x14,
                                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x08,
                                (byte) 0x00, (byte) 0xf7, (byte) 0x5e, (byte) 0xf6,
                                (byte) 0x44, (byte) 0x0c, (byte) 0x7e, (byte) 0x7f,
                                (byte) 0xd8, (byte) 0x06, (byte) 0x00, (byte) 0x00,
                                (byte) 0x00, (byte) 0x04, (byte) 0x00, (byte) 0x00,
                                (byte) 0x00, (byte) 0x09, (byte) 0x00, (byte) 0x00,
                                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x74,
                                (byte) 0x65, (byte) 0x73, (byte) 0x74, (byte) 0x2e,
                                (byte) 0x6a, (byte) 0x61, (byte) 0x76, (byte) 0x61,
                                (byte) 0x50, (byte) 0x4b, (byte) 0x05, (byte) 0x06,
                                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                                (byte) 0x01, (byte) 0x00, (byte) 0x01, (byte) 0x00,
                                (byte) 0x37, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                                (byte) 0x2d, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                                (byte) 0x00, (byte) 0x00};

        final Map<String, byte[]> result = Zip.decompress(content);

        assertEquals(1, result.size());
        checkZipContainsFileWithContents(result, "test.java", stringToByteArray("test"));
    }

    @Test
    public void testDecompressZipContainingTwoFiles() {

        // ZIP Achive containing 'test.java' file with content 'test'
        final byte[] content = {(byte) 0x50, (byte) 0x4b, (byte) 0x03, (byte) 0x04,
                                (byte) 0x14, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                                (byte) 0x08, (byte) 0x00, (byte) 0x24, (byte) 0x61,
                                (byte) 0xf6, (byte) 0x44, (byte) 0xac, (byte) 0xb0,
                                (byte) 0x0d, (byte) 0xd4, (byte) 0x0a, (byte) 0x00,
                                (byte) 0x00, (byte) 0x00, (byte) 0x08, (byte) 0x00,
                                (byte) 0x00, (byte) 0x00, (byte) 0x09, (byte) 0x00,
                                (byte) 0x00, (byte) 0x00, (byte) 0x4d, (byte) 0x61,
                                (byte) 0x69, (byte) 0x6e, (byte) 0x2e, (byte) 0x6a,
                                (byte) 0x61, (byte) 0x76, (byte) 0x61, (byte) 0xcb,
                                (byte) 0xcd, (byte) 0x2f, (byte) 0xca, (byte) 0xcb,
                                (byte) 0xcc, (byte) 0x4b, (byte) 0x2f, (byte) 0x06,
                                (byte) 0x00, (byte) 0x50, (byte) 0x4b, (byte) 0x03,
                                (byte) 0x04, (byte) 0x14, (byte) 0x00, (byte) 0x00,
                                (byte) 0x00, (byte) 0x08, (byte) 0x00, (byte) 0x24,
                                (byte) 0x61, (byte) 0xf6, (byte) 0x44, (byte) 0xce,
                                (byte) 0xe5, (byte) 0xe0, (byte) 0x12, (byte) 0x06,
                                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x04,
                                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x09,
                                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x44,
                                (byte) 0x6f, (byte) 0x67, (byte) 0x65, (byte) 0x2e,
                                (byte) 0x6a, (byte) 0x61, (byte) 0x76, (byte) 0x61,
                                (byte) 0x2b, (byte) 0x2f, (byte) 0x4d, (byte) 0x4b,
                                (byte) 0x03, (byte) 0x00, (byte) 0x50, (byte) 0x4b,
                                (byte) 0x01, (byte) 0x02, (byte) 0x00, (byte) 0x00,
                                (byte) 0x14, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                                (byte) 0x08, (byte) 0x00, (byte) 0x24, (byte) 0x61,
                                (byte) 0xf6, (byte) 0x44, (byte) 0xac, (byte) 0xb0,
                                (byte) 0x0d, (byte) 0xd4, (byte) 0x0a, (byte) 0x00,
                                (byte) 0x00, (byte) 0x00, (byte) 0x08, (byte) 0x00,
                                (byte) 0x00, (byte) 0x00, (byte) 0x09, (byte) 0x00,
                                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                                (byte) 0x4d, (byte) 0x61, (byte) 0x69, (byte) 0x6e,
                                (byte) 0x2e, (byte) 0x6a, (byte) 0x61, (byte) 0x76,
                                (byte) 0x61, (byte) 0x50, (byte) 0x4b, (byte) 0x01,
                                (byte) 0x02, (byte) 0x00, (byte) 0x00, (byte) 0x14,
                                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x08,
                                (byte) 0x00, (byte) 0x24, (byte) 0x61, (byte) 0xf6,
                                (byte) 0x44, (byte) 0xce, (byte) 0xe5, (byte) 0xe0,
                                (byte) 0x12, (byte) 0x06, (byte) 0x00, (byte) 0x00,
                                (byte) 0x00, (byte) 0x04, (byte) 0x00, (byte) 0x00,
                                (byte) 0x00, (byte) 0x09, (byte) 0x00, (byte) 0x00,
                                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x31,
                                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x44,
                                (byte) 0x6f, (byte) 0x67, (byte) 0x65, (byte) 0x2e,
                                (byte) 0x6a, (byte) 0x61, (byte) 0x76, (byte) 0x61,
                                (byte) 0x50, (byte) 0x4b, (byte) 0x05, (byte) 0x06,
                                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                                (byte) 0x02, (byte) 0x00, (byte) 0x02, (byte) 0x00,
                                (byte) 0x6e, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                                (byte) 0x5e, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                                (byte) 0x00, (byte) 0x00};

        final Map<String, byte[]> result = Zip.decompress(content);

        assertEquals(2, result.size());
        checkZipContainsFileWithContents(result, "Main.java", stringToByteArray("mornings"));
        checkZipContainsFileWithContents(result, "Doge.java", stringToByteArray("wuff"));
    }

    @Test(expected = RuntimeException.class)
    public void testDecompressFailsWithInvalidInput() {

        Zip.decompress(null);
    }

    private void checkZipContainsFileWithContents(final Map<String, byte[]> zip, final String filename, final byte[] content) {

        assertTrue(zip.containsKey(filename));
        assertArrayEquals(content, zip.get(filename));
    }

    private byte[] stringToByteArray(final String data) {

        return data.getBytes();
    }
}
