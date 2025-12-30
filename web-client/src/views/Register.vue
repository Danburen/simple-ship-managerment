<template>
  <div class="login-container">
    <!-- 左侧背景图 -->
    <div class="login-bg"></div>
    <!-- 右侧注册表单 -->
    <div class="login-form-wrapper">
      <el-card class="login-card" shadow="hover">
        <template #header>
          <div class="login-header">
            <h2>船舶管理系统</h2>
            <p>请注册您的账户</p>
          </div>
        </template>
        <el-form
          ref="registerFormRef"
          :model="registerForm"
          :rules="registerRules"
          class="login-form"
          label-position="top"
        >
          <el-form-item prop="username">
            <el-input
              v-model="registerForm.username"
              placeholder="请输入用户名"
              size="large"
              :prefix-icon="User"
            />
          </el-form-item>
          <el-form-item prop="password">
            <el-input
              v-model="registerForm.password"
              type="password"
              placeholder="请输入密码"
              show-password
              size="large"
              :prefix-icon="Lock"
            />
          </el-form-item>
          <el-form-item prop="confirmPwd">
            <el-input
              v-model="registerForm.confirmPwd"
              type="password"
              placeholder="请确认密码"
              show-password
              size="large"
              :prefix-icon="Lock"
            />
          </el-form-item>
          <el-form-item prop="nickname">
            <el-input
              v-model="registerForm.nickname"
              placeholder="请输入昵称"
              size="large"
              :prefix-icon="User"
            />
          </el-form-item>
           <el-form-item>
            <div ref="turnstileContainerRef"></div>
          </el-form-item>
          <el-form-item>
            <el-button
              type="primary"
              :loading="isLoading"
              @click="handleRegister"
              size="large"
              class="login-button"
            >
              注册
            </el-button>
          </el-form-item>
          <div class="login-footer">
            <span @click="$router.push('/login')">已有账户？立即登录</span>
          </div>
        </el-form>
      </el-card>
    </div>

  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { ElForm, ElFormItem, ElInput, ElButton, FormRules, ElMessage } from 'element-plus';
import { User, Lock } from '@element-plus/icons-vue';
import { register } from '../api/auth';

const router = useRouter();
const registerFormRef = ref<InstanceType<typeof ElForm> | null>(null);
const isLoading = ref(false);
const turnstileContainerRef = ref<HTMLElement>()

const registerForm = reactive({
  username: '',
  password: '',
  confirmPwd: '',
  nickname: '',
  cfTurnstileToken: ''
});
// 注册表单规则
const registerRules = reactive<FormRules>({
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 4, max: 16, message: '用户名长度在 4 到 16 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 8, max: 16, message: '密码长度在 8 到 16 个字符', trigger: 'blur' }
  ],
  confirmPwd: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { 
      validator: (_rule, value, callback) => {
        if (value !== registerForm.password) {
          callback(new Error('两次输入密码不一致'));
        } else {
          callback();
        }
      },
      trigger: 'blur'
    }
  ],
  nickname: [
    { min: 2, max: 16, message: '昵称长度在 2 到 16 个字符', trigger: 'blur' }
  ],
});

const tryInitTurnstile = () => {
  if (!(window as any).turnstile) {
    const script = document.createElement('script');
    script.src = 'https://challenges.cloudflare.com/turnstile/v0/api.js?render=explicit';
    script.async = true;
    script.defer = true;
    document.head.appendChild(script);
    script.onload = () => {
      initializeTurnstile();
    };
  } else {
    initializeTurnstile();
  }
}

const initializeTurnstile = () => {
  if (turnstileContainerRef.value && (window as any).turnstile) {
    if (turnstileContainerRef.value.children.length === 0) {
      (window as any).turnstile.render(turnstileContainerRef.value, {
        sitekey: '0x4AAAAAACJv7L1reoh5p0NV',
        callback: (token: string) => {
          console.log('Turnstile验证成功:', token);
        },
        'error-callback': () => {
          console.error('Turnstile验证失败');
        },
        'expired-callback': () => {
          console.warn('Turnstile验证已过期');
        }
      });
    }
  }
}

const handleRegister = () => {
  if (!registerFormRef.value) return;
  const turnstileToken = (window as any).turnstile?.getResponse();
  if (!turnstileToken) {
    ElMessage.error('请完成人机验证');
  }
  
  registerFormRef.value.validate().then(() => {
    isLoading.value = true;
    registerForm.cfTurnstileToken = turnstileToken;
    
    register(registerForm).then(() => {
      router.push('/login');
    }).catch((error: any) => {
      console.error('注册失败:', error);
      registerForm.cfTurnstileToken = ''
    }).finally(() => {
      isLoading.value = false;
    });
  }).catch((error: any) => {
    console.error('注册失败:', error);
    registerForm.cfTurnstileToken = '';
  });
};

onMounted(() => {
  tryInitTurnstile();
})
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  min-height: 100vh;
  background-color: #f5f7fa;
  position: relative;
  overflow: hidden;
}

/* 左侧背景图 */
.login-bg {
  position: absolute;
  left: 0;
  top: 0;
  width: 55%;
  height: 100%;
  background-image: url('/bg-1.jpg');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  z-index: 0;
}

/* 右侧注册表单容器 */
.login-form-wrapper {
  width: 45%;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 0 50px;
  z-index: 1;
}

.login-card {
  width: 400px;
  max-width: 100%;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.login-header {
  text-align: center;
}

.login-header h2 {
  margin: 0;
  color: #303133;
  font-size: 24px;
  font-weight: bold;
}

.login-header p {
  margin: 8px 0 0 0;
  color: #606266;
  font-size: 14px;
}

.login-form {
  padding: 20px 0;
}

.login-button {
  width: 100%;
}

.login-footer {
  text-align: center;
  margin-top: 10px;
}

.login-footer span {
  color: #409eff;
  cursor: pointer;
  font-size: 14px;
}

.login-footer span:hover {
  text-decoration: underline;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .login-container {
    justify-content: center;
  }
  
  .login-bg {
    width: 100%;
    opacity: 0.3;
  }
  
  .login-form-wrapper {
    width: 100%;
    padding: 0 20px;
  }
  
  .login-card {
    background-color: rgba(255, 255, 255, 0.95);
  }
}
</style>
