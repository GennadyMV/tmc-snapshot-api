package fi.helsinki.cs.tmc.snapshot.api.controller;

import fi.helsinki.cs.tmc.snapshot.api.exception.NotFoundException;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotFile;
import fi.helsinki.cs.tmc.snapshot.api.service.SnapshotService;
import fi.helsinki.cs.tmc.snapshot.api.service.TmcService;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "{instance}/participants", produces = "application/json")
public final class SnapshotFileController {

    @Autowired
    private SnapshotService snapshotService;

    @Autowired
    private TmcService tmcService;

    @RequestMapping(method = RequestMethod.GET, value = "{participant}/snapshots/{snapshot}/files")
    public Collection<SnapshotFile> list(@PathVariable final String instance,
                                   @PathVariable final Long participant,
                                   @PathVariable final Long snapshot) throws IOException {

        final String username = tmcService.findUsernameById(instance, participant);

        if (username == null) {
            throw new NotFoundException();
        }

        return snapshotService.find(instance, username, snapshot).getFiles();
    }

    @RequestMapping(method = RequestMethod.GET,
                    value = "{participant}/snapshots/{snapshot}/files/**",
                    produces = "text/plain")
    public String read(final HttpServletRequest request,
                       @PathVariable final String instance,
                       @PathVariable final Long participant,
                       @PathVariable final Long snapshot) throws IOException {

        final String url = request.getRequestURI();
        final String separator = "/files/";
        final String path = "/" + url.substring(url.indexOf(separator) + separator.length());

        final String username = tmcService.findUsernameById(instance, participant);

        if (username == null) {
            throw new NotFoundException();
        }

        final SnapshotFile file = snapshotService.find(instance, username, snapshot).getFile(path);
        
        if (file == null) {
            throw new NotFoundException();
        }

        return file.getContent();
    }
}
