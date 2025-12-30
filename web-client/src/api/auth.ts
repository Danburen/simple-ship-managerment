import request from './request';
import axios from 'axios';
// 登录请求参数接口
export interface LoginParams {
  username: string;
  password: string;
  verifyCode: string;
}

// 新的登录请求参数接口，支持Cloudflare Turnstile
export interface LoginParamsV2 {
  username: string;
  password: string;
}

// Cloudflare Turnstile登录请求参数接口
export interface LoginWithTurnstileParams {
  username: string;
  password: string;
  cfTurnstileToken: string;
}

// 注册请求参数接口
export interface RegisterParams {
  username: string;
  password: string;
  confirmPwd: string;
  nickname?: string;
  cfTurnstileToken?: string;
}

// 登录响应接口
export interface LoginResponse {
  token: string;
  expireAt: number;
}


export const getCaptcha = () => {
  return axios.get('http://localhost:8080/api/auth/captcha', {
    responseType: 'arraybuffer',
    withCredentials: true,
  });
};

export const login = (params: LoginParams) => {
  return request.post<LoginResponse>('/auth/login', params);
};

// 使用Cloudflare Turnstile的登录API
export const loginWithTurnstile = (params: LoginWithTurnstileParams) => {
  return request.post<LoginResponse>('/auth/login', {
    username: params.username,
    password: params.password,
    cfTurnstileToken: params.cfTurnstileToken,
  }, {
    headers: {
      'Content-Type': 'application/json'
    }
  });
};

export const register = (params: RegisterParams) => {
  return request.post<string>('/auth/register', params);
};

export const logout = () => {
  return request.post('/auth/logout');
};