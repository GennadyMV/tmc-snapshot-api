package fi.helsinki.cs.tmc.snapshot.api.controller;

import java.util.EmptyStackException;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class ExceptionControllerTest {

    private ExceptionController exceptionController;

    @Before
    public void setUp() {

        exceptionController = new ExceptionController();
    }

    @Test
    public void returnsErrorWithCorrectMessageForGenericException() {

        assertEquals("Something went wrong.", exceptionController.handleException(new Exception()).getError());
    }

    @Test
    public void returnsErrorWithCorrectMessageForExceptionWithCause() {

        assertEquals("Something went wrong.", exceptionController.handleException(new Exception(new EmptyStackException())).getError());
    }

    @Test
    public void returnsErrorWithCorrectMessageForNotFoundException() {

        assertEquals("Not found.", exceptionController.handleNotFoundException().getError());
    }

    @Test
    public void returnsErrorWithCorrectMessageForNotAcceptableMediaType() {

        assertEquals("Unsupported media type for request.", exceptionController.handleHttpMediaTypeNotAcceptableException().getError());
    }
}
