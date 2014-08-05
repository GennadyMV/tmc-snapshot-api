package fi.helsinki.cs.tmc.snapshot.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public final class Course {

    private final Long id;
    private final String name;

    @JsonCreator
    public Course(@JsonProperty("id") final Long id, @JsonProperty("name") final String name) {

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

        return 59 * 7 + Objects.hashCode(this.name);
    }

    @Override
    public boolean equals(final Object object) {

        if (object == null) {
            return false;
        }

        if (getClass() != object.getClass()) {
            return false;
        }

        final Course other = (Course) object;

        return name.equals(other.getName());
    }
}
