import Vue from 'vue'
import Router from 'vue-router'
const  index =  resolve => require.ensure( [], () =>resolve(require('@/components/Index/Index')))

const  nav =  resolve => require.ensure( [], () =>resolve(require('@/components/navBar/nav')))

// 首页
const  home =  resolve => require.ensure( [], () =>resolve(require('@/components/Home/index')))
//登录
const  login =  resolve => require.ensure( [], () =>resolve(require('@/components/Home/login')))
// 开发统计
const  developerStatistics =  resolve => require.ensure( [], () =>resolve(require('@/components/developerStatistics/index')))
// 测试统计
const  testerStatistics =  resolve => require.ensure( [], () =>resolve(require('@/components/testerStatistics/index')))
// 项目统计
const  projectStatistics =  resolve => require.ensure( [], () =>resolve(require('@/components/projectStatistics/index')))
// 迭代关联
const  iteration =  resolve => require.ensure( [], () =>resolve(require('@/components/iteration/index')))
// 人数分配
const  distributePeople =  resolve => require.ensure( [], () =>resolve(require('@/components/distributePeople/index')))

Vue.use(Router)
export default new Router({
  routes: [
    {
      path: '/',
      name: 'Index',
      component: index,
      redirect: '/app/jdg/home'
    },
    {
      path: '/login',
      name: 'login',
      component: login,
    },
    {
      path: '/app/jdg',
      component: nav,
      meta: {requireAuth: true},
      children:[
        {
          desc: '首页',
          path: 'home',
          name: 'home',
          component: home,
        },
        {
          desc: '开发统计',
          path: 'developerStatistics',
          name: 'developerStatistics',
          component: developerStatistics,
        },
        {
          desc: '测试统计',
          path: 'testerStatistics',
          name: 'testerStatistics',
          component: testerStatistics,
        },
        {
          desc: '项目统计',
          path: 'projectStatistics',
          name: 'projectStatistics',
          component: projectStatistics,
        },
        {
          desc: '迭代关联',
          path: 'iteration',
          name: 'iteration',
          component: iteration,
        },
        {
          desc: '人员分类',
          path: 'distributePeople',
          name: 'distributePeople',
          component: distributePeople,
        },
      ]
    },
  ]
})
