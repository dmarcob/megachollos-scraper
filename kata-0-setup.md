# Kata 0 — Environment Setup

## Part 1 — Install a Docker runtime

### Option A: Local with Rancher Desktop (Windows)

1. **Install WSL2** (Windows only): Open PowerShell as Administrator and run:
   ```
   wsl --install
   ```

2. **Install Rancher Desktop**: Download from https://rancherdesktop.io/ and install.

3. **Configure Rancher Desktop**:
   - Open Rancher Desktop
   - Go to **Preferences > Container Engine**
   - Select **dockerd (moby)** (not containerd)
   - Wait for it to finish starting

4. **Verify Docker works**:
   ```bash
   docker info
   ```
   You should see `Server Version: ...` in the output.

   ```bash
   docker run hello-world
   ```
   You should see "Hello from Docker!"

### Option B: GitHub Codespace (no local install needed)

1. Go to the repository on GitHub
2. Click **Code > Codespaces > Create codespace on main**
3. Wait ~2 minutes for it to build (Java 17, Maven, and Docker are pre-installed)
4. Verify:
   ```bash
   docker info
   docker run hello-world
   ```

---

## Part 2 — Your first PostgreSQL container

Let's run a real PostgreSQL database in a container and connect to it.

1. **Start a PostgreSQL container**:
   ```bash
   docker run --name my-postgres \
     -e POSTGRES_USER=test \
     -e POSTGRES_PASSWORD=test \
     -e POSTGRES_DB=testdb \
     -p 5432:5432 \
     -d postgres:17-alpine
   ```

2. **Verify it's running**:
   ```bash
   docker ps
   ```
   You should see `my-postgres` in the list.

3. **Connect to the database**:
   ```bash
   docker exec -it my-postgres psql -U test -d testdb
   ```

4. **Run some SQL** (you are now inside the PostgreSQL shell):
   ```sql
   CREATE TABLE students (id SERIAL PRIMARY KEY, name VARCHAR(100));
   INSERT INTO students (name) VALUES ('your-name-here');
   SELECT * FROM students;
   \dt
   ```

5. **Exit the PostgreSQL shell**:
   ```
   \q
   ```

6. **Stop and remove the container**:
   ```bash
   docker stop my-postgres
   docker rm my-postgres
   ```

7. **Verify it's gone**:
   ```bash
   docker ps -a
   ```

---

## Part 3 — Clone and verify the project

1. **Clone the repository**:
   ```bash
   git clone <REPO_URL>
   cd megachollos-scraper
   ```

2. **Create your personal branch** (use your name):
   ```bash
   git checkout -b nombre-apellido
   ```
   Example: `git checkout -b <USERNAME>`

3. **Run unit tests** (should take ~10 seconds):
   ```bash
   mvn test
   ```

4. **Run integration tests** (should take ~30 seconds):
   ```bash
   mvn failsafe:integration-test
   ```

5. **Push your branch**:
   ```bash
   git push -u origin nombre-apellido
   ```

---
