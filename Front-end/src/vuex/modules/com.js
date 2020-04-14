import {getQueryString} from '../../util/tools'
/**
 * 通用配置
 */
const state = {
  functionCode: getQueryString('FunctionCode'),
  topNavActive: '',
  secondActive: '',
  appMenus: [],
  systemConfig: {},
  functionArr: [],
  toggle:false,
  currentNode:null,
  currentItemIndex:[],
  configPanel:true,
  pageConfig:{
    width:1920,
    height:1080,
    scale:1,
    bgImage:"",
    pageBGColor:"rgba(14,16,55,1)",
    displayName:"",
  },
  pageConfigCopy:{
    width:1920,
    height:1080,
    scale:1,
    bgImage:"",
    pageBGColor:"rgba(14,16,55,1)",
    displayName:"",
  },
  currentIds:[],//当前操作数组ID集合
  currentMode:1,//1、新增  2、编辑  3、预览
  displayData:{},
  elementData:[],
  showSpaceMask:false,//拖拽面板遮罩层
  loading:false,//我的看板loading动画
}

const actions = {
  setTopNavActive ({ commit }, res) {
    commit('setTopNavActive', res)
  },
  setSecondActive ({ commit }, res) {
    commit('setSecondActive', res)
  },
  setAppMenus ({ commit }, res) {
    commit('setAppMenus', res)
  },
  setSystemConfig ({ commit }, res) {
    commit('setSystemConfig', res)
  },
  setFunctionArr ({ commit }, res) {
    commit('setFunctionArr', res)
  },
  setToggle ({ commit }, res) {
    commit('setToggle', res)
  },
  setCurrentNode ({ commit }, res) {
    commit('setCurrentNode', res)
  },
  setCurrentItemIndex ({ commit }, res) {
    commit('setCurrentItemIndex', res)
  },
  setConfigPanel ({ commit }, res) {
    commit('setConfigPanel', res)
  },
  setPageConfig ({ commit }, res) {
    commit('setPageConfig', res)
  },
  setPageConfigCopy ({ commit }, res) {
    commit('setPageConfigCopy', res)
  },
  setCurrentIds ({ commit }, res) {
    commit('setCurrentIds', res)
  },
  setCurrentMode ({ commit }, res) {
    commit('setCurrentMode', res)
  },
  setDisplayData ({ commit }, res) {
    commit('setDisplayData', res)
  },
  setElementData({ commit }, res) {
    commit('setElementData', res)
  },
  setSpaceMask({ commit }, res) {
    commit('setSpaceMask', res)
  },
  setLoading({ commit }, res) {
    commit('setLoading', res)
  },
}

const mutations = {
  setTopNavActive (state, res) {
    state.topNavActive = res
  },
  setSecondActive (state, res) {
    state.secondActive = res
  },
  setAppMenus (state, res) {
    state.appMenus = res
  },
  setSystemConfig (state, res) {
    state.systemConfig = res
  },
  setFunctionArr (state, res) {
    state.functionArr = res
  },
  setToggle (state, res) {
    state.toggle = res
  },
  setCurrentNode (state, res) {
    state.currentNode = res
  },
  setCurrentItemIndex (state, res) {
    state.currentItemIndex = res
  },
  setConfigPanel (state, res) {
    state.configPanel = res
  },
  setPageConfig(state, res) {
    state.pageConfig = res
  },
  setPageConfigCopy(state, res) {
    state.setPageConfigCopy = res
  },
  setCurrentIds(state, res) {
    state.currentIds = res
  },
  setCurrentMode (state, res) {
    state.currentMode = res
  },
  setDisplayData (state, res) {
    state.displayData = res
  },
  setElementData(state, res) {
    state.elementData = res
  },
  setSpaceMask(state, res) {
    state.showSpaceMask = res
  },
  setLoading(state, res) {
    state.loading = res
  },
}

const getters = {
  getTopNavActive: (state) => state.topNavActive,
  getToggle: (state) => state.toggle,
  getCurrentNode: (state) => state.currentNode,
  getCurrentItemIndex: (state) => state.currentItemIndex,
  getConfigPanel: (state) => state.configPanel,
  getPageConfig: (state) => state.pageConfig,
  getPageConfigCopy: (state) => state.pageConfigCopy,
  getCurrentIds: (state) => state.currentIds,
  getCurrentMode: (state) => state.currentMode,
  getDisplayData: (state) => state.displayData,
  getElement: (state) => state.elementData,
  getSpaceMask: (state) => state.showSpaceMask,
  getLoading: (state) => state.loading,
}

export default {
  state,
  actions,
  getters,
  mutations
}
