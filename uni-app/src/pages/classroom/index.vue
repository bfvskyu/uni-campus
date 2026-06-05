<template>
  <view class="container">
    <scroll-view scroll-x class="scroll-x">
      <view class="building-item" :class="{ active: activeB === b }" v-for="b in buildings" :key="b" @click="activeB = b">
        <text>{{ b }}</text>
      </view>
    </scroll-view>

    <view class="slot-bar">
      <view class="slot-item" :class="{ active: activeSlot === s.id }" v-for="s in slots" :key="s.id" @click="activeSlot = s.id">
        <text class="slot-label">{{ s.label }}</text>
        <text class="slot-time">{{ s.time }}</text>
      </view>
    </view>

    <view class="room-card" v-for="room in currentRooms" :key="room.id">
      <view class="room-info">
        <text class="room-id">{{ room.roomId }}</text>
        <text class="room-type">{{ room.type }}</text>
        <text class="room-capacity">容量：{{ room.capacity }}人</text>
      </view>
      <view class="room-status" :class="getStatus(room.roomId) ? 'occupied' : 'free'">
        <text>{{ getStatus(room.roomId) ? '使用中' : '空闲' }}</text>
        <text class="course-name" v-if="getStatus(room.roomId)">{{ getStatus(room.roomId).courseName }}</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, computed } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { api } from '../../api'

const buildings = ref([])
const rooms = ref([])
const schedules = ref([])
const activeB = ref('')
const activeSlot = ref(1)

const slots = [
  { id: 1, label: '1-2节', time: '08:00-09:40' },
  { id: 2, label: '3-4节', time: '10:00-11:40' },
  { id: 3, label: '5-6节', time: '14:00-15:40' },
  { id: 4, label: '7-8节', time: '16:00-17:40' },
  { id: 5, label: '9-10节', time: '19:00-20:40' }
]

onLoad(async () => {
  try {
    const [b, s] = await Promise.all([api.classroom.buildings(), api.classroom.schedule()])
    buildings.value = b
    activeB.value = b[0] || ''
    rooms.value = s.rooms || []
    schedules.value = s.schedules || []
  } catch (e) { console.error(e) }
})

const currentRooms = computed(() => rooms.value.filter(r => r.building === activeB.value))
const getStatus = (roomId) => schedules.value.find(s => s.roomId === roomId && s.slotId === activeSlot.value && s.occupied === 1)
</script>

<style>
.container { padding: 12px; }
.scroll-x { display: flex; gap: 8px; overflow-x: auto; background: white; border-radius: 10px; padding: 10px; margin-bottom: 12px; }
.building-item { padding: 6px 18px; border-radius: 16px; font-size: 13px; color: #666; background: #f5f6fa; flex-shrink: 0; }
.building-item.active { background: #7c3aed; color: white; font-weight: bold; }
.slot-bar { display: flex; gap: 6px; margin-bottom: 12px; }
.slot-item { flex: 1; text-align: center; background: white; padding: 10px 4px; border-radius: 8px; }
.slot-item.active { background: #7c3aed; color: white; }
.slot-label { font-size: 13px; font-weight: bold; display: block; }
.slot-time { font-size: 10px; color: #999; display: block; margin-top: 2px; }
.slot-item.active .slot-time { color: rgba(255,255,255,0.8); }
.room-card { display: flex; justify-content: space-between; align-items: center; background: white; padding: 14px; border-radius: 10px; margin-bottom: 8px; }
.room-info { flex: 1; }
.room-id { font-size: 16px; font-weight: bold; display: block; }
.room-type { font-size: 11px; color: #666; margin-top: 2px; display: block; }
.room-capacity { font-size: 11px; color: #999; margin-top: 2px; display: block; }
.room-status { padding: 10px 16px; border-radius: 8px; text-align: center; min-width: 70px; }
.room-status.free { background: #ede9fe; color: #7c3aed; }
.room-status.occupied { background: #fde8e8; color: #e17055; }
.course-name { font-size: 10px; display: block; margin-top: 2px; opacity: 0.8; }
</style>
