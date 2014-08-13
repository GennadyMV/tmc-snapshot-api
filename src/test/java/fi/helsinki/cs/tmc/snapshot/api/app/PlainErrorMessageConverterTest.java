package fi.helsinki.cs.tmc.snapshot.api.app;

import fi.helsinki.cs.tmc.snapshot.api.model.Error;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;

import static org.junit.Assert.*;

public final class PlainErrorMessageConverterTest {

    private PlainErrorMessageConverter plainErrorMessageConverter;

    @Before
    public void setUp() {

        plainErrorMessageConverter = new PlainErrorMessageConverter();
    }

    @Test
    public void testPlainErroMessageConverterExtendsAbstractHttpMessageConverterForClassError() {

        assertTrue(plainErrorMessageConverter instanceof AbstractHttpMessageConverter);
        assertTrue(plainErrorMessageConverter.canRead(Error.class, MediaType.TEXT_PLAIN));
    }

    @Test
    public void testPlainErrorMessageConverterSupportsErrorClass() {

        assertTrue(plainErrorMessageConverter.supports(Error.class));
    }

    @Test
    public void testPlainErrorMessageConverterDoesntSupportStringClass() {

        assertFalse(plainErrorMessageConverter.supports(String.class));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testReadInternalThrowUnsupportedOperationException() throws IOException {

        plainErrorMessageConverter.readInternal(null, null);
    }

    @Test
    public void testWriteInternalWritesErrorToOutpuMessage() throws Exception {

        final MockHttpOutputMessage outputMessage = new MockHttpOutputMessage();
        plainErrorMessageConverter.writeInternal(new Error("Test"), outputMessage);

        assertEquals("Test", outputMessage.getBodyAsString());
    }

    @Test(expected = NullPointerException.class)
    public void testWriteInternalShouldThrowNullPointerExceptionWhenOutputMessageIsNull() throws Exception {

        plainErrorMessageConverter.writeInternal(new Error("Test"), null);
    }

    @Test(expected = NullPointerException.class)
    public void testWriteInternalShouldThrowNullPointerExceptionWhenErrorIsNull() throws Exception {

        plainErrorMessageConverter.writeInternal(null, new MockHttpOutputMessage());
    }

    @Test(expected = NullPointerException.class)
    public void testWriteInternalShouldThrowNullPointerExceptionWhenOutputMessageAndErrorAreNull() throws IOException {

        plainErrorMessageConverter.writeInternal(null, null);
    }
}
