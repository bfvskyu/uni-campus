<template>
  <view class="container">
    <view class="banner">
      <text class="banner-title">大学生服务中心</text>
      <text class="banner-sub">服务校园 · 方便你我</text>
    </view>

    <view class="search-box" @click="goSearch">
      <text class="search-placeholder">🔍 搜索招聘、考试、公告...</text>
    </view>

    <view class="grid-section">
      <view class="grid-row">
        <view class="grid-item" @click="toPage('recruitment')">
          <text class="grid-icon">💼</text>
          <text class="grid-label">校园招聘</text>
        </view>
        <view class="grid-item" @click="goLostFound">
          <text class="grid-icon">📦</text>
          <text class="grid-label">失物招领</text>
        </view>
        <view class="grid-item" @click="toPage('announcement')">
          <text class="grid-icon">📢</text>
          <text class="grid-label">校园公告</text>
        </view>
        <view class="grid-item" @click="toPage('exam')">
          <text class="grid-icon">📝</text>
          <text class="grid-label">考试竞赛</text>
        </view>
      </view>
      <view class="grid-separator"></view>
      <view class="grid-row calendar-row">
        <view class="grid-item calendar-item" @click="toCalendar">
          <text class="grid-icon">📅</text>
          <text class="grid-label">校历</text>
        </view>
      </view>
    </view>

    <view class="section" v-if="latestNotices.length">
      <view class="section-header">
        <text class="section-title">📢 最新公告</text>
        <text class="section-more" @click="toPage('announcement')">更多 ›</text>
      </view>
      <view class="notice-list">
        <view class="notice-item" v-for="item in latestNotices" :key="item.id" @click="goNotice(item.id)">
          <text class="notice-tag" :style="{ background: tagColor(item.category) }">{{ item.category }}</text>
          <text class="notice-text">{{ item.title }}</text>
          <text class="notice-date">{{ item.publishDate }}</text>
        </view>
      </view>
    </view>

    <view class="section" v-if="hotJobs.length">
      <view class="section-header">
        <text class="section-title">🔥 热门招聘</text>
        <text class="section-more" @click="toPage('recruitment')">更多 ›</text>
      </view>
      <view class="job-card" v-for="job in hotJobs" :key="job.id" @click="goJob(job.id)">
        <view class="job-card-header">
          <text class="job-title">{{ job.title }}</text>
          <text class="job-salary">{{ job.salary }}</text>
        </view>
        <text class="job-shop">{{ job.shopName }}</text>
        <view class="job-tags">
          <text class="tag" v-for="t in (job.tags || '').split(',')" :key="t">{{ t }}</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { api } from '../../api'

const latestNotices = ref([])
const hotJobs = ref([])

onLoad(async () => {
  try {
    const [notices, jobs] = await Promise.all([
      api.announcement.list(),
      api.recruitment.list('shop')
    ])
    latestNotices.value = (notices || []).slice(0, 3)
    hotJobs.value = (jobs || []).slice(0, 3)
  } catch (e) {
    console.error('加载首页数据失败', e)
  }
})

const tagColor = (cat) => ({ '教务通知': '#0984e3', '服务通知': '#7c3aed', '保卫通知': '#e17055', '校园生活': '#fdcb6e', '技术通知': '#6c5ce7' }[cat] || '#0984e3')

const toPage = (page) => {
  const tabs = ['recruitment', 'exam', 'profile']
  tabs.includes(page) ? uni.switchTab({ url: '/pages/' + page + '/index' }) : uni.navigateTo({ url: '/pages/' + page + '/index' })
}
const goSearch = () => uni.navigateTo({ url: '/pages/search/index' })
const goLostFound = () => uni.navigateTo({ url: '/pages/lost-found/index' })
const goNotice = (id) => uni.navigateTo({ url: '/pages/announcement-detail/index?id=' + id })
const goJob = (id) => uni.navigateTo({ url: '/pages/recruitment-detail/index?id=' + id })
const toCalendar = () => uni.navigateTo({ url: '/pages/profile/calendar' })
</script>

<style>
.container { padding: 0 0 20px; }
.banner { background: linear-gradient(135deg, #7c3aed, #3b82f6); padding: 30px 20px 24px; color: white; }
.banner-title { font-size: 22px; font-weight: bold; display: block; }
.banner-sub { font-size: 13px; opacity: 0.9; margin-top: 4px; display: block; }
.search-box { background: white; margin: 8px 12px 0; padding: 10px 14px; border-radius: 10px; box-shadow: 0 1px 6px rgba(0,0,0,0.06); }
.search-placeholder { font-size: 13px; color: #bbb; }
.grid-section { background: white; padding: 16px 10px; margin: 12px; border-radius: 12px; box-shadow: 0 1px 6px rgba(0,0,0,0.06); }
.grid-row { display: flex; justify-content: space-around; }
.grid-item { display: flex; flex-direction: column; align-items: center; width: 20%; }
.grid-icon { font-size: 26px; }
.grid-label { font-size: 12px; color: #333; margin-top: 6px; }
.grid-separator { height: 1px; background: #f0f0f0; margin: 12px 4px; }
.calendar-item { width: auto; padding: 0 20px; }
.section { margin: 0 12px 12px; background: white; border-radius: 12px; padding: 14px 16px; box-shadow: 0 1px 6px rgba(0,0,0,0.06); }
.section-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 12px; }
.section-title { font-size: 15px; font-weight: bold; }
.section-more { font-size: 12px; color: #7c3aed; }
.notice-item { display: flex; align-items: center; padding: 10px 0; border-bottom: 1px solid #f0f0f0; }
.notice-item:last-child { border-bottom: none; }
.notice-tag { font-size: 10px; padding: 2px 8px; border-radius: 4px; color: white; margin-right: 10px; }
.notice-text { flex: 1; font-size: 13px; color: #333; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.notice-date { font-size: 11px; color: #999; margin-left: 10px; }
.job-card { padding: 12px 0; border-bottom: 1px solid #f0f0f0; }
.job-card:last-child { border-bottom: none; }
.job-card-header { display: flex; justify-content: space-between; align-items: center; }
.job-title { font-size: 14px; font-weight: bold; }
.job-salary { font-size: 13px; color: #e17055; font-weight: bold; }
.job-shop { font-size: 12px; color: #666; margin-top: 4px; display: block; }
.job-tags { display: flex; margin-top: 8px; gap: 8px; flex-wrap: wrap; }
.tag { font-size: 11px; background: #ede9fe; color: #7c3aed; padding: 2px 10px; border-radius: 6px; }
</style>
