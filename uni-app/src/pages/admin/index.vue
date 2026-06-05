<template>
  <!-- 管理员看板 -->
  <view class="container" v-if="isAdmin">
    <view class="card purple" @click="toPage('announcement')">
      <text class="card-icon">📋</text>
      <text class="card-title">公告管理</text>
      <text class="card-desc">发布、编辑、删除校园公告</text>
    </view>
    <view class="card blue" @click="toPage('exam')">
      <text class="card-icon">📝</text>
      <text class="card-title">考试信息管理</text>
      <text class="card-desc">管理考试与竞赛信息</text>
    </view>
    <view class="card green" @click="toPage('recruitment')">
      <text class="card-icon">💼</text>
      <text class="card-title">招聘信息管理</text>
      <text class="card-desc">发布、编辑、删除招聘信息</text>
    </view>
    <view class="card pink" @click="toPage('feedback')">
      <text class="card-icon">💬</text>
      <text class="card-title">反馈中心</text>
      <text class="card-desc">查看用户反馈与建议</text>
    </view>
    <view class="card orange">
      <text class="card-icon">🏫</text>
      <text class="card-title">教室功能管理</text>
      <text class="card-desc">功能建设中...</text>
    </view>
  </view>

  <view class="no-access" v-else>
    <text class="no-access-icon">🔒</text>
    <text class="no-access-title">无权限访问</text>
    <text class="no-access-desc">仅管理员可进入管理面板</text>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { api } from '../../api'

const isAdmin = ref(false)

onShow(async () => {
  const user = uni.getStorageSync('login_user')
  if (user && user.studentId) {
    if (user.role === 0) {
      isAdmin.value = true
      return
    }
    try {
      const res = await api.admin.checkRole(user.studentId)
      isAdmin.value = res && res.isAdmin
    } catch (e) {
      isAdmin.value = false
    }
  }
})

const toPage = (name) => {
  uni.navigateTo({ url: `/pages/admin/${name}` })
}
</script>

<style>
.container {
  display: flex; flex-direction: column; gap: 12px;
  min-height: 100vh; padding: 12px; box-sizing: border-box;
}
.card {
  flex: 1; display: flex; flex-direction: column;
  align-items: center; justify-content: center;
  color: white; padding: 16px; border-radius: 14px;
}
.card-icon { font-size: 36px; margin-bottom: 8px; }
.card-title { font-size: 18px; font-weight: bold; margin-bottom: 4px; }
.card-desc { font-size: 12px; opacity: 0.85; }
.purple { background: linear-gradient(135deg, #8b5cf6, #7c3aed); }
.blue { background: linear-gradient(135deg, #3b82f6, #2563eb); }
.green { background: linear-gradient(135deg, #10b981, #059669); }
.pink { background: linear-gradient(135deg, #f472b6, #ec4899); }
.orange { background: linear-gradient(135deg, #f59e0b, #d97706); }
.no-access {
  display: flex; flex-direction: column; align-items: center;
  justify-content: center; min-height: 70vh; padding: 20px;
}
.no-access-icon { font-size: 48px; margin-bottom: 12px; }
.no-access-title { font-size: 18px; font-weight: bold; color: #333; margin-bottom: 6px; }
.no-access-desc { font-size: 13px; color: #999; }
</style>
