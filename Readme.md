# Software Engineering Methods

* Master Build Status ![GitHub Workflow Status (branch)](https://img.shields.io/github/actions/workflow/status/YOUR_USERNAME/seMethods/main.yml?branch=master)
* Develop Branch Status ![GitHub Workflow Status (branch)](https://img.shields.io/github/actions/workflow/status/YOUR_USERNAME/seMethods/main.yml?branch=develop)
* License [![LICENSE](https://img.shields.io/github/license/YOUR_USERNAME/seMethods.svg?style=flat-square)](https://github.com/YOUR_USERNAME/seMethods/blob/master/LICENSE)
* Release [![Releases](https://img.shields.io/github/release/YOUR_USERNAME/seMethods/all.svg?style=flat-square)](https://github.com/YOUR_USERNAME/seMethods/releases)

This is a simple Java application that runs inside a Docker container with continuous integration setup using GitHub Actions.

---

## 📦 Project Structure
├── .github/workflows/main.yml
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── src/main/java/com/napier/sem/App.java


## 🚀 Getting Started

### 1. Prerequisites
Make sure you have installed:
- [Docker](https://docs.docker.com/get-docker/)
- [Docker Compose](https://docs.docker.com/compose/)
- [Java 11](https://adoptium.net/)
- [Maven](https://maven.apache.org/)

Verify installation:
```bash
docker --version
docker compose version
java --version
mvn --version
```

## 🏃‍♂️ Running the Application

### Method 1: Docker Compose (Recommended)
```bash
docker-compose up -d    # Start both app and MongoDB
docker logs sem_app     # View application logs
docker-compose down     # Stop all containers
```

### Method 2: Standalone Docker with Custom Network
```bash
# Create network and start MongoDB
docker network create --driver bridge se-methods-network
docker run -d --name mongo-standalone --network se-methods-network -p 27001:27017 mongo:latest

# Run application on same network
docker run --network se-methods-network --name sem_app_standalone -e MONGO_HOST=mongo-standalone sem_app:latest
```

### Method 3: Standalone Docker with Localhost Connection
```bash
# Start MongoDB with port mapping
docker run -d --name mongo-standalone -p 27001:27017 mongo:latest

# Run application connecting to host
docker run --name sem_app_localhost -e MONGO_HOST=host.docker.internal -e MONGO_PORT=27001 sem_app:latest
```

### Method 4: Using the Helper Script
```bash
./run-standalone.sh 1    # Docker compose
./run-standalone.sh 2    # Custom network
./run-standalone.sh 3    # Localhost connection
./run-standalone.sh clean # Clean up everything
```

## 🔧 Environment Variables

The application supports these environment variables:

- `MONGO_HOST`: MongoDB hostname (default: `mongo-dbserver`)
- `MONGO_PORT`: MongoDB port (default: `27017`)