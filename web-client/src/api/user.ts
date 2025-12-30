import request from './request';
import type { PresignedUrl } from '../types';

// 用户资料响应接口
export interface UserProfile {
  id: number;
  username: string;
  nickname: string;
  avatar: PresignedUrl;
}

// 更新个人资料请求接口
export interface UpdateProfileRequest {
  nickname: string;
}

// 修改密码请求接口
export interface UpdatePasswordRequest {
  oldPassword: string;
  newPassword: string;
  confirmPassword: string;
  verifyCode: string;
}

// PostPolicyDto接口
export interface PostPolicyDto {
  url: string;
  method: string;
  key: string;
}


// 获取当前用户资料
export const getCurrentUserProfile = () => {
  return request.get<UserProfile>('/user');
};

// 更新用户资料
export const updateUserProfile = (data: UpdateProfileRequest) => {
  return request.put<UserProfile>('/user', data);
};

// 修改密码
export const updatePassword = (data: UpdatePasswordRequest) => {
  return request.post<void>('/user/change-pwd', data);
};

// 获取头像URL
export const getAvatarUrl = () => {
  return request.get<PresignedUrl>('/user/avatar');
};

// 获取头像上传策略
export const getAvatarUploadPolicy = (fileSuffix: string) => {
  return request.get<PostPolicyDto>('/user/avatar/upload', {
    params: {
      fileSuffix
    }
  });
};

// 更新头像URL
export const updateAvatarUrl = (avatarKey: string) => {
  return request.post<void>('/user/avatar', { avatarKey });
};
