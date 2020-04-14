import store from '../vuex/store';
import Vue from 'vue';
import moment from 'moment';
import axios from "axios"
Object.defineProperty(Vue.prototype, '$moment', { value: moment });
/**
 * 判斷字符串或对象是否为空
 * @param name 需获取的参数名称
 */
export function isEmpty (params) {
  if(typeof(params) == "string"){
    if (params == "" ) return true;
    var regu = "^[ ]+$";
    var re = new RegExp(regu);
    return re.test(params);
  }else if(typeof(params) == "object"){
    let empty = true
    for(let key in params){
      if(key){
        empty =false
        break
      }
    }
    return empty
  }
  return null;
}
/**
 * 获取URL参数
 * @param name 需获取的参数名称
 */
export function getQueryString (name) {
  var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
  var search = window.location.href.indexOf("?") == -1 ? window.location.search : "?"+window.location.href.split("?")[1];
  var r = decodeURI(search).substr(1).match(reg);
  if (r != null) {
    return unescape(r[2]);
  }
  return null;
}
/*树形结构数据转换部分*/
export function TreeUtil(){
  function addRange(loopArr, array) {
    for (var i = 0, j = array.length; i < j; i++) loopArr[loopArr.length] = array[i];
    return loopArr;
  }
  //递归添加父级数据
  function findParentNode(node, opts, view, existNodes, _idHash) {
    var parentId = node[opts.parentIdField];
    if (parentId && !existNodes[parentId]) {
      var parentNode = _idHash[parentId];
      if (parentNode) {
        view.push(citms.clone(parentNode));
        existNodes[parentId] = true;
        findParentNode(parentNode, opts, view, existNodes, _idHash);
      }
    }
  }
  return {
    /*
     * 功能：    将树形结构转换成普通的 id,pid结构数组形式
     * 参数：    nodes 树形数据
     *           nodesField 子节点字段名
     *           idField    id节点字段名
     *           parentIdField  父节点字段名
     * 返回值：  数组
     * 创建人：  杜冬军
     * 创建时间：2016-01-27
     */
    treeToArray: function (nodes, nodesField, idField, parentIdField, parentId) {
      if (!nodesField) nodesField = 'children';
      var array = [];

      for (var i = 0, l = nodes.length; i < l; i++) {
        var node = nodes[i];
        array[array.length] = node;

        if (parentIdField) node[parentIdField] = parentId;

        var childrenNodes = node[nodesField];
        if (childrenNodes && childrenNodes.length > 0) {
          var id = node[idField];
          var childrenArray = this.treeToArray(childrenNodes, nodesField, idField, parentIdField, id);
          addRange(array, childrenArray);
        }

      }
      return array;
    },
    /*
     * 功能：    将普通的id,pid结构数组形式转换成树形结构
     * 参数：    data 数组树形数据
     *           nodesField 子节点字段名
     *           idField    id节点字段名
     *           parentIdField  父节点字段名
     * 返回值：  树形数据
     * 创建人：  杜冬军
     * 创建时间：2016-01-27
     */
    arrayToTree: function (data, nodesField, idField, parentIdField) {
      if (!nodesField) nodesField = 'children';
      idField = idField || '_id';
      parentIdField = parentIdField || '_pid';
      var nodes = [];
      var idHash = {};
      for (var i = 0, l = data.length; i < l; i++) {
        var o = data[i];
        if (!o) continue;
        var id = o[idField];
        if (id !== null && id !== undefined) {
          idHash[id] = o;
        }
        delete o[nodesField];
      }
      for (var i = 0, l = data.length; i < l; i++) {
        var o = data[i];
        var p = idHash[o[parentIdField]];
        if (!p) {
          nodes.push(o);
          continue;
        }
        if (!p[nodesField]) {
          p[nodesField] = [];
        }
        p[nodesField].push(o);
      }
      function setDeep(arr,dep){
        arr.forEach(item=>{
          item.deep = dep
          if(item[nodesField]){
            let deep = dep+1
            setDeep(item[nodesField],deep)
          }
        })
      }
      setDeep(nodes,0)
      return nodes;
    },
    search: function (option) {
      var defaults = {
        filterScope: function (node) { return true; },//过滤方法 返回true则表示节点符合规则
        data: [],
        nodesField: "children",
        idField: "id",
        parentIdField: "pid",
        isTree: false, //是否树形数据 为否的则认为是列表型数据
        isMatchingChild: true //为true会匹配到每个节点，为false父级匹配上那么直接认为所有子集也是匹配上的
      };
      var opts = $.extend(false, defaults, option);
      if (!$.isFunction(opts.filterScope)) {
        return opts.data;
      } else {
        var nodes, view = [], _idHash = [];
        if (opts.isTree) {
          nodes = this.treeToArray(opts.data, opts.nodesField, opts.idField, opts.parentIdField, "");
        } else {
          nodes = opts.data;
        }
        if (nodes && nodes.length > 0) {
          var node;
          for (var i = 0, l = nodes.length; i < l; i++) {
            node = nodes[i];
            _idHash[node[opts.idField]] = node;
          }
          var existNodes = [];
          for (var i = 0, l = nodes.length; i < l; i++) {
            node = nodes[i];
            if (opts.filterScope(node) !== false) {
              view.push(citms.clone(node));
              existNodes[node[opts.idField]] = true;
              findParentNode(node, opts, view, existNodes, _idHash);
            }
          }
          return this.arrayToTree(view, opts.nodesField, opts.idField, opts.parentIdField);
        }
        return [];
      }


    }
  };
}
/**
 * 导出Excel
 * @param options.columns 必选，列头名称
 * @param options.data 必选，如果是导出选中参数类型为数组，如果是导出全部为字符串URL
 * @param options.title 必选，导出文件的名称
 * @param options.methods 可选，如果data为url则填写请求方式 默认为post
 * @param options.params 可选，如果data是url，则可以传过滤条件
 * @param options.extra 可选，数组格式，额外的添加项
 */
