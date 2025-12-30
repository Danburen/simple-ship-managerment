
<template>
  <el-card class="user-password">
    <template #header>
      <div class="card-header">
        <h2>修改密码</h2>
      </div>
    </template>
    <el-form ref="formRef" :model="passwordForm" :rules="rules" label-width="120px" class="password-form">
      <el-form-item label="旧密码" prop="oldPassword">
        <el-input v-model="passwordForm.oldPassword" type="password" placeholder="请输入旧密码" show-password />
      </el-form-item>
      <el-form-item label="新密码" prop="newPassword">
        <el-input v-model="passwordForm.newPassword" type="password" placeholder="请输入新密码" show-password />
      </el-form-item>
      <el-form-item label="确认新密码" prop="confirmPassword">
        <el-input v-model="passwordForm.confirmPassword" type="password" placeholder="请再次输入新密码" show-password />
      </el-form-item>
      <el-form-item class="form-actions">
        <el-button type="primary" @click="showCaptcha = true">提交</el-button>
      </el-form-item>
    </el-form>
  </el-card>
  <CaptchaDialog v-model="showCaptcha" @confirm="handlePwdChange" />
</template>

<script setup lang="ts">
import { ElMessage, FormRules, FormInstance } from 'element-plus';
import { ref } from 'vue';
import CaptchaDialog from '@/components/CaptchaDialog.vue';
import { updatePassword } from '@/api/user';
const formRef = ref<FormInstance>();
const showCaptcha = ref(false);

const passwordForm = ref({
  oldPassword: '',
  newPassword: '',
  confirmPassword: '',
  verifyCode: '',
});

const rules = ref<FormRules>({
  oldPassword: [
    { required: true, message: '请输入旧密码', trigger: 'blur' },
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '新密码长度不能小于6位', trigger: 'blur' },
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    { validator: (rule: any, value: string, callback: (error?: string | Error) => void) => {
      if (value !== passwordForm.value.newPassword) {
        console.log('两次输入的新密码不一致', value, passwordForm.value.newPassword);
        callback(new Error('两次输入的新密码不一致'));
      } else {
        callback();
      }
    }, message: '两次输入的新密码不一致', trigger: 'blur' },
  ],
});

const handlePwdChange = (captcha: string, callback: (boolean) => void) => {
  passwordForm.value.verifyCode = captcha;
  formRef.value?.validate((valid) => {
    if (valid) {
      updatePassword(passwordForm.value).then(() => {
        ElMessage.success('密码修改成功');
        showCaptcha.value = false;
        passwordForm.value = {
          oldPassword: '',
          newPassword: '',
          confirmPassword: '',
          verifyCode: '',
        };
      }).catch(() => {
        ElMessage.error('密码修改失败');
      });
    } else {
      ElMessage.warning('请填写正确的密码');
    }
  });
};

</script>

<style scoped>
.user-password {
  max-width: 600px;
  margin: 0 auto;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.password-form {
  margin-top: 20px;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}
</style>