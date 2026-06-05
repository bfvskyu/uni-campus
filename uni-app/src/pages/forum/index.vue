<template>
  <view class="container">
    <!-- 分类 Tab -->
    <scroll-view scroll-x class="tabs-wrap">
      <view class="tabs">
        <view
          v-for="tab in categories"
          :key="tab.value"
          class="tab"
          :class="{ active: currentCategory === tab.value }"
          @click="switchTab(tab.value)"
        >{{ tab.label }}</view>
      </view>
    </scroll-view>

    <!-- 帖子列表 -->
    <scroll-view
      scroll-y
      class="post-list"
      refresher-enabled
      :refresher-triggered="isRefreshing"
      @refresherrefresh="onRefresh"
      @scrolltolower="loadMore"
    >
      <view v-if="list.length">
        <view
          class="post-card"
          v-for="item in list"
          :key="item.id"
          @click="goDetail(item.id)"
        >
          <!-- 置顶标记 -->
          <view class="pin-badge" v-if="item.isPinned">📌 置顶</view>

          <!-- 标题行 -->
          <view class="post-header">
            <view class="category-tag" :class="'cat-' + item.category">{{ categoryLabel(item.category) }}</view>
            <text class="post-title">{{ item.title }}</text>
          </view>

          <!-- 内容预览 -->
          <text class="post-content">{{ item.content }}</text>

          <!-- 失物招领：显示联系方式 -->
          <view class="contact-row" v-if="item.category === 'lost_found' && item.phone">
            <text class="contact-text">📱 {{ item.phone }}</text>
          </view>

          <!-- 底部：作者 + 统计 -->
          <view class="post-footer">
            <view class="author-info">
              <image
                class="avatar"
                v-if="item.authorAvatar"
                :src="'http://localhost:8080' + item.authorAvatar"
                mode="aspectFill"
              />
              <view class="avatar avatar-text" v-else>{{ (item.authorName || '匿')[0] }}</view>
              <text class="author-name">{{ item.authorName || '匿名' }}</text>
            </view>
            <view class="stats">
              <text class="stat-item">👁 {{ item.viewCount || 0 }}</text>
              <text class="stat-item">👍 {{ item.likeCount || 0 }}</text>
              <text class="stat-item">💬 {{ item.commentCount || 0 }}</text>
            </view>
          </view>
        </view>
      </view>

      <!-- 加载状态 -->
      <view class="load-tip" v-if="loading">{{ loadingText }}</view>
      <view class="load-tip" v-if="!loading && !list.length && !refreshing">暂无帖子</view>
    </scroll-view>

    <!-- 发帖按钮 -->
    <view class="fab" @click="goCreate">+</view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { api } from '../../api'

const categories = [
  { label: '全部', value: '' },
  { label: '求助', value: 'help' },
  { label: '讨论', value: 'discuss' },
  { label: '失物招领', value: 'lost_found' },
  { label: '公告', value: 'notice' }
]

const list = ref([])
const currentCategory = ref('')
const page = ref(1)
const totalPages = ref(1)
const loading = ref(false)
const refreshing = ref(false)
const isRefreshing = ref(false)
const loadingText = ref('加载中...')

const getStudentId = () => {
  try {
    const user = uni.getStorageSync('login_user')
    return user?.studentId || ''
  } catch { return '' }
}

const load = async (isLoadMore = false) => {
  if (loading.value) return
  loading.value = true
  loadingText.value = isLoadMore ? '加载中...' : '加载中...'
  try {
    const res = await api.post.list(currentCategory.value, getStudentId(), page.value)
    const records = res.records || []
    if (isLoadMore) {
      list.value = [...list.value, ...records]
    } else {
      list.value = records
    }
    totalPages.value = res.pages || 1
  } catch (e) {
    if (!isLoadMore) list.value = []
  } finally {
    loading.value = false
    isRefreshing.value = false
    refreshing.value = false
  }
}

const switchTab = (val) => {
  currentCategory.value = val
  page.value = 1
  load()
}

const onRefresh = () => {
  refreshing.value = true
  isRefreshing.value = true
  page.value = 1
  load()
}

const loadMore = () => {
  if (page.value >= totalPages.value) return
  page.value++
  load(true)
}

const categoryLabel = (val) => {
  const found = categories.find(c => c.value === val)
  return found ? found.label : val
}

const goDetail = (id) => uni.navigateTo({ url: `/pages/forum/detail?id=${id}` })
const goCreate = () => uni.navigateTo({ url: '/pages/forum/create' })

onShow(() => {
  page.value = 1
  load()
})
</script>

<style scoped>
.container { display: flex; flex-direction: column; height: 100vh; background: #f5f6fa; }

/* 分类 Tab */
.tabs-wrap { white-space: nowrap; background: #fff; border-bottom: 1px solid #eee; }
.tabs { display: inline-flex; padding: 10px 12px; gap: 8px; }
.tab {
  display: inline-block; font-size: 13px; color: #666;
  padding: 6px 16px; border-radius: 16px; background: #f5f5f5;
  flex-shrink: 0;
}
.tab.active { background: #7c3aed; color: #fff; font-weight: bold; }

/* 帖子列表 */
.post-list { flex: 1; padding: 10px 12px; }

.post-card {
  background: #fff; border-radius: 12px; padding: 14px 16px;
  margin-bottom: 10px; box-shadow: 0 1px 6px rgba(0,0,0,0.05);
}

/* 置顶 */
.pin-badge { font-size: 11px; color: #7c3aed; margin-bottom: 6px; font-weight: bold; }

/* 标题行 */
.post-header { display: flex; align-items: center; gap: 8px; margin-bottom: 8px; }
.category-tag {
  font-size: 11px; padding: 2px 8px; border-radius: 4px;
  color: #fff; flex-shrink: 0;
}
.cat-help { background: #f59e0b; }
.cat-discuss { background: #3b82f6; }
.cat-lost_found { background: #10b981; }
.cat-notice { background: #ef4444; }
.post-title {
  font-size: 15px; font-weight: bold; color: #333;
  overflow: hidden; text-overflow: ellipsis; white-space: nowrap;
}

/* 内容预览 */
.post-content {
  font-size: 13px; color: #888; line-height: 1.6;
  display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical;
  overflow: hidden; margin-bottom: 10px;
}

/* 联系方式 */
.contact-row { margin-bottom: 8px; }
.contact-text { font-size: 12px; color: #10b981; }

/* 底部 */
.post-footer { display: flex; justify-content: space-between; align-items: center; }
.author-info { display: flex; align-items: center; gap: 6px; }
.avatar { width: 22px; height: 22px; border-radius: 50%; }
.avatar-text {
  display: flex; align-items: center; justify-content: center;
  background: #7c3aed; color: #fff; font-size: 11px; font-weight: bold;
}
.author-name { font-size: 12px; color: #999; }
.stats { display: flex; gap: 12px; }
.stat-item { font-size: 11px; color: #bbb; }

/* 加载提示 */
.load-tip { text-align: center; padding: 20px; font-size: 12px; color: #ccc; }

/* 发帖 FAB */
.fab {
  position: fixed; right: calc(50% - 215px + 20px); bottom: 100px;
  width: 52px; height: 52px; border-radius: 50%;
  background: #7c3aed; color: #fff;
  display: flex; align-items: center; justify-content: center;
  font-size: 28px; font-weight: bold;
  box-shadow: 0 4px 12px rgba(124,58,237,0.4);
  z-index: 999;
}
</style>
