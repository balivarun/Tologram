#!/bin/bash

# Tologram Development Stop Script

echo "🛑 Stopping Tologram Development Environment..."

# Function to stop process by PID file
stop_process() {
    local name=$1
    local pid_file=$2
    
    if [ -f "$pid_file" ]; then
        local pid=$(cat "$pid_file")
        if kill -0 "$pid" 2>/dev/null; then
            echo "Stopping $name (PID: $pid)..."
            kill "$pid"
            sleep 2
            
            # Force kill if still running
            if kill -0 "$pid" 2>/dev/null; then
                echo "Force stopping $name..."
                kill -9 "$pid"
            fi
            
            echo "✅ $name stopped"
        else
            echo "⚠️  $name process not found (PID: $pid)"
        fi
        rm -f "$pid_file"
    else
        echo "⚠️  $name PID file not found"
    fi
}

# Stop backend
stop_process "Backend" "backend.pid"

# Stop frontend
stop_process "Frontend" "frontend.pid"

# Also kill any remaining processes on the ports
echo "🧹 Cleaning up any remaining processes..."

# Kill processes on port 8080 (backend)
lsof -ti:8080 | xargs kill -9 2>/dev/null || true

# Kill processes on port 3000 (frontend)
lsof -ti:3000 | xargs kill -9 2>/dev/null || true

echo "✅ All processes stopped"
echo ""
echo "📄 Log files are still available:"
echo "   Backend: backend.log"
echo "   Frontend: frontend.log"