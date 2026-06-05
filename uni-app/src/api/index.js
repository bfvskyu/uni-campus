const BASE = 'http://localhost:8080/api'

function request(url, method = 'GET', data = null) {
  return new Promise((resolve, reject) => {
    const header = {}
    if (data) header['Content-Type'] = 'application/json'
    uni.request({
      url,
      method,
      data,
      header,
      timeout: 10000,
      success: (res) => {
        const body = res.data
        if (body.code === 200) resolve(body.data)
        else reject(new Error(body.message || '请求失败'))
      },
      fail: (err) => reject(new Error(err.errMsg || '网络错误'))
    })
  })
}

export const api = {
  recruitment: {
    list: (type) => request(`${BASE}/recruitment${type ? `?type=${type}` : ''}`),
    detail: (id) => request(`${BASE}/recruitment/${id}`),
    add: (data) => request(`${BASE}/recruitment`, 'POST', data),
    update: (id, data) => request(`${BASE}/recruitment/${id}`, 'PUT', data),
    delete: (id) => request(`${BASE}/recruitment/${id}`, 'DELETE')
  },
  announcement: {
    list: () => request(`${BASE}/announcement`),
    detail: (id) => request(`${BASE}/announcement/${id}`),
    add: (data) => request(`${BASE}/announcement`, 'POST', data),
    update: (id, data) => request(`${BASE}/announcement/${id}`, 'PUT', data),
    delete: (id) => request(`${BASE}/announcement/${id}`, 'DELETE')
  },
  classroom: {
    buildings: () => request(`${BASE}/classroom/buildings`),
    rooms: (building) => request(`${BASE}/classroom?building=${building}`),
    schedule: () => request(`${BASE}/classroom/schedule`)
  },
  exam: {
    list: (type) => request(`${BASE}/exam${type ? `?type=${type}` : ''}`),
    detail: (id) => request(`${BASE}/exam/${id}`),
    add: (data) => request(`${BASE}/exam`, 'POST', data),
    update: (id, data) => request(`${BASE}/exam/${id}`, 'PUT', data),
    delete: (id) => request(`${BASE}/exam/${id}`, 'DELETE')
  },
  user: {
    login: (studentId, password) => request(`${BASE}/user/login`, 'POST', { studentId, password }),
    changePassword: (data) => request(`${BASE}/user/password`, 'PUT', data),
    verifyPhone: (studentId, phone) => request(`${BASE}/user/verify-phone`, 'POST', { studentId, phone }),
    profile: (studentId) => request(`${BASE}/user/profile${studentId ? `?studentId=${studentId}` : ''}`),
    update: (data) => request(`${BASE}/user/profile`, 'PUT', data),
    uploadAvatar: (file, studentId) => uploadFile(`${BASE}/user/avatar${studentId ? `?studentId=${studentId}` : ''}`, file)
  },
  memo: {
    list: (studentId) => request(`${BASE}/memo/list?studentId=${studentId}`),
    check: (refType, refId, studentId) => request(`${BASE}/memo/check?refType=${refType}&refId=${refId}&studentId=${studentId}`),
    add: (data) => request(`${BASE}/memo`, 'POST', data),
    remove: (id) => request(`${BASE}/memo/${id}`, 'DELETE'),
    batchDelete: (ids) => request(`${BASE}/memo/batch-delete`, 'POST', { ids })
  },
  admin: {
    checkRole: (studentId) => request(`${BASE}/admin/check-role`, 'POST', { studentId })
  },
  search: (keyword) => request(`${BASE}/search?keyword=${encodeURIComponent(keyword)}`),
  feedback: {
    submit: (data) => request(`${BASE}/feedback`, 'POST', data),
    list: () => request(`${BASE}/feedback`),
    toggleStatus: (id) => request(`${BASE}/feedback/${id}/status`, 'PUT')
  },
  lostFound: {
    list: (type, page = 1, size = 10) => request(`${BASE}/lost-found?page=${page}&size=${size}${type ? `&type=${type}` : ''}`),
    detail: (id) => request(`${BASE}/lost-found/${id}`),
    add: (data) => request(`${BASE}/lost-found`, 'POST', data),
    update: (id, data) => request(`${BASE}/lost-found/${id}`, 'PUT', data),
    delete: (id, studentId) => request(`${BASE}/lost-found/${id}?studentId=${studentId}`, 'DELETE'),
    upload: (file) => uploadFile(`${BASE}/lost-found/upload`, file)
  },
  notification: {
    list: (studentId, page = 1 , size = 10) => request(`${BASE}/notification/list?page=${page}&size=${size}${studentId ? `&studentId=${studentId}` : ``}`),
    unreadCount: (studentId) => request(`${BASE}/notification/unread-count?${studentId ? `&studentId=${studentId}` : ``}`),
    markRead: (id) => request(`${BASE}/notification/read/${id}`,'PUT'),
    markAllRead: (studentId) => request(`${BASE}/notification/read-all`,'PUT')
  },
  post: {
    list: (category, currentStudentId, page = 1, size = 10) => {
      let url = `${BASE}/post/list?page=${page}&size=${size}`
      if (category) url += `&category=${category}`
      if (currentStudentId) url += `&currentStudentId=${currentStudentId}`
      return request(url)
    },
    detail: (id, studentId) => request(`${BASE}/post/${id}${studentId ? `?currentStudentId=${studentId}` : ''}`),
    create: (data, studentId) => request(`${BASE}/post?studentId=${studentId}`, 'POST', data),
    delete: (id, studentId) => request(`${BASE}/post/${id}?studentId=${studentId}`, 'DELETE'),
    togglePin: (id) => request(`${BASE}/post/${id}/pin`, 'PUT'),
    toggleLike: (id, studentId) => request(`${BASE}/post/${id}/like?studentId=${studentId}`, 'POST'),
    toggleFavorite: (id, studentId) => request(`${BASE}/post/${id}/favorite?studentId=${studentId}`, 'POST'),
    myFavorites: (studentId, page = 1, size = 10) => request(`${BASE}/post/favorites?studentId=${studentId}&page=${page}&size=${size}`)
  },
  comment: {
    list: (postId, page = 1, size = 10) => request(`${BASE}/comment/list?postId=${postId}&page=${page}&size=${size}`),
    create: (data, studentId) => request(`${BASE}/comment?studentId=${studentId}`, 'POST', data),
    delete: (id, studentId) => request(`${BASE}/comment/${id}?studentId=${studentId}`, 'DELETE')
  },
  sensitive: {
    check: (text) => request(`${BASE}/sensitive-word/check`, 'POST', { text })
  }
}

function uploadFile(url, file) {
  return new Promise((resolve, reject) => {
    uni.uploadFile({
      url,
      filePath: file,
      name: 'file',
      success: (res) => {
        const data = typeof res.data === 'string' ? JSON.parse(res.data) : res.data
        if (data.code === 200) resolve(data.data)
        else reject(new Error(data.message || '上传失败'))
      },
      fail: (err) => reject(new Error(err.errMsg || '上传失败'))
    })
  })
}
