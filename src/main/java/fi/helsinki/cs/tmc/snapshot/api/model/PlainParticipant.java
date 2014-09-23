package fi.helsinki.cs.tmc.snapshot.api.model;

public class PlainParticipant extends AbstractBase64Identifier {

    private final String username;

    public PlainParticipant(final String username) {

        super(username);
        this.username = username;
    }

    public String getUsername() {

        return username;
    }
}
