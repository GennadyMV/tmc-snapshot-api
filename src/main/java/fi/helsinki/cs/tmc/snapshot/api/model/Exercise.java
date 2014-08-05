package fi.helsinki.cs.tmc.snapshot.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public final class Exercise {

    private final Long id;
    private final String name;

    @JsonCreator
    public Exercise(@JsonProperty("id") final Long id, @JsonProperty("name") final String name) {

        this.id = id;
        this.name = name;
    }

    public Long getId() {

        return id;
    }

    public String getName() {

        return name;
    }

    @Override
    public int hashCode() {

        return 83 * 7 + Objects.hashCode(this.name);
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

        return name.equals(other.getName());
    }
}
