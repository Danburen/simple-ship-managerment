import request from './request';
import { PageResponse, DictOption } from '@/types';

// 船舶响应接口
export interface ShipResponse {
  id: number;
  name: string;
  type: string;
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

// 船舶请求参数接口
export interface ShipRequest {
  name: string;
  type: string;
  status?: string;
}

// 获取船舶列表
export const getShipList = (page: number = 0, size: number = 10, name?: string, type?: string, status?: string) => {
  return request.get<PageResponse<ShipResponse>>('/ship/list', {
    params: {
      page,
      size,
      name: name || undefined,
      type: type || undefined,
      status: status || undefined
    }
  });
};

// 获取船舶详情
export const getShipById = (id: number) => {
  return request.get<ShipResponse>(`/ship/${id}`);
};

// 新增船舶
export const createShip = (data: ShipRequest) => {
  return request.post<ShipResponse>('/ship', data);
};

// 更新船舶
export const updateShip = (id: number, data: ShipRequest) => {
  return request.put<ShipResponse>(`/ship/${id}`, data);
};

// 删除船舶
export const deleteShip = (id: number) => {
  return request.delete<string>(`/ship/${id}`);
};

// 获取船舶选项
export const getShipOptions = () => {
  return request.get<DictOption[]>('/ship/options');
};

export const getShipStatusOptions = () => {
  return request.get<string[]>('/ship/status/options');
};