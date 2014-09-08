package fi.helsinki.cs.tmc.snapshot.api.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class InstanceTest {

    private static final String ID = "mooc";

    private Instance instance;

    @Before
    public void setUp() {

        instance = new Instance(ID);
    }

    @Test
    public void constructorSetsId() {

        assertEquals(ID, instance.getId());
    }

    @Test
    public void comparatorUsesIdsToCompare() {

        final Instance i1 = new Instance("1");
        final Instance i2 = new Instance("2");
        final Instance i3 = new Instance("3");

        assertEquals("1".compareTo("2"), i1.compareTo(i2));
        assertEquals("1".compareTo("3"), i1.compareTo(i3));
        assertEquals("2".compareTo("3"), i2.compareTo(i3));
        assertEquals("3".compareTo("3"), i3.compareTo(i3));
    }
}
