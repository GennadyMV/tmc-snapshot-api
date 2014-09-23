package fi.helsinki.cs.tmc.snapshot.api.model;

public enum SnapshotLevel {

    KEY, CODE, RAW;

    public static SnapshotLevel fromString(final String level) {

        return valueOf(level.toUpperCase());
    }
}
