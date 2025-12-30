// 通用字典选项类型
export interface DictOption {
  id: number;
  name: string;
  code?: string;
}

// 分页响应接口
export interface PageResponse<T> {
  content: T[];
  page: {
    size: number;
    totalElements: number;
    totalPages: number;
    number: number;
  };
}

export interface PresignedUrl {
  url: string;
  expireAt: number;
}