<template>
  <div class="container">
    <c-container class="search-box  block-bg bordered">
      <c-row>
        <c-column :col="4" class="context">
          <label for="">资源库名称</label>
          <c-input v-model="searchData.name" width="200px"></c-input>
        </c-column>
        <c-column :col="4" class="context">
          <label for="">数据库类型</label>
          <c-select
           width="200px"
            :options="dictList.datasourceTypeList"
            v-model="searchData.datasourceType"
            name="heartbeatDetection"
          ></c-select>
        </c-column>
        <c-column :col="4" class="context">
          <c-button type="primary" @click.native="searchEvent">查询</c-button>
          <c-button type="gray" @click.native="reset">重置</c-button>
        </c-column>
      </c-row>
    </c-container>
    <div class="of-h btn-container">
      <c-button type="linear" @click.native="editResource('add')">  新增</c-button>
      <c-button class="rf" type="linear"  @click.native="checkAll()"> {{checkFlag?"全选":"全部取消"}} </c-button>
      <c-button class="rf" type="linear" @click.native="batchDelete"> 批量删除 </c-button>
    </div>
    <div class="main-box of-h">
      <div class="main-content" v-for="(item, key) in dataList" :key="key">
        <div class="panel-head">
          <c-checkbox
            v-model="item.checked"
            @change="checkedFun(key)"
          ></c-checkbox>
          <span>{{item.name}}</span>
        </div>
        <div class="panel-content">
          <ul>
            <li>
              <label for="">数据源名：</label>
              <span>{{item.datasourceName||"--"}}</span>
            </li>
            <li>
              <label for="">数据库类型：</label>
              <span>{{item.datasourceType||"--"}}</span>
            </li>
            <li>
              <label for="">资源库说明：</label>
              <span>{{item.comment||"--"}}</span>
            </li>
          </ul>
          <div>
            <c-button type="linear" class="rf" @click.native="viewDataBase(item.id)">
              <c-icon type="search"></c-icon>查看数据库
            </c-button>
            <c-button type="linear" class="rf" @click.native="deleteFun([item.id])"><span class="iconfont icon-shanchu"></span> 删除
            </c-button>
            <c-button type="linear" class="rf" @click.native="editResource('edit',item.id)">
              <span class="iconfont icon-shuxiebianji"></span> 编辑
            </c-button>
          </div>
        </div>
      </div>
    </div>
    <!-- 分页 -->
    <div align="center" class="page-wrap">
      <c-page
        :total="pagination.total"
        @change="changeGridEvent"
        v-model="pagination.pageNo"
        :pagesize="pagination.pageSize"
        :pagesize-opts="pageOp"
        :show-sizer="true"
        :show-elevator="true"
        :show-total="true"
      ></c-page>
    </div>
    <!-- 查看数据源 -->
    <database-modal ref="dataBaseModal"></database-modal>
    <!-- 新增 -->
    <add-modal ref="addModal" :dictList="dictList" @update="searchEventFun"></add-modal>
    <c-page-loading v-if="loading"></c-page-loading>
  </div>
</template>

<script>
import dataBaseModal from "../modal/database";
import addModal from "../modal/addResource"
export default {
  data() {
    return {
      loading:false,
      checkFlag: true,// 是否全选
      dictList:{
        datasourceTypeList:[{
          label:"mysql",
          value:"mysql"
        }]
      },
      dataList: [],
      searchData: {
        name: "",
        datasourceType:"",
        isPagination: true
      },
      pagination: {
        isPagination: true,
        pageNo: 1,
        pageSize: 10,
        total: 0
      },
      pageOp: [10, 20, 30, 40]
    };
  },
  methods: {
    reset(){
      this.searchData = {
        name: "",
        datasourceType:"",
        isPagination: true
      }
    },
    getDict(){
      // 数据库类型 3901
      this.api.getDict(["3901"]).then(res=>{
        if(res.code===0 && res.result["3901"]){
          this.dictList.datasourceTypeList = res.result["3901"].map(item=>{
            return {
              label:item.dictionaryValue,
              value:item.dictionaryNo
            }
          })
        }
      })
    },
    searchEvent() {
      this.pagination.pageIndex = 1;
      this.searchEventFun();
    },
    changeGridEvent(index, size) {
      if (index) {
        this.pagination.pageIndex = index;
        this.pagination.pageSize = size;
        this.checkFlag = true;
        this.searchEventFun();
      }
    },
    searchEventFun(){
      this.loading = true;
      let params = Object.assign({},this.searchData, this.pagination);
      this.api.searchDcmDatasourceLocal(params).then(res=>{
        this.loading = false;
        if(res.code===0){
          res.result.forEach(item => {
            item["checked"] = false;
          });
          this.dataList = res.result;
          this.pagination.total = res.totalCount;
        }
      }).catch(err => {
        this.loading = false;
      });
    },
    stopPropagation(e) {
      e.stopPropagation();
    },
    checkedFun(index) {
      this.dataList[index].checked = !this.dataList[index].checked;
      this.checkFlag = this.dataList.every(item=>{
        return !item.checked
      })
    },
    // 查看数据库
    viewDataBase(id) {
      this.$refs.dataBaseModal.openModal(id);
    },
    // 编辑基本信息
    editResource(mode,id){
      this.$refs.addModal.openModal(mode,id)
    },
    // 删除
    deleteFun(ids){
      this.api.deleteDcmDatasource(ids).then(res=>{
        if(res.code===0){
          this.cToast({
            text: "删除成功",
            type: "success",
            placement: "top",
            duration: 2000,
            closeOnClick: true,
          });
          this.searchEventFun();
        }
      })
    },
    batchDelete(){
      let selection = [];
      this.dataList.forEach(item => {
        if (item.checked) {
          selection.push(item.id);
        }
      });
      if (selection.length == 0) {
        this.cToast({
          text: "请选择要删除的数据",
          type: "warning",
          placement: "top",
          closeOnClick: true
        });
        return;
      }
      this.cModal.confirm({
        title: "消息",
        message: "确定要删除吗?",
        effect: "zoom",
        type: "success",
        onConfirm: () => {
         this.deleteFun(selection);
        }
      });
    },
    checkAll() {
      this.dataList.forEach(item => {
        item.checked = this.checkFlag;
      });
      this.checkFlag = !this.checkFlag;
    },
  },
  components: {
    "database-modal": dataBaseModal,
    "add-modal":addModal,
  },
  mounted(){
    this.searchEvent();
    // this.getDict();
  }
};
</script>
<style lang="scss" scoped>
@import "../resource.scss";
</style>
