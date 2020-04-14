<template>
    <div id="dataConfig">
        <c-form ref='dataForm' v-if="showDataConfig">
            <div class="titleSelection" v-for="(item , index) in formData" :key="index">
                <div class="otherConfig" v-if="!item.isGrid" v-for="(itemChild , indexChild) in item.child" :key="indexChild">
                    <c-form-item
                        v-if="itemChild.type=='radioGroup'&&itemChild.relationVal==model[itemChild.relationKey]"
                        :need='itemChild.need'
                        :label="itemChild.label"
                        :label-col="3">
                        <c-radio-group
                            @change='radioGroupChange(itemChild.modeKey)'
                            :name="itemChild.modeKey"
                            v-model="model[itemChild.modeKey]"
                            :rules="itemChild.need?[{type:'required'}]:[]">
                            <c-radio :label="itemRadio.value" v-for="(itemRadio , indexRadio) in itemChild.list" :key="indexRadio">{{itemRadio.label}}</c-radio>
                        </c-radio-group>
                    </c-form-item>
                    <c-form-item
                        v-if="itemChild.type=='textarea'&&itemChild.relationVal==model[itemChild.relationKey]"
                        :need='itemChild.need'
                        :label="itemChild.label"
                        :label-col="3">
                        <c-textarea
                            v-if="model[itemChild.relationKey]=='2'"
                            :name="itemChild.modeKey"
                            min-height='200px'
                            placeholder='请输入...'
                            width='100%'
                            v-model="model[itemChild.modeKey]"
                            :rules="itemChild.need?[{type:'required'}]:[]">
                        </c-textarea>

                        <pre v-if="model[itemChild.relationKey]=='1'">{{model[itemChild.modeKey]}}</pre>

                    </c-form-item>
                    <c-form-item
                        v-if="itemChild.type=='input'&&itemChild.relationVal==model[itemChild.relationKey]"
                        :need='itemChild.need'
                        :label="itemChild.label"
                        :label-col="3">
                        <c-input
                            :name="itemChild.modeKey"
                            v-model="model[itemChild.modeKey]"
                            width='100%'
                            :rules="itemChild.need?[{type:'required'}]:[]">
                        </c-input>
                    </c-form-item>
                    <c-form-item
                        v-if="itemChild.type=='inputNumber'&&itemChild.relationVal==model[itemChild.relationKey]"
                        :need='itemChild.need'
                        :label="itemChild.label"
                        :label-col="3">
                        <c-input-number
                            :name="itemChild.modeKey"
                            v-model="model[itemChild.modeKey]"
                            width='100%'
                            :rules="itemChild.need?[{type:'required'}]:[]">
                        </c-input-number>
                    </c-form-item>
                </div>
                <!-- grid -->
                <div class="gridParam"  v-if="item.isGrid&&item.relationVal==model[item.relationKey]&&!baseConfig.isScrollGrid">
                    <h5 v-if="item.label">{{item.label}}</h5>
                    <div class="gridHeader">
                        <span v-for="(itemGridHeader , indexGridHeader) in item.gridHeader" :key="indexGridHeader">{{itemGridHeader}}</span>
                    </div>
                    <div class="gridBody">
                        <div v-if='baseConfig.needAxis'>
                            <div class="bodyItem" v-for="(itemGridBody , indexGridBody) in item.gridBody" :key="indexGridBody">
                                <span>{{itemGridBody.key}}</span>
                                <c-input
                                    :name="itemGridBody.modeKey"
                                    v-model="model[itemGridBody.modeKey]"
                                    width='58%'>
                                </c-input>
                                <span>{{itemGridBody.remark}}</span>
                            </div>
                        </div>
                        <div class="bodyItem" v-for="(seriesItem , seriesIndex) in baseConfig['serieslist']" :key="'series'+seriesIndex">
                            <span>{{seriesItem.seriesName}}</span>
                            <c-input
                                :name="seriesItem.seriesKey"
                                v-model="seriesItem.seriesKey"
                                width='58%'>
                            </c-input>
                            <span>{{seriesItem.seriesRemark}}</span>
                        </div>
                    </div>
                    
                </div>
            </div>
        </c-form>
        <input type="hidden" v-model="currentItemIndex">
    </div>
