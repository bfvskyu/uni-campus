<template>
  <view class="container">
    <view class="login-box">
      <text class="icon">🔑</text>
      <text class="title">管理员验证</text>
      <text class="desc">请输入管理员密钥</text>
      <textarea class="input" v-model="secretKey" placeholder="请输入密钥" :style="{ height: '42px' }" />
      <text class="error" v-if="error">{{ error }}</text>
      <view class="btn" @click="handleLogin">确认</view>
    </view>
  </view>
</template>

<script setup>
import { ref, nextTick } from 'vue'
import { onReady } from '@dcloudio/uni-app'
import { api } from '../../api'

const secretKey = ref('')
const error = ref('')

const handleLogin = async () => {
  error.value = ''
  if (!secretKey.value.trim()) {
    error.value = '请输入密钥'
    return
  }
  try {
    const res = await api.admin.validateKey(secretKey.value.trim())
    if (res.valid) {
      uni.setStorageSync('admin_key', secretKey.value.trim())
      uni.redirectTo({ url: '/pages/admin/index' })
    } else {
      error.value = '密钥无效或已过期'
    }
  } catch (e) {
    error.value = e.message || '验证失败'
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
.icon { font-size: 40px; margin-bottom: 12px; }
.title { font-size: 18px; font-weight: bold; margin-bottom: 6px; }
.desc { font-size: 13px; color: #888; margin-bottom: 20px; }
.input {
  border: 1px solid #e5e5e5; border-radius: 8px; padding: 12px;
  font-size: 14px; width: 100%; box-sizing: border-box;
  background: #f9f9f9;
}
.error { color: #ef4444; font-size: 12px; margin-top: 10px; }
.btn {
  margin-top: 20px; background: #7c3aed; color: white;
  width: 100%; text-align: center; padding: 12px; border-radius: 8px;
  font-size: 15px; font-weight: bold;
}
</style>
