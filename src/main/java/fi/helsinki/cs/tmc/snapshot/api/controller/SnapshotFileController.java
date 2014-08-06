package fi.helsinki.cs.tmc.snapshot.api.controller;

import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotFile;
import fi.helsinki.cs.tmc.snapshot.api.service.SnapshotFileService;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "{instance}/participants/{username}/courses/{course}/exercises/{exercise}/snapshots/{snapshot}/files", produces = "application/json")
public final class SnapshotFileController {

    @Autowired
    private SnapshotFileService snapshotFileService;

    @RequestMapping(method = RequestMethod.GET)
    public Collection<SnapshotFile> list(@PathVariable final String instance,
                               @PathVariable final String username,
                               @PathVariable final String course,
                               @PathVariable final String exercise,
                               @PathVariable final Long snapshot) throws IOException {

        return snapshotFileService.findAll(instance, username, course, exercise, snapshot);
    }

    @RequestMapping(method = RequestMethod.GET,
                    value = "**",
                    produces = "text/plain")
    public String read(final HttpServletRequest request,
                       @PathVariable final String instance,
                       @PathVariable final String username,
                       @PathVariable final String course,
                       @PathVariable final String exercise,
                       @PathVariable final Long snapshot) throws IOException {

        final String url = request.getRequestURI();
        final String separator = "/files/";
        final String path = "/" + url.substring(url.indexOf(separator) + separator.length());

        return snapshotFileService.find(instance, username, course, exercise, snapshot, path);
    }
}
