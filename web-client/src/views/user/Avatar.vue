<!-- web-client/src/views/user/Avatar.vue -->
<template>
  <el-card class="user-avatar">
    <template #header>
      <div class="card-header">
        <h2>修改头像</h2>
      </div>
    </template>
    <div class="avatar-container">
      <div class="current-avatar">
        <h3>当前头像</h3>
        <el-avatar :size="150" :src="userStore.profile.avatar?.url" />
      </div>
      
      <div class="avatar-upload-area">
        <h3>上传新头像</h3>
        <el-upload
          class="avatar-uploader"
          :auto-upload="false"
          :on-change="handleFileChange"
          :before-upload="beforeUpload"
          :show-file-list="false"
        >
          <el-icon v-if="!imageUrl"><Plus /></el-icon>
          <img v-else :src="imageUrl" class="avatar" />
        </el-upload>
        <div class="upload-tips">
          <p>支持 JPG、PNG 格式，大小不超过 2MB</p>
        </div>
      </div>
    </div>
    
    <div class="form-actions">
      <el-button type="primary" @click="handleSubmit" :loading="isSubmitting">保存修改</el-button>
      <el-button @click="handleCancel">取消</el-button>
    </div>
  </el-card>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { Plus } from '@element-plus/icons-vue';
import { getAvatarUploadPolicy } from '../../api/user';
import { useUserStore } from '../../stores/userStore';
import type { PostPolicyDto } from '../../api/user';
import { getAvatarUrl } from '../../api/user';
import type { PresignedUrl } from '../../types';

const router = useRouter();
const userStore = useUserStore();

const imageUrl = ref<string>('');
const selectedFile = ref<File | null>(null);
const isSubmitting = ref<boolean>(false);

// 处理文件选择
const handleFileChange = (file: any) => {
  const rawFile = file.raw;
  if (rawFile) {
    selectedFile.value = rawFile;
    // 预览图片
    const reader = new FileReader();
    reader.onload = (e) => {
      imageUrl.value = e.target?.result as string;
    };
    reader.readAsDataURL(rawFile);
  }
};

// 上传前验证
const beforeUpload = (file: File) => {
  const isJPG = file.type === 'image/jpeg' || file.type === 'image/png';
  const isLt2M = file.size / 1024 / 1024 < 2;

  if (!isJPG) {
    ElMessage.error('只支持 JPG/PNG 格式的图片');
    return false;
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过 2MB');
    return false;
  }
  return true;
};

// 手动上传文件到远程服务器
const uploadFileToRemote = async (policy: PostPolicyDto, file: File): Promise<boolean> => {
  try {
    const response = await fetch(policy.url, {
      method: policy.method,
      body: file,
      headers: {
        'Content-Type': file.type
      }
    });
    
    if (response.ok) {
      ElMessage.success('文件上传成功');
      return true;
    } else {
      ElMessage.error('文件上传失败');
      return false;
    }
  } catch (error) {
    ElMessage.error('文件上传失败');
    console.error('文件上传失败:', error);
    return false;
  }
};

// 保存修改
const handleSubmit = async () => {
  if (!selectedFile.value) {
    ElMessage.warning('请先选择头像文件');
    return;
  }
  
  isSubmitting.value = true;
  try {
    // 1. 获取上传策略
    ElMessage.info('正在获取上传策略...');
    const policyResponse = await getAvatarUploadPolicy(selectedFile.value.type.split('/')[1]);
    const policy = policyResponse.data;
    console.log('上传策略:', policy);
    // 2. 手动上传文件到远程服务器
    ElMessage.info('正在上传文件...');
    const uploadSuccess = await uploadFileToRemote(policy, selectedFile.value);
    
    if (uploadSuccess) {
      // 3. 更新用户头像URL
      ElMessage.info('正在更新头像信息...');
      const { data: avatarUrl } = await getAvatarUrl();
      await userStore.updateUserAvatar(
        avatarUrl
      );
      // 4. 更新store中的头像URL
      await userStore.fetchUserProfile();
      ElMessage.success('头像修改成功');
      router.push('/user/profile');
    }
  } catch (error) {
    ElMessage.error('头像修改失败');
    console.error('头像修改失败:', error);
  } finally {
    isSubmitting.value = false;
  }
};

// 取消
const handleCancel = () => {
  router.back();
};

onMounted(() => {
  // 组件挂载时无需获取上传策略
});
</script>

<style scoped>
.user-avatar {
  max-width: 600px;
  margin: 0 auto;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.avatar-container {
  display: flex;
  gap: 40px;
  margin: 20px 0;
}

.current-avatar,
.avatar-upload-area {
  flex: 1;
}

.current-avatar h3,
.avatar-upload-area h3 {
  margin-top: 0;
  margin-bottom: 20px;
}

.avatar-uploader {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: all 0.3s;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 200px;
}

.avatar-uploader:hover {
  border-color: #409eff;
}

.avatar {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.upload-tips {
  margin-top: 10px;
  color: #909399;
  font-size: 14px;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 30px;
}
</style>