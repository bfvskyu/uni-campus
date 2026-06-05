<template>
  <view class="container" v-if="item">
    <view class="card">
      <text class="title">{{ item.title }}</text>
      <view class="meta">
        <text class="tag" :style="{ background: tagColor(item.category) }">{{ item.category }}</text>
        <text class="meta-text">{{ item.publishDate }}</text>
        <text class="meta-text">{{ item.publisher }}</text>
      </view>
    </view>
    <view class="card content-card"><text class="content">{{ item.content }}</text></view>
  </view>
  <Skeleton type="detail" v-else />
</template>

<script setup>
import { ref } from 'vue'
import Skeleton from '../../components/Skeleton.vue'
import { onLoad } from '@dcloudio/uni-app'
import { api } from '../../api'

const item = ref(null)

onLoad(async (query) => {
  try { item.value = await api.announcement.detail(Number(query.id)) }
  catch (e) { console.error(e) }
})

const tagColor = (cat) => ({ '教务通知': '#0984e3', '服务通知': '#7c3aed', '保卫通知': '#e17055', '校园生活': '#fdcb6e', '技术通知': '#6c5ce7' }[cat] || '#0984e3')
</script>

<style>
.container { padding: 12px; }
.card { background: white; border-radius: 10px; padding: 14px; margin-bottom: 10px; }
.title { font-size: 17px; font-weight: bold; display: block; margin-bottom: 12px; line-height: 1.5; }
.meta { display: flex; align-items: center; gap: 10px; flex-wrap: wrap; }
.tag { font-size: 10px; color: white; padding: 2px 10px; border-radius: 4px; }
.meta-text { font-size: 11px; color: #999; }
.content-card { padding: 16px; }
.content { font-size: 14px; line-height: 2; color: #333; white-space: pre-wrap; display: block; }
.empty { text-align: center; padding: 60px; color: #999; }
</style>
