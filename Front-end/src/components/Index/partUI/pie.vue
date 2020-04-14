<template>
  <div class="chart">
    <chart :options="options" class="chart" ref="chart" :autoresize='true' @mousemove='stopAutoShow' @globalout='zRenderGlobalOut'></chart>
  </div>
</template>
<script type="text/ecmascript-6">
  import store from '../../../vuex/store'
  import api from '../../../api/api'
  import configData from '../configData.js'
  import loopTooltips from './loopTooltips.js'
  import chart from "vue-echarts"
  export default{
    data(){
      let self = this
      return{
        loop:null,
        timeOutFlag:null,
        options:{},
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
      currentMode(){
        return store.getters.getCurrentMode;
      },
    },
    watch:{
      data:{
        deep:true,
        immediate: true,
        handler(cur){
          // if(this.loop){
          //   this.stopAutoShow();
          // }
          this.getSeriesData();
        },
      },
      'data.basicSwitch':function(nweVal,oldVew){
        if(nweVal){
          this.zRenderGlobalOut();
        }else{
          this.stopAutoShow();
        }
      }
    },
    methods:{
      getOptions(seriesData,legendData){
        let vm = this,data = JSON.parse(JSON.stringify(this.data));
        let options = {
          title : {
            text: data.titleName,
            x:data.titleTextAlign,
            textStyle:{
              color:data.titleFontColor,
              fontSize:data.titleFontSize,
            },
          },
          tooltip : {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
          },
          legend: {
            show: data.legendSwitch,
            x : data.legendPosition,
            y : 'bottom',
            x : data.legendPosition,
            data: legendData?legendData:['示例1','示例2','示例3','示例4'],
            textStyle:{
              fontSize:data.legendFontSize,
              color:data.legendFontColor
            },
          },
          series : [
            {
              name: '示例',
              type: 'pie',
              radius : !data.basicShape?'70%':data.basicShape=='annular'?['50%', '70%']:data.basicShape=='radius'?'70%':data.basicShape,
              roseType:data.basicShape=='radius'?'radius':'',
              center: ['50%', '50%'],
              data:seriesData,
              label:{
                show:true,
                fontSize:data.numberFontSize,
                color:data.numberFontColor
              },
              itemStyle: {
                emphasis: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
              }
            }
          ]
        };
        vm.$set(vm,'options',options);
        vm.$nextTick(()=>{
          // 自动轮播tootips
          if(vm.data.basicSwitch&&!vm.loop){
            vm.loop = loopTooltips(vm.$refs.chart,vm.options,{loopSeries: true});
          }
        })
      },
      getSeriesData(){
        let vm = this;
        let data = JSON.parse(JSON.stringify(this.data));
        let currentDisplayData = store.getters.getDisplayData;
        let pageConfig = JSON.parse(JSON.stringify(store.getters.getPageConfig));
        // isPublish==1 当前看板已发布  ；dateType==2 当前组件动态数据   currentMode==3 预览模式
        if(currentDisplayData.isPublish==1&&data.dataConfig.dateType==2&&vm.currentMode==3){
          vm.getDataByAxios().then(res=>{
            // 将接口返回数据，处理成 configData中的exampleData数据格式即可
            let result = vm.handlerData(res);
            vm.getOptions(result.exampleData,result.legendData);
          });
        }else{
          vm.getOptions(data.dataConfig.exampleData);
        }
      },
      // 数据处理
      handlerData(result){
        let vm= this;
        let exampleData = [],legendData=[];
        let data = JSON.parse(JSON.stringify(vm.data));
        let serieslist = data.serieslist;
        serieslist.map(item=>{
          let seriesObj = {
            name:item.seriesName,
            value:result[item.seriesKey],
            itemStyle:{color:item.seriesColor},
          };
          exampleData.push(seriesObj);
          legendData.push(item.seriesName);
        })
        return {exampleData,legendData};
      },
      // 动态获取数据
      getDataByAxios(){
        let vm = this;
        return new Promise((resolve,reject)=>{
          let dataConfig = JSON.parse(JSON.stringify(this.data)).dataConfig;
          api.getDataByAxios(dataConfig.url,{},dataConfig.axiosType).then(res=>{
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
      stopAutoShow(){
        let vm = this;
        if(!vm.loop){
          vm.loop = loopTooltips(vm.$refs.chart,vm.getOptions(vm.data),{loopSeries: true});
        }
        vm.loop.stopAutoShow();
      },
      zRenderGlobalOut(){
        let vm = this;
        if(!vm.loop){
          vm.loop = loopTooltips(vm.$refs.chart,vm.getOptions(vm.data),{loopSeries: true});
        }
        vm.loop.zRenderGlobalOut();
      },
    },
    mounted(){
      let vm = this;
      vm.$nextTick(()=>{
        
      });
    }
  }
</script>
<style scoped type="text/css">
  .chart{
    width: 100%;
    height: 100%;
  }
</style>
