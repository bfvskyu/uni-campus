<template>
  <view class="container">
    <view class="list" v-if="list.length">
      <view class="card" v-for="item in list" :key="item.id" @click="goDetail(item.id)">
        <view class="card-header">
          <view class="category-tag" :class="'cat-' + item.category">{{ categoryLabel(item.category) }}</view>
          <text class="card-title">{{ item.title }}</text>
        </view>
        <text class="card-content">{{ item.content }}</text>
        <view class="card-footer">
          <text class="author">{{ item.authorName || '匿名' }}</text>
          <text class="time">{{ item.createTime ? item.createTime.slice(0, 10) : '' }}</text>
        </view>
      </view>
    </view>

    <view class="empty" v-if="!loading && !list.length">
      <text class="empty-icon">⭐</text>
      <text class="empty-text">暂无收藏</text>
    </view>

    <view class="load-tip" v-if="loading">加载中...</view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { api } from '../../api'

const list = ref([])
const loading = ref(false)
const page = ref(1)
const totalPages = ref(1)

const getStudentId = () => {
  try {
    const user = uni.getStorageSync('login_user')
    return user?.studentId || ''
  } catch { return '' }
}

const categoryLabel = (val) => {
  const map = { help: '求助', discuss: '讨论', lost_found: '失物招领', notice: '公告' }
  return map[val] || val
}

const load = async () => {
  const sid = getStudentId()
  if (!sid) return
  loading.value = true
  try {
    const res = await api.post.myFavorites(sid, page.value)
    list.value = res.records || []
    totalPages.value = res.pages || 1
  } catch (e) {
    list.value = []
  } finally {
    loading.value = false
  }
}

const goDetail = (id) => uni.navigateTo({ url: `/pages/forum/detail?id=${id}` })

onShow(() => { page.value = 1; load() })
</script>

<style scoped>
.container { padding: 12px; background: #f5f6fa; min-height: 100vh; }
.card {
  background: #fff; border-radius: 12px; padding: 14px 16px;
  margin-bottom: 10px; box-shadow: 0 1px 6px rgba(0,0,0,0.05);
}
.card-header { display: flex; align-items: center; gap: 8px; margin-bottom: 8px; }
.category-tag {
  font-size: 11px; padding: 2px 8px; border-radius: 4px;
  color: #fff; flex-shrink: 0;
}
.cat-help { background: #f59e0b; }
.cat-discuss { background: #3b82f6; }
.cat-lost_found { background: #10b981; }
.cat-notice { background: #ef4444; }
.card-title { font-size: 15px; font-weight: bold; color: #333; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.card-content { font-size: 13px; color: #888; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; margin-bottom: 8px; }
.card-footer { display: flex; justify-content: space-between; }
.author { font-size: 12px; color: #999; }
.time { font-size: 11px; color: #ccc; }
.empty { display: flex; flex-direction: column; align-items: center; padding: 60px 0; }
.empty-icon { font-size: 40px; }
.empty-text { font-size: 13px; color: #999; margin-top: 12px; }
.load-tip { text-align: center; padding: 40px; color: #999; font-size: 13px; }
</style>
