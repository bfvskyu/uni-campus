<template></template>
<script setup></script>
<style>·
page { background: #f5f6fa; }

/* H5 手机视图居中装甲 */
@media screen and (min-width: 500px) {
  /* 1. 约束主体内容区 */
  uni-app { 
    max-width: 430px; 
    margin: 0 auto; 
  }
  
  /* 2. 约束顶部导航栏 (上面那个) */
  uni-page-head, .uni-page-head {
    max-width: 430px !important;
    margin: 0 auto !important;
    left: 0 !important;
    right: 0 !important;
  }
  
  /* 3. 约束底部 Tab 栏 (最下面导航栏) */
  uni-tabbar, .uni-tabbar {
    max-width: 430px !important;
    margin: 0 auto !important;
    left: 0 !important;
    right: 0 !important;
  }
}
</style>

