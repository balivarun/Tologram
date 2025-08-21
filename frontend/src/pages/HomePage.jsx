import { useState } from 'react'
import { useQuery } from '@tanstack/react-query'
import { postService } from '../services/postService'
import PostCard from '../components/posts/PostCard'
import LoadingSpinner from '../components/common/LoadingSpinner'

const HomePage = () => {
  const [posts, setPosts] = useState([])
  const [page, setPage] = useState(0)

  const { data, isLoading, error, refetch } = useQuery(
    ['feed', page],
    () => postService.getFeed(page),
    {
      onSuccess: (data) => {
        if (page === 0) {
          setPosts(data.content || [])
        } else {
          setPosts(prev => [...prev, ...(data.content || [])])
        }
      },
    }
  )

  const loadMore = () => {
    if (data && !data.last) {
      setPage(prev => prev + 1)
    }
  }

  if (isLoading && page === 0) {
    return <LoadingSpinner />
  }

  if (error) {
    return (
      <div className="text-center py-8">
        <p className="text-gray-500 mb-4">Something went wrong</p>
        <button onClick={() => refetch()} className="btn-primary">
          Try Again
        </button>
      </div>
    )
  }

  if (posts.length === 0 && !isLoading) {
    return (
      <div className="text-center py-16">
        <h2 className="text-2xl font-semibold text-gray-700 mb-4">Welcome to Tologram!</h2>
        <p className="text-gray-500 mb-6">Your feed is empty. Start by following some users or creating your first post.</p>
        <div className="space-x-4">
          <button className="btn-primary">Explore Users</button>
          <button className="btn-outline">Create Post</button>
        </div>
      </div>
    )
  }

  return (
    <div className="max-w-lg mx-auto">
      <div className="space-y-6">
        {posts.map((post) => (
          <PostCard key={post.id} post={post} />
        ))}
      </div>

      {data && !data.last && (
        <div className="text-center mt-8">
          <button
            onClick={loadMore}
            disabled={isLoading}
            className="btn-outline disabled:opacity-50"
          >
            {isLoading ? 'Loading...' : 'Load More'}
          </button>
        </div>
      )}
    </div>
  )
}

export default HomePage