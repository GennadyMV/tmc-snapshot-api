package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotEvent;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotLevel;
import fi.helsinki.cs.tmc.snapshot.api.util.CodeLevelEventProcessor;
import fi.helsinki.cs.tmc.snapshot.api.util.KeyLevelEventProcessor;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class DefaultEventProcessorService implements EventProcessorService {

    @Autowired
    private CodeLevelEventProcessor codeLevelEventProcessor;

    @Autowired
    private KeyLevelEventProcessor keyLevelEventProcessor;

    @Override
    public void processEvents(final Collection<SnapshotEvent> events, final SnapshotLevel level) {

        if (level == SnapshotLevel.CODE) {
            codeLevelEventProcessor.process(events);
        } else {
            keyLevelEventProcessor.process(events);
        }
    }
}
