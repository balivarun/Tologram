# Tologram Installation Guide

## Quick Setup

### Prerequisites
- Node.js v16 or higher
- Java 17 or higher
- MongoDB Atlas account (already configured)

### Installation Steps

1. **Install Frontend Dependencies**
   ```bash
   cd frontend
   npm install
   ```

2. **Install Backend Dependencies**
   ```bash
   cd backend
   ./gradlew build
   ```

3. **Start the Application**
   ```bash
   # From the root directory
   ./start-dev.sh
   ```

### Troubleshooting

#### Tailwind CSS Errors
If you see PostCSS/Tailwind errors, make sure:
- Dependencies are installed: `npm install`
- Tailwind config is correct (already fixed)

#### React Query Errors
If you see React Query import errors:
- Make sure to use `@tanstack/react-query` (already updated)
- Run `npm install` to update dependencies

#### Port Already in Use
If ports 3000 or 8080 are in use:
```bash
./stop-dev.sh  # Stop any running instances
./start-dev.sh # Start again
```

### Manual Start (Alternative)

If the scripts don't work, start manually:

**Frontend:**
```bash
cd frontend
npm install
npm run dev
```

**Backend:**
```bash
cd backend
./gradlew bootRun
```

### Verify Installation

1. Frontend: http://localhost:3000
2. Backend API: http://localhost:8080/api
3. API Docs: http://localhost:8080/swagger-ui.html

### Common Issues Fixed

✅ Tailwind CSS `border-border` class error
✅ React Query package update
✅ ESLint configuration
✅ MongoDB Atlas connection
✅ Missing dependencies