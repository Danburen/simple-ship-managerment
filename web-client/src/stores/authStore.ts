import { defineStore } from 'pinia'
import {  ref } from 'vue'
import { login, register } from '../api/auth'
import type { LoginParams, LoginResponse, RegisterParams } from '../api/auth'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'

import { useUserStore } from './userStore';

export interface AuthorizationData {
    token: string,
    expireAt: number
}

export const useAuthStore = defineStore('auth', () => {
  const authData = ref<AuthorizationData>({
    token: '',
    expireAt: 0
  })

  const tryLogin = async(reqBody: LoginParams) => {
    return login(reqBody).then((res) => {
        authData.value = {
            token: res.data.token,
            expireAt: res.data.expireAt
        };
        return Promise.resolve(res)
    }).catch((err) => {
      return Promise.reject(err)
    })
  }

  const tryRegister = async(reqBody: RegisterParams) => {
    return register(reqBody).then((res) => {
      return Promise.resolve(res.data)
    }).catch((err) => {
      return Promise.reject(err)
    })
  }     

  const logout = () => {
    authData.value = {
      token: '',
      expireAt: 0
    },
    useUserStore().clearProfile();
  }

  return { authData, tryLogin, tryRegister, logout }
}, {
  persist: {
    key: 'auth',
    storage: localStorage,
  }
})
