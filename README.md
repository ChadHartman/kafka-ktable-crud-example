# Kafka KTable CRUD Example

This project is an example implementation of Kafka KTable CRUD.

---

## Prereqs

* Docker Compose
* Java 11

---

## Steps to Run

In the current directory:

1. `docker compose up -d`
2. `mvn quarkus:dev`

### Readiness Check

```shell
curl localhost:8080/q/health/ready
```

will respond with:

```json
{
    "status": "UP",
    "checks": [
        {
            "name": "SmallRye Reactive Messaging - readiness check",
            "status": "UP"
        },
        {
            "name": "app",
            "status": "UP"
        }
    ]
}
```

---

## "CRUD" Operations

Conventional creation, update, delete operations are feasible; but the concept of a replace does not really exist.

### Creating an Event

```shell
curl localhost:8080 -X POST localhost:8080/event/alpha | jq
```

will respond with:

```json
{
    "id": 1,
    "timestamp": "2022-04-15T19:15:10.560241Z",
    "name": "alpha",
    "deleteEvent": false
}
```

### Getting an Event List

```shell
curl localhost:8080/event/alpha | jq
```

will respond with

```json
{
    "alpha": [
        {
            "id": 1,
            "timestamp": "2022-04-15T19:15:10.560241Z",
            "name": "alpha",
            "deleteEvent": false
        }
    ]
}
```

### Getting All Event Lists

```shell
curl localhost:8080/events | jq
```

will respond with

```json
{
    "alpha": [
        {
            "id": 1,
            "timestamp": "2022-04-15T19:15:10.560241Z",
            "name": "alpha",
            "deleteEvent": false
        }
    ]
}
```

NOTE: there will be multiple names if provided.

### Deleting an Event

As with creations and updates; deletions must occur with an event:

```shell
curl localhost:8080 -X DELETE localhost:8080/event/alpha | jq
```

will respond with

```json
{
    "id": 6,
    "timestamp": "2022-04-15T19:16:19.606692Z",
    "name": "alpha",
    "deleteEvent": true
}
```