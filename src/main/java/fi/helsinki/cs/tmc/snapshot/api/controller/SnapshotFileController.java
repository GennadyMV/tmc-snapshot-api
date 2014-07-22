package fi.helsinki.cs.tmc.snapshot.api.controller;

import fi.helsinki.cs.tmc.snapshot.api.exception.NotFoundException;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotFile;
import fi.helsinki.cs.tmc.snapshot.api.service.SnapshotService;
import fi.helsinki.cs.tmc.snapshot.api.service.TmcService;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/participants", produces = "application/json")
public final class SnapshotFileController {

    @Autowired
    private SnapshotService snapshotService;

    @Autowired
    private TmcService tmcService;

    @RequestMapping(method = RequestMethod.GET, value = "{participant}/snapshots/{snapshot}/files")
    public List<SnapshotFile> list(@PathVariable final Long participant, @PathVariable final Long snapshot) throws IOException {

        final String username = tmcService.findByUsername("", participant);

        if (username == null) {
            throw new NotFoundException();
        }

        return snapshotService.find("/hy/", username, snapshot).getFiles();
    }

    @RequestMapping(method = RequestMethod.GET,
                    value = "{participant}/snapshots/{snapshot}/files/**",
                    produces = "text/plain")
    public String read(final HttpServletRequest request,
                       @PathVariable final Long participant,
                       @PathVariable final Long snapshot) throws IOException {

        final String url = request.getRequestURI();
        final String separator = "/files/";
        final String path = url.substring(url.indexOf(separator) + separator.length());

        final String username = tmcService.findByUsername("", participant);

        if (username == null) {
            throw new NotFoundException();
        }

        for (SnapshotFile file : snapshotService.find("/hy/", username, snapshot).getFiles()) {
            if (file.getPath().equals("/" + path)) {
                return file.getContent();
            }
        }

        throw new NotFoundException();
    }
}
