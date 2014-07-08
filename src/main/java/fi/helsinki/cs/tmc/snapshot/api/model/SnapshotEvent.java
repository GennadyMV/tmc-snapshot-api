package fi.helsinki.cs.tmc.snapshot.api.model;

import java.util.HashMap;
import java.util.Map;

public final class SnapshotEvent implements Comparable<SnapshotEvent> {

    private String exerciseName;
    private String eventType;
    private String data;
    private String happenedAt;
    private String systemNanotime;
    private String metadata;
    private final Map<String, String> files = new HashMap<>();

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
            return new Long(Long.parseLong(happenedAt)).compareTo(Long.parseLong(event.happenedAt));
        }

        return systemNanotime.compareTo(event.systemNanotime);
    }
}
