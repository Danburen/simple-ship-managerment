<!-- web-client/src/views/user/Profile.vue -->
<template>
  <el-card class="user-profile-card">
    <template #header>
      <div class="card-header">
        <h2 class="card-title">个人中心</h2>
        <el-tag type="info" size="small">个人资料</el-tag>
      </div>
    </template>
    
    <div class="profile-container">
      <!-- 左侧：头像和基本信息 -->
      <div class="profile-left">
        <div class="avatar-wrapper">
          <div class="avatar-container">
            <el-avatar :size="140" :src="userStore.profile.avatar?.url" class="profile-avatar" />
            <el-button 
              type="primary" 
              plain 
              @click="handleAvatarUpload" 
              class="avatar-upload-btn"
              size="small"
            >
              <el-icon><Camera /></el-icon>
              更换头像
            </el-button>
          </div>
        </div>
        
        <div class="basic-info-section">
          <h3 class="section-title">基本信息</h3>
          <div class="info-list">
            <div class="info-item">
              <div class="info-label-group">
                <span class="info-label">用户名</span>
                <el-tag type="success" size="small">已设置</el-tag>
              </div>
              <div class="info-content">
                <span class="info-value">{{ userStore.profile.username }}</span>
              </div>
            </div>
            
            <div class="info-item">
              <div class="info-label-group">
                <span class="info-label">昵称</span>
                <el-tag :type="userStore.profile.nickname ? 'success' : 'warning'" size="small">
                  {{ userStore.profile.nickname ? '已设置' : '未设置' }}
                </el-tag>
              </div>
              <div class="info-content">
                <div class="nickname-edit">
                  <el-input
                    v-if="isEditingNickname"
                    v-model="nicknameInput"
                    placeholder="请输入昵称"
                    @keyup.enter="confirmNickname"
                    @blur="confirmNickname"
                    ref="nicknameInputRef"
                    size="small"
                    :class="{ 'nickname-input': true }"
                  />
                  <span v-else class="nickname-value">{{ userStore.profile.nickname || '未设置昵称' }}</span>
                  <div class="nickname-actions">
                    <el-button
                      v-if="!isEditingNickname"
                      type="primary"
                      text
                      @click="startEditNickname"
                      size="small"
                    >
                      <el-icon><EditPen /></el-icon>
                      编辑
                    </el-button>
                    <div v-else class="edit-actions">
                      <el-button
                        type="primary"
                        text
                        @click="confirmNickname"
                        size="small"
                      >
                        确认
                      </el-button>
                      <el-button
                        type="text"
                        @click="cancelEditNickname"
                        size="small"
                      >
                        取消
                      </el-button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 右侧：操作区域 -->
      <div class="profile-right">
        <el-card class="operation-card" shadow="hover">
          <template #header>
            <h3 class="section-title">账号管理</h3>
          </template>
          <div class="operation-list">
            <div class="operation-item">
              <div class="operation-icon">
                <el-icon class="icon-large"><Lock /></el-icon>
              </div>
              <div class="operation-content">
                <h4 class="operation-title">修改密码</h4>
                <p class="operation-desc">定期修改密码有助于保护账号安全</p>
              </div>
              <el-button type="primary" @click="handlePasswordChange">
                立即修改
              </el-button>
            </div>
            
            <div class="operation-item">
              <div class="operation-icon">
                <el-icon class="icon-large"><Camera /></el-icon>
              </div>
              <div class="operation-content">
                <h4 class="operation-title">更换头像</h4>
                <p class="operation-desc">上传个性化头像，展示您的风采</p>
              </div>
              <el-button type="primary" @click="handleAvatarUpload">
                更换头像
              </el-button>
            </div>
          </div>
        </el-card>
        
        <el-card class="security-tips" shadow="hover">
          <template #header>
            <h3 class="section-title">安全提示</h3>
          </template>
          <div class="tips-list">
            <div class="tip-item">
              <el-icon class="tip-icon"><Warning /></el-icon>
              <span class="tip-text">请勿将账号密码告知他人</span>
            </div>
            <div class="tip-item">
              <el-icon class="tip-icon"><Check /></el-icon>
              <span class="tip-text">定期更换密码，增强账号安全性</span>
            </div>
            <div class="tip-item">
              <el-icon class="tip-icon"><InfoFilled /></el-icon>
              <span class="tip-text">确保昵称符合平台规范</span>
            </div>
          </div>
        </el-card>
      </div>
    </div>
  </el-card>
</template>

<script setup lang="ts">
import { ref, onMounted, nextTick } from 'vue';
import { useRouter } from 'vue-router';
import { Camera, EditPen, Lock, Warning, Check, InfoFilled } from '@element-plus/icons-vue';
import { useUserStore } from '../../stores/userStore';

const router = useRouter();
const userStore = useUserStore();

// 昵称编辑相关
const isEditingNickname = ref(false);
const nicknameInput = ref('');
const nicknameInputRef = ref(null);

// 加载用户资料
const loadUserProfile = async () => {
  await userStore.fetchUserProfile();
};

// 开始编辑昵称
const startEditNickname = () => {
  nicknameInput.value = userStore.profile.nickname;
  isEditingNickname.value = true;
  nextTick(() => {
    // @ts-ignore
    nicknameInputRef.value?.focus();
  });
};

