<template>
  <base-list-container
    title="航行记录"
    :show-add-button="true"
    add-button-text="新增航行记录"
    :show-pagination="true"
    :page-info="pagination"
    @add="handleAdd"
    @size-change="handleSizeChange"
    @current-change="handleCurrentChange"
  >
    <el-form :inline="true" :model="searchForm" class="search-form">
      <el-form-item label="船舶">
        <el-select style="width: 120px" v-model="searchForm.shipId" placeholder="请选择船舶">
          <el-option v-for="ship in shipOptions" :key="ship.id" :label="ship.name" :value="ship.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="出发港口">
        <el-select style="width: 120px" v-model="searchForm.departurePortId" placeholder="请选择出发港口">
          <el-option v-for="port in portOptions" :key="port.id" :label="port.name" :value="port.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="到达港口">
        <el-select style="width: 120px" v-model="searchForm.arrivalPortId" placeholder="请选择到达港口">
          <el-option v-for="port in portOptions" :key="port.id" :label="port.name" :value="port.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态">
        <el-select style="width: 120px" v-model="searchForm.status" placeholder="请选择状态">
          <el-option v-for="status in voyageStatusOptions" :key="status" :label="status" :value="status" />
        </el-select>
      </el-form-item>
       <el-form-item>
        <el-button type="primary" @click="handleSearch">查询</el-button>
        <el-button @click="handleReset">重置</el-button>
      </el-form-item>
    </el-form>
  
    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="voyageList" style="width: 100%">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="ship.name" label="船舶名称" width="150" />
      <el-table-column prop="departurePort.name" label="出发港口" width="150" />
      <el-table-column prop="arrivalPort.name" label="到达港口" width="150" />
      <el-table-column prop="departureTime" label="出发时间" width="180">
        <template #default="scope">
          {{ formatDate(scope.row.departureTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="arrivalTime" label="到达时间" width="180">
        <template #default="scope">
          {{ formatDate(scope.row.arrivalTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100" align="center">
        <template #default="scope">
          <el-tag :type="getStatusType(scope.row.status)">
            {{ scope.row.status }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="log" label="航行日志" min-width="200" />
      <el-table-column prop="createdAt" label="创建时间" width="180">
        <template #default="scope">
          {{ formatDate(scope.row.createdAt) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180" fixed="right">
        <template #default="scope">
          <el-button type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </base-list-container>

  <!-- 航行记录表单弹窗 -->
  <base-dialog
    v-model="dialogVisible"
    :title="dialogTitle"
    width="800px"
    @confirm="handleDialogConfirm"
    @cancel="handleDialogCancel"
  >
    <div class="voyage-form">
      <el-form :model="formData" label-width="120px">
        <el-form-item label="船舶名称" required>
          <el-select v-model="formData.shipId" placeholder="请选择船舶">
            <el-option v-for="ship in shipOptions" :key="ship.id" :label="ship.name" :value="ship.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="出发港口" required>
          <el-select v-model="formData.departurePortId" placeholder="请选择出发港口">
            <el-option v-for="port in portOptions" :key="port.id" :label="port.name" :value="port.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="到达港口" required>
          <el-select v-model="formData.arrivalPortId" placeholder="请选择到达港口">
            <el-option v-for="port in portOptions" :key="port.id" :label="port.name" :value="port.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="出发时间" required>
          <el-date-picker v-model="formData.departureTime" type="datetime" placeholder="请选择出发时间" style="width: 100%" />
        </el-form-item>
        <el-form-item label="到达时间">
          <el-date-picker v-model="formData.arrivalTime" type="datetime" placeholder="请选择到达时间" style="width: 100%" />
        </el-form-item>
        <el-form-item label="航行状态" required>
          <el-select v-model="formData.status" placeholder="请选择航行状态">
            <el-option v-for="status in voyageStatusOptions" :key="status" :label="status" :value="status" />
          </el-select>
        </el-form-item>
        <el-form-item label="航行日志">
          <el-input v-model="formData.log" type="textarea" rows="3" placeholder="请输入航行日志" />
        </el-form-item>
      </el-form>
    </div>
  </base-dialog>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import BaseListContainer from '../../components/BaseListContainer.vue';
import BaseDialog from '../../components/BaseDialog.vue';
import { getVoyageList, createVoyage, updateVoyage, deleteVoyage } from '../../api/voyage';
import { DictOption } from '@/types';
import { getShipOptions } from '@/api/ship';
import { getPortOptions } from '@/api/port';
import { getVoyageStatusOptions } from '@/api/voyage';



// 加载状态
const loading = ref(false);

// 分页信息
const pagination = ref({
  currentPage: 1,
  pageSize: 10,
  total: 0
});

const searchForm = ref({
  shipId: null,
  departurePortId: null,
  arrivalPortId: null,
  status: null,
});

const shipOptions = ref<DictOption[]>([]);
const portOptions = ref<DictOption[]>([]);
const voyageStatusOptions = ref<String[]>([]);

// 航行记录列表数据
const voyageList = ref<any[]>([]);

// 弹窗状态
const dialogVisible = ref(false);
const dialogTitle = ref('新增航行记录');
const currentAction = ref<'add' | 'edit'>('add');
const currentEditId = ref<number | null>(null);

// 表单数据
const formData = ref({
  shipId: '',
  departurePortId: '',
  arrivalPortId: '',
  departureTime: '',
  arrivalTime: '',
  status: '',
  log: ''
});

// 格式化时间
const formatDate = (dateObj: any) => {
  if (!dateObj) return '';
  let date;
  if (typeof dateObj === 'string') {
    date = new Date(dateObj);
  } else if (dateObj.seconds !== undefined) {
    date = new Date(dateObj.seconds * 1000 + dateObj.nanos / 1000000);
  } else {
    date = new Date(dateObj);
  }
  return isNaN(date.getTime()) ? '' : date.toLocaleString('zh-CN');
};

// 获取状态标签类型
const getStatusType = (status: string) => {
  switch (status) {
    case '计划中':
      return 'info';
    case '航行中':
      return 'warning';
    case '已到达':
      return 'success';
    case '已完成':
      return 'success';
    default:
      return 'info';
  }
};

const fetchOptionData = async () => {
  await Promise.all([
    getShipOptions().then(res => shipOptions.value = res.data),
    getPortOptions().then(res => portOptions.value = res.data),
    getVoyageStatusOptions().then(res => voyageStatusOptions.value = res.data)
  ]);
}
// 查询数据
const fetchData = async () => {
  loading.value = true;
  try {
    // 调用后端API获取航行记录列表数据
    const res = await getVoyageList(
      pagination.value.currentPage - 1, 
      pagination.value.pageSize,
      searchForm.value.shipId,
      searchForm.value.departurePortId,
      searchForm.value.arrivalPortId,
      searchForm.value.status
    );
  // 处理数据，转换格式
  voyageList.value = res.data.content.map((item: any) => ({
    id: item.id,
    ship: item.ship, // 使用实际船舶对象
    shipId: item.ship.id,
    departurePort: item.departurePort, // 实际港口对象
    departurePortId: item.departurePort.id, // 港口ID
    arrivalPort: item.arrivalPort, // 实际港口对象
    arrivalPortId: item.arrivalPort.id, // 港口ID
    departureTime: item.departureTime,
    arrivalTime: item.arrivalTime,
    status: item.status, // 使用船舶状态作为航行状态
    log: item.log, // 使用实际航行日志
    createdAt: item.createdAt
  }));
    pagination.value.total = res.data.page.totalElements || 0;
  } catch (error) {
    ElMessage.error('获取航行记录列表失败');
    console.error('获取航行记录列表失败:', error);
  } finally {
    loading.value = false;
  }
};

// 打开新增弹窗
const handleAdd = async () => {
  currentAction.value = 'add';
  dialogTitle.value = '新增航行记录';
  resetForm();
  dialogVisible.value = true;
  await Promise.all([
    getShipOptions().then(res => shipOptions.value = res.data),
    getPortOptions().then(res => portOptions.value = res.data),
    getVoyageStatusOptions().then(res => voyageStatusOptions.value = res.data)
  ]);
};

// 打开编辑弹窗
const handleEdit = (row: any) => {
  currentAction.value = 'edit';
  dialogTitle.value = '编辑航行记录';
  currentEditId.value = row.id;
  // 填充表单数据
  formData.value = {
    shipId: row.shipId || '1', // 模拟数据
    departurePortId: row.departurePortId || '1', // 模拟数据
    arrivalPortId: row.arrivalPortId || '2', // 模拟数据
    departureTime: row.departureTime,
    arrivalTime: row.arrivalTime,
    status: row.status,
    log: row.log
  };
  dialogVisible.value = true;
};

// 重置表单
const resetForm = () => {
  formData.value = {
    shipId: '',
    departurePortId: '',
    arrivalPortId: '',
    departureTime: '',
    arrivalTime: '',
    status: '',
    log: ''
  };
  currentEditId.value = null;
};

// 弹窗确认
const handleDialogConfirm = async () => {
  try {
    // 调用后端API保存航行记录
    const voyageData = {
      shipId: parseInt(formData.value.shipId),
      departurePort: formData.value.departurePortId,
      arrivalPort: formData.value.arrivalPortId,
      departureTime: formData.value.departureTime,
      arrivalTime: formData.value.arrivalTime,
      status: formData.value.status
    };

    if (currentAction.value === 'add') {
      await createVoyage({
        shipId: parseInt(formData.value.shipId),
        departurePortId: parseInt(formData.value.departurePortId),
        arrivalPortId: parseInt(formData.value.arrivalPortId),
        departureTime: formData.value.departureTime,
        arrivalTime: formData.value.arrivalTime,
        status: formData.value.status,
        log: formData.value.log
      });
      ElMessage.success('新增航行记录成功');
    } else {
      await updateVoyage(currentEditId.value!, {
        shipId: parseInt(formData.value.shipId),
        departurePortId: parseInt(formData.value.departurePortId),
        arrivalPortId: parseInt(formData.value.arrivalPortId),
        departureTime: formData.value.departureTime,
        arrivalTime: formData.value.arrivalTime,
        status: formData.value.status,
        log: formData.value.log
      });
      ElMessage.success('更新航行记录成功');
    }
    dialogVisible.value = false;
    fetchData();
  } catch (error) {
    ElMessage.error(currentAction.value === 'add' ? '新增失败' : '更新失败');
    console.error(`${currentAction.value === 'add' ? '新增' : '更新'}航行记录失败:`, error);
  }
};

// 弹窗取消
const handleDialogCancel = () => {
  dialogVisible.value = false;
  resetForm();
};

// 删除航行记录
const handleDelete = (row: any) => {
  ElMessageBox.confirm('确定要删除这条航行记录吗？', '删除确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteVoyage(row.id);
      ElMessage.success('删除成功');
      fetchData();
    } catch (error) {
      ElMessage.error('删除失败');
      console.error('删除航行记录失败:', error);
    }
  }).catch(() => {
    // 取消删除
  });
};

// 分页大小变化
const handleSizeChange = (size: number) => {
  pagination.value.pageSize = size;
  fetchData();
};

// 当前页码变化
const handleCurrentChange = (current: number) => {
  pagination.value.currentPage = current;
  fetchData();
};

const handleSearch = () => {
  pagination.value.currentPage = 1;
  fetchData();
};

const handleReset = () => {
  searchForm.value = {
    shipId: '',
    departurePortId: '',
    arrivalPortId: '',
    status: ''
  };
  fetchData();
};

onMounted(() => {
  fetchData();
  fetchOptionData();
});
</script>

<style scoped>
.voyage-form {
  max-width: 800px;
}

:deep(.el-form-item) {
  margin-bottom: 20px;
}
</style>