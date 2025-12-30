<template>
  <base-list-container
    title="船舶"
    :show-add-button="true"
    add-button-text="新增船舶"
    :show-pagination="true"
    :total="pagination.total"
    :page-size="pagination.pageSize"
    :current-page="pagination.currentPage"
    @add="handleAdd"
    @size-change="handleSizeChange"
    @current-change="handleCurrentChange"
  >
    <!-- 搜索条件 -->
    <el-form :inline="true" :model="searchForm" class="search-form">
      <el-form-item label="船舶名称">
        <el-input v-model="searchForm.name" placeholder="请输入船舶名称" />
      </el-form-item>
      <el-form-item label="船舶类型">
        <el-input v-model="searchForm.type" placeholder="请输入船舶类型" />
      </el-form-item>
      <el-form-item label="状态">
        <el-select style="width: 150px;" v-model="searchForm.status" placeholder="请选择状态">
          <el-option v-for="item in shipStatusOptions" :key="item" :label="item" :value="item" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSearch">查询</el-button>
        <el-button @click="handleReset">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="shipList" style="width: 100%">
      <el-table-column prop="id" label="ID" width="80" align="center" />
      <el-table-column prop="name" label="船舶名称" min-width="150" />
      <el-table-column prop="type" label="船舶类型" min-width="120" />
      <el-table-column prop="status" label="状态" min-width="100" align="center">
        <template #default="scope">
          <el-tag :type="getButtonStatusType(scope.row.status)">
            {{ scope.row.status }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createdAt" label="创建时间" min-width="180">
        <template #default="scope">
          <span>{{ formatDate(scope.row.createdAt) }}</span>
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

  <!-- 船舶表单弹窗 -->
  <base-dialog
    v-model="dialogVisible"
    :title="dialogTitle"
    width="500px"
    @confirm="handleDialogConfirm"
    @cancel="handleDialogCancel"
  >
    <div class="ship-form">
      <el-form :model="formData" label-width="120px">
        <el-form-item label="船舶名称" required>
          <el-input v-model="formData.name" placeholder="请输入船舶名称" />
        </el-form-item>
        <el-form-item label="船舶类型" required>
          <el-input v-model="formData.type" placeholder="请输入船舶类型" />
        </el-form-item>
        <el-form-item label="状态" required>
          <el-select v-model="formData.status" placeholder="请选择状态">
            <el-option v-for="item in shipStatusOptions" :key="item" :label="item" :value="item" />
          </el-select>
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
import { getShipList as getShipListApi, deleteShip as deleteShipApi, createShip as createShipApi, updateShip as updateShipApi, getShipStatusOptions } from '@/api/ship';

// 加载状态
const loading = ref(false);

// 搜索表单
const searchForm = ref({
  name: '',
  type: '',
  status: ''
});

// 分页信息
const pagination = ref({
  currentPage: 1,
  pageSize: 10,
  total: 0
});

// 船舶列表数据
const shipList = ref<any[]>([]);
const shipStatusOptions = ref<String[]>([]);

// 弹窗状态
const dialogVisible = ref(false);
const dialogTitle = ref('新增船舶');
const currentAction = ref<'add' | 'edit'>('add');
const currentEditId = ref<number | null>(null);

// 表单数据
const formData = ref({
  name: '',
  type: '',
  status: '正常'
});

const getButtonStatusType = (status: string) => {
  switch (status) {
    case '正常':
      return 'success';
    case '维修中':
      return 'info';
    case '停运':
      return 'warning';
    case '已退役':
      return 'danger';
    default:
      return 'info';
  }
}

// 格式化时间
const formatDate = (dateObj: any) => {
  if (!dateObj) return '';
  let date;
  if (typeof dateObj === 'string') {
    // ISO格式字符串
    date = new Date(dateObj);
  } else if (dateObj.seconds !== undefined) {
    // 时间戳对象格式
    date = new Date(dateObj.seconds * 1000 + dateObj.nanos / 1000000);
  } else {
    date = new Date(dateObj);
  }
  return isNaN(date.getTime()) ? '' : date.toLocaleString('zh-CN');
};

// 查询数据
const fetchData = async () => {
  loading.value = true;
  try {
    const res = await getShipListApi(
      pagination.value.currentPage - 1, 
      pagination.value.pageSize, 
      searchForm.value.name, 
      searchForm.value.type, 
      searchForm.value.status
    );
    const statusRes = await getShipStatusOptions();
    shipStatusOptions.value = statusRes.data || [];
    shipList.value = res.data.content || [];
    pagination.value.total = res.data.page.totalElements || 0;
  } catch (error) {
    console.error('获取船舶列表失败:', error);
    ElMessage.error('获取船舶列表失败');
    shipList.value = [];
    pagination.value.total = 0;
  } finally {
    loading.value = false;
  }
};

// 查询
const handleSearch = () => {
  pagination.value.currentPage = 1;
  console.log(searchForm.value);
  fetchData();
};

// 重置
const handleReset = () => {
  searchForm.value = {
    status: '',
    name: '',
    type: ''
  };
  pagination.value.currentPage = 1;
  fetchData();
};

// 打开新增弹窗
const handleAdd = () => {
  currentAction.value = 'add';
  dialogTitle.value = '新增船舶';
  resetForm();
  dialogVisible.value = true;
};

// 打开编辑弹窗
const handleEdit = (row: any) => {
  currentAction.value = 'edit';
  dialogTitle.value = '编辑船舶';
  currentEditId.value = row.id;
  // 填充表单数据
  formData.value = {
    name: row.name,
    type: row.type,
    status: row.status
  };
  dialogVisible.value = true;
};

// 重置表单
const resetForm = () => {
  formData.value = {
    name: '',
    type: '',
    status: '正常'
  };
  currentEditId.value = null;
};

// 弹窗确认
const handleDialogConfirm = async () => {
  try {
    if (currentAction.value === 'add') {
      await createShipApi(formData.value);
      ElMessage.success('新增船舶成功');
    } else {
      await updateShipApi(currentEditId.value!, formData.value);
      ElMessage.success('更新船舶成功');
    }
    dialogVisible.value = false;
    fetchData();
  } catch (error) {
    ElMessage.error(currentAction.value === 'add' ? '新增失败' : '更新失败');
    console.error(`${currentAction.value === 'add' ? '新增' : '更新'}船舶失败:`, error);
  }
};

// 弹窗取消
const handleDialogCancel = () => {
  dialogVisible.value = false;
  resetForm();
};

// 删除船舶
const handleDelete = (row: any) => {
  ElMessageBox.confirm('确定要删除这条船舶吗？', '删除确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteShipApi(row.id);
      ElMessage.success('删除船舶成功');
      fetchData();
    } catch (error) {
      ElMessage.error('删除失败');
      console.error('删除船舶失败:', error);
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

// 组件挂载时获取数据
onMounted(() => {
  fetchData();
});
</script>

<style scoped>

.ship-form {
  max-width: 500px;
}

:deep(.el-form-item) {
  margin-bottom: 20px;
}
</style>