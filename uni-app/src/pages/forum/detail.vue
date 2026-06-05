<template>
  <view class="container">
    <view class="loading" v-if="loading">加载中...</view>

    <view v-if="post && !loading">
      <!-- 帖子主体 -->
      <view class="post-section">
        <view class="post-header">
          <view class="category-tag" :class="'cat-' + post.category">{{ categoryLabel(post.category) }}</view>
          <text class="post-title">{{ post.title }}</text>
        </view>

        <view class="author-row">
          <image
            class="avatar"
            v-if="post.authorAvatar"
            :src="'http://localhost:8080' + post.authorAvatar"
            mode="aspectFill"
          />
          <view class="avatar avatar-text" v-else>{{ (post.authorName || '匿')[0] }}</view>
          <view class="author-detail">
            <text class="author-name">{{ post.authorName || '匿名' }}</text>
            <text class="post-time">{{ post.createTime ? post.createTime.slice(0, 16) : '' }}</text>
          </view>
        </view>

        <text class="post-content">{{ post.content }}</text>

        <view class="contact" v-if="post.category === 'lost_found' && post.phone">
          <text class="contact-label">📱 联系方式：</text>
          <text class="contact-value">{{ post.phone }}</text>
        </view>

        <!-- 操作栏 -->
        <view class="action-bar">
          <view class="action-item" :class="{ active: post.liked }" @click="toggleLike">
            <text>{{ post.liked ? '👍' : '👍' }} {{ post.likeCount || 0 }}</text>
          </view>
          <view class="action-item" :class="{ active: post.favorited }" @click="toggleFavorite">
            <text>{{ post.favorited ? '⭐' : '☆' }} {{ post.favoriteCount || 0 }}</text>
          </view>
          <view class="action-item">
            <text>💬 {{ post.commentCount || 0 }}</text>
          </view>
        </view>
      </view>

      <!-- 评论区 -->
      <view class="comment-section">
        <view class="section-title">评论 ({{ post.commentCount || 0 }})</view>

        <view v-if="comments.length">
          <view class="comment-card" v-for="c in comments" :key="c.id">
            <view class="comment-header">
              <view class="comment-avatar-wrap">
                <image
                  class="comment-avatar"
                  v-if="c.authorAvatar"
                  :src="'http://localhost:8080' + c.authorAvatar"
                  mode="aspectFill"
                />
                <view class="comment-avatar avatar-text" v-else>{{ (c.authorName || '匿')[0] }}</view>
              </view>
              <view class="comment-meta">
                <text class="comment-author">{{ c.authorName || '匿名' }}</text>
                <text class="comment-time">{{ c.createTime ? c.createTime.slice(5, 16) : '' }}</text>
              </view>
            </view>
            <text class="comment-content">{{ c.content }}</text>
          </view>
        </view>

        <view class="empty-comment" v-if="!comments.length && !loadingComments">
          <text>暂无评论，快来抢沙发~</text>
        </view>
      </view>
    </view>

    <!-- 底部发评论 -->
    <view class="comment-input-bar" v-if="post">
      <input class="comment-input" v-model="commentText" placeholder="写评论..." />
      <view class="send-btn" :class="{ active: commentText.trim() && !sending }" @click="sendComment">{{ sending ? '...' : '发送' }}</view>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { api } from '../../api'

const post = ref(null)
const comments = ref([])
const loading = ref(true)
const loadingComments = ref(false)
const commentText = ref('')
const postId = ref(null)

const getStudentId = () => {
  try {
    const user = uni.getStorageSync('login_user')
    return user?.studentId || ''
  } catch { return '' }
}

const categoryLabel = (val) => {
  const map = { help: '求助', discuss: '讨论', lost_found: '失物招领', notice: '公告' }
  return map[val] || val
}

const loadPost = async () => {
  loading.value = true
  try {
    post.value = await api.post.detail(postId.value, getStudentId())
  } catch (e) {
    uni.showToast({ title: '加载失败', icon: 'none' })
  } finally {
    loading.value = false
  }
}

const loadComments = async () => {
  loadingComments.value = true
  try {
    const res = await api.comment.list(postId.value)
    comments.value = res.records || []
  } catch (e) {
    comments.value = []
  } finally {
    loadingComments.value = false
  }
}

const toggleLike = async () => {
  if (!getStudentId()) return uni.showToast({ title: '请先登录', icon: 'none' })
  try {
    await api.post.toggleLike(postId.value, getStudentId())
    await loadPost()
  } catch (e) {
    uni.showToast({ title: '操作失败', icon: 'none' })
  }
}

