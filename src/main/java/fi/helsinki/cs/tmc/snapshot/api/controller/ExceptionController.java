package fi.helsinki.cs.tmc.snapshot.api.controller;

import fi.helsinki.cs.tmc.snapshot.api.model.ErrorMessage;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(IOException.class)
    @ResponseBody
    public ErrorMessage handleIOException(final IOException ex) {

        Logger logger = LoggerFactory.getLogger(ExceptionController.class);
        logger.error(ex.getMessage());

        return new ErrorMessage("Something went wrong.");
    }
}
