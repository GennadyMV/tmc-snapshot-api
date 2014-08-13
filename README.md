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

#API
##Participant

Information about a single participant.

```
/{instance}/participants/{participantID}/
```
```
/hy/participants/MDEyMzQ1Njc4/
```
```
{
  "id":"MDEyMzQ1Njc4",
  "username":"012345678"
}
```

##Courses

A single participant's course list.

```
/{instance}/participants/{participantID}/courses/
```
```
/hy/participants/MDEyMzQ1Njc4/courses
```
```
[
  {
    "id":"WFhYLW9ocGUta2VydGF1cw==",
    "name":"XXX-ohpe-kertaus"
  },
  {
    "id":"WFhYLW9oamEta2VydGF1cw",
    "name":"XXX-ohja-kertaus"
  }
]
```

##Single course

Information about a single participant's single course.

```
/{instance}/participants/{participantID}/courses/{courseID}
```
```
/hy/participants/MDEyMzQ1Njc4/courses/WFhYLW9oamEta2VydGF1cw
```
```
{
  "id":"WFhYLW9oamEta2VydGF1cw",
  "name":"XXX-ohja-kertaus"
}
```

##Exercises

A list containing all the exercises of a single course for the requested participant.

```
/{instance}/participants/{participantID}/courses/{courseID}/exercises
```
```
/hy/participants/MDEyMzQ1Njc4/courses/WFhYLW9oamEta2VydGF1cw/exercises
```
```
[
  {
    "id":"c2V0dGkxLTAxLlRhdmFyYU1hdGthbGF1a2t1SmFMYXN0aXJ1dW1h",
    "name":"setti1-01.TavaraMatkalaukkuJaLastiruuma"
  },
  {
    "id":"c2V0dGkxLTAyLkxlbXBpbmltZXQ=",
    "name":"setti1-02.Lempinimet"
  }
]
```

##Single Exercise

An exercise of the requested participant.

```
/{instance}/participants/{participantID}/courses/{courseID}/exercises/{exerciseID}
```
```
/hy/participants/MDEyMzQ1Njc4/courses/WFhYLW9oamEta2VydGF1cw/exercises/c2V0dGkxLTAxLlRhdmFyYU1hdGthbGF1a2t1SmFMYXN0aXJ1dW1h
```
```
{
  "id":"c2V0dGkxLTAxLlRhdmFyYU1hdGthbGF1a2t1SmFMYXN0aXJ1dW1h",
  "name":"setti1-01.TavaraMatkalaukkuJaLastiruuma"
}
```

##Snapshots

A list of all the snapshots of the requested participants requested exercise.

```
/{instance}/participants/{participantID}/courses/{courseID}/exercises/{exerciseID}/snapshots/
```
```
/hy/participants/MDEyMzQ1Njc4/courses/WFhYLW9oamEta2VydGF1cw/exercises/c2V0dGkxLTAxLlRhdmFyYU1hdGthbGF1a2t1SmFMYXN0aXJ1dW1h/snapshots
```
```
[
  {
    "id":"14067100104316662619414800",
    "timestamp":1406710010431,
    "files":
      [
        "/src/Main.java"
      ]
  },
  {
    "id":"14067100104546662642970893",
    "timestamp":1406710010454,
    "files":
      [
        "/src/Main.java"
      ]
  },
  {
    "id":"14067100562006708388460953",
    "timestamp":1406710056200,
    "files":
      [
        "/src/Main.java",
        "/src/testi/TestiLuokka.java"
      ]
  }
]
```

##Single snapshots

A single snapshot.

```
/{instance}/participants/{participantID}/courses/{courseID}/exercises/{exerciseID}/snapshots/{snapshotID}
```
```
/hy/participants/MDEyMzQ1Njc4/courses/WFhYLW9oamEta2VydGF1cw/exercises/c2V0dGkxLTAxLlRhdmFyYU1hdGthbGF1a2t1SmFMYXN0aXJ1dW1h/snapshots/14067100562006708388460953
```
```
{
  "id":"14067100562006708388460953",
  "timestamp":1406710056200,
  "files":
    [
      "/src/Main.java",
      "/src/testi/TestiLuokka.java"
    ]
}
```

##Files

A list of all the files in the requested snapshot.

```
/{instance}/participants/{participantID}/courses/{courseID}/exercises/{exerciseID}/snapshots/{snapshotID}/files/
```
```
/hy/participants/MDEyMzQ1Njc4/courses/WFhYLW9oamEta2VydGF1cw/exercises/c2V0dGkxLTAxLlRhdmFyYU1hdGthbGF1a2t1SmFMYXN0aXJ1dW1h/snapshots/14067100562006708388460953/files
```
```
[
  "/src/Main.java",
  "/src/testi/TestiLuokka.java"
]
```

##Single file

The content of the requested file at the time the requested snapshot was taken, as plain text.

```
/{instance}/participants/{participantID}/courses/{courseID}/exercises/{exerciseID}/snapshots/{snapshotID}/files/{filePath}
```
```
/hy/participants/MDEyMzQ1Njc4/courses/WFhYLW9oamEta2VydGF1cw/exercises/c2V0dGkxLTAxLlRhdmFyYU1hdGthbGF1a2t1SmFMYXN0aXJ1dW1h/snapshots/14067100562006708388460953/files/src/Main.java
```
```
public class Main {

    public static void main(String[] args) {
        System.out.println("Hello, World!")
    }
}
```
