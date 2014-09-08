package fi.helsinki.cs.tmc.snapshot.api.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class SnapshotFileTest {

    @Test
    public void constructorSetsValues() {

        final SnapshotFile file = new SnapshotFile("c3JjL215UGF0aA", "src/myPath", "myContent");

        assertEquals("src/myPath", file.getPath());
        assertEquals("myContent", file.getContent());
        assertEquals("myPath", file.getName());
        assertEquals("c3JjL215UGF0aA", file.getId());
    }

    @Test
    public void comparatorUsesIdsToCompare() {

        final SnapshotFile f1 = new SnapshotFile("1", "src/path", "content");
        final SnapshotFile f2 = new SnapshotFile("2", "src/path", "content");
        final SnapshotFile f3 = new SnapshotFile("3", "src/path", "content");

        assertEquals("1".compareTo("2"), f1.compareTo(f2));
        assertEquals("1".compareTo("3"), f1.compareTo(f3));
        assertEquals("2".compareTo("3"), f2.compareTo(f3));
        assertEquals("3".compareTo("3"), f3.compareTo(f3));
    }

    @Test
    public void shouldSortSnapshotFilesCorrectly() {

        final SnapshotFile f1 = new SnapshotFile("student", "src/yourPath", "yourContent");
        final SnapshotFile f2 = new SnapshotFile("admin", "src/myPath", "myContent");
        final SnapshotFile f3 = new SnapshotFile("user", "src/ourPath", "ourContent");

        final List<SnapshotFile> files = new ArrayList<>();

        files.add(f1);
        files.add(f2);
        files.add(f3);

        Collections.sort(files);

        assertEquals(f2, files.get(0));
        assertEquals(f1, files.get(1));
        assertEquals(f3, files.get(2));
    }
}
