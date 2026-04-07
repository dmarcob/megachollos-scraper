# Kata 2 — Understanding Containers

## Goal

Understand Docker images, container configuration, and observe Testcontainers-managed containers.

## Context

Open `src/test/java/com/megachollos/kata/Kata2_ContainerDebugTest.java`. It has 3 exercises, each with a broken container configuration. Fix them one by one.

## Tasks

### Exercise A — Wrong image tag

Run the test `exerciseA_wrongImage`. It fails because the Docker image doesn't exist.

1. Read the error message
2. Go to https://hub.docker.com/_/postgres/tags and find a valid tag
3. Fix the image tag in the test
4. Run it again — it should pass

### Exercise B — Wrong credentials

Run the test `exerciseB_wrongCredentials`. The container starts but the test can't connect.

1. Read the error — what credentials is it using?
2. The container is configured with `withUsername("admin")` and `withPassword("admin")`, but the test connects with different credentials
3. Fix the `withUsername`, `withPassword`, and `withDatabaseName` so they match
4. Run it again — it should pass

### Exercise C — Observe the container

This exercise lets you see what Testcontainers does behind the scenes.

1. In the test `exerciseC_observeContainer`, there is a `Thread.sleep(60000)` — this keeps the test alive for 60 seconds
2. Run the test
3. While it's running, open **another terminal** and run:
   ```bash
   docker ps
   ```
   You should see:
   - A PostgreSQL container with a **random port** mapped to 5432
   - A `testcontainers/ryuk` container (Testcontainers' cleanup daemon)

4. Connect to the PostgreSQL container:
   ```bash
   docker exec -it <CONTAINER_ID> psql -U test -d test
   ```
   Run:
   ```sql
   SELECT 1;
   \q
   ```

5. Wait for the test to finish (or stop it). Run `docker ps` again — the containers are gone. Testcontainers cleaned them up.

6. **Remove the `Thread.sleep`** after you're done.

### Bonus — Container logs

Add this line inside any test method:
```java
System.out.println(postgres.getLogs());
```

Run the test and look for the line `database system is ready to accept connections` in the output.

## Checklist

- [ ] Exercise A: Fixed the image tag
- [ ] Exercise B: Fixed the credentials
- [ ] Exercise C: Observed a running container with `docker ps` and connected with `psql`
- [ ] (Bonus) Printed and read the container logs
