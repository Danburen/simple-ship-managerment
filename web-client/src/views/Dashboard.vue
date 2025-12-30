<template>
  <div class="dashboard-container">
    <el-card class="welcome-card">
      <template #header>
        <div class="card-header">
          <span>欢迎使用船舶管理系统</span>
        </div>
      </template>
      <div class="welcome-content">
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="info-item">
              <h3>系统概述</h3>
              <p>船舶管理系统是一个用于管理船舶、港口、维护和航行记录的综合平台。</p>
              <p>通过本系统，您可以方便地管理船舶信息、查询港口数据、记录维护情况以及跟踪航行轨迹。</p>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <h3>快捷操作</h3>
              <el-row :gutter="10">
                <el-col :span="12">
                  <el-button type="primary" plain @click="$router.push('/ship')">
                    <el-icon><Ship /></el-icon>
                    查看船舶
                  </el-button>
                </el-col>
                <el-col :span="12">
                  <el-button type="success" plain @click="$router.push('/port')">
                    <el-icon><Location /></el-icon>
                    查看港口
                  </el-button>
                </el-col>
                <el-col :span="12" style="margin-top: 10px;">
                  <el-button type="warning" plain @click="$router.push('/maintenance')">
                    <el-icon><Tools /></el-icon>
                    查看维护
                  </el-button>
                </el-col>
                <el-col :span="12" style="margin-top: 10px;">
                  <el-button type="info" plain @click="$router.push('/voyage')">
                    <el-icon><Map /></el-icon>
                    查看航行
                  </el-button>
                </el-col>
              </el-row>
            </div>
          </el-col>
        </el-row>
      </div>
    </el-card>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stats-item">
            <div class="stats-icon ship-icon">
              <el-icon><Ship /></el-icon>
            </div>
            <div class="stats-info">
              <h4>船舶总数</h4>
              <p class="stats-number">{{ shipCount }}</p>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stats-item">
            <div class="stats-icon port-icon">
              <el-icon><Location /></el-icon>
            </div>
            <div class="stats-info">
              <h4>港口总数</h4>
              <p class="stats-number">{{ portCount }}</p>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stats-item">
            <div class="stats-icon maintenance-icon">
              <el-icon><Tools /></el-icon>
            </div>
            <div class="stats-info">
              <h4>维护记录</h4>
              <p class="stats-number">{{ maintenanceCount }}</p>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stats-item">
            <div class="stats-icon voyage-icon">
              <el-icon><Map /></el-icon>
            </div>
            <div class="stats-info">
              <h4>航行记录</h4>
              <p class="stats-number">{{ voyageCount }}</p>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { Ship, Location, Tools, MapLocation as Map } from '@element-plus/icons-vue';
import { getDashboardData } from '../api/dashboard';
import type { DashboardData } from '../api/dashboard';

// 统计数据
const shipCount = ref(0);
const portCount = ref(0);
const maintenanceCount = ref(0);
const voyageCount = ref(0);

// 加载数据
const loadDashboardData = async () => {
  try {
    const response = (await getDashboardData()).data;
    shipCount.value = response.shipCount;
    portCount.value = response.portCount;
    maintenanceCount.value = response.maintenanceCount;
    voyageCount.value = response.voyageCount;
  } catch (error) {
    console.error('获取仪表盘数据失败:', error);
  }
};

onMounted(() => {
  loadDashboardData();
});
</script>

<style scoped>
.dashboard-container {
  width: 100%;
}

.welcome-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.welcome-content {
  padding: 20px 0;
}

.info-item {
  padding: 10px;
  background-color: #fafafa;
  border-radius: 8px;
}

.info-item h3 {
  margin-top: 0;
  margin-bottom: 10px;
  font-size: 18px;
  color: #303133;
}

.info-item p {
  margin-bottom: 8px;
  color: #606266;
  line-height: 1.5;
}

.stats-item {
  display: flex;
  align-items: center;
  padding: 10px;
}

.stats-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  margin-right: 20px;
}

.ship-icon {
  background-color: rgba(64, 158, 255, 0.1);
  color: #409eff;
}

.port-icon {
  background-color: rgba(103, 194, 58, 0.1);
  color: #67c23a;
}

.maintenance-icon {
  background-color: rgba(230, 162, 60, 0.1);
  color: #e6a23c;
}

.voyage-icon {
  background-color: rgba(144, 147, 153, 0.1);
  color: #909399;
}

.stats-info h4 {
  margin: 0;
  font-size: 16px;
  color: #606266;
  font-weight: normal;
}

.stats-number {
  margin: 5px 0 0 0;
  font-size: 28px;
  font-weight: bold;
  color: #303133;
}
</style>
