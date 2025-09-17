#!/bin/bash

# Script to run the application in different standalone Docker configurations

echo "🚀 SEM Methods - Standalone Docker Runner"
echo "========================================"

# Function to show usage
show_usage() {
    echo "Usage: $0 [option]"
    echo ""
    echo "Options:"
    echo "  1 | compose     - Run with docker-compose (recommended)"
    echo "  2 | network     - Run standalone with custom network"
    echo "  3 | localhost   - Run standalone connecting to localhost MongoDB"
    echo "  clean          - Clean up all containers and networks"
    echo "  help           - Show this help message"
    echo ""
}

# Function to clean up
cleanup() {
    echo "🧹 Cleaning up containers and networks..."
    docker-compose down 2>/dev/null || true
    docker stop mongo-standalone 2>/dev/null || true
    docker rm mongo-standalone 2>/dev/null || true
    docker stop sem_app_standalone 2>/dev/null || true
    docker rm sem_app_standalone 2>/dev/null || true
    docker stop sem_app_localhost 2>/dev/null || true
    docker rm sem_app_localhost 2>/dev/null || true
    docker network rm se-methods-network 2>/dev/null || true
    echo "✅ Cleanup complete"
}

# Option 1: Docker Compose (Recommended)
run_compose() {
    echo "📦 Running with Docker Compose..."
    docker-compose up -d
    echo "✅ Containers started. View logs with: docker logs sem_app"
}

# Option 2: Custom Network
run_network() {
    echo "🌐 Setting up custom network..."
    
    # Create network if it doesn't exist
    docker network create --driver bridge se-methods-network 2>/dev/null || true
    
    # Start MongoDB
    echo "🍃 Starting MongoDB..."
    docker run -d --name mongo-standalone --network se-methods-network -p 27001:27017 mongo:latest
    
    # Wait a moment for MongoDB to start
    echo "⏳ Waiting for MongoDB to start..."
    sleep 3
    
    # Start application
    echo "🚀 Starting application..."
    docker run --network se-methods-network --name sem_app_standalone -e MONGO_HOST=mongo-standalone sem_app:latest
    
    echo "✅ Application completed. MongoDB is still running on port 27001"
}

# Option 3: Localhost connection
run_localhost() {
    echo "🏠 Running with localhost connection..."
    
    # Check if MongoDB is running on host
    if ! docker ps | grep -q mongo-standalone; then
        echo "🍃 Starting MongoDB on host..."
        docker run -d --name mongo-standalone -p 27001:27017 mongo:latest
        echo "⏳ Waiting for MongoDB to start..."
        sleep 3
    fi
    
    # Start application connecting to host MongoDB
    echo "🚀 Starting application..."
    docker run --name sem_app_localhost -e MONGO_HOST=host.docker.internal -e MONGO_PORT=27001 sem_app:latest
    
    echo "✅ Application completed. MongoDB is still running on port 27001"
}

# Main script logic
case "${1:-help}" in
    1|compose)
        cleanup
        run_compose
        ;;
    2|network)
        cleanup
        run_network
        ;;
    3|localhost)
        cleanup
        run_localhost
        ;;
    clean)
        cleanup
        ;;
    help|*)
        show_usage
        ;;
esac
