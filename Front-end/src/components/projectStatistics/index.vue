<template>
  <div id="page" class="content">
    <!-- 统计图面板 -->
    <div class="list">
      <div id="search-box" class="block-shadow-bg bordered">
        <div>
          <label for="">项目选择：</label>
          <div>
            <c-select
              search
              width="230px"
              v-model="selProject"
              :options="projectList"
              menu-width="230px"
              @change="getProjectData"
            ></c-select>
          </div>
        </div>

        <div>
          <label for="">迭代：</label>
          <div>
            <c-select
              width="230px"
              v-model="sprintId"
              :options="iterationList"
              menu-width="230px"
            ></c-select>
          </div>
        </div>

        <div>
          <c-button size="sm" type="linear" @click.native="searchEventFun">查询</c-button>
        </div>
      </div>
      <div>
        <div class="horizontal" >
            <div v-if="showTarget" >
              <div class="table-tool">
                <h4>指定项目迭代内的迭代统计</h4>
                <div>
                  <c-button size="sm" type="linear" @click.native="showRule" >统计规则</c-button>
                  <c-button size="sm" type="linear" @click.native="exportExcel" >导出</c-button>
                </div>
              </div>
              <div data-v-b430ee82="" class="c-data-table">
                <div class="c-data-table-bar clearfix">
                  <div style="float: left; display: inline-block;"></div>
                </div>
                <div class="" style="width: 100%; overflow: hidden; position: relative;">
                  <div class="clearfix bordered-b" style="overflow: hidden; position: relative; z-index: 1;">
                    <div style="overflow: auto; position: relative; padding-bottom: 0px; width: calc(100% - 0px);">
                      <table class="c-table c-table-bordered c-table-hover" style="overflow: auto;">
                        <thead class="c-content-header">
                        <tr>
                          <th width="40px" style="text-align: center;">序号</th>
                          <th width="150px" data-index="0" colspan="1" class="" style="width: 250px;">
                            <div class="title" style="width: 150px; text-align: left;">
                              统计项
                            </div>
                            <!----></th>
                          <th width="100px" data-index="1" colspan="1" class="" style="width: 100px;">
                            <div class="title" style="width: 100px; text-align: left;">
                              指标
                            </div>
                            <!----></th>
                          <th width="100px" data-index="2" colspan="1" class="" style="width: 100px;">
                            <div class="title" style="width: 100px; text-align: left;">
                              得分值
                            </div>
                            <!----></th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr track-by="cKey" class="tr-pointer">
                          <!---->
                          <td width="40px" class="nopadding" style="text-align: center;"><span style="max-width: 40px; display: inline-block;">1</span></td>
                          <td width="150px" colspan="1" rowspan="1" style="text-align: left; width: 150px;"><span title="迭代目标完成率" style="width: 150px;">迭代目标完成率</span></td>
                          <td width="100px" colspan="1" rowspan="1" style="text-align: left; width: 100px;"><span title="" style="width: 100px;">{{targetData.sprintTargetRate}}</span></td>
                          <td width="100px" colspan="1" rowspan="1" style="text-align: left; width: 100px;"><span title="" style="width: 100px;">{{targetData.sprintTargetRateScore}}</span></td>
                        </tr>
                        <tr track-by="cKey" class="tr-pointer">
                          <!---->
                          <td width="40px" class="nopadding" style="text-align: center;"><span style="max-width: 40px; display: inline-block;">2</span></td>
                          <td width="150px" colspan="1" rowspan="1" style="text-align: left; width: 150px;"><span title="人均关闭故事点数" style="width: 150px;">人均关闭故事点数</span></td>
                          <td width="100px" colspan="1" rowspan="1" style="text-align: left; width: 100px;"><span title="" style="width: 100px;">{{targetData.avgStoryPoint}}</span></td>
                          <td width="100px" colspan="1" rowspan="1" style="text-align: left; width: 100px;"><span title="" style="width: 100px;">{{targetData.avgStoryPointScore}}</span></td>
                        </tr>
                        <tr track-by="cKey" class="tr-pointer">
                          <!---->
                          <td width="40px" class="nopadding" style="text-align: center;"><span style="max-width: 40px; display: inline-block;">3</span></td>
                          <td width="150px" colspan="1" rowspan="1" style="text-align: left; width: 150px;"><span title="平均故事关闭天数" style="width: 150px;">平均故事关闭天数</span></td>
                          <td width="100px" colspan="1" rowspan="1" style="text-align: left; width: 100px;"><span title="" style="width: 100px;">{{targetData.avgStoryTime}}</span></td>
                          <td width="100px" colspan="1" rowspan="1" style="text-align: left; width: 100px;"><span title="" style="width: 100px;">{{targetData.avgStoryTimeScore}}</span></td>
                        </tr>
                        <tr track-by="cKey" class="tr-pointer">
                          <!---->
                          <td width="40px" class="nopadding" style="text-align: center;"><span style="max-width: 40px; display: inline-block;">4</span></td>
                          <td width="150px" colspan="1" rowspan="1" style="text-align: left; width: 150px;"><span title="平均故事提测延时天数" style="width: 150px;">平均故事提测延时天数</span></td>
                          <td width="100px" colspan="1" rowspan="1" style="text-align: left; width: 100px;"><span title="" style="width: 100px;">{{targetData.avgStoryDelay}}</span></td>
                          <td width="100px" colspan="1" rowspan="1" style="text-align: left; width: 100px;"><span title="" style="width: 100px;">{{targetData.avgStoryDelayScore}}</span></td>
                        </tr>
                        <tr track-by="cKey" class="tr-pointer">
                          <!---->
                          <td width="40px" class="nopadding" style="text-align: center;"><span style="max-width: 40px; display: inline-block;">5</span></td>
                          <td width="150px" colspan="1" rowspan="1" style="text-align: left; width: 150px;"><span title="重新打开故事次数" style="width: 150px;">重新打开故事次数</span></td>
                          <td width="100px" colspan="1" rowspan="1" style="text-align: left; width: 100px;"><span title="" style="width: 100px;">{{targetData.reopenStory}}</span></td>
                          <td width="100px" colspan="1" rowspan="1" style="text-align: left; width: 100px;"><span title="" style="width: 100px;">{{targetData.reopenStoryScore}}</span></td>
                        </tr>
                        <tr track-by="cKey" class="tr-pointer">
                          <!---->
                          <td width="40px" class="nopadding" style="text-align: center;"><span style="max-width: 40px; display: inline-block;">6</span></td>
                          <td width="150px" colspan="1" rowspan="1" style="text-align: left; width: 150px;"><span title="遗留故事占比" style="width: 150px;">遗留故事占比</span></td>
                          <td width="100px" colspan="1" rowspan="1" style="text-align: left; width: 100px;"><span title="" style="width: 100px;">{{targetData.remainStory}}</span></td>
                          <td width="100px" colspan="1" rowspan="1" style="text-align: left; width: 100px;"><span title="" style="width: 100px;">{{targetData.remainStoryScore}}</span></td>
                        </tr>
                        <tr track-by="cKey" class="tr-pointer">
                          <!---->
                          <td width="40px" class="nopadding" style="text-align: center;"><span style="max-width: 40px; display: inline-block;">7</span></td>
                          <td width="150px" colspan="1" rowspan="1" style="text-align: left; width: 150px;"><span title="逾期完成故事点数" style="width: 150px;">逾期完成故事点数</span></td>
                          <td width="100px" colspan="1" rowspan="1" style="text-align: left; width: 100px;"><span title="" style="width: 100px;">{{targetData.overdueStoryPoint}}</span></td>
                          <td width="100px" colspan="1" rowspan="1" style="text-align: left; width: 100px;"><span title="" style="width: 100px;">{{targetData.overdueStoryPointScore}}</span></td>
                        </tr>
                        <tr track-by="cKey" class="tr-pointer">
                          <!---->
                          <td width="40px" class="nopadding" style="text-align: center;"><span style="max-width: 40px; display: inline-block;">8</span></td>
                          <td width="150px" colspan="1" rowspan="1" style="text-align: left; width: 150px;"><span title="缺陷关闭率" style="width: 150px;">缺陷关闭率</span></td>
                          <td width="100px" colspan="1" rowspan="1" style="text-align: left; width: 100px;"><span title="" style="width: 100px;">{{targetData.defectCloseRate}}</span></td>
                          <td width="100px" colspan="1" rowspan="1" style="text-align: left; width: 100px;"><span title="" style="width: 100px;">{{targetData.defectCloseRateScore}}</span></td>
                        </tr>
                        <tr track-by="cKey" class="tr-pointer">
                          <!---->
                          <td width="40px" class="nopadding" style="text-align: center;"><span style="max-width: 40px; display: inline-block;">9</span></td>
                          <td width="150px" colspan="1" rowspan="1" style="text-align: left; width: 150px;"><span title="缺陷密度" style="width: 150px;">缺陷密度</span></td>
                          <td width="100px" colspan="1" rowspan="1" style="text-align: left; width: 100px;"><span title="" style="width: 100px;">{{targetData.defectDensity}}</span></td>
                          <td width="100px" colspan="1" rowspan="1" style="text-align: left; width: 100px;"><span title="" style="width: 100px;">{{targetData.defectDensityScore}}</span></td>
                        </tr>
                        <tr track-by="cKey" class="tr-pointer">
                          <!---->
                          <td width="40px" class="nopadding" style="text-align: center;"><span style="max-width: 40px; display: inline-block;">10</span></td>
                          <td width="150px" colspan="1" rowspan="1" style="text-align: left; width: 150px;"><span title="遗留缺陷比例" style="width: 150px;">遗留缺陷比例</span></td>
                          <td width="100px" colspan="1" rowspan="1" style="text-align: left; width: 100px;"><span title="" style="width: 100px;">{{targetData.remainDefectRate}}</span></td>
                          <td width="100px" colspan="1" rowspan="1" style="text-align: left; width: 100px;"><span title="" style="width: 100px;">{{targetData.remainDefectRateScore}}</span></td>
                        </tr>
                        <tr track-by="cKey" class="tr-pointer">
                          <!---->
                          <td width="40px" class="nopadding" style="text-align: center;"><span style="max-width: 40px; display: inline-block;">11</span></td>
                          <td width="150px" colspan="1" rowspan="1" style="text-align: left; width: 150px;"><span title="线上缺陷数量" style="width: 150px;">线上缺陷数量</span></td>
                          <td width="100px" colspan="1" rowspan="1" style="text-align: left; width: 100px;"><span title="" style="width: 100px;">{{targetData.onlineDefect}}</span></td>
                          <td width="100px" colspan="1" rowspan="1" style="text-align: left; width: 100px;"><span title="" style="width: 100px;">{{targetData.onlineDefectScore}}</span></td>
                        </tr>
                        <tr track-by="cKey" class="tr-pointer">
                          <!---->
                          <td width="40px" class="nopadding" style="text-align: center;"><span style="max-width: 40px; display: inline-block;">12</span></td>
                          <td width="150px" colspan="1" rowspan="1" style="text-align: left; width: 150px;"><span title="线上缺陷平均修复天数" style="width: 150px;">线上缺陷平均修复天数</span></td>
                          <td width="100px" colspan="1" rowspan="1" style="text-align: left; width: 100px;"><span title="" style="width: 100px;">{{targetData.onlineRepairTime}}</span></td>
                          <td width="100px" colspan="1" rowspan="1" style="text-align: left; width: 100px;"><span title="" style="width: 100px;">{{targetData.onlineRepairTimeScore}}</span></td>
                        </tr>
                        <tr track-by="cKey" class="tr-pointer">
                          <!---->
                          <td width="40px" class="nopadding" style="text-align: center;"><span style="max-width: 40px; display: inline-block;">13</span></td>
                          <td width="150px" colspan="1" rowspan="1" style="text-align: left; width: 150px;"><span title="迭代期间新增故事数" style="width: 150px;">迭代期间新增故事数</span></td>
                          <td width="100px" colspan="1" rowspan="1" style="text-align: left; width: 100px;"><span title="" style="width: 100px;">{{targetData.addStory}}</span></td>
                          <td width="100px" colspan="1" rowspan="1" style="text-align: left; width: 100px;"><span title="" style="width: 100px;">{{targetData.addStoryScore}}</span></td>
                        </tr>
                        </tbody>
                      </table>
                    </div>
                    <!---->
                    <!---->
                  </div>
                  <!---->
                </div>
              </div>
            </div>
        </div>
        <div class="horizontal-list">
          <div class="project" style="padding-left: 100px">
            <div class="chartBox" >
              <!-- 迭代总缺陷数量人员占比 -->
              <div  class="chart" v-if="projectData.defectPieData.length" >
                <defectPie-Chart :chartData="projectData.defectPieData"  ref="defectPie"></defectPie-Chart>
              </div>
              <!-- 迭代故事点数人员占比 -->
              <div class="chart"  v-if="projectData.storyPointPieData.length">
                <storyPointPie-Chart :chartData="projectData.storyPointPieData" ref="storyPointPie"></storyPointPie-Chart>
              </div>

            </div>
          </div>
          <div class="project" style="padding-left: 100px">
            <div class="chartBox" >
              <!-- 迭代故事点数燃尽图 -->
              <!--<div  class="chart" v-if="projectData.storyPointLineData" style="width:calc(66.6% - 10px)">-->
              <!--<storyPointLine-Chart :chartData="projectData.storyPointLineData"  ref="storyPointLineData"></storyPointLine-Chart>-->
              <!--</div>-->
              <!-- 迭代新增问题类型统计 -->
              <div class="chart"  v-if="projectData.issueBarData.length">
                <issueBar-Chart :chartData="projectData.issueBarData" ref="issueBarData"></issueBar-Chart>
              </div>

            </div>
          </div>
        </div>
        <div class="clean"/>
      </div>

    </div>
    <c-page-loading v-if="loading"></c-page-loading>
    <!-- 规则管理 弹窗 -->
    <statistic-rule ref="rules"></statistic-rule>
  </div>
