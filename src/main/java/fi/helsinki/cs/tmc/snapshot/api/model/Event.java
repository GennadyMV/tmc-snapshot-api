package fi.helsinki.cs.tmc.snapshot.api.model;

public final class Event implements Comparable<Event> {

    private String courseName;
    private String exerciseName;
    private String eventType;
    private String content;
    private String data;
    private String happenedAt;
    private String systemNanotime;
    private EventInformation eventInformation;
    private String metadataString;
    private Metadata metadata;

    public boolean isCodeSnapshotEvent() {

        return eventType.contains("code_snapshot");
    }

    public String getMetadataAsString() {

        return metadataString;
    }

    public void setMetadata(final Metadata metadata) {

        this.metadata = metadata;
    }

    @Override
    public int compareTo(final Event event) {

        if (!happenedAt.equals(event.happenedAt)) {
            return new Long(Long.parseLong(happenedAt)).compareTo(Long.parseLong(event.happenedAt));
        }

        return systemNanotime.compareTo(event.systemNanotime);
    }
}
