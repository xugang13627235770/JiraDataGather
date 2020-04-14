/**
 * Created by renming on 2018/1/29.
 */
import Vue from 'vue'
import axios from 'axios'
import qs from 'qs'
import router from '../router'
import store from '../vuex/store'
import * as types from '../vuex/types'
import citmsUI from 'citms-ui-package'
Vue.prototype.axios = axios
// axios 配置
axios.defaults.timeout = 7000
// 后台接口公共前缀
axios.defaults.baseURL = baseURL
let apiAddress = ''
// axios.defaults.withCredentials=true
// POST传参序列化
axios.interceptors.request.use((config) => {
  let token = window.localStorage.getItem("token") ? window.localStorage.getItem("token") : ""
  // let token = Token
  apiAddress = config.url.split(baseURL)[0]
  if (token) {
    if (!config.data) {
      config.data = {}
    }
  }
  if (config.headers) {
    if (!config.headers['Content-Type']) {
      config.data = qs.stringify(config.data)
    } else {
      config.data = JSON.stringify(config.data)
    }
  } else {
    config.headers = {}
    if (config.method != 'get' && config.method != 'delete') {
      config.headers['Content-Type'] = 'application/x-www-form-urlencoded;charset=UTF-8'
    } else {
      config.url = config.url + '?' + qs.stringify(config.data)
    }
    config.data = qs.stringify(config.data)
  }
  config.headers.Authorization = token
  // config.headers = token
  return config
}, (error) => {
  citmsUI.cAlertMethod({
    content: '请求异常',
    type: 'warning',
    placement: 'top',
    duration: 2000,
    width: '220px'
  })
  // return Promise.reject(error);
})

// 返回状态判断
axios.interceptors.response.use((res) => {
  if (res.data && res.data.code === 401) { // 401, token失效
    //清除登录信息
    window.localStorage.setItem("token","");
    router.replace({ name: 'login' })
  }else if (res.data.code != 0) {
    citmsUI.cAlertMethod({
      content: res.data.msg || res.data.msg,
      type: 'danger',
      placement: 'top',
      duration: 2000,
      width: '200px'
    })
  }
  return res
}, (error) => {
  let massage = '数据返回异常'
  console.log(error)
  if (error.request.status != 200) { massage = apiAddress + '服务地址异常' }
  citmsUI.cAlertMethod({
    content: massage,
    type: 'danger',
    placement: 'top',
    duration: 3000
  })
})

export function fetch (url, params, method, headers) {
  method = method || 'post'
  return new Promise((resolve, reject) => {
    var obj = {
      method,
      url,
      data: params,
      headers
    }
    if (method == 'get') {
      delete obj.data
      obj.params = params
    }
    axios(obj).then(response => {
      resolve(response.data)
    }, err => {
      reject(err)
    }).catch((err) => {
      reject(err)
    })
  })
}
const APPJSON = {'Content-Type': 'application/json;charset=UTF-8'}