// 确认修改昵称
const confirmNickname = async () => {
  if (nicknameInput.value.trim() === userStore.profile.nickname) {
    isEditingNickname.value = false;
    return;
  }
  
  if (nicknameInput.value.trim()) {
    await userStore.updateUserNickname(nicknameInput.value.trim());
  }
  isEditingNickname.value = false;
};

// 取消编辑昵称
const cancelEditNickname = () => {
  isEditingNickname.value = false;
};

// 更换头像
const handleAvatarUpload = () => {
  router.push('/user/avatar');
};

// 修改密码
const handlePasswordChange = () => {
  router.push('/user/password');
};

onMounted(() => {
  loadUserProfile();
});
</script>

<style scoped>
/* 主卡片样式 */
.user-profile-card {
  max-width: 900px;
  margin: 0 auto;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  overflow: hidden;
}

/* 卡片头部 */
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
}

.card-title {
  font-size: 24px;
  font-weight: 600;
  margin: 0;
  color: #333;
  display: flex;
  align-items: center;
  gap: 12px;
}

/* 整体布局 */
.profile-container {
  display: flex;
  gap: 30px;
  padding: 20px;
}

/* 左侧布局 */
.profile-left {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 25px;
}

/* 右侧布局 */
.profile-right {
  width: 350px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

/* 头像区域 */
.avatar-wrapper {
  display: flex;
  justify-content: center;
  padding: 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 12px;
}

.avatar-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 15px;
}

.profile-avatar {
  border: 4px solid rgba(255, 255, 255, 0.8);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
  transition: transform 0.3s ease;
}

.profile-avatar:hover {
  transform: scale(1.05);
}

.avatar-upload-btn {
  background: rgba(255, 255, 255, 0.9);
  color: #667eea;
  border-color: rgba(255, 255, 255, 0.8);
  transition: all 0.3s ease;
}

.avatar-upload-btn:hover {
  background: white;
  color: #5a6fd8;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

/* 基本信息区域 */
.basic-info-section {
  background: white;
  padding: 25px;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.section-title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  display: flex;
  align-items: center;
  gap: 8px;
  padding-bottom: 10px;
}

/* 信息列表 */
.info-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.info-item {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.info-label-group {
  display: flex;
  align-items: center;
  gap: 8px;
}

.info-label {
  font-size: 14px;
  font-weight: 600;
  color: #666;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.info-content {
  display: flex;
  align-items: center;
  gap: 15px;
  flex-wrap: wrap;
}

.info-value {
  font-size: 16px;
  color: #333;
  font-weight: 500;
}

/* 昵称编辑 */
.nickname-edit {
  display: flex;
  align-items: center;
  gap: 12px;
  flex: 1;
  min-width: 0;
}

.nickname-value {
  font-size: 16px;
  color: #333;
  font-weight: 500;
  min-width: 100px;
  word-break: break-word;
}

.nickname-input {
  flex: 1;
  max-width: 250px;
}

.nickname-actions {
  display: flex;
  align-items: center;
  gap: 8px;
}

.edit-actions {
  display: flex;
  gap: 8px;
}

/* 操作卡片 */
.operation-card {
  border-radius: 12px;
  overflow: hidden;
}

.operation-list {
  display: flex;
  flex-direction: column;
  gap: 0;
}

.operation-item {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 20px;
  border-bottom: 1px solid #f0f0f0;
  transition: background-color 0.3s ease;
}

.operation-item:last-child {
  border-bottom: none;
}

.operation-item:hover {
  background-color: #fafafa;
}

.operation-icon {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  font-size: 24px;
  flex-shrink: 0;
}

.icon-large {
  font-size: 20px;
}

.operation-content {
  flex: 1;
  min-width: 0;
}

.operation-title {
  font-size: 16px;
  font-weight: 600;
  margin: 0 0 5px 0;
  color: #333;
}

.operation-desc {
  font-size: 13px;
  color: #999;
  margin: 0;
  line-height: 1.5;
}

.operation-item .el-button {
  flex-shrink: 0;
}

/* 安全提示卡片 */
.security-tips {
  border-radius: 12px;
  overflow: hidden;
}

.tips-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
  padding: 0 20px 20px;
}

.tip-item {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  padding: 12px;
  background-color: #f8f9fa;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.tip-item:hover {
  background-color: #e9ecef;
  transform: translateX(5px);
}

.tip-icon {
  font-size: 18px;
  color: #667eea;
  margin-top: 2px;
  flex-shrink: 0;
}

.tip-text {
  font-size: 14px;
  color: #666;
  line-height: 1.5;
  margin: 0;
}

/* 响应式设计 */
@media (max-width: 992px) {
  .profile-container {
    flex-direction: column;
  }
  
  .profile-right {
    width: 100%;
  }
}

@media (max-width: 576px) {
  .card-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
  
  .card-title {
    font-size: 20px;
  }
  
  .profile-container {
    padding: 10px;
  }
  
  .avatar-wrapper {
    padding: 15px;
  }
  
  .profile-avatar {
    size: 120px;
  }
  
  .basic-info-section {
    padding: 15px;
  }
}
</style>