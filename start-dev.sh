#!/bin/bash

# Tologram Development Startup Script

echo "🚀 Starting Tologram Development Environment..."

# Function to check if a port is in use
check_port() {
    if lsof -Pi :$1 -sTCP:LISTEN -t >/dev/null ; then
        echo "⚠️  Port $1 is already in use"
        return 1
    else
        return 0
    fi
}

# Function to find available port
find_available_port() {
    local start_port=$1
    local port=$start_port
    while lsof -Pi :$port -sTCP:LISTEN -t >/dev/null 2>&1; do
        port=$((port + 1))
    done
    echo $port
}

# Function to start backend
start_backend() {
    echo "🔧 Starting Backend (Spring Boot)..."
    cd backend
    
    # Check if port 8080 is available
    if check_port 8080; then
        echo "✅ Port 8080 is available"
    else
        echo "❌ Backend port 8080 is already in use. Please stop the existing process."
        return 1
    fi
    
    # Start the backend in background
    nohup ./gradlew bootRun > ../backend.log 2>&1 &
    BACKEND_PID=$!
    echo "Backend started with PID: $BACKEND_PID"
    echo $BACKEND_PID > ../backend.pid
    
    cd ..
}

# Function to start frontend
start_frontend() {
    echo "🎨 Starting Frontend (React + Vite)..."
    cd frontend
    
    # Check if port 3000 is available
    if check_port 3000; then
        echo "✅ Port 3000 is available"
    else
        echo "❌ Frontend port 3000 is already in use. Please stop the existing process."
        return 1
    fi
    
    # Start the frontend in background
    nohup npm run dev > ../frontend.log 2>&1 &
    FRONTEND_PID=$!
    echo "Frontend started with PID: $FRONTEND_PID"
    echo $FRONTEND_PID > ../frontend.pid
    
    cd ..
}

# Create uploads directory if it doesn't exist
mkdir -p uploads

# Start backend and frontend
start_backend
if [ $? -eq 0 ]; then
    echo "⏳ Waiting for backend to initialize..."
    sleep 10
    
    start_frontend
    if [ $? -eq 0 ]; then
        echo ""
        echo "🎉 Tologram is starting up!"
        echo ""
        FRONTEND_PORT=$(find_available_port 3000)
        BACKEND_PORT=$(find_available_port 8080)
        
        echo "📱 Frontend: http://localhost:$FRONTEND_PORT"
        echo "🔧 Backend API: http://localhost:$BACKEND_PORT/api"
        echo "📚 API Documentation: http://localhost:$BACKEND_PORT/swagger-ui.html"
        echo ""
        echo "📄 Logs:"
        echo "   Backend: tail -f backend.log"
        echo "   Frontend: tail -f frontend.log"
        echo ""
        echo "🛑 To stop the servers, run: ./stop-dev.sh"
        echo ""
    else
        echo "❌ Failed to start frontend"
    fi
else
    echo "❌ Failed to start backend"
fi