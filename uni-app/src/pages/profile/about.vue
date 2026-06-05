<template>
  <view class="container">
    <view class="logo-section">
      <view class="logo">🏫</view>
      <text class="app-name">大学生服务中心</text>
      <text class="version">v2.1.4</text>
    </view>

    <view class="desc-card">
      <text class="section-title">产品简介</text>
      <text class="desc-text">
        大学生服务中心是一款专为高校师生打造的校园生活助手，集信息查询、教务管理、生活服务于一体，致力于让校园生活更便捷高效。
      </text>
    </view>

    <view class="desc-card">
      <text class="section-title">功能特性</text>
      <view class="feature-item">
        <text class="feature-icon">📢</text>
        <view class="feature-info">
          <text class="feature-name">校园公告</text>
          <text class="feature-desc">实时浏览学校发布的各类公告通知</text>
        </view>
      </view>
      <view class="feature-item">
        <text class="feature-icon">💼</text>
        <view class="feature-info">
          <text class="feature-name">校园招聘</text>
          <text class="feature-desc">汇聚店铺与派送岗位，轻松找到校内兼职</text>
        </view>
      </view>
      <view class="feature-item">
        <text class="feature-icon">🏛️</text>
        <view class="feature-info">
          <text class="feature-name">教室查询</text>
          <text class="feature-desc">按建筑查看空闲教室与占用情况</text>
        </view>
      </view>
      <view class="feature-item">
        <text class="feature-icon">📝</text>
        <view class="feature-info">
          <text class="feature-name">考试竞赛</text>
          <text class="feature-desc">考试与竞赛信息一览，不错过重要节点</text>
        </view>
      </view>
      <view class="feature-item">
        <text class="feature-icon">📋</text>
        <view class="feature-info">
          <text class="feature-name">备忘录</text>
          <text class="feature-desc">随手记录重要事项，支持批量管理</text>
        </view>
      </view>
      <view class="feature-item">
        <text class="feature-icon">🔑</text>
        <view class="feature-info">
          <text class="feature-name">管理员模式</text>
          <text class="feature-desc">密钥认证，一站式管理平台内容</text>
        </view>
      </view>
    </view>

    <view class="footer">
      <text class="footer-text">大学生服务中心 v1.0.0</text>
      <text class="footer-text">2026 校园出品</text>
    </view>
  </view>
</template>

<style>
.container { padding: 20px 16px; }

.logo-section {
  display: flex; flex-direction: column; align-items: center;
  margin-bottom: 20px;
}
.logo { font-size: 48px; margin-bottom: 8px; }
.app-name { font-size: 20px; font-weight: bold; color: #333; }
.version { font-size: 12px; color: #999; margin-top: 4px; }

.desc-card {
  background: white; border-radius: 12px; padding: 16px;
  margin-bottom: 12px; box-shadow: 0 1px 4px rgba(0,0,0,0.06);
}
.section-title {
  font-size: 15px; font-weight: bold; color: #333;
  display: block; margin-bottom: 8px;
}
.desc-text { font-size: 13px; color: #666; line-height: 1.7; }

.feature-item {
  display: flex; gap: 12px; padding: 10px 0;
  border-bottom: 1px solid #f5f5f5;
}
.feature-item:last-child { border-bottom: none; }
.feature-icon { font-size: 22px; width: 28px; text-align: center; }
.feature-info { flex: 1; }
.feature-name { font-size: 14px; font-weight: bold; display: block; margin-bottom: 2px; }
.feature-desc { font-size: 12px; color: #999; }

.footer { text-align: center; margin-top: 20px; padding-bottom: 20px; }
.footer-text { display: block; font-size: 11px; color: #ccc; line-height: 1.6; }
</style>
