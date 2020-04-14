<template>
    <div id="numberAnimate">
        <div class="textRemark" :style="titleStyle" :class="{'floatLeft':data.titleArrayType=='left'}" v-if="data.titleArrayType!='bottom'">{{data.titleName}}</div>
        <div class="numberArea" :class="{'floatLeft':data.titleArrayType=='left'}">
            <span class="numAreaPonit pointSpan" :style="preStyle">{{data.prefixName}}</span>
            <div class="iCountUp" :style="numberStyle" v-if="update">
                <ICountUp :delay="delay" :endVal="endVal" :options="options" ref="ICountUp"/>
            </div>
            <span class="SymbolText pointSpan" :style="nextStyle">{{data.SuffixName}}</span>
        </div>
        <div class="textRemark" :style="titleStyle" v-if="data.titleArrayType=='bottom'">{{data.titleName}}</div>
    </div>
</template>
<script>
import store from '../../../vuex/store'
import api from '../../../api/api'
import configData from '../configData.js'
import ICountUp from 'vue-countup-v2';
export default {
    name:'numberAnimate',
    components:{ICountUp},
    props: {
        data:{
            type:Object,
            default(){
                return { }
            }
        },
    },
    data(){
        let self = this;
        return{
            titleStyle:{},
            preStyle:{},
            numberStyle:{},
            nextStyle:{},
            delay: 200,
            endVal: 0,
            options: {
                useEasing: true,
                useGrouping: true,
                separator: ',',
                decimal: '',
                prefix: '',
                suffix: ''
            },
            update:true,
            aa:{
                areaCode: "120100",
                buFullName: "天津市公安交通管理局11",
                buLevel: 10,
                deviceTotal: 277,
                happenFaultNumber: 610,
                sortHierArchYcode: "0001",
                date:"2019-11-20"
            },
        }
    },
    computed: {
      currentMode(){
        return store.getters.getCurrentMode;
      },
    },
    watch:{
        data:{
            deep:true,
            immediate: true,
            handler(cur){
                this.getSeriesData();
                Object.assign(this.data,cur);
                this.titleStyle= {color:this.data.titleFontColor,fontSize:this.data.titleFontSize+"px",fontWeight:this.data.titlefontWeight,letterSpacing:this.data.titleLetterSpacing+"px"};
                this.preStyle = {color:this.data.prefixFontColor,fontSize:this.data.prefixFontSize+"px",fontWeight:this.data.prefixfontWeight,letterSpacing:this.data.prefixLetterSpacing+"px"};
                this.nextStyle = {color:this.data.SuffixFontColor,fontSize:this.data.SuffixFontSize+"px",fontWeight:this.data.SuffixfontWeight,letterSpacing:this.data.SuffixLetterSpacing+"px"};
                this.numberStyle = {color:this.data.loopNumFontColor,fontSize:this.data.loopNumFontSize+"px",fontWeight:this.data.loopNumfontWeight};
            },
        },
        'data.loopNumSeparator':function(newVal,oldVal){
            let vm = this;
            this.update = false;
            this.$set(this.options,'separator',newVal?',':'');
            setTimeout(()=>{
                vm.update = true;
            },10);
        }
    },
    methods:{
        getSeriesData(){
            let vm = this;
            let data = JSON.parse(JSON.stringify(this.data));
            let currentDisplayData = store.getters.getDisplayData;
            let pageConfig = JSON.parse(JSON.stringify(store.getters.getPageConfig));
            // isPublish==1 当前看板已发布  ；dateType==2 当前组件动态数据  currentMode==3 预览模式
            if(currentDisplayData.isPublish==1&&data.dataConfig.dateType==2&&vm.currentMode==3){
                vm.getDataByAxios().then(res=>{
                    // 将接口返回数据，处理成 configData中的exampleData数据格式即可
                    let serieslist = data.serieslist;
                    vm.endVal = res[serieslist[0].seriesKey]?res[serieslist[0].seriesKey]:0;;
                });
            }else{
                vm.endVal = data.dataConfig.exampleData;
            }
        },
        // 动态获取数据
        getDataByAxios(){
            let vm = this;
            return new Promise((resolve,reject)=>{
                let dataConfig = JSON.parse(JSON.stringify(this.data)).dataConfig;
                api.getDataByAxios(dataConfig.url,{},dataConfig.axiosType).then(res=>{
                    // if(vm.timeOutFlag){clearTimeout(vm.timeOutFlag)};
                    // TODO  数据处理...
                    if(res.code==0){
                        resolve(res.result);
                    }else{
                        resolve([]);
                    }
                    vm.timeOutFlag = setTimeout(()=>{
                        vm.getSeriesData();
                    },dataConfig.refreshTime);
                });
            })
        },
    },
    mounted(){
        let vm = this;
        vm.$nextTick(()=>{
            
        })
    },
}
</script>
<style lang="scss" scoped>
#numberAnimate{
    >div.floatLeft{
        float: left !important;
    }
    .textRemark{
        line-height: 30px;
        font-size: 14px;
        padding: 0 3px;
    }
    .numberArea{
        >span.pointSpan{
            &.numAreaPonit {
                font-size: 30px;
                float: left;
            }
            &.SymbolText{
                vertical-align: bottom;
                font-size: 16px;
                display: inline-block;
            }
        }
        .iCountUp{
            display: inline-block;
            color: rgba(255,255,255,1);
            font-size: 25px;
        }
    }
}
</style>
<style lang="scss">
#numberAnimate{

}
</style>