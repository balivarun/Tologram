import { useParams } from 'react-router-dom'
import { useQuery } from '@tanstack/react-query'
import { userService } from '../services/userService'
import { useAuth } from '../context/AuthContext'
import PostGrid from '../components/posts/PostGrid'
import UserProfile from '../components/users/UserProfile'
import LoadingSpinner from '../components/common/LoadingSpinner'

const ProfilePage = () => {
  const { userId } = useParams()
  const { user: currentUser } = useAuth()
  const isOwnProfile = currentUser?.id === userId

  const { data: user, isLoading: userLoading, error } = useQuery(
    ['user', userId],
    () => userService.getProfile(userId)
  )

  const { data: posts, isLoading: postsLoading } = useQuery(
    ['userPosts', userId],
    () => userService.getUserPosts(userId),
    {
      enabled: !!userId,
    }
  )

  if (userLoading) {
    return <LoadingSpinner />
  }

  if (error) {
    return (
      <div className="text-center py-16">
        <h2 className="text-xl font-semibold text-gray-700 mb-2">User Not Found</h2>
        <p className="text-gray-500">The user you're looking for doesn't exist.</p>
      </div>
    )
  }

  return (
    <div className="max-w-4xl mx-auto">
      <UserProfile user={user} isOwnProfile={isOwnProfile} />
      
      <div className="mt-8">
        <div className="border-t border-gray-200 pt-8">
          <h2 className="text-lg font-semibold mb-6">
            {isOwnProfile ? 'Your Posts' : `${user?.username}'s Posts`}
          </h2>
          
          {postsLoading ? (
            <div className="text-center py-8">
              <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary-500 mx-auto"></div>
            </div>
          ) : posts && posts.content && posts.content.length > 0 ? (
            <PostGrid posts={posts.content} />
          ) : (
            <div className="text-center py-16">
              <p className="text-gray-500 mb-4">
                {isOwnProfile ? "You haven't shared any posts yet" : "No posts yet"}
              </p>
              {isOwnProfile && (
                <button className="btn-primary">Create Your First Post</button>
              )}
            </div>
          )}
        </div>
      </div>
    </div>
  )
}

export default ProfilePage