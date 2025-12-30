import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router';
import MainLayout from '../layout/MainLayout.vue';
import Dashboard from '../views/Dashboard.vue';
import { useAuthStore } from '../stores/authStore';

// 定义路由记录
const routes: Array<RouteRecordRaw> = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: {
      title: '登录',
      requiresAuth: false
    }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue'),
    meta: {
      title: '注册',
      requiresAuth: false
    }
  },
  {
    path: '/',
    component: MainLayout,
    meta: {
      requiresAuth: true
    },
    children: [
      {
        path: '',
        redirect: '/dashboard'
      },
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: Dashboard,
        meta: {
          title: '仪表盘'
        }
      },
      {
        path: 'ship',
        name: 'Ship',
        component: () => import('../views/ship/ShipList.vue'),
        meta: {
          title: '船舶管理'
        }
      },
      {
        path: 'port',
        name: 'Port',
        component: () => import('../views/port/PortList.vue'),
        meta: {
          title: '港口管理'
        }
      },
      {
        path: 'maintenance',
        name: 'Maintenance',
        component: () => import('../views/maintenance/MaintenanceList.vue'),
        meta: {
          title: '维护管理'
        }
      },
      {
        path: 'voyage',
        name: 'Voyage',
        component: () => import('../views/voyage/VoyageList.vue'),
        meta: {
          title: '航行管理'
        }
      },
      {
        path: 'user',
        name: 'User',
        component: () => import('../views/User.vue'),
        meta: {
          title: '用户管理'
        }
      },
       {
        path: 'user/profile',
        name: 'UserProfile',
        component: () => import('../views/user/Profile.vue'),
        meta: {
          title: '个人中心'
        }
      },
      {
        path: 'user/avatar',
        name: 'UserAvatar',
        component: () => import('../views/user/Avatar.vue'),
        meta: {
          title: '修改头像'
        }
      },
      {
        path: 'user/password',
        name: 'UserPassword',
        component: () => import('../views/user/Password.vue'),
        meta: {
          title: '修改密码'
        }
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('../views/NotFound.vue'),
    meta: {
      title: '404'
    }
  }
];

// 创建路由实例
const router = createRouter({
  history: createWebHistory(),
  routes
});

// 路由守卫
router.beforeEach((to, _from, next) => {
  document.title = `${to.meta.title || '船舶管理系统'} - 船舶管理系统`;
  if (to.matched.some(record => record.meta.requiresAuth)) {
    const token = useAuthStore().authData.token   
    console.log(token)
    if (!token) {
      next({
        path: '/login',
        query: {
          redirect: to.fullPath
        }
      });
    } else {
      next();
    }
  } else {
    next();
  }
});

export default router;
