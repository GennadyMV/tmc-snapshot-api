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

All IDs in the API are specified as strings. The ID for a participant is a URL-safe Base64-encoded string from a username, which matches to the username specified in TMC. The ID for a course and exercise is a URL-safe Base64-encoded string from a course and exercise name, which also match to their corresponding names in TMC. The ID for a snapshot is a string concatenated from the timestamp and nanotime for the snapshot. The ID for a file is a URL-safe Base64-encoded string concatenated from a file path, snapshot timestamp and nanotime.

### Snapshot Level

There are two levels for snapshots. Key-level snapshots — which is the default — provide snapshots that progress one keystroke (or event) at a time. This provides a way to “playback” the snapshots, the same way as a participant has implemented the solution. Code-level snapshots provide snapshots that have a larger scope. These snapshots are “full snapshots” that are collected for example when the participant saves the solution.

### 1. Participants

```
Method: GET
Content-Type: application/json
URL: /{instance}/participants/
Returns: A list of participants with the provided instance
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

### 1.2. Participant

```
Method: GET
Content-Type: application/json
URL: /{instance}/participants/{participantID}/
Returns: A single participant with the provided instance and ID
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

### 2. Courses

```
Method: GET
Content-Type: application/json
URL: /{instance}/participants/{participantID}/courses/
Returns: A list of courses for a participant with the provided instance and ID
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

### 2.2. Course

```
Method: GET
Content-Type: application/json
URL: /{instance}/participants/{participantID}/courses/{courseID}/
Returns: A single course for a participant with the provided instance and IDs
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

### 3. Exercises

```
Method: GET
Content-Type: application/json
URL: /{instance}/participants/{participantID}/courses/{courseID}/exercises/
Returns: A list of exercises for a participant and course with the provided instance and IDs
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

### 3.2. Exercise

```
Method: GET
Content-Type: application/json
URL: /{instance}/participants/{participantID}/courses/{courseID}/exercises/{exerciseID}/
Returns: A single exercise for a participant and course with the provided instance and IDs
```

#### Example Request

`GET /hy/participants/MDEyMzQ1Njc4/courses/WFhYLW9oamEta2VydGF1cw/exercises/c2V0dGkxLTAxLlRhdmFyYU1hdGthbGF1a2t1SmFMYXN0aXJ1dW1h/`

```
{
    "id": "c2V0dGkxLTAxLlRhdmFyYU1hdGthbGF1a2t1SmFMYXN0aXJ1dW1h",
    "name": "setti1-01.TavaraMatkalaukkuJaLastiruuma"
}
```

### 4. Snapshots

```
Method: GET
Content-Type: application/json
URL: /{instance}/participants/{participantID}/courses/{courseID}/exercises/{exerciseID}/snapshots/
Parameters: level [key or code], defaults to key when no level is defined
Returns: A list of snapshots for a participant, course and exercise with the provided instance and IDs
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
                "path": "/src/Main.java",
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
                "path": "/src/Main.java",
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
                "path": "/src/Main.java",
                "id": "L3NyYy9NYWluLmphdmExNDA2NzEwMDU2MjAwMA",
                "name": "Main.java"
            },
            {
                "path": "/src/testi/TestiLuokka.java",
                "id": "L3NyYy90ZXN0aS9UZXN0aUx1b2trYS5qYXZhMTQwNjcxMDA1NjIwMDA",
                "name": "TestiLuokka.java"
            }
        ]
    }
]
```

### 4.2. Snapshot File Contents

```
Method: GET
Content-Type: application/zip
URL: /{instance}/participants/{participantID}/courses/{courseID}/exercises/{exerciseID}/snapshots/files.zip
Parameters: level [key or code], defaults to key when no level is defined
Returns: The contents for each file in the snapshot as a ZIP for a participant, course and exercise with the provided instance and IDs
```

#### Example Request

`GET /hy/participants/MDEyMzQ1Njc4/courses/WFhYLW9oamEta2VydGF1cw/exercises/c2V0dGkxLTAxLlRhdmFyYU1hdGthbGF1a2t1SmFMYXN0aXJ1dW1h/snapshots/files.zip`

```
.
└── 14067100562006708388460953 # Snapshot ID
    ├── L3NyYy9NYWluLmphdmExNDA2NzEwMDU2MjAwMA # File ID
    └── L3NyYy90ZXN0aS9UZXN0aUx1b2trYS5qYXZhMTQwNjcxMDA1NjIwMDA
