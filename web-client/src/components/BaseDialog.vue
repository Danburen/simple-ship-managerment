<template>
  <el-dialog
    v-model="visible"
    :title="title"
    :width="width"
    :destroy-on-close="destroyOnClose"
    @close="handleClose"
  >
    <!-- 弹窗内容插槽 -->
    <slot></slot>
    
    <!-- 弹窗底部操作按钮 -->
    <template #footer>
      <span class="dialog-footer">
        <slot name="footer">
          <el-button @click="handleCancel">{{ cancelText }}</el-button>
          <el-button type="primary" @click="handleConfirm">{{ confirmText }}</el-button>
        </slot>
      </span>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { computed } from 'vue';

// Props
const props = withDefaults(defineProps<{
  modelValue: boolean;
  title: string;
  width?: string;
  destroyOnClose?: boolean;
  confirmText?: string;
  cancelText?: string;
}>(), {
  width: '500px',
  destroyOnClose: false,
  confirmText: '确定',
  cancelText: '取消'
});

// Emits
const emit = defineEmits<{
  'update:modelValue': [value: boolean];
  confirm: [];
  cancel: [];
  close: [];
}>();

// 计算属性
const visible = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
});

// 处理确认
const handleConfirm = () => {
  emit('confirm');
};

// 处理取消
const handleCancel = () => {
  emit('cancel');
  visible.value = false;
};

// 处理关闭
const handleClose = () => {
  emit('close');
};
</script>

<style scoped>
.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>