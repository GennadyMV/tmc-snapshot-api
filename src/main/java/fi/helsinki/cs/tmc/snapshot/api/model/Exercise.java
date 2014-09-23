package fi.helsinki.cs.tmc.snapshot.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fi.helsinki.cs.tmc.snapshot.api.spyware.model.SnapshotEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.apache.commons.codec.binary.Base64;

public final class Exercise implements Comparable<Exercise> {

    private final String id;
    private final String name;
    private final List<SnapshotEvent> snapshotEvents;

    public Exercise(final String name) {

        this.id = Base64.encodeBase64URLSafeString(name.getBytes());
        this.name = name;
        this.snapshotEvents = new ArrayList<>();
    }

    public String getId() {

        return id;
    }

    public String getName() {

        return name;
    }

    @JsonIgnore
    public Collection<SnapshotEvent> getSnapshotEvents() {

        return snapshotEvents;
    }

    public void addSnapshotEvent(final SnapshotEvent snapshot) {

        snapshotEvents.add(snapshot);
    }

    @Override
    public int hashCode() {

        return 83 * 7 + Objects.hashCode(name);
    }

    @Override
    public boolean equals(final Object object) {

        if (object == null) {
            return false;
        }

        if (getClass() != object.getClass()) {
            return false;
        }

        final Exercise other = (Exercise) object;

        return id.equals(other.getId());
    }

    @Override
    public int compareTo(final Exercise other) {

        return id.compareTo(other.getId());
    }
}