```

### 4.3. Snapshot

```
Method: GET
Content-Type: application/json
URL: /{instance}/participants/{participantID}/courses/{courseID}/exercises/{exerciseID}/snapshots/{snapshotID}/
Parameters: level [key or code], defaults to key when no level is defined
Returns: A single snapshot for a participant, course and exercise with the provided instance and IDs
```

#### Example Request

`GET /hy/participants/MDEyMzQ1Njc4/courses/WFhYLW9oamEta2VydGF1cw/exercises/c2V0dGkxLTAxLlRhdmFyYU1hdGthbGF1a2t1SmFMYXN0aXJ1dW1h/snapshots/14067100562006708388460953/`

```
{
    "id": "14067100562006708388460953",
    "timestamp": 1406710056200,
    "files": [
        {
            "path": "/src/Main.java",
            "id": "L3NyYy9NYWluLmphdmExNDA2NzEwMDU2MjAwMA",
            "name": "Main.java"
        },
        {
            "path": "/src/testi/TestiLuokka.java",
            "id": "L3NyYy90ZXN0aS9UZXN0aUx1b2trYS5qYXZhMTQwNjcxMDA1NjIwMDA",
            "name": "TestiLuokka.java"
        }
    ]
}
```

### 5. Files

```
Method: GET
Content-Type: application/json
URL: /{instance}/participants/{participantID}/courses/{courseID}/exercises/{exerciseID}/snapshots/{snapshotID}/files/
Parameters: level [key or code], defaults to key when no level is defined
Returns: A list of files for a participant, course, exercise and snapshot with the provided instance and IDs
```

#### Example Request

`GET /hy/participants/MDEyMzQ1Njc4/courses/WFhYLW9oamEta2VydGF1cw/exercises/c2V0dGkxLTAxLlRhdmFyYU1hdGthbGF1a2t1SmFMYXN0aXJ1dW1h/snapshots/14067100562006708388460953/files/`

```
[
    {
        "path": "/src/Main.java",
        "id": "L3NyYy9NYWluLmphdmExNDA2NzEwMDU2MjAwMA",
        "name": "Main.java"
    },
    {
        "path": "/src/testi/TestiLuokka.java",
        "id": "L3NyYy90ZXN0aS9UZXN0aUx1b2trYS5qYXZhMTQwNjcxMDA1NjIwMDA",
        "name": "TestiLuokka.java"
    }
]
```

### 5.2. File

```
Method: GET
Content-Type: application/json
URL: /{instance}/participants/{participantID}/courses/{courseID}/exercises/{exerciseID}/snapshots/{snapshotID}/files/{fileID}/
Parameters: level [key or code], defaults to key when no level is defined
Returns: A single file for a participant, course, exercise, snapshot and file with the provided instance and IDs
```

#### Example Request

`GET /hy/participants/MDEyMzQ1Njc4/courses/WFhYLW9oamEta2VydGF1cw/exercises/c2V0dGkxLTAxLlRhdmFyYU1hdGthbGF1a2t1SmFMYXN0aXJ1dW1h/snapshots/14067100562006708388460953/files/L3NyYy9NYWluLmphdmExNDA2NzEwMDU2MjAwMA/`

```
{
    "path": "/src/Main.java",
    "id": "L3NyYy9NYWluLmphdmExNDA2NzEwMDU2MjAwMA",
    "name": "Main.java"
}
```

### 5.3. File Content

```
Method: GET
Content-Type: text/plain
URL: /{instance}/participants/{participantID}/courses/{courseID}/exercises/{exerciseID}/snapshots/{snapshotID}/files/{fileID}/content/
Parameters: level [key or code], defaults to key when no level is defined
Returns: The content for a file for a participant, course, exercise, snapshot and file with the provided instance and IDs
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

## Credits

This project has been developed at the University of Helsinki’s [Department of Computer Science](http://cs.helsinki.fi/en/) by:

* Kenny Heinonen ([kennyhei](https://github.com/kennyhei/))
* Kasper Hirvikoski ([kasperhirvikoski](https://github.com/kasperhirvikoski/))
* Leo Leppänen ([ljleppan](https://github.com/ljleppan/))
* Joni Salmi ([josalmi](https://github.com/josalmi/))

## License

This project is licensed under [GPL2](LICENSE.txt).
