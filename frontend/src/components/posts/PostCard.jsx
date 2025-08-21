import { useState } from 'react'
import { Link } from 'react-router-dom'
import { Heart, MessageCircle, Share, Bookmark, MoreHorizontal } from 'lucide-react'
import { formatDistanceToNow } from 'date-fns'
import { toast } from 'react-hot-toast'
import { postService } from '../../services/postService'
import { useAuth } from '../../context/AuthContext'
import CommentSection from './CommentSection'

const PostCard = ({ post, showComments = false }) => {
  const [liked, setLiked] = useState(post.liked || false)
  const [likesCount, setLikesCount] = useState(post.likesCount || 0)
  const [showCommentsSection, setShowCommentsSection] = useState(showComments)
  const { user } = useAuth()

  const handleLike = async () => {
    try {
      if (liked) {
        await postService.unlikePost(post.id)
        setLiked(false)
        setLikesCount(prev => prev - 1)
      } else {
        await postService.likePost(post.id)
        setLiked(true)
        setLikesCount(prev => prev + 1)
      }
    } catch (error) {
      toast.error('Failed to update like')
    }
  }

  const toggleComments = () => {
    setShowCommentsSection(!showCommentsSection)
  }

  return (
    <div className="card">
      <div className="flex items-center justify-between mb-4">
        <div className="flex items-center space-x-3">
          <Link to={`/profile/${post.user.id}`}>
            <img
              src={post.user.profilePictureUrl || `https://ui-avatars.com/api/?name=${post.user.username}&background=6366f1&color=fff`}
              alt={post.user.username}
              className="w-10 h-10 rounded-full"
            />
          </Link>
          <div>
            <Link
              to={`/profile/${post.user.id}`}
              className="font-semibold hover:text-primary-500 transition-colors"
            >
              {post.user.username}
            </Link>
            <p className="text-sm text-gray-500">
              {formatDistanceToNow(new Date(post.createdAt), { addSuffix: true })}
            </p>
          </div>
        </div>
        <button className="p-2 hover:bg-gray-100 rounded-full">
          <MoreHorizontal className="h-5 w-5" />
        </button>
      </div>

      <div className="mb-4">
        <img
          src={post.imageUrl}
          alt="Post"
          className="w-full rounded-lg"
        />
      </div>

      <div className="flex items-center justify-between mb-4">
        <div className="flex items-center space-x-4">
          <button
            onClick={handleLike}
            className={`p-2 rounded-full transition-colors ${
              liked ? 'text-red-500' : 'hover:bg-gray-100'
            }`}
          >
            <Heart className={`h-6 w-6 ${liked ? 'fill-current' : ''}`} />
          </button>
          <button
            onClick={toggleComments}
            className="p-2 hover:bg-gray-100 rounded-full"
          >
            <MessageCircle className="h-6 w-6" />
          </button>
          <button className="p-2 hover:bg-gray-100 rounded-full">
            <Share className="h-6 w-6" />
          </button>
        </div>
        <button className="p-2 hover:bg-gray-100 rounded-full">
          <Bookmark className="h-6 w-6" />
        </button>
      </div>

      <div className="mb-4">
        {likesCount > 0 && (
          <p className="font-semibold mb-2">{likesCount} likes</p>
        )}
        {post.caption && (
          <p>
            <Link
              to={`/profile/${post.user.id}`}
              className="font-semibold mr-2"
            >
              {post.user.username}
            </Link>
            {post.caption}
          </p>
        )}
      </div>

      {post.commentsCount > 0 && !showCommentsSection && (
        <button
          onClick={toggleComments}
          className="text-gray-500 text-sm mb-4 hover:text-gray-700 transition-colors"
        >
          View all {post.commentsCount} comments
        </button>
      )}

      {showCommentsSection && (
        <CommentSection postId={post.id} />
      )}
    </div>
  )
}

export default PostCard