<template>
  <view class="container">
    <view class="avatar-section">
      <view class="avatar-box" @click="chooseImage">
        <image v-if="avatarUrl" class="avatar-preview" :src="avatarUrl" mode="aspectFill" />
        <view v-else class="avatar-placeholder">
          <text class="plus">+</text>
        </view>
      </view>
      <view class="avatar-tip" v-if="!avatarUrl">
        <text class="tip-text">还没有个性头像呢？</text>
        <text class="tip-text">不妨加个试试！😊</text>
      </view>
    </view>

    <view class="form-card">
      <view class="form-item">
        <text class="form-label">姓名</text>
        <input class="form-input" v-model="form.name" placeholder="请输入姓名" />
      </view>
      <view class="form-item">
        <text class="form-label">学号</text>
        <input class="form-input" v-model="form.studentId" placeholder="学号" disabled />
      </view>
      <view class="form-item">
        <text class="form-label">院系</text>
        <input class="form-input" v-model="form.major" placeholder="请输入院系/专业" />
      </view>
      <view class="form-item">
        <text class="form-label">手机</text>
        <input class="form-input" v-model="form.phone" placeholder="请输入手机号" />
      </view>
    </view>
    <view class="save-btn" @click="save">保存</view>
    <view class="change-pwd-btn" @click="goChangePwd">修改密码</view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { api } from '../../api'

const BASE = 'http://localhost:8080'

const form = ref({ name: '', studentId: '', major: '', phone: '' })
const avatarUrl = ref('')
const studentId = ref('')

onLoad(async () => {
  const user = uni.getStorageSync('login_user')
  if (!user || !user.studentId) return uni.navigateBack()
  studentId.value = user.studentId
  try {
    const p = await api.user.profile(user.studentId)
    form.value = { name: p.name || '', studentId: p.studentId || '', major: p.major || '', phone: p.phone || '' }
    if (p.avatarUrl) avatarUrl.value = BASE + p.avatarUrl
  } catch (e) { console.error(e) }
})

const chooseImage = () => {
  uni.chooseImage({
    count: 1,
    sizeType: ['compressed'],
    sourceType: ['album', 'camera'],
    success: (res) => {
      const tempPath = res.tempFilePaths[0]
      uni.showLoading({ title: '上传中...' })
      uni.uploadFile({
        url: BASE + '/api/user/avatar?studentId=' + studentId.value,
        filePath: tempPath,
        name: 'file',
        header: { 'Accept': 'application/json' },
        success: (uploadRes) => {
          uni.hideLoading()
          let data = uploadRes.data
          if (typeof data === 'string') data = JSON.parse(data)
          if (data.code === 200) {
            avatarUrl.value = BASE + data.data
            uni.showToast({ title: '头像上传成功', icon: 'success' })
          } else {
            uni.showToast({ title: data.message || '上传失败', icon: 'none' })
          }
        },
        fail: (err) => {
          uni.hideLoading()
          uni.showToast({ title: '上传失败', icon: 'none' })
        }
      })
    }
  })
}

const save = async () => {
  try {
    await api.user.update(form.value)
    // 更新 localStorage 中的信息
    const user = uni.getStorageSync('login_user')
    if (user) {
      user.name = form.value.name
      uni.setStorageSync('login_user', user)
    }
    uni.showToast({ title: '保存成功', icon: 'success' })
    uni.navigateBack()
  } catch (e) {
    uni.showToast({ title: '保存失败', icon: 'none' })
  }
}

const goChangePwd = () => {
  const user = uni.getStorageSync('login_user')
  if (user && user.studentId) uni.navigateTo({ url: '/pages/login/change-pwd?studentId=' + user.studentId })
}
</script>

<style>
.container { padding: 12px; }

.avatar-section {
  display: flex; align-items: center; gap: 16px;
  margin-bottom: 16px;
}
.avatar-box {
  width: 80px; height: 80px; border-radius: 12px;
  border: 2px dashed #ccc; overflow: hidden; flex-shrink: 0;
}
.avatar-placeholder {
  width: 100%; height: 100%; background: #f5f5f5;
  display: flex; align-items: center; justify-content: center;
}
.plus { font-size: 32px; color: #bbb; font-weight: bold; }
.avatar-preview { width: 100%; height: 100%; }
.avatar-tip { flex: 1; }
.tip-text { display: block; font-size: 12px; color: #999; line-height: 1.6; }

.form-card { background: white; border-radius: 12px; padding: 4px 16px; }
.form-item { display: flex; align-items: center; padding: 14px 0; border-bottom: 1px solid #f5f5f5; }
.form-item:last-child { border-bottom: none; }
.form-label { font-size: 14px; color: #333; width: 60px; flex-shrink: 0; }
.form-input { flex: 1; font-size: 14px; color: #333; padding: 0; border: none; outline: none; }
.save-btn {
  background: #7c3aed; color: white; text-align: center;
  padding: 12px; border-radius: 10px; font-size: 15px; margin-top: 20px;
}
.change-pwd-btn {
  background: white; color: #7c3aed; text-align: center;
  padding: 12px; border-radius: 10px; font-size: 14px; margin-top: 10px;
  border: 1px solid #7c3aed;
}
</style>
