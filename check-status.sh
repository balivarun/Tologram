#!/bin/bash

echo "🔍 Checking Tologram Application Status..."
echo ""

# Check if backend is running
if lsof -Pi :8080 -sTCP:LISTEN -t >/dev/null ; then
    echo "✅ Backend is running on port 8080"
    echo "   📄 API: http://localhost:8080/api"
    echo "   📚 Docs: http://localhost:8080/swagger-ui.html"
else
    echo "❌ Backend is not running on port 8080"
fi

echo ""

# Check if frontend is running
FRONTEND_PORT=""
for port in 3000 3001 3002 3003 5173; do
    if lsof -Pi :$port -sTCP:LISTEN -t >/dev/null ; then
        FRONTEND_PORT=$port
        break
    fi
done

if [ -n "$FRONTEND_PORT" ]; then
    echo "✅ Frontend is running on port $FRONTEND_PORT"
    echo "   📱 App: http://localhost:$FRONTEND_PORT"
else
    echo "❌ Frontend is not running"
fi

echo ""

# Check MongoDB connection (if backend is running)
if lsof -Pi :8080 -sTCP:LISTEN -t >/dev/null ; then
    echo "🔍 Testing MongoDB connection..."
    response=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/actuator/health 2>/dev/null)
    if [ "$response" = "200" ]; then
        echo "✅ MongoDB connection is healthy"
    else
        echo "⚠️  MongoDB connection status unknown (check backend logs)"
    fi
else
    echo "⚠️  Cannot check MongoDB (backend not running)"
fi

echo ""

# Check log files
if [ -f "backend.log" ]; then
    echo "📄 Backend log available: backend.log"
else
    echo "⚠️  Backend log not found"
fi

if [ -f "frontend.log" ]; then
    echo "📄 Frontend log available: frontend.log"
else
    echo "⚠️  Frontend log not found"
fi

echo ""
echo "💡 Commands:"
echo "   Start: ./start-dev.sh"
echo "   Stop:  ./stop-dev.sh"
echo "   Logs:  tail -f backend.log  or  tail -f frontend.log"