export default {
  // 获取字典项
  getDict (params) {
    return fetch('/smw/Dict/GetSomeDicts', params, 'post', APPJSON)
  },
  // 登录
  loginUser (params) {
    return fetch('/sys/login', params, 'post')
  },
  // 退出
  logout (params) {
    return fetch('/sys/logout', params, 'post')
  },
  // 获取 项目列表
  getProjectListByName (params) {
    return fetch('/project/getProjectListByName', params, 'get')
  },
  // 根据项目 id 获取 迭代信息
  getSprintListByProjectId (params) {
    return fetch('/sprint/getSprintListByProjectId', params, 'get')
  },
  // 根据迭代id 获取 统计信息
  getDeveloperList (params) {
    return fetch('/developer/list', params, 'get')
  },
  // 根据迭代id 获取 统计信息
  getConfigDeveloper (params) {
    return fetch('/developer/getConfigDeveloper', params, 'get')
  },
  // 保存开发配置信息
  saveConfigDeveloper (params) {
    return fetch('/developer/saveConfigDeveloper', params, 'post')
  },
  // 导出开发者统计数据
  exportExcelDeveloper (params) {
    return fetch('/developer/exportExcel', params, 'post')
  },
  // 获取测试者统计接口
  getConfigTester (params) {
    return fetch('/tester/getConfigTester', params, 'get')
  },
  // 保存测试者统计接口
  saveConfigTesterVO (params) {
    return fetch('/tester/saveConfigTesterVO', params, 'post')
  },
  // 获取项目统计规则
  getConfigProject (params) {
    return fetch('/configProject/getConfigProjectRuleInfo', params, 'get')
  },
  // 保存项目统计规则
  saveConfigProject (params) {
    return fetch('/configProject/saveConfigProjectRule', params, 'post', APPJSON)
  },
  // 获取测试统计
  getTesterList (params) {
    return fetch('/tester/list', params, 'get')
  },
  // 导出开发者统计数据
  exportExcelTester (params) {
    return fetch('/tester/exportExcel', params, 'post')
  },
  // 获取人员分类列表
  getUserList (params) {
    return fetch('/user/list', params, 'get')
  },
  // 获取人员分类类型
  getUserTypeList (params) {
    return fetch('/user/getUserTypeList', params, 'get')
  },
  // 设置人员分类类型
  updateUserTypeByIds (params) {
    return fetch('/user/updateUserTypeByIds', params, 'post')
  },
  // 查询迭代关联关系-根据项目id
  getRelatedSprintList (params) {
    return fetch('/sprint/getRelatedSprintList', params, 'get')
  },
  // 查询未关联迭代
  getUnrelatedSprintList (params) {
    return fetch('/sprint/getUnrelatedSprintList', params, 'get')
  },
  // 保存迭代关联关系
  saveSprintRelatedInfo (params) {
    return fetch('/sprint/saveSprintRelatedInfo', params, 'post')
  },
  // 首页-根据项目ID统计出问题
  getIssueDataByProjectId (params) {
    return fetch('/home/getIssueDataByProjectId', params, 'get')
  },
  // // 项目-根据迭代ID统计项目信息
  // getProjectInfoBySprintId (params) {
  //   return fetch('/project/getProjectInfoBySprintId', params, 'get')
  // },
  // 项目-迭代总缺陷数量人员占比
  getDefectRate (params) {
    return fetch('/project/getDefectRate', params, 'get')
  },
  // 项目-迭代故事点数人员占比
  getStoryPointRate (params) {
    return fetch('/project/getStoryPointRate', params, 'get')
  },
  // // 项目-迭代内各项指标
  // getSprintTargetList (params) {
  //   return fetch('/project/getSprintTargetList', params, 'get')
  // },
  // // 项目-迭代故事点数燃尽图
  // getStoryPointRecordBySprintId (params) {
  //   return fetch('/project/getStoryPointRecordBySprintId', params, 'get')
  // },
  // 项目-迭代新增问题类型统计
  getAddIssueTypeList (params) {
    return fetch('/project/getAddIssueTypeList', params, 'get')
  },
  // 导出迭代内各项指标数据
  exportSprintTarget (params) {
    return fetch('/project/exportExcel', params, 'post')
  },
  // // 迭代目标完成率
  // getSprintTargetRate (params) {
  //   return fetch('/project/getSprintTargetRate', params, 'get')
  // },
  // // 人均关闭故事点数
  // getAvgStoryPoint (params) {
  //   return fetch('/project/getAvgStoryPoint', params, 'get')
  // },
  // // 平均故事关闭时长
  // getAvgStoryTime (params) {
  //   return fetch('/project/getAvgStoryTime', params, 'get')
  // },
  // // 平均故事延期时长
  // getAvgStoryDelay (params) {
  //   return fetch('/project/getAvgStoryDelay', params, 'get')
  // },
  // // 重新打开故事次数
  // getReopenStory (params) {
  //   return fetch('/project/getReopenStory', params, 'get')
  // },
  // // 迭代遗留故事占比
  // getRemainStory (params) {
  //   return fetch('/project/getRemainStory', params, 'get')
  // },
  // // 逾期完成故事点数
  // getOverdueStoryPoint (params) {
  //   return fetch('/project/getOverdueStoryPoint', params, 'get')
  // },
  // // 缺陷关闭率
  // getDefectCloseRate (params) {
  //   return fetch('/project/getDefectCloseRate', params, 'get')
  // },
  // // 缺陷密度
  // getDefectDensity (params) {
  //   return fetch('/project/getDefectDensity', params, 'get')
  // },
  // // 遗留缺陷比例
  // getRemainDefectRate (params) {
  //   return fetch('/project/getRemainDefectRate', params, 'get')
  // },
  // // 本迭代内线上反馈的缺陷
  // getOnlineDefect (params) {
  //   return fetch('/project/getOnlineDefect', params, 'get')
  // },
  // // 平均线上缺陷修复时长
  // getOnlineRepairTime (params) {
  //   return fetch('/project/getOnlineRepairTime', params, 'get')
  // },
  // 迭代内各项指标
  getSprintStatisticsEntityBySprintId (params) {
    return fetch('/project/getSprintStatisticsEntityBySprintId', params, 'get')
  },
  //下载导出文件
  downloadFile(name){
    const iframe = document.createElement('iframe')
    iframe.style.display = 'none' // 防止影响页面
    iframe.style.height = 0 // 防止影响页面
    iframe.src = window.baseURL + '/developer/downloadExcel/' + name
    document.body.appendChild(iframe) // 这一行必须，iframe挂在到dom树上才会发请求
    setTimeout(() => {
      iframe.remove()
    }, 0.5 * 60 * 1000)
  },
  //弹出提示
  warnTip(obj, tip){
    obj.cAlert({
      content: tip,
      type: 'info',
      placement: 'center',
      duration: 2000,
      width:'300px' // 内容不确定，建议设置width
    });
  },
  // 封装项目统计的数据
  isInArray(arr,value){
    for(var i = 0; i < arr.length; i++){
      if(value == arr[i]){
        return true;
      }
    }
    return false;
  },
}
