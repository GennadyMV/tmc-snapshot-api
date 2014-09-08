package fi.helsinki.cs.tmc.snapshot.api.model;

public final class Instance implements Comparable<Instance>  {

    private final String id;

    public Instance(final String id) {

        this.id = id;
    }

    public String getId() {

        return id;
    }

    public String getName() {

        return id;
    }

    @Override
    public int compareTo(final Instance other) {

        return id.compareTo(other.getId());
    }
}
