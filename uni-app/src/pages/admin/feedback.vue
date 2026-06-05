<template>
  <view class="container">
    <view v-if="list.length === 0" class="empty">暂无反馈</view>

    <view class="list">
      <view class="card" v-for="item in list" :key="item.id" :style="{ background: item.color }">
        <view class="card-status">
          <text class="status-badge" :class="item.status === 1 ? 'done' : 'pending'">
            {{ item.status === 1 ? '已处理' : '待处理' }}
          </text>
          <text class="card-time">{{ item.createTime?.substring(0, 10) }}</text>
        </view>
        <text class="card-title">{{ item.title }}</text>
        <text class="card-detail" v-if="item.detail">{{ item.detail }}</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { api } from '../../api'

const COLORS = [
  '#fce4ec', '#f3e5f5', '#e8eaf6', '#e0f2f1',
  '#fff3e0', '#f1f8e9', '#e1f5fe', '#fbe9e7'
]

const list = ref([])

const fetchList = async () => {
  try {
    const data = await api.feedback.list()
    list.value = (data || []).map((item, i) => ({
      ...item,
      color: COLORS[i % COLORS.length]
    }))
  } catch (e) { console.error(e) }
}
onShow(fetchList)
</script>

<style>
.container { padding: 12px; }
.empty { text-align: center; color: #999; padding: 40px 0; font-size: 14px; }
.list { display: flex; flex-direction: column; gap: 12px; }

.card {
  border-radius: 14px; padding: 16px;
  box-shadow: 0 1px 4px rgba(0,0,0,0.06);
}
.card-status {
  display: flex; align-items: center; gap: 8px; margin-bottom: 8px;
}
.status-badge {
  font-size: 11px; padding: 2px 10px; border-radius: 10px;
}
.pending { background: rgba(0,0,0,0.1); color: #666; }
.done { background: #d1fae5; color: #059669; }
.card-time { font-size: 11px; color: #999; }
.card-title { font-size: 15px; font-weight: bold; color: #333; display: block; margin-bottom: 4px; }
.card-detail { font-size: 13px; color: #666; line-height: 1.6; display: block; }
</style>
