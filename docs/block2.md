Block 2 — Containers

The problem: everyone has a different environment
DeveloperOSPostgreSQLDev 1MacPostgreSQL 14Dev 2WindowsPostgreSQL 16Dev 3LinuxPostgreSQL 15Dev 4Macnot installed
All pointing to → Production — PostgreSQL 16

bugs appear only here


The solution: Same container everywhere
DeveloperOSContainerDev 1Macpostgres:16Dev 2Windowspostgres:16Dev 3Linuxpostgres:16Dev 4Macpostgres:16
All pointing to → Production — PostgreSQL 16 — identical

What is a container
Docker engine (left side)

docker run
docker stop
docker ps
docker rm
starts / stops / cleans up

PostgreSQL container (right side, top to bottom):

PostgreSQL 16 binaries — the database engine, runs queries
Config layer — postgresql.conf, env vars, passwords
Base OS — Alpine Linux — minimal linux, ~5MB, just enough