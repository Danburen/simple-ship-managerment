import request from './request';
import { PageResponse } from '@/types';

// 维护记录响应接口
interface MaintenanceResponse {
  id: number;
  shipId: number;
  maintenanceType: string;
  description?: string;
  status: string;
  createdAt: {
    seconds: number;
    nanos: number;
    epochSecond: number;
    nano: number;
  };
  updatedAt: {
    seconds: number;
    nanos: number;
    epochSecond: number;
    nano: number;
  };
  isDeleted: boolean;
}

// 维护记录请求参数接口
interface MaintenanceRequest {
  shipId: number;
  maintenanceType: string;
  description?: string;
  status: string;
}

// 获取维护记录列表
export const getMaintenanceList = (page: number = 0, size: number = 10, shipId?: number, shipName?: string, description?: string, status?: string) => {
  console.log(page, size, shipId, shipName, description, status);
  return request.get<PageResponse<MaintenanceResponse>>('/maintenance/list', {
    params: {
      page,
      size,
      shipId,
      shipName,
      description,
      status
    }
  });
};

// 获取维护记录详情
export const getMaintenanceById = (id: number) => {
  return request.get<MaintenanceResponse>(`/maintenance/${id}`);
};

// 新增维护记录
export const createMaintenance = (data: MaintenanceRequest) => {
  return request.post<MaintenanceResponse>('/maintenance', data);
};

// 更新维护记录
export const updateMaintenance = (id: number, data: MaintenanceRequest) => {
  return request.put<MaintenanceResponse>(`/maintenance/${id}`, data);
};

// 删除维护记录
export const deleteMaintenance = (id: number) => {
  return request.delete<string>(`/maintenance/${id}`);
};

export const getMaintenanceStatusOptions = () => {
  return request.get<string[]>('/maintenance/status/options');
};