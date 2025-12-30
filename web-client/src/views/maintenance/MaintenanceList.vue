<template>
  <base-list-container
    title="维护管理"
    :show-add-button="true"
    add-button-text="新增维护"
    :show-pagination="true"
    :page-info="pagination"
    @add="handleAdd"
    @size-change="handleSizeChange"
    @current-change="handleCurrentChange"
  >
    <!-- 搜索条件 -->
    <el-form :inline="true" :model="searchForm" >
      <el-form-item label="船舶">
        <el-select style="width: 120px" v-model="searchForm.shipId" placeholder="请选择船舶ID">
          <el-option v-for="item in shipOptions" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="船舶名称">
        <el-input v-model="searchForm.shipName" placeholder="请输入船舶名称" />
      </el-form-item>
      <el-form-item label="维护描述">
        <el-input v-model="searchForm.description" placeholder="请输入维护描述" />
      </el-form-item>
      <el-form-item label="维护状态">
        <el-select style="width: 120px" v-model="searchForm.status" placeholder="请选择维护状态">
          <el-option v-for="item in maintenanceStatusOptions" :key="item" :label="item" :value="item" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSearch">查询</el-button>
        <el-button @click="handleReset">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="maintenanceList" style="width: 100%">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="shipName" label="船舶名称" />
      <el-table-column prop="type" label="维护类型" />
      <el-table-column prop="maintenanceDate" label="维护日期" />
      <el-table-column prop="status" label="维护状态">
        <template #default="scope">
          <el-tag :type="getStatusTagType(scope.row.status)">
            {{ scope.row.status }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="cost" label="维护费用" />
      <el-table-column prop="remark" label="备注" show-overflow-tooltip />
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="scope">
          <el-button type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </base-list-container>

  <!-- 维护表单弹窗 -->
  <base-dialog
    v-model="dialogVisible"
    :title="dialogTitle"
    width="800px"
    @confirm="handleDialogConfirm"
    @cancel="handleDialogCancel"
  >
    <div class="maintenance-form">
      <el-form :model="formData" label-width="120px">
        <el-form-item label="船舶名称" required>
          <el-select v-model="formData.shipId" placeholder="请选择船舶">
            <el-option
              v-for="ship in shipOptions"
              :key="ship.id"
              :label="ship.name"
              :value="ship.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="维护日期" required>
          <el-date-picker v-model="formData.maintenanceDate" type="date" placeholder="请选择维护日期" style="width: 100%" />
        </el-form-item>
        <el-form-item label="维护状态" required>
          <el-select v-model="formData.status" placeholder="请选择维护状态">
            <el-option v-for="item in maintenanceStatusOptions" :key="item" :label="item" :value="item" />
          </el-select>
        </el-form-item>
        <el-form-item label="维护费用" required>
          <el-input v-model.number="formData.cost" type="number" placeholder="请输入维护费用" />
        </el-form-item>
        <el-form-item label="维护人员">
          <el-input v-model="formData.maintenancePerson" placeholder="请输入维护人员" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="formData.remark" type="textarea" rows="3" placeholder="请输入备注" />
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
import { getMaintenanceList, createMaintenance, updateMaintenance, deleteMaintenance, getMaintenanceStatusOptions } from '../../api/maintenance';
import { getShipOptions } from '../../api/ship';
import type { DictOption } from '@/types';

// 加载状态
const loading = ref(false);

// 搜索表单
const searchForm = ref({
  shipId: null,
  shipName: null,
  description: null,
  status: null
});

// 分页信息
const pagination = ref({
  currentPage: 1,
  pageSize: 10,
  total: 0
});

// 维护列表数据
const maintenanceList = ref<any[]>([]);

// 弹窗状态
const dialogVisible = ref(false);
const dialogTitle = ref('新增维护');
const currentAction = ref<'add' | 'edit'>('add');
const currentEditId = ref<number | null>(null);

// 船舶选项
const shipOptions = ref<DictOption[]>([]);
const maintenanceStatusOptions = ref<string[]>([]);

// 表单数据
const formData = ref({
  shipId: null,
  type: '',
  maintenanceDate: '',
  status: '',
  cost: 0,
  maintenancePerson: '',
  remark: ''
});

// 获取状态标签类型
const getStatusTagType = (status: string) => {
  switch (status) {
    case '待维护':
      return 'warning';
    case '维护中':
      return 'info';
    case '已完成':
      return 'success';
    default:
      return 'info';
  }
};

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
const fetchOptionData = async () => {
  try {
    const res = await getMaintenanceStatusOptions();
    maintenanceStatusOptions.value = res.data || [];
    const response = await getShipOptions();
    shipOptions.value = response.data;
  } catch (error) {
    ElMessage.error('加载维护状态选项失败');
    console.error('加载维护状态选项失败:', error);
  }
};

const fetchData = async () => {
  loading.value = true;
  try {
    const res = await getMaintenanceList(
      pagination.value.currentPage - 1, 
      pagination.value.pageSize,
      searchForm.value.shipId,
      searchForm.value.shipName,
      searchForm.value.description,
      searchForm.value.status
    );
    maintenanceList.value = res.data.content.map((item: any) => ({
      id: item.id,
      shipId: item.ship.id,
      shipName: item.ship.name, // 使用实际船舶名称
      shipType: item.ship.type, // 船舶类型
      shipStatus: item.ship.status, // 船舶状态
      type: item.description, // 使用description作为维护类型描述
      maintenanceDate: formatDate(item.maintenanceDate), // 使用maintenanceDate字段
      status: item.status, // 使用船舶状态作为维护状态
      cost: item.cost, // 使用实际费用
      remark: item.description || '',
      createdAt: item.createdAt
    }));
    pagination.value.total = res.data.page.totalElements || 0;
  } catch (error) {
    ElMessage.error('获取维护列表失败');
    console.error('获取维护列表失败:', error);
  } finally {
    loading.value = false;
  }
};

// 打开新增弹窗
const handleAdd = () => {
  currentAction.value = 'add';
  dialogTitle.value = '新增维护';
  resetForm();
  dialogVisible.value = true;
};

// 打开编辑弹窗
const handleEdit = (row: any) => {
  currentAction.value = 'edit';
  dialogTitle.value = '编辑维护';
  currentEditId.value = row.id;
  // 填充表单数据
  formData.value = {
    shipId: row.shipId || '1', // 模拟数据
    type: row.type,
    maintenanceDate: row.maintenanceDate,
    status: row.status,
    cost: row.cost,
    maintenancePerson: row.maintenancePerson || '张三', // 模拟数据
    remark: row.remark
  };
  dialogVisible.value = true;
};

// 重置表单
const resetForm = () => {
  formData.value = {
    shipId: null,
    type: '',
    maintenanceDate: '',
    status: '待维护',
    cost: 0,
    maintenancePerson: '',
    remark: ''
  };
  currentEditId.value = null;
};

// 弹窗确认
const handleDialogConfirm = async () => {
  try {
    // 调用后端API保存维护记录
    const maintenanceData = {
      shipId: parseInt(formData.value.shipId),
      maintenanceDate: formData.value.maintenanceDate,  
      maintenanceType: formData.value.type,
      description: formData.value.remark,
      status: formData.value.status,
      cost: formData.value.cost
    };

    if (currentAction.value === 'add') {
      await createMaintenance(maintenanceData);
      ElMessage.success('新增维护成功');
    } else {
      await updateMaintenance(currentEditId.value!, maintenanceData);
      ElMessage.success('更新维护成功');
    }
    dialogVisible.value = false;
    fetchData();
  } catch (error) {
    ElMessage.error(currentAction.value === 'add' ? '新增失败' : '更新失败');
    console.error(`${currentAction.value === 'add' ? '新增' : '更新'}维护记录失败:`, error);
  }
};

// 弹窗取消
const handleDialogCancel = () => {
  dialogVisible.value = false;
  resetForm();
};

// 删除维护
const handleDelete = (row: any) => {
  ElMessageBox.confirm('确定要删除这条维护记录吗？', '删除确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteMaintenance(row.id);
      ElMessage.success('删除成功');
      fetchData();
    } catch (error) {
      ElMessage.error('删除失败');
      console.error('删除维护记录失败:', error);
    }
  }).catch(() => {
    // 取消删除
  });
};

// 查询
const handleSearch = () => {
  pagination.value.currentPage = 1;
  fetchData();
};

// 重置
const handleReset = () => {
  searchForm.value = {
    shipId: null,
    shipName: null,
    description: null,
    status: null
  };
  fetchData();
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
onMounted(async () => {
  fetchOptionData();
  fetchData();
});
</script>

<style scoped>
</style>