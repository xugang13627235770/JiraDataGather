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
                  <ul class="rule_list">
                    <li>
                      <span class="circle-icon"></span>迭代目标完成率：指在统计计划故事点数、关闭故事点数的数据。关闭故事点数指在迭代内故事状态为“关闭”的故事点数之和。
                      <p class="tips-remark">指标 = 关闭新增故事点数 / 本迭代计划故事点数 * <span class="tips-blue">新增故事关闭权重</span> + 关闭历史遗留故事点数 / 历史遗留故事点数 * <span class="tips-blue">历史遗留故事关闭权重</span>。</p>
                      <p class="tips-remark">得分 = <span class="tips-blue">迭代目标完成率基础分</span> * 迭代目标完成率 / <span class="tips-blue">迭代目标完成率基线值</span>。</p>
                    </li>
                    <li>
                      <span class="circle-icon"></span>人均关闭故事点数：指参与迭代人员的平均关闭故事点数。参与迭代人员指有故事、改进、任务、Epic事项的人。
                      <p class="tips-remark">指标 = 关闭故事点数之和 / 参与迭代的人数。</p>
                      <p class="tips-remark">得分 = <span class="tips-blue">人均关闭故事点数基础分</span> * 人均关闭故事点数 / <span class="tips-blue">人均关闭故事点数基线值</span>。</p>
                    </li>
                    <li>
                      <span class="circle-icon"></span>平均故事关闭时长：指统计故事点数平均处理时长。
                      <p class="tips-remark">指标 = SUM (故事关闭时间 - 故事开始处理时间) / 关闭故事点数之和。</p>
                      <p class="tips-remark">得分 = FIND (平均故事关闭时长,<span class="tips-red">平均故事关闭时长得分区间</span>)。</p>
                    </li>
                    <li>
                      <span class="circle-icon"></span>平均故事延期时长：指统计故事提测延期。
                      <p class="tips-remark">指标 = SUM(实际提测日期 - 预计提测日期) / 延期故事点数之和。</p>
                      <p class="tips-remark">得分 = FIND(平均故事延期时长,<span class="tips-red">平均故事延期时长得分区间</span>)。</p>
                    </li>
                    <li>
                      <span class="circle-icon"></span>重新打开故事次数：指统计故事“重新打开“的次数，即故事提测打回的次数。
                      <p class="tips-remark">指标 = 故事重新打开次数。</p>
                      <p class="tips-remark">得分 = FIND(故事重新打开次数,<span class="tips-red">故事重新打开次数得分区间</span>)。</p>
                    </li>
                    <li>
                      <span class="circle-icon"></span>迭代遗留故事占比：指迭代关闭时故事没有关闭故事点数的占比。
                      <p class="tips-remark">指标 = (总故事点数 - 完成故事点数) / 总故事点数。</p>
                      <p class="tips-remark">得分 = FIND(迭代遗留故事占比,<span class="tips-red">迭代遗留故事占比得分区间</span>)。</p>
                    </li>
                    <li>
                      <span class="circle-icon"></span>逾期完成故事点数：指故事关闭日期晚于预计关闭日期的故事点数。
                      <p class="tips-remark">指标 = 逾期完成故事点数。</p>
                      <p class="tips-remark">得分 = FIND(逾期完成故事点数,<span class="tips-red">逾期完成故事点数得分区间</span>)。</p>
                    </li>
                    <li>
                      <span class="circle-icon"></span>缺陷关闭率：指迭代内关闭缺陷的比率。
                      <p class="tips-remark">指标 = 关闭缺陷数 / 当前迭代的总缺陷数。</p>
                      <p class="tips-remark">得分 = <span class="tips-blue">缺陷关闭率基础分</span> * 缺陷关闭率 / <span class="tips-blue">迭代缺陷关闭率基线值</span>。</p>
                    </li>
                    <li>
                      <span class="circle-icon"></span>缺陷密度：指故事点数平均产生的缺陷数。
                      <p class="tips-remark">指标 = 当前迭代的总缺陷数 / 关闭故事点数。</p>
                      <p class="tips-remark">得分 = <span class="tips-blue">缺陷密度基础分</span> * <span class="tips-blue">缺陷密度基线值</span> / 缺陷密度;当前迭代的总缺陷数为0，得<span class="tips-blue">缺陷密度基础分</span>。</p>
                    </li>
                    <li>
                      <span class="circle-icon"></span>遗留缺陷比例：指未关闭的缺陷，即状态不是“关闭”的缺陷。
                      <p class="tips-remark">指标 = 未关闭缺陷个数 / 当前迭代的总缺陷数。</p>
                      <p class="tips-remark">得分 = FIND(遗留缺陷比例,<span class="tips-red">遗留缺陷比例得分区间</span>)。</p>
                    </li>
                    <li>
                      <span class="circle-icon"></span>线上缺陷数量。
                      <p class="tips-remark">指标 = 本迭代内线上反馈的缺陷数。</p>
                      <p class="tips-remark">得分 = FIND(线上缺陷数量,<span class="tips-red">线上缺陷数量得分区间</span>)。</p>
                    </li>
                    <li>
                      <span class="circle-icon"></span>线上缺陷平均修复时长。
                      <p class="tips-remark">指标 = SUM(线上缺陷关闭时间 - 创建时间) / 线上缺陷关闭个数。</p>
                      <p class="tips-remark">得分 = FIND(线上缺陷平均修复时长,<span class="tips-red">线上缺陷平均修复时长得分区间</span>)。</p>
                    </li>
                    <li>
                      <span class="circle-icon"></span>迭代期间新增故事个数。
                      <p class="tips-remark">指标 = 迭代期间新增的故事个数。</p>
                      <p class="tips-remark">得分 = 0 - 迭代期间新增的故事个数 * <span class="tips-blue">迭代新增故事扣分基数</span>。</p>
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
                      <c-column :col="4" class="context">
                        <c-form-item need label="新增故事关闭权重" :label-col="7">
                          <c-input-number
                            width="100%"
                            v-model="ruleDetail.newStoryCloseWeight"
                            :rules="[{type:'required'}]"
                          ></c-input-number>
                        </c-form-item>
                      </c-column>
                      <c-column :col="4" class="context">
                        <c-form-item need label="历史遗留故事关闭权重" :label-col="7">
                          <c-input-number
                            width="100%"
                            v-model="ruleDetail.historyStoryCloseWeight"
                            :rules="[{type:'required'}]"
                          ></c-input-number>
                        </c-form-item>
                      </c-column>
                      <c-column :col="4" class="context">
                        <c-form-item need label="迭代目标完成率基础分" :label-col="7">
                          <c-input-number
                            width="100%"
                            v-model="ruleDetail.sprintCompletionRateBaseScore"
                            :rules="[{type:'required'}]"
                          ></c-input-number>
                        </c-form-item>
                      </c-column>
                    </c-row>
                    <c-row>
                      <c-column :col="4" class="context">
                        <c-form-item need label="迭代目标完成率基线值" :label-col="7">
                          <c-input-number
                            width="100%"
                            v-model="ruleDetail.sprintCompletionRateBaseLine"
                            :rules="[{type:'required'}]"
                          ></c-input-number>
                        </c-form-item>
                      </c-column>
                      <c-column :col="4" class="context">
                        <c-form-item need label="人均关闭故事点数基础分" :label-col="7">
                          <c-input-number
                            width="100%"
                            v-model="ruleDetail.personClosePointBaseScore"
                            :rules="[{type:'required'}]"
                          ></c-input-number>
                        </c-form-item>
                      </c-column>
                      <c-column :col="4" class="context">
                        <c-form-item need label="人均关闭故事点数基线值" :label-col="7">
                          <c-input-number
                            width="100%"
                            v-model="ruleDetail.personClosePointBaseLine"
                            :rules="[{type:'required'}]"
                          ></c-input-number>
                        </c-form-item>
                      </c-column>
                    </c-row>
                    <c-row>
                      <c-column :col="4" class="context">
                        <c-form-item need label="缺陷关闭率基础分" :label-col="7">
                          <c-input-number
                            width="100%"
                            v-model="ruleDetail.defectCloseRateBaseScore"
                            :rules="[{type:'required'}]"
                          ></c-input-number>
                        </c-form-item>
                      </c-column>
                      <c-column :col="4" class="context">
                        <c-form-item need label="迭代缺陷关闭率基线值" :label-col="7">
                          <c-input-number
                            width="100%"
                            v-model="ruleDetail.defectCloseRateBaseLine"
                            :rules="[{type:'required'}]"
                          ></c-input-number>
                        </c-form-item>
                      </c-column>
                      <c-column :col="4" class="context">
                        <c-form-item need label="缺陷密度基础分" :label-col="7">
                          <c-input-number
                            width="100%"
                            v-model="ruleDetail.defectDensityBaseScore"
                            :rules="[{type:'required'}]"
                          ></c-input-number>
                        </c-form-item>
                      </c-column>
                    </c-row>
                    <c-row>
                      <c-column :col="4" class="context">
                        <c-form-item need label="缺陷密度基线值" :label-col="7">
                          <c-input-number
                            width="100%"
                            v-model="ruleDetail.defectDensityBaseLine"
                            :rules="[{type:'required'}]"
                          ></c-input-number>
                        </c-form-item>
                      </c-column>
                      <c-column :col="4" class="context">
                        <c-form-item need label="迭代新增故事扣分基数" :label-col="7">
                          <c-input-number
                            width="100%"
                            v-model="ruleDetail.sprintAddStoryDeductionBase"
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
                      <h4 class="title">平均故事关闭时长得分区间<i class="iconfont icon-jia1" @click="addRule('1')"></i></h4>
                      <c-container fluid>
                        <c-row v-for="(item,index) in ruleDetail.avgStoryCloseTimeRange" :key="index">
                          <c-column :col="3" class="context">
                            <c-form-item need label="≥" :label-col="4">
                              <c-input
                                width="100%"
                                v-model="item.leftSide"
                                :rules="[{type:'required'},{type: 'number' }]"
                              ></c-input>
                            </c-form-item>
                          </c-column>
                          <c-column :col="4" class="context">
                            <c-form-item :need="(index!=ruleDetail.avgStoryCloseTimeRange.length-1)?true:false" label="<" :label-col="4">
                              <c-input
                                width="100%"
                                v-model="item.rightSide"
                                :rules="(index!=ruleDetail.avgStoryCloseTimeRange.length-1)?[{type:'required'},{type: 'number' }]:[]"
                              ></c-input>
                            </c-form-item>
                          </c-column>
                          <c-column :col="5" class="context">
                            <c-form-item  label="加分值" :label-col="5">
                              <c-input
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
                      <h4 class="title">平均故事延期时长得分区间<i class="iconfont icon-jia1" @click="addRule('2')"></i></h4>
                      <c-container fluid>
                        <c-row v-for="(item,index) in ruleDetail.avgStoryDelayTimeRange" :key="index">
                          <c-column :col="3" class="context">
                            <c-form-item need label="≥" :label-col="4">
                              <c-input
                                width="100%"
                                v-model="item.leftSide"
                                :rules="[{type:'required'},{type: 'number' }]"
                              ></c-input>
                            </c-form-item>
                          </c-column>
                          <c-column :col="4" class="context">
                            <c-form-item :need="(index!=ruleDetail.avgStoryDelayTimeRange.length-1)?true:false" label="<" :label-col="4">
                              <c-input
                                width="100%"
                                v-model="item.rightSide"
                                :rules="(index!=ruleDetail.avgStoryDelayTimeRange.length-1)?[{type:'required'},{type: 'number' }]:[]"
                              ></c-input>
                            </c-form-item>
                          </c-column>
                          <c-column :col="5" class="context">
                            <c-form-item  label="加分值" :label-col="5">
                              <c-input
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
                      <h4 class="title">故事重新打开次数得分区间<i class="iconfont icon-jia1" @click="addRule('3')"></i></h4>
                      <c-container fluid>
                        <c-row v-for="(item,index) in ruleDetail.storyReopenNumRange" :key="index">
                          <c-column :col="3" class="context">
                            <c-form-item need label="≥" :label-col="4">
                              <c-input
                                width="100%"
                                v-model="item.leftSide"
                                :rules="[{type:'required'},{type: 'number' }]"
                              ></c-input>
                            </c-form-item>
                          </c-column>
                          <c-column :col="4" class="context">
                            <c-form-item :need="(index!=ruleDetail.storyReopenNumRange.length-1)?true:false" label="<" :label-col="4">
                              <c-input
                                width="100%"
                                v-model="item.rightSide"
                                :rules="(index!=ruleDetail.storyReopenNumRange.length-1)?[{type:'required'},{type: 'number' }]:[]"
                              ></c-input>
                            </c-form-item>
                          </c-column>
                          <c-column :col="5" class="context">
                            <c-form-item  label="加分值" :label-col="5">
                              <c-input
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
                      <h4 class="title">迭代遗留故事占比得分区间<i class="iconfont icon-jia1" @click="addRule('4')"></i></h4>
                      <c-container fluid>
                        <c-row v-for="(item,index) in ruleDetail.leftOverStoryRateRange" :key="index">
                          <c-column :col="3" class="context">
                            <c-form-item need label="≥" :label-col="4">
                              <c-input
                                width="100%"
                                v-model="item.leftSide"
                                :rules="[{type:'required'},{type: 'number' }]"
                              ></c-input>
                            </c-form-item>
                          </c-column>
                          <c-column :col="4" class="context">
                            <c-form-item :need="(index!=ruleDetail.leftOverStoryRateRange.length-1)?true:false" label="<" :label-col="4">
                              <c-input
                                width="100%"
                                v-model="item.rightSide"
                                :rules="(index!=ruleDetail.leftOverStoryRateRange.length-1)?[{type:'required'},{type: 'number' }]:[]"
                              ></c-input>
                            </c-form-item>
                          </c-column>
                          <c-column :col="5" class="context">
                            <c-form-item  label="加分值" :label-col="5">
                              <c-input
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
                  <div class="rule">
                    <div class="ruleArr">
                      <h4 class="title">逾期完成故事点数得分区间<i class="iconfont icon-jia1" @click="addRule('5')"></i></h4>
                      <c-container fluid>
                        <c-row v-for="(item,index) in ruleDetail.overdueCompletePointRange" :key="index">
                          <c-column :col="3" class="context">
                            <c-form-item need label="≥" :label-col="4">
                              <c-input
                                width="100%"
                                v-model="item.leftSide"
                                :rules="[{type:'required'},{type: 'number' }]"
                              ></c-input>
                            </c-form-item>
                          </c-column>
                          <c-column :col="4" class="context">
                            <c-form-item :need="(index!=ruleDetail.overdueCompletePointRange.length-1)?true:false" label="<" :label-col="4">
                              <c-input
                                width="100%"
                                v-model="item.rightSide"
                                :rules="(index!=ruleDetail.overdueCompletePointRange.length-1)?[{type:'required'},{type: 'number' }]:[]"
                              ></c-input>
                            </c-form-item>
                          </c-column>
                          <c-column :col="5" class="context">
                            <c-form-item  label="加分值" :label-col="5">
                              <c-input
                                width="calc(100% - 20px)"
                                v-model="item.score"
                                :custom-validate="numberValidate"
                                :rules="[{type:'required'}]"
                              ></c-input>
                              <i class="iconfont icon-shanchu" style="margin-left:0" @click="delRule('5',index)"></i>
                            </c-form-item>
                          </c-column>

                        </c-row>
                      </c-container>
                    </div>
                    <div class="ruleArr">
                      <h4 class="title">遗留缺陷比例得分区间<i class="iconfont icon-jia1" @click="addRule('6')"></i></h4>
                      <c-container fluid>
                        <c-row v-for="(item,index) in ruleDetail.leftOverDefectRateRange" :key="index">
                          <c-column :col="3" class="context">
                            <c-form-item need label="≥" :label-col="4">
                              <c-input
                                width="100%"
                                v-model="item.leftSide"
                                :rules="[{type:'required'},{type: 'number' }]"
                              ></c-input>
                            </c-form-item>
                          </c-column>
                          <c-column :col="4" class="context">
                            <c-form-item :need="(index!=ruleDetail.leftOverDefectRateRange.length-1)?true:false" label="<" :label-col="4">
                              <c-input
                                width="100%"
                                v-model="item.rightSide"
                                :rules="(index!=ruleDetail.leftOverDefectRateRange.length-1)?[{type:'required'},{type: 'number' }]:[]"
                              ></c-input>
                            </c-form-item>
                          </c-column>
                          <c-column :col="5" class="context">
                            <c-form-item  label="加分值" :label-col="5">
                              <c-input
                                width="calc(100% - 20px)"
                                v-model="item.score"
                                :custom-validate="numberValidate"
                                :rules="[{type:'required'}]"
                              ></c-input>
                              <i class="iconfont icon-shanchu" style="margin-left:0" @click="delRule('6',index)"></i>
                            </c-form-item>
                          </c-column>
                        </c-row>
                      </c-container>
                    </div>
                  </div>
                  <div class="rule">
                    <div class="ruleArr">
                      <h4 class="title">线上缺陷数量得分区间<i class="iconfont icon-jia1" @click="addRule('7')"></i></h4>
                      <c-container fluid>
                        <c-row v-for="(item,index) in ruleDetail.onlineDefectNumRange" :key="index">
                          <c-column :col="3" class="context">
                            <c-form-item need label="≥" :label-col="4">
                              <c-input
                                width="100%"
                                v-model="item.leftSide"
                                :rules="[{type:'required'},{type: 'number' }]"
                              ></c-input>
                            </c-form-item>
                          </c-column>
                          <c-column :col="4" class="context">
                            <c-form-item :need="(index!=ruleDetail.onlineDefectNumRange.length-1)?true:false" label="<" :label-col="4">
                              <c-input
                                width="100%"
                                v-model="item.rightSide"
                                :rules="(index!=ruleDetail.onlineDefectNumRange.length-1)?[{type:'required'},{type: 'number' }]:[]"
                              ></c-input>
                            </c-form-item>
                          </c-column>
                          <c-column :col="5" class="context">
                            <c-form-item  label="加分值" :label-col="5">
                              <c-input
                                width="calc(100% - 20px)"
                                v-model="item.score"
                                :custom-validate="numberValidate"
                                :rules="[{type:'required'}]"
                              ></c-input>
                              <i class="iconfont icon-shanchu" style="margin-left:0" @click="delRule('7',index)"></i>
                            </c-form-item>
                          </c-column>

                        </c-row>
                      </c-container>
                    </div>
                    <div class="ruleArr">
                      <h4 class="title">线上缺陷平均修复时长得分区间<i class="iconfont icon-jia1" @click="addRule('8')"></i></h4>
                      <c-container fluid>
                        <c-row v-for="(item,index) in ruleDetail.onlineDefectAvgRepairTimeRange" :key="index">
                          <c-column :col="3" class="context">
                            <c-form-item need label="≥" :label-col="4">
                              <c-input
                                width="100%"
                                v-model="item.leftSide"
                                :rules="[{type:'required'},{type: 'number' }]"
                              ></c-input>
                            </c-form-item>
                          </c-column>
                          <c-column :col="4" class="context">
                            <c-form-item :need="(index!=ruleDetail.onlineDefectAvgRepairTimeRange.length-1)?true:false" label="<" :label-col="4">
                              <c-input
                                width="100%"
                                v-model="item.rightSide"
                                :rules="(index!=ruleDetail.onlineDefectAvgRepairTimeRange.length-1)?[{type:'required'},{type: 'number' }]:[]"
                              ></c-input>
                            </c-form-item>
                          </c-column>
                          <c-column :col="5" class="context">
                            <c-form-item  label="加分值" :label-col="5">
                              <c-input
                                width="calc(100% - 20px)"
                                v-model="item.score"
                                :custom-validate="numberValidate"
                                :rules="[{type:'required'}]"
                              ></c-input>
                              <i class="iconfont icon-shanchu" style="margin-left:0" @click="delRule('8',index)"></i>
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
  "newStoryCloseWeight": 0,
  "historyStoryCloseWeight": 0,
  "sprintCompletionRateBaseScore": 0,
  "sprintCompletionRateBaseLine":0,
  "personClosePointBaseScore": 0,
  "personClosePointBaseLine": 0,
  "defectCloseRateBaseScore": 0,
  "defectCloseRateBaseLine": 0,
  "defectDensityBaseScore": 0,
  "defectDensityBaseLine": 0,
  "sprintAddStoryDeductionBase": 0,
  "avgStoryCloseTimeRange": [],
  "avgStoryDelayTimeRange": [],
  "storyReopenNumRange": [],
  "leftOverStoryRateRange": [],
  "overdueCompletePointRange": [],
  "leftOverDefectRateRange": [],
  "onlineDefectNumRange": [],
  "onlineDefectAvgRepairTimeRange": []
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
        "newStoryCloseWeight": 0,
        "historyStoryCloseWeight": 0,
        "sprintCompletionRateBaseScore": 0,
        "sprintCompletionRateBaseLine":0,
        "personClosePointBaseScore": 0,
        "personClosePointBaseLine": 0,
        "defectCloseRateBaseScore": 0,
        "defectCloseRateBaseLine": 0,
        "defectDensityBaseScore": 0,
        "defectDensityBaseLine": 0,
        "sprintAddStoryDeductionBase": 0,
        "avgStoryCloseTimeRange": [],
        "avgStoryDelayTimeRange": [],
        "storyReopenNumRange": [],
        "leftOverStoryRateRange": [],
        "overdueCompletePointRange": [],
        "leftOverDefectRateRange": [],
        "onlineDefectNumRange": [],
        "onlineDefectAvgRepairTimeRange": []
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
      this.api.getConfigProject().then(res=>{
        this.loading = false;
        if(res.code===0){
          this.ruleDetail = res.projectRuleInfo;
          for(let key in this.ruleDetail) {
            if (!this.ruleDetail[key] && key != 'pkid') {
              this.ruleDetail[key] = 0
            }
          }
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
          this.api.saveConfigProject(this.ruleDetail).then(res=>{
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
    },
    // 增加规则
    addRule(index){
      if(index==1){
        let obj={"code": "AVG_STORY_CLOSE_TIME", "leftSide":"","rightSide":"","score":""};
        this.ruleDetail.avgStoryCloseTimeRange.push(obj)
      }else if(index==2){
        let obj={"code": "AVG_STORY_DELAY_TIME", "leftSide":"","rightSide":"","score":""};
        this.ruleDetail.avgStoryDelayTimeRange.push(obj)
      }else if(index==3){
        let obj={"code": "STORY_REOPEN_NUM", "leftSide":"","rightSide":"","score":""};
        this.ruleDetail.storyReopenNumRange.push(obj)
      }else if(index==4){
        let obj={"code": "LEFT_OVER_STORY_RATE", "leftSide":"","rightSide":"","score":""};
        this.ruleDetail.leftOverStoryRateRange.push(obj)
      }else if(index==5){
        let obj={"code": "OVERDUE_COMPLETE_POINT", "leftSide":"","rightSide":"","score":""};
        this.ruleDetail.overdueCompletePointRange.push(obj)
      }else if(index==6){
        let obj={"code": "LEFT_OVER_DEFECT_RATE", "leftSide":"","rightSide":"","score":""};
        this.ruleDetail.leftOverDefectRateRange.push(obj)
      }else if(index==7){
        let obj={"code": "ONLINE_DEFECT_NUM", "leftSide":"","rightSide":"","score":""};
        this.ruleDetail.onlineDefectNumRange.push(obj)
      }else if(index==8){
        let obj={"code": "ONLINE_DEFECT_AVG_REPAIR_TIME", "leftSide":"","rightSide":"","score":""};
        this.ruleDetail.onlineDefectAvgRepairTimeRange.push(obj)
      }
    },
    // 删除规则
    delRule(name,index){
      if(name==1){
        this.ruleDetail.avgStoryCloseTimeRange.splice(index,1)
      }else if(name==2){
        this.ruleDetail.avgStoryDelayTimeRange.splice(index,1)
      }else if(name==3){
        this.ruleDetail.storyReopenNumRange.splice(index,1)
      }else if(name==4){
        this.ruleDetail.leftOverStoryRateRange.splice(index,1)
      }else if(name==5){
        this.ruleDetail.overdueCompletePointRange.splice(index,1)
      }else if(name==6){
        this.ruleDetail.leftOverDefectRateRange.splice(index,1)
      }else if(name==7){
        this.ruleDetail.onlineDefectNumRange.splice(index,1)
      }else if(name==8){
        this.ruleDetail.onlineDefectAvgRepairTimeRange.splice(index,1)
      }
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
  .tips-remark {
    font-size: 14px !important;
    text-indent: 4em;
    margin:0px !important;
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
