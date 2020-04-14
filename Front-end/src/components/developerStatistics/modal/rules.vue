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
                    <span class="tips-blue">总基础分</span> + 关闭故事加分值 - 提测延期扣分值 - 提测打回扣分值 - 缺陷回归扣分值 - 缺陷遗留扣分值 - 有效缺陷扣分值 - 缺陷关闭率扣分值 - 线上反馈缺陷扣分值
                  </p>
                  <ul class="rule_list">
                    <li>
                      <span class="circle-icon"></span>关闭故事加分值 =MAX(
                      <span class="tips-blue">故事基础分</span> * 关闭故事点数 /
                      <span class="tips-blue">基线关闭故事点数, 关闭故事最大加分值)</span>
                    </li>
                    <li>
                      <span class="circle-icon"></span>提测延期扣分值 = SUM(实际提测日期-预计提测日期) *
                      <span class="tips-blue">提测延期系数</span> -- 去除有豁免标记状态的故事
                    </li>
                    <li>
                      <span class="circle-icon"></span>提测打回扣分值 = 迭代内故事、改进和任务重新打开次数 *
                      <span class="tips-blue">提测打回系数</span> -- 同一问题的多次重新打开累计计算
                    </li>
                    <li>
                      <span class="circle-icon"></span>缺陷回归扣分值 = 迭代内缺陷重新打开次数 *
                      <span class="tips-blue">缺陷回归系数</span> -- 同一问题的多次重新打开累计计算
                    </li>
                    <li>
                      <span class="circle-icon"></span>缺陷遗留扣分值 = 未关闭的不紧急、较急优先级缺陷数量 *
                      <span class="tips-blue">非红色缺陷系数</span> + 未关闭的紧急、致命、阻碍优先级缺陷数量 *
                      <span class="tips-blue">红色缺陷系数</span>
                    </li>
                    <li>
                      <span class="circle-icon"></span>有效缺陷扣分值 = (迭代内解决结果为“已解决”和“未解决”状态的总缺陷数 - 按
                      <span
                        class="tips-red"
                      >工种缺陷密度</span>*关闭故事点数) *
                      <span class="tips-blue">有效缺陷系数</span>
                    </li>
                    <li>
                      <span class="circle-icon"></span>缺陷关闭率扣分值 = (缺陷关闭数量 / 迭代内所有缺陷数量) &lt;
                      <span class="tips-blue">缺陷关闭率阈值</span> ?
                      <span class="tips-blue">缺陷未关闭系数</span> : 0
                    </li>
                    <li>
                      <span class="circle-icon"></span>线上缺陷扣分值 = 线上反馈缺陷数量 *
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
                            name="deviceno"
                            width="100%"
                            v-model="ruleDetail.totalBaseScore"
                            :rules="[{type:'required'}]"
                          ></c-input-number>
                        </c-form-item>
                      </c-column>
                      <c-column :col="3" class="context">
                        <c-form-item need label="故事基础分" :label-col="7">
                          <c-input-number
                            name="deviceno"
                            width="100%"
                            v-model="ruleDetail.storyBaseScore"
                            :rules="[{type:'required'}]"
                          ></c-input-number>
                        </c-form-item>
                      </c-column>
                      <c-column :col="3" class="context">
                        <c-form-item need label="基线关闭故事点数" :label-col="7">
                          <c-input-number
                            name="deviceno"
                            width="100%"
                            v-model="ruleDetail.closeStory"
                            :rules="[{type:'required'}]"
                          ></c-input-number>
                        </c-form-item>
                      </c-column>
                      <c-column :col="3" class="context">
                        <c-form-item need label="提测延期系数" :label-col="7">
                          <c-input-number
                            name="deviceno"
                            width="100%"
                            v-model="ruleDetail.testDelayRatio"
                            :rules="[{type:'required'}]"
                          ></c-input-number>
                        </c-form-item>
                      </c-column>
                    </c-row>
                    <c-row>
                      <c-column :col="3" class="context">
                        <c-form-item need label="提测打回系数" :label-col="7">
                          <c-input-number
                            name="deviceno"
                            width="100%"
                            v-model="ruleDetail.testBackRatio"
                            :rules="[{type:'required'}]"
                          ></c-input-number>
                        </c-form-item>
                      </c-column>
                      <c-column :col="3" class="context">
                        <c-form-item need label="缺陷回归系数" :label-col="7">
                          <c-input-number
                            name="deviceno"
                            width="100%"
                            v-model="ruleDetail.issueReturnRatio"
                            :rules="[{type:'required'}]"
                          ></c-input-number>
                        </c-form-item>
                      </c-column>
                      <c-column :col="3" class="context">
                        <c-form-item need label="非红色缺陷系数" :label-col="7">
                          <c-input-number
                            name="deviceno"
                            width="100%"
                            v-model="ruleDetail.noredIssueRatio"
                            :rules="[{type:'required'}]"
                          ></c-input-number>
                        </c-form-item>
                      </c-column>
                      <c-column :col="3" class="context">
                        <c-form-item need label="红色缺陷系数" :label-col="7">
                          <c-input-number
                            name="deviceno"
                            width="100%"
                            v-model="ruleDetail.redIssueRatio"
                            :rules="[{type:'required'}]"
                          ></c-input-number>
                        </c-form-item>
                      </c-column>
                    </c-row>
                    <c-row>
                      <c-column :col="3" class="context">
                        <c-form-item need label="有效缺陷系数" :label-col="7">
                          <c-input-number
                            name="deviceno"
                            width="100%"
                            v-model="ruleDetail.validIssueRatio"
                            :rules="[{type:'required'}]"
                          ></c-input-number>
                        </c-form-item>
                      </c-column>
                      <c-column :col="3" class="context">
                        <c-form-item need label="缺陷关闭率阈值" :label-col="7">
                          <c-input-number
                            name="deviceno"
                            width="100%"
                            v-model="ruleDetail.issueCloseValue"
                            :rules="[{type:'required'}]"
                          ></c-input-number>
                        </c-form-item>
                      </c-column>
                      <c-column :col="3" class="context">
                        <c-form-item need label="未关闭缺陷系数" :label-col="7">
                          <c-input-number
                            name="deviceno"
                            width="100%"
                            v-model="ruleDetail.nocloseIssueRatio"
                            :rules="[{type:'required'}]"
                          ></c-input-number>
                        </c-form-item>
                      </c-column>
                      <c-column :col="3" class="context">
                        <c-form-item need label="线上缺陷系数" :label-col="7">
                          <c-input-number
                            name="deviceno"
                            width="100%"
                            v-model="ruleDetail.onlineIssueRatio"
                            :rules="[{type:'required'}]"
                          ></c-input-number>
                        </c-form-item>
                      </c-column>
                    </c-row>

                    <c-row>
                      <c-column :col="3" class="context">
                        <c-form-item need label="关闭故事最大加分" :label-col="7">
                          <c-input-number
                            name="deviceno"
                            width="100%"
                            v-model="ruleDetail.maxScore"
                            :rules="[{type:'required'}]"
                          ></c-input-number>
                        </c-form-item>
                      </c-column>
                      <!-- <c-column :col="3" class="context">
                        <c-form-item need label="迭代目标完成率" :label-col="7">
                          <c-input-number
                            name="deviceno"
                            width="100%"
                            v-model="ruleDetail.baseTargetRate"
                            :rules="[{type:'required'}]"
                          ></c-input-number>
                        </c-form-item>
                      </c-column>
                      <c-column :col="3" class="context">
                        <c-form-item need label="基线缺陷关闭率" :label-col="7">
                          <c-input-number
                            name="deviceno"
                            width="100%"
                            v-model="ruleDetail.baseDefectCloseRate"
                            :rules="[{type:'required'}]"
                          ></c-input-number>
                        </c-form-item>
                      </c-column> -->
                    </c-row>
                  </c-container>
                </div>
              </div>
              <div class="form-box">
                <div class="form-contain">
                  <h4 class="title">工种缺陷密度</h4>
                  <c-container fluid>
                    <c-row>
                      <c-column :col="3" class="context">
                        <c-form-item need label="Java/C#工程师" :label-col="7">
                          <c-input-number
                            name="deviceno"
                            width="100%"
                            v-model="ruleDetail.densityJava"
                            :rules="[{type:'required'}]"
                          ></c-input-number>
                        </c-form-item>
                      </c-column>
                      <c-column :col="3" class="context">
                        <c-form-item need label="前端工程师" :label-col="7">
                          <c-input-number
                            name="deviceno"
                            width="100%"
                            v-model="ruleDetail.densityFront"
                            :rules="[{type:'required'}]"
                          ></c-input-number>
                        </c-form-item>
                      </c-column>
                      <c-column :col="3" class="context">
                        <c-form-item need label="大数据工程师" :label-col="7">
                          <c-input-number
                            name="deviceno"
                            width="100%"
                            v-model="ruleDetail.densityBigdata"
                            :rules="[{type:'required'}]"
                          ></c-input-number>
                        </c-form-item>
                      </c-column>
                      <c-column :col="3" class="context">
                        <c-form-item need label="默认" :label-col="7">
                          <c-input-number
                            name="deviceno"
                            width="100%"
                            v-model="ruleDetail.densityDefault"
                            :rules="[{type:'required'}]"
                          ></c-input-number>
                        </c-form-item>
                      </c-column>
                    </c-row>
                  </c-container>
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
  "storyBaseScore": 0,
  "closeStory": 0,
  "testDelayRatio":0,
  "testBackRatio": 0,
  "issueReturnRatio": 0,
  "noredIssueRatio": 0,
  "redIssueRatio": 0,
  "validIssueRatio": 0,
  "issueCloseValue": 0,
  "nocloseIssueRatio": 0,
  "onlineIssueRatio": 0,
  "maxScore": 0,
  "baseTargetRate": 0,
  "baseDefectCloseRate": 0,
  "densityJava": 0,
  "densityFront": 0,
  "densityBigdata": 0,
  "densityDefault": 0
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
        "storyBaseScore": 0,
        "closeStory": 0,
        "testDelayRatio":0,
        "testBackRatio": 0,
        "issueReturnRatio": 0,
        "noredIssueRatio": 0,
        "redIssueRatio": 0,
        "validIssueRatio": 0,
        "issueCloseValue": 0,
        "nocloseIssueRatio": 0,
        "onlineIssueRatio": 0,
        "maxScore": 0,
        "baseTargetRate": 0,
        "baseDefectCloseRate": 0,
        "densityJava": 0,
        "densityFront": 0,
        "densityBigdata": 0,
        "densityDefault": 0
      }
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
      this.api.getConfigDeveloper().then(res=>{
        this.loading = false;
        if(res.code===0){
          this.ruleDetail = res.configDeveloper;
          this.loading = false;
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
          this.api.saveConfigDeveloper(this.ruleDetail).then(res=>{
            this.loading = false;
            if(res.code===0){
              this.closeModal();
              this.loading = false;
            }
          }).catch(err=>{
            this.loading = false;
          })

        }
      });
    },
    closeModal() {
      this.$refs.rule.close();
    }
  }
};
</script>
<style lang="scss" scoped>
.rules_text {
  p {
    line-height: 30px;
    font-size: 16px;
  }
  .rule_list {
    color: #c3c3c3;
    font-size: 14px;
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
}
</style>
