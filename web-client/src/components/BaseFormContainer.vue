<template>
  <div class="base-form-container">
    <el-card>
      <!-- 表单标题和操作栏 -->
      <template #header>
        <div class="card-header">
          <div class="header-left">
            <h3>{{ title }}</h3>
          </div>
          <div class="header-right">
            <slot name="action-buttons">
              <el-button @click="$emit('cancel')">取消</el-button>
              <el-button type="primary" @click="$emit('submit')">保存</el-button>
            </slot>
          </div>
        </div>
      </template>
      
      <!-- 表单内容插槽 -->
      <div class="card-content">
        <slot></slot>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
// Props
const props = defineProps<{
  title: string;
}>();

// Emits
const emit = defineEmits<{
  submit: [];
  cancel: [];
}>();
</script>

<style scoped>
.base-form-container {
  width: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0;
}

.header-left h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 500;
  color: #303133;
}

.header-right {
  display: flex;
  gap: 10px;
}

.card-content {
  padding: 20px 0;
}
</style>