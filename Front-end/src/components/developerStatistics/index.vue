<template>
  <slider-left id="containerWeb">
    <div slot="left">
      <div class="form-box search-of">
        <div class="sel_project_wrap" style="float: left">
          <div class="sel_project_title">
            项目选择
            <i class="rf c-fa c-fa-caret-down"></i>
          </div>
          <c-select
            multiple
            v-model="selProject"
            search
            :options="projectList"
            @change="selProjectFun"
            width="150px"
            selected-max-height="140px"
          ></c-select>
        </div>
        <div class="" style="float: left;padding-left: 20px">
          <c-button
            type="primary"
            @click.native="searchEvent"
            class="c-btn-normal"
          >统计</c-button
          >
        </div>
        <div style="clear: both"></div>
        <div class="project_list">
          <div
            class="project_content"
            v-for="(item, index) in projectListDetail"
            :key="index"
          >
            <b
              class="iconfont icon-guanbi"
              @click="delProject(item, index)"
            ></b>
            <ul>
              <li>
                <label>项目：</label>
                <div>{{ item.projectName }}</div>
              </li>
              <li>
                <label>迭代：</label>
                <div>
                  <c-select
                    v-model="item.selIteration"
                    search
                    :options="item.iterationList"
                    @change="selSprintFun"
                    width="100%"
                  ></c-select>
                </div>
              </li>
            </ul>
          </div>
        </div>
      </div>

    </div>
    <div slot="right">
      <div class="table-box">
        <div class="table-tool">
          <h4>指定项目迭代内的迭代统计</h4>
          <div>
            <c-button size="sm" type="linear" @click.native="exportExcel" >导出</c-button>
            <span class="iconfont icon-shezhi1" @click="viewRule"></span>
          </div>
        </div>
        <c-data-table
          :url="baseUrl"
          :token="token"
          :appNo="appNo"
          fixed-columns
          tableId="Table"
          :height="tableHeight"
          :async="false"
          :filter="false"
          :pagination="pagination"
          :selectCols="false"
          :dataNumber="true"
          :search="false"
          :source="sourceGrid"
          :columns="columns"
          @change="changeGridEvent"
          idFiled="id"
          ref="table"
        ></c-data-table>
      </div>
    </div>
    <div slot="full">
      <c-page-loading v-if="loading"></c-page-loading>
      <!-- 规则管理 弹窗 -->
      <statistic-rule ref="rules"></statistic-rule>
    </div>
  </slider-left>
