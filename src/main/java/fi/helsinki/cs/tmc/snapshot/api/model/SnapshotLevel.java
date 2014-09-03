package fi.helsinki.cs.tmc.snapshot.api.model;

public enum SnapshotLevel {

    KEY, CODE;

    public static SnapshotLevel fromString(final String level) {

        return valueOf(level.toUpperCase());
    }
}
