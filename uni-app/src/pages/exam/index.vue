<template>
  <view class="container">
    <!-- 轮播图 -->
    <swiper
      class="swiper-box"
      :indicator-dots="true"
      indicator-color="#d5c4f0"
      indicator-active-color="#7c3aed"
      :autoplay="true"
      :interval="4000"
      :circular="true"
      v-if="swiperList.length">
      <swiper-item
        v-for="(item, i) in swiperList"
        :key="item.id"
        @click="goDetail(item.id)">
        <view class="slide" :style="{ background: gradients[i % gradients.length] }">
          <view class="slide-body">
            <text class="slide-type">{{ item.type }}</text>
            <text class="slide-title">{{ item.title }}</text>
            <view class="slide-meta">
              <text class="slide-date">📅 {{ item.examDate }}</text>
              <text class="slide-deadline">⏰ {{ item.deadline }} 截止</text>
            </view>
          </view>
          <text class="slide-status" :class="'status-' + statusKey(item.status)">{{ item.status }}</text>
        </view>
      </swiper-item>
    </swiper>

    <view class="tab-bar">
      <view class="tab-item" :class="{ active: tab === 'all' }" @click="switchTab('all')"><text>全部</text></view>
      <view class="tab-item" :class="{ active: tab === '考试' }" @click="switchTab('考试')"><text>📝 考试</text></view>
      <view class="tab-item" :class="{ active: tab === '竞赛' }" @click="switchTab('竞赛')"><text>🏆 竞赛</text></view>
    </view>
    <view class="card" v-for="item in list" :key="item.id" @click="goDetail(item.id)">
      <view class="card-top">
        <text class="card-title">{{ item.title }}</text>
        <text class="status" :class="statusClass(item.status)">{{ item.status }}</text>
      </view>
      <view class="card-meta">
        <text class="meta-item">📅 {{ item.examDate }}</text>
        <text class="meta-item">⏰ {{ item.deadline }} 截止</text>
      </view>
      <view class="card-tags">
        <text class="tag" v-for="t in (item.tags || '').split(',')" :key="t" :style="tagStyle(t)">{{ t }}</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, computed } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { api } from '../../api'

const tab = ref('all')
const list = ref([])

const swiperList = computed(() => list.value.slice(0, 4))

const gradients = [
  'linear-gradient(135deg, #BC8F8F, #B8A9C9)',
  'linear-gradient(135deg, #9CAF9F, #A5B8C9)',
  'linear-gradient(135deg, #D4A5A5, #C9A9C5)',
  'linear-gradient(135deg, #8FA5B5, #B8A9C9)',
]

const statusKey = (s) => ({ '报名中': 'registering', '即将开始': 'upcoming', '未开始': 'pending' }[s] || '')

const tagColors = [
  '#ede9fe', '#e8f4fd', '#fef3e2', '#e8faf5',
  '#fce8ef', '#f3e8ff', '#e0f7fa', '#fff0f6',
]

const tagStyle = (t) => ({ background: tagColors[hashCode(t) % tagColors.length] })

function hashCode(s) {
  let h = 0
  for (let i = 0; i < s.length; i++) h = ((h << 5) - h) + s.charCodeAt(i)
  return Math.abs(h)
}

async function fetchData() {
  try { list.value = await api.exam.list(tab.value === 'all' ? null : tab.value) }
  catch (e) { console.error(e) }
}

const switchTab = (t) => { tab.value = t; fetchData() }
const goDetail = (id) => uni.navigateTo({ url: '/pages/exam-detail/index?id=' + id })
const statusClass = (s) => ({ '报名中': 'registering', '即将开始': 'upcoming', '未开始': 'pending' }[s] || '')
onLoad(fetchData)
</script>

<style>
.container { padding: 12px; }

/* swiper */
.swiper-box { height: 360rpx; margin-bottom: 12px; border-radius: 12px; overflow: hidden; }
.slide { width: 100%; height: 100%; padding: 24px 20px; box-sizing: border-box; display: flex; flex-direction: column; justify-content: space-between; }
.slide-body { flex: 1; display: flex; flex-direction: column; justify-content: center; }
.slide-type { font-size: 11px; color: rgba(255,255,255,0.8); background: rgba(255,255,255,0.2); align-self: flex-start; padding: 2px 12px; border-radius: 10px; margin-bottom: 8px; }
.slide-title { font-size: 20px; font-weight: bold; color: #fff; line-height: 1.4; text-shadow: 0 1px 4px rgba(0,0,0,0.15); }
.slide-meta { display: flex; gap: 16px; margin-top: 10px; }
.slide-date, .slide-deadline { font-size: 12px; color: rgba(255,255,255,0.95); text-shadow: 0 1px 3px rgba(0,0,0,0.12); }
.slide-status { align-self: flex-end; font-size: 12px; padding: 4px 14px; border-radius: 12px; background: rgba(255,255,255,0.25); color: #fff; }
.slide-status.status-registering { background: rgba(255,255,255,0.35); color: #fff; }

/* tab-bar */
.tab-bar { display: flex; background: white; border-radius: 10px; overflow: hidden; margin-bottom: 12px; }
.tab-item { flex: 1; text-align: center; padding: 12px 0; font-size: 14px; color: #666; }
.tab-item.active { background: #7c3aed; color: white; font-weight: bold; }

/* card list */
.card { background: white; padding: 14px; border-radius: 10px; margin-bottom: 10px; }
.card-top { display: flex; justify-content: space-between; align-items: center; }
.card-title { font-size: 14px; font-weight: bold; flex: 1; margin-right: 10px; }
.status { font-size: 11px; padding: 4px 12px; border-radius: 12px; }
.status.registering { background: #ede9fe; color: #7c3aed; }
.status.upcoming { background: #fff3e0; color: #f39c12; }
.status.pending { background: #f0f0f0; color: #999; }
.card-meta { display: flex; gap: 16px; margin-top: 8px; }
.meta-item { font-size: 12px; color: #666; }
.card-tags { display: flex; margin-top: 8px; gap: 8px; flex-wrap: wrap; }
.tag { font-size: 11px; padding: 2px 10px; border-radius: 6px; white-space: nowrap; color: #444; }
</style>
