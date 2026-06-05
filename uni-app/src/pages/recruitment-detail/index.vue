<template>
  <view class="container" v-if="job">
    <view class="card">
      <view class="header-row">
        <view class="header-left">
          <text class="title">{{ job.title }}</text>
          <text class="salary">{{ job.salary }}</text>
        </view>
        <text class="memo-btn" :class="{ memorized }" @click="toggleMemo">{{ memorized ? '−' : '+' }}</text>
      </view>
      <text class="shop">{{ job.shopName }}</text>
      <view class="tags">
        <text class="tag" v-for="t in (job.tags || '').split(',')" :key="t">{{ t }}</text>
      </view>
    </view>
    <view class="card">
      <text class="section-title">📋 职位描述</text>
      <text class="desc">{{ job.description }}</text>
    </view>
    <view class="card">
      <text class="section-title">✅ 任职要求</text>
      <text class="desc">{{ job.requirements }}</text>
    </view>
    <view class="card">
      <text class="section-title">📍 工作地点</text>
      <text class="desc">{{ job.location }}</text>
    </view>
    <view class="card">
      <text class="section-title">📞 联系方式</text>
      <text class="desc">{{ job.contact }}</text>
      <text class="date">发布时间：{{ job.publishDate }}</text>
    </view>
    <view class="contact-btn" @click="showContact"><text>📞 立即联系</text></view>
  </view>
  <Skeleton type="detail" v-else />
</template>

<script setup>
import { ref } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { api } from '../../api'
import Skeleton from '../../components/Skeleton.vue'

const job = ref(null)
const memorized = ref(false)
const memoId = ref(null)

const myStudentId = ref('')

onLoad(async (query) => {
  const id = Number(query.id)
  const user = uni.getStorageSync('login_user')
  if (user && user.studentId) myStudentId.value = user.studentId
  try {
    job.value = await api.recruitment.detail(id)
    if (myStudentId.value) {
      const memo = await api.memo.check('recruitment', id, myStudentId.value)
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
      const res = await api.memo.add({ refType: 'recruitment', refId: job.value.id, title: job.value.title, studentId: myStudentId.value })
      memoId.value = res.id
      memorized.value = true
      uni.showToast({ title: '已添加备忘', icon: 'success' })
    }
  } catch (e) {
    uni.showToast({ title: '操作失败', icon: 'none' })
  }
}

const showContact = () => uni.showToast({ title: '请直接拨打上方联系电话', icon: 'none' })
</script>

<style>
.container { padding: 12px; }
.card { background: white; border-radius: 10px; padding: 14px; margin-bottom: 10px; }
.header-row { display: flex; justify-content: space-between; align-items: flex-start; }
.header-left { flex: 1; }
.title { font-size: 17px; font-weight: bold; display: block; }
.salary { font-size: 15px; color: #e17055; font-weight: bold; margin-top: 4px; display: inline-block; }
.memo-btn {
  width: 32px; height: 32px; border-radius: 50%; flex-shrink: 0;
  background: #7c3aed; color: white; font-size: 20px; font-weight: bold;
  display: flex; align-items: center; justify-content: center; line-height: 1;
  margin-left: 10px;
}
.memo-btn.memorized { background: #e74c3c; }
.shop { font-size: 13px; color: #666; margin-top: 6px; display: block; }
.tags { display: flex; margin-top: 8px; gap: 8px; flex-wrap: wrap; }
.tag { font-size: 11px; background: #ede9fe; color: #7c3aed; padding: 2px 10px; border-radius: 6px; }
.section-title { font-size: 14px; font-weight: bold; display: block; margin-bottom: 8px; }
.desc { font-size: 13px; color: #444; line-height: 1.8; white-space: pre-wrap; display: block; }
.date { font-size: 11px; color: #999; margin-top: 8px; display: block; }
.contact-btn { background: #7c3aed; color: white; text-align: center; padding: 12px; border-radius: 10px; font-size: 15px; }
</style>
