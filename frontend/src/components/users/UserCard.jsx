import { useState } from 'react'
import { Link } from 'react-router-dom'
import { toast } from 'react-hot-toast'
import { userService } from '../../services/userService'
import { useAuth } from '../../context/AuthContext'

const UserCard = ({ user }) => {
  const [isFollowing, setIsFollowing] = useState(user.isFollowing || false)
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

  const isOwnProfile = currentUser?.id === user.id

  return (
    <div className="card">
      <div className="flex items-center justify-between">
        <div className="flex items-center space-x-4">
          <Link to={`/profile/${user.id}`}>
            <img
              src={user.profilePictureUrl || `https://ui-avatars.com/api/?name=${user.username}&background=6366f1&color=fff`}
              alt={user.username}
              className="w-12 h-12 rounded-full"
            />
          </Link>
          <div>
            <Link
              to={`/profile/${user.id}`}
              className="font-semibold hover:text-primary-500 transition-colors"
            >
              {user.username}
            </Link>
            <p className="text-sm text-gray-500">{user.bio || 'No bio yet'}</p>
            <div className="flex items-center space-x-4 text-sm text-gray-500 mt-1">
              <span>{user.followersCount || 0} followers</span>
              <span>{user.followingCount || 0} following</span>
            </div>
          </div>
        </div>
        
        {!isOwnProfile && (
          <button
            onClick={handleFollowToggle}
            disabled={loading}
            className={`px-4 py-2 rounded-lg font-medium text-sm transition-colors disabled:opacity-50 ${
              isFollowing
                ? 'bg-gray-200 text-gray-700 hover:bg-gray-300'
                : 'btn-primary'
            }`}
          >
            {loading ? 'Loading...' : isFollowing ? 'Following' : 'Follow'}
          </button>
        )}
      </div>
    </div>
  )
}

export default UserCard