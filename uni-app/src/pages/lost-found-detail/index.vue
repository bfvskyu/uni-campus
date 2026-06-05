<template>
  <view class="container" v-if="item">
    <view class="card">
      <view class="author">
        <image v-if="item.authorAvatar" class="avatar" :src="'http://localhost:8080' + item.authorAvatar" mode="aspectFill" />
        <view v-else class="avatar avatar-text">{{ (item.authorName || '匿')[0] }}</view>
        <view class="author-info">
          <text class="author-name">{{ item.authorName || '匿名' }}</text>
          <text class="author-time">{{ item.createTime ? item.createTime.slice(0, 10) : '' }}</text>
        </view>
        <text class="type-tag" :class="'tag-' + item.type">{{ item.type === 'lost' ? '寻物' : '拾物' }}</text>
      </view>

      <text class="title">{{ item.title }}</text>
      <text class="desc">{{ item.description }}</text>

      <view class="image-row" v-if="imgs.length">
        <image class="detail-img" v-for="(img, idx) in imgs" :key="idx" :src="'http://localhost:8080' + img" mode="aspectFill" @click="previewImg(idx)" />
      </view>

      <view class="contact">
        <text class="contact-title">联系方式</text>
        <text class="contact-item" v-if="item.phone">📱 {{ item.phone }}</text>
        <text class="contact-item" v-if="item.wechat">💬 微信：{{ item.wechat }}</text>
      </view>
    </view>

    <view class="action-bar" v-if="isOwner">
      <view class="action-btn edit" @click="goEdit">编辑</view>
      <view class="action-btn delete" @click="handleDelete">删除</view>
    </view>
  </view>

  <view class="loading" v-else>加载中...</view>
</template>

<script setup>
import { ref, computed } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { api } from '../../api'

const item = ref(null)
const imgs = ref([])
const myStudentId = ref('')

onLoad(async (query) => {
  if (!query.id) return
  const user = uni.getStorageSync('login_user')
  if (user && user.studentId) myStudentId.value = user.studentId
  try {
    item.value = await api.lostFound.detail(query.id)
    if (item.value.images) imgs.value = JSON.parse(item.value.images) || []
  } catch (e) {
    uni.showToast({ title: '加载失败', icon: 'none' })
  }
})

const isOwner = computed(() => item.value && myStudentId.value && item.value.studentId === myStudentId.value)

const previewImg = (idx) => {
  const urls = imgs.value.map(i => 'http://localhost:8080' + i)
  uni.previewImage({ current: urls[idx], urls })
}

const goEdit = () => {
  uni.navigateTo({ url: '/pages/lost-found/edit?id=' + item.value.id })
}

const handleDelete = () => {
  uni.showModal({ title: '确认删除', content: '确定要删除这条信息吗？', success: async (r) => {
    if (r.confirm) {
      try {
        await api.lostFound.delete(item.value.id, myStudentId.value)
        uni.showToast({ title: '删除成功' })
        setTimeout(() => uni.navigateBack(), 500)
      } catch (e) {
        uni.showToast({ title: e.message || '删除失败', icon: 'none' })
      }
    }
  }})
}
</script>

<style>
.container { padding: 12px; }

.card { background: white; border-radius: 12px; padding: 16px; box-shadow: 0 1px 6px rgba(0,0,0,0.06); }

.author { display: flex; align-items: center; gap: 10px; margin-bottom: 14px; }
.avatar { width: 40px; height: 40px; border-radius: 50%; display: block; }
.avatar-text { width: 40px; height: 40px; border-radius: 50%; background: linear-gradient(135deg, #7c3aed, #3b82f6); color: white; display: flex; align-items: center; justify-content: center; font-size: 16px; font-weight: bold; }
.author-info { flex: 1; }
.author-name { font-size: 14px; font-weight: bold; display: block; }
.author-time { font-size: 11px; color: #999; display: block; margin-top: 2px; }
.type-tag { font-size: 11px; padding: 3px 10px; border-radius: 4px; color: white; }
.tag-lost { background: #f59e0b; }
.tag-found { background: #10b981; }

.title { font-size: 17px; font-weight: bold; color: #333; display: block; margin-bottom: 10px; }
.desc { font-size: 14px; color: #555; line-height: 1.7; display: block; margin-bottom: 12px; }

.image-row { display: flex; gap: 10px; margin-bottom: 14px; flex-wrap: wrap; }
.detail-img { width: 150px; height: 150px; border-radius: 8px; display: block; }

.contact { background: #f9f9f9; border-radius: 8px; padding: 12px; }
.contact-title { font-size: 13px; font-weight: bold; color: #333; display: block; margin-bottom: 6px; }
.contact-item { font-size: 13px; color: #555; display: block; margin-top: 4px; }

.action-bar { display: flex; gap: 10px; margin-top: 14px; }
.action-btn { flex: 1; text-align: center; padding: 12px; border-radius: 8px; font-size: 14px; font-weight: bold; }
.edit { background: #7c3aed; color: white; }
.delete { background: #fee2e2; color: #ef4444; }

.loading { text-align: center; padding: 40px; color: #999; font-size: 13px; }
</style>
