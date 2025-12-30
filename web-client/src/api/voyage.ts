import request from './request';
import type { PageResponse, DictOption } from '@/types/index';

// 航行记录响应接口
interface VoyageResponse {
  id: number;
  shipId: number;
  departurePort: string;
  arrivalPort: string;
  departureTime: string;
  arrivalTime: string;
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

// 航行记录请求参数接口
interface VoyageRequest {
  shipId: number;
  departurePortId: number;
  arrivalPortId: number;  
  departureTime: string;
  arrivalTime: string;
  status: string;
  log: string;
}

// 获取航行记录列表
export const getVoyageList = (page: number = 0, size: number = 10, shipId?: number, departurePortId?: number, arrivalPortId?: number, status?: string) => {
  return request.get<PageResponse<VoyageResponse>>('/voyage/list', {
    params: {
      page,
      size,
      shipId,
      departurePortId,
      arrivalPortId,
      status
    }
  });
};

// 获取航行记录详情
export const getVoyageById = (id: number) => {
  return request.get<VoyageResponse>(`/voyage/${id}`);
};

// 新增航行记录
export const createVoyage = (data: VoyageRequest) => {
  console.log(data);
  return request.post<VoyageResponse>('/voyage', data);
};

// 更新航行记录
export const updateVoyage = (id: number, data: VoyageRequest) => {
  return request.put<VoyageResponse>(`/voyage/${id}`, data);
};

// 删除航行记录
export const deleteVoyage = (id: number) => {
  return request.delete<string>(`/voyage/${id}`);
};

// 获取船舶选项
export const getShipOptions = () => {
  return request.get<DictOption[]>('/ship/options');
};

// 获取港口选项
export const getPortOptions = () => {
  return request.get<DictOption[]>('/port/options');
};

export const getVoyageStatusOptions = () => {
  return request.get<String[]>('/voyage/status/options');
};
