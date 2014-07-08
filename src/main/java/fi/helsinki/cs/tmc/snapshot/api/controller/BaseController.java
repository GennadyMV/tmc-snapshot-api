package fi.helsinki.cs.tmc.snapshot.api.controller;

import fi.helsinki.cs.tmc.snapshot.api.service.SnapshotService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public final class BaseController {

    @Autowired
    private SnapshotService spywareService;

    @RequestMapping(method = RequestMethod.GET, value = "/")
    @ResponseBody
    public String getRoot() throws Exception {

        spywareService.findAll("/peliohjelmointi/", "Miklu04");

        return "Hello!";
    }
}
