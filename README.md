tmc-snapshot-api
================

[![Build Status](https://travis-ci.org/kesapojat/tmc-snapshot-api.svg?branch=master)](https://travis-ci.org/kesapojat/tmc-snapshot-api/)
[![Coverage Status](https://img.shields.io/coveralls/kesapojat/tmc-snapshot-api.svg)](https://coveralls.io/r/kesapojat/tmc-snapshot-api/)

A Spring Boot Web API for accessing TMC exercise snapshots (Java 7 + Java EE 7 + Spring Boot 1.1.4.RELEASE) from the [tmc-spyware-server](https://github.com/testmycode/tmc-spyware-server/).

## Start Server

Start the server with `mvn spring-boot:run`.

## Run Tests

Run tests with `mvn test`.

## Build

Build the project with `mvn package`.

## Configuration

You need to configure a few properties to get the API running — mainly setting the credentials for the [tmc-spyware-server](https://github.com/testmycode/tmc-spyware-server/) and the credentials for the service itself.

### Development

1. For development purposes create a configuration-file `src/main/resources/application-development.properties`. See a sample configuration in `src/main/resources/application-development.properties.sample`.
2. Modify the properties to set the credentials for the service and `tmc-spyware-server`.

### Production

1. Modify the active profile `spring.profiles.active` to `production` in `src/main/resources/application.properties`.
2. Create a configuration-file `src/main/resources/application-production.properties` for the production-profile. See a sample configuration in `src/main/resources/application-development.properties.sample`.
3. Modify the properties to set the credentials for the service and `tmc-spyware-server`.

You can also set additional properties as declared in the `.properties`-files.

## REST API

The REST API provides mostly JSON-responses as `application/json`. Files are returned as `text/plain`. Incase of an error, an error response is returned with the related status code and requested content-type. The API uses basic access for authentication.

### IDs

All IDs in the API are specified as strings. The ID for an instance is its name. The ID for a participant is a URL-safe Base64-encoded string from a username, which matches to the username specified in TMC. The ID for a course and exercise is a URL-safe Base64-encoded string from a course and exercise name, which also match to their corresponding names in TMC. The ID for a snapshot is a string concatenated from the timestamp and nanotime for the snapshot. The ID for a file is a URL-safe Base64-encoded string concatenated from a file path, snapshot timestamp and nanotime. The ID for an event is a string concatenated from the timestamp and nanotime for the event.

### Snapshot Level

There are two levels for snapshots. Key-level snapshots — which is the default — provide snapshots that progress one keystroke (or event) at a time. This provides a way to “playback” the snapshots, the same way as a participant has implemented the solution. Code-level snapshots provide snapshots that have a larger scope. These snapshots are “full snapshots” that are collected for example when the participant saves the solution.

### 1. Instances

```
Method: GET
Content-Type: application/json
URL: /
Returns: A list of instances
```

#### Example Request

`GET /`

```
[
    {
        "id": "hy"
    },
    {
        "id": "mooc"
    }
]
```

### 1.2. Instance

```
Method: GET
Content-Type: application/json
URL: /{instanceId}/
Returns: A single instance with the provided ID
```

#### Example Request

`GET /hy/`

```
{
    "id": "hy"
}
```

### 2. Participants

```
Method: GET
Content-Type: application/json
URL: /{instanceId}/participants/
Returns: A list of participants with the provided instance ID
```

#### Example Request

`GET /hy/participants/`

```
[
    {
        "id": "MDEyMzQ1Njc4",
        "username": "012345678"
    },
    {
        "id": "MDIyMzQ1Njc4",
        "username": "022345678"
    }
]
```

### 2.2. Participant

```
Method: GET
Content-Type: application/json
URL: /{instanceId}/participants/{participantId}/
Returns: A single participant with the provided IDs
```

#### Example Request

`GET /hy/participants/MDEyMzQ1Njc4/`

```
{
    "id": "MDEyMzQ1Njc4",
    "username": "012345678",
    "courses": [
        {
            "id": "WFhYLW9oamEta2VydGF1cw",
            "name": "XXX-ohja-kertaus",
            "exercises": [
                {
                    "id": "c2V0dGkxLTAxLlRhdmFyYU1hdGthbGF1a2t1SmFMYXN0aXJ1dW1h",
                    "name": "setti1-01.TavaraMatkalaukkuJaLastiruuma"
                },
                {
                    "id": "c2V0dGkxLTAyLkxlbXBpbmltZXQ",
                    "name": "setti1-02.Lempinimet"
                }
            ]
        }
    ]
}
```

### 3. Courses

```
Method: GET
Content-Type: application/json
URL: /{instanceId}/participants/{participantId}/courses/
Returns: A list of courses for a participant with the provided IDs
```

#### Example Request

`GET /hy/participants/MDEyMzQ1Njc4/courses/`

```
[
    {
        "id": "WFhYLW9oamEta2VydGF1cw",
        "name": "XXX-ohja-kertaus",
        "exercises": [
            {
                "id": "c2V0dGkxLTAxLlRhdmFyYU1hdGthbGF1a2t1SmFMYXN0aXJ1dW1h",
                "name": "setti1-01.TavaraMatkalaukkuJaLastiruuma"
            },
            {
                "id": "c2V0dGkxLTAyLkxlbXBpbmltZXQ",
                "name": "setti1-02.Lempinimet"
            }
        ]
    }
]
```

### 3.2. Course

```
Method: GET
Content-Type: application/json
URL: /{instanceId}/participants/{participantId}/courses/{courseId}/
Returns: A single course for a participant with the provided IDs
```

#### Example Request

`GET /hy/participants/MDEyMzQ1Njc4/courses/WFhYLW9oamEta2VydGF1cw/`

```
{
    "id": "WFhYLW9oamEta2VydGF1cw",
    "name": "XXX-ohja-kertaus",
    "exercises": [
        {
            "id": "c2V0dGkxLTAxLlRhdmFyYU1hdGthbGF1a2t1SmFMYXN0aXJ1dW1h",
            "name": "setti1-01.TavaraMatkalaukkuJaLastiruuma"
        },
        {
            "id": "c2V0dGkxLTAyLkxlbXBpbmltZXQ",
            "name": "setti1-02.Lempinimet"
        }
    ]
}
```

### 4. Exercises

```
Method: GET
Content-Type: application/json
URL: /{instanceId}/participants/{participantId}/courses/{courseId}/exercises/
Returns: A list of exercises for a participant and course with the provided IDs
```

#### Example Request

`GET /hy/participants/MDEyMzQ1Njc4/courses/WFhYLW9oamEta2VydGF1cw/exercises/`

```
[
    {
        "id": "c2V0dGkxLTAxLlRhdmFyYU1hdGthbGF1a2t1SmFMYXN0aXJ1dW1h",
        "name": "setti1-01.TavaraMatkalaukkuJaLastiruuma"
    },
    {
        "id": "c2V0dGkxLTAyLkxlbXBpbmltZXQ",
        "name": "setti1-02.Lempinimet"
    }
]
```

### 4.2. Exercise

```
Method: GET
Content-Type: application/json
URL: /{instanceId}/participants/{participantId}/courses/{courseId}/exercises/{exerciseId}/
Returns: A single exercise for a participant and course with the provided IDs
```

#### Example Request

`GET /hy/participants/MDEyMzQ1Njc4/courses/WFhYLW9oamEta2VydGF1cw/exercises/c2V0dGkxLTAxLlRhdmFyYU1hdGthbGF1a2t1SmFMYXN0aXJ1dW1h/`

```
{
    "id": "c2V0dGkxLTAxLlRhdmFyYU1hdGthbGF1a2t1SmFMYXN0aXJ1dW1h",
    "name": "setti1-01.TavaraMatkalaukkuJaLastiruuma"
}
```

### 5. Snapshots

```
Method: GET
Content-Type: application/json
URL: /{instanceId}/participants/{participantId}/courses/{courseId}/exercises/{exerciseId}/snapshots/
Parameters: level [key, code or raw], defaults to key when no level is defined
Returns: A list of snapshots for a participant, course and exercise with the provided IDs
```

#### Example Request

`GET /hy/participants/MDEyMzQ1Njc4/courses/WFhYLW9oamEta2VydGF1cw/exercises/c2V0dGkxLTAxLlRhdmFyYU1hdGthbGF1a2t1SmFMYXN0aXJ1dW1h/snapshots/`

```
[
    {
        "id": "14067100104316662619414800",
        "timestamp": 1406710010431,
        "files": [
            {
                "path": "src/Main.java",
                "id": "L3NyYy9NYWluLmphdmExNDA2NzEwMDEwNDMxMA",
                "name": "Main.java"
            }
        ]
    },
    {
        "id": "14067100104546662642970893",
        "timestamp": 1406710010454,
        "files": [
            {
                "path": "src/Main.java",
                "id": "L3NyYy9NYWluLmphdmExNDA2NzEwMDEwNDU0MA",
                "name": "Main.java"
            }
        ]
    },
    {
        "id": "14067100562006708388460953",
        "timestamp": 1406710056200,
        "files": [
            {
                "path": "src/Main.java",
                "id": "L3NyYy9NYWluLmphdmExNDA2NzEwMDU2MjAwMA",
                "name": "Main.java"
            },
            {
                "path": "src/testi/TestiLuokka.java",
                "id": "L3NyYy90ZXN0aS9UZXN0aUx1b2trYS5qYXZhMTQwNjcxMDA1NjIwMDA",
                "name": "TestiLuokka.java"
            }
        ]
    }
]
```

`GET /hy/participants/MDEyMzQ1Njc4/courses/WFhYLW9oamEta2VydGF1cw/exercises/c2V0dGkxLTAxLlRhdmFyYU1hdGthbGF1a2t1SmFMYXN0aXJ1dW1h/snapshots/?level=raw`

```
[
    {
        courseName: "2014-mooc-no-deadline",
        exerciseName: "viikko1-Viikko1_001.Nimi",
        eventType: "code_snapshot",
        happenedAt: 1411297524487,
        systemNanotime: 4569956362905,
        metadata: "{"cause":"file_create","file":"/nbproject/private/private.properties"}",
        data: "UEsFBgAAAAAAAAAAAAAAAAAAAAAAAA==",
        files: { },
        projectActionEvent: false,
        codeSnapshot: true
    },
    {
        courseName: "2014-mooc-no-deadline",
        exerciseName: "viikko1-Viikko1_001.Nimi",
        eventType: "text_remove",
        happenedAt: 1411297721309,
        systemNanotime: 4766776038639,
        metadata: null,
        data: "eyJmaWxlIjoiL3NyYy9OaW1pLmphdmEiLCJwYXRjaGVzIjoiQEAgLTAsMCArMSw4OCBAQFxuK3B1YmxpYyBjbGFzcyBOaW1pICU3QiUwQSAgICAlMEEgICAgcHVibGljIHN0YXRpYyB2b2lkIG1haW4oU3RyaW5nJTVCJTVEIGFyZ3MpICU3QiUwQSAgICAgICAgJTBBJTBBICAgICU3RCUwQSUwQSU3RFxuIiwiZnVsbF9kb2N1bWVudCI6dHJ1ZX0=",
        files: { },
        projectActionEvent: false,
        codeSnapshot: false
    },
    {
        courseName: "2014-mooc-no-deadline",
        exerciseName: "viikko1-Viikko1_001.Nimi",
        eventType: "text_insert",
        happenedAt: 1411297721339,
        systemNanotime: 4766808950124,
        metadata: null,
        data: "eyJmaWxlIjoiL3NyYy9OaW1pLmphdmEiLCJwYXRjaGVzIjoiQEAgLTcxLDE2ICs3MSwxNyBAQFxuICAgICAgICAgXG4rU1xuICUwQSUwQSAgICAlN0QlMEFcbiIsImZ1bGxfZG9jdW1lbnQiOmZhbHNlfQ==",
        files: { },
        projectActionEvent: false,
        codeSnapshot: false
    },
    {
        courseName: "2014-mooc-no-deadline",
        exerciseName: "viikko1-Viikko1_001.Nimi",
        eventType: "text_insert",
        happenedAt: 1411297721749,
        systemNanotime: 4767212587047,
        metadata: null,
        data: "eyJmaWxlIjoiL3NyYy9OaW1pLmphdmEiLCJwYXRjaGVzIjoiQEAgLTcyLDE2ICs3MiwxNyBAQFxuICAgICAgICBTXG4reVxuICUwQSUwQSAgICAlN0QlMEFcbiIsImZ1bGxfZG9jdW1lbnQiOmZhbHNlfQ==",
        files: { },
        projectActionEvent: false,
        codeSnapshot: false
    }
]
```

### 5.2. Snapshot File Contents

```
Method: GET
Content-Type: application/zip
URL: /{instanceId}/participants/{participantId}/courses/{courseId}/exercises/{exerciseId}/snapshots/files.zip
Parameters: level [key or code], defaults to key when no level is defined
Returns: The contents for each file in the snapshot as a ZIP for a participant, course and exercise with the provided IDs
```

#### Example Request

`GET /hy/participants/MDEyMzQ1Njc4/courses/WFhYLW9oamEta2VydGF1cw/exercises/c2V0dGkxLTAxLlRhdmFyYU1hdGthbGF1a2t1SmFMYXN0aXJ1dW1h/snapshots/files.zip`

```
.
└── 14067100562006708388460953 # Snapshot ID
    ├── L3NyYy9NYWluLmphdmExNDA2NzEwMDU2MjAwMA # File ID
    └── L3NyYy90ZXN0aS9UZXN0aUx1b2trYS5qYXZhMTQwNjcxMDA1NjIwMDA
```

### 5.3. Snapshot

```
Method: GET
Content-Type: application/json
URL: /{instanceId}/participants/{participantId}/courses/{courseId}/exercises/{exerciseId}/snapshots/{snapshotId}/
Parameters: level [key or code], defaults to key when no level is defined
Returns: A single snapshot for a participant, course and exercise with the provided IDs
```

#### Example Request

`GET /hy/participants/MDEyMzQ1Njc4/courses/WFhYLW9oamEta2VydGF1cw/exercises/c2V0dGkxLTAxLlRhdmFyYU1hdGthbGF1a2t1SmFMYXN0aXJ1dW1h/snapshots/14067100562006708388460953/`

```
{
    "id": "14067100562006708388460953",
    "timestamp": 1406710056200,
    "files": [
        {
            "path": "src/Main.java",
            "id": "L3NyYy9NYWluLmphdmExNDA2NzEwMDU2MjAwMA",
            "name": "Main.java"
        },
        {
            "path": "src/testi/TestiLuokka.java",
            "id": "L3NyYy90ZXN0aS9UZXN0aUx1b2trYS5qYXZhMTQwNjcxMDA1NjIwMDA",
            "name": "TestiLuokka.java"
        }
    ]
}
```

### 6. Files

```
Method: GET
Content-Type: application/json
URL: /{instanceId}/participants/{participantId}/courses/{courseId}/exercises/{exerciseId}/snapshots/{snapshotId}/files/
Parameters: level [key or code], defaults to key when no level is defined
Returns: A list of files for a participant, course, exercise and snapshot with the provided IDs
```

#### Example Request

`GET /hy/participants/MDEyMzQ1Njc4/courses/WFhYLW9oamEta2VydGF1cw/exercises/c2V0dGkxLTAxLlRhdmFyYU1hdGthbGF1a2t1SmFMYXN0aXJ1dW1h/snapshots/14067100562006708388460953/files/`

```
[
    {
        "path": "src/Main.java",
        "id": "L3NyYy9NYWluLmphdmExNDA2NzEwMDU2MjAwMA",
        "name": "Main.java"
    },
    {
        "path": "src/testi/TestiLuokka.java",
        "id": "L3NyYy90ZXN0aS9UZXN0aUx1b2trYS5qYXZhMTQwNjcxMDA1NjIwMDA",
        "name": "TestiLuokka.java"
    }
]
```

### 6.2. File

```
Method: GET
Content-Type: application/json
URL: /{instanceId}/participants/{participantId}/courses/{courseId}/exercises/{exerciseId}/snapshots/{snapshotId}/files/{fileId}/
Parameters: level [key or code], defaults to key when no level is defined
Returns: A single file for a participant, course, exercise, snapshot and file with the provided IDs
```

#### Example Request

`GET /hy/participants/MDEyMzQ1Njc4/courses/WFhYLW9oamEta2VydGF1cw/exercises/c2V0dGkxLTAxLlRhdmFyYU1hdGthbGF1a2t1SmFMYXN0aXJ1dW1h/snapshots/14067100562006708388460953/files/L3NyYy9NYWluLmphdmExNDA2NzEwMDU2MjAwMA/`

```
{
    "path": "src/Main.java",
    "id": "L3NyYy9NYWluLmphdmExNDA2NzEwMDU2MjAwMA",
    "name": "Main.java"
}
```

### 6.3. File Content

```
Method: GET
Content-Type: text/plain
URL: /{instanceId}/participants/{participantId}/courses/{courseId}/exercises/{exerciseId}/snapshots/{snapshotId}/files/{fileId}/content/
Parameters: level [key or code], defaults to key when no level is defined
Returns: The content for a file for a participant, course, exercise, snapshot and file with the provided IDs
```

#### Example Request

`GET /hy/participants/MDEyMzQ1Njc4/courses/WFhYLW9oamEta2VydGF1cw/exercises/c2V0dGkxLTAxLlRhdmFyYU1hdGthbGF1a2t1SmFMYXN0aXJ1dW1h/snapshots/14067100562006708388460953/files/L3NyYy9NYWluLmphdmExNDA2NzEwMDU2MjAwMA/content/`

```
public class Main {

    public static void main(String[] args) {
        System.out.println("Hello, World!")
    }
}
```

### 7. Events

```
Method: GET
Content-Type: application/json
URL: /{instanceId}/participants/{participantId}/courses/{courseId}/exercises/{exerciseId}/events/
Returns: A list of events for a participant, course and exercise with the provided IDs
```

#### Example Request

`GET /hy/participants/MDEyMzQ1Njc4/courses/WFhYLW9oamEta2VydGF1cw/exercises/c2V0dGkxLTAxLlRhdmFyYU1hdGthbGF1a2t1SmFMYXN0aXJ1dW1h/events/`

```
[
    {
        "id": "139526261085413507305253514",
        "eventType": "text_insert",
        "timestamp": 1395262610854,
        "metadata": {}
    },
    {
        "id": "139526257830313474754345700",
        "eventType": "text_remove",
        "timestamp": 1395262578303,
        "metadata": {}
    },
    {
        "id": "139526261650613512957311747",
        "eventType": "code_snapshot",
        "timestamp": 1395262616506,
        "metadata": {
            "cause": "file_change",
            "file": "/src/Main.java"
        }
    }
]
```

### 7. Event

```
Method: GET
Content-Type: application/json
URL: /{instanceId}/participants/{participantId}/courses/{courseId}/exercises/{exerciseId}/events/{eventId}/
Returns: A single event for a participant, course and exercise with the provided IDs
```

#### Example Request

`GET /hy/participants/MDEyMzQ1Njc4/courses/WFhYLW9oamEta2VydGF1cw/exercises/c2V0dGkxLTAxLlRhdmFyYU1hdGthbGF1a2t1SmFMYXN0aXJ1dW1h/events/139526261085413507305253514/`

```
{
    "id": "139526261085413507305253514",
    "eventType": "text_insert",
    "timestamp": 1395262610854,
    "metadata": {}
}
```

## Credits

This project has been developed at the University of Helsinki’s [Department of Computer Science](http://cs.helsinki.fi/en/) by:

* Kenny Heinonen ([kennyhei](https://github.com/kennyhei/))
* Kasper Hirvikoski ([kasperhirvikoski](https://github.com/kasperhirvikoski/))
* Leo Leppänen ([ljleppan](https://github.com/ljleppan/))
* Joni Salmi ([josalmi](https://github.com/josalmi/))

## License

This project is licensed under [GPL2](LICENSE.txt).
