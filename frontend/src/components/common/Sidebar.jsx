import { useAuth } from '../../context/AuthContext'
import { Link } from 'react-router-dom'

const Sidebar = () => {
  const { user } = useAuth()

  return (
    <aside className="w-80 hidden lg:block">
      <div className="card mb-6">
        <div className="flex items-center space-x-3 mb-4">
          <img
            src={user?.profilePictureUrl || `https://ui-avatars.com/api/?name=${user?.username}&background=6366f1&color=fff`}
            alt={user?.username}
            className="w-12 h-12 rounded-full"
          />
          <div>
            <h3 className="font-semibold">{user?.username}</h3>
            <p className="text-sm text-gray-500">{user?.bio || 'No bio yet'}</p>
          </div>
        </div>
        <Link
          to={`/profile/${user?.id}`}
          className="text-sm text-primary-500 hover:text-primary-600 transition-colors"
        >
          View Profile
        </Link>
      </div>

      <div className="card">
        <h3 className="font-semibold mb-4">Suggested for you</h3>
        <div className="space-y-3">
          <div className="flex items-center justify-between">
            <div className="flex items-center space-x-3">
              <img
                src="https://ui-avatars.com/api/?name=John+Doe&background=8b5cf6&color=fff"
                alt="John Doe"
                className="w-8 h-8 rounded-full"
              />
              <div>
                <p className="text-sm font-medium">john_doe</p>
                <p className="text-xs text-gray-500">Suggested for you</p>
              </div>
            </div>
            <button className="text-xs text-primary-500 hover:text-primary-600 font-medium">
              Follow
            </button>
          </div>
          
          <div className="flex items-center justify-between">
            <div className="flex items-center space-x-3">
              <img
                src="https://ui-avatars.com/api/?name=Jane+Smith&background=f59e0b&color=fff"
                alt="Jane Smith"
                className="w-8 h-8 rounded-full"
              />
              <div>
                <p className="text-sm font-medium">jane_smith</p>
                <p className="text-xs text-gray-500">Suggested for you</p>
              </div>
            </div>
            <button className="text-xs text-primary-500 hover:text-primary-600 font-medium">
              Follow
            </button>
          </div>
        </div>
      </div>
    </aside>
  )
}

export default Sidebar