const toggleFavorite = async () => {
  if (!getStudentId()) return uni.showToast({ title: '请先登录', icon: 'none' })
  try {
    await api.post.toggleFavorite(postId.value, getStudentId())
    await loadPost()
  } catch (e) {
    uni.showToast({ title: '操作失败', icon: 'none' })
  }
}

const sending = ref(false)

const sendComment = async () => {
  if (!commentText.value.trim()) return
  if (!getStudentId()) return uni.showToast({ title: '请先登录', icon: 'none' })
  if (sending.value) return

  const content = commentText.value.trim()
  sending.value = true
  try {
    await api.comment.create({
      postId: postId.value,
      content
    }, getStudentId())
    commentText.value = ''
    uni.showToast({ title: '评论成功', icon: 'success' })
    await Promise.all([loadPost(), loadComments()])
  } catch (e) {
    uni.showToast({ title: e.message || '评论失败', icon: 'none' })
  } finally {
    sending.value = false
  }
}

onLoad((query) => {
  postId.value = query.id
  loadPost()
  loadComments()
})
</script>

<style scoped>
.container { background: #f5f6fa; min-height: 100vh; padding-bottom: 60px; }
.loading { text-align: center; padding: 40px; color: #999; }

/* 帖子主体 */
.post-section { background: #fff; padding: 16px; margin-bottom: 10px; }
.post-header { margin-bottom: 12px; }
.post-header { display: flex; align-items: center; gap: 8px; margin-bottom: 10px; }
.category-tag {
  font-size: 11px; padding: 2px 8px; border-radius: 4px;
  color: #fff; flex-shrink: 0;
}
.cat-help { background: #f59e0b; }
.cat-discuss { background: #3b82f6; }
.cat-lost_found { background: #10b981; }
.cat-notice { background: #ef4444; }
.post-title { font-size: 17px; font-weight: bold; color: #333; }

.author-row { display: flex; align-items: center; gap: 8px; margin-bottom: 12px; }
.avatar { width: 32px; height: 32px; border-radius: 50%; }
.avatar-text {
  display: flex; align-items: center; justify-content: center;
  background: #7c3aed; color: #fff; font-size: 13px; font-weight: bold;
}
.author-detail { display: flex; flex-direction: column; }
.author-name { font-size: 13px; color: #333; }
.post-time { font-size: 11px; color: #ccc; }

.post-content { font-size: 14px; color: #555; line-height: 1.8; margin-bottom: 12px; }

.contact { margin-bottom: 12px; }
.contact-label { font-size: 13px; color: #999; }
.contact-value { font-size: 13px; color: #10b981; font-weight: bold; }

/* 操作栏 */
.action-bar {
  display: flex; gap: 24px; padding-top: 12px; border-top: 1px solid #f0f0f0;
}
.action-item { font-size: 13px; color: #999; }
.action-item.active { color: #7c3aed; }

/* 评论区 */
.comment-section { background: #fff; padding: 16px; }
.section-title { font-size: 15px; font-weight: bold; color: #333; margin-bottom: 12px; }

.comment-card { padding: 12px 0; border-bottom: 1px solid #f5f5f5; }
.comment-header { display: flex; align-items: center; gap: 8px; margin-bottom: 8px; }
.comment-avatar-wrap { flex-shrink: 0; }
.comment-avatar { width: 28px; height: 28px; border-radius: 50%; }
.comment-avatar.avatar-text {
  width: 28px; height: 28px; display: flex; align-items: center; justify-content: center;
  background: #7c3aed; color: #fff; font-size: 11px; font-weight: bold;
}
.comment-meta { display: flex; flex-direction: column; }
.comment-author { font-size: 13px; color: #333; }
.comment-time { font-size: 11px; color: #ccc; }
.comment-content { font-size: 14px; color: #555; line-height: 1.6; }

.empty-comment { text-align: center; padding: 30px; color: #ccc; font-size: 13px; }

/* 底部评论输入 */
.comment-input-bar {
  position: fixed; bottom: 0;
  left: 50%; transform: translateX(-50%);
  width: 100%; max-width: 430px;
  display: flex; align-items: center; gap: 8px;
  padding: 8px 12px; background: #fff;
  border-top: 1px solid #eee; box-sizing: border-box;
}
.comment-input {
  flex: 1; height: 36px; border: 1px solid #eee; border-radius: 18px;
  padding: 0 14px; font-size: 13px; box-sizing: border-box;
}
.send-btn {
  flex-shrink: 0; font-size: 14px; color: #ccc; font-weight: bold;
}
.send-btn.active { color: #7c3aed; }
</style>
