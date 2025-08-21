# Tologram - Social Media Application

## Project Overview
Tologram is a social media application similar to Instagram with a custom theme. Built with React frontend, Spring Boot backend, and MongoDB database.

## Architecture

### Frontend (React)
- **Framework**: React 18+
- **Styling**: Tailwind CSS with custom theme
- **State Management**: React Context API or Redux Toolkit
- **Routing**: React Router DOM
- **HTTP Client**: Axios
- **Build Tool**: Vite

### Backend (Spring Boot)
- **Framework**: Spring Boot 3.x
- **Security**: Spring Security with JWT
- **Database**: Spring Data MongoDB
- **File Upload**: Multipart file handling
- **API**: RESTful endpoints

### Database (MongoDB)
- **Collections**: Users, Posts, Comments, Notifications
- **Features**: Indexing, aggregation pipelines
- **File Storage**: GridFS or cloud storage

## Custom Theme Colors
```css
:root {
  --primary: #6366f1;     /* Indigo */
  --secondary: #8b5cf6;   /* Violet */
  --accent: #f59e0b;      /* Amber */
  --background: #f8fafc;  /* Slate 50 */
  --surface: #ffffff;     /* White */
  --text-primary: #1e293b; /* Slate 800 */
  --text-secondary: #64748b; /* Slate 500 */
}
```

## Core Features

### Authentication
- User registration/login
- JWT token-based auth
- Password reset functionality
- Protected routes

### User Management
- Profile creation/editing
- Follow/Unfollow users
- User search
- Profile picture upload

### Posts
- Photo/video upload
- Caption and hashtags
- Like/Unlike posts
- Post deletion/editing

### Feed
- Personalized timeline
- Infinite scrolling
- Post interactions
- Real-time updates

### Comments
- Add/view/delete comments
- Nested comment threads
- Comment notifications

### Search
- User search by username
- Post search by hashtags
- Content discovery

## Project Structure

```
tologram/
├── frontend/                 # React application
│   ├── public/
│   ├── src/
│   │   ├── components/       # Reusable UI components
│   │   │   ├── common/       # Generic components
│   │   │   ├── auth/         # Authentication components
│   │   │   ├── posts/        # Post-related components
│   │   │   └── users/        # User-related components
│   │   ├── pages/            # Page components
│   │   ├── hooks/            # Custom React hooks
│   │   ├── services/         # API service functions
│   │   ├── context/          # React Context providers
│   │   ├── utils/            # Utility functions
│   │   └── styles/           # Global styles and themes
│   ├── package.json
│   └── tailwind.config.js
├── backend/                  # Spring Boot application
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── com/tologram/
│   │       │       ├── config/       # Configuration classes
│   │       │       ├── controller/   # REST controllers
│   │       │       ├── service/      # Business logic
│   │       │       ├── repository/   # Data access layer
│   │       │       ├── model/        # Entity models
│   │       │       ├── dto/          # Data Transfer Objects
│   │       │       ├── security/     # Security configuration
│   │       │       └── exception/    # Exception handling
│   │       └── resources/
│   │           ├── application.yml
│   │           └── static/
│   ├── build.gradle
│   └── gradle/
└── docs/                     # Documentation
    ├── api/                  # API documentation
    └── setup/                # Setup instructions
```

## API Endpoints

### Authentication
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login
- `POST /api/auth/refresh` - Refresh JWT token
- `POST /api/auth/logout` - User logout

### Users
- `GET /api/users/profile` - Get current user profile
- `PUT /api/users/profile` - Update user profile
- `GET /api/users/{userId}` - Get user by ID
- `POST /api/users/{userId}/follow` - Follow user
- `DELETE /api/users/{userId}/follow` - Unfollow user
- `GET /api/users/search?q={query}` - Search users

### Posts
- `GET /api/posts/feed` - Get personalized feed
- `POST /api/posts` - Create new post
- `GET /api/posts/{postId}` - Get post by ID
- `PUT /api/posts/{postId}` - Update post
- `DELETE /api/posts/{postId}` - Delete post
- `POST /api/posts/{postId}/like` - Like post
- `DELETE /api/posts/{postId}/like` - Unlike post

### Comments
- `GET /api/posts/{postId}/comments` - Get post comments
- `POST /api/posts/{postId}/comments` - Add comment
- `PUT /api/comments/{commentId}` - Update comment
- `DELETE /api/comments/{commentId}` - Delete comment

## Database Schema

### Users Collection
```javascript
{
  _id: ObjectId,
  username: String (unique, indexed),
  email: String (unique, indexed),
  passwordHash: String,
  profilePictureUrl: String,
  bio: String,
  followers: [ObjectId], // User IDs
  following: [ObjectId], // User IDs
  createdAt: Date,
  updatedAt: Date
}
```

### Posts Collection
```javascript
{
  _id: ObjectId,
  userId: ObjectId (indexed),
  imageUrl: String,
  videoUrl: String,
  caption: String,
  hashtags: [String] (indexed),
  likes: [ObjectId], // User IDs
  commentsCount: Number,
  createdAt: Date (indexed),
  updatedAt: Date
}
```

### Comments Collection
```javascript
{
  _id: ObjectId,
  postId: ObjectId (indexed),
  userId: ObjectId,
  text: String,
  parentCommentId: ObjectId, // For nested comments
  createdAt: Date,
  updatedAt: Date
}
```

### Notifications Collection
```javascript
{
  _id: ObjectId,
  recipientUserId: ObjectId (indexed),
  senderUserId: ObjectId,
  type: String, // 'like', 'comment', 'follow'
  postId: ObjectId, // Optional
  readStatus: Boolean,
  createdAt: Date (indexed)
}
```

## Development Commands

### Frontend
```bash
cd frontend
npm install
npm run dev          # Development server
npm run build        # Production build
npm run test         # Run tests
npm run lint         # Lint code
```

### Backend
```bash
cd backend
./gradlew bootRun    # Development server
./gradlew build      # Build application
./gradlew test       # Run tests
./gradlew clean      # Clean build
```

## Environment Variables

### Frontend (.env)
```
VITE_API_BASE_URL=http://localhost:8080/api
VITE_UPLOAD_MAX_SIZE=10485760
```

### Backend (application.yml)
```yaml
spring:
  data:
    mongodb:
      uri: ${MONGODB_URI:mongodb://localhost:27017/tologram}
  servlet:
    multipart:
      max-file-size: 10MB
      max-request-size: 10MB

jwt:
  secret: ${JWT_SECRET:your-secret-key}
  expiration: 86400000 # 24 hours

file:
  upload:
    dir: ${UPLOAD_DIR:./uploads}
```

## Next Steps
1. Set up project structure
2. Configure development environment
3. Implement authentication system
4. Build core UI components
5. Create API endpoints
6. Integrate frontend with backend
7. Add real-time features
8. Implement file upload
9. Add search functionality
10. Deploy application