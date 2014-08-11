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
        files.put("test.java", new SnapshotFile("test.java", "public class Test { }"));

        final Snapshot snapshot = new Snapshot(2L, files);

        when(snapshotService.find(any(String.class),
                                  any(String.class),
                                  any(String.class),
                                  any(String.class),
                                  any(Long.class)))
                                 .thenReturn(snapshot);

        final String content = fileService.find("hy", "user", "course", "exercise", 2L, "test.java");

        assertEquals("public class Test { }", content);
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowExceptionOnNonExistentPath() throws IOException {

        final Snapshot snapshot = new Snapshot(2L, new HashMap<String, SnapshotFile>());

        when(snapshotService.find(any(String.class),
                                  any(String.class),
                                  any(String.class),
                                  any(String.class),
                                  any(Long.class)))
                                 .thenReturn(snapshot);

        fileService.find("hy", "user", "course", "exercise", 2L, "404.java");
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowExceptionOnNonExistentContent() throws IOException {

        final Map<String, SnapshotFile> files = new HashMap<>();
        files.put("trial.java", new SnapshotFile("trial.java", null));

        final Snapshot snapshot = new Snapshot(2L, files);

        when(snapshotService.find(any(String.class),
                                  any(String.class),
                                  any(String.class),
                                  any(String.class),
                                  any(Long.class)))
                                 .thenReturn(snapshot);

        fileService.find("hy", "user", "course", "exercise", 2L, "trial.java");
    }

    @Test
    public void shouldFindAllSnapshotFiles() throws IOException {

        final Map<String, SnapshotFile> files = new HashMap<>();
        files.put("example.java", new SnapshotFile("example.java", "public class Example { }"));
        files.put("exercise.java", new SnapshotFile("exercise.java", "public class Exercise { }"));

        final Snapshot snapshot = new Snapshot(2L, files);

        when(snapshotService.find(any(String.class),
                                  any(String.class),
                                  any(String.class),
                                  any(String.class),
                                  any(Long.class)))
                                 .thenReturn(snapshot);

        final Collection<SnapshotFile> filesCollection = fileService.findAll("mooc", "admin", "java-course", "ex", 2L);
        final List<SnapshotFile> snapshotFiles = new ArrayList(filesCollection);

        assertEquals(2, snapshotFiles.size());

        assertEquals("exercise.java", snapshotFiles.get(0).getPath());
        assertEquals("public class Exercise { }", snapshotFiles.get(0).getContent());

        assertEquals("example.java", snapshotFiles.get(1).getPath());
        assertEquals("public class Example { }", snapshotFiles.get(1).getContent());
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowExceptionOnNonExistentSnapshotFiles() throws IOException {

        final Snapshot snapshot = new Snapshot(2L, new HashMap<String, SnapshotFile>());
        final Snapshot spy = spy(snapshot);

        when(snapshotService.find(any(String.class),
                                  any(String.class),
                                  any(String.class),
                                  any(String.class),
                                  any(Long.class)))
                                 .thenReturn(spy);

        doReturn(null).when(spy).getFiles();

        fileService.findAll("mooc", "admin", "java-course", "ex", 2L);
    }
}
