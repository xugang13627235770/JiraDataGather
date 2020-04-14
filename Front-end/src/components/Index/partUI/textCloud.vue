<template>
  <div id="wordCloud" class="wordCloud" :style="{width: '100%', height: '100%'}"></div>
</template>
<script>
  import store from '../../../vuex/store'
  import api from '../../../api/api'
  import 'echarts/dist/echarts.min'
  import 'echarts-wordcloud/dist/echarts-wordcloud.min'
  import configData from '../configData.js'
  import loopTooltips from './loopTooltips.js'
  export default {
    data(){
      return{
        wordCloud:null,
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
    props:{
      data:{
        type:Object,
        default(){
          return {}
        }
      }
    },
    watch:{
      data:{
        deep:true,
        immediate: true,
        handler(cur,old){
          let vm = this;
          vm.$nextTick(()=>{
            // if(vm.loop){
            //   vm.stopAutoShow();
            // }
            if(!vm.wordCloud){
              let wordCloudNode = document.getElementsByClassName('wordCloud');
              vm.wordCloud = vm.$echarts.init(wordCloudNode[wordCloudNode.length-1]);
            }
            vm.drawCloud();
          })
        },
      },
      'data.width':function(){
        this.wordCloud.resize();
      },
      'data.height':function(){
        this.wordCloud.resize();
      },
      'data.basicSwitch':function(nweVal,oldVew){
        if(nweVal){
          this.zRenderGlobalOut();
        }else{
          this.stopAutoShow();
        }
      }
    },
    computed: {
      currentMode(){
        return store.getters.getCurrentMode;
      },
    },
    methods:{
      drawCloud(){
        let vm = this;
        let data = JSON.parse(JSON.stringify(this.data));
        let currentDisplayData = store.getters.getDisplayData;
        let pageConfig = JSON.parse(JSON.stringify(store.getters.getPageConfig));
        // isPublish==1 当前看板已发布  ；dateType==2 当前组件动态数据 currentMode==3 预览模式
        let exampleData = [];
        if(currentDisplayData.isPublish==1&&data.dataConfig.dateType==2&&vm.currentMode==3){
          vm.getDataByAxios().then(res=>{
            // 将接口返回数据，处理成 configData中的exampleData数据格式即可
            let result = vm.handlerData(res);
            exampleData = result.exampleData;
            vm.wordCloud.setOption(vm.getOptions(result.exampleData));
            vm.$nextTick(()=>{
              if(vm.data.basicSwitch&&!vm.loop){
                // 自动轮播tootips
                vm.loop = loopTooltips(vm.wordCloud,vm.getOptions(exampleData),{loopSeries: true});
              }
            })
          });
        }else{
          exampleData = data.dataConfig.exampleData;
          vm.wordCloud.setOption(vm.getOptions(data.dataConfig.exampleData));
          vm.$nextTick(()=>{
            if(vm.data.basicSwitch&&!vm.loop){
              // 自动轮播tootips
              vm.loop = loopTooltips(vm.wordCloud,vm.getOptions(exampleData),{loopSeries: true});
            }
          })
        }
      },
      // 数据处理
      handlerData(result){
        let vm= this;
        let exampleData = [];
        let data = JSON.parse(JSON.stringify(vm.data));
        let serieslist = data.serieslist;
        serieslist.map(item=>{
          let seriesObj = {
            name:item.seriesName,
            value:result[item.seriesKey],
            itemStyle:{color:item.seriesColor},
          };
          exampleData.push(seriesObj);
        })
        return {exampleData};
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
              vm.drawCloud();
            },dataConfig.refreshTime);
          });
        })
      },
      getOptions(seriesData){
        let colors=['#a0edff','#4fffff','#00b9ff','#b26fff','#2affa2','#345aff','#01e3ec']
        return {
          tooltip: {},
          series: [ {
            type: 'wordCloud',
            shape: 'pentagon',
            width: 540,
            height: 120,
            sizeRange: [15, 40],
            drawOutOfBound: true,
            textStyle: {
              normal: {
                color: function () {
                  return colors[parseInt(Math.random()*7)];
                }
              },
              emphasis: {
                shadowBlur: 10,
                shadowColor: 'rgba(0,0,0,0)',
              }
            },
            data:seriesData,
          }]
        }
      },
      stopAutoShow(){
        let vm = this;
        if(!vm.loop){
          vm.loop = loopTooltips(vm.wordCloud,vm.getOptions(vm.data),{loopSeries: true});
        }
        vm.loop.stopAutoShow();
      },
      zRenderGlobalOut(){
        let vm = this;
        if(!vm.loop){
          vm.loop = loopTooltips(vm.wordCloud,vm.getOptions(vm.data),{loopSeries: true});
        }
        vm.loop.zRenderGlobalOut();
      },
    },
    
    mounted(){
      let vm = this;
      vm.$nextTick(()=>{

        vm.wordCloud.on('mousemove', function (params) {
          vm.stopAutoShow();
        });
        
        vm.wordCloud.on('globalout', function (params) {
          vm.zRenderGlobalOut();
        });

        window.addEventListener('resize', ()=>{
          vm.wordCloud.resize();
        });
        
      })
    },
  }
</script>
