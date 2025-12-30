import request from './request';
import { PageResponse, DictOption } from '@/types';

// 港口响应接口
interface PortResponse {
  id: number;
  name: string;
  location: string;
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

// 港口请求参数接口
interface PortRequest {
  name: string;
  location?: string;
}

// 获取港口列表
export const getPortListApi = (page: number = 0, size: number = 10, name?: string, location?: string) => {
  return request.get<PageResponse<PortResponse>>('/port/list', {
    params: {
      page,
      size,
      name: name || undefined,
      location: location || undefined
    }
  });
};

// 获取港口详情
export const getPortById = (id: number) => {
  return request.get<PortResponse>(`/port/${id}`);
};

// 新增港口
export const createPort = (data: PortRequest) => {
  return request.post<PortResponse>('/port', data);
};

// 更新港口
export const updatePort = (id: number, data: PortRequest) => {
  return request.put<PortResponse>(`/port/${id}`, data);
};

// 删除港口
export const deletePort = (id: number) => {
  return request.delete<string>(`/port/${id}`);
};

// 获取港口选项
export const getPortOptions = () => {
  return request.get<DictOption[]>('/port/options');
};