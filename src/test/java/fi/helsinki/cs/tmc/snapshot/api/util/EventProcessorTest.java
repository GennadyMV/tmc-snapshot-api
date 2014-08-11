package fi.helsinki.cs.tmc.snapshot.api.util;

import fi.helsinki.cs.tmc.snapshot.api.model.SnapshotEvent;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EventProcessorTest {

    private static final String COURSENAME = "mooc";
    private static final String EXERCISENAME = "exercise1";
    private static final String BASICTYPE = "text_insert";

    private static final String FILENAME = "/src/Nimi.java";

    private EventProcessor p;
    private List<SnapshotEvent> events;

    @Before
    public void setUp() {

        p = new EventProcessor();
        events = new ArrayList<>();
    }

    @Test
    public void testProcess() throws Exception {

        generateSnapshotEvent(COURSENAME, EXERCISENAME, BASICTYPE, 1L, 1L, null,
                "eyJmaWxlIjoiL3NyYy9OaW1pLmphdmEiLCJwYXRjaGVzIjoiQEAgLTAsMCArMSwyNTEgQEBcbitwdWJsaWMgY2xhc3MgTmltaSAlN0IlMEEgICAgJTBBICAgIHB1YmxpYyBzdGF0aWMgdm9pZCBtYWluKFN0cmluZyU1QiU1RCBhcmdzKSAlN0IlMEEgICAgICAgIC8vIEtpcmpvaXRhIG9oamVsbWFzaSB0JUMzJUE0aCVDMyVBNG4gYWxsZSUwQSAgICAgICUwQSAgICAgICAgLy8gTWlrJUMzJUE0bGkgZXQgdmllbCVDMyVBNCBvbGUgdmFzdGFubnV0IHZpZWwlQzMlQTQga3lzZWx5eW4sIHRlZSBzZSBIRVRJJTBBICAgICAgICAvLyBvc29pdHRlZXNzYTogaHR0cDovL2xhYXR1LmphbW8uZmkvICUwQSAgICAgICAgJTBBICAgICU3RCUwQSUwQSU3RFxuIiwiZnVsbF9kb2N1bWVudCI6dHJ1ZX0\\u003d");
        generateSnapshotEvent(COURSENAME, EXERCISENAME, BASICTYPE, 1L, 1L, null,
                "eyJmaWxlIjoiL3NyYy9OaW1pLmphdmEiLCJwYXRjaGVzIjoiQEAgLTIzMSwyMSArMjMxLDIyIEBAXG4gaS8gJTBBICAgICAgICBcbitzXG4gJTBBICAgICU3RCUwQSUwQSU3RFxuIiwiZnVsbF9kb2N1bWVudCI6ZmFsc2V9");
        generateSnapshotEvent(COURSENAME, EXERCISENAME, BASICTYPE, 1L, 1L, null,
                "eyJmaWxlIjoiL3NyYy9OaW1pLmphdmEiLCJwYXRjaGVzIjoiQEAgLTIzNiwxNiArMjM2LDE3IEBAXG4gICAgICAgIHNcbitvXG4gJTBBICAgICU3RCUwQSUwQVxuIiwiZnVsbF9kb2N1bWVudCI6ZmFsc2V9");
        generateSnapshotEvent(COURSENAME, EXERCISENAME, BASICTYPE, 1L, 1L, null,
                "eyJmaWxlIjoiL3NyYy9OaW1pLmphdmEiLCJwYXRjaGVzIjoiQEAgLTIzNywxNiArMjM3LDE3IEBAXG4gICAgICAgc29cbit1XG4gJTBBICAgICU3RCUwQSUwQVxuIiwiZnVsbF9kb2N1bWVudCI6ZmFsc2V9");
        generateSnapshotEvent(COURSENAME, EXERCISENAME, BASICTYPE, 1L, 1L, null,
                "eyJmaWxlIjoiL3NyYy9OaW1pLmphdmEiLCJwYXRjaGVzIjoiQEAgLTIzOCwxNiArMjM4LDE3IEBAXG4gICAgICBzb3Vcbit0XG4gJTBBICAgICU3RCUwQSUwQVxuIiwiZnVsbF9kb2N1bWVudCI6ZmFsc2V9");
        generateSnapshotEvent(COURSENAME, EXERCISENAME, "text_remove", 1L, 1L, null,
                "eyJmaWxlIjoiL3NyYy9OaW1pLmphdmEiLCJwYXRjaGVzIjoiQEAgLTIzOSwxMiArMjM5LDggQEBcbiAgICAgXG4tc291dFxuICUwQSAgIFxuIiwiZnVsbF9kb2N1bWVudCI6ZmFsc2V9");
        generateSnapshotEvent(COURSENAME, EXERCISENAME, BASICTYPE, 1L, 1L, null,
                "eyJmaWxlIjoiL3NyYy9OaW1pLmphdmEiLCJwYXRjaGVzIjoiQEAgLTIzMSwyMSArMjMxLDQ0IEBAXG4gaS8gJTBBICAgICAgICBcbitTeXN0ZW0ub3V0LnByaW50bG4oJTIyJTIyKTtcbiAlMEEgICAgJTdEJTBBJTBBJTdEXG4iLCJmdWxsX2RvY3VtZW50IjpmYWxzZX0\\u003d");
        p.process(events);

        for (SnapshotEvent snapshotEvent : events) {
            assertEquals(1, snapshotEvent.getFiles().size());
        }

        assertEquals("public class Nimi {\n    \n    public static void main(String[] args) {\n        // Kirjoita ohjelmasi tähän alle\n      \n        // Mikäli et vielä ole vastannut vielä kyselyyn, tee se HETI\n        // osoitteessa: http://laatu.jamo.fi/ \n        \n    }\n\n}", events.get(0).getFiles().get(FILENAME));

        assertEquals("public class Nimi {\n    \n    public static void main(String[] args) {\n        // Kirjoita ohjelmasi tähän alle\n      \n        // Mikäli et vielä ole vastannut vielä kyselyyn, tee se HETI\n        // osoitteessa: http://laatu.jamo.fi/ \n        sout\n    }\n\n}", events.get(4).getFiles().get(FILENAME));

        assertEquals("public class Nimi {\n    \n    public static void main(String[] args) {\n        // Kirjoita ohjelmasi tähän alle\n      \n        // Mikäli et vielä ole vastannut vielä kyselyyn, tee se HETI\n        // osoitteessa: http://laatu.jamo.fi/ \n        System.out.println(\"\");\n    }\n\n}", events.get(6).getFiles().get(FILENAME));

    }

    @Test
    public void testErrorDataProcess() throws Exception {

        generateSnapshotEvent(COURSENAME, EXERCISENAME, BASICTYPE, 1L, 1L, null,
                "eyJmaWxlIjoiL3NyYy9OaW1pLmphdmEiLCJwYXRjaGVzIjoiQEAgLTAsMCArMSwyNTEgQEBcbitwdWJsaWMgY2xhc3MgTmltaSAlN0IlMEEgICAgJTBBICAgIHB1YmxpYyBzdGF0aWMgdm9pZCBtYWluKFN0cmluZyU1QiU1RCBhcmdzKSAlN0IlMEEgICAgICAgIC8vIEtpcmpvaXRhIG9oamVsbWFzaSB0JUMzJUE0aCVDMyVBNG4gYWxsZSUwQSAgICAgICUwQSAgICAgICAgLy8gTWlrJUMzJUE0bGkgZXQgdmllbCVDMyVBNCBvbGUgdmFzdGFubnV0IHZpZWwlQzMlQTQga3lzZWx5eW4sIHRlZSBzZSBIRVRJJTBBICAgICAgICAvLyBvc29pdHRlZXNzYTogaHR0cDovL2xhYXR1LmphbW8uZmkvICUwQSAgICAgICAgJTBBICAgICU3RCUwQSUwQSU3RFxuIiwiZnVsbF9kb2N1bWVudCI6dHJ1ZX0\\u003d");
        generateSnapshotEvent(COURSENAME, EXERCISENAME, BASICTYPE, 1L, 1L, null,
                "eyJmaWxlIjoiL3NyYy9OaW1pMi5qYXZhIiwicGF0Y2hlcyI6IkBAIC0wLDAgKzEsMjUxIEBAXG4rcHVibGljIGNsYXNzIE5pbWkgJTdCJTBBICAgICUwQSAgICBwdWJsaWMgc3RhdGljIHZvaWQgbWFpbihTdHJpbmclNUIlNUQgYXJncykgJTdCJTBBICAgICAgICAvLyBLaXJqb2l0YSBvaGplbG1hc2kgdCVDMyVBNGglQzMlQTRuIGFsbGUlMEEgICAgICAlMEEgICAgICAgIC8vIE1payVDMyVBNGxpIGV0IHZpZWwlQzMlQTQgb2xlIHZhc3Rhbm51dCB2aWVsJUMzJUE0IGt5c2VseXluLCB0ZWUgc2UgSEVUSSUwQSAgICAgICAgLy8gb3NvaXR0ZWVzc2E6IGh0dHA6Ly9sYWF0dS5qYW1vLmZpLyAlMEEgICAgICAgICUwQSAgICAlN0QlMEElMEElN0RcbiIsImZ1bGxfZG9jdW1lbnQiOnRydWV9");
        generateSnapshotEvent(COURSENAME, EXERCISENAME, BASICTYPE, 1L, 1L, null,
                "eyJmaWxlIjoiL3NyYy9OaW1pLmphdmEiLCJwYXRjaGVzIjoiIiwiZnVsbF9kb2N1bWVudCI6ZmFsc2V9");
        generateSnapshotEvent(COURSENAME, EXERCISENAME, BASICTYPE, 1L, 1L, null,
                "eyJmaWxlIjoiL3NyYy9OaW1pLmphdmEiLCJwYXRjaGVzIjpudWxsLCJmdWxsX2RvY3VtZW50IjpmYWxzZX0=");
        generateSnapshotEvent(COURSENAME, EXERCISENAME, BASICTYPE, 1L, 1L, "\"cause\":\"file_change\",\"file\":\"/src/Nimi.java\"", "UEsDBBQACAgIAFlsLkQAAAAAAAAAAAAAAAAZAAAAdmlpa2tvMS1WaWlra28xXzAwMS5OaW1p\n" +
                "LwMAUEsHCAAAAAACAAAAAAAAAFBLAwQUAAgICABZbC5EAAAAAAAAAAAAAAAAHQAAAHZpaWtrbzEtVmlpa2tvMV8wMDEuTmltaS9zcmMvAwBQSwcIAAAAAAIAAAAAAAAAUEsDBBQACAgIAFlsLkQAAAAAAAAAAAAA\n" +
                "AAAmAAAAdmlpa2tvMS1WaWlra28xXzAwMS5OaW1pL3NyYy9OaW1pLmphdmFNj0FOw0AMRfc5xVdXrYQy" +
                "+7JGAiHYlB1iYYppnHpmotiJFKHeJjfJxRhKBf0LW/L395O74V1lj72SGZ4lCr4qFJ1L92uak5c2ZvlA" +
                "JEnrnfeSDq9voP5gm0viRyHgUfo2ixNy07JGMoEvc7PMCaTKl9XrxJMcl1kF7BiFdZmRlTFSwaY0/A2P" +
                "k7FOU7qBM8MY93cvD9d3shVu8cxoi8a924agRD7ULcVcf0r4x+4mc451Hrzuyi+uab1abW7P/qmqTt9Q" +
                "SwcI4k6l3sMAAAAXAQAAUEsBAhQAFAAICAgAWWwuRAAAAAACAAAAAAAAABkAAAAAAAAAAAAAAAAAAAAA" +
                "AHZpaWtrbzEtVmlpa2tvMV8wMDEuTmltaS9QSwECFAAUAAgICABZbC5EAAAAAAIAAAAAAAAAHQAAAAAA" +
                "AAAAAAAAAABJAAAAdmlpa2tvMS1WaWlra28xXzAwMS5OaW1pL3NyYy9QSwECFAAUAAgICABZbC5E4k6l" +
                "3sMAAAAXAQAAJgAAAAAAAAAAAAAAAACWAAAAdmlpa2tvMS1WaWlra28xXzAwMS5OaW1pL3NyYy9OaW1p" +
                "LmphdmFQSwUGAAAAAAMAAwDmAAAArQEAAAAA");
        generateSnapshotEvent(COURSENAME, EXERCISENAME, "code_snapshot", 1L, 1L, "\"cause\":\"file_change\",\"file\":\"/src/Nimi.java\"", "UEsDBBQACAgIAFlsLkQAAAAAAAAAAAAAAAAZAAAAdmlpa2tvMS1WaWlra28xXzAwMS5OaW1p\n" +
                "LwMAUEsHCAAAAAACAAAAAAAAAFBLAwQUAAgICABZbC5EAAAAAAAAAAAAAAAAHQAAAHZpaWtrbzEtVmlpa2tvMV8wMDEuTmltaS9zcmMvAwBQSwcIAAAAAAIAAAAAAAAAUEsDBBQACAgIAFlsLkQAAAAAAAAAAAAA\n" +
                "AAAmAAAAdmlpa2tvMS1WaWlra28xXzAwMS5OaW1pL3NyYy9OaW1pLmphdmFNj0FOw0AMRfc5xVdXrYQy" +
                "+7JGAiHYlB1iYYppnHpmotiJFKHeJjfJxRhKBf0LW/L395O74V1lj72SGZ4lCr4qFJ1L92uak5c2ZvlA" +
                "JEnrnfeSDq9voP5gm0viRyHgUfo2ixNy07JGMoEvc7PMCaTKl9XrxJMcl1kF7BiFdZmRlTFSwaY0/A2P" +
                "k7FOU7qBM8MY93cvD9d3shVu8cxoi8a924agRD7ULcVcf0r4x+4mc451Hrzuyi+uab1abW7P/qmqTt9Q" +
                "SwcI4k6l3sMAAAAXAQAAUEsBAhQAFAAICAgAWWwuRAAAAAACAAAAAAAAABkAAAAAAAAAAAAAAAAAAAAA" +
                "AHZpaWtrbzEtVmlpa2tvMV8wMDEuTmltaS9QSwECFAAUAAgICABZbC5EAAAAAAIAAAAAAAAAHQAAAAAA" +
                "AAAAAAAAAABJAAAAdmlpa2tvMS1WaWlra28xXzAwMS5OaW1pL3NyYy9QSwECFAAUAAgICABZbC5E4k6l" +
                "3sMAAAAXAQAAJgAAAAAAAAAAAAAAAACWAAAAdmlpa2tvMS1WaWlra28xXzAwMS5OaW1pL3NyYy9OaW1p" +
                "LmphdmFQSwUGAAAAAAMAAwDmAAAArQEAAAAA");
        generateSnapshotEvent(COURSENAME, EXERCISENAME, BASICTYPE, 1L, 1L, null, "bnVsbA==");
        generateSnapshotEvent(COURSENAME, EXERCISENAME, "code_snapshot", 1L, 1L, "{\"cause\":\"folder_create\",\"file\":\"/nbproject/private\"}", "UEsDBBQACAgIADxsLkQAAAAAAAAAAAAAAAAaAAAAdmlpa2tvMS1WaWlra28xXzAwMy5LdXVzaS8DAFBLBwgAAAAAAgAAAAAAAABQSwMEFAAICAgAPGwuRAAAAAAAAAAAAAAAAB4AAAB2aWlra28xLVZpaWtrbzFfMDAzLkt1dXNpL3NyYy8DAFBLBwgAAAAAAgAAAAAAAABQSwMEFAAICAgAPGwuRAAAAAAAAAAAAAAAACgAAAB2aWlra28xLVZpaWtrbzFfMDAzLkt1dXNpL3NyYy9LdXVzaS5qYXZhRUw7DsIwDN1zCqtTy9AegCMwlg0xmGK1gcSJaqcSQr0NN+FiGBDiLe89vU8up+AHGAKKwK4U8XB3Dgz5m4iiGi3JnyGi57rX2fN4OALOozS/9htdB3si0OeD2YgmU4sZZE4MsVxtTgxpulCI6P7D/iZKsU1F22znGriuNlWz/RRW59YXUEsHCMDfbXyEAAAApgAAAFBLAQIUABQACAgIADxsLkQAAAAAAgAAAAAAAAAaAAAAAAAAAAAAAAAAAAAAAAB2aWlra28xLVZpaWtrbzFfMDAzLkt1dXNpL1BLAQIUABQACAgIADxsLkQAAAAAAgAAAAAAAAAeAAAAAAAAAAAAAAAAAEoAAAB2aWlra28xLVZpaWtrbzFfMDAzLkt1dXNpL3NyYy9QSwECFAAUAAgICAA8bC5EwN9tfIQAAACmAAAAKAAAAAAAAAAAAAAAAACYAAAAdmlpa2tvMS1WaWlra28xXzAwMy5LdXVzaS9zcmMvS3V1c2kuamF2YVBLBQYAAAAAAwADAOoAAAByAQAAAAA\\u003d");
        generateSnapshotEvent(COURSENAME, EXERCISENAME, "code_snapshot", 1L, 1L, "{\"cause\":\"file_delete\",\"file\":\"/src/Nimi2.java\"}", "UEsDBBQACAgIADxsLkQAAAAAAAAAAAAAAAAaAAAAdmlpa2tvMS1WaWlra28xXzAwMy5LdXVzaS8DAFBLBwgAAAAAAgAAAAAAAABQSwMEFAAICAgAPGwuRAAAAAAAAAAAAAAAAB4AAAB2aWlra28xLVZpaWtrbzFfMDAzLkt1dXNpL3NyYy8DAFBLBwgAAAAAAgAAAAAAAABQSwMEFAAICAgAPGwuRAAAAAAAAAAAAAAAACgAAAB2aWlra28xLVZpaWtrbzFfMDAzLkt1dXNpL3NyYy9LdXVzaS5qYXZhRUw7DsIwDN1zCqtTy9AegCMwlg0xmGK1gcSJaqcSQr0NN+FiGBDiLe89vU8up+AHGAKKwK4U8XB3Dgz5m4iiGi3JnyGi57rX2fN4OALOozS/9htdB3si0OeD2YgmU4sZZE4MsVxtTgxpulCI6P7D/iZKsU1F22znGriuNlWz/RRW59YXUEsHCMDfbXyEAAAApgAAAFBLAQIUABQACAgIADxsLkQAAAAAAgAAAAAAAAAaAAAAAAAAAAAAAAAAAAAAAAB2aWlra28xLVZpaWtrbzFfMDAzLkt1dXNpL1BLAQIUABQACAgIADxsLkQAAAAAAgAAAAAAAAAeAAAAAAAAAAAAAAAAAEoAAAB2aWlra28xLVZpaWtrbzFfMDAzLkt1dXNpL3NyYy9QSwECFAAUAAgICAA8bC5EwN9tfIQAAACmAAAAKAAAAAAAAAAAAAAAAACYAAAAdmlpa2tvMS1WaWlra28xXzAwMy5LdXVzaS9zcmMvS3V1c2kuamF2YVBLBQYAAAAAAwADAOoAAAByAQAAAAA\\u003d");
        generateSnapshotEvent(COURSENAME, EXERCISENAME, "code_snapshot", 1L, 1L, null, "UEsDBBQACAgIADxsLkQAAAAAAAAAAAAAAAAaAAAAdmlpa2tvMS1WaWlra28xXzAwMy5LdXVzaS8DAFBLBwgAAAAAAgAAAAAAAABQSwMEFAAICAgAPGwuRAAAAAAAAAAAAAAAAB4AAAB2aWlra28xLVZpaWtrbzFfMDAzLkt1dXNpL3NyYy8DAFBLBwgAAAAAAgAAAAAAAABQSwMEFAAICAgAPGwuRAAAAAAAAAAAAAAAACgAAAB2aWlra28xLVZpaWtrbzFfMDAzLkt1dXNpL3NyYy9LdXVzaS5qYXZhRUw7DsIwDN1zCqtTy9AegCMwlg0xmGK1gcSJaqcSQr0NN+FiGBDiLe89vU8up+AHGAKKwK4U8XB3Dgz5m4iiGi3JnyGi57rX2fN4OALOozS/9htdB3si0OeD2YgmU4sZZE4MsVxtTgxpulCI6P7D/iZKsU1F22znGriuNlWz/RRW59YXUEsHCMDfbXyEAAAApgAAAFBLAQIUABQACAgIADxsLkQAAAAAAgAAAAAAAAAaAAAAAAAAAAAAAAAAAAAAAAB2aWlra28xLVZpaWtrbzFfMDAzLkt1dXNpL1BLAQIUABQACAgIADxsLkQAAAAAAgAAAAAAAAAeAAAAAAAAAAAAAAAAAEoAAAB2aWlra28xLVZpaWtrbzFfMDAzLkt1dXNpL3NyYy9QSwECFAAUAAgICAA8bC5EwN9tfIQAAACmAAAAKAAAAAAAAAAAAAAAAACYAAAAdmlpa2tvMS1WaWlra28xXzAwMy5LdXVzaS9zcmMvS3V1c2kuamF2YVBLBQYAAAAAAwADAOoAAAByAQAAAAA\\u003d");
        generateSnapshotEvent(COURSENAME, EXERCISENAME, BASICTYPE, 1L, 1L, null,
                "eyJmaWxlIjoiL3NyYy9OaW1pLmphdmEiLCJwYXRjaGVzIjoiQEAgLTIzMSwyMSArMjMxLDQ0IEBAXG4gaS8gJTBBICAgICAgICBcbitTeXN0ZW0ub3V0LnByaW50bG4oJTIyJTIyKTtcbiAlMEEgICAgJTdEJTBBJTBBJTdEXG4iLCJmdWxsX2RvY3VtZW50IjpmYWxzZX0\\u003d");
        p.process(events);

        assertEquals(1, events.get(1).getFiles().size());
        assertEquals(1, events.get(events.size() - 1).getFiles().size());

        assertEquals("public class Nimi {\n    \n    public static void main(String[] args) {\n        // Kirjoita ohjelmasi tähän alle\n      \n        // Mikäli et vielä ole vastannut vielä kyselyyn, tee se HETI\n        // osoitteessa: http://laatu.jamo.fi/ \n        \n    }\n\n}", events.get(0).getFiles().get(FILENAME));

        assertEquals("public class Nimi {\n    \n    public static void main(String[] args) {\n        // Kirjoita ohjelmasi tähän alle\n      \n        // Mikäli et vielä ole vastannut vielä kyselyyn, tee se HETI\n        // osoitteessa: http://laatu.jamo.fi/ \n        System.out.println(\"\");\n    }\n\n}", events.get(events.size() - 1).getFiles().get(FILENAME));


    }

    private void generateSnapshotEvent(final String courseName, final String exerciseName,
            final String eventType, final Long happendAt, final Long systemNanoTime, final String metadata,
            final String data) {

        final SnapshotEvent snapshotEvent = new SnapshotEvent();

        snapshotEvent.setCourseName(courseName);
        snapshotEvent.setExerciseName(exerciseName);
        snapshotEvent.setEventType(eventType);
        snapshotEvent.setHappenedAt(happendAt);
        snapshotEvent.setSystemNanotime(systemNanoTime);
        snapshotEvent.setMetadata(metadata);
        snapshotEvent.setData(data);

        events.add(snapshotEvent);
    }

}
