package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.exception.NotFoundException;
import fi.helsinki.cs.tmc.snapshot.api.model.Instance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public final class DefaultInstanceService implements InstanceService {

    @Value("#{'${spyware.instances}'.split(', ')}")
    private List<String> spywareInstances;

    private Collection<Instance> getInstances() {

        final List<Instance> instances = new ArrayList<>();

        for (String instance : spywareInstances) {
            instances.add(new Instance(instance));
        }

        Collections.sort(instances);

        return instances;
    }

    @Override
    public Collection<Instance> findAll() {

        return getInstances();
    }

    @Override
    public Instance find(final String id) {

        for (Instance instance : getInstances()) {

            if (instance.getId().equals(id)) {
                return instance;
            }
        }

        throw new NotFoundException();
    }
}
