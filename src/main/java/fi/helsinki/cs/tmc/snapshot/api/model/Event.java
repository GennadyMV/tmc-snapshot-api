package fi.helsinki.cs.tmc.snapshot.api.model;

public final class Event {

    private final String id;
    private final String eventType;
    private final String metadata;

    public Event(final String id, final String eventType, final String metadata) {

        this.id = id;
        this.eventType = eventType;
        this.metadata = metadata;
    }

    public String getId() {

        return id;
    }

    public String getEventType() {

        return eventType;
    }

    public String getMetadata() {

        return metadata;
    }
}
