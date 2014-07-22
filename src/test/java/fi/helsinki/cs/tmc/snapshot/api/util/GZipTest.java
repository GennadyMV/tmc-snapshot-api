package fi.helsinki.cs.tmc.snapshot.api.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class GZipTest {

    @Test
    public void testDecompressTestString() {

        // GZip encoded String 'test'
        final byte[] content = {(byte) 0x1f, (byte) 0x8b, (byte) 0x08, (byte) 0x00,
                                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                                (byte) 0x00, (byte) 0x03, (byte) 0x2b, (byte) 0x49,
                                (byte) 0x2d, (byte) 0x2e, (byte) 0x01, (byte) 0x00,
                                (byte) 0x0c, (byte) 0x7e, (byte) 0x7f, (byte) 0xd8,
                                (byte) 0x04, (byte) 0x00, (byte) 0x00, (byte) 0x00};


        final byte[] expResult = stringToByteArray("test");

        assertArrayEquals(expResult, GZip.decompress(content));
    }

    @Test
    public void testDecompressJsonString() {

        // GZip encodes JSON String '{"json":"test"}'
        final byte[] content = {(byte) 0x1f, (byte) 0x8b, (byte) 0x08, (byte) 0x00,
                                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                                (byte) 0x00, (byte) 0x03, (byte) 0xab, (byte) 0x56,
                                (byte) 0xca, (byte) 0x2a, (byte) 0xce, (byte) 0xcf,
                                (byte) 0x53, (byte) 0xb2, (byte) 0x52, (byte) 0x2a,
                                (byte) 0x49, (byte) 0x2d, (byte) 0x2e, (byte) 0x51,
                                (byte) 0xaa, (byte) 0x05, (byte) 0x00, (byte) 0x11,
                                (byte) 0x84, (byte) 0x36, (byte) 0xca, (byte) 0x0f,
                                (byte) 0x00, (byte) 0x00, (byte) 0x00};


        final byte[] expResult = stringToByteArray("{\"json\":\"test\"}");

        assertArrayEquals(expResult, GZip.decompress(content));
    }

    @Test(expected = RuntimeException.class)
    public void testDecompressFailsWithInvalidInput() {

        GZip.decompress(null);
    }

    private byte[] stringToByteArray(final String data) {

        return data.getBytes();
    }
}
