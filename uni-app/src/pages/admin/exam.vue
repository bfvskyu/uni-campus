<template>
  <view class="container">
    <view class="toolbar">
      <button class="add-btn" @click="openAdd">+ 新增考试</button>
    </view>

    <view v-if="loadError" class="empty error">{{ loadError }}</view>
    <view v-if="list.length === 0 && !loadError" class="empty">暂无考试信息</view>

    <view class="list">
      <view class="item" v-for="item in list" :key="item.id">
        <view class="item-row">
          <text class="item-title">{{ item.title }}</text>
          <text class="item-status" :class="'status-' + item.status">{{ item.status }}</text>
        </view>
        <view class="item-row sub">
          <text class="item-tag" :class="item.type === '竞赛' ? 'tag-competition' : 'tag-exam'">{{ item.type }}</text>
          <text class="item-meta">{{ item.examDate }}</text>
        </view>
        <view class="item-actions">
          <text class="act edit" @click="openEdit(item)">编辑</text>
          <text class="act delete" @click="handleDelete(item)">删除</text>
        </view>
      </view>
    </view>

    <!-- 弹窗 -->
    <view class="dialog-overlay" v-if="showDialog" @tap="onOverlayTap">
      <view class="dialog-box" @tap.stop>
        <text class="dialog-title">{{ isEdit ? '编辑考试' : '新增考试' }}</text>
        <textarea class="field" v-model="form.title" placeholder="标题" />
        <textarea class="field" v-model="form.type" placeholder="类型（考试/竞赛）" />
        <textarea class="field" v-model="form.examDate" placeholder="考试日期（yyyy-MM-dd）" />
        <textarea class="field" v-model="form.deadline" placeholder="截止日期（yyyy-MM-dd）" />
        <textarea class="field" v-model="form.status" placeholder="状态（报名中/即将开始/未开始）" />
        <textarea class="field" v-model="form.tags" placeholder="标签" />
        <textarea class="field" v-model="form.websiteUrl" placeholder="官网链接" />
        <textarea class="field textarea" v-model="form.detail" placeholder="详细信息" />
        <view class="dialog-btns">
          <view class="btn cancel" @click="showDialog = false">取消</view>
          <view class="btn confirm" @click="handleSave">保存</view>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { api } from '../../api'

const list = ref([])
const loadError = ref('')
const showDialog = ref(false)
const isEdit = ref(false)
const editingId = ref(null)
const form = ref({
  title: '', type: '', examDate: '', deadline: '', status: '',
  tags: '', detail: '', websiteUrl: ''
})

const onOverlayTap = (e) => {
  if (e.target === e.currentTarget) showDialog.value = false
}

const fetchList = async () => {
  loadError.value = ''
  try { list.value = await api.exam.list() }
  catch (e) { loadError.value = '加载失败：' + e.message }
}
onShow(fetchList)

const openAdd = () => {
  isEdit.value = false
  editingId.value = null
  form.value = { title: '', type: '', examDate: '', deadline: '', status: '', tags: '', detail: '', websiteUrl: '' }
  showDialog.value = true
}
const openEdit = (item) => {
  isEdit.value = true
  editingId.value = item.id
  form.value = {
    title: item.title, type: item.type, examDate: item.examDate, deadline: item.deadline,
    status: item.status, tags: item.tags, detail: item.detail, websiteUrl: item.websiteUrl
  }
  showDialog.value = true
}

const handleSave = async () => {
  try {
    if (isEdit.value) {
      await api.exam.update(editingId.value, form.value)
    } else {
      await api.exam.add(form.value)
    }
    showDialog.value = false
    uni.showToast({ title: '保存成功', icon: 'success' })
    fetchList()
  } catch (e) {
    uni.showToast({ title: e.message || '保存失败', icon: 'none' })
  }
}

const handleDelete = (item) => {
  uni.showModal({
    title: '确认删除',
    content: `确定删除「${item.title}」吗？`,
    success: async (res) => {
      if (res.confirm) {
        try {
          await api.exam.delete(item.id)
          uni.showToast({ title: '删除成功', icon: 'success' })
          fetchList()
        } catch (e) {
          uni.showToast({ title: e.message || '删除失败', icon: 'none' })
        }
      }
    }
  })
}
</script>

<style>
.container { padding: 12px; }
.toolbar { margin-bottom: 12px; }
.add-btn {
  background: #2563eb; color: white; border: none;
  border-radius: 8px; height: 40px; line-height: 40px; font-size: 14px;
}
.empty { text-align: center; color: #999; padding: 40px 0; font-size: 14px; }
.error { color: #ef4444; font-size: 13px; }
.list { display: flex; flex-direction: column; gap: 10px; }
.item {
  background: white; border-radius: 10px; padding: 14px;
  box-shadow: 0 1px 4px rgba(0,0,0,0.06);
}
.item-row { display: flex; align-items: center; gap: 8px; }
.item-row.sub { margin-top: 6px; }
.item-title { font-size: 15px; font-weight: bold; flex: 1; }
.item-status {
  font-size: 11px; padding: 2px 10px; border-radius: 10px;
  flex-shrink: 0;
}
.status-报名中 { background: #d1fae5; color: #059669; }
.status-即将开始 { background: #fef3c7; color: #d97706; }
.status-未开始 { background: #e5e5e5; color: #666; }
.item-tag {
  font-size: 11px; padding: 2px 8px; border-radius: 10px;
}
.tag-exam { background: #dbeafe; color: #2563eb; }
.tag-competition { background: #fce7f3; color: #ec4899; }
.item-meta { font-size: 12px; color: #888; }
.item-actions { display: flex; gap: 16px; margin-top: 8px; }
.act { font-size: 13px; padding: 4px 0; }
.act.edit { color: #3b82f6; }
.act.delete { color: #ef4444; }

.dialog-overlay {
  position: fixed; top: 0; left: 0; right: 0; bottom: 0;
  background: rgba(0,0,0,0.4); display: flex;
  align-items: center; justify-content: center; z-index: 999;
}
.dialog-box {
  background: white; border-radius: 14px; padding: 20px;
  width: 300px; max-height: 80vh; overflow-y: auto;
}
.dialog-title { font-size: 17px; font-weight: bold; display: block; text-align: center; margin-bottom: 14px; }
.field {
  border: 1px solid #e5e5e5; border-radius: 8px; padding: 10px 12px;
  font-size: 14px; width: 100%; box-sizing: border-box; margin-bottom: 10px;
  height: 40px; line-height: 1.2;
}
.textarea { height: 80px; }
.dialog-btns { display: flex; gap: 12px; margin-top: 4px; }
.btn {
  flex: 1; text-align: center; padding: 10px; border-radius: 8px;
  font-size: 14px; font-weight: bold;
}
.btn.cancel { background: #f5f5f5; color: #666; }
.btn.confirm { background: #2563eb; color: white; }
</style>
