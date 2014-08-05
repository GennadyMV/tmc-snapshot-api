package fi.helsinki.cs.tmc.snapshot.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Snapshot implements Comparable<Snapshot> {

    private final Long id;
    private final Course course;
    private final Exercise exercise;
    private final Map<String, SnapshotFile> files;
    private final Date timestamp;

    @JsonIgnore
    private final boolean fromCompleteSnapshot;

    @JsonCreator
    public Snapshot(@JsonProperty("id") final Long id,
                    @JsonProperty("course") final Course course,
                    @JsonProperty("exercise") final Exercise exercise,
                    @JsonProperty("files") final List<SnapshotFile> files) {

        this.id = id;
        this.course = course;
        this.exercise = exercise;
        this.timestamp = new Date(id);
        this.fromCompleteSnapshot = false;

        this.files = new HashMap<>();

        for (SnapshotFile file : files) {
            this.files.put(file.getPath(), file);
        }
    }

    public Snapshot(final Long id,
                    final Course course,
                    final Exercise exercise,
                    final Map<String, SnapshotFile> files) {

        this(id, course, exercise, files, false);
    }

    public Snapshot(final Long id,
                    final Course course,
                    final Exercise exercise,
                    final Map<String, SnapshotFile> files,
                    final boolean fromCompleteSnapshot) {

        this.id = id;
        this.course = course;
        this.exercise = exercise;
        this.timestamp = new Date(id);
        this.fromCompleteSnapshot = fromCompleteSnapshot;
        this.files = files;
    }

    public Long getId() {

        return id;
    }

    public Course getCourse() {

        return course;
    }

    public Exercise getExercise() {

        return exercise;
    }

    public void addFile(final SnapshotFile file) {

        files.put(file.getPath(), file);
    }

    public SnapshotFile getFile(final String path) {

        return files.get(path);
    }

    public Collection<SnapshotFile> getFiles() {

        return files.values();
    }

    public Date getTimestamp() {

        return timestamp;
    }

    public boolean isFromCompleteSnapshot() {

        return fromCompleteSnapshot;
    }

    @Override
    public int compareTo(final Snapshot other) {

        return this.timestamp.compareTo(other.timestamp);
    }
}
