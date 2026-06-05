<template>
  <view class="container">
    <view class="box">
      <text class="icon">🔒</text>
      <text class="title">首次登录，请修改密码</text>
      <text class="subtitle">出于安全考虑，请设置您的新密码</text>

      <textarea class="input" v-model="oldPassword" placeholder="旧密码（默认 Stu+学号+355）" />
      <textarea class="input" v-model="newPassword" placeholder="新密码（至少6位）" />
      <textarea class="input" v-model="confirmPassword" placeholder="确认新密码" />

      <text class="error" v-if="error">{{ error }}</text>
      <view class="btn" @click="handleChange">确认修改</view>
      <text class="skip" @click="skip">稍后再说</text>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { api } from '../../api'

const studentId = ref('')
const oldPassword = ref('')
const newPassword = ref('')
const confirmPassword = ref('')
const error = ref('')

onLoad((query) => {
  studentId.value = query.studentId || ''
  oldPassword.value = 'Stu' + studentId.value + '355'
})

const handleChange = async () => {
  error.value = ''
  if (!newPassword.value.trim()) return error.value = '请输入新密码'
  if (newPassword.value.length < 6) return error.value = '密码至少6位'
  if (newPassword.value !== confirmPassword.value) return error.value = '两次密码不一致'

  try {
    await api.user.changePassword({
      studentId: studentId.value,
      oldPassword: oldPassword.value,
      newPassword: newPassword.value
    })
    const user = uni.getStorageSync('login_user')
    if (user) {
      user.needChangePwd = 0
      uni.setStorageSync('login_user', user)
    }
    uni.showToast({ title: '密码修改成功' })
    setTimeout(() => uni.switchTab({ url: '/pages/profile/index' }), 500)
  } catch (e) {
    error.value = e.message || '修改失败'
  }
}

const skip = () => uni.navigateBack()
</script>

<style>
.container {
  display: flex; align-items: center; justify-content: center;
  min-height: 70vh; padding: 20px;
}
.box {
  background: white; border-radius: 14px; padding: 32px 24px;
  width: 280px; display: flex; flex-direction: column; align-items: center;
  box-shadow: 0 2px 12px rgba(0,0,0,0.08);
}
.icon { font-size: 40px; margin-bottom: 8px; }
.title { font-size: 16px; font-weight: bold; text-align: center; }
.subtitle { font-size: 12px; color: #999; margin: 4px 0 20px; text-align: center; }
.input {
  width: 100%; height: 40px; line-height: 1.2;
  border: 1px solid #e5e5e5; border-radius: 8px; padding: 10px 12px;
  font-size: 14px; background: #f9f9f9; box-sizing: border-box; margin-bottom: 10px;
}
.error { font-size: 12px; color: #ef4444; margin-top: 4px; }
.btn {
  margin-top: 12px; background: #7c3aed; color: white;
  width: 100%; text-align: center; padding: 12px; border-radius: 8px;
  font-size: 15px; font-weight: bold;
}
.skip { margin-top: 12px; font-size: 12px; color: #999; }
</style>
