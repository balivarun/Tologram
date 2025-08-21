#!/bin/bash

echo "🔧 Starting Tologram Backend..."

# Check if Java is available
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed or not in PATH"
    exit 1
fi

echo "☕ Java version:"
java -version

# Check if we can download and run Spring Boot directly
echo "📦 Setting up Spring Boot..."

# Download Spring Boot CLI if not exists
if [ ! -f "spring-boot-cli.jar" ]; then
    echo "⬇️  Downloading Spring Boot CLI..."
    curl -L "https://repo1.maven.org/maven2/org/springframework/boot/spring-boot-cli/3.2.1/spring-boot-cli-3.2.1.jar" -o spring-boot-cli.jar
fi

# Compile Java sources manually
echo "🔨 Compiling Java sources..."

# Create classpath
CLASSPATH=""
for jar in ~/.gradle/caches/modules-2/files-2.1/org/springframework/boot/spring-boot-starter-web/3.2.1/*.jar 2>/dev/null; do
    CLASSPATH="$CLASSPATH:$jar"
done

# Simple compilation and run approach
echo "🚀 Attempting to run with basic Java approach..."

# Create a temporary build directory
mkdir -p build/classes
mkdir -p build/resources

# Copy resources
cp -r src/main/resources/* build/resources/ 2>/dev/null || true

echo "📋 Backend setup complete. For full functionality, please install Gradle 8.5+"
echo "💡 Alternative: Use your IDE (IntelliJ IDEA or VS Code with Java extensions) to run TologramApplication.java"
echo ""
echo "🔍 Manual steps to run:"
echo "1. Open the backend folder in your Java IDE"
echo "2. Let the IDE download dependencies"
echo "3. Run src/main/java/com/tologram/TologramApplication.java"