import { Link } from 'react-router-dom'
import { Heart, MessageCircle } from 'lucide-react'

const PostGrid = ({ posts }) => {
  return (
    <div className="grid grid-cols-3 gap-1 md:gap-4">
      {posts.map((post) => (
        <Link
          key={post.id}
          to={`/post/${post.id}`}
          className="relative aspect-square group overflow-hidden rounded-lg"
        >
          <img
            src={post.imageUrl}
            alt="Post"
            className="w-full h-full object-cover transition-transform group-hover:scale-105"
          />
          <div className="absolute inset-0 bg-black bg-opacity-0 group-hover:bg-opacity-50 transition-opacity flex items-center justify-center">
            <div className="flex items-center space-x-6 text-white opacity-0 group-hover:opacity-100 transition-opacity">
              <div className="flex items-center space-x-1">
                <Heart className="h-6 w-6 fill-current" />
                <span className="font-semibold">{post.likesCount || 0}</span>
              </div>
              <div className="flex items-center space-x-1">
                <MessageCircle className="h-6 w-6 fill-current" />
                <span className="font-semibold">{post.commentsCount || 0}</span>
              </div>
            </div>
          </div>
        </Link>
      ))}
    </div>
  )
}

export default PostGrid