import axios, { AxiosInstance, AxiosResponse } from 'axios';
import router from '../router';
import { useAuthStore } from '../stores/authStore';

const service: AxiosInstance = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 5000,
  headers: {
    'Content-Type': 'application/json'
  },
  withCredentials: true
});

const publicPaths = ['/auth/login', '/auth/register', '/auth/captcha'];

service.interceptors.request.use(
  (config) => {
    const isPublicPath = publicPaths.some(path => config.url?.includes(path));
    const token = useAuthStore().authData.token;
    if (token && !isPublicPath) {
      (config.headers as any)['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    console.error('请求错误:', error);
    return Promise.reject(error);
  }
);

// 响应拦截器
service.interceptors.response.use(
  (response: AxiosResponse) => {
    const res = response.data;
    
    if (res.code && res.code !== '200') {
      console.error('业务错误:', res.message);
      return Promise.reject(new Error(res.message || 'Error'));
    }
    return res;
  },
  (error) => {
    console.error('响应错误:', error);
    if (error.response && error.response.status === 401) {
      router.push({
        path: '/login',
        query: {
          redirect: router.currentRoute.value.fullPath
        }
      });
    }
    return Promise.reject(error);
  }
);

export default service;