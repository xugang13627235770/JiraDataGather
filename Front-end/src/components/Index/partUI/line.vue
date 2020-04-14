<template>
  <div class="chart">
    <chart :options="options" class="chart" ref="chart" :autoresize='true' @mousemove='stopAutoShow' @globalout='zRenderGlobalOut'></chart>
  </div>
</template>
<script type="text/ecmascript-6">
  import store from '../../../vuex/store'
  import configData from '../configData.js'
  import loopTooltips from './loopTooltips.js'
  import chart from "vue-echarts"
  import api from '../../../api/api'
  export default{
    data(){
      let self = this
      return{
        loop:null,
        timeOutFlag:null,
        options:{},
        aa:[
          {
            areaCode: "120100",
            buFullName: "天津市公安交通管理局11",
            buLevel: 10,
            deviceTotal: 277,
            happenFaultNumber: 610,
            sortHierArchYcode: "0001",
            date:"2019-11-20"
          },
          {
            areaCode: "120100",
            buFullName: "天津市公安交通管理局11",
            buLevel: 20,
            deviceTotal: 277,
            happenFaultNumber: 50,
            sortHierArchYcode: "0001",
            date:"2019-11-21"
          },
          {
            areaCode: "120100",
            buFullName: "天津市公安交通管理局11",
            buLevel: 30,
            deviceTotal: 277,
            happenFaultNumber: 330,
            sortHierArchYcode: "0001",
            date:"2019-11-22"
          },
          {
            areaCode: "120100",
            buFullName: "天津市公安交通管理局11",
            buLevel: 40,
            deviceTotal: 277,
            happenFaultNumber: 10,
            sortHierArchYcode: "0001",
            date:"2019-11-23"
          }
        ],
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
      },
    },
    methods:{
      getOptions(seriesData,axisData,legendData){
        let vm = this,data = JSON.parse(JSON.stringify(this.data));
        let {areaStyle,smooth,symbolSize} = {areaStyle:data.areaStackSwitch?{}:null,smooth:data.lineModel,symbolSize:data.pointSize};
        seriesData.map(item=>{
          Object.assign(item,{areaStyle,smooth,symbolSize});
        });
        let options = {
          title: {
            text: data.titleName,
            x:data.titleTextAlign,
            textStyle:{
              color:data.titleFontColor,
              fontSize:data.titleFontSize,
            },
          },
          tooltip: {
            trigger: 'axis'
          },
          legend: {
            show: data.legendSwitch,
            x : data.legendPosition,
            y : 'bottom',
            data: legendData?legendData:['示例1','示例2','示例3','示例4'],
            icon:data.legendIcon,
            textStyle:{
              fontSize:data.legendFontSize,
              color:data.legendFontColor
            },
          },
          xAxis: {
            type: data.basicViewModel?'category':'value',
            name:data.xAxisName,
            boundaryGap: false,
            data : data.basicViewModel?(axisData?axisData:['周一','周二','周三','周四','周五','周六','周日']):'',
            splitLine:{
              show:data.XSplitLine,
              lineStyle:{
                color:data.SplitColor,
              },
            },
            axisLabel:{
              fontSize:data.axisFontSize,
              color:data.axisFontColor
            },
            axisLine: {
              symbol:['none','path://M5,20,L5,5 L8,8 L5,2 L2,8 L5,5 L5.3,6 L5.3,20 '],
              symbolOffset:-5,
              symbolSize:[25,25],
              lineStyle: {
                color: data.axisFontColor
              }
            },
            axisTick: {
              alignWithLabel: true,
              inside:true,
              length:3,
            },
          },
          yAxis: {
            type : !data.basicViewModel?'category':'value',
            data : !data.basicViewModel?(axisData?axisData:['周一','周二','周三','周四','周五','周六','周日']):'',
            name:data.yAxisName,
            splitLine:{
              show:data.YSplitLine,
              lineStyle:{
                color:data.SplitColor,
              },
            },
            axisLabel:{
              rotate:data.basicViewModel?0:60,
              fontSize:data.axisFontSize,
            },
            axisTick: {
              inside:true,
              length:3,
            },
            axisLine: {
              symbol:['none','path://M5,20,L5,5 L8,8 L5,2 L2,8 L5,5 L5.3,6 L5.3,20 '],
              symbolOffset:-5,
              symbolSize:[25,25],
              lineStyle: {
                color: data.axisFontColor
              }
            },
          },
          series: seriesData
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
        // isPublish==1 当前看板已发布 ; dateType==2 当前组件动态数据   currentMode==3 预览模式
        if(currentDisplayData.isPublish==1&&data.dataConfig.dateType==2&&vm.currentMode==3){
          vm.getDataByAxios().then(res=>{
            // 将接口返回数据，处理成 configData中的exampleData数据格式即可
            let result = vm.handlerData(res);
            vm.getOptions(result.exampleData,result.axisData,result.legendData);
          });
        }else{
          vm.getOptions(data.dataConfig.exampleData);
        }
      },
      // 数据处理
      handlerData(result){
        let vm= this;
        let exampleData = [],axisData=[],legendData=[];
        let data = JSON.parse(JSON.stringify(vm.data));
        let dataConfig = data.dataConfig;
        let serieslist = data.serieslist;
        serieslist.map(item=>{
          axisData=[];
          let seriesObj = {
            name:item.seriesName,
            type:'line',
            stack: '总量',
            data:[],
            itemStyle:{color:item.seriesColor},
          };
          result.map(items=>{
            seriesObj.data.push(items[item.seriesKey]);
            axisData.push(items[dataConfig.category]);
          })
          exampleData.push(seriesObj);
          legendData.push(item.seriesName);
        })
        return {exampleData,axisData,legendData};
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
      stopAutoShow(){
        let vm = this;
        if(!vm.loop){
          vm.loop = loopTooltips(vm.$refs.chart,vm.options,{loopSeries: true});
        }
        vm.loop.stopAutoShow();
      },
      zRenderGlobalOut(){
        let vm = this;
        if(!vm.loop){
          vm.loop = loopTooltips(vm.$refs.chart,vm.options,{loopSeries: true});
        }
        vm.loop.zRenderGlobalOut();
      },
    },
    mounted(){
      let vm = this;
      vm.$nextTick(()=>{
        
      })
    }
  }
</script>
<style scoped type="text/css">
  .chart{
    width: 100%;
    height: 100%;
  }
</style>
