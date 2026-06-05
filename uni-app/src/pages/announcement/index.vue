<template>
  <view class="container">
    <view class="card" v-for="item in list" :key="item.id" @click="goDetail(item.id)">
      <view class="card-header">
        <text class="tag" :style="{ background: tagColor(item.category) }">{{ item.category }}</text>
        <text class="date">{{ item.publishDate }}</text>
      </view>
      <text class="card-title">{{ item.title }}</text>
      <text class="card-desc">{{ (item.content || '').slice(0, 80) }}...</text>
      <text class="publisher">{{ item.publisher }}</text>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { api } from '../../api'

const list = ref([])

onLoad(async () => {
  try { list.value = await api.announcement.list() }
  catch (e) { console.error(e) }
})

const goDetail = (id) => uni.navigateTo({ url: '/pages/announcement-detail/index?id=' + id })
const tagColor = (cat) => ({ '教务通知': '#0984e3', '服务通知': '#7c3aed', '保卫通知': '#e17055', '校园生活': '#fdcb6e', '技术通知': '#6c5ce7' }[cat] || '#0984e3')
</script>

<style>
.container { padding: 12px; }
.card { background: white; border-radius: 10px; padding: 14px; margin-bottom: 10px; }
.card-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 8px; }
.tag { font-size: 10px; color: white; padding: 2px 10px; border-radius: 4px; }
.date { font-size: 11px; color: #999; }
.card-title { font-size: 15px; font-weight: bold; display: block; margin-bottom: 6px; }
.card-desc { font-size: 13px; color: #666; line-height: 1.5; display: block; }
.publisher { font-size: 11px; color: #aaa; margin-top: 6px; display: block; }
</style>
