<template>
  <view class="container">
    <!-- 学期概览 -->
    <view class="header-card">
      <text class="semester-title">2025-2026学年 第二学期</text>
      <text class="semester-range">2026年3月1日 — 2026年6月30日</text>
      <view class="progress-bar">
        <view class="progress-fill" :style="{ width: progress + '%' }" />
      </view>
      <text class="progress-text">学期进度 {{ progress }}%</text>
    </view>

    <!-- 暑假预告 -->
    <view class="summer-card">
      <text class="summer-icon">🏖️</text>
      <view class="summer-info">
        <text class="summer-title">暑假倒计时</text>
        <text class="summer-countdown">{{ summerDays }} 天</text>
      </view>
    </view>

    <!-- 月份时间线 -->
    <view class="timeline">
      <view class="month-section" v-for="month in months" :key="month.name">
        <view class="month-header">
          <text class="month-name">{{ month.name }}</text>
          <text class="month-desc">{{ month.desc }}</text>
        </view>

        <view
          class="event-card"
          :class="event.type"
          v-for="event in month.events"
          :key="event.date"
        >
          <view class="event-left">
            <text class="event-date">{{ event.date.split('-')[2] }}</text>
            <text class="event-weekday">{{ event.weekday }}</text>
          </view>
          <view class="event-right">
            <text class="event-title">{{ event.title }}</text>
            <text class="event-detail">{{ event.detail }}</text>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { computed } from 'vue'

const today = new Date()
const semStart = new Date(2026, 2, 1)  // Mar 1
const semEnd = new Date(2026, 5, 30)   // Jun 30
const summerStart = new Date(2026, 6, 1) // Jul 1

const totalDays = (semEnd - semStart) / (1000 * 60 * 60 * 24)
const passedDays = Math.max(0, Math.min(totalDays, (today - semStart) / (1000 * 60 * 60 * 24)))
const progress = Math.min(100, Math.round((passedDays / totalDays) * 100))

const summerDays = Math.max(0, Math.ceil((summerStart - today) / (1000 * 60 * 60 * 24)))

const months = [
  {
    name: '3月', desc: '开学季',
    events: [
      { date: '2026-03-01', weekday: '日', title: '新学期开始', detail: '正式上课', type: 'school' },
      { date: '2026-03-05', weekday: '四', title: '开学第一周', detail: '选课补退选截止', type: 'school' },
    ]
  },
  {
    name: '4月', desc: '期中月',
    events: [
      { date: '2026-04-04', weekday: '六', title: '清明节', detail: '4月4日-6日放假，共3天', type: 'holiday' },
      { date: '2026-04-06', weekday: '一', title: '清明调休', detail: '与周末连休', type: 'holiday' },
      { date: '2026-04-13', weekday: '一', title: '期中考试周', detail: '数据结构与算法 期中测验', type: 'exam' },
      { date: '2026-04-15', weekday: '三', title: '期中考试', detail: 'Java企业级开发 期中项目提交', type: 'exam' },
      { date: '2026-04-17', weekday: '五', title: '期中考试', detail: '数据库原理 期中测验', type: 'exam' },
      { date: '2026-04-20', weekday: '一', title: '课程设计启动', detail: '软件工程课程设计 分组选题', type: 'school' },
    ]
  },
  {
    name: '5月', desc: '竞赛与实训',
    events: [
      { date: '2026-05-01', weekday: '五', title: '劳动节', detail: '5月1日-5日放假调休，共5天', type: 'holiday' },
      { date: '2026-05-04', weekday: '一', title: '五四青年节', detail: '校园文化活动周', type: 'school' },
      { date: '2026-05-10', weekday: '日', title: '课程设计中期检查', detail: '软件工程课程设计 中期答辩', type: 'exam' },
      { date: '2026-05-15', weekday: '五', title: '大作业截止', detail: '操作系统 大作业提交', type: 'exam' },
      { date: '2026-05-20', weekday: '三', title: '实训周', detail: '校内实训项目启动', type: 'school' },
    ]
  },
  {
    name: '6月', desc: '期末冲刺',
    events: [
      { date: '2026-06-08', weekday: '一', title: '端午节', detail: '6月8日-10日放假，共3天', type: 'holiday' },
      { date: '2026-06-10', weekday: '三', title: '端午调休', detail: '与周末连休', type: 'holiday' },
      { date: '2026-06-15', weekday: '一', title: '四六级考试', detail: '大学英语四六级笔试', type: 'exam' },
      { date: '2026-06-20', weekday: '六', title: '课程设计终期答辩', detail: '软件工程课程设计 终期演示', type: 'exam' },
      { date: '2026-06-22', weekday: '一', title: '期末考试周', detail: '全学期课程期末考试', type: 'exam' },
      { date: '2026-06-22', weekday: '一', title: 'Java考试', detail: 'Java企业级开发 期末', type: 'exam' },
      { date: '2026-06-24', weekday: '三', title: '数据结构考试', detail: '数据结构与算法 期末', type: 'exam' },
      { date: '2026-06-26', weekday: '五', title: '数据库考试', detail: '数据库原理 期末', type: 'exam' },
      { date: '2026-06-28', weekday: '日', title: '软件工程考试', detail: '软件工程 期末', type: 'exam' },
      { date: '2026-06-30', weekday: '二', title: '学期结束', detail: '本学期教学任务完成', type: 'school' },
    ]
  },
]
</script>

