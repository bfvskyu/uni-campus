<template>
  <view class="container">
    <view class="header">
      <view class="tabs">
        <view class="tab" :class="{ active: !filterType }" @click="filterType = ''">全部</view>
        <view class="tab" :class="{ active: filterType === 'lost' }" @click="filterType = 'lost'">寻物</view>
        <view class="tab" :class="{ active: filterType === 'found' }" @click="filterType = 'found'">拾物</view>
      </view>
      <view class="add-btn" @click="goAdd">+</view>
    </view>

    <view class="list" v-if="!loading && list.length">
      <view class="card" v-for="item in list" :key="item.id" @click="goDetail(item.id)">
        <view class="card-top">
          <text class="type-tag" :class="'tag-' + item.type">{{ item.type === 'lost' ? '寻物' : '拾物' }}</text>
          <text class="card-title">{{ item.title }}</text>
        </view>
        <text class="card-desc">{{ item.description }}</text>
        <view class="card-meta">
          <text class="meta-item">👤 {{ item.authorName || '匿名' }}</text>
          <text class="meta-item">📱 {{ item.phone || '未留' }}</text>
        </view>
        <text class="card-time">{{ item.createTime ? item.createTime.slice(0, 10) : '' }}</text>
      </view>
    </view>

    <!-- 分页 -->
    <view class="pagination" v-if="totalPages > 1">
      <view class="page-btn" :class="{ disabled: currentPage <= 1 }" @click="goPage(currentPage - 1)">‹</view>
      <view class="page-info">
        <textarea class="page-input" v-model="jumpPage" @confirm="jump" @blur="jump" />
        <text class="page-total">/ {{ totalPages }}</text>
      </view>
      <view class="page-btn" :class="{ disabled: currentPage >= totalPages }" @click="goPage(currentPage + 1)">›</view>
    </view>

    <view class="empty" v-if="!loading && !list.length">
      <text class="empty-icon">🔍</text>
      <text class="empty-text">暂无相关信息</text>
    </view>

    <view class="loading" v-if="loading">加载中...</view>
  </view>
</template>

<script setup>
import { ref, watch } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { api } from '../../api'

const list = ref([])
const loading = ref(false)
const filterType = ref('')
const currentPage = ref(1)
const totalPages = ref(1)
const total = ref(0)
const jumpPage = ref('')

const load = async () => {
  loading.value = true
  try {
    const res = await api.lostFound.list(filterType.value || null, currentPage.value)
    list.value = res.records || []
    currentPage.value = res.current || 1
    totalPages.value = res.pages || 1
    total.value = res.total || 0
    jumpPage.value = ''
  } catch (e) {
    list.value = []
  } finally {
    loading.value = false
  }
}

const goPage = (p) => {
  if (p < 1 || p > totalPages.value) return
  currentPage.value = p
  load()
}

const jump = () => {
  const p = parseInt(jumpPage.value)
  if (p && p >= 1 && p <= totalPages.value) {
    currentPage.value = p
    load()
  }
}

onShow(() => { currentPage.value = 1; load() })
watch(filterType, () => { currentPage.value = 1; load() })

const goDetail = (id) => uni.navigateTo({ url: `/pages/lost-found-detail/index?id=${id}` })
const goAdd = () => uni.navigateTo({ url: '/pages/lost-found/edit' })
</script>

<style>
.container { padding: 12px; }

.header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 12px; }
.tabs { display: flex; gap: 8px; }
.tab { font-size: 14px; color: #666; padding: 6px 16px; border-radius: 16px; background: white; }
.tab.active { background: #7c3aed; color: white; font-weight: bold; }
.add-btn {
  width: 40px; height: 40px; border: 2px solid #7c3aed; border-radius: 8px;
  display: flex; align-items: center; justify-content: center;
  font-size: 24px; color: #7c3aed; font-weight: bold;
}

.card { background: white; border-radius: 12px; padding: 14px 16px; margin-bottom: 10px; box-shadow: 0 1px 6px rgba(0,0,0,0.06); }
.card-top { display: flex; align-items: center; gap: 8px; margin-bottom: 6px; }
.type-tag { font-size: 11px; padding: 2px 8px; border-radius: 4px; color: white; flex-shrink: 0; }
.tag-lost { background: #f59e0b; }
.tag-found { background: #10b981; }
.card-title { font-size: 15px; font-weight: bold; color: #333; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.card-desc { font-size: 13px; color: #888; display: block; line-height: 1.5; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; margin-bottom: 8px; }
.card-meta { display: flex; gap: 16px; margin-bottom: 4px; }
.meta-item { font-size: 12px; color: #999; }
.card-time { font-size: 11px; color: #ccc; }

.pagination {
  display: flex; align-items: center; justify-content: center; gap: 8px;
  padding: 16px 0;
}
.page-btn {
  width: 36px; height: 36px; border: 1px solid #ddd; border-radius: 8px;
  display: flex; align-items: center; justify-content: center;
  font-size: 18px; color: #333; background: white;
}
.page-btn.disabled { opacity: 0.3; }
.page-info { display: flex; align-items: center; gap: 2px; font-size: 14px; color: #333; }
.page-input {
  width: 36px; height: 36px; border: 1px solid #ddd; border-radius: 6px;
  text-align: center; font-size: 14px; padding: 0; line-height: 36px;
  background: white;
}
.page-total { font-size: 14px; color: #999; }

.empty { display: flex; flex-direction: column; align-items: center; padding: 60px 0; }
.empty-icon { font-size: 40px; }
.empty-text { font-size: 13px; color: #999; margin-top: 12px; }
.loading { text-align: center; padding: 40px; color: #999; font-size: 13px; }
</style>
