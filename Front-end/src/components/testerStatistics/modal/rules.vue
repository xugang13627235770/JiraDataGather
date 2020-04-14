<template>
  <div>
    <c-modal
      title="规则"
      effect="fade"
      width="1300px"
      ref="rule"
      class="rule-wrap"
      @hide="hideModal"
      height="800px"
    >
      <div slot="body" class="modal-main-wrap">
        <div class="modal-main">
          <div class="modal-box">
            <div class="form-box">
              <div class="form-contain">
                <h4 class="title">规则说明</h4>
                <div class="rules_text">
                  <p>
                    总积分 =
                    <span class="tips-blue">总基础分</span> + 有效缺陷新增加分值 + 缺陷平均耗时加分值 + 历史缺陷关闭加分值 - 缺陷有效率扣分值 - 违规操作扣分值 - 线上反馈缺陷扣分值
                  </p>
                  <ul class="rule_list">
                    <li>
                      <span class="circle-icon"></span>有效缺陷新增加分值  =
                      FIND(迭代内新增的、解决结果为“已解决”和“未解决”状态的缺陷数量,
                      <span class="tips-red">缺陷数量加分区间</span>)
                    </li>
                    <li>
                      <span class="circle-icon"></span>缺陷平均耗时加分值 = FIND(迭代内新增且关闭的缺陷总耗时 / 迭代内新增且关闭的缺陷数量,
                      <span class="tips-red">缺陷平均耗时加分区间</span>）
                    </li>
                    <li>
                      <span class="circle-icon"></span>历史缺陷关闭加分值 = FIND(迭代内历史遗留的缺陷关闭数量,
                      <span class="tips-red">历史缺陷关闭加分区间</span> ）
                    </li>
                    <li>
                      <span class="circle-icon"></span>缺陷有效率扣分值 = 解决结果为“已解决”和“未解决”状态的缺陷数量 >
                      <span class="tips-blue"> 缺陷有效率统计基数 </span> ? FIND(迭代内解决结果为“已解决”和“未解决”状态的缺陷数量/ / 迭代总缺陷数,
                      <span class="tips-red">缺陷有效率扣分区间</span> ) : 0
                    </li>
                    <li>
                      <span class="circle-icon"></span>违规操作扣分值 = MAX(
                      <span class="tips-blue">违规操作最大扣分值</span>, 迭代内新增未指定经办人或经办人为测试人员的缺陷数量)
                    </li>
                    <li>
                      <span class="circle-icon"></span>线上缺陷扣分值 = 迭代内新增，缺陷来源为“线上”的缺陷数量 *
                      <span class="tips-blue">线上缺陷系数</span>
                    </li>
                  </ul>
                </div>
              </div>
            </div>
            <c-form ref="rulesForm">
              <div class="form-box">
                <div class="form-contain">
                  <h4 class="title">参数配置</h4>
                  <c-container fluid>
                    <c-row>
                      <c-column :col="3" class="context">
                        <c-form-item need label="总基础分" :label-col="7">
                          <c-input-number
                            name="totalBaseScore"
                            width="100%"
                            v-model="ruleDetail.totalBaseScore"
                            :rules="[{type:'required'}]"
                          ></c-input-number>
                        </c-form-item>
                      </c-column>
                      <c-column :col="3" class="context">
                        <c-form-item need label="缺陷有效率统计基数" :label-col="8">
                          <c-input-number
                            name="issueValidBase"
                            width="100%"
                            v-model="ruleDetail.issueValidBase"
                            :rules="[{type:'required'}]"
                          ></c-input-number>
                        </c-form-item>
                      </c-column>
                      <c-column :col="3" class="context">
                        <c-form-item need label="违规操作最大扣分值" :label-col="8">
                          <c-input-number
                            name="violateMaxScore"
                            width="100%"
                            v-model="ruleDetail.violateMaxScore"
                            :rules="[{type:'required'}]"
                          ></c-input-number>
                        </c-form-item>
                      </c-column>
                      <c-column :col="3" class="context">
                        <c-form-item need label="线上缺陷系数" :label-col="7">
                          <c-input-number
                            name="onlineIssueRatio"
                            width="100%"
                            v-model="ruleDetail.onlineIssueRatio"
                            :rules="[{type:'required'}]"
                          ></c-input-number>
                        </c-form-item>
                      </c-column>
                    </c-row>
                  </c-container>
                </div>
              </div>
              <div class="form-box">
                <div class="form-contain">
                  <div class="rule">
                    <div class="ruleArr">
                      <h4 class="title">缺陷数量加分区间<i class="iconfont icon-jia1" @click="addRule('1')"></i></h4>
                      <c-container fluid>
                        <c-row v-for="(item,index) in ruleDetail.issueNumList" :key="index">
                          <c-column :col="3" class="context">
                            <c-form-item need label="≥" :label-col="4">
                              <c-input
                                :name="'issueNumLeftSide'+index"
                                width="100%"
                                v-model="item.leftSide"
                                :rules="[{type:'required'},{type: 'number' }]"
                              ></c-input>
                            </c-form-item>
                          </c-column>
                          <c-column :col="4" class="context">
                            <c-form-item :need="(index!=ruleDetail.issueNumList.length-1)?true:false" label="<" :label-col="4">
                              <c-input
                               :name="'issueNumRightSide'+index"
                                width="100%"
                                v-model="item.rightSide"
                                :rules="(index!=ruleDetail.issueNumList.length-1)?[{type:'required'},{type: 'number' }]:[]"
                              ></c-input>
                            </c-form-item>
                          </c-column>
                          <c-column :col="5" class="context">
                            <c-form-item  label="加分值" :label-col="5">
                              <c-input
                                :name="'deviceno'+index"
                                width="calc(100% - 20px)"
                                v-model="item.score"
                                :custom-validate="numberValidate"
                                :rules="[{type:'required'}]"
                              ></c-input>
                              <i class="iconfont icon-shanchu" style="margin-left:0" @click="delRule('1',index)"></i>
                            </c-form-item>
                          </c-column>

                        </c-row>
                      </c-container>
                    </div>
                    <div class="ruleArr">
                      <h4 class="title">缺陷平均耗时(天数)加分区间<i class="iconfont icon-jia1" @click="addRule('2')"></i></h4>
                      <c-container fluid>
                        <c-row v-for="(item,index) in ruleDetail.taskTimeList" :key="index">
                          <c-column :col="3" class="context">
                            <c-form-item need label="≥" :label-col="4">
                              <c-input
                               :name="'taskTimeLeftSide'+index"
                                width="100%"
                                v-model="item.leftSide"
                                :rules="[{type:'required'},{type: 'number' }]"
                              ></c-input>
                            </c-form-item>
                          </c-column>
                          <c-column :col="4" class="context">
                            <c-form-item :need="(index!=ruleDetail.taskTimeList.length-1)?true:false" label="<" :label-col="4">
                              <c-input
                                :name="'taskTimerightSide'+index"
                                width="100%"
                                v-model="item.rightSide"
                                :rules="(index!=ruleDetail.taskTimeList.length-1)?[{type:'required'},{type: 'number' }]:[]"
                              ></c-input>
                            </c-form-item>
                          </c-column>
                          <c-column :col="5" class="context">
                            <c-form-item  label="加分值" :label-col="5">
                              <c-input
                                :name="'deviceno'+index"
                                width="calc(100% - 20px)"
                                v-model="item.score"
                                :custom-validate="numberValidate"
                                :rules="[{type:'required'}]"
                              ></c-input>
                              <i class="iconfont icon-shanchu" style="margin-left:0" @click="delRule('2',index)"></i>
                            </c-form-item>
                          </c-column>

                        </c-row>
                      </c-container>
                    </div>
                  </div>
                  <div class="rule">
                    <div class="ruleArr">
                      <h4 class="title">历史缺陷关闭加分区间<i class="iconfont icon-jia1" @click="addRule('3')"></i></h4>
                      <c-container fluid>
                        <c-row v-for="(item,index) in ruleDetail.historyCloseList" :key="index">
                          <c-column :col="3" class="context">
                            <c-form-item need label="≥" :label-col="4">
                              <c-input

                                :name="'historyLeftSide'+index"
                                width="100%"
                                v-model="item.leftSide"
                                :rules="[{type:'required'},{type: 'number' }]"
                              ></c-input>
                            </c-form-item>
                          </c-column>
                          <c-column :col="4" class="context">
                            <c-form-item :need="(index!=ruleDetail.historyCloseList.length-1)?true:false" label="<" :label-col="4">
                              <c-input
                                :name="'historyrightSide'+index"
                                width="100%"
                                v-model="item.rightSide"
                                :rules="(index!=ruleDetail.historyCloseList.length-1)?[{type:'required'},{type: 'number' }]:[]"
                              ></c-input>
                            </c-form-item>
                          </c-column>
                          <c-column :col="5" class="context">
                            <c-form-item  label="加分值" :label-col="5">
                              <c-input
                                :name="'deviceno'+index"
                                width="calc(100% - 20px)"
                                v-model="item.score"
                                :custom-validate="numberValidate"
                                :rules="[{type:'required'}]"
                              ></c-input>
                              <i class="iconfont icon-shanchu" style="margin-left:0" @click="delRule('3',index)"></i>
                            </c-form-item>
                          </c-column>

                        </c-row>
                      </c-container>
                    </div>
                    <div class="ruleArr">
                      <h4 class="title">缺陷有效率扣分区间<i class="iconfont icon-jia1" @click="addRule('4')"></i></h4>
                      <c-container fluid>
                        <c-row v-for="(item,index) in ruleDetail.validRateList" :key="index">
                          <c-column :col="3" class="context">
                            <c-form-item need label="≥" :label-col="4">
                              <c-input

                                :name="'validRateLeftSide'+index"
                                width="100%"
                                v-model="item.leftSide"
                                 :custom-validate="numberValidate"
                                :rules="[{type:'required'}]"
                              ></c-input>
                            </c-form-item>
                          </c-column>
                          <c-column :col="4" class="context">
                            <c-form-item :need="(index!=ruleDetail.validRateList.length-1)?true:false" label="<" :label-col="4">
                              <c-input
                                :name="'validRaterightSide'+index"
                                width="100%"
                                v-model="item.rightSide"
                                 :custom-validate="(index!=ruleDetail.validRateList.length-1)?numberValidate:''"
                                :rules="(index!=ruleDetail.validRateList.length-1)?[{type:'required'}]:[]"
                              ></c-input>
                            </c-form-item>
                          </c-column>
                          <c-column :col="5" class="context">
                            <c-form-item  label="加分值" :label-col="5">
                              <c-input
                                :name="'deviceno'+index"
                                width="calc(100% - 20px)"
                                v-model="item.score"
                                :custom-validate="numberValidate"
                                :rules="[{type:'required'}]"
                              ></c-input>
                              <i class="iconfont icon-shanchu" style="margin-left:0" @click="delRule('4',index)"></i>
                            </c-form-item>
                          </c-column>

                        </c-row>
                      </c-container>
                    </div>
                  </div>

                </div>
              </div>
            </c-form>
          </div>
        </div>
        <div class="btn-box">
          <c-button type="primary" @click.native="save">保存</c-button>
          <c-button type="gray" @click.native="closeModal">关闭</c-button>
        </div>
      </div>
      <div slot="footer"></div>
    </c-modal>
    <c-page-loading v-if="loading"></c-page-loading>
  </div>
</template>

<script>
const ruleDetailCache = {
  "pkid": 0,
  "totalBaseScore": 0,
  "issueValidBase": 0,
  "violateMaxScore": 0,
  "onlineIssueRatio": 0,
  "issueNumList":[],
  "taskTimeList":[],
  "historyCloseList":[],
  "validRateList":[]
};
export default {
  data() {
    return {
      loading: false,
      title: "",
      mode: "add",
      modeText: {
        add: "新增",
        edit: "编辑"
      },
      ruleDetail: {
         "pkid": 0,
        "totalBaseScore": 0,
        "issueValidBase": 0,
        "violateMaxScore": 0,
        "onlineIssueRatio": 0,
        "issueNumList":this.issueNumList,
        "taskTimeList":this.taskTimeList,
        "historyCloseList":this.historyCloseList,
        "validRateList":this.validRateList
      },
      issueNumList:[
        {"code": "CODE_ISSUE_NUM","leftSide":0,"rightSide":16,"score":1}
      ],
      taskTimeList:[
        {"code": "CODE_TAKE_TIME","leftSide":0,"rightSide":16,"score":1}
      ],
      historyCloseList:[
        {"code": "CODE_HISTORY_CLOSE","leftSide":0,"rightSide":16,"score":1}
      ],
      validRateList:[
        {"code": "CODE_VALID_RATE","leftSide":0,"rightSide":16,"score":1}
      ]

    };
  },
  methods: {
    openModal() {
      this.$refs.rule.open();
      this.getDetail();
    },
    getDetail() {
      // 获取 规则详情
      this.loading = true;
      this.api.getConfigTester().then(res=>{
        this.loading = false;
        if(res.code===0){
          debugger
          this.ruleDetail = res.configTesterVO;
        }
      }).catch(err=>{
        this.loading = false;
      })
    },
    hideModal() {
      this.ruleDetail = Object.assign({}, ruleDetailCache);
    },
    save() {
      this.$refs.rulesForm.validateFields(result => {
        if (result.isvalid) {
          this.$refs.rulesForm.$emit("closeValidate");
          this.loading = true;
          // 保存
          this.api.saveConfigTesterVO({ configTesterVOStr: JSON.stringify(this.ruleDetail) }).then(res=>{
            this.loading = false;
            if(res.code===0){
              this.closeModal();
            }
          }).catch(err=>{
            this.loading = false;
          })
        }
      });
    },
    // 增加规则
    addRule(index){
      if(index==1){
        let obj={"code": "CODE_ISSUE_NUM", "leftSide":"","rightSide":"","score":""};
        this.ruleDetail.issueNumList.push(obj)
      }else if(index==2){
        let obj={"code": "CODE_TAKE_TIME", "leftSide":"","rightSide":"","score":""};
        this.ruleDetail.taskTimeList.push(obj)
      }else if(index==3){
        let obj={"code": "CODE_HISTORY_CLOSE", "leftSide":"","rightSide":"","score":""};
        this.ruleDetail.historyCloseList.push(obj)
      }else{
        let obj={"code": "CODE_VALID_RATE", "leftSide":"","rightSide":"","score":""};
        this.ruleDetail.validRateList.push(obj)
      }
    },
    // 删除规则
    delRule(name,index){
      if(name==1){
        this.ruleDetail.issueNumList.splice(index,1)
      }else if(name==2){
        this.ruleDetail.taskTimeList.splice(index,1)
      }else if(name==3){
        this.ruleDetail.historyCloseList.splice(index,1)
      }else if(name==4){
        this.ruleDetail.validRateList.splice(index,1)
      }
    },
    closeModal() {
      this.$refs.rule.close();
    },
    // 校验是否为整数或小数
    numberValidate(val){
      if (/(^[\-0-9][0-9]*(.[0-9]+)?)$/.test(val)) {
        return {
          validStatus: 'success'
        }
      } else {
        return {
          validStatus: 'error',
          tips: '请输入整数或者小数'
        }
      }
    }
  }
};
</script>
<style lang="scss" scoped>
.rules_text {
  p {
    line-height: 30px;
    font-size: 14px;
  }
  .rule_list {
    color: #c3c3c3;
    font-size: 12px;
    li {
      text-indent: 2em;
      line-height: 30px;
      .circle-icon {
        content: "";
        display: inline-block;
        width: 6px;
        height: 6px;
        border-radius: 50%;
        background-color: #fff;
        margin-right: 8px;
      }
    }
  }
  .tips-blue {
    color: #4addfb;
  }
  .tips-red {
    color: #d03714;
  }
}
.form-contain {
  width: 100% !important;
  .rule{
    display: flex;
    justify-content: space-between;
    .ruleArr{
      width:calc(50% - 10px);
      .iconfont{
        margin-left:5px;
        cursor: pointer;
      }
    }
    .c-row{
      clear: both;
    }
  }
}
</style>