</template>
<script>
import store from '../../../../vuex/store'
export default {
    name:'dataConfig',
    props:{
        
    },
    data(){
        return{
            model:{},
            baseConfig:{},
            /**
             * type 使用控件类型
             * modeKey 接口保存绑定字段
             * need 是否必填项
             * relationKey 关联字段 （主要用来与其他字段的值做关联，通过其他的值控制这一项是否在页面上显示）
             * relationVal 关联字段的值 （同上）
             */
            formData:[
                {
                    child:[
                        {
                            label:"数据类型",
                            type:"radioGroup",
                            modeKey:"dateType",
                            need:true,
                            list:[
                                {label:'静态数据',value:"1"},
                                {label:'动态数据',value:"2"},
                            ]
                        },
                        {label:"数据值",type:"textarea",modeKey:"exampleData",need:true,relationKey:"dateType",relationVal:'1'},
                        {label:"接口地址",type:"textarea",modeKey:"url",need:true,relationKey:"dateType",relationVal:'2'},
                        {
                            label:"接口方式",
                            type:"radioGroup",
                            modeKey:"axiosType",
                            need:true,
                            relationKey:"dateType",
                            relationVal:'2',
                            list:[
                                {label:'post',value:"post"},
                                {label:'get',value:"get"},
                            ]
                        },
                        {label:"刷新时间",type:"inputNumber",modeKey:"refreshTime",need:true,relationKey:"dateType",relationVal:'2'},
                    ]
                },
                {
                    label:'数据元素对应字段',
                    relationKey:"dateType",relationVal:'2',
                    isGrid:true,
                    gridHeader:['字段','映射','说明'],
                    gridBody:[
                        {key:"X",type:"input",modeKey:"category",remark:"类目",need:true},
                        {key:"Y",type:"input",modeKey:"value",remark:"值",need:true},
                    ],
                }
            ],
            currentIndex:[],
            showDataConfig:true,
        }
    },
    computed: {
        currentItemIndex(){
            let vm = this;
            vm.currentIndex = store.getters.getCurrentItemIndex;
            return store.getters.getCurrentItemIndex;
        },
        elementData(){
            return store.getters.getElement
        },
    },
    watch:{
        currentIndex(){
            let vm = this;
            let ElementData = vm.elementData,currentData={};
            if(vm.currentIndex.length==1){
                currentData = ElementData[vm.currentIndex[0]];
            }else if(vm.currentIndex.length==2){
                currentData = ElementData[vm.currentIndex[0]].children[vm.currentIndex[1]];
            }
            vm.baseConfig = currentData;
            if(currentData.isGroup||currentData.tabData.indexOf('dataTabArea')==-1){
                vm.$set(vm,'model',{});
                vm.$set(vm,'showDataConfig',false);
            }else{
                vm.$set(vm,'model',currentData.dataConfig);
                vm.$set(vm,'showDataConfig',true);
            }
        },
    },
    methods:{
        radioGroupChange(modeKey){
            
        },
        // 获取参数
        getDataParam(){
            return this.model;
        },
    },
    mounted(){

    },
}
</script>
<style lang="scss" scoped>
#dataConfig{
    width: 100%;
    height: 100%;
    .gridParam{
        padding:0 0 0 20px;
        .gridHeader{
            width: 100%;
            height: 35px;
            line-height: 35px;
            >span{
                float: left;
                &:nth-child(1){
                    width: 15%;
                }
                &:nth-child(2){
                    width: 60%;
                }
                &:nth-child(3){
                    width: 25%;
                }
            }
        }
        .bodyItem{
            width: 100%;
            height: 35px;
            line-height: 35px;
            margin-bottom: 5px;
            >span{
                display: inline-block;
                overflow: hidden;
                text-overflow: ellipsis;
                white-space: nowrap;
                vertical-align: middle;
                &:nth-child(1){
                    width: 15%;
                }
                &:nth-child(3){
                    width: 25%;
                }
            }
        }
    }
}
</style>
<style lang="scss">
#dataConfig{

}
</style>