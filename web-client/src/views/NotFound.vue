<template>
  <div class="not-found-container">
    <el-card class="not-found-card">
      <div class="not-found-content">
        <h1 class="error-code">404</h1>
        <h2 class="error-message">页面未找到</h2>
        <p class="error-description">您访问的页面不存在或已被删除</p>
        <el-button type="primary" @click="goBack">返回首页</el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router';

const router = useRouter();

const goBack = () => {
  router.push('/');
};
</script>

<style scoped>
.not-found-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f5f7fa;
}

.not-found-card {
  width: 500px;
  max-width: 90%;
  text-align: center;
}

.not-found-content {
  padding: 40px 0;
}

.error-code {
  font-size: 80px;
  font-weight: bold;
  color: #409eff;
  margin: 0 0 20px 0;
}

.error-message {
  font-size: 24px;
  color: #303133;
  margin: 0 0 10px 0;
}

.error-description {
  font-size: 14px;
  color: #606266;
  margin: 0 0 30px 0;
}
</style>
