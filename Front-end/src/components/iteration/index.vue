<template>
  <div id="containerWeb">
    <div class="r-wrap">
        <!-- <c-button @click.native="save" class="save-btn" type="primary">保存</c-button> -->
      <div class="sel_project_wrap">
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
          width="100%"
          selected-max-height="140px"
        ></c-select>
      </div>

    </div>

    <div class="interationList-wrap" style="margin-top:0px;padding-top:0px">
      <h5>未关联迭代</h5>
      <vue-draggable
        class="drag-main"
        v-model="unassociatedIteration"
        :group="{ name: 'task', put: true }"
        v-bind="dragOptions"
        :sort="false"
        @end="save"
      >
        <div
          v-for="(item, index) in unassociatedIteration"
          :key="index"
          class="dragItem"
        >
          {{ item.label }}
        </div>
      </vue-draggable>
    </div>

    <div
      class="interationList-wrap"
      v-for="(pro, key1) in projectDetailList"
      :key="key1"
    >
      <h5>{{ pro.projectName }}</h5>
      <vue-draggable
        class="drag-main"
        v-model="pro.iterationList"
        :sort="false"
        v-bind="dragOptions"
        :group="{ name: 'task', put: true }"
        @end="save"
      >
        <c-button
          type="linear"
          v-for="(iteration, key2) in pro.iterationList"
          :key="key2"
          >{{ iteration.label }}</c-button
        >
      </vue-draggable>
    </div>
  </div>
</template>

<script>
import vuedraggable from "vuedraggable";
export default {
  data() {
    return {
      dragOptionUnassociate: {
        group: "task",
        animation: 150,
        // ghostClass: "sortable-ghost",
        // chosenClass: "chosenClass",
        scroll: true,
        scrollSensitivity: 200
      },

      dragOptions: {
        animation: 300
      },
      selProject: [],
      //   项目列表
      projectList: [{
        "label":"",
        "value":""
      }],
      //   未关联的迭代
      unassociatedIteration: [],
      iterationList:[],
      //   项目相关迭代任务
      projectDetailList: [{
        "projectName": "",
        "projectId": "",
        "total": "",
        "iterationList": this.iterationList,
      }],
      sprintList:[{
        "sprintIdStr":"",
        "projectId":""
      }]
    };
  },
  methods: {
    //查询未关联迭代
    getUnrelatedSprintList() {
      this.loading = true;
      this.api
        .getUnrelatedSprintList()
        .then(res=>{
          this.loading = false;
          if (res.code === 0) {
            this.unassociatedIteration = res.unrelatedSprintList.map(item => {
              return {
                label: item.name,
                value: item.id
              };
            });
          }

        }).catch(err => {
        this.loading = false;
      });
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
    selProjectFun(value, obj) {
      // this.projectDetailList.splice(0, this.projectDetailList.length);
      // value.forEach(projectId => {
      //   this.getSprintListByProject(projectId, obj);
      // });
      //反选
      if(value.length==0 && this.projectDetailList.length>0){
        this.projectDetailList=[];
        return;
      }

      let projectIdArray = this.projectDetailList.map(item=>{
        return item.projectId;
      })

      //删除项目
      for(var i=0;i<projectIdArray.length;i++){
        if(!this.api.isInArray(value, projectIdArray[i])){
          this.projectDetailList.splice(i,1);
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
        this.getSprintListByProject(addProjectArray, obj);
      }

    },
    //根据项目ID获取迭代信息
    getSprintListByProject(addProjectArray, obj) {
      this.api
        .getRelatedSprintList({ projects: addProjectArray.join(",") })
        .then(res => {
          if (res.code === 0) {
            //遍历项目信息
            res.relatedSprintList.forEach(sprintProjectVO=>{
              let sprintList=[]
              if(sprintProjectVO.sprintList != null){
                sprintList = sprintProjectVO.sprintList;
                sprintList.forEach(el => {
                  el.value = el.id;
                  el.label = el.name;
                });
              }

              let projectObj = {
                projectName: sprintProjectVO.projectName,
                projectId: sprintProjectVO.projectId,
                total: sprintList.length,
                iterationList: sprintList
              };
              this.projectDetailList.push(projectObj);
            });
          }
        })
        .catch(err => {});
    },
    change: function(evt) {
      console.log(evt);
    },
    save(){
      this.loading = true;
      this.sprintList = this.projectDetailList.map(item => {
        let sprintsList = [];
        item.iterationList.forEach(iterationItem => {
            sprintsList.push(iterationItem.value);
        });
        return {
          sprintIdStr: sprintsList.join(","),
          projectId: item.projectId
        };
      });

      this.api
        .saveSprintRelatedInfo({ sprintProjectStr: JSON.stringify(this.sprintList) })
        .then(res=>{
          this.loading = false;
          if (res.code === 0) {
          }

        }).catch(err => {
        this.loading = false;
      });
    }
  },
  components: {
    "vue-draggable": vuedraggable
  },
  mounted() {
    this.$nextTick(()=>{
      this.getProjectList().then(res=>{
        this.selProject = window.localStorage.getItem("jira_iteration_project") ? window.localStorage.getItem("jira_iteration_project").split(",") : [];
        let projectObj = this.projectList.filter(item=>{
          return this.selProject.includes(""+item.value);
        })
        this.selProjectFun(this.selProject,projectObj);
      });
    });

    this.getUnrelatedSprintList();

  },
  watch:{
    selProject(newV,oldV) {
      if(newV && newV.length>0){
        window.localStorage.setItem("jira_iteration_project",newV.join(","));
        console.log(window.localStorage.getItem("jira_iteration_project"))
      }
    },
  }
  //   computed: {
  //     unassociatedIteration: function() {
  //         this.projectDetailList.forEach(()=>{});
  //       return this.availableMenus.filter(menu => {
  //         let collection = this.collections.find(
  //           collection => collection.menu.id === menu.id
  //         );
  //         return collection == undefined ? true : false;
  //       });
  //     }
  //   }
};
</script>

<style scoped lang="scss">
#containerWeb {
  height: 100%;
  width: 100%;
  position: relative;
  padding: 10px;
  overflow:scroll;
}
.r-wrap {
  position: absolute;
  top: 5px;
  right: 10px;
  padding: 10px;
  height: 50px;
  .sel_project_wrap {
    position: absolute;
    /deep/ .c-select-group {
      opacity: 0;
      height: 32px;
      width: 150px !important;
      //  z-index: 99;
    }
    height: 35px;
    position: relative;
    .sel_project_title {
      right: 0px;
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
}


.interationList-wrap {
  padding: 10px;
  h5 {
    height: 40px;
    line-height: 40px;
    font-size: 16px;
    color: #fff;
    border-bottom: 1px solid #232d50;
  }
  .dragItem {
    float: left;
    padding: 6px 13px;
    border-radius: 3px;
    background-color: #2f3b63;
    margin: 3px 5px;
    color: #fff;
    font-size: 14px;
    cursor: pointer;
  }
  .c-btn {
    margin: 3px 5px;
    float: left;
  }
  .drag-main {
    width: 100%;
    min-height:30px;
    overflow: hidden;
  }
}
</style>
