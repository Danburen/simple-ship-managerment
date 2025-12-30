import request from './request';

// 仪表盘数据响应接口
export interface DashboardData {
  shipCount: number;
  portCount: number;
  maintenanceCount: number;
  voyageCount: number;
}

// 获取仪表盘统计数据
export const getDashboardData = () => {
  return request.get<DashboardData>('/dashboard');
};
