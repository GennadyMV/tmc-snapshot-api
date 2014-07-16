package fi.helsinki.cs.tmc.snapshot.api.controller;

import fi.helsinki.cs.tmc.snapshot.api.app.ApiException;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotEvent;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotFile;
import fi.helsinki.cs.tmc.snapshot.api.model.view.View;
import fi.helsinki.cs.tmc.snapshot.api.service.SnapshotService;
import fi.helsinki.cs.tmc.snapshot.api.service.TmcDataService;
import fi.helsinki.cs.tmc.snapshot.api.util.JsonViewWriter;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
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
    private TmcDataService tmcData;

    @RequestMapping(method = RequestMethod.GET, value = "{participant}/snapshots/{snapshot}/files")
    public String list(@PathVariable final Long participant, @PathVariable final Long snapshot) {

        final SnapshotEvent event;
        try {
            final String username = tmcData.findUsername("", participant);
            event = snapshots.find("/hy/", username, snapshot);
        } catch (ApiException ex) {
            Logger.getLogger(SnapshotFileController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        final List<SnapshotFile> files = new ArrayList<>();
        for (Entry<String, String> entry : event.getFiles().entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
            files.add(new SnapshotFile(entry.getKey(), entry.getValue()));
        }

        return JsonViewWriter.getView(files, View.Summary.class);
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
            event = snapshots.find("/hy/", username, snapshot);
        } catch (ApiException ex) {
            Logger.getLogger(SnapshotFileController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        final List<SnapshotFile> files = new ArrayList<>();
        for (Entry<String, String> entry : event.getFiles().entrySet()) {
            if (entry.getKey().equals(path) || entry.getKey().equals("/" + path)) {
                final SnapshotFile file = new SnapshotFile(entry.getKey(), entry.getValue());
                return JsonViewWriter.getView(file, View.Complete.class);
            }
        }

        return null;
    }
}
