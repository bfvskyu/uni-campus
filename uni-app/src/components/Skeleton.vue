<template>
  <view class="skeleton">
    <view class="s-card" v-for="i in count" :key="i" v-if="type === 'card'">
      <view class="s-line w-60"></view>
      <view class="s-line w-80"></view>
      <view class="s-line w-40"></view>
    </view>
    <view class="s-detail" v-if="type === 'detail'">
      <view class="s-line w-70"></view>
      <view class="s-line w-100"></view>
      <view class="s-line w-100"></view>
      <view class="s-line w-30"></view>
    </view>
  </view>
</template>

<script setup>
defineProps({
  type: { type: String, default: 'card' },
  count: { type: Number, default: 3 }
})
</script>

<style scoped>
.s-card { background: white; border-radius: 10px; padding: 14px; margin-bottom: 10px; }
.s-detail { background: white; border-radius: 10px; padding: 14px; margin-bottom: 10px; }
.s-line { height: 14px; background: linear-gradient(90deg, #eee 25%, #f5f5f5 50%, #eee 75%); background-size: 200% 100%; animation: shimmer 1.5s infinite; border-radius: 6px; margin-bottom: 10px; }
.s-line:last-child { margin-bottom: 0; }
.w-30 { width: 30%; }
.w-40 { width: 40%; }
.w-60 { width: 60%; }
.w-70 { width: 70%; }
.w-80 { width: 80%; }
.w-100 { width: 100%; }
@keyframes shimmer { 0% { background-position: 200% 0; } 100% { background-position: -200% 0; } }
</style>
