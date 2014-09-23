package fi.helsinki.cs.tmc.snapshot.api.model;

import org.apache.commons.codec.binary.Base64;

public abstract class AbstractBase64Identifier implements StringIdentifier, Comparable<StringIdentifier> {

    private final String id;

    public AbstractBase64Identifier(final String id) {

        this.id = Base64.encodeBase64URLSafeString(id.getBytes());
    }

    @Override
    public String getId() {

        return id;
    }

    @Override
    public int compareTo(final StringIdentifier other) {

        return id.compareTo(other.getId());
    }
}
