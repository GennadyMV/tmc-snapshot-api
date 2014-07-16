package fi.helsinki.cs.tmc.snapshot.api.model;

import com.fasterxml.jackson.annotation.JsonView;

import fi.helsinki.cs.tmc.snapshot.api.model.view.View;

import java.util.HashMap;
import java.util.Map;

public final class SnapshotEvent implements Comparable<SnapshotEvent> {

    @JsonView(View.Summary.class)
    private String exerciseName;

    @JsonView(View.Summary.class)
    private String eventType;

    @JsonView(View.IdOnly.class)
    private String happenedAt;

    @JsonView(View.Complete.class)
    private String systemNanotime;

    @JsonView(View.Complete.class)
    private String metadata;

    @JsonView(View.Complete.class)
    private final Map<String, String> files = new HashMap<>();

    @JsonView(View.Complete.class)
    private String data;

    @JsonView(View.Complete.class)
    public boolean isProjectActionEvent() {

        return eventType.contains("project_action");
    }

    public void setExerciseName(final String exerciseName) {

        this.exerciseName = exerciseName;
    }

    public String getExerciseName() {

        return exerciseName;
    }

    public void setEventType(final String eventType) {

        this.eventType = eventType;
    }

    public void setData(final String data) {

        this.data = data;
    }

    public String getData() {

        return data;
    }

    public void setHappenedAt(final String happenedAt) {

        this.happenedAt = happenedAt;
    }

    public void setSystemNanotime(final String systemNanotime) {

        this.systemNanotime = systemNanotime;
    }

    public String getHappenedAt() {

        return happenedAt;
    }

    public void setMetadata(final String metadata) {

        this.metadata = metadata;
    }

    public String getMetadata() {

        return metadata;
    }

    public Map<String, String> getFiles() {

        return files;
    }

    @Override
    public int compareTo(final SnapshotEvent event) {

        if (!happenedAt.equals(event.happenedAt)) {
            return new Long(Long.parseLong(getHappenedAt())).compareTo(Long.parseLong(event.getHappenedAt()));
        }

        return systemNanotime.compareTo(event.systemNanotime);
    }
}
