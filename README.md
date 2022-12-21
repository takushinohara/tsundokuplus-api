# Tsundokuplus API

Backend API for [Tsundoku+](http://tsundokuplus.com)

## Setup for Development Server

Create .env file:

```bash
cp .env.example .env
```

Launch MySQL using Docker:
```bash
docker compose up -d
```

Run your Spring Boot application with Gradle:

```bash
./gradlew bootRun
```

After the application starts, create a test user with Spring Shell command `create-user` like below:

```bash
shell:>create-user
email: test@example.com
password: ************
name: Test User
```
