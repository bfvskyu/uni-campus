<template>
  <view class="container">
    <view class="login-box">
      <text class="logo">🏛️</text>
      <text class="title">大学生服务中心</text>
      <text class="subtitle">学号登录</text>

      <textarea class="input" v-model="studentId" placeholder="请输入学号" />
      <textarea class="input" v-model="password" placeholder="请输入密码" style="height: 40px; line-height: 1.2;" />
      <text class="tip">默认密码：Stu+学号+355</text>
      <text class="error" v-if="error">{{ error }}</text>

      <view class="login-btn" @click="handleLogin">登 录</view>
      <text class="forgot-link" @click="goForgot">忘记密码？</text>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { api } from '../../api'

const studentId = ref('')
const password = ref('')
const error = ref('')

const goForgot = () => uni.navigateTo({ url: '/pages/login/forgot-pwd' })

const handleLogin = async () => {
  error.value = ''
  if (!studentId.value.trim()) return error.value = '请输入学号'
  if (!password.value.trim()) return error.value = '请输入密码'

  try {
    const user = await api.user.login(studentId.value.trim(), password.value)
    uni.setStorageSync('login_user', user)
    uni.showToast({ title: '登录成功' })
    if (user.needChangePwd) {
      uni.navigateTo({ url: '/pages/login/change-pwd?studentId=' + user.studentId })
    } else {
      uni.switchTab({ url: '/pages/profile/index' })
    }
  } catch (e) {
    error.value = e.message || '登录失败'
  }
}
</script>

<style>
.container {
  display: flex; align-items: center; justify-content: center;
  min-height: 70vh; padding: 20px;
}
.login-box {
  background: white; border-radius: 14px; padding: 32px 24px;
  width: 280px; display: flex; flex-direction: column; align-items: center;
  box-shadow: 0 2px 12px rgba(0,0,0,0.08);
}
.logo { font-size: 48px; margin-bottom: 8px; }
.title { font-size: 18px; font-weight: bold; margin-bottom: 4px; }
.subtitle { font-size: 13px; color: #999; margin-bottom: 20px; }
.input {
  width: 100%; height: 40px; line-height: 1.2;
  border: 1px solid #e5e5e5; border-radius: 8px; padding: 10px 12px;
  font-size: 14px; background: #f9f9f9; box-sizing: border-box; margin-bottom: 10px;
}
.tip { font-size: 11px; color: #bbb; align-self: flex-start; margin-bottom: 4px; }
.error { font-size: 12px; color: #ef4444; margin-top: 4px; }
.login-btn {
  margin-top: 16px; background: #7c3aed; color: white;
  width: 100%; text-align: center; padding: 12px; border-radius: 8px;
  font-size: 15px; font-weight: bold;
}
.forgot-link { margin-top: 14px; font-size: 12px; color: #999; }
</style>
