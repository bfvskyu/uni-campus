<template>
  <view class="container">
    <view class="box">
      <text class="icon">🔐</text>
      <text class="title">找回密码</text>
      <text class="subtitle">验证身份后即可重置密码</text>

      <textarea class="input" v-model="studentId" placeholder="请输入学号" />
      <textarea class="input" v-model="phone" placeholder="请输入绑定的手机号" />

      <text class="error" v-if="error">{{ error }}</text>
      <view class="btn" @click="handleNext">下一步</view>
      <text class="back" @click="goBack">返回登录</text>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { api } from '../../api'

const studentId = ref('')
const phone = ref('')
const error = ref('')

const handleNext = async () => {
  error.value = ''
  if (!studentId.value.trim()) return error.value = '请输入学号'
  if (!phone.value.trim()) return error.value = '请输入绑定的手机号'

  try {
    await api.user.verifyPhone(studentId.value.trim(), phone.value.trim())
    uni.navigateTo({ url: '/pages/login/change-pwd?studentId=' + studentId.value.trim() })
  } catch (e) {
    error.value = e.message || '验证失败'
  }
}

const goBack = () => uni.navigateBack()
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
.title { font-size: 18px; font-weight: bold; margin-bottom: 4px; }
.subtitle { font-size: 12px; color: #999; margin-bottom: 20px; }
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
.back { margin-top: 12px; font-size: 12px; color: #999; }
</style>
