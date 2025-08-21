import api from './api'

export const userService = {
  async getProfile(userId) {
    const response = await api.get(`/users/${userId}`)
    return response.data
  },

  async updateProfile(userData) {
    const response = await api.put('/users/profile', userData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    })
    return response.data
  },

  async followUser(userId) {
    const response = await api.post(`/users/${userId}/follow`)
    return response.data
  },

  async unfollowUser(userId) {
    const response = await api.delete(`/users/${userId}/follow`)
    return response.data
  },

  async searchUsers(query) {
    const response = await api.get(`/users/search?q=${encodeURIComponent(query)}`)
    return response.data
  },

  async getUserPosts(userId, page = 0, size = 10) {
    const response = await api.get(`/users/${userId}/posts?page=${page}&size=${size}`)
    return response.data
  },
}