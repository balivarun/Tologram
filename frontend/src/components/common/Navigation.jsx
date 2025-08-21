import { Link, useNavigate, useLocation } from 'react-router-dom'
import { Home, Search, PlusCircle, User, LogOut, Camera } from 'lucide-react'
import { useAuth } from '../../context/AuthContext'

const Navigation = () => {
  const { user, logout } = useAuth()
  const navigate = useNavigate()
  const location = useLocation()

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  const isActive = (path) => location.pathname === path

  return (
    <nav className="bg-surface border-b border-gray-200 sticky top-0 z-50">
      <div className="max-w-6xl mx-auto px-4">
        <div className="flex items-center justify-between h-16">
          <Link to="/" className="flex items-center space-x-2">
            <Camera className="h-8 w-8 text-primary-500" />
            <span className="text-xl font-bold text-primary-500">Tologram</span>
          </Link>

          <div className="flex items-center space-x-6">
            <Link
              to="/"
              className={`p-2 rounded-lg transition-colors ${
                isActive('/') ? 'bg-primary-100 text-primary-700' : 'hover:bg-gray-100'
              }`}
            >
              <Home className="h-6 w-6" />
            </Link>
            
            <Link
              to="/explore"
              className={`p-2 rounded-lg transition-colors ${
                isActive('/explore') ? 'bg-primary-100 text-primary-700' : 'hover:bg-gray-100'
              }`}
            >
              <Search className="h-6 w-6" />
            </Link>
            
            <Link
              to="/create"
              className={`p-2 rounded-lg transition-colors ${
                isActive('/create') ? 'bg-primary-100 text-primary-700' : 'hover:bg-gray-100'
              }`}
            >
              <PlusCircle className="h-6 w-6" />
            </Link>
            
            <Link
              to={`/profile/${user?.id}`}
              className={`p-2 rounded-lg transition-colors ${
                isActive(`/profile/${user?.id}`) ? 'bg-primary-100 text-primary-700' : 'hover:bg-gray-100'
              }`}
            >
              <User className="h-6 w-6" />
            </Link>
            
            <button
              onClick={handleLogout}
              className="p-2 rounded-lg hover:bg-gray-100 transition-colors"
            >
              <LogOut className="h-6 w-6" />
            </button>
          </div>
        </div>
      </div>
    </nav>
  )
}

export default Navigation