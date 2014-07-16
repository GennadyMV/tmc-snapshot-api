package fi.helsinki.cs.tmc.snapshot.api.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

public interface ServerDataService {

    byte[] getData(final String event, final String instance, final String username) throws IOException, URISyntaxException;

    InputStream getIndex(final String instance, final String username) throws IOException, URISyntaxException;

}
