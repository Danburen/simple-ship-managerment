import { defineStore } from 'pinia'
import { getCurrentUserProfile, updateUserProfile } from '../api/user'
import { ElMessage } from 'element-plus'
import type { PresignedUrl } from '../types'
import { ref } from 'vue'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'


export interface userInfo {
  id: number,
  username: string,
  nickname: string,
  avatar: PresignedUrl | null,
}

export const useUserStore = defineStore('user',() => {
  const profile = ref({
    id: 0,
    username: '',
    nickname: '',
    avatar: null as PresignedUrl,
  })

  const loading = ref(false)

  const fetchUserProfile = async () => {
    loading.value = true
    getCurrentUserProfile().then((res)=>{
      console.log('获取用户资料成功:', res)
      profile.value = {
        ...res.data,
        avatar: res.data.avatar || null
      }
    }).catch((error)=>{
      ElMessage.error('获取用户资料失败')
      console.error('获取用户资料失败:', error)
    }).finally(()=>{
      loading.value = false
    })
  }

  const clearProfile = () => {
    profile.value = {
      id: 0,
      username: '',
      nickname: '',
      avatar: null as PresignedUrl,
    }
  }

  const updateUserNickname = async (nickname: string) => {
    loading.value = true
    updateUserProfile({ nickname }).then((res)=>{
      profile.value.nickname = res.data.nickname
      ElMessage.success('昵称更新成功')
    }).catch((error)=>{
      ElMessage.error('昵称更新失败')
      console.error('昵称更新失败:', error)
    }).finally(()=>{
      loading.value = false
    })
  }

  const updateUserAvatar = async (avatar: PresignedUrl) => {
    loading.value = true
    avatar = {
      ...avatar,
    }
    loading.value = false
  }

  return {
    profile,
    loading,
    fetchUserProfile,
    clearProfile,
    updateUserNickname,
    updateUserAvatar,
  }
}, {
  persist: true
})
