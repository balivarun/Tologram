import { useState } from 'react'
import { Search } from 'lucide-react'
import { useQuery } from '@tanstack/react-query'
import { userService } from '../services/userService'
import UserCard from '../components/users/UserCard'

const ExplorePage = () => {
  const [searchQuery, setSearchQuery] = useState('')
  const [searchResults, setSearchResults] = useState([])

  const { isLoading } = useQuery(
    ['searchUsers', searchQuery],
    () => userService.searchUsers(searchQuery),
    {
      enabled: searchQuery.length > 2,
      onSuccess: (data) => {
        setSearchResults(data || [])
      },
    }
  )

  return (
    <div className="max-w-2xl mx-auto">
      <div className="mb-8">
        <h1 className="text-2xl font-bold mb-6">Explore</h1>
        
        <div className="relative">
          <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-5 w-5 text-gray-400" />
          <input
            type="text"
            placeholder="Search users..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="input-field pl-10"
          />
        </div>
      </div>

      {searchQuery.length > 2 && (
        <div>
          <h2 className="text-lg font-semibold mb-4">Search Results</h2>
          
          {isLoading ? (
            <div className="text-center py-8">
              <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary-500 mx-auto"></div>
            </div>
          ) : searchResults.length > 0 ? (
            <div className="space-y-4">
              {searchResults.map((user) => (
                <UserCard key={user.id} user={user} />
              ))}
            </div>
          ) : (
            <div className="text-center py-8">
              <p className="text-gray-500">No users found matching "{searchQuery}"</p>
            </div>
          )}
        </div>
      )}

      {searchQuery.length <= 2 && (
        <div className="text-center py-16">
          <Search className="h-16 w-16 text-gray-300 mx-auto mb-4" />
          <h2 className="text-xl font-semibold text-gray-700 mb-2">Discover People</h2>
          <p className="text-gray-500">Search for users to follow and discover new content</p>
        </div>
      )}
    </div>
  )
}

export default ExplorePage