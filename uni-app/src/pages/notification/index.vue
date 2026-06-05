<template>
  <view class="container">
    <!-- 顶部操作栏 -->
    <view class="header">
      <text class="header-title">消息中心</text>
      <text class="header-btn" @click="handleMarkAll" v-if="unreadCount > 0">全部已读</text>
    </view>

    <!-- 列表 -->
    <view class="list" v-if="!loading && list.length">
      <view
        class="card"
        v-for="item in list"
        :key="item.id"
        :class="{ unread: item.isRead === 0 }"
        @click="handleClick(item)"
      >
        <view class="unread-dot" v-if="item.isRead === 0"></view>
        <view class="card-left">
          <text class="card-icon">{{ typeIcon(item.type) }}</text>
        </view>
        <view class="card-body">
          <view class="card-top">
            <text class="card-title" :class="{ bold: item.isRead === 0 }">{{ item.title }}</text>
            <text class="card-time">{{ item.createTime ? item.createTime.slice(0, 16).replace('T', ' ') : '' }}</text>
          </view>
          <text class="card-desc">{{ item.content }}</text>
        </view>
      </view>
    </view>

    <!-- 分页 -->
    <view class="pagination" v-if="totalPages > 1">
      <view class="page-btn" :class="{ disabled: currentPage <= 1 }" @click="goPage(currentPage - 1)">‹</view>
      <view class="page-info">
        <textarea class="page-input" v-model="jumpPage" @confirm="jump" @blur="jump" />
        <text class="page-total">/ {{ totalPages }}</text>
      </view>
      <view class="page-btn" :class="{ disabled: currentPage >= totalPages }" @click="goPage(currentPage + 1)">›</view>
    </view>

    <!-- 空状态 -->
    <view class="empty" v-if="!loading && !list.length">
      <text class="empty-icon">🔔</text>
      <text class="empty-text">暂无消息</text>
    </view>

    <view class="loading" v-if="loading">加载中...</view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { api } from '../../api'

const list = ref([])
const loading = ref(false)
const unreadCount = ref(0)
const myStudentId = ref('')
const currentPage = ref(1)
const totalPages = ref(1)
const jumpPage = ref('')

onShow(() => {
  const user = uni.getStorageSync('login_user')
  if (user && user.studentId) {
    myStudentId.value = user.studentId
    currentPage.value = 1
    loadList()
    // 进入消息页，清除 tab 红点
    uni.removeTabBarBadge({ index: 1 })
  }
})

async function loadList() {
  if (!myStudentId.value) return
  loading.value = true
  try {
    const res = await api.notification.list(myStudentId.value, currentPage.value)
    list.value = res.records || []
    totalPages.value = res.pages || 1
    currentPage.value = res.current || 1
    jumpPage.value = ''
    updateUnread()
  } catch (e) {
    list.value = []
  } finally {
    loading.value = false
  }
}

async function updateUnread() {
  try {
    unreadCount.value = await api.notification.unreadCount(myStudentId.value)
  } catch (e) {
    unreadCount.value = 0
  }
}

async function handleClick(item) {
  if (item.isRead === 0) {
    try {
      await api.notification.markRead(item.id)
      item.isRead = 1
      unreadCount.value = Math.max(0, unreadCount.value - 1)
    } catch (e) { /* ignore */ }
  }
  // 根据类型跳转
  if (item.type === 'announcement') {
    uni.navigateTo({ url: `/pages/announcement-detail/index?id=${item.refId}` })
  } else {
    // like / comment / favorite → 帖子详情
    uni.navigateTo({ url: `/pages/forum/detail?id=${item.refId}` })
  }
}

async function handleMarkAll() {
  try {
    await api.notification.markAllRead(myStudentId.value)
    list.value.forEach(i => { i.isRead = 1 })
    unreadCount.value = 0
    uni.showToast({ title: '全部已读', icon: 'success' })
  } catch (e) {
    uni.showToast({ title: '操作失败', icon: 'none' })
  }
}

const goPage = (p) => {
  if (p < 1 || p > totalPages.value) return
  currentPage.value = p
  loadList()
}

const jump = () => {
  const p = parseInt(jumpPage.value)
  if (p && p >= 1 && p <= totalPages.value) {
    currentPage.value = p
    loadList()
  }
}

const typeIcon = (type) => {
  const map = { like: '👍', comment: '💬', favorite: '⭐', announcement: '📢' }
  return map[type] || '🔔'
}
</script>

<style>
.container { padding: 12px; min-height: 100vh; box-sizing: border-box; }

.header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 12px; }
.header-title { font-size: 18px; font-weight: bold; color: #333; }
.header-btn { font-size: 13px; color: #7c3aed; padding: 6px 12px; }

.card {
  background: white; border-radius: 12px; padding: 14px 16px;
  margin-bottom: 10px; display: flex; align-items: flex-start;
  box-shadow: 0 1px 6px rgba(0,0,0,0.06); position: relative;
}
.card.unread { background: #faf5ff; }

.card-left { display: flex; align-items: center; gap: 8px; flex-shrink: 0; padding-top: 2px; }
.unread-dot {
  width: 10px; height: 10px; border-radius: 50%; background: #e74c3c;
  position: absolute; top: 10px; right: 10px;
}
.card-icon { font-size: 20px; }

.card-body { flex: 1; min-width: 0; margin-left: 8px; }
.card-top { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 4px; }
.card-title { font-size: 14px; color: #999; display: block; flex: 1; }
.card-title.bold { color: #333; font-weight: bold; }
.card-time { font-size: 11px; color: #bbb; flex-shrink: 0; margin-left: 8px; }
.card-desc { font-size: 13px; color: #aaa; display: block; line-height: 1.4; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }

.pagination {
  display: flex; align-items: center; justify-content: center; gap: 8px;
  padding: 16px 0 24px;
}
.page-btn {
  width: 36px; height: 36px; border: 1px solid #ddd; border-radius: 8px;
  display: flex; align-items: center; justify-content: center;
  font-size: 18px; color: #333; background: white;
}
.page-btn.disabled { opacity: 0.3; }
.page-info { display: flex; align-items: center; gap: 2px; font-size: 14px; color: #333; }
.page-input {
  width: 36px; height: 36px; border: 1px solid #ddd; border-radius: 6px;
  text-align: center; font-size: 14px; padding: 0; line-height: 36px;
  background: white;
}
.page-total { font-size: 14px; color: #999; }

.empty { display: flex; flex-direction: column; align-items: center; padding: 80px 0; }
.empty-icon { font-size: 48px; }
.empty-text { font-size: 14px; color: #999; margin-top: 12px; }
.loading { text-align: center; padding: 40px; color: #999; font-size: 13px; }
</style>
