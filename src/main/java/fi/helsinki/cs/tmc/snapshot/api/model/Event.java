package fi.helsinki.cs.tmc.snapshot.api.model;

public final class Event implements Comparable<Event> {

    private String exerciseName;
    private String eventType;
    private String data;
    private String happenedAt;
    private String systemNanotime;
    private String metadata;

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

    @Override
    public int compareTo(final Event event) {

        if (!happenedAt.equals(event.happenedAt)) {
            return new Long(Long.parseLong(happenedAt)).compareTo(Long.parseLong(event.happenedAt));
        }

        return systemNanotime.compareTo(event.systemNanotime);
    }
}
