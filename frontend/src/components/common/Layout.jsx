import { Outlet } from 'react-router-dom'
import Navigation from './Navigation'
import Sidebar from './Sidebar'

const Layout = () => {
  return (
    <div className="min-h-screen bg-background">
      <Navigation />
      <div className="max-w-6xl mx-auto px-4 py-8 flex gap-8">
        <main className="flex-1">
          <Outlet />
        </main>
        <Sidebar />
      </div>
    </div>
  )
}

export default Layout