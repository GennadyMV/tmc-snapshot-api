package fi.helsinki.cs.tmc.snapshot.api.util;

import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotEvent;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public final class CodeLevelEventProcessorTest {

    private static final String FILENAME = "src/Nimi.java";
    private static final String PATCH = "eyJmaWxlIjoiL3NyYy9OaW1pLmphdmEiLCJwYXRjaGVzIjoiQEAgLTAsMCArMSwyNTEgQEBcbitwdWJsaWMgY2xhc3MgTmltaSAlN0IlMEEgICAgJTBBICAgIHB1YmxpYyBzdGF0aWMgdm9pZCBtYWluKFN0cmluZyU1QiU1RCBhcmdzKSAlN0IlMEEgICAgICAgIC8vIEtpcmpvaXRhIG9oamVsbWFzaSB0JUMzJUE0aCVDMyVBNG4gYWxsZSUwQSAgICAgICUwQSAgICAgICAgLy8gTWlrJUMzJUE0bGkgZXQgdmllbCVDMyVBNCBvbGUgdmFzdGFubnV0IHZpZWwlQzMlQTQga3lzZWx5eW4sIHRlZSBzZSBIRVRJJTBBICAgICAgICAvLyBvc29pdHRlZXNzYTogaHR0cDovL2xhYXR1LmphbW8uZmkvICUwQSAgICAgICAgJTBBICAgICU3RCUwQSUwQSU3RFxuIiwiZnVsbF9kb2N1bWVudCI6dHJ1ZX0\\u003d";
    private static final String PATCHFILECONTENT = "public class Nimi {\n    \n    public static void main(String[] args) {\n        // Kirjoita ohjelmasi tähän alle\n      \n        // Mikäli et vielä ole vastannut vielä kyselyyn, tee se HETI\n        // osoitteessa: http://laatu.jamo.fi/ \n        \n    }\n\n}";
    private static final String ZIP = "UEsDBBQACAgIAFlsLkQAAAAAAAAAAAAAAAAZAAAAdmlpa2tvMS1WaWlra28xXzAwMS5OaW1pLwMAUEsHCAAAAAACAAAAAAAAAFBLAwQUAAgICABZbC5EAAAAAAAAAAAAAAAAHQAAAHZpaWtrbzEtVmlpa2tvMV8wMDEuTmltaS9zcmMvAwBQSwcIAAAAAAIAAAAAAAAAUEsDBBQACAgIAFlsLkQAAAAAAAAAAAAAAAAmAAAAdmlpa2tvMS1WaWlra28xXzAwMS5OaW1pL3NyYy9OaW1pLmphdmFNj0FOw0AMRfc5xVdXrYQy+7JGAiHYlB1iYYppnHpmotiJFKHeJjfJxRhKBf0LW/L395O74V1lj72SGZ4lCr4qFJ1L92uak5c2ZvlAJEnrnfeSDq9voP5gm0viRyHgUfo2ixNy07JGMoEvc7PMCaTKl9XrxJMcl1kF7BiFdZmRlTFSwaY0/A2Pk7FOU7qBM8MY93cvD9d3shVu8cxoi8a924agRD7ULcVcf0r4x+4mc451Hrzuyi+uab1abW7P/qmqTt9QSwcI4k6l3sMAAAAXAQAAUEsBAhQAFAAICAgAWWwuRAAAAAACAAAAAAAAABkAAAAAAAAAAAAAAAAAAAAAAHZpaWtrbzEtVmlpa2tvMV8wMDEuTmltaS9QSwECFAAUAAgICABZbC5EAAAAAAIAAAAAAAAAHQAAAAAAAAAAAAAAAABJAAAAdmlpa2tvMS1WaWlra28xXzAwMS5OaW1pL3NyYy9QSwECFAAUAAgICABZbC5E4k6l3sMAAAAXAQAAJgAAAAAAAAAAAAAAAACWAAAAdmlpa2tvMS1WaWlra28xXzAwMS5OaW1pL3NyYy9OaW1pLmphdmFQSwUGAAAAAAMAAwDmAAAArQEAAAAA";
    private static final String ZIPFILECONTENT = "public class Nimi {\n    \n    public static void main(String[] args) {\n        // Kirjoita ohjelmasi tähän alle\n      \n        // Mikäli et vielä ole vastannut vielä kyselyyn, tee se HETI\n        // osoitteessa: http://laatu.jamo.fi/ \n        System.out.println(\"\");\n    }\n\n}";
    private static final String ZIPWITHTWOFILES = "UEsDBAoAAAAAAFlsLkQAAAAAAAAAAAAAAAAZABwAdmlpa2tvMS1WaWlra28xXzAwMS5OaW1pL1VUCQAD2iDVUn2rDVR1eAsAAQRWJBIABOUuAABQSwMECgAAAAAAw4EoRQAAAAAAAAAAAAAAAB0AHAB2aWlra28xLVZpaWtrbzFfMDAxLk5pbWkvc3JjL1VUCQADnasNVJ6rDVR1eAsAAQRWJBIABOUuAABQSwMEFAAAAAgAw4EoRcZn6ZbGAAAAHAEAACYAHAB2aWlra28xLVZpaWtrbzFfMDAxLk5pbWkvc3JjL05pbWkuamF2YVVUCQADnasNVJ2rDVR1eAsAAQRWJBIABOUuAABNT0tOw0AM3ecUT121EsrsyxYkECqbskMsTDGJU2cmip1IEeptcpNcjKFU0LewJb+f3A3vKgcclMzwLK3gq0DGeXS/pDl5XmOSD7Qkcb33XmL1+gbqK9tcHD8IAU/SN0mckOqGtSUT+DLXyxxBqnyRXjt2clxmFbBjFNZlRlLGSLk2xuHveJyMdZriDZwZxni4f3m8zkmWezNnRlvU7t02BCXyoWyoTeWnhP/a/WTObZkGL7v8i2tcr+5SxavN7VlzKopT8Q1QSwMEFAAAAAgAtoEoReJOpd7DAAAAFwEAACcAHAB2aWlra28xLVZpaWtrbzFfMDAxLk5pbWkvc3JjL05pbWkyLmphdmFVVAkAA4erDVSHqw1UdXgLAAEEViQSAATlLgAATY9BTsNADEX3OcVXV62EMvuyRgIh2JQdYmGKaZx6ZqLYiRSh3iY3ycUYSgX9C1vy9/eTu+FdZY+9khmeJQq+KhSdS/drmpOXNmb5QCRJ6533kg6vb6D+YJtL4kch4FH6NosTctOyRjKBL3OzzAmkypfV68STHJdZBewYhXWZkZUxUsGmNPwNj5OxTlO6gTPDGPd3Lw/Xd7IVbvHMaIvGvduGoEQ+1C3FXH9K+MfuJnOOdR687sovrmm9Wm1uz/6pqk7fUEsBAh4DCgAAAAAAWWwuRAAAAAAAAAAAAAAAABkAGAAAAAAAAAAQAMBBAAAAAHZpaWtrbzEtVmlpa2tvMV8wMDEuTmltaS9VVAUAA9og1VJ1eAsAAQRWJBIABOUuAABQSwECHgMKAAAAAADDgShFAAAAAAAAAAAAAAAAHQAYAAAAAAAAABAAwEFTAAAAdmlpa2tvMS1WaWlra28xXzAwMS5OaW1pL3NyYy9VVAUAA52rDVR1eAsAAQRWJBIABOUuAABQSwECHgMUAAAACADDgShFxmfplsYAAAAcAQAAJgAYAAAAAAABAAAAgIGqAAAAdmlpa2tvMS1WaWlra28xXzAwMS5OaW1pL3NyYy9OaW1pLmphdmFVVAUAA52rDVR1eAsAAQRWJBIABOUuAABQSwECHgMUAAAACAC2gShF4k6l3sMAAAAXAQAAJwAYAAAAAAABAAAAgIHQAQAAdmlpa2tvMS1WaWlra28xXzAwMS5OaW1pL3NyYy9OaW1pMi5qYXZhVVQFAAOHqw1UdXgLAAEEViQSAATlLgAAUEsFBgAAAAAEAAQAmwEAAPQCAAAAAA==";
    private static final String EMPTYZIP = "UEsFBgAAAAAAAAAAAAAAAAAAAAAAAA==";

    private CodeLevelEventProcessor processor;
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

        processor = new CodeLevelEventProcessor();
        events = new ArrayList<>();
    }

    @Test
    public void testCodeSnapshotShouldReturnFileContent() throws UnsupportedEncodingException {

        generateCodeSnapshotForExampleExercise(FILENAME, ZIP);

        process();

        verifyEventFilesCount(0, 1);
        verifyEventFileContent(0, FILENAME, ZIPFILECONTENT);
    }

    @Test
    public void testDuplicateCodeSnapshotsShouldBeRemoved() throws UnsupportedEncodingException {

        generateCodeSnapshotForExampleExercise(FILENAME, ZIP);
        generateCodeSnapshotForExampleExercise(FILENAME, ZIP);

        process();

        verifyEventCount(1);
        verifyEventFilesCount(0, 1);
        verifyEventFileContent(0, FILENAME, ZIPFILECONTENT);
    }

    @Test
    public void testPatchDataShouldBeSkipped() throws UnsupportedEncodingException {

        generatePatchForExampleExercise(PATCH);

        process();

        verifyEventCount(0);
    }

    @Test
    public void testMultipleCodeSnapshotsShouldContainsCorrectInformation() throws UnsupportedEncodingException {

        generateCodeSnapshotForExampleExercise(FILENAME, ZIP);
        generateCodeSnapshotForExampleExercise(FILENAME, ZIPWITHTWOFILES);

        process();

        verifyEventCount(2);
        verifyEventFilesCount(0, 1);
        verifyEventFileContent(0, FILENAME, ZIPFILECONTENT);
        verifyEventFileContent(1, "src/Nimi2.java", ZIPFILECONTENT);
    }

    @Test
    public void testProcessingCorruptedZipShouldFail() throws UnsupportedEncodingException {

        generateCodeSnapshotForExampleExercise(generateFileChangeMetadata(FILENAME), "UEsDBBQACAgIAFlsLkQAAAAAAAAAAAAAAAAZAAAAdmlpa2tvMS1WaWlra28xXzAwMS5OaW1pLwMAUEsHCAAAAAACAAAAAAAAAFBLAwQUAAgICABZbC5EAAAAAAAAAAAAAAAAHQAAAHZpaWtrbzEtVmlpa2tvMV8wMDEuTmltaS9zcmMvAwBQSwcIAAAAAAIAAAAAAAAAUEsDBBQACAgIAFlsLkQAAAAAAAAAAAAAAAAmAAAAdmlpa2tvMS1WaWlra28xXzAwMS5OaW1pL3NyYy9OaW1pLmphdmFNj0FOw0AMRfc5xVdXrYQy+7JGAiHYlB1iYYppnHpmotiJFKHeJjfJxRhKBf0LW/L395O74V1lj72SGZ4lYXNkCg==");

        process();

        verifyEventCount(0);
    }
}
