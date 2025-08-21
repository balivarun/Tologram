import { useState } from 'react'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { Link } from 'react-router-dom'
import { formatDistanceToNow } from 'date-fns'
import { toast } from 'react-hot-toast'
import { postService } from '../../services/postService'
import { useAuth } from '../../context/AuthContext'

const CommentSection = ({ postId }) => {
  const [newComment, setNewComment] = useState('')
  const { user } = useAuth()
  const queryClient = useQueryClient()

  const { data: comments, isLoading } = useQuery(
    ['comments', postId],
    () => postService.getComments(postId),
    {
      enabled: !!postId,
    }
  )

  const addCommentMutation = useMutation(
    (text) => postService.addComment(postId, text),
    {
      onSuccess: () => {
        queryClient.invalidateQueries(['comments', postId])
        setNewComment('')
        toast.success('Comment added!')
      },
      onError: () => {
        toast.error('Failed to add comment')
      },
    }
  )

  const handleSubmit = (e) => {
    e.preventDefault()
    if (newComment.trim()) {
      addCommentMutation.mutate(newComment.trim())
    }
  }

  if (isLoading) {
    return (
      <div className="border-t pt-4">
        <div className="animate-pulse space-y-3">
          <div className="h-4 bg-gray-200 rounded w-3/4"></div>
          <div className="h-4 bg-gray-200 rounded w-1/2"></div>
        </div>
      </div>
    )
  }

  return (
    <div className="border-t pt-4">
      <div className="space-y-3 mb-4">
        {comments && comments.length > 0 ? (
          comments.map((comment) => (
            <div key={comment.id} className="flex items-start space-x-3">
              <Link to={`/profile/${comment.user.id}`}>
                <img
                  src={comment.user.profilePictureUrl || `https://ui-avatars.com/api/?name=${comment.user.username}&background=6366f1&color=fff`}
                  alt={comment.user.username}
                  className="w-8 h-8 rounded-full"
                />
              </Link>
              <div className="flex-1">
                <p className="text-sm">
                  <Link
                    to={`/profile/${comment.user.id}`}
                    className="font-semibold mr-2 hover:text-primary-500 transition-colors"
                  >
                    {comment.user.username}
                  </Link>
                  {comment.text}
                </p>
                <p className="text-xs text-gray-500 mt-1">
                  {formatDistanceToNow(new Date(comment.createdAt), { addSuffix: true })}
                </p>
              </div>
            </div>
          ))
        ) : (
          <p className="text-gray-500 text-sm">No comments yet</p>
        )}
      </div>

      <form onSubmit={handleSubmit} className="flex items-center space-x-3">
        <img
          src={user?.profilePictureUrl || `https://ui-avatars.com/api/?name=${user?.username}&background=6366f1&color=fff`}
          alt={user?.username}
          className="w-8 h-8 rounded-full"
        />
        <div className="flex-1">
          <input
            type="text"
            placeholder="Add a comment..."
            value={newComment}
            onChange={(e) => setNewComment(e.target.value)}
            className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-transparent text-sm"
          />
        </div>
        <button
          type="submit"
          disabled={!newComment.trim() || addCommentMutation.isLoading}
          className="text-primary-500 hover:text-primary-600 font-semibold text-sm disabled:opacity-50 disabled:cursor-not-allowed"
        >
          Post
        </button>
      </form>
    </div>
  )
}

export default CommentSection