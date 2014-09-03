package fi.helsinki.cs.tmc.snapshot.api.controller;

import fi.helsinki.cs.tmc.snapshot.api.model.Instance;
import fi.helsinki.cs.tmc.snapshot.api.service.InstanceService;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/", produces = "application/json")
public class InstanceController {

    @Autowired
    private InstanceService instanceService;

    @RequestMapping(method = RequestMethod.GET)
    public Collection<Instance> list() {

        return instanceService.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, value = "{id}")
    public Instance read(@PathVariable final String id) {

        return instanceService.find(id);
    }
}
