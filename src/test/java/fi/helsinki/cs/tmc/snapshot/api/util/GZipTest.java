package fi.helsinki.cs.tmc.snapshot.api.util;

import java.io.IOException;
import java.lang.reflect.Constructor;
import org.junit.Test;

import static org.junit.Assert.*;

public final class GZipTest {

    private byte[] stringToByteArray(final String data) {

        return data.getBytes();
    }

    @Test
    public void testDecompressTestString() throws IOException {

        // GZip encoded String 'test'
        final byte[] content = { (byte) 0x1f, (byte) 0x8b, (byte) 0x08, (byte) 0x00,
                                 (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                                 (byte) 0x00, (byte) 0x03, (byte) 0x2b, (byte) 0x49,
                                 (byte) 0x2d, (byte) 0x2e, (byte) 0x01, (byte) 0x00,
                                 (byte) 0x0c, (byte) 0x7e, (byte) 0x7f, (byte) 0xd8,
                                 (byte) 0x04, (byte) 0x00, (byte) 0x00, (byte) 0x00 };

        final byte[] expResult = stringToByteArray("test");

        assertArrayEquals(expResult, GZip.decompress(content));
    }

    @Test
    public void testDecompressJsonString() throws IOException {

        // GZip encodes JSON String '{"json":"test"}'
        final byte[] content = { (byte) 0x1f, (byte) 0x8b, (byte) 0x08, (byte) 0x00,
                                 (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                                 (byte) 0x00, (byte) 0x03, (byte) 0xab, (byte) 0x56,
                                 (byte) 0xca, (byte) 0x2a, (byte) 0xce, (byte) 0xcf,
                                 (byte) 0x53, (byte) 0xb2, (byte) 0x52, (byte) 0x2a,
                                 (byte) 0x49, (byte) 0x2d, (byte) 0x2e, (byte) 0x51,
                                 (byte) 0xaa, (byte) 0x05, (byte) 0x00, (byte) 0x11,
                                 (byte) 0x84, (byte) 0x36, (byte) 0xca, (byte) 0x0f,
                                 (byte) 0x00, (byte) 0x00, (byte) 0x00 };

        final byte[] expResult = stringToByteArray("{\"json\":\"test\"}");

        assertArrayEquals(expResult, GZip.decompress(content));
    }

    /*
     * Make sure constructor is private
     */
    @Test(expected = IllegalAccessException.class)
    public void shouldHavePrivateConstructor() throws InstantiationException, IllegalAccessException {

        GZip.class.newInstance();
        fail("Should have private constructor");
    }

    /*
     * Make sure cobertura knows we visited the private constructor...
     */
    @Test
    public void coverageForPrivateConstructor() throws Exception {

        final Constructor<?>[] constructor = GZip.class.getDeclaredConstructors();
        constructor[0].setAccessible(true);
        constructor[0].newInstance((Object[]) null);
    }
}
