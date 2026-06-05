<template>
  <view class="container">
    <view class="form">
      <view class="form-item">
        <text class="label">分类</text>
        <picker :range="categoryOptions" range-key="label" @change="onCategoryChange">
          <view class="picker">{{ selectedCategory.label || '请选择分类' }}</view>
        </picker>
      </view>

      <view class="form-item">
        <text class="label">标题</text>
        <input class="input" v-model="form.title" placeholder="请输入标题（10字以内）" maxlength="10" />
      </view>

      <view class="form-item">
        <text class="label">内容</text>
        <textarea class="textarea" v-model="form.content" placeholder="请输入内容" maxlength="500" />
      </view>

      <view class="form-item" v-if="selectedCategory.value === 'lost_found'">
        <text class="label">联系方式</text>
        <input class="input" v-model="form.phone" placeholder="选填，方便失主联系你" />
      </view>
    </view>

    <view class="submit-btn" :class="{ disabled: submitting }" @click="submit">
      {{ submitting ? '发布中...' : '发布' }}
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { api } from '../../api'

const categoryOptions = [
  { label: '求助', value: 'help' },
  { label: '讨论', value: 'discuss' },
  { label: '失物招领', value: 'lost_found' },
  { label: '公告', value: 'notice' }
]

const selectedCategory = ref({ label: '', value: '' })
const form = ref({ title: '', content: '', phone: '' })
const submitting = ref(false)

const getStudentId = () => {
  try {
    const user = uni.getStorageSync('login_user')
    return user?.studentId || ''
  } catch { return '' }
}

const onCategoryChange = (e) => {
  selectedCategory.value = categoryOptions[e.detail.value]
}

const submit = async () => {
  if (!selectedCategory.value.value) return uni.showToast({ title: '请选择分类', icon: 'none' })
  if (!form.value.title.trim()) return uni.showToast({ title: '请输入标题', icon: 'none' })
  if (!form.value.content.trim()) return uni.showToast({ title: '请输入内容', icon: 'none' })
  const sid = getStudentId()
  if (!sid) return uni.showToast({ title: '请先登录', icon: 'none' })

  submitting.value = true
  try {
    // 敏感词检测
    const checkText = form.value.title.trim() + form.value.content.trim()
    const checkRes = await api.sensitive.check(checkText)
    if (checkRes.hasSensitive) {
      uni.showToast({ title: `含有违禁词「${checkRes.word}」，请修改`, icon: 'none' })
      submitting.value = false
      return
    }
    await api.post.create({
      category: selectedCategory.value.value,
      title: form.value.title.trim(),
      content: form.value.content.trim(),
      phone: form.value.phone.trim() || null
    }, sid)
    uni.showToast({ title: '发布成功', icon: 'success' })
    setTimeout(() => uni.navigateBack(), 1000)
  } catch (e) {
    uni.showToast({ title: e.message || '发布失败', icon: 'none' })
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.container { padding: 16px; background: #f5f6fa; min-height: 100vh; }
.form { background: #fff; border-radius: 12px; padding: 16px; }
.form-item { margin-bottom: 16px; }
.label { font-size: 14px; font-weight: bold; color: #333; margin-bottom: 8px; display: block; }
.input {
  width: 100%; height: 40px; border: 1px solid #eee; border-radius: 8px;
  padding: 0 12px; font-size: 14px; box-sizing: border-box;
}
.textarea {
  width: 100%; height: 150px; border: 1px solid #eee; border-radius: 8px;
  padding: 10px 12px; font-size: 14px; box-sizing: border-box;
}
.picker {
  height: 40px; line-height: 40px; border: 1px solid #eee; border-radius: 8px;
  padding: 0 12px; font-size: 14px; color: #666;
}
.submit-btn {
  margin-top: 20px; height: 44px; border-radius: 22px;
  background: #7c3aed; color: #fff; font-size: 16px; font-weight: bold;
  display: flex; align-items: center; justify-content: center;
}
.submit-btn.disabled { opacity: 0.5; }
</style>