</template>
<script>
import sliderLeft from "../common/sliderLeft/sliderLeft";
import statisticRule from "./modal/rules";
import api from "../../api/api";
export default {
  data() {
    return {
      appNo: appNo,
      baseUrl: baseURL,
      token: "Basic " + Token,
      loading: false, // 加载动效
      tableHeight: "400px",
      selProject: [1],
      projectListDetail: [{
        "projectName": "",
        "projectId": "",
        "iterationList": this.iterationList,
        "selIteration": ""
      }],
      iterationList: [{
        "value":"",
        "label":""
      }],
      projectList: [{
        "value":"",
        "label":""
      }],
      columns: [
        {
          title: "人员名称",
          width: "100",
          align: "left",
          fixed: true,
          dataIndex: "developerName"
        },
        {
          title: "关闭故事点数",
          width: "100",
          align: "left",
          fixed: true,
          dataIndex: "closeStoryNum",
          sort:true
        },
        {
          title: "提测延期天数",
          width: "100",
          align: "left",
          fixed: true,
          dataIndex: "testDelayDays",
          sort:true
        },
        {
          title: "提测打回次数",
          width: "100",
          align: "left",
          fixed: true,
          dataIndex: "testBackTimes",
          sort:true
        },
        {
          title: "缺陷回归次数",
          width: "100",
          align: "left",
          fixed: true,
          dataIndex: "issueReturnTimes",
          sort:true
        },

        {
          title: "红色缺陷遗留",
          width: "100",
          align: "left",
          fixed: true,
          dataIndex: "redIssue",
          sort:true
        },
        {
          title: "非红色缺陷遗留",
          width: "100",
          align: "left",
          fixed: true,
          dataIndex: "noredIssue",
          sort:true
        },

        {
          title: "有效缺陷数量",
          width: "100",
          align: "left",
          fixed: true,
          dataIndex: "validIssueNum",
          sort:true
        },
        {
          title: "缺陷关闭率",
          width: "100",
          align: "left",
          fixed: true,
          dataIndex: "issueCloseRateStr",
          sort:true
        },
        {
          title: "线上反馈缺陷",
          width: "100",
          align: "left",
          fixed: true,
          dataIndex: "onlineIssue",
          sort:true
        },
        {
          title: "总积分",
          width: "80",
          align: "left",
          fixed: true,
          dataIndex: "totalScore",
          sort:true,
          render:(value,obj)=>{
            if(obj.order==1){
              return '<div class="arrow_up" style="background: url(../../../static/images/arrow_bg.png) -543px -160px;"></div><span class="red_bg">'+value+'</span><div style="clear: both;"/>'
            }else if(obj.order ==-1){
              return '<div class="arrow_down" style="background: url(../../../static/images/arrow_bg.png) -512px -162px;"></div><span class="green_bg">'+value+'</span><div style="clear: both;"/>'
            }else{
              return value
            }
          }
        }
      ],
      sourceGrid: [],
      selection: {
        checkRows: []
      },
      pagination: {
        current: 1,
        total: 20,
        pagesize: 20,
        pagesizeOpts: [10, 20, 40, 100]
      }
    };
  },
  methods: {
    // 获取表格gap度
    calcTableHeight() {
      this.tableHeight =
        document.getElementById("containerWeb").offsetHeight - 125 + "px";
    },
    // 获取项目列表
    async getProjectList() {
      this.loading = true;
      await this.api
        .getProjectListByName()
        .then(res => {
          this.loading = false;
          if (res.code === 0) {
            this.projectList = res.projectList.map(item => {
              return {
                label: item.name,
                value: item.id
              };
            });
          } else {
            this.projectList = [];
          }
        })
        .catch(err => {
          this.loading = false;
          this.projectList = [];
        });
    },
    setProject() {},
    // 选择项目 触发的 方法
    selProjectFun(value, obj) {
      //反选
      if(value.length==0 && this.projectListDetail.length>0){
        this.projectListDetail=[];
        return;
      }

      let projectIdArray = this.projectListDetail.map(item=>{
        return item.projectId;
      })

      //删除项目
      for(var i=0;i<projectIdArray.length;i++){
        if(!this.api.isInArray(value, projectIdArray[i])){
          this.projectListDetail.splice(i,1);
        }
      }
      //新增项目
      let addProjectArray=[];
      value.forEach(projectId =>{
        if(!this.api.isInArray(projectIdArray, projectId)){
          addProjectArray.push(projectId);
        }
      });
      if(addProjectArray.length>0){
         this.getSprintListFun(addProjectArray, obj);
      }

    },
    //根据项目ID获取迭代信息
    getSprintListFun(addProjectArray, obj) {
      this.api
        .getRelatedSprintList({ projects: addProjectArray.join(",") })
        .then(res => {
          if (res.code === 0) {
            res.relatedSprintList.forEach(sprintProjectVO=>{
              let sprintId = this.getSprintId(sprintProjectVO.projectId)
              let sprintList=[]
              if(sprintProjectVO.sprintList != null){
                sprintList = sprintProjectVO.sprintList;
                sprintList.forEach((el, index) => {
                  //设置name和id
                  let name = el.name
                  if(el.state == 1){
                    name = el.name+"(当前迭代)";
                  }
                  el.label = name;
                  el.value = el.id;
                });

                if(sprintList.length==1 && sprintId == ""){
                  //如果只有一个迭代信息默认选中当前迭代
                  sprintId = sprintList[0].value
                }else if(sprintList.length>1 && sprintId == ""){
                  //如果大于一个迭代信息默认选中当前迭代的上个迭代
                  sprintId = sprintList[1].value
                }
              }

              let projectObj = {
                projectName: sprintProjectVO.projectName,
                projectId: sprintProjectVO.projectId,
                iterationList: sprintList,
                selIteration: sprintId
              };
              this.projectListDetail.push(projectObj);
            });
          }
        })
        .catch(err => {});
    },
    //获取本地缓存的sprintid
    getSprintId(projectId){
      let dataStr = window.localStorage.getItem("developer_sel_sprint") ? window.localStorage.getItem("developer_sel_sprint") : "";
      let dataArray = [];
      if(dataStr.length > 0){
        dataArray = JSON.parse(dataStr)
      }

      let sprintId = "";
      dataArray.forEach(obj => {
        if(obj.projectId == projectId){
          sprintId = obj.sprintId
        }
      });
      return sprintId;

    },
    //选择迭代id监听事件
    selSprintFun(value, obj){
      if(value.length== 0){
        return;
      }
      let sprintObj = {
        projectId : obj.projectId,
        sprintId:value
      }
      let dataStr = window.localStorage.getItem("developer_sel_sprint") ? window.localStorage.getItem("developer_sel_sprint") : "";
      let dataArray = [];
      if(dataStr.length > 0){
        dataArray = JSON.parse(dataStr)
      }

      for(var i=0;i<dataArray.length;i++){
        if(dataArray[i].projectId==obj.projectId){
          dataArray.splice(i,1)
        }
      }
      dataArray.push(sprintObj);
      window.localStorage.setItem("developer_sel_sprint",JSON.stringify(dataArray));
    },
    delProject(item, index) {
      if (index >= 0) {
        this.projectListDetail.splice(index, 1);
        let selProjectIndex = this.selProject.indexOf(item.projectId);
        this.selProject.splice(selProjectIndex, 1);
      }
    },
    // 统计方法；
    searchEventFun() {
      let sprintsList = [];
      this.projectListDetail.forEach(projectDetail => {
        if (projectDetail.selIteration) {
          sprintsList.push(projectDetail.selIteration);
        }
      });
      if(this.selProject.length == 0){
        this.api.warnTip(this, "请选择项目信息");
        return;
      }
      if(sprintsList.length == 0){
        this.api.warnTip(this, "请选择迭代信息");
        return;
      }

      let params = {
        sprints: sprintsList.join(","),
        limit: this.pagination.pageSize,
        page: this.pagination.pageIndex
      };
      // let sortInfo = this.$refs.table.sortInfo
      // if(sortInfo && sortInfo.index){
      //   params.sortFiled = sortInfo.index
      //   params.sortOrder = sortInfo.type
      // }
      this.loading = true;
      this.api
        .getDeveloperList(params)
        .then(res => {
          this.loading = false;
          if (res.code === 0) {
            this.sourceGrid = res.page.list;
            this.pagination.total = res.page.totalCount;
          }
        })
        .catch(err => {
          this.loading = false;
        });
    },
    // 统计方法
    searchEvent() {
      this.pagination.pageIndex = 1;
      this.searchEventFun();
    },
    changeGridEvent(obj) {
      if (obj.current) {
        this.pagination.pageIndex = obj.current;
        this.pagination.pageSize = obj.pagesize;
        this.checkFlag = true;
        this.searchEventFun();
      }
    },
    // 查看规则
    viewRule() {
      this.$refs.rules.openModal();
    },
    // 导出
    exportExcel() {
      let sprintsList = [];
      this.projectListDetail.forEach(projectDetail => {
        if (projectDetail.selIteration) {
          sprintsList.push(projectDetail.selIteration);
        }
      });
      if(sprintsList.length == 0){
        this.api.warnTip(this, "请选择迭代信息");
        return;
      }

      this.loading = true;
      this.api
        .exportExcelDeveloper({ sprints: sprintsList.join(",")})
        .then(res => {
          this.loading = false;
          if (res.code === 0) {
            this.api.downloadFile(res.data);
          }
        })
        .catch(err => {
          this.loading = false;
        });
    }
  },
  mounted() {
    this.$nextTick(()=>{
      this.calcTableHeight();
      this.getProjectList().then(res=>{
        this.selProject = window.localStorage.getItem("developer_sel_project") ? window.localStorage.getItem("developer_sel_project").split(",") : [];
        let projectObj = this.projectList.filter(item=>{
          return this.selProject.includes(""+item.value);
        })
        this.selProjectFun(this.selProject,projectObj)
        // setTimeout(() => {
        //   this.searchEvent()
        // }, 1000)

      });
    })
  },
  components: {
    "slider-left": sliderLeft,
    "statistic-rule": statisticRule,
  },
  watch:{
    selProject(newV,oldV) {
      if(newV && newV.length>0){
        window.localStorage.setItem("developer_sel_project",newV.join(","));
        console.log(window.localStorage.getItem("developer_sel_project"))
      }else{
        window.localStorage.setItem("developer_sel_project",[]);
      }
    }

  }
};
</script>
<style scoped lang="scss">
.table-tool {
  display: flex;
  justify-content: space-between;
  align-items: center;
  h4 {
    color: #d3d3d3;
  }
  .iconfont {
    font-size: 20px;
    color: #d3d3d3;
    line-height: 30px;
    margin-left: 10px;
    cursor: pointer;
  }
}
.project_list {
  .project_content {
    position: relative;
    margin-top: 20px;
    padding: 20px;
    background-color: #232d50;
    border-radius: 3px;
    &:hover {
      & .icon-guanbi {
        opacity: 1;
      }
    }
    .icon-guanbi {
      transition: 0.2s;
      opacity: 0;
      position: absolute;
      right: -6px;
      top: -10px;
      font-size: 18px;
      font-weight: bold;
      color: #d3d3d3;
      cursor: pointer;
    }
    ul {
      font-size: 14px;
      color: #fff;
      li {
        overflow: hidden;
        margin: 5px 0;
        line-height: 30px;
        label {
          float: left;
          width: 60px;
          text-align: right;
          margin-right: 5px;
        }
        & > div {
          float: left;
          width: calc(100% - 70px);
        }
      }
    }
  }
}

.sel_project_wrap {
  /deep/ .c-select-group {
    opacity: 0;
    height: 32px;
    //  z-index: 99;
  }
  height: 35px;
  position: relative;
  .sel_project_title {
    position: absolute;
    // z-index: 88;
    height: 32px;
    line-height: 32px;
    width: 150px;
    border: 1px solid rgba(24, 117, 192, 0.5);
    border-radius: 3px;
    padding: 0 10px;
    i {
      line-height: 32px;
    }
  }
  /deep/ .c-selected-tag {
    display: none !important;
  }
}
</style>

<style>
  .arrow_up {
    float: right;
    width:30px;height:20px;
  }
  .red_bg{
    width:50px;height:20px;line-height:20px;color: red;float:right
  }

  .arrow_down {
    float: right;
    width:30px;height:20px;
  }
  .green_bg{
    width:50px;height:20px;line-height:20px;color: green;float:right;
  }
</style>
