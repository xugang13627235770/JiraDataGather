<template>
  <div id="page" class="content">
      <div class="toolsBar">
            <c-select class="ico fr" v-model="selProject" :options="projectList" search multiple extra width="20px" @change="getProjectData"></c-select>
     </div>
     <!-- 统计图面板 -->
     <div class="list" v-if="projectData.length">
         <!-- 项目 -->
         <div class="project" v-for="(item,index) in projectData" :key="index">
             <h2 class="title"><span>{{item.name}}</span><span>故事数:{{item.totalStory}}</span><span>故事点数:{{item.totalStoryPoint}}</span><span>缺陷数:{{item.totalIssue}}</span><span>开发人员:{{item.totalDeveloper}}</span><span>测试人员:{{item.totalTester}}</span><span>设计人员:{{item.totalDesigner}}</span></h2>
             <div class="chartBox" >
                 <!-- 问题类型统计 -->
                <div  class="chart" v-if="item.barChartData">
                    <bar-Chart :chartData="item.barChartData"  :ref="'barChart'+index"></bar-Chart>
                </div>
                 <!-- 人员总问题占比 -->
                <div class="chart"  v-if="item.pieChartData">
                    <pie-Chart :chartData="item.pieChartData" :ref="'pieChart'+index"></pie-Chart>
                </div>
                <!-- 总问题累计（仅显示仅30日） -->
                <div class="chart" v-if="item.lineChartData">
                    <line-Chart :chartData="item.lineChartData" :ref="'lineChart'+index"></line-Chart>
                </div>
             </div>
         </div>
     </div>
     <c-page-loading v-if="loading"></c-page-loading>
  </div>
</template>

<script>
import bar from "../common/chart/bar"
import pie from "../common/chart/pie"
import line from "../common/chart/line"
export default {
    data(){
        return {
            // projectIdList:[],
            loading:false,
            projectList:[],
            selProject: [1],
            projectData:[{
              id:"",
              name:"",
              totalStoryPoint:"",
              totalIssue:"",
              totalStory:"",
              totalDeveloper:"",
              totalTester:"",
              totalDesigner:"",
              barChartData:this.barChartData,
              pieChartData:this.pieChartData,
              lineChartData:this.lineChartData
            }],
            // bar图模拟数据
            barChartData:[{
              name:"",
              closeNum:"",
              unCloseNum:""
            }],
            // pie图模拟数据
            pieChartData:[{
              name:"",
              value:""
            }],
            // 总问题累计模拟数据
            lineChartData:{
                timeArr:[],
                list:[
                    {type:"",data:[]}
                ]
            }

        }
    },
    methods: {
        // 获取项目列表
        getProjectListByName(){
            let _this=this
            return new Promise(resolve=>{
                this.api.getProjectListByName().then(res=>{
                    if(res){
                        if(res.code==0){
                            if(res.projectList){
                                _this.projectList=res.projectList.map(item=>{
                                    return {
                                        "label":item.name,
                                        "value":item.id
                                    }
                                })
                                resolve(_this.projectList)
                            }
                        }else{
                            resolve([])
                        }
                    }
                }).catch(err=>{
                    resolve([])
                })
            })

        },
      // 获取项目列表统计数据
      getProjectData(value,data){
        //反选
        if(value.length==0 && this.projectData.length>0){
          this.projectData=[];
          return;
        }

        let projectIdArray = this.projectData.map(item=>{
          return item.id;
        })

        //删除项目-全部删除，否则图形统计无法更新
        for(var i=0;i<projectIdArray.length;i++){
          if(!this.api.isInArray(value, projectIdArray[i])){
            this.projectData=[]
            projectIdArray=[]
          }
        }
        //新增项目
        value.forEach(projectId =>{
          if(!this.api.isInArray(projectIdArray, projectId)){
            this.getDataByProjectId(projectId);
          }
        });


      },
      //封装项目数据
      getDataByProjectId(value){
        this.api.getIssueDataByProjectId({projectId : value}).then(res=>{
          if(res.code==0){
            let item = res.list
            let obj={}
            obj.id = item.issueStatistics.projectId
            obj.name=item.issueStatistics.projectName
            obj.totalStoryPoint=item.issueStatistics.totalStoryPoint
            obj.totalIssue=item.issueStatistics.totalIssue
            obj.totalStory=item.issueStatistics.totalStory
            obj.totalDeveloper=item.issueStatistics.totalDeveloper
            obj.totalTester=item.issueStatistics.totalTester
            obj.totalDesigner=item.issueStatistics.totalDesigner
            //问题类型统计
            obj.barChartData = item.issueTypeList.map(obj => {
              return {
                name: obj.issueTypeStr,
                closeNum: obj.closeIssueNum,
                unCloseNum: obj.noCloseIssueNum
              };
            });
            // 人员总问题占比
            obj.pieChartData=item.issueScaleList.map(obj => {
              return {
                name: obj.displayName,
                value: obj.issueRate
              };
            });
            // 总问题累计
            obj.lineChartData={
              timeArr: item.issueDateChart.timeArr,
              list: item.issueDateChart.list.map(issueDateObj=>{
                return {
                  type:issueDateObj.type,
                  data:issueDateObj.data
                }
              })
            };
            this.projectData.push(obj);
          }
        });
      }
    },
    components: {
      "bar-Chart":bar,
      "pie-Chart":pie,
      "line-Chart":line
    },
    // created(){
    //     let _this=this
    //     this.getProjectListByName().then(res=>{
    //         if(res.length){
    //             // 判断是否选择过项目
    //             if(window.localStorage.getItem("projectList")){
    //                 let projectList=JSON.parse(window.localStorage.getItem("projectList"))
    //                 let projectData=JSON.parse(window.localStorage.getItem("projectData"))
    //                     //确认项目接口加载完，回写选择的项目
    //                     _this.$set(_this,"projectIdList",projectList)
    //                     // 回写项目对应的数据
    //                     //_this.$set(_this,"projecData",projecData)
    //                     _this.projectData=projectData
    //             }
    //         }
    //     })
    //
    // },
    mounted(){
      this.$nextTick(()=>{
        this.getProjectListByName().then(res=>{
          this.selProject = window.localStorage.getItem("home_sel_project") ? window.localStorage.getItem("home_sel_project").split(",") : [];
          let projectObj = this.projectList.filter(item=>{
            return this.selProject.includes(""+item.value);
          })
          if(this.selProject == 0){
            let projectIdArray = this.projectList.map(item=>{
              return item.value
            })
            this.selProject.push(projectIdArray[0])
          }
          this.getProjectData(this.selProject,projectObj);
        });
      })
    },
    watch:{
      selProject(newV,oldV) {
        if(newV && newV.length>0){
          window.localStorage.setItem("home_sel_project",newV.join(","));
          console.log(window.localStorage.getItem("home_sel_project"))
        }else{
          window.localStorage.setItem("home_sel_project",[]);
        }
      },
    }
}

</script>
<style lang="scss" scoped>
@import "../../../static/styles/index.scss";
</style>
