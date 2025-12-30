<template>
  <div class="login-container">
    <!-- 左侧背景图 -->
    <div class="login-bg"></div>
    <!-- 右侧登录表单 -->
    <div class="login-form-wrapper">
      <el-card class="login-card" shadow="hover">
        <template #header>
          <div class="login-header">
            <h2>船舶管理系统</h2>
            <p>请登录您的账户</p>
          </div>
        </template>
        <el-form
          ref="loginFormRef"
          :model="loginForm"
          :rules="loginRules"
          class="login-form"
          label-position="top"
        >
          <el-form-item prop="username">
            <el-input
              v-model="loginForm.username"
              placeholder="请输入用户名"
              size="large"
              :prefix-icon="User"
            />
          </el-form-item>
          <el-form-item prop="password">
            <el-input
              v-model="loginForm.password"
              type="password"
              placeholder="请输入密码"
              show-password
              size="large"
              :prefix-icon="Lock"
            />
          </el-form-item>
          <el-form-item>
            <div ref="turnstileContainerRef"></div>
          </el-form-item>
          <el-form-item>
            <el-button 
              type="primary" 
              size="large" 
              :loading="isLoading" 
              @click="handleLoginClick"
              class="login-button"
            >
              登录
            </el-button>
          </el-form-item>
          <div class="login-footer">
            <span @click="goToRegister">没有账户？立即注册</span>
          </div>
        </el-form>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElForm, ElFormItem, ElMessage, ElInput, ElButton, FormRules } from 'element-plus';
import { User, Lock } from '@element-plus/icons-vue';
import { login, loginWithTurnstile } from '../api/auth';
import { useAuthStore } from '@/stores/authStore';
import { useUserStore } from '@/stores/userStore';



const router = useRouter();
const loginFormRef = ref<InstanceType<typeof ElForm> | null>(null);
const isLoading = ref(false);
const authStore = useAuthStore();
const turnstileContainerRef = ref<HTMLElement>()


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

// 登录表单数据
const loginForm = reactive({
  username: '',
  password: '',
  verifyCode: ''
});

// 登录表单规则
const loginRules = reactive<FormRules>({
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 4, max: 16, message: '用户名长度在 4 到 16 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 8, max: 16, message: '密码长度在 8 到 16 个字符', trigger: 'blur' }
  ]
});

const handleLoginClick = () => {
  const turnstileToken = (window as any).turnstile?.getResponse();
  if (!turnstileToken) {
    ElMessage.error('请完成人机验证');
  }
  
  if (!loginFormRef.value) return;
  loginFormRef.value.validate().then(()=>{
    isLoading.value = true;
    loginWithTurnstile({
      username: loginForm.username,
      password: loginForm.password,
      cfTurnstileToken: turnstileToken
    }).then((res) => {

      authStore.$patch({
        authData: {
          token: res.data.token,
          expireAt: res.data.expireAt
        }
      });
      
      const redirect = router.currentRoute.value.query.redirect as string;
      useUserStore().fetchUserProfile();
      ElMessage.success('登录成功');
      console.log(redirect)
      router.push(redirect || '/');
    }).catch((error: any) => {
      console.log(error)
      ElMessage.error(error.response?.data?.message || '登录失败');
    }).finally(() => {
      isLoading.value = false;
      // (window as any).turnstile?.reset();
    });
  })
};

// 跳转到注册页面
const goToRegister = () => {
  router.push('/register');
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

/* 右侧登录表单容器 */
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

.captcha-container {
  display: flex;
  align-items: center;
  height: 40px;
}

.captcha-image {
  width: 100%;
  height: 100%;
  cursor: pointer;
  border-radius: 4px;
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
