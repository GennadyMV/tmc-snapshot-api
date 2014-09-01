package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotLevel;
import fi.helsinki.cs.tmc.snapshot.api.util.CodeLevelEventProcessor;
import fi.helsinki.cs.tmc.snapshot.api.util.KeyLevelEventProcessor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ CodeLevelEventProcessor.class, KeyLevelEventProcessor.class })
public class DefaultEventProcessorServiceTest {

    @Mock
    private CodeLevelEventProcessor codeLevelEventProcessor;

    @Mock
    private KeyLevelEventProcessor keyLevelEventProcessor;

    @InjectMocks
    private DefaultEventProcessorService eventProcessorService;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldProcessEventsAtCodeLevel() {

        eventProcessorService.processEvents(null, SnapshotLevel.CODE);

        verify(codeLevelEventProcessor).process(null);
    }

    @Test
    public void shouldProcessEventsAtKeyLevel() {

        eventProcessorService.processEvents(null, SnapshotLevel.KEY);

        verify(keyLevelEventProcessor).process(null);
    }
}
