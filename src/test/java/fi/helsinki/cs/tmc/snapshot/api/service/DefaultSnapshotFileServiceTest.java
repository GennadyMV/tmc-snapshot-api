package fi.helsinki.cs.tmc.snapshot.api.service;

import fi.helsinki.cs.tmc.snapshot.api.exception.NotFoundException;
import fi.helsinki.cs.tmc.snapshot.api.model.Snapshot;
import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ DefaultSnapshotService.class, DefaultSnapshotFileService.class, Snapshot.class })
public final class DefaultSnapshotFileServiceTest {

    private static final String FILE = "test.java";

    @Mock
    private DefaultSnapshotService snapshotService;

    @InjectMocks
    private DefaultSnapshotFileService fileService;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldFindSnapshotFile() throws IOException {

        final Map<String, SnapshotFile> files = new HashMap<>();
        files.put(FILE, new SnapshotFile("dGVzdC5qYXZh", FILE, "public class Test { }"));

        final Snapshot snapshot = new Snapshot("11", 3L, files, false);

        when(snapshotService.find(any(String.class),
                                  any(String.class),
                                  any(String.class),
                                  any(String.class),
                                  any(String.class)))
                                 .thenReturn(snapshot);

        final SnapshotFile file = fileService.find("hy", "user", "course", "exercise", "3", "dGVzdC5qYXZh");

        assertEquals(FILE, file.getPath());
        assertEquals(FILE, file.getName());
        assertEquals("dGVzdC5qYXZh", file.getId());
        assertEquals("public class Test { }", file.getContent());
    }

    @Test
    public void shouldFindSnapshotFileContent() throws IOException {

        final Map<String, SnapshotFile> files = new HashMap<>();
        files.put(FILE, new SnapshotFile("id", FILE, "public class Test { }"));

        final Snapshot snapshot = new Snapshot("11", 2L, files, false);

        when(snapshotService.find(any(String.class),
                                  any(String.class),
                                  any(String.class),
                                  any(String.class),
                                  any(String.class)))
                                 .thenReturn(snapshot);

        final String content = fileService.findContent("hy", "user", "course", "exercise", "2", "id");

        assertEquals("public class Test { }", content);
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowExceptionOnNonExistentPath() throws IOException {

        final Snapshot snapshot = new Snapshot("22", 2L, new HashMap<String, SnapshotFile>(), false);

        when(snapshotService.find(any(String.class),
                                  any(String.class),
                                  any(String.class),
                                  any(String.class),
                                  any(String.class)))
                                 .thenReturn(snapshot);

        fileService.find("hy", "user", "course", "exercise", "2", "404.java");
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowExceptionOnNonExistentContent() throws IOException {

        final Map<String, SnapshotFile> files = new HashMap<>();
        files.put("trial.java", new SnapshotFile("id", "trial.java", null));

        final Snapshot snapshot = new Snapshot("33", 2L, files, false);

        when(snapshotService.find(any(String.class),
                                  any(String.class),
                                  any(String.class),
                                  any(String.class),
                                  any(String.class)))
                                 .thenReturn(snapshot);

        fileService.findContent("hy", "user", "course", "exercise", "2", "trial.java");
    }

    @Test
    public void shouldFindAllSnapshotFiles() throws IOException {

        final Map<String, SnapshotFile> files = new HashMap<>();
        files.put("example.java", new SnapshotFile("hd", "example.java", "public class Example { }"));
        files.put("exercise.java", new SnapshotFile("cd", "exercise.java", "public class Exercise { }"));

        final Snapshot snapshot = new Snapshot("44", 2L, files, false);

        when(snapshotService.find(any(String.class),
                                  any(String.class),
                                  any(String.class),
                                  any(String.class),
                                  any(String.class)))
                                 .thenReturn(snapshot);

        final Collection<SnapshotFile> filesCollection = fileService.findAll("mooc", "admin", "java-course", "ex", "2");
        final List<SnapshotFile> snapshotFiles = new ArrayList(filesCollection);

        assertEquals(2, snapshotFiles.size());

        assertEquals("example.java", snapshotFiles.get(0).getPath());
        assertEquals("public class Example { }", snapshotFiles.get(0).getContent());

        assertEquals("exercise.java", snapshotFiles.get(1).getPath());
        assertEquals("public class Exercise { }", snapshotFiles.get(1).getContent());
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowExceptionOnNonExistentSnapshotFiles() throws IOException {

        final Snapshot snapshot = new Snapshot("55", 2L, new HashMap<String, SnapshotFile>(), false);
        final Snapshot spy = spy(snapshot);

        when(snapshotService.find(any(String.class),
                                  any(String.class),
                                  any(String.class),
                                  any(String.class),
                                  any(String.class)))
                                 .thenReturn(spy);

        doReturn(null).when(spy).getFiles();

        fileService.findAll("mooc", "admin", "java-course", "ex", "2");
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowErrorOnEmptyContentForSnapshotFile() throws IOException {

        final Snapshot snapshot = new Snapshot("55", 2L, new HashMap<String, SnapshotFile>(), false);
        snapshot.addFile(new SnapshotFile("1", "path", null));

        when(snapshotService.find(any(String.class),
                                  any(String.class),
                                  any(String.class),
                                  any(String.class),
                                  any(String.class)))
                                 .thenReturn(snapshot);

        fileService.findContent("mooc", "admin", "java-course", "ex", "15", "1");
    }
}
