import { useParams } from 'react-router-dom'
import { useQuery } from '@tanstack/react-query'
import { postService } from '../services/postService'
import PostCard from '../components/posts/PostCard'
import LoadingSpinner from '../components/common/LoadingSpinner'

const PostDetailsPage = () => {
  const { postId } = useParams()

  const { data: post, isLoading, error } = useQuery(
    ['post', postId],
    () => postService.getPost(postId),
    {
      enabled: !!postId,
    }
  )

  if (isLoading) {
    return <LoadingSpinner />
  }

  if (error) {
    return (
      <div className="text-center py-16">
        <h2 className="text-xl font-semibold text-gray-700 mb-2">Post Not Found</h2>
        <p className="text-gray-500">The post you're looking for doesn't exist or has been deleted.</p>
      </div>
    )
  }

  return (
    <div className="max-w-lg mx-auto">
      <PostCard post={post} showComments={true} />
    </div>
  )
}

export default PostDetailsPage