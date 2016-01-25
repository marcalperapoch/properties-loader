package com.vasplan.utilities.properties.model;

/**
 * Created by marcal.perapoch on 20/03/2015.
 */
public class PropertiesLoaderException extends RuntimeException {

    private final Exception wrappedException;

    public PropertiesLoaderException(Exception e) {
        this.wrappedException = e;
    }

    @Override
    public String toString() {
        return wrappedException.toString();
    }

    @Override
    public synchronized Throwable getCause() {
        return wrappedException.getCause();
    }

    @Override
    public String getMessage() {
        return wrappedException.getMessage();
    }
}
