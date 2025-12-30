<template>
  <div class="app-container">
    <el-container class="main-container" style="height: 100vh; overflow: hidden;">
      <!-- 左侧侧边栏 -->
      <el-aside :width="isCollapse ? '64px' : '200px'" class="sidebar-container">
        <div class="sidebar-header">
          <h2 v-if="!isCollapse">船舶管理系统</h2>
        </div>
        <el-menu
          :default-active="activeMenu"
          class="el-menu-vertical-demo"
          background-color="#545c64"
          text-color="#fff"
          active-text-color="#ffd04b"
          router
          :collapse="isCollapse"
          collapse-transition
        >
          <el-menu-item index="/dashboard">
            <el-icon><Dashboard /></el-icon>
            <span>仪表盘</span>
          </el-menu-item>
          <el-menu-item index="/ship">
            <el-icon><Ship /></el-icon>
            <span>船舶管理</span>
          </el-menu-item>
          <el-menu-item index="/port">
            <el-icon><Location /></el-icon>
            <span>港口管理</span>
          </el-menu-item>
          <el-menu-item index="/maintenance">
            <el-icon><Tools /></el-icon>
            <span>维护管理</span>
          </el-menu-item>
          <el-menu-item index="/voyage">
            <el-icon><Map /></el-icon>
            <span>航行管理</span>
          </el-menu-item>
          <el-sub-menu index="/user">
            <template #title>
              <el-icon><User /></el-icon>
              <span>用户管理</span>
            </template>
            <el-menu-item index="/user/profile">
              <el-icon><User /></el-icon>
              <span>个人中心</span>
            </el-menu-item>
            <el-menu-item index="/user/avatar">
              <el-icon><Camera /></el-icon>
              <span>修改头像</span>
            </el-menu-item>
            <el-menu-item index="/user/password">
              <el-icon><Lock /></el-icon>
              <span>修改密码</span>
            </el-menu-item>
          </el-sub-menu>
        </el-menu>
      </el-aside>
      <!-- 主内容区域 - 嵌套容器 -->
      <el-container class="main-content-container">
        <el-header class="navbar-container">
            <div class="navbar-left">
              <el-icon @click="toggleSidebar" class="menu-toggle">
                <Menu />
              </el-icon>
            </div>
            <div class="navbar-right">
              <el-dropdown trigger="click">
                <span class="user-info">
                  <el-avatar :size="32" :src="userStore.profile.avatar?.url || 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'" />
                  <span class="username">{{ userStore.profile.nickname || userStore.profile.username || '用户名' }}</span>
                  <el-icon class="el-icon--right">
                    <ArrowDown />
                  </el-icon>
                </span>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item @click="$router.push('/user/profile')">
                      <el-icon><User /></el-icon>
                      个人中心
                    </el-dropdown-item>
                    <el-dropdown-item @click="$router.push('/user/avatar')">
                      <el-icon><Camera /></el-icon>
                      修改头像
                    </el-dropdown-item>
                    <el-dropdown-item @click="$router.push('/user/password')">
                      <el-icon><Lock /></el-icon>
                      修改密码
                    </el-dropdown-item>
                    <el-dropdown-item divided>
                      <el-icon><Setting /></el-icon>
                      系统设置
                    </el-dropdown-item>
                    <el-dropdown-item divided @click="handleLogout" class="logout-item">
                      <el-icon><CircleClose /></el-icon>
                      退出登录
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>
        </el-header>
        <!-- 页面内容 -->
        <el-main class="main-content app-main">
           <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { ElMessage } from 'element-plus';
import {
  DataBoard as Dashboard,
  Ship,
  Location,
  Tools,
  MapLocation as Map,
  User,
  Menu,
  ArrowDown,
  Setting,
  SwitchButton,
  Camera,
  Lock,
  CircleClose
} from '@element-plus/icons-vue';
import { useUserStore } from '../stores/userStore';
import { useAuthStore } from '../stores/authStore';
import request from '../api/request';

const router = useRouter();
const route = useRoute();
const userStore = useUserStore();

// 侧边栏是否折叠
const isCollapse = ref(false);

// 当前激活的菜单
const activeMenu = computed(() => {
  return route.path;
});

// 切换侧边栏折叠状态
const toggleSidebar = () => {
  isCollapse.value = !isCollapse.value;
};

// 退出登录
const handleLogout = async () => {
  useAuthStore().logout();
};


</script>

<style scoped>
.app-container {
  width: 100%;
  height: 100vh;
  overflow: hidden;
}

/* 主容器默认横向排列 */
.main-container {
  display: flex;
  flex-direction: row;
  height: 100vh;
  overflow: hidden;
}

.sidebar-container {
  background-color: #545c64;
  border-right: none;
  transition: width 0.3s;
  /* 固定侧边栏高度 */
  height: 100%;
}

.sidebar-header {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #409eff;
  color: white;
  font-size: 18px;
}

.sidebar-header h2 {
  margin: 0;
  font-size: 18px;
  font-weight: 500;
  transition: opacity 0.3s;
}

/* 主内容容器纵向排列 */
.main-content-container {
  display: flex;
  flex-direction: column;
  flex: 1;
  height: 100%;
  overflow: hidden;
}

.navbar-container {
  background-color: white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  width: 100%;
  height: 60px;
  box-sizing: border-box;
}

.navbar-left {
  display: flex;
  align-items: center;
}

.menu-toggle {
  font-size: 20px;
  cursor: pointer;
  margin-right: 20px;
}

.navbar-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 8px 12px;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.user-info:hover {
  background-color: #f5f7fa;
}

.username {
  margin-left: 8px;
  margin-right: 4px;
}

/* 修改主内容区域样式，确保它能占据剩余宽度 */
.main-content {
  background-color: #f5f7fa;
  padding: 20px;
  overflow: auto;
  flex: 1;
  min-width: 0;
  width: 100%;
  box-sizing: border-box;
}

/* 确保el-main能正确扩展 */
:deep(.el-main) {
  flex: 1;
  width: 100%;
  padding: 0;
  margin: 0;
}

/* 调整内容区域的padding */
.main-content {
  padding: 20px;
}
</style>