export function exportExcel (options) {
  let ColumnInfo = [];
  if(!options.columns || !options.data || !options.title){
    return Promise.reject("参数错位,columns、data、title必选");
  }
  if(options.columns.length > 0) {
    for(let i = 0; i < options.columns.length; i++) {
      let item = options.columns[i]
      if(options.columns[i].dataIndex) {
        let jsonCol = {};
        jsonCol["Align"] = item.align || "left";
        jsonCol["Header"] = item.title;
        jsonCol["Field"] = item.dataIndex;
        ColumnInfo.push(jsonCol);
      }
    }
    if(options.extra)
      ColumnInfo = ColumnInfo.concat(options.extra)
    let exportParam = {
      "FileName": options.title,
      "ColumnInfoList": ColumnInfo,
      "Type": options.methods||"post",
    };
    if(typeof(options.data) != "string") {
      if(options.data.length > 0) {
        exportParam["Data"] = options.data;
        exportParam["IsExportSelectData"] = true;
      } else {
        return Promise.reject("请至少选择一条数据");
      }
    } else {
      exportParam['Condition'] = options.params;
      exportParam['Api'] = options.data;
      exportParam['IsExportSelectData'] = false;
    }
    return new Promise((resolve,reject)=>{
      axios({
        method: 'post',
        url: '/api/Common/Excel/Export',
        headers: {
          'Content-Type': 'application/json'
        },
        data: exportParam
      }).then((response) => {
        if(!response.code) {
          let fragment = document.createDocumentFragment();
          let a = document.createElement('a');
          a.setAttribute("href", response.data.result.url);
          fragment.appendChild(a);
          document.body.appendChild(fragment);
          a.click();
          a.remove();
          resolve()
        }else{
          reject(response.message)
        }
      }).catch(err=>{
        reject(err)
      })
    })

  } else {
    return Promise.reject("当前表格没有列");
  }
}
/**
 *   获取权限公用方法
 *   @param currentCode 当前功能代码
 *   @param actionCode 动作代码
 */
export function getAuthority(currentCode,actionCode){
  let PermissionList = store.getters.getUserInfo.PermissionList,
    thisPermission = PermissionList.filter(item=>item.FunctionCode == currentCode)[0]
  if(!thisPermission)
    return false
  return thisPermission.ActionCodeList.indexOf(actionCode) != -1
}
Object.defineProperty(Vue.prototype, 'getAuthority', { value: getAuthority });
/**
 * 日期格式化
 * @param date 需要格式化的日期
 * @param format 格式化参数 YYYY-MM-DD HH:mm:ss
 */
export function formatDate(date,format) {
  format = format || 'YYYY-MM-DD HH:mm:ss';
  return moment(date).format(format);
}
Object.defineProperty(Vue.prototype, '$formatDate', { value: formatDate });

/***
 * 生成唯一uuid
 */
export function uuid () {
  let s = []
  let hexDigits = '0123456789abcdef'
  for (let i = 0; i < 36; i++) {
    s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1)
  }
  s[14] = '4'  // bits 12-15 of the time_hi_and_version field to 0010
  s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1)  // bits 6-7 of the clock_seq_hi_and_reserved to 01
  s[8] = s[13] = s[18] = s[23] = '-'

  let uuid = s.join('')
  return uuid
}
Object.defineProperty(Vue.prototype, 'uuid', { value: uuid });
