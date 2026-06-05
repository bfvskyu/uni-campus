<template>
  <view class="container">
    <!-- 顶部操作栏 -->
    <view class="toolbar">
      <text class="toolbar-title">备忘录 ({{ list.length }})</text>
      <text v-if="!editing" class="toolbar-btn" @click="enterEdit">管理</text>
      <view v-else class="toolbar-actions">
        <text class="toolbar-btn" @click="selectAll">{{ allSelected ? '取消全选' : '全选' }}</text>
        <text class="toolbar-btn danger" @click="batchDelete">删除 ({{ selected.size }})</text>
        <text class="toolbar-btn" @click="exitEdit">完成</text>
      </view>
    </view>

    <!-- 列表 -->
    <view class="empty" v-if="!list.length"><text>暂无备忘录</text></view>
    <view class="card" v-for="item in list" :key="item.id" @click="editing ? toggle(item.id) : goDetail(item)" @longpress="enterEdit">
      <view class="card-left">
        <view class="checkbox" v-if="editing" :class="{ checked: selected.has(item.id) }">
          <text v-if="selected.has(item.id)">✓</text>
        </view>
        <text class="type-tag" :class="item.refType">{{ item.refType === 'recruitment' ? '招聘' : '考试' }}</text>
      </view>
      <view class="card-body">
        <text class="card-title">{{ item.title }}</text>
        <text class="card-time">{{ item.createTime }}</text>
      </view>
      <text class="card-arrow">›</text>
    </view>
  </view>
</template>

<script setup>
import { ref, computed } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { api } from '../../api'

const list = ref([])
const editing = ref(false)
const selected = ref(new Set())
const myStudentId = ref('')

const allSelected = computed(() => list.value.length > 0 && selected.value.size === list.value.length)

onShow(() => {
  const user = uni.getStorageSync('login_user')
  if (user && user.studentId) myStudentId.value = user.studentId
  fetchData()
})

async function fetchData() {
  if (!myStudentId.value) return
  try { list.value = await api.memo.list(myStudentId.value) }
  catch (e) { console.error(e) }
}

const enterEdit = () => { editing.value = true }
const exitEdit = () => { editing.value = false; selected.value = new Set() }
const toggle = (id) => {
  const s = new Set(selected.value)
  s.has(id) ? s.delete(id) : s.add(id)
  selected.value = s
}
const selectAll = () => {
  if (allSelected.value) { selected.value = new Set(); return }
  selected.value = new Set(list.value.map(i => i.id))
}

const batchDelete = async () => {
  if (!selected.value.size) return
  try {
    await api.memo.batchDelete([...selected.value])
    uni.showToast({ title: `已删除 ${selected.value.size} 条`, icon: 'success' })
    exitEdit()
    fetchData()
  } catch (e) {
    uni.showToast({ title: '删除失败', icon: 'none' })
  }
}

const goDetail = (item) => {
  const prefix = item.refType === 'recruitment' ? 'recruitment-detail' : 'exam-detail'
  uni.navigateTo({ url: `/pages/${prefix}/index?id=${item.refId}` })
}
</script>

<style>
.container { padding: 12px; }

.toolbar { display: flex; align-items: center; margin-bottom: 12px; }
.toolbar-title { font-size: 16px; font-weight: bold; flex: 1; }
.toolbar-btn { font-size: 13px; color: #7c3aed; margin-left: 12px; }
.toolbar-btn.danger { color: #e74c3c; }
.toolbar-actions { display: flex; }

.card {
  background: white; border-radius: 10px; padding: 14px;
  margin-bottom: 10px; display: flex; align-items: center;
}
.card-left { display: flex; align-items: center; gap: 10px; }
.checkbox {
  width: 20px; height: 20px; border-radius: 50%; border: 2px solid #ccc;
  display: flex; align-items: center; justify-content: center; flex-shrink: 0;
}
.checkbox.checked { background: #7c3aed; border-color: #7c3aed; }
.checkbox.checked text { color: white; font-size: 12px; font-weight: bold; }
.type-tag {
  font-size: 11px; padding: 3px 10px; border-radius: 6px; flex-shrink: 0;
}
.type-tag.recruitment { background: #ede9fe; color: #7c3aed; }
.type-tag.exam { background: #fff3e0; color: #e17055; }
.card-body { flex: 1; margin: 0 12px; }
.card-title { font-size: 14px; color: #333; display: block; margin-bottom: 4px; }
.card-time { font-size: 11px; color: #999; display: block; }
.card-arrow { font-size: 16px; color: #ccc; }
.empty { text-align: center; padding: 60px; color: #999; }
</style>
