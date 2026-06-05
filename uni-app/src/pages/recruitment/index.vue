<template>
  <view class="container">
    <view class="tab-bar">
      <view class="tab-item" :class="{ active: tab === 'shop' }" @click="switchTab('shop')">
        <text>🏪 店铺招聘</text>
      </view>
      <view class="tab-item" :class="{ active: tab === 'delivery' }" @click="switchTab('delivery')">
        <text>🛵 外送兼职</text>
      </view>
    </view>
    <view class="card" v-for="job in list" :key="job.id" @click="goDetail(job.id)">
      <view class="card-header">
        <text class="card-title">{{ job.title }}</text>
        <text class="card-salary">{{ job.salary }}</text>
      </view>
      <text class="card-shop">{{ job.shopName }}</text>
      <view class="card-tags">
        <text class="tag" v-for="t in (job.tags || '').split(',')" :key="t">{{ t }}</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { api } from '../../api'

const tab = ref('shop')
const list = ref([])

async function fetchData() {
  try { list.value = await api.recruitment.list(tab.value) }
  catch (e) { console.error(e) }
}

const switchTab = (t) => { tab.value = t; fetchData() }
const goDetail = (id) => uni.navigateTo({ url: '/pages/recruitment-detail/index?id=' + id })
onLoad(fetchData)
</script>

<style>
.container { padding: 12px; }
.tab-bar { display: flex; background: white; border-radius: 10px; overflow: hidden; margin-bottom: 12px; }
.tab-item { flex: 1; text-align: center; padding: 12px 0; font-size: 14px; color: #666; }
.tab-item.active { background: #7c3aed; color: white; font-weight: bold; }
.card { background: white; padding: 14px; border-radius: 10px; margin-bottom: 10px; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
.card-title { font-size: 15px; font-weight: bold; }
.card-salary { font-size: 14px; color: #e17055; font-weight: bold; }
.card-shop { font-size: 13px; color: #666; margin-top: 6px; display: block; }
.card-tags { display: flex; margin-top: 8px; gap: 8px; flex-wrap: wrap; }
.tag { font-size: 11px; background: #ede9fe; color: #7c3aed; padding: 2px 10px; border-radius: 6px; }
</style>
