# Tologram - Social Media Application

A modern social media platform built with React, Spring Boot, and MongoDB. Share your moments with friends and family in a beautiful, Instagram-inspired interface with a custom theme.

## Features

- 🔐 **User Authentication** - Secure registration and login with JWT tokens
- 👤 **User Profiles** - Create and customize your profile with photos and bio
- 📸 **Photo Sharing** - Upload and share photos with captions and hashtags
- 💗 **Social Interactions** - Like, comment, and engage with posts
- 👥 **Follow System** - Follow users and see their posts in your feed
- 🔍 **Search** - Discover new users and content
- 📱 **Responsive Design** - Beautiful UI that works on all devices
- 🎨 **Custom Theme** - Unique color scheme with indigo, violet, and amber accents

## Technology Stack

### Frontend
- **React 18** - Modern React with hooks and functional components
- **Tailwind CSS** - Utility-first CSS framework with custom theme
- **React Router** - Client-side routing
- **React Query** - Server state management
- **Axios** - HTTP client for API calls
- **React Hook Form** - Form handling and validation
- **Vite** - Fast development build tool

### Backend
- **Spring Boot 3** - Java framework for REST APIs
- **Spring Security** - Authentication and authorization with JWT
- **Spring Data MongoDB** - Database operations
- **MongoDB** - NoSQL database for flexibility and scalability
- **Gradle** - Build automation and dependency management

### Infrastructure
- **JWT Authentication** - Stateless authentication
- **File Upload** - Image and media handling
- **CORS** - Cross-origin resource sharing
- **API Documentation** - Swagger/OpenAPI integration

## Getting Started

### Prerequisites
- **Node.js** (v16 or higher)
- **Java** (JDK 17 or higher)
- **MongoDB** (v5.0 or higher)
- **npm** or **yarn**

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd Tologram
   ```

2. **Setup Frontend**
   ```bash
   cd frontend
   npm install
   ```

3. **Setup Backend**
   ```bash
   cd backend
   ./gradlew build
   ```

4. **Configure Environment Variables**

   Frontend (.env):
   ```
   VITE_API_BASE_URL=http://localhost:8080/api
   VITE_UPLOAD_MAX_SIZE=10485760
   VITE_APP_NAME=Tologram
   ```

   Backend (application.yml or environment):
   ```
   MONGODB_URI=mongodb://localhost:27017/tologram
   JWT_SECRET=your-secret-key-here
   UPLOAD_DIR=./uploads
   ```

5. **Start MongoDB**
   Make sure MongoDB is running on your system.

### Running the Application

**Option 1: Using Scripts (Recommended)**
```bash
# Start everything
./start-dev.sh

# Check status
./check-status.sh

# Stop everything
./stop-dev.sh
```

**Option 2: Manual Start**

1. **Start the Backend**
   ```bash
   cd backend
   ./gradlew bootRun
   ```

2. **Start the Frontend**
   ```bash
   cd frontend
   npm run dev
   ```

3. **Access the Application**
   - Frontend: `http://localhost:3000` (or next available port)
   - Backend API: `http://localhost:8080/api`
   - API documentation: `http://localhost:8080/swagger-ui.html`

## API Endpoints

### Authentication
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login
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

## Project Structure

```
tologram/
├── frontend/                 # React application
│   ├── public/
│   ├── src/
│   │   ├── components/       # Reusable UI components
│   │   ├── pages/            # Page components
│   │   ├── services/         # API service functions
│   │   ├── context/          # React Context providers
│   │   └── styles/           # Global styles and themes
│   └── package.json
├── backend/                  # Spring Boot application
│   ├── src/main/java/com/tologram/
│   │   ├── controller/       # REST controllers
│   │   ├── service/          # Business logic
│   │   ├── repository/       # Data access layer
│   │   ├── model/            # Entity models
│   │   ├── dto/              # Data Transfer Objects
│   │   ├── security/         # Security configuration
│   │   └── exception/        # Exception handling
│   └── build.gradle
├── CLAUDE.md                 # Project documentation
└── README.md
```

## Development

### Frontend Development
```bash
cd frontend
npm run dev          # Start development server
npm run build        # Build for production
npm run lint         # Lint code
npm run test         # Run tests
```

### Backend Development
```bash
cd backend
./gradlew bootRun    # Start development server
./gradlew build      # Build application
./gradlew test       # Run tests
./gradlew clean      # Clean build files
```

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Support

If you have any questions or need help, please open an issue on GitHub or contact the development team.

---

Built with ❤️ using React, Spring Boot, and MongoDB