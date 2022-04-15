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

## CRUD Operations

### Creating an Event

```shell
curl localhost:8080 -X POST localhost:8080/event/alpha
```

### Getting an Event List

```shell
curl localhost:8080/event/alpha
```

### Getting All Event Lists

```shell
curl localhost:8080/events
```

### Deleting an Event

```shell
curl localhost:8080 -X DELETE localhost:8080/event/alpha
```