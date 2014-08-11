
package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.model.Participant;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.codec.binary.Base64;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DefaultParticipantServiceTest {

    private static final String INSTANCE = "testInstance";
    private static final String USERNAME = "testUsername";
    private static final String ID = new String(Base64.encodeBase64(USERNAME.getBytes()));

    @Mock
    private SnapshotEventService snapshotEventService;

    @Mock
    private SnapshotOrganiserService snapshotOrganiser;

    @InjectMocks
    private DefaultParticipantService participantService;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void returnsParticipantWithCorrectUsername() throws IOException {

        final Participant participant = participantService.find(INSTANCE, ID);

        assertEquals(ID, participant.getId());
        assertEquals(USERNAME, participant.getUsername());
    }

    @Test
    public void retrievesEventsFromSnapshotEventServiceAndOrganizesThem() throws IOException {

        final Collection<SnapshotEvent> events = new ArrayList<>();
        when(snapshotEventService.findAll(INSTANCE, USERNAME)).thenReturn(events);

        final Participant participant = participantService.find(INSTANCE, ID);

        verify(snapshotEventService).findAll(INSTANCE, USERNAME);
        verify(snapshotOrganiser).organise(participant, events);
    }
}
