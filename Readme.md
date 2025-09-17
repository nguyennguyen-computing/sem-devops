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