package fi.helsinki.cs.tmc.snapshot.api.model;

import com.fasterxml.jackson.annotation.JsonView;

import fi.helsinki.cs.tmc.snapshot.api.model.views.Views;

public final class SnapshotFile {

    @JsonView(Views.IdOnly.class)
    private final String path;

    @JsonView(Views.Complete.class)
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
