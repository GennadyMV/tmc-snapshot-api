package fi.helsinki.cs.tmc.snapshot.api.model;

public class SimpleParticipant extends AbstractBase64Identifier {

    private final String username;

    public SimpleParticipant(final String username) {

        super(username);
        this.username = username;
    }

    public String getUsername() {

        return username;
    }
}
