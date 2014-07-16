package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.app.ApiException;

import java.io.InputStream;

public interface SpywareDataService {

    byte[] getData(final String event, final String instance, final String username) throws ApiException;
    InputStream getIndex(final String instance, final String username) throws ApiException;

}
