package fi.helsinki.cs.tmc.snapshot.api.service;


import fi.helsinki.cs.tmc.snapshot.api.model.Snapshot;
import java.io.IOException;
import java.util.List;

public interface SnapshotDiffMatchPatchService {

    List<Snapshot> patch(List<byte[]> content) throws IOException;

}
