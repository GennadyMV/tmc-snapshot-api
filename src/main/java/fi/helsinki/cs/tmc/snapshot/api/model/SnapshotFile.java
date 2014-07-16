package fi.helsinki.cs.tmc.snapshot.api.model;

import com.fasterxml.jackson.annotation.JsonView;

import fi.helsinki.cs.tmc.snapshot.api.model.view.View;

public final class SnapshotFile {

    @JsonView(View.IdOnly.class)
    private final String path;

    @JsonView(View.Complete.class)
    private final String content;

    public SnapshotFile(final String path, final String content) {

        this.path = path;
        this.content = content;
    }

    public String getPath() {

        return path;
    }

    public String getContent() {

        return content;
    }
}
