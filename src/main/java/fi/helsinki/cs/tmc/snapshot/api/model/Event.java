package fi.helsinki.cs.tmc.snapshot.api.model;

import java.util.Map;

public final class Event {

    private final String id;
    private final String eventType;
    private final long happendAt;
    private final Map<String, Object> metadata;

    public Event(final String id, final String eventType, final long happendAt, final Map<String, Object> metadata) {

        this.id = id;
        this.eventType = eventType;
        this.happendAt = happendAt;
        this.metadata = metadata;
    }

    public String getId() {

        return id;
    }

    public String getEventType() {

        return eventType;
    }

    public long getHappendAt() {

        return happendAt;
    }

    public Map<String, Object> getMetadata() {

        return metadata;
    }
}