<style>
.container { padding: 12px; }

.header-card {
  background: linear-gradient(135deg, #7c3aed, #3b82f6);
  border-radius: 14px; padding: 20px; color: white; margin-bottom: 12px;
}
.semester-title { font-size: 17px; font-weight: bold; display: block; }
.semester-range { font-size: 12px; opacity: 0.8; display: block; margin-top: 4px; }
.progress-bar {
  height: 6px; background: rgba(255,255,255,0.3); border-radius: 3px;
  margin-top: 12px; overflow: hidden;
}
.progress-fill {
  height: 100%; background: white; border-radius: 3px;
  transition: width 0.5s;
}
.progress-text { font-size: 11px; opacity: 0.8; display: block; text-align: right; margin-top: 4px; }

.summer-card {
  background: linear-gradient(135deg, #f59e0b, #f97316);
  border-radius: 14px; padding: 16px 20px; color: white; margin-bottom: 12px;
  display: flex; align-items: center; gap: 12px;
}
.summer-icon { font-size: 28px; }
.summer-info { flex: 1; }
.summer-title { font-size: 15px; font-weight: bold; display: block; }
.summer-countdown { font-size: 24px; font-weight: bold; display: block; margin-top: 2px; }

.timeline { display: flex; flex-direction: column; gap: 16px; }

.month-section { }
.month-header {
  display: flex; align-items: baseline; gap: 8px;
  margin-bottom: 8px; padding-left: 4px;
}
.month-name {
  font-size: 18px; font-weight: bold; color: #333;
}
.month-desc { font-size: 12px; color: #999; }

.event-card {
  background: white; border-radius: 12px; padding: 12px 14px;
  margin-bottom: 8px; display: flex; gap: 12px;
  border-left: 4px solid #ccc; box-shadow: 0 1px 4px rgba(0,0,0,0.05);
}
.event-left { width: 36px; text-align: center; flex-shrink: 0; }
.event-date { font-size: 20px; font-weight: bold; color: #333; display: block; line-height: 1.2; }
.event-weekday { font-size: 11px; color: #999; display: block; }
.event-right { flex: 1; }
.event-title { font-size: 14px; font-weight: bold; color: #333; display: block; }
.event-detail { font-size: 12px; color: #888; display: block; margin-top: 2px; }

.event-card.holiday { border-left-color: #f59e0b; }
.event-card.exam { border-left-color: #ef4444; }
.event-card.school { border-left-color: #3b82f6; }
</style>
