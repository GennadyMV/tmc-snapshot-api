package fi.helsinki.cs.tmc.snapshot.api.model;

public final class TmcParticipant {

    private Long id;
    private String username;

    public void setId(Long id) {

        this.id = id;
    }

    public void setUsername(String username) {

        this.username = username;
    }

    public Long getId() {

        return id;
    }

    public String getUsername() {

        return username;
    }
}
