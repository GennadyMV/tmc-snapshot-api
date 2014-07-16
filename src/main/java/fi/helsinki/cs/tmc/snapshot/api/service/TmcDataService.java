package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.app.ApiException;

public interface TmcDataService {

    String findUsername(String instance, long id) throws ApiException;

}
