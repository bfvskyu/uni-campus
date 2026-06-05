<template>
  <view class="container">
    <!-- 常见问题 -->
    <view class="section">
      <text class="section-title">❓ 常见问题</text>
      <view class="faq-item" v-for="(faq, i) in faqs" :key="i" @click="faq.open = !faq.open">
        <view class="faq-q">
          <text class="faq-q-text">{{ faq.q }}</text>
          <text class="faq-arrow">{{ faq.open ? '▼' : '▶' }}</text>
        </view>
        <text class="faq-a" v-if="faq.open">{{ faq.a }}</text>
      </view>
    </view>

    <!-- 提交反馈 -->
    <view class="section">
      <text class="section-title">💬 意见反馈</text>
      <textarea class="input" v-model="feedbackTitle" placeholder="反馈标题（必填）" />
      <textarea class="input detail" v-model="feedbackDetail" placeholder="详细描述…" />
      <view class="submit-btn" @click="submitFeedback">提交反馈</view>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { api } from '../../api'

const faqs = ref([
  { q: '如何进入管理员模式？', a: '在「我」页面点击「管理员模式」，输入有效密钥即可进入管理面板。', open: false },
  { q: '管理员密钥从哪里获取？', a: '联系系统管理员获取密钥。密钥有效期为 7 天，过期后需重新申请。', open: false },
  { q: '备忘录可以保存哪些内容？', a: '在招聘和考试详情页点击「+」按钮可将信息添加到备忘录，方便快速查看。', open: false },
  { q: '教室查询的数据多久更新？', a: '教室占用数据为演示数据，如有疑问请联系教务处。', open: false },
  { q: '如何修改个人资料？', a: '在「我」页面点击「编辑」按钮，可修改姓名、学号和院系信息。', open: false },
])

const feedbackTitle = ref('')
const feedbackDetail = ref('')

const submitFeedback = async () => {
  if (!feedbackTitle.value.trim()) {
    uni.showToast({ title: '请输入反馈标题', icon: 'none' })
    return
  }
  try {
    await api.feedback.submit({
      title: feedbackTitle.value.trim(),
      detail: feedbackDetail.value.trim()
    })
    uni.showToast({ title: '反馈提交成功', icon: 'success' })
    feedbackTitle.value = ''
    feedbackDetail.value = ''
  } catch (e) {
    uni.showToast({ title: e.message || '提交失败', icon: 'none' })
  }
}
</script>

<style>
.container { padding: 12px; }

.section {
  background: white; border-radius: 12px; padding: 16px;
  margin-bottom: 12px; box-shadow: 0 1px 4px rgba(0,0,0,0.06);
}
.section-title {
  font-size: 16px; font-weight: bold; display: block;
  margin-bottom: 12px;
}

.faq-item { border-bottom: 1px solid #f5f5f5; padding: 12px 0; }
.faq-item:last-child { border-bottom: none; }
.faq-q {
  display: flex; align-items: center; gap: 8px;
}
.faq-q-text { flex: 1; font-size: 14px; color: #333; }
.faq-arrow { font-size: 10px; color: #999; }
.faq-a {
  display: block; font-size: 13px; color: #888;
  margin-top: 8px; line-height: 1.6; padding-left: 16px;
}

.input {
  border: 1px solid #e5e5e5; border-radius: 8px; padding: 12px;
  font-size: 14px; width: 100%; box-sizing: border-box;
  margin-bottom: 10px; height: 42px; background: #fafafa;
}
.detail { height: 100px; }
.submit-btn {
  background: #7c3aed; color: white; text-align: center;
  padding: 12px; border-radius: 8px; font-size: 15px; font-weight: bold;
}
</style>
