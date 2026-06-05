<template>
  <view class="container">
    <!-- 用户信息卡 -->
    <view class="profile-card">
      <image v-if="profile.avatarUrl" class="avatar-img" :src="'http://localhost:8080' + profile.avatarUrl" mode="aspectFill" />
      <view v-else class="avatar">{{ initials }}</view>
      <view class="info">
        <text class="name">{{ profile.name || '用户' }}</text>
        <text class="detail" v-if="profile.studentId">学号：{{ profile.studentId }}</text>
        <text class="detail" v-if="profile.major">院系：{{ profile.major }}</text>
      </view>
      <view class="edit-btn" @click="goEdit">编辑</view>
    </view>

    <!-- 功能列表 -->
    <view class="menu-card">
      <view class="menu-item" @click="toMemo">
        <text class="menu-icon">📋</text>
        <text class="menu-text">我的备忘录</text>
        <text class="menu-arrow">›</text>
      </view>
      <view class="menu-item" @click="toHelp">
        <text class="menu-icon">💬</text>
        <text class="menu-text">帮助与反馈</text>
        <text class="menu-arrow">›</text>
      </view>
      <view class="menu-item" @click="toAdmin">
        <text class="menu-icon">🔑</text>
        <text class="menu-text">管理员模式</text>
        <text class="menu-arrow">›</text>
      </view>
      <view class="menu-item" @click="toAbout">
        <text class="menu-icon">ℹ️</text>
        <text class="menu-text">关于</text>
        <text class="menu-arrow">›</text>
      </view>
      <view class="menu-item" @click="handleLogout">
        <text class="menu-icon">🚪</text>
        <text class="menu-text" style="color: #ef4444;">退出登录</text>
        <text class="menu-arrow">›</text>
      </view>
    </view>

  </view>
</template>

<script setup>
import { ref, computed } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { api } from '../../api'

const profile = ref({})

const initials = computed(() => {
  const n = profile.value.name || '用户'
  return n.charAt(0)
})

onShow(async () => {
  const stored = uni.getStorageSync('login_user')
  if (!stored || !stored.studentId) {
    uni.navigateTo({ url: '/pages/login/index' })
    return
  }
  try {
    profile.value = await api.user.profile(stored.studentId)
  } catch (e) {
    profile.value = stored
  }
})

const goEdit = () => uni.navigateTo({ url: '/pages/profile/edit' })
const toMemo = () => uni.navigateTo({ url: '/pages/memo/index' })
const toHelp = () => uni.navigateTo({ url: '/pages/profile/help' })
const toAbout = () => uni.navigateTo({ url: '/pages/profile/about' })
const toAdmin = () => uni.navigateTo({ url: '/pages/admin/index' })

const handleLogout = () => {
  uni.showModal({
    title: '退出登录',
    content: '确定要退出当前账号吗？',
    success: (r) => {
      if (r.confirm) {
        uni.removeStorageSync('login_user')
        uni.navigateTo({ url: '/pages/login/index' })
      }
    }
  })
}
</script>

<style>
.container { padding: 12px; }

.profile-card {
  background: white; border-radius: 12px; padding: 20px;
  display: flex; align-items: center; gap: 14px;
  box-shadow: 0 1px 6px rgba(0,0,0,0.06);
}
.avatar, .avatar-img {
  width: 56px; height: 56px; border-radius: 50%;
  flex-shrink: 0;
}
.avatar {
  background: linear-gradient(135deg, #7c3aed, #3b82f6);
  color: white; font-size: 22px; font-weight: bold;
  display: flex; align-items: center; justify-content: center;
}
.avatar-img { display: block; }
.info { flex: 1; }
.name { font-size: 17px; font-weight: bold; display: block; margin-bottom: 4px; }
.detail { font-size: 12px; color: #888; display: block; line-height: 1.6; }
.edit-btn {
  font-size: 12px; color: #7c3aed; background: #ede9fe;
  padding: 6px 16px; border-radius: 16px; flex-shrink: 0;
}

.menu-card {
  background: white; border-radius: 12px; margin-top: 12px;
  box-shadow: 0 1px 6px rgba(0,0,0,0.06); overflow: hidden;
}
.menu-item {
  display: flex; align-items: center; padding: 16px;
  border-bottom: 1px solid #f5f5f5;
}
.menu-item:last-child { border-bottom: none; }
.menu-icon { font-size: 18px; margin-right: 12px; }
.menu-text { flex: 1; font-size: 14px; color: #333; }
.menu-arrow { font-size: 18px; color: #ccc; }
</style>
