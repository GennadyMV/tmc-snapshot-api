package fi.helsinki.cs.tmc.snapshot.api.controller;

import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotFile;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/participants", produces = "application/json")
public final class SnapshotFileController {

    @RequestMapping(method = RequestMethod.GET, value = "{participant}/snapshots/{snapshot}/files")
    public List<SnapshotFile> list(@PathVariable final Long participant, @PathVariable final Long snapshot) {

        final List<SnapshotFile> files = new ArrayList<>();
        files.add(new SnapshotFile("Example.java", "content"));

        return files;
    }

    @RequestMapping(method = RequestMethod.GET,
                    value = "{participant}/snapshots/{snapshot}/files/**",
                    produces = "text/plain")
    public String read(final HttpServletRequest request,
                       @PathVariable final Long participant,
                       @PathVariable final Long snapshot) throws UnsupportedEncodingException {

        final String url = request.getRequestURI();
        final String separator = "/files/";
        final String path = url.substring(url.indexOf(separator) + separator.length());

        return new SnapshotFile("Example.java", "Content for: " + path).getContent();
    }
}
