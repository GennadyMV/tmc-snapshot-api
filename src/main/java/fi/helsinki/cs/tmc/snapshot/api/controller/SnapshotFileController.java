package fi.helsinki.cs.tmc.snapshot.api.controller;

import fi.helsinki.cs.tmc.snapshot.api.app.ApiException;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotEvent;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotFile;
import fi.helsinki.cs.tmc.snapshot.api.service.SnapshotService;
import fi.helsinki.cs.tmc.snapshot.api.service.TmcService;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private SnapshotService snapshots;

    @Autowired
    private TmcService tmcData;

    @RequestMapping(method = RequestMethod.GET, value = "{participant}/snapshots/{snapshot}/files")
    public List<SnapshotFile> list(@PathVariable final Long participant, @PathVariable final Long snapshot) {

        try {
            final String username = tmcData.findUsername("", participant);
            return snapshots.find("/hy/", username, snapshot).getFiles();
        } catch (ApiException ex) {
            Logger.getLogger(SnapshotFileController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
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

        final SnapshotEvent event;
        try {
            final String username = tmcData.findUsername("", participant);
            for (SnapshotFile file : snapshots.find("/hy/", username, snapshot).getFiles()) {
                if (file.getPath().equals("/" + path)) {
                    return file.getContent();
                }
            }
        } catch (ApiException ex) {
            Logger.getLogger(SnapshotFileController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        return null;
    }
}
