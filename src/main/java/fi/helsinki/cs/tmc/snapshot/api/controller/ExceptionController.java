package fi.helsinki.cs.tmc.snapshot.api.controller;

import fi.helsinki.cs.tmc.snapshot.api.exception.NotFoundException;
import fi.helsinki.cs.tmc.snapshot.api.model.Error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public final class ExceptionController {

    private static final Logger LOG = LoggerFactory.getLogger(ExceptionController.class);

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Error handleException(final Exception exception) {

        final Throwable cause = exception.getCause();

        if (cause == null) {
            LOG.error("{}: {}.", exception.getClass().getName(), exception.getMessage());
        } else {
            LOG.error("{}: {} caused by {}.", exception.getClass().getName(), exception.getMessage(), cause.getMessage());
        }

        return new Error("Something went wrong.");
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ResponseBody
    public Error handleHttpMediaTypeNotAcceptableException() {

        return new Error("Unsupported media type for request.");
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Error handleNotFoundException() {

        return new Error("Not found.");
    }
}
