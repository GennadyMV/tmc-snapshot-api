package fi.helsinki.cs.tmc.snapshot.api.controller;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/participants", produces = "application/json")
public class ParticipantController {

    @RequestMapping(method = RequestMethod.GET)
    public String getParticipants() {

        return "Participants";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{participant}")
    public String getParticipant(@PathVariable final Long participant) {

        return "Participant " + participant;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{participant}/snapshots")
    public String getSnapshots(@PathVariable final Long participant) {

        return "Participant " + participant + "'s snapshots";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{participant}/snapshots/{snapshot}")
    public String getSnapshot(@PathVariable final Long participant, @PathVariable final Long snapshot) {

        return "Participant " + participant + "'s snapshot " + snapshot;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{participant}/snapshots/{snapshot}/files")
    public String getFiles(@PathVariable final Long participant, @PathVariable final Long snapshot) {

        return "Participant " + participant + "'s snapshot " + snapshot + "'s files";
    }

    @RequestMapping(method = RequestMethod.GET,
                    value = "/{participant}/snapshots/{snapshot}/files/**",
                    produces = "text/plain")
    public String getFile(final HttpServletRequest request, @PathVariable final Long participant, @PathVariable final Long snapshot) throws UnsupportedEncodingException {

        final String url = request.getRequestURI();
        final String separator = "/files/";
        final String path = url.substring(url.indexOf(separator) + separator.length());

        return "Participant " + participant + "'s snapshot " + snapshot + "'s file " + path;
    }

}
