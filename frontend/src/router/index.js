import Vue from 'vue'
import Router from 'vue-router'
import Login from '@/components/Login'
import store from '../store'
import Doctor from "../components/Doctor";
import HeadNurse from "../components/HeadNurse";
import WardNurse from "../components/WardNurse";
import EmergencyNurse from "../components/EmergencyNurse";

import PatientCard from "../components/PatientCard";

import HeadNurseCard from "../components/HeadNurseCard";
import WardNurseCard from "../components/WardNurseCard";
import Bed from "../components/Bed";
import MessageCard from "../components/MessageCard";
import PatientCardForEmergency from "../components/PatientCardForEmergency";
import PatientCardForWard from "../components/PatientCardForWard";

Vue.use(Router)

export const router = new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      name:'login',
      component: Login
    },
    {
      path: '/doctor',
      name:'doctor',
      component: Doctor,
      children:[
        {
          path:'patients',
          name:'patient-card',
          component:PatientCard,
        },
        {
          path:'headNurses',
          name:'head-nurse-card',
          component:HeadNurseCard,
        },
        {
          path:'wardNurses',
          name:'ward-nurse',
          component:WardNurseCard,
        },
        {
          path:'messages',
          name:"messages",
          component: MessageCard
        }
      ],
      meta: {
        requireAuth: true // 需要登录权限
      }
    },
    {
      path: '/headNurse',
      name:'headNurse',
      component: HeadNurse,
      children:[
        {
          path:'patients',
          name:'patient_card',
          component:PatientCard,
        },
        {
          path: 'wardNurses',
          name:'WardNurseCard',
          component: WardNurseCard,
        },
        {
          path: 'beds',
          name:'bed',
          component: Bed,
        },
        {
          path:'messages',
          name:"messages",
          component: MessageCard
        }
      ],
      meta: {
        requireAuth: true // 需要登录权限
      }
    },
    {
      path: '/wardNurse',
      name:'wardNurse',
      component: WardNurse,
      children: [
        {
          path:'patients',
          name:"patients",
          component: PatientCardForWard
        },
        {
          path:'messages',
          name:"messages",
          component: MessageCard
        }
      ],
      meta: {
        requireAuth: true // 需要登录权限
      }
    },
    {
      path: '/emergencyNurse',
      name: 'emergencyNurse',
      component: EmergencyNurse,
      children: [
        {
          path:'patients',
          name:"patients",
          component: PatientCardForEmergency
        },
        {
          path:'messages',
          name:"messages",
          component: MessageCard
        }
      ],
      meta: {
        requireAuth:true // 需要登录权限
      }
    },
  ]
})

/**
 * 重写路由的replace方法
 */
// const routerReplace = Router.prototype.replace
// Router.prototype.replace = function replace(location) {
//   return routerReplace.call(this, location).catch(error=> error)
// }

// 前端登录拦截
router.beforeEach(function (to, from, next) {
  //判断该路由是否需要登录权限
  if (to.matched.some(record => record.meta.requireAuth)) {
    if (store.state.token) {
      // 判断缓存里面是否有 userName
      // 在登录的时候设置它的值
      next()
    } else {
      next({
        path: '/',// 将跳转的路由path作为参数，登录成功后跳转到该路由
        query: { redirect: to.fullPath }
      })
    }
  } else {
    next()
  }
})
