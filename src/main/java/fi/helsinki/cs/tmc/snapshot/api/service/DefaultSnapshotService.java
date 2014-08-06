package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.exception.NotFoundException;
import fi.helsinki.cs.tmc.snapshot.api.model.Snapshot;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotEvent;
import fi.helsinki.cs.tmc.snapshot.api.util.EventProcessor;
import fi.helsinki.cs.tmc.snapshot.api.util.EventTransformer;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class DefaultSnapshotService implements SnapshotService {

    @Autowired
    private ExerciseService exerciseService;

    @Autowired
    private EventProcessor eventProcessor;

    @Autowired
    private EventTransformer eventTransformer;

    @Override
    public List<Snapshot> findAll(final String instance,
                                  final String userId,
                                  final String courseId,
                                  final String exerciseId) throws IOException {

        final Collection<SnapshotEvent> events = exerciseService.find(instance, userId, courseId, exerciseId)
                                                                .getSnapshotEvents();

        eventProcessor.process(events);

        final List<Snapshot> snapshots = eventTransformer.toSnapshotList(events);

        if (snapshots == null) {
            throw new NotFoundException();
        }

        return snapshots;
    }

    @Override
    public Snapshot find(final String instance,
                         final String userId,
                         final String courseId,
                         final String exerciseId,
                         final Long snapshotId) throws IOException {

        final List<Snapshot> snapshots = findAll(instance, userId, courseId, exerciseId);

        for (Snapshot snapshot : snapshots) {
            if (snapshot.getId().equals(snapshotId)) {
                return snapshot;
            }
        }

        throw new NotFoundException();
    }
}
