package fi.helsinki.cs.tmc.snapshot.api.model;

public enum SnapshotLevel {

    KEY, CODE;

    public static SnapshotLevel fromString(final String string) {

        return valueOf(string.toUpperCase());
    }
}
