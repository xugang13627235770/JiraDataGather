<template>
  <div class="chart">
    <chart :options="getOptions(data)" class="chart" ref="chart" :autoresize='true' @mousemove='stopAutoShow' @globalout='zRenderGlobalOut'></chart>
  </div>
</template>
<script type="text/ecmascript-6">
  import store from '../../../vuex/store'
  import configData from '../configData.js'
  import loopTooltips from './loopTooltips.js'
  import chart from "vue-echarts"
  export default{
    data(){
      let self = this
      return{
        loop:null,
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
      'data.basicSwitch':function(nweVal,oldVew){
        if(nweVal){
          this.zRenderGlobalOut();
        }else{
          this.stopAutoShow();
        }
      },
    },
    methods:{
      getOptions(data){
        let vm = this;
        return {
          title: {
            text: data.titleName,
            x:data.titleTextAlign,
            textStyle:{
              color:data.titleFontColor,
              fontSize:data.titleFontSize,
            },
          },
          legend: {
            show: data.legendSwitch,
            x : data.legendPosition,
            y : 'bottom',
            icon:data.legendIcon,
            data: ['示例1','示例2','示例3','示例4'],
            textStyle:{
              fontSize:data.legendFontSize,
              color:data.legendFontColor
            },
          },
          tooltip : {
            trigger: 'axis',
            axisPointer : {            // 坐标轴指示器，坐标轴触发有效
              type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
            }
          },
          xAxis : [
            {
              type : data.basicViewModel?'category':'value',
              name:data.xAxisName,
              nameTextStyle:{
                fontSize:data.axisFontSize,
              },
              data : data.basicViewModel?['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']:'',
              axisTick: {
                alignWithLabel: true,
                inside:true,
                length:3,
              },
              splitLine:{
                show:data.XSplitLine
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
            }
          ],
          yAxis : [
            {
              type : !data.basicViewModel?'category':'value',
              data : !data.basicViewModel?['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']:'',
              name:data.yAxisName,
              nameTextStyle:{
                fontSize:data.axisFontSize,
              },
              splitLine:{
                show:data.YSplitLine
              },
              axisLabel:{
                fontSize:data.axisFontSize,
                color:data.axisFontColor
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
            }
          ],
          series : vm.getSeriesData()
          // [
          //   {
          //     name:'合计',
          //     type:'bar',
          //     stack:data.barStackSwitch?'stack':'',
          //     label: {
          //       normal: {
          //         show: data.numberSwitch,
          //         position: data.basicViewModel?'top':'right',
          //         textStyle: {
          //           color: data.numberFontColor,
          //           fontSize:data.numberFontSize
          //         },
          //         formatter:data.barStackSwitch?vm.sumNum:''
          //       }
          //     },
          //     data:[0,0,0,0,0,0,0]   //思路一：给series集合末尾多加一栏用于展示合计，但是值都是0；缺点：必须根据xAxis的data生成一组为空的数据，且tooltip不能加trigger: 'axis',这个条件，不然会展示合计：0
          //   }
          // ]
        };
      },
      getSeriesData(){
        let vm = this;
        let data = this.data;
        let exampleData = data.dataConfig.exampleData;
        let {stack,barWidth,label} = {
          stack:data.barStackSwitch?'stack':'',
          barWidth:data.basicBarWidth,
          label:{
            normal: {
              show:!data.barStackSwitch&&data.numberSwitch,
              rotate:data.basicViewModel?(data.barStackSwitch?0:90):360,
              textStyle: {
                color: data.numberFontColor,
                fontSize:data.numberFontSize
              },
            },
          }
        };
        let sun = [];
        exampleData.map(item=>{
          Object.assign(item,{stack,barWidth,label});
        });
        // exampleData.push({
        //   name:"合计",type:"bar",data:[0,0,0,0,0,0,0],
        //   stack:data.barStackSwitch?'stack':'',
        //   label: {
        //     normal: {
        //       show: data.numberSwitch,
        //       position: data.basicViewModel?'top':'right',
        //       textStyle: {
        //         color: data.numberFontColor,
        //         fontSize:data.numberFontSize
        //       },
        //       formatter:data.barStackSwitch?vm.sumNum:''
        //     }
        //   },
        // });
        return exampleData;
      },
      // 计算总和
      sumNum(params){
        let series = this.getOptions(this.data).series;
        let data3 =0;
        for(let i=0,l=series.length;i<l;i++){ 
          data3 += series[i].data[params.dataIndex]
        } 
        return data3
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
        if(vm.data.basicSwitch){
          // 自动轮播tootips
          vm.loop = loopTooltips(vm.$refs.chart,vm.getOptions(vm.data),{loopSeries: true});
        }
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
