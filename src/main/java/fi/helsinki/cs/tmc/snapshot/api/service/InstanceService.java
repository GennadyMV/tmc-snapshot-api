package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.model.Instance;

import java.util.Collection;

public interface InstanceService {

    Collection<Instance> findAll();
    Instance find(String id);

}
