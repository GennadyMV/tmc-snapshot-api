package fi.helsinki.cs.tmc.snapshot.api.spyware.model;

import java.util.HashMap;
import java.util.Map;

public final class SnapshotEvent implements Comparable<SnapshotEvent> {

    private String courseName;
    private String exerciseName;
    private String eventType;
    private Long happenedAt;
    private Long systemNanotime;
    private String metadata;
    private String data;
    private final Map<String, String> files = new HashMap<>();

    public boolean isProjectActionEvent() {

        return eventType.contains("project_action");
    }

    public boolean isCodeSnapshot() {

        return "code_snapshot".equals(eventType);
    }

    public void setCourseName(final String courseName) {

        this.courseName = courseName;
    }

    public String getCourseName() {

        return courseName;
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

    public String getEventType() {

        return eventType;
    }

    public void setHappenedAt(final Long happenedAt) {

        this.happenedAt = happenedAt;
    }

    public Long getHappenedAt() {

        return happenedAt;
    }

    public void setSystemNanotime(final Long systemNanotime) {

        this.systemNanotime = systemNanotime;
    }

    public Long getSystemNanotime() {

        return systemNanotime;
    }

    public void setMetadata(final String metadata) {

        this.metadata = metadata;
    }

    public String getMetadata() {

        return metadata;
    }

    public void setData(final String data) {

        this.data = data;
    }

    public String getData() {

        return data;
    }

    public Map<String, String> getFiles() {

        return files;
    }

    @Override
    public int compareTo(final SnapshotEvent event) {

        if (!happenedAt.equals(event.happenedAt)) {
            return getHappenedAt().compareTo(event.getHappenedAt());
        }

        return systemNanotime.compareTo(event.systemNanotime);
    }
}
