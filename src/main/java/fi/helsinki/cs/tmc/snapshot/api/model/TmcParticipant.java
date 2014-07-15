package fi.helsinki.cs.tmc.snapshot.api.model;

public final class TmcParticipant {

    private Long id;
    private String username;

    public void setId(final Long id) {

        this.id = id;
    }

    public void setUsername(final String username) {

        this.username = username;
    }

    public Long getId() {

        return id;
    }

    public String getUsername() {

        return username;
    }
}
