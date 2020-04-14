/**
 * Created by renming on 2017/6/28.
 */
import {getQueryString} from "../../util/tools"
const state = {
  // 用户登录信息
  userInfo: {},
  //当前皮肤
  skin: sessionStorage.getItem('SKIN'),
};

const actions = {
  /* 用户登录成功 */
  setUserInfo({ commit }, res) {
    commit("setUserInfo", res);
  }
};

const mutations = {
  setUserInfo(state, res) {
    state.userInfo = res;
  },
};

const getters = {
  getUserInfo: state => state.userInfo,
};


export default {
  state,
  actions,
  getters,
  mutations
};
