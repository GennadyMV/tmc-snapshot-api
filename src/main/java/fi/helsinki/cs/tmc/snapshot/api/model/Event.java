package fi.helsinki.cs.tmc.snapshot.api.model;

import java.util.Date;
import java.util.Map;

public final class Event {

    private final String id;
    private final String eventType;
    private final Date timestamp;
    private final Map<String, Object> metadata;

    public Event(final String id,
                 final String eventType,
                 final Date timestamp,
                 final Map<String, Object> metadata) {

        this.id = id;
        this.eventType = eventType;
        this.timestamp = timestamp;
        this.metadata = metadata;
    }

    public String getId() {

        return id;
    }

    public String getEventType() {

        return eventType;
    }

    public Date getTimestamp() {

        return timestamp;
    }

    public Map<String, Object> getMetadata() {

        return metadata;
    }
}