</template>

<script>
  import defectPie from "../common/chart1/defectPie"
  import issueBar from "../common/chart1/issueBar"
  import storyPointLine from "../common/chart1/storyPointLine"
  import storyPointPie from "../common/chart1/storyPointPie"
  import statisticRule from "./modal/rules"
  export default {
    data(){
      return {
        showDrop: false,
        appNo: appNo,
        baseUrl: baseURL,
        token: "Basic " + Token,
        tableHeight: "400px",
        iterationList:[],
        sprintId:"",
        loading:false,
        showTarget:false,
        // projectIdList:[],
        projectList:[],
        selProject: "",
        projectData:{
          defectPieData:[],// 迭代总缺陷数量人员占比
          storyPointPieData:[],//迭代故事点数人员占比
          storyPointLineData:null,// 迭代故事点数燃尽图
          issueBarData:[] // 迭代新增问题类型统计
        },
        targetData:{
          sprintTargetRate:null,//迭代目标完成率
          sprintTargetRateScore:null,//迭代目标完成率得分
          avgStoryPoint:null,//人均关闭故事点数
          avgStoryPointScore:null,//人均关闭故事点数得分
          avgStoryTime:null,//平均故事关闭时长
          avgStoryTimeScore:null,//平均故事关闭时长得分
          avgStoryDelay:null,//平均故事延期时长
          avgStoryDelayScore:null,//平均故事延期时长得分
          reopenStory:null,//重新打开故事次数
          reopenStoryScore:null,//重新打开故事次数得分
          remainStory:null,//迭代遗留故事占比
          remainStoryScore:null,//迭代遗留故事占比得分
          overdueStoryPoint:null,//逾期完成故事点数
          overdueStoryPointScore:null,//逾期完成故事点数得分
          defectCloseRate:null,//缺陷关闭率
          defectCloseRateScore:null,//缺陷关闭率得分
          defectDensity:null,//缺陷密度
          defectDensityScore:null,//缺陷密度得分
          remainDefectRate:null,//遗留缺陷比例
          remainDefectRateScore:null,//遗留缺陷比例得分
          onlineDefect:null,//本迭代内线上反馈的缺陷
          onlineDefectScore:null,//线上缺陷得分
          onlineRepairTime:null,//平均线上缺陷修复时长
          onlineRepairTimeScore:null,//线上缺陷修复时长得分
          addStory:null,//迭代期间新增故事数
          addStoryScore:null,//迭代期间新增故事数扣分
        },
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
        if(value.length==0){
          this.sprintId=""
          return;
        }
        this.api
          .getSprintListByProjectId({projectId: value })
          .then(res => {
            if (res.code == 0) {
              if(res.sprintList != null && res.sprintList.length!=0){
                this.iterationList = res.sprintList.map(obj => {
                  let name = obj.name
                  if(obj.state == 1){
                    name = obj.name+"(当前迭代)";
                  }
                  return {
                    value: obj.id,
                    label: name
                  }
                })
              }else{
                this.iterationList=[]
                this.sprintId=""
              }
            }
          })
          .catch(err => {
          });
      },
      searchEventFun() {
        if(this.selProject.length==0){
          this.api.warnTip(this, "请选择项目信息");
          return;
        }
        if(this.sprintId.length==0){
          this.api.warnTip(this, "请选择迭代信息");
          return;
        }

        this.getSprintStatisticsEntityBySprintId();
        this.getDefectRate();
        this.getStoryPointRate();
        this.getAddIssueTypeList();
      //   setTimeout(() => {
      //     this.getDefectRate();
      //     this.getStoryPointRate();
      //     this.getAddIssueTypeList();
      //   }, 300)
      //   setTimeout(() => {
      //     this.getAvgStoryPoint();
      //     this.getAvgStoryTime();
      //     this.getAvgStoryDelay();
      //     this.getReopenStory();
      //     this.getRemainStory();
      //     this.getOverdueStoryPoint();
      //     this.getDefectCloseRate();
      //     this.getDefectDensity();
      //     this.getRemainDefectRate();
      //     this.getOnlineDefect();
      //     this.getOnlineRepairTime();
      //   }, 500)
      },
      //迭代总缺陷数量人员占比
      getDefectRate(){
        this.$set(this.projectData,"defectPieData",[])
        this.api
          .getDefectRate({sprintId:this.sprintId})
          .then(res => {
            if (res.code == 0) {
              let obj={};
              //迭代总缺陷数量人员占比
              obj.defectPieData = res.defectRateList.map(obj => {
                return {
                  name: obj.displayName,
                  value: obj.defectRate
                }
              });
              this.$set(this.projectData,"defectPieData",obj.defectPieData)
            }
          })
          .catch(err => {
          });
      },
      //迭代故事点数人员占比
      getStoryPointRate(){
        this.$set(this.projectData,"storyPointPieData",[])
        this.api
          .getStoryPointRate({sprintId:this.sprintId})
          .then(res => {
            if (res.code == 0) {
              let obj={};
              //迭代故事点数人员占比
              obj.storyPointPieData = res.storyPointRateList.map(obj => {
                return {
                  name: obj.displayName,
                  value: obj.storyPointRate
                }
              });
              this.$set(this.projectData,"storyPointPieData",obj.storyPointPieData)
            }
          })
          .catch(err => {
          });
      },
      //迭代故事点数燃尽图
      // getStoryPointRecordBySprintId(){
      //   let vm=this
      //   vm.$set(vm.projectData,"storyPointLineData",null)
      //   this.api
      //     .getStoryPointRecordBySprintId({sprintId:this.sprintId})
      //     .then(res => {
      //       if (res.code == 0) {
      //         let obj={};
      //         //迭代故事点数燃尽图
      //         obj.storyPointLineData = {
      //           timeArr: res.storyPointChart.timeArr,
      //           list: res.storyPointChart.list.map(storyPointObj=>{
      //             return {
      //               type:storyPointObj.type,
      //               data:storyPointObj.data
      //             }
      //           })
      //         };
      //         vm.$set(vm.projectData,"storyPointLineData",obj.storyPointLineData)
      //       }
      //     })
      //     .catch(err => {
      //     });
      // },
      //迭代新增问题类型统计
      getAddIssueTypeList(){
        this.$set(this.projectData,"issueBarData",[])
        this.api
          .getAddIssueTypeList({sprintId:this.sprintId})
          .then(res => {
            if (res.code == 0) {
              let obj={};
              //迭代新增问题类型统计
              obj.issueBarData = res.addIssueTypeList.map(obj => {
                return {
                  name: obj.issueTypeStr,
                  closeNum: obj.closeIssueNum,
                  unCloseNum: obj.noCloseIssueNum
                };
              });
              this.$set(this.projectData,"issueBarData",obj.issueBarData)
            }
          })
          .catch(err => {
          });
      },
      //迭代内各项指标
      getSprintStatisticsEntityBySprintId(){
        this.loading = true;
        this.showTarget = false;
        this.api
          .getSprintStatisticsEntityBySprintId({sprintId:this.sprintId})
          .then(res => {
            this.loading = false;
            this.showTarget = true;
            if (res.code == 0 && res.sprintStatistics !=null) {
              this.targetData = res.sprintStatistics
            }else{
              this.targetData={}
            }
          })
          .catch(err => {
            this.loading = false;
          });
      },
      // //迭代目标完成率
      // getSprintTargetRate(){
      //   this.loading = true;
      //   this.showTarget = false;
      //   this.api
      //     .getSprintTargetRate({sprintId:this.sprintId})
      //     .then(res => {
      //       this.loading = false;
      //       this.showTarget = true;
      //       if (res.code == 0) {
      //         this.targetData.sprintTargetRate = res.sprintTargetRate
      //       }
      //     })
      //     .catch(err => {
      //       this.loading = false;
      //     });
      // },
      // //人均关闭故事点数
      // getAvgStoryPoint(){
      //   this.api
      //     .getAvgStoryPoint({sprintId:this.sprintId})
      //     .then(res => {
      //       if (res.code == 0) {
      //         this.targetData.avgStoryPoint = res.avgStoryPoint
      //       }
      //     })
      //     .catch(err => {
      //     });
      // },
      // //平均故事关闭时长
      // getAvgStoryTime(){
      //   this.api
      //     .getAvgStoryTime({sprintId:this.sprintId})
      //     .then(res => {
      //       if (res.code == 0) {
      //         this.targetData.avgStoryTime = res.avgStoryTime
      //       }
      //     })
      //     .catch(err => {
      //     });
      // },
      // //平均故事延期时长
      // getAvgStoryDelay(){
      //   this.api
      //     .getAvgStoryDelay({sprintId:this.sprintId})
      //     .then(res => {
      //       if (res.code == 0) {
      //         this.targetData.avgStoryDelay = res.avgStoryDelay
      //       }
      //     })
      //     .catch(err => {
      //     });
      // },
      // //重新打开故事次数
      // getReopenStory(){
      //   this.api
      //     .getReopenStory({sprintId:this.sprintId})
      //     .then(res => {
      //       if (res.code == 0) {
      //         this.targetData.reopenStory = res.reopenStory
      //       }
      //     })
      //     .catch(err => {
      //     });
      // },
      // //迭代遗留故事占比
      // getRemainStory(){
      //   this.api
      //     .getRemainStory({sprintId:this.sprintId})
      //     .then(res => {
      //       if (res.code == 0) {
      //         this.targetData.remainStory = res.remainStory
      //       }
      //     })
      //     .catch(err => {
      //     });
      // },
      // //逾期完成故事点数
      // getOverdueStoryPoint(){
      //   this.api
      //     .getOverdueStoryPoint({sprintId:this.sprintId})
      //     .then(res => {
      //       if (res.code == 0) {
      //         this.targetData.overdueStoryPoint = res.overdueStoryPoint
      //       }
      //     })
      //     .catch(err => {
      //     });
      // },
      // //缺陷关闭率
      // getDefectCloseRate(){
      //   this.api
      //     .getDefectCloseRate({sprintId:this.sprintId})
      //     .then(res => {
      //       if (res.code == 0) {
      //         this.targetData.defectCloseRate = res.defectCloseRate
      //       }
      //     })
      //     .catch(err => {
      //     });
      // },
      // //缺陷密度
      // getDefectDensity(){
      //   this.api
      //     .getDefectDensity({sprintId:this.sprintId})
      //     .then(res => {
      //       if (res.code == 0) {
      //         this.targetData.defectDensity = res.defectDensity
      //       }
      //     })
      //     .catch(err => {
      //     });
      // },
      // //遗留缺陷比例
      // getRemainDefectRate(){
      //   this.api
      //     .getRemainDefectRate({sprintId:this.sprintId})
      //     .then(res => {
      //       if (res.code == 0) {
      //         this.targetData.remainDefectRate = res.remainDefectRate
      //       }
      //     })
      //     .catch(err => {
      //     });
      // },
      // //本迭代内线上反馈的缺陷
      // getOnlineDefect(){
      //   this.api
      //     .getOnlineDefect({sprintId:this.sprintId})
      //     .then(res => {
      //       if (res.code == 0) {
      //         this.targetData.onlineDefect = res.onlineDefect
      //       }
      //     })
      //     .catch(err => {
      //     });
      // },
      // //平均线上缺陷修复时长
      // getOnlineRepairTime(){
      //   this.api
      //     .getOnlineRepairTime({sprintId:this.sprintId})
      //     .then(res => {
      //       if (res.code == 0) {
      //         this.targetData.onlineRepairTime = res.onlineRepairTime
      //       }
      //     })
      //     .catch(err => {
      //     });
      // },
      // 导出
      exportExcel() {
        if(this.sprintId == {}){
          this.api.warnTip(this, "请选择迭代信息");
          return;
        }

        this.loading = true;
        this.api
          .exportSprintTarget({sprintId:this.sprintId})
          .then(res => {
            if (res.code == 0) {
              this.api.downloadFile(res.data);
            }
            this.loading = false;
          })
          .catch(err => {
            this.loading = false;
          });
      },
      // 查看统计规则
      showRule() {
        this.$refs.rules.openModal();
      }
    },
    components: {
      "defectPie-Chart":defectPie,
      "issueBar-Chart":issueBar,
      "storyPointLine-Chart":storyPointLine,
      "storyPointPie-Chart":storyPointPie,
      "statistic-rule":statisticRule
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
          this.selProject = window.localStorage.getItem("project_sel_project") ? window.localStorage.getItem("project_sel_project") : "";
          let projectObj = this.projectList.filter(item=>{
            return this.selProject.includes(""+item.value);
          })
          this.getProjectData(this.selProject,projectObj)
          this.sprintId = window.localStorage.getItem("project_sel_sprint") ? window.localStorage.getItem("project_sel_sprint") : "";
          this.searchEventFun()
        });
      })
    },
    watch:{
      selProject(newV,oldV) {
        if(newV){
          window.localStorage.setItem("project_sel_project",newV);
          this.sprintId = ""
          console.log(window.localStorage.getItem("project_sel_project"))
        }
      },
      sprintId(newV,oldV) {
        if(newV){
          window.localStorage.setItem("project_sel_sprint",newV);
          console.log(window.localStorage.getItem("project_sel_sprint"))
        }
      },
    }
  }

</script>
<style lang="scss" scoped>
  @import "../../../static/styles/index.scss";

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

  #search-box {
    margin: 18px 18px 0 18px;
    height: 60px;
    display: flex;
    align-items: center;
    &>div{
      margin-right: 15px;
      display: inline-block;
      label{
        display: inline-block;
        width: 80px;
        text-align: right;
      }
      &>div{
        display: inline-block
      }
    }
    .c-row {
      height: 100%;
      display: flex;
      align-items: center;
    }
  }

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
  .horizontal-list{
    width:calc(100%);
    margin:0 10px;
    height: calc(100%);
    padding-right: 100px;
  }
  .horizontal{
    float:left;//添加float样式即可实现水平排列
    border:1px solid black;
    margin-left:20px;
  }
  .clean{
    clear:both;
  }
</style>
