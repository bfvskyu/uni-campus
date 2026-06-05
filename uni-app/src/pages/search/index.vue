<template>
  <view class="container">
    <view class="search-bar">
      <input class="search-input" v-model="keyword" placeholder="搜索招聘、考试、公告..." @confirm="doSearch" />
      <view class="search-btn" @click="doSearch">搜索</view>
    </view>

    <view class="result" v-if="!loading && results.length">
      <template v-for="(items, type) in grouped" :key="type">
        <view class="group" v-if="items.length">
          <view class="group-header">{{ groupLabel(type) }}</view>
          <view class="item" v-for="item in items" :key="type + '-' + item.id" @click="goDetail(item)">
            <text class="item-title">{{ item.title }}</text>
            <text class="item-sub">{{ item.subInfo }}</text>
          </view>
        </view>
      </template>
    </view>

    <view class="empty" v-if="!loading && searched && !results.length">
      <text class="empty-icon">🔍</text>
      <text class="empty-text">没有找到 "{{ keyword }}" 相关的结果</text>
    </view>

    <view class="loading" v-if="loading">搜索中...</view>
  </view>
</template>

<script setup>
import { ref, computed } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { api } from '../../api'

const keyword = ref('')
const results = ref([])
const loading = ref(false)
const searched = ref(false)

onLoad((query) => {
  if (query.keyword) {
    keyword.value = query.keyword
    doSearch()
  }
})

const grouped = computed(() => {
  const map = { exam: [], recruitment: [], announcement: [] }
  for (const item of results.value) {
    if (map[item.type]) map[item.type].push(item)
  }
  return map
})

const groupLabel = (type) => ({
  exam: '📝 考试',
  recruitment: '💼 招聘',
  announcement: '📢 公告'
}[type] || type)

const doSearch = async () => {
  const kw = keyword.value.trim()
  if (!kw) {
    uni.showToast({ title: '请输入关键词', icon: 'none' })
    return
  }
  loading.value = true
  searched.value = true
  try {
    results.value = (await api.search(kw)) || []
  } catch (e) {
    results.value = []
    uni.showToast({ title: e.message || '搜索失败', icon: 'none' })
  } finally {
    loading.value = false
  }
}

const goDetail = (item) => {
  const pages = { exam: 'exam-detail', recruitment: 'recruitment-detail', announcement: 'announcement-detail' }
  const page = pages[item.type]
  if (page) uni.navigateTo({ url: `/pages/${page}/index?id=${item.id}` })
}
</script>

<style>
.container { padding: 12px; }

.search-bar { display: flex; gap: 10px; margin-bottom: 12px; }
.search-input {
  flex: 1; height: 40px; border: 1px solid #e5e5e5; border-radius: 8px;
  padding: 0 12px; font-size: 14px; background: white;
}
.search-btn {
  background: #7c3aed; color: white; padding: 0 20px; border-radius: 8px;
  font-size: 14px; line-height: 40px; font-weight: bold;
}

.group { background: white; border-radius: 12px; padding: 12px 16px; margin-bottom: 12px; box-shadow: 0 1px 6px rgba(0,0,0,0.06); }
.group-header { font-size: 14px; font-weight: bold; color: #333; margin-bottom: 8px; }
.item { padding: 10px 0; border-bottom: 1px solid #f0f0f0; }
.item:last-child { border-bottom: none; }
.item-title { font-size: 14px; color: #333; display: block; }
.item-sub { font-size: 12px; color: #999; display: block; margin-top: 2px; }

.empty { display: flex; flex-direction: column; align-items: center; padding: 60px 0; }
.empty-icon { font-size: 40px; }
.empty-text { font-size: 13px; color: #999; margin-top: 12px; }

.loading { text-align: center; padding: 40px; color: #999; font-size: 13px; }
</style>
