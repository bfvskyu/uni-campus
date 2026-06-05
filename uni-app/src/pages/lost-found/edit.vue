<template>
  <view class="container">
    <view class="form">
      <view class="form-group">
        <text class="label">类型 *</text>
        <view class="type-select">
          <view class="type-option" :class="{ selected: form.type === 'lost' }" @click="form.type = 'lost'">寻物</view>
          <view class="type-option" :class="{ selected: form.type === 'found' }" @click="form.type = 'found'">拾物</view>
        </view>
      </view>

      <view class="form-group">
        <text class="label">标题 *</text>
        <textarea class="input" v-model="form.title" placeholder="请输入标题" />
      </view>

      <view class="form-group">
        <text class="label">详细描述 *</text>
        <textarea class="input textarea" v-model="form.description" placeholder="请详细描述物品特征、丢失/捡到的时间地点等" />
      </view>

      <view class="form-group">
        <text class="label">图片（最多2张）</text>
        <view class="image-list">
          <view class="image-item" v-for="(img, idx) in images" :key="idx">
            <image class="preview" :src="'http://localhost:8080' + img" mode="aspectFill" />
            <text class="image-del" @click="removeImage(idx)">×</text>
          </view>
          <view class="image-upload" v-if="images.length < 2" @click="chooseImage">
            <text class="upload-icon">+</text>
            <text class="upload-text">上传图片</text>
          </view>
        </view>
      </view>

      <view class="form-group">
        <text class="label">联系电话</text>
        <textarea class="input" v-model="form.phone" placeholder="选填" />
      </view>

      <view class="form-group">
        <text class="label">微信号</text>
        <textarea class="input" v-model="form.wechat" placeholder="选填" />
      </view>

      <view class="submit-btn" @click="submit">发布</view>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { api } from '../../api'

const form = ref({ type: 'lost', studentId: '', title: '', description: '', phone: '', wechat: '' })
const images = ref([])
const editId = ref(null)

// 从登录态自动获取学号
const user = uni.getStorageSync('login_user')
if (user && user.studentId) form.value.studentId = user.studentId

onLoad((query) => {
  if (query.id) {
    editId.value = query.id
    uni.setNavigationBarTitle({ title: '编辑信息' })
    loadDetail(query.id)
  }
})

const loadDetail = async (id) => {
  try {
    const data = await api.lostFound.detail(id)
    form.value = { ...form.value, type: data.type, title: data.title, description: data.description, phone: data.phone || '', wechat: data.wechat || '' }
    if (data.images) images.value = JSON.parse(data.images)
  } catch (e) {
    uni.showToast({ title: '加载失败', icon: 'none' })
  }
}

const chooseImage = () => {
  uni.chooseImage({
    count: 2 - images.value.length,
    success: async (res) => {
      uni.showLoading({ title: '上传中...' })
      try {
        for (const path of res.tempFilePaths) {
          const url = await api.lostFound.upload(path)
          images.value.push(url)
        }
      } catch (e) {
        uni.showToast({ title: '上传失败', icon: 'none' })
      } finally {
        uni.hideLoading()
      }
    }
  })
}

const removeImage = (idx) => {
  images.value.splice(idx, 1)
}

const submit = async () => {
  if (!form.value.title.trim()) return uni.showToast({ title: '请输入标题', icon: 'none' })
  if (!form.value.description.trim()) return uni.showToast({ title: '请输入描述', icon: 'none' })

  const data = { ...form.value, images: JSON.stringify(images.value) }
  try {
    if (editId.value) {
      await api.lostFound.update(editId.value, data)
      uni.showToast({ title: '修改成功' })
    } else {
      await api.lostFound.add(data)
      uni.showToast({ title: '发布成功' })
    }
    setTimeout(() => uni.navigateBack(), 500)
  } catch (e) {
    uni.showToast({ title: e.message || '操作失败', icon: 'none' })
  }
}
</script>

<style>
.container { padding: 12px; }

.form { background: white; border-radius: 12px; padding: 16px; box-shadow: 0 1px 6px rgba(0,0,0,0.06); }
.form-group { margin-bottom: 14px; }
.label { font-size: 13px; color: #333; font-weight: bold; display: block; margin-bottom: 6px; }

.input { width: 100%; border: 1px solid #e5e5e5; border-radius: 8px; padding: 10px 12px; font-size: 14px; background: #f9f9f9; box-sizing: border-box; }
.textarea { height: 100px; }

.type-select { display: flex; gap: 10px; }
.type-option { padding: 8px 24px; border-radius: 8px; border: 1px solid #e5e5e5; font-size: 14px; color: #666; }
.type-option.selected { background: #7c3aed; color: white; border-color: #7c3aed; }

.image-list { display: flex; gap: 10px; flex-wrap: wrap; }
.image-item { width: 100px; height: 100px; border-radius: 8px; overflow: hidden; position: relative; }
.preview { width: 100%; height: 100%; display: block; }
.image-del { position: absolute; top: 2px; right: 2px; width: 20px; height: 20px; background: rgba(0,0,0,0.5); color: white; border-radius: 50%; text-align: center; line-height: 20px; font-size: 14px; }
.image-upload { width: 100px; height: 100px; border: 2px dashed #e5e5e5; border-radius: 8px; display: flex; flex-direction: column; align-items: center; justify-content: center; }
.upload-icon { font-size: 28px; color: #ccc; }
.upload-text { font-size: 11px; color: #ccc; margin-top: 2px; }

.submit-btn { background: #7c3aed; color: white; text-align: center; padding: 12px; border-radius: 8px; font-size: 15px; font-weight: bold; margin-top: 10px; }
</style>
