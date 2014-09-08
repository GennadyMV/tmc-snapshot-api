package fi.helsinki.cs.tmc.snapshot.api.util;

import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotEvent;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public final class KeyLevelEventProcessorTest {

    private static final String FILENAME = "src/Nimi.java";
    private static final String PATCH = "eyJmaWxlIjoiL3NyYy9OaW1pLmphdmEiLCJwYXRjaGVzIjoiQEAgLTAsMCArMSwyNTEgQEBcbitwdWJsaWMgY2xhc3MgTmltaSAlN0IlMEEgICAgJTBBICAgIHB1YmxpYyBzdGF0aWMgdm9pZCBtYWluKFN0cmluZyU1QiU1RCBhcmdzKSAlN0IlMEEgICAgICAgIC8vIEtpcmpvaXRhIG9oamVsbWFzaSB0JUMzJUE0aCVDMyVBNG4gYWxsZSUwQSAgICAgICUwQSAgICAgICAgLy8gTWlrJUMzJUE0bGkgZXQgdmllbCVDMyVBNCBvbGUgdmFzdGFubnV0IHZpZWwlQzMlQTQga3lzZWx5eW4sIHRlZSBzZSBIRVRJJTBBICAgICAgICAvLyBvc29pdHRlZXNzYTogaHR0cDovL2xhYXR1LmphbW8uZmkvICUwQSAgICAgICAgJTBBICAgICU3RCUwQSUwQSU3RFxuIiwiZnVsbF9kb2N1bWVudCI6dHJ1ZX0\\u003d";
    private static final String PATCHFILECONTENT = "public class Nimi {\n    \n    public static void main(String[] args) {\n        // Kirjoita ohjelmasi tähän alle\n      \n        // Mikäli et vielä ole vastannut vielä kyselyyn, tee se HETI\n        // osoitteessa: http://laatu.jamo.fi/ \n        \n    }\n\n}";
    private static final String ZIP = "UEsDBBQACAgIAFlsLkQAAAAAAAAAAAAAAAAZAAAAdmlpa2tvMS1WaWlra28xXzAwMS5OaW1pLwMAUEsHCAAAAAACAAAAAAAAAFBLAwQUAAgICABZbC5EAAAAAAAAAAAAAAAAHQAAAHZpaWtrbzEtVmlpa2tvMV8wMDEuTmltaS9zcmMvAwBQSwcIAAAAAAIAAAAAAAAAUEsDBBQACAgIAFlsLkQAAAAAAAAAAAAAAAAmAAAAdmlpa2tvMS1WaWlra28xXzAwMS5OaW1pL3NyYy9OaW1pLmphdmFNj0FOw0AMRfc5xVdXrYQy+7JGAiHYlB1iYYppnHpmotiJFKHeJjfJxRhKBf0LW/L395O74V1lj72SGZ4lCr4qFJ1L92uak5c2ZvlAJEnrnfeSDq9voP5gm0viRyHgUfo2ixNy07JGMoEvc7PMCaTKl9XrxJMcl1kF7BiFdZmRlTFSwaY0/A2Pk7FOU7qBM8MY93cvD9d3shVu8cxoi8a924agRD7ULcVcf0r4x+4mc451Hrzuyi+uab1abW7P/qmqTt9QSwcI4k6l3sMAAAAXAQAAUEsBAhQAFAAICAgAWWwuRAAAAAACAAAAAAAAABkAAAAAAAAAAAAAAAAAAAAAAHZpaWtrbzEtVmlpa2tvMV8wMDEuTmltaS9QSwECFAAUAAgICABZbC5EAAAAAAIAAAAAAAAAHQAAAAAAAAAAAAAAAABJAAAAdmlpa2tvMS1WaWlra28xXzAwMS5OaW1pL3NyYy9QSwECFAAUAAgICABZbC5E4k6l3sMAAAAXAQAAJgAAAAAAAAAAAAAAAACWAAAAdmlpa2tvMS1WaWlra28xXzAwMS5OaW1pL3NyYy9OaW1pLmphdmFQSwUGAAAAAAMAAwDmAAAArQEAAAAA";
    private static final String EMPTYZIP = "UEsFBgAAAAAAAAAAAAAAAAAAAAAAAA==";

    private KeyLevelEventProcessor processor;
    private List<SnapshotEvent> events;

    private void generatePatchForExampleExercise(final String data) {

        generatePatch("hy", "viikko1-Viikko1_001.Nimi", data);
    }

    private void generateCodeSnapshotForExampleExercise(final String metadata, final String data) {

        generateCodeSnapshot("hy", "viikko1-Viikko1_001.Nimi", metadata, data);
    }

    private void generatePatch(final String courseName, final String exerciseName, final String data) {

        generateSnapshotEvent(courseName, exerciseName, "patch", null, data);
    }

    private void generateCodeSnapshot(final String courseName, final String exerciseName,
                                      final String metadata, final String data) {

        generateSnapshotEvent(courseName, exerciseName, "code_snapshot", metadata, data);
    }

    private void generateSnapshotEvent(final String courseName, final String exerciseName,
                                       final String eventType, final String metadata, final String data) {

        final SnapshotEvent snapshotEvent = new SnapshotEvent();

        snapshotEvent.setCourseName(courseName);
        snapshotEvent.setExerciseName(exerciseName);
        snapshotEvent.setEventType(eventType);
        snapshotEvent.setMetadata(metadata);
        snapshotEvent.setData(data);

        events.add(snapshotEvent);
    }

    private String generateFileChangeMetadata(final String file) {

        return generateMetadata("file_change", file);
    }

    private String generateFileRemoveMetadata(final String file) {

        return generateMetadata("file_delete", file);
    }

    private String generateMetadata(final String cause, final String file) {

        return "{\"cause\":\"" + cause + "\",\"file\":\"" + file + "\"}";
    }

    private void verifyEventFileContentForExampleFile(final int index, final String content) {

        verifyEventFileContent(index, FILENAME, content);
    }

    private void verifyEventFileContent(final int index, final String filename, final String content) {

        assertEquals(content, events.get(index).getFiles().get(filename));
    }

    private void verifyEventFilesCount(final int index, final int fileCount) {

        assertEquals(fileCount, events.get(index).getFiles().size());
    }

    private void verifyEventCount(final int count) {

        assertEquals(count, events.size());
    }

    private void process() throws UnsupportedEncodingException {

        processor.process(events);
    }

    @Before
    public void setUp() {

        processor = new KeyLevelEventProcessor();
        events = new ArrayList<>();
    }

    @Test
    public void testPatchFullDocumentShouldReturnFileContent() throws UnsupportedEncodingException {

        generatePatchForExampleExercise(PATCH);

        process();

        verifyEventFilesCount(0, 1);
        verifyEventFileContent(0, FILENAME, PATCHFILECONTENT);
    }

    @Test
    public void testPatchingFullDocumentShouldReturnPatchedFileContent() throws UnsupportedEncodingException {

        generatePatchForExampleExercise(PATCH);
        generatePatchForExampleExercise("eyJmaWxlIjoiL3NyYy9OaW1pLmphdmEiLCJwYXRjaGVzIjoiQEAgLTIzMSwyMSArMjMxLDIyIEBAXG4gaS8gJTBBICAgICAgICBcbitzXG4gJTBBICAgICU3RCUwQSUwQSU3RFxuIiwiZnVsbF9kb2N1bWVudCI6ZmFsc2V9");

        process();

        verifyEventFilesCount(0, 1);
        verifyEventFileContent(0, FILENAME, PATCHFILECONTENT);
        verifyEventFilesCount(1, 1);
        verifyEventFileContent(1, FILENAME, "public class Nimi {\n    \n    public static void main(String[] args) {\n        // Kirjoita ohjelmasi tähän alle\n      \n        // Mikäli et vielä ole vastannut vielä kyselyyn, tee se HETI\n        // osoitteessa: http://laatu.jamo.fi/ \n        s\n    }\n\n}");
    }

    @Test
    public void testPatchingFullDocumentWithMultiplePatchesShouldReturnFileContent() throws UnsupportedEncodingException {

        generatePatchForExampleExercise(PATCH);
        generatePatchForExampleExercise("eyJmaWxlIjoiL3NyYy9OaW1pLmphdmEiLCJwYXRjaGVzIjoiQEAgLTIzMSwyMSArMjMxLDIyIEBAXG4gaS8gJTBBICAgICAgICBcbitzXG4gJTBBICAgICU3RCUwQSUwQSU3RFxuIiwiZnVsbF9kb2N1bWVudCI6ZmFsc2V9");
        generatePatchForExampleExercise("eyJmaWxlIjoiL3NyYy9OaW1pLmphdmEiLCJwYXRjaGVzIjoiQEAgLTIzNiwxNiArMjM2LDE3IEBAXG4gICAgICAgIHNcbitvXG4gJTBBICAgICU3RCUwQSUwQVxuIiwiZnVsbF9kb2N1bWVudCI6ZmFsc2V9");
        generatePatchForExampleExercise("eyJmaWxlIjoiL3NyYy9OaW1pLmphdmEiLCJwYXRjaGVzIjoiQEAgLTIzNywxNiArMjM3LDE3IEBAXG4gICAgICAgc29cbit1XG4gJTBBICAgICU3RCUwQSUwQVxuIiwiZnVsbF9kb2N1bWVudCI6ZmFsc2V9");
        generatePatchForExampleExercise("eyJmaWxlIjoiL3NyYy9OaW1pLmphdmEiLCJwYXRjaGVzIjoiQEAgLTIzOCwxNiArMjM4LDE3IEBAXG4gICAgICBzb3Vcbit0XG4gJTBBICAgICU3RCUwQSUwQVxuIiwiZnVsbF9kb2N1bWVudCI6ZmFsc2V9");
        generatePatchForExampleExercise("eyJmaWxlIjoiL3NyYy9OaW1pLmphdmEiLCJwYXRjaGVzIjoiQEAgLTIzOSwxMiArMjM5LDggQEBcbiAgICAgXG4tc291dFxuICUwQSAgIFxuIiwiZnVsbF9kb2N1bWVudCI6ZmFsc2V9");
        generatePatchForExampleExercise("eyJmaWxlIjoiL3NyYy9OaW1pLmphdmEiLCJwYXRjaGVzIjoiQEAgLTIzMSwyMSArMjMxLDQ0IEBAXG4gaS8gJTBBICAgICAgICBcbitTeXN0ZW0ub3V0LnByaW50bG4oJTIyJTIyKTtcbiAlMEEgICAgJTdEJTBBJTBBJTdEXG4iLCJmdWxsX2RvY3VtZW50IjpmYWxzZX0\\u003d");

        process();

        verifyEventFileContentForExampleFile(1, "public class Nimi {\n    \n    public static void main(String[] args) {\n        // Kirjoita ohjelmasi tähän alle\n      \n        // Mikäli et vielä ole vastannut vielä kyselyyn, tee se HETI\n        // osoitteessa: http://laatu.jamo.fi/ \n        s\n    }\n\n}");
        verifyEventFileContentForExampleFile(2, "public class Nimi {\n    \n    public static void main(String[] args) {\n        // Kirjoita ohjelmasi tähän alle\n      \n        // Mikäli et vielä ole vastannut vielä kyselyyn, tee se HETI\n        // osoitteessa: http://laatu.jamo.fi/ \n        so\n    }\n\n}");
        verifyEventFileContentForExampleFile(3, "public class Nimi {\n    \n    public static void main(String[] args) {\n        // Kirjoita ohjelmasi tähän alle\n      \n        // Mikäli et vielä ole vastannut vielä kyselyyn, tee se HETI\n        // osoitteessa: http://laatu.jamo.fi/ \n        sou\n    }\n\n}");
        verifyEventFileContentForExampleFile(4, "public class Nimi {\n    \n    public static void main(String[] args) {\n        // Kirjoita ohjelmasi tähän alle\n      \n        // Mikäli et vielä ole vastannut vielä kyselyyn, tee se HETI\n        // osoitteessa: http://laatu.jamo.fi/ \n        sout\n    }\n\n}");
        verifyEventFileContentForExampleFile(5, "public class Nimi {\n    \n    public static void main(String[] args) {\n        // Kirjoita ohjelmasi tähän alle\n      \n        // Mikäli et vielä ole vastannut vielä kyselyyn, tee se HETI\n        // osoitteessa: http://laatu.jamo.fi/ \n        \n    }\n\n}");
        verifyEventFileContentForExampleFile(6, "public class Nimi {\n    \n    public static void main(String[] args) {\n        // Kirjoita ohjelmasi tähän alle\n      \n        // Mikäli et vielä ole vastannut vielä kyselyyn, tee se HETI\n        // osoitteessa: http://laatu.jamo.fi/ \n        System.out.println(\"\");\n    }\n\n}");
    }

    @Test
    public void testPatchingWithEmptyPatch() throws UnsupportedEncodingException {

        generatePatchForExampleExercise(PATCH);
        generatePatchForExampleExercise("eyJmaWxlIjoiL3NyYy9OaW1pLmphdmEiLCJwYXRjaGVzIjoiIiwiZnVsbF9kb2N1bWVudCI6ZmFsc2V9");

        process();

        assertEquals(1, events.size());

        verifyEventFileContentForExampleFile(0, PATCHFILECONTENT);
    }

    @Test
    public void testPatchingWithDifferentFilenames() throws UnsupportedEncodingException {

        generatePatchForExampleExercise(PATCH);
        generatePatchForExampleExercise("eyJmaWxlIjoiL3NyYy9OaW1pMi5qYXZhIiwicGF0Y2hlcyI6IkBAIC0wLDAgKzEsMjUxIEBAXG4rcHVibGljIGNsYXNzIE5pbWkgJTdCJTBBICAgICUwQSAgICBwdWJsaWMgc3RhdGljIHZvaWQgbWFpbihTdHJpbmclNUIlNUQgYXJncykgJTdCJTBBICAgICAgICAvLyBLaXJqb2l0YSBvaGplbG1hc2kgdCVDMyVBNGglQzMlQTRuIGFsbGUlMEEgICAgICAlMEEgICAgICAgIC8vIE1payVDMyVBNGxpIGV0IHZpZWwlQzMlQTQgb2xlIHZhc3Rhbm51dCB2aWVsJUMzJUE0IGt5c2VseXluLCB0ZWUgc2UgSEVUSSUwQSAgICAgICAgLy8gb3NvaXR0ZWVzc2E6IGh0dHA6Ly9sYWF0dS5qYW1vLmZpLyAlMEEgICAgICAgICUwQSAgICAlN0QlMEElMEElN0RcbiIsImZ1bGxfZG9jdW1lbnQiOnRydWV9");

        process();

        verifyEventFileContentForExampleFile(0, PATCHFILECONTENT);
        verifyEventFileContent(1, "src/Nimi2.java", PATCHFILECONTENT);
    }

    @Test
    public void testPatchingWithNullPatch() throws UnsupportedEncodingException {

        generatePatchForExampleExercise(PATCH);
        generatePatchForExampleExercise("eyJmaWxlIjoiL3NyYy9OaW1pLmphdmEiLCJwYXRjaGVzIjpudWxsLCJmdWxsX2RvY3VtZW50IjpmYWxzZX0=");

        process();

        assertEquals(1, events.size());
        verifyEventFileContentForExampleFile(0, PATCHFILECONTENT);
    }

    @Test
    public void testPatchingWithNonBase64Data() throws UnsupportedEncodingException {

        generatePatchForExampleExercise(PATCH);
        generatePatchForExampleExercise("fail===={");

        process();
        assertEquals(1, events.size());
        verifyEventFileContentForExampleFile(0, PATCHFILECONTENT);
    }

    @Test
    public void testPatchingWithNullJsonData() throws UnsupportedEncodingException {

        generatePatchForExampleExercise(PATCH);
        generatePatchForExampleExercise("bnVsbA==");

        process();
        assertEquals(1, events.size());
        verifyEventFileContentForExampleFile(0, PATCHFILECONTENT);
    }

    @Test
    public void testPatchingWithNonJsonData() throws UnsupportedEncodingException {

        generatePatchForExampleExercise(PATCH);
        generatePatchForExampleExercise("ZmFpbD09PT17Cg==");

        process();

        assertEquals(1, events.size());
        verifyEventFileContentForExampleFile(0, PATCHFILECONTENT);
    }

    @Test
    public void testPatchingAfterFileDelete() throws UnsupportedEncodingException {

        generatePatchForExampleExercise(PATCH);
        generateCodeSnapshotForExampleExercise(generateFileRemoveMetadata(FILENAME), EMPTYZIP);
        generatePatchForExampleExercise(PATCH);

        process();

        verifyEventFileContentForExampleFile(0, PATCHFILECONTENT);
        verifyEventFileContentForExampleFile(2, PATCHFILECONTENT);
    }

    @Test
    public void testPatchingAfterFileDeleteWithoutMetadata() throws UnsupportedEncodingException {

        generatePatchForExampleExercise(PATCH);
        generateCodeSnapshotForExampleExercise(null, EMPTYZIP);
        generatePatchForExampleExercise(PATCH);

        process();

        assertEquals(2, events.size());
        verifyEventFileContentForExampleFile(0, PATCHFILECONTENT);
        verifyEventFileContentForExampleFile(1, PATCHFILECONTENT);
    }

    @Test
    public void testPatchingAfterFileDeleteWithNullMetadata() throws UnsupportedEncodingException {

        generatePatchForExampleExercise(PATCH);
        generateCodeSnapshotForExampleExercise("null", EMPTYZIP);
        generatePatchForExampleExercise(PATCH);

        process();
        assertEquals(2, events.size());
        verifyEventFileContentForExampleFile(0, PATCHFILECONTENT);
        verifyEventFileContentForExampleFile(1, PATCHFILECONTENT);
    }

    @Test
    public void testPatchingAfterDeletingDifferentFile() throws UnsupportedEncodingException {

        generatePatchForExampleExercise(PATCH);
        generateCodeSnapshotForExampleExercise(generateFileRemoveMetadata("/src/Nimi2.java"), EMPTYZIP);
        generatePatchForExampleExercise("eyJmaWxlIjoiL3NyYy9OaW1pLmphdmEiLCJwYXRjaGVzIjoiQEAgLTIzMSwyMSArMjMxLDIyIEBAXG4gaS8gJTBBICAgICAgICBcbitzXG4gJTBBICAgICU3RCUwQSUwQSU3RFxuIiwiZnVsbF9kb2N1bWVudCI6ZmFsc2V9");

        process();

        verifyEventFileContentForExampleFile(0, PATCHFILECONTENT);
        verifyEventFileContentForExampleFile(2, "public class Nimi {\n    \n    public static void main(String[] args) {\n        // Kirjoita ohjelmasi tähän alle\n      \n        // Mikäli et vielä ole vastannut vielä kyselyyn, tee se HETI\n        // osoitteessa: http://laatu.jamo.fi/ \n        s\n    }\n\n}");
    }

    @Test
    public void testPatchingAfterNonJsonMetadata() throws UnsupportedEncodingException {

        generatePatchForExampleExercise(PATCH);
        generateCodeSnapshotForExampleExercise("fail===={", EMPTYZIP);
        generatePatchForExampleExercise(PATCH);

        process();

        assertEquals(2, events.size());
        verifyEventFileContentForExampleFile(0, PATCHFILECONTENT);
        verifyEventFileContentForExampleFile(1, PATCHFILECONTENT);
    }

    @Test
    public void testPatchingAfterEmptyMetadata() throws UnsupportedEncodingException {

        generatePatchForExampleExercise(PATCH);
        generateCodeSnapshotForExampleExercise("", EMPTYZIP);
        generatePatchForExampleExercise(PATCH);

        process();

        assertEquals(2, events.size());
        verifyEventFileContentForExampleFile(0, PATCHFILECONTENT);
        verifyEventFileContentForExampleFile(1, PATCHFILECONTENT);
    }

    @Test
    public void testCodeSnapshotPatching() throws UnsupportedEncodingException {

        generatePatchForExampleExercise(PATCH);
        generateCodeSnapshotForExampleExercise(generateFileChangeMetadata(FILENAME), ZIP);

        process();

        verifyEventCount(2);

        verifyEventFileContentForExampleFile(0, "public class Nimi {\n    \n    public static void main(String[] args) {\n        // Kirjoita ohjelmasi tähän alle\n      \n        // Mikäli et vielä ole vastannut vielä kyselyyn, tee se HETI\n        // osoitteessa: http://laatu.jamo.fi/ \n        \n    }\n\n}");
        verifyEventFileContentForExampleFile(1, "public class Nimi {\n    \n    public static void main(String[] args) {\n        // Kirjoita ohjelmasi tähän alle\n      \n        // Mikäli et vielä ole vastannut vielä kyselyyn, tee se HETI\n        // osoitteessa: http://laatu.jamo.fi/ \n        System.out.println(\"\");\n    }\n\n}");
    }

    @Test
    public void testCodeSnapshotPatchingForDifferentFileNames() throws UnsupportedEncodingException {

        generatePatchForExampleExercise("eyJmaWxlIjoiL3NyYy9OaW1pMi5qYXZhIiwicGF0Y2hlcyI6IkBAIC0wLDAgKzEsMjUxIEBAXG4rcHVibGljIGNsYXNzIE5pbWkgJTdCJTBBICAgICUwQSAgICBwdWJsaWMgc3RhdGljIHZvaWQgbWFpbihTdHJpbmclNUIlNUQgYXJncykgJTdCJTBBICAgICAgICAvLyBLaXJqb2l0YSBvaGplbG1hc2kgdCVDMyVBNGglQzMlQTRuIGFsbGUlMEEgICAgICAlMEEgICAgICAgIC8vIE1payVDMyVBNGxpIGV0IHZpZWwlQzMlQTQgb2xlIHZhc3Rhbm51dCB2aWVsJUMzJUE0IGt5c2VseXluLCB0ZWUgc2UgSEVUSSUwQSAgICAgICAgLy8gb3NvaXR0ZWVzc2E6IGh0dHA6Ly9sYWF0dS5qYW1vLmZpLyAlMEEgICAgICAgICUwQSAgICAlN0QlMEElMEElN0RcbiIsImZ1bGxfZG9jdW1lbnQiOnRydWV9");
        generateCodeSnapshotForExampleExercise(generateFileChangeMetadata(FILENAME), ZIP);

        process();

        assertEquals(2, events.size());
        verifyEventFileContent(0, "src/Nimi2.java", "public class Nimi {\n    \n    public static void main(String[] args) {\n        // Kirjoita ohjelmasi tähän alle\n      \n        // Mikäli et vielä ole vastannut vielä kyselyyn, tee se HETI\n        // osoitteessa: http://laatu.jamo.fi/ \n        \n    }\n\n}");
    }

    @Test
    public void testCodeSnapshotPatchingWithCorruptedZip() throws UnsupportedEncodingException {

        generatePatchForExampleExercise(PATCH);
        generateCodeSnapshotForExampleExercise(generateFileChangeMetadata(FILENAME), "UEsDBBQACAgIAFlsLkQAAAAAAAAAAAAAAAAZAAAAdmlpa2tvMS1WaWlra28xXzAwMS5OaW1pLwMAUEsHCAAAAAACAAAAAAAAAFBLAwQUAAgICABZbC5EAAAAAAAAAAAAAAAAHQAAAHZpaWtrbzEtVmlpa2tvMV8wMDEuTmltaS9zcmMvAwBQSwcIAAAAAAIAAAAAAAAAUEsDBBQACAgIAFlsLkQAAAAAAAAAAAAAAAAmAAAAdmlpa2tvMS1WaWlra28xXzAwMS5OaW1pL3NyYy9OaW1pLmphdmFNj0FOw0AMRfc5xVdXrYQy+7JGAiHYlB1iYYppnHpmotiJFKHeJjfJxRhKBf0LW/L395O74V1lj72SGZ4lYXNkCg==");

        process();

        assertEquals(1, events.size());
        verifyEventFileContentForExampleFile(0, PATCHFILECONTENT);
    }

    @Test
    public void testCodeSnapshotPatchingWithCorruptedPatch() throws UnsupportedEncodingException {

        generatePatchForExampleExercise(PATCH);
        generatePatchForExampleExercise("eyJmaWxlIjoiL3NyYy9OaW1pLmphdmEiLCJwYXRjaGVzIjoiMTIzIiwiZnVsbF9kb2N1bWVudCI6dHJ1ZX0=");

        process();

        verifyEventCount(1);
        verifyEventFileContentForExampleFile(0, PATCHFILECONTENT);
    }

    @Test
    public void testCodeSnapshotPatchingWithEmptyPatch() throws UnsupportedEncodingException {

        generatePatchForExampleExercise(PATCH);
        generatePatchForExampleExercise("eyJmaWxlIjoiL3NyYy9OaW1pLmphdmEiLCJwYXRjaGVzIjoiQEAgLTAsMCArMCwwIEBAXG5cbiIsImZ1bGxfZG9jdW1lbnQiOnRydWV9");

        process();

        verifyEventCount(1);
        verifyEventFileContentForExampleFile(0, PATCHFILECONTENT);
    }
}
