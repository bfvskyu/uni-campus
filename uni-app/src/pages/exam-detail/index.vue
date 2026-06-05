<template>
  <view class="container" v-if="item">
    <view class="card">
      <view class="top-row">
        <view class="title-area">
          <text class="title">{{ item.title }}</text>
          <text class="type-tag">{{ item.type }}</text>
        </view>
        <text class="memo-btn" :class="{ memorized }" @click="toggleMemo">{{ memorized ? '−' : '+' }}</text>
      </view>
      <view class="meta-row">
        <text class="meta">📅 考试时间：{{ item.examDate }}</text>
        <text class="meta">⏰ 报名截止：{{ item.deadline }}</text>
      </view>
      <view class="status-bar" :class="statusClass(item.status)">
        <text>{{ item.status === '报名中' ? '🟢 正在报名' : item.status === '即将开始' ? '🟡 即将开始' : '⚪ 未开始' }}</text>
      </view>
    </view>
    <view class="card">
      <text class="section-title">📋 详细信息</text>
      <text class="content">{{ item.detail }}</text>
    </view>
    <view class="action-btn" v-if="item.status === '报名中'" @click="goRegister"><text>📝 立即报名</text></view>
  </view>
  <Skeleton type="detail" v-else />
</template>

<script setup>
import { ref } from 'vue'
import Skeleton from '../../components/Skeleton.vue'
import { onLoad } from '@dcloudio/uni-app'
import { api } from '../../api'

const item = ref(null)
const memorized = ref(false)
const memoId = ref(null)
const myStudentId = ref('')

onLoad(async (query) => {
  const id = Number(query.id)
  const user = uni.getStorageSync('login_user')
  if (user && user.studentId) myStudentId.value = user.studentId
  try {
    item.value = await api.exam.detail(id)
    if (myStudentId.value) {
      const memo = await api.memo.check('exam', id, myStudentId.value)
      if (memo) { memorized.value = true; memoId.value = memo.id }
    }
  } catch (e) { console.error(e) }
})

const toggleMemo = async () => {
  try {
    if (memorized.value) {
      if (memoId.value) await api.memo.remove(memoId.value)
      memorized.value = false
      uni.showToast({ title: '已取消备忘', icon: 'none' })
    } else {
      const res = await api.memo.add({ refType: 'exam', refId: item.value.id, title: item.value.title, studentId: myStudentId.value })
      memoId.value = res.id
      memorized.value = true
      uni.showToast({ title: '已添加备忘', icon: 'success' })
    }
  } catch (e) {
    uni.showToast({ title: '操作失败', icon: 'none' })
  }
}

const goRegister = () => {
  const url = item.value?.websiteUrl
  if (!url) {
    uni.showToast({ title: '报名功能正在开发中', icon: 'none' })
    return
  }
  uni.setClipboardData({
    data: url,
    success: () => uni.showToast({ title: '报名链接已复制' }),
    fail: () => uni.showToast({ title: '复制失败请手动复制链接', icon: 'none' })
  })
}

const statusClass = (s) => ({ '报名中': 'registering', '即将开始': 'upcoming', '未开始': 'pending' }[s] || '')
</script>

<style>
.container { padding: 12px; }
.card { background: white; border-radius: 10px; padding: 14px; margin-bottom: 10px; }
.top-row { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 12px; }
.title-area { flex: 1; display: flex; align-items: center; }
.title { font-size: 16px; font-weight: bold; line-height: 1.4; }
.type-tag { font-size: 11px; background: #ede9fe; color: #7c3aed; padding: 4px 12px; border-radius: 6px; margin-left: 10px; }
.memo-btn {
  width: 32px; height: 32px; border-radius: 50%; flex-shrink: 0;
  background: #7c3aed; color: white; font-size: 20px; font-weight: bold;
  display: flex; align-items: center; justify-content: center; line-height: 1;
  margin-left: 10px;
}
.memo-btn.memorized { background: #e74c3c; }
.meta-row { display: flex; flex-direction: column; gap: 4px; margin-bottom: 10px; }
.meta { font-size: 12px; color: #666; }
.status-bar { text-align: center; padding: 8px; border-radius: 6px; font-size: 13px; }
.status-bar.registering { background: #ede9fe; color: #7c3aed; }
.status-bar.upcoming { background: #fff3e0; color: #f39c12; }
.status-bar.pending { background: #f5f6fa; color: #999; }
.section-title { font-size: 14px; font-weight: bold; display: block; margin-bottom: 8px; }
.content { font-size: 13px; line-height: 1.9; color: #333; white-space: pre-wrap; display: block; }
.action-btn { background: #7c3aed; color: white; text-align: center; padding: 12px; border-radius: 10px; font-size: 15px; }
</style>
