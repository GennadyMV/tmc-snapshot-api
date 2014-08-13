tmc-snapshot-api
================

[![Build Status](https://travis-ci.org/kesapojat/tmc-snapshot-api.svg?branch=master)](https://travis-ci.org/kesapojat/tmc-snapshot-api/)
[![Coverage Status](https://img.shields.io/coveralls/kesapojat/tmc-snapshot-api.svg)](https://coveralls.io/r/kesapojat/tmc-snapshot-api/)

A Spring Boot Web API for accessing TMC exercise snapshots (Java 7 + Java EE 7 + Spring Boot 1.1.4.RELEASE).

## Start Server

Start the server with `mvn spring-boot:run`.

## Run Tests

Run tests with `mvn test`.

## Build

Build the project with `mvn package`.

## REST API

The REST API provides mostly JSON-responses as `application/json`. Files are returned as `text/plain`. Incase of an error, an error response is returned with the related status code and requested content-type. The API uses basic access for authentication.

### 1. Participant

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
    "username": "012345678"
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
        "name": "XXX-ohja-kertaus"
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
    "name": "XXX-ohja-kertaus"
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
Returns: A list of snapshots for a participant, course and exercise with the provided instance and IDs
```

#### Example Request

`GET /hy/participants/MDEyMzQ1Njc4/courses/WFhYLW9oamEta2VydGF1cw/exercises/c2V0dGkxLTAxLlRhdmFyYU1hdGthbGF1a2t1SmFMYXN0aXJ1dW1h/snapshots/`

```
[
    {
        "id": "14067100104316662619414800",
        "timestamp": 1406710010431,
        "files":
            [
                "/src/Main.java"
            ]
    },
    {
        "id": "14067100104546662642970893",
        "timestamp": 1406710010454,
        "files":
            [
                "/src/Main.java"
            ]
    },
    {
        "id": "14067100562006708388460953",
        "timestamp": 1406710056200,
        "files":
            [
                "/src/Main.java",
                "/src/testi/TestiLuokka.java"
            ]
    }
]
```

### 4.2. Snapshot

```
Method: GET
Content-Type: application/json
URL: /{instance}/participants/{participantID}/courses/{courseID}/exercises/{exerciseID}/snapshots/{snapshotID}/
Returns: A single snapshot for a participant, course and exercise with the provided instance and IDs
```

#### Example Request

`GET /hy/participants/MDEyMzQ1Njc4/courses/WFhYLW9oamEta2VydGF1cw/exercises/c2V0dGkxLTAxLlRhdmFyYU1hdGthbGF1a2t1SmFMYXN0aXJ1dW1h/snapshots/14067100562006708388460953/`

```
{
    "id": "14067100562006708388460953",
    "timestamp": 1406710056200,
    "files":
        [
            "/src/Main.java",
            "/src/testi/TestiLuokka.java"
        ]
}
```

### 5. Files

```
Method: GET
Content-Type: application/json
URL: /{instance}/participants/{participantID}/courses/{courseID}/exercises/{exerciseID}/snapshots/{snapshotID}/files/
Returns: A list of files for a participant, course, exercise and snapshot with the provided instance and IDs
```

#### Example Request

`GET /hy/participants/MDEyMzQ1Njc4/courses/WFhYLW9oamEta2VydGF1cw/exercises/c2V0dGkxLTAxLlRhdmFyYU1hdGthbGF1a2t1SmFMYXN0aXJ1dW1h/snapshots/14067100562006708388460953/files/`

```
[
    "/src/Main.java",
    "/src/testi/TestiLuokka.java"
]
```

### 5.2. File

```
Method: GET
Content-Type: text/plain
URL: /{instance}/participants/{participantID}/courses/{courseID}/exercises/{exerciseID}/snapshots/{snapshotID}/files/{filePath}
Returns: The content for a file for a participant, course, exercise and snapshot with the provided instance and IDs
```

#### Example Request

`GET /hy/participants/MDEyMzQ1Njc4/courses/WFhYLW9oamEta2VydGF1cw/exercises/c2V0dGkxLTAxLlRhdmFyYU1hdGthbGF1a2t1SmFMYXN0aXJ1dW1h/snapshots/14067100562006708388460953/files/src/Main.java`

```
public class Main {

    public static void main(String[] args) {
        System.out.println("Hello, World!")
    }
}
```
