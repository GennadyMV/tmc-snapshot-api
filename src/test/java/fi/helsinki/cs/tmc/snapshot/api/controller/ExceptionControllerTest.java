package fi.helsinki.cs.tmc.snapshot.api.controller;

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
    public void returnsErrorWithCorrectMessageForGenericException() throws Exception {

        assertEquals("Something went wrong.", exceptionController.handleException(new Exception()).getError());
    }

    @Test
    public void returnsErrorWithCorrectMessageForNotFoundException() throws Exception {

        assertEquals("Not found.", exceptionController.handleNotFoundException().getError());
    }
}
