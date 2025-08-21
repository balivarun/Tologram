import api from './api'

export const postService = {
  async getFeed(page = 0, size = 10) {
    const response = await api.get(`/posts/feed?page=${page}&size=${size}`)
    return response.data
  },

  async createPost(postData) {
    const response = await api.post('/posts', postData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    })
    return response.data
  },

  async getPost(postId) {
    const response = await api.get(`/posts/${postId}`)
    return response.data
  },

  async updatePost(postId, postData) {
    const response = await api.put(`/posts/${postId}`, postData)
    return response.data
  },

  async deletePost(postId) {
    const response = await api.delete(`/posts/${postId}`)
    return response.data
  },

  async likePost(postId) {
    const response = await api.post(`/posts/${postId}/like`)
    return response.data
  },

  async unlikePost(postId) {
    const response = await api.delete(`/posts/${postId}/like`)
    return response.data
  },

  async getComments(postId) {
    const response = await api.get(`/posts/${postId}/comments`)
    return response.data
  },

  async addComment(postId, text) {
    const response = await api.post(`/posts/${postId}/comments`, { text })
    return response.data
  },

  async deleteComment(commentId) {
    const response = await api.delete(`/comments/${commentId}`)
    return response.data
  },
}