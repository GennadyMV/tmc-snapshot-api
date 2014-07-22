package fi.helsinki.cs.tmc.snapshot.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class TmcParticipant {

    private Long id;
    private String username;

    public void setId(final Long id) {

        this.id = id;
    }

    @JsonProperty
    public void setUsername(final String username) {

        this.username = username;
    }

    public Long getId() {

        return id;
    }

    @JsonIgnore
    public String getUsername() {

        return username;
    }
}
