package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.exception.NotFoundException;
import fi.helsinki.cs.tmc.snapshot.api.model.Instance;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public final class DefaultInstanceService implements InstanceService {

    @Value("#{'${spyware.instances}'.split(', ')}")
    private List<String> instanceIds;

    private final Map<String, Instance> instances = new TreeMap<>();

    @PostConstruct
    private void initialise() {

        for (String instanceId : instanceIds) {
            instances.put(instanceId, new Instance(instanceId));
        }
    }

    @Override
    public Collection<Instance> findAll() {

        return instances.values();
    }

    @Override
    public Instance find(final String id) {

        if (!instances.containsKey(id)) {
            throw new NotFoundException();
        }

        return instances.get(id);
    }
}
