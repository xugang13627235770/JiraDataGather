import axios from 'axios'
import { getQueryString, TreeUtil } from './util/tools'
import Vue from 'vue'
import App from './App'
import router from './router'
import store from './vuex/store'
import '@/element-ui'
import '@/element-ui-theme'
import citmsUI from 'citms-ui-package'

// 全局组件
import api from './api/api'
import * as tools from './util/tools'
import moment from 'moment'
import echarts from 'echarts'
import sliderLeft from './components/common/sliderLeft/sliderLeft'
// 复制到粘贴板插件
import VueClipboard from 'vue-clipboard2'

import { Sketch } from 'vue-color'
Vue.component('slider-left', sliderLeft)
VueClipboard.config.autoSetContainer = true
Vue.use(VueClipboard)

if (window.doMock && window.doMock === true) {
  mocker.mock(api)
}

Vue.prototype.api = api
Vue.prototype.tools = tools
Vue.prototype.$echarts = echarts

Vue.config.productionTip = false
Vue.use(citmsUI)
Vue.use(moment)

// window.$ = $;
window.Token = getQueryString('Token') || localStorage.getItem('TOKEN')

window.Skin = getQueryString('Skin') || localStorage.getItem('Skin')
if (window.Token) {
  localStorage.setItem('TOKEN', window.Token)
  localStorage.setItem('SKIN', window.Skin)
}

// 解决登录超时问题
// let startTime = ''
// let interval = 0
// window.addEventListener('click', function (e) {
//   if (startTime) {
//     interval = parseInt(`${moment().format('YYYYMMDDHHmmss')}`) - parseInt(startTime)
//     startTime = `${moment().format('YYYYMMDDHHmmss')}`
//   } else {
//     startTime = `${moment().format('YYYYMMDDHHmmss')}`
//   }
//   if (interval > 10 && window.Token) {
//     axios({
//       url: window.baseURL + '/smw/UAC/DelayTimeout',
//       method: 'post',
//       headers: {
//         'Authorization': 'Basic' + window.Token
//       }
//     }).then(res => {
//     })
//   }
// })

// 获取用户信息异步处理
// let initData = new Promise(resolve => {
//   axios({
//     url: window.baseURL + '/smw/UAC/GetUserInfo',
//     method: 'get',
//     headers: {
//       'Authorization': 'Basic ' + Token
//     }
//   }).then(res => {
//     if (res.data.code == 0) {
//       // 用户信息
//       var data = res.data
//       resolve({userInfo: data.result});
//     } else {
//       var local = window.location.href
//       if (local.indexOf('/common/GoLogin') == -1 || local.indexOf('login.html') == -1) {
//         if (window.top == self) {
//           window.location.href = window.baseURL + '/common/GoLogin?returnUri=' + local
//         } else {
//           window.parent.postMessage({ type: 'exit' }, '*') // 是否从集成框架退出
//         }
//       }
//     }
//   }).catch(err => {
//     console.log(err)
//   })
// })
let mainStyle = 'midNightBlue'
if (window.Skin) { mainStyle = window.Skin }
// 动态插入主体样式表
let head = document.getElementsByTagName('HEAD').item(0)
var style = document.createElement('link')
style.href = '/static/skin/' + mainStyle + '-min.css'
style.rel = 'stylesheet'
style.type = 'text/css'
head.appendChild(style)

let setTopNavActive = (data, url) => {
  let urlArr = url.split('/').slice(0, 3)
  url = urlArr.join('/')
  let pData = data.filter(item => item.path == url)
  if (pData.length > 0) {
    store.dispatch('setTopNavActive', pData[0].id)
  } else {
    data.forEach(item => {
      if (item.children) {
        let cData = item.children.filter(ntem => ntem.path == url)
        if (cData.length > 0) {
          store.dispatch('setTopNavActive', cData[0].id)
        }
      }
    })
  }
}
new Vue({
  el: '#app',
  components: {
    'sketch-picker': Sketch
  },
  router,
  store,
  watch: {
    '$route' (to, from) {
      // setTopNavActive(res.appMenus, to.path)
    }
  },
  components: { App },
  template: '<App/>',
  mounted () {
    let nowPath = window.location.pathname
    // setTopNavActive(res.appMenus, nowPath)
  }
})
// initData.then(res => {
//   store.dispatch('setUserInfo', res.userInfo)
//   // store.dispatch('setAppMenus', res.appMenus)
//   // store.dispatch('setFunctionArr', res.functionArr)
//   new Vue({
//     el: '#app',
//     components: {
//       'sketch-picker': Sketch
//     },
//     router,
//     store,
//     watch: {
//       '$route' (to, from) {
//         // setTopNavActive(res.appMenus, to.path)
//       }
//     },
//     components: { App },
//     template: '<App/>',
//     mounted () {
//       let nowPath = window.location.pathname
//       // setTopNavActive(res.appMenus, nowPath)
//     }
//   })
// })
