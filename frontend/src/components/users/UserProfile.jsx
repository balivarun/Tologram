import { useState } from 'react'
import { toast } from 'react-hot-toast'
import { Settings, Users, Grid } from 'lucide-react'
import { userService } from '../../services/userService'
import { useAuth } from '../../context/AuthContext'

const UserProfile = ({ user, isOwnProfile }) => {
  const [isFollowing, setIsFollowing] = useState(user?.isFollowing || false)
  const [loading, setLoading] = useState(false)
  const { user: currentUser } = useAuth()

  const handleFollowToggle = async () => {
    if (loading) return

    setLoading(true)
    try {
      if (isFollowing) {
        await userService.unfollowUser(user.id)
        setIsFollowing(false)
        toast.success(`Unfollowed ${user.username}`)
      } else {
        await userService.followUser(user.id)
        setIsFollowing(true)
        toast.success(`Following ${user.username}`)
      }
    } catch (error) {
      toast.error('Failed to update follow status')
    } finally {
      setLoading(false)
    }
  }

  if (!user) {
    return (
      <div className="animate-pulse">
        <div className="flex items-center space-x-8 mb-8">
          <div className="w-32 h-32 bg-gray-200 rounded-full"></div>
          <div className="flex-1">
            <div className="h-8 bg-gray-200 rounded w-48 mb-4"></div>
            <div className="h-4 bg-gray-200 rounded w-64 mb-2"></div>
            <div className="h-4 bg-gray-200 rounded w-32"></div>
          </div>
        </div>
      </div>
    )
  }

  return (
    <div className="mb-8">
      <div className="flex flex-col md:flex-row md:items-center md:space-x-8 space-y-6 md:space-y-0">
        <div className="flex justify-center md:justify-start">
          <img
            src={user.profilePictureUrl || `https://ui-avatars.com/api/?name=${user.username}&background=6366f1&color=fff&size=128`}
            alt={user.username}
            className="w-32 h-32 rounded-full"
          />
        </div>
        
        <div className="flex-1 text-center md:text-left">
          <div className="flex flex-col md:flex-row md:items-center md:space-x-4 mb-4">
            <h1 className="text-2xl font-bold mb-2 md:mb-0">{user.username}</h1>
            
            {isOwnProfile ? (
              <button className="btn-outline inline-flex items-center">
                <Settings className="h-4 w-4 mr-2" />
                Edit Profile
              </button>
            ) : (
              <div className="flex space-x-2">
                <button
                  onClick={handleFollowToggle}
                  disabled={loading}
                  className={`px-6 py-2 rounded-lg font-medium transition-colors disabled:opacity-50 ${
                    isFollowing
                      ? 'bg-gray-200 text-gray-700 hover:bg-gray-300'
                      : 'btn-primary'
                  }`}
                >
                  {loading ? 'Loading...' : isFollowing ? 'Following' : 'Follow'}
                </button>
                <button className="btn-outline">Message</button>
              </div>
            )}
          </div>
          
          <div className="flex justify-center md:justify-start space-x-8 mb-4">
            <div className="text-center">
              <span className="font-semibold text-lg">{user.postsCount || 0}</span>
              <p className="text-sm text-gray-500">Posts</p>
            </div>
            <div className="text-center">
              <span className="font-semibold text-lg">{user.followersCount || 0}</span>
              <p className="text-sm text-gray-500">Followers</p>
            </div>
            <div className="text-center">
              <span className="font-semibold text-lg">{user.followingCount || 0}</span>
              <p className="text-sm text-gray-500">Following</p>
            </div>
          </div>
          
          {user.bio && (
            <p className="text-gray-700 max-w-md">{user.bio}</p>
          )}
        </div>
      </div>
    </div>
  )
}

export default UserProfile