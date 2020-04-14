<template>
    <div id="pieProgress">
        <div class="chart" v-if="!data.progressType">
            <chart :options="options" class="chart" ref="chart" :autoresize='true' @mousemove='stopAutoShow' @globalout='zRenderGlobalOut'></chart>
        </div>
        <div class="barProgress" v-if="data.progressType">
            <div class="titleText" :style="titleStyle">{{data.titleName}} <span :style="numStyle">{{progressData}}</span></div>
            <div class="colorPogress">
                <div class="realWidth" :style="realWidthStyle"></div>
                <div class="grayWidth"></div>
            </div>
        </div>
    </div>
</template>
<script>
import store from '../../../vuex/store'
import api from '../../../api/api'
import configData from '../configData.js'
import loopTooltips from './loopTooltips.js'
import chart from "vue-echarts"
export default {
    name:'pieProgress',
    data(){
        let self = this
        return{
            loop:null,
            titleStyle:{},
            numStyle:{},
            realWidthStyle:{},
            progressData:0,
            options:{},
        }
    },
    components:{chart},
    props:{
      data:{
        type:Object,
        default(){
          return { }
        }
      },
    },
    computed: {
        
    },
    watch:{
        data:{
            deep:true,
            immediate: true,
            handler(cur){
                this.getSeriesData();
                this.titleStyle = {'textAlign':this.data.titleTextAlign,'fontSize':this.data.titleFontSize+"px",'color':this.data.titleFontColor};
                this.numStyle = {'fontSize':this.data.numberFontSize+"px",'color':this.data.numberFontColor};
                this.realWidthStyle = {'width':this.progressData+'% !important','background':this.data.barBGColor};
            },
        },
    },
    methods:{
        getOptions(progressData){
            let vm = this,data = JSON.parse(JSON.stringify(this.data));
            let options = {
                title : {
                    text: data.titleName,
                    x: data.titleTextAlign,
                    y: 'center',
                    textStyle:{
                        color:data.titleFontColor,
                        fontSize:data.titleFontSize,
                    },
                    subtext:progressData+'%',
                    subtextStyle: {
                        color:data.numberFontColor,
                        fontSize:data.numberFontSize,
                        fontWeight: 'normal'
                    }
                },
                tooltip : {
                    trigger: 'item',
                    formatter: "{a} <br/>{b} : {c} ({d}%)"
                },
                series : [
                    {
                        name: data.titleName,
                        type: 'pie',
                        radius: ['50%', '60%'],
                        labelLine: {
                            show: false
                        },
                        data:[
                            {
                                hoverOffset: 1,
                                value: progressData,
                                itemStyle: {
                                    color: data.barBGColor
                                },
                                label: {
                                    show: false
                                }
                            },
                            {
                                markArea: {
                                    silent: true,
                                },
                                value: 100-progressData,
                                itemStyle: {
                                    color: '#DDDDDD'
                                },
                                label: {
                                    show: false
                                }
                            }
                        ],
                    }
                ]
            };
            vm.$set(vm,'options',options);
        },
        stopAutoShow(){
            // let vm = this;
            // vm.loop.stopAutoShow();
        },
        zRenderGlobalOut(){
            // let vm = this;
            // vm.loop.zRenderGlobalOut();
        },
        getSeriesData(){
            let vm = this;
            let data = JSON.parse(JSON.stringify(this.data));
            let currentDisplayData = store.getters.getDisplayData;
            let pageConfig = JSON.parse(JSON.stringify(store.getters.getPageConfig));
            let exampleData = [];
            // isPublish==1 当前看板已发布  ；dateType==2 当前组件动态数据 
            if(currentDisplayData.isPublish==1&&data.dataConfig.dateType==2){
                vm.getDataByAxios().then(res=>{
                    // 将接口返回数据，处理成 configData中的exampleData数据格式即可
                    exampleData = res;
                    vm.progressData = exampleData;
                    vm.getOptions(exampleData);
                });
            }else{
                exampleData = data.dataConfig.exampleData;
                vm.progressData = exampleData;
                vm.getOptions(exampleData);
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
                    resolve();
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
#pieProgress{
    width: 100%;
    height: 100%;
    position: relative;
    .chart{
        width: 100%;
        height: 100%;
    }
    .barProgress{
        position: absolute;
        width: 100%;
        height: 60px;
        padding: 0 20px;
        top: 50%;
        left: 50%;
        transform: translate(-50% , -50%);
        >div{
            width: 100%;
            &.titleText{
                height: 30px;
                line-height: 30px;
                font-size: 25px;
                >span{
                    font-size: 25px;
                }
            }
            &.colorPogress{
                height: 20px;
                line-height: 20px;
                position: relative;
                width: 100%;
                .realWidth{
                    width: 100%;
                    height: 10px;
                    border-radius: 5px;
                    position: absolute;
                    top: 50%;
                    left: 0;
                    transform: translate(0 , -50%);
                    transition: width .5s;
                    z-index: 2;
                }
                .grayWidth{
                    width: 100%;
                    height: 10px;
                    background: rgba(255,255,255,.3);
                    border-radius: 5px;
                    position: absolute;
                    top: 50%;
                    left: 50%;
                    transform: translate(-50% , -50%);
                    z-index: 1;
                }
            }
        }
    }
}
</style>
<style lang="scss">
#pieProgress{

}
</style>