<template>
    <div id="scrollGrid" class="bordered">
        <div class="scrollGridHeader inblock3 bordered-b">
            <span></span>
            <span v-for="(item , index) in header" :key="index" :style="headerStyle">{{item.label}}</span>
        </div>
        <div class="scrollGridBody" :class="{'unLoop':!data.basicSwitch}">
            <div class="gtridBody" v-for="(item , index) in copy" :key="index">
                <div class="bodyList bordered-b" v-for="(item , index) in gridData" :key="index" :class="{'inblock3':index%2!=0}">
                    <span>{{index+1}}</span>
                    <span :style="headerStyle" v-for="(listItem , listIndex) in header" :key="listIndex" :title="item[listItem.value]">{{item[listItem.value]}}</span>
                </div>
                <c-empty text="暂无数据！" color="#f00" size="16px" v-if="gridData.length==0"></c-empty>
            </div>
        </div>
    </div>
</template>
<script>
import store from '../../../vuex/store'
import api from '../../../api/api'
import configData from '../configData.js'
export default {
    name:"scrollGrid",
    props:{
      data:{
        type:Object,
        default(){
          return { }
        }
      },
    },
    data(){
        return{
            header:[],
            headerCopy:[
                {label:'表头1',value:"key1"},
                {label:'表头2',value:"key2"},
                {label:'表头3',value:"key3"},
                {label:'表头4',value:"key4"},
                {label:'表头5',value:"key5"},
            ],
            headerStyle:{},
            timer:null,
            textHeight:0,
            boxHeight:0,
            box:null,
            content:null,
            text:null,
            innerHTML:'',
            copy:1,
            gridData:[],
            bb:[
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
            ],
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
            },
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
            },
            ],
            count:0,
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
                let vm = this;
                vm.getSeriesData();
            },
        },
        'data.basicSwitch':function(nweVal,oldVew){
            let vm = this;
            if(nweVal){
                vm.getSeriesData();
            }else{
                vm.stopLamp();
            }
        },
    },
    methods:{
        checkScrollLeft(){
            let vm = this;
            let gtridBody = document.getElementsByClassName('gtridBody');
            // 判断body高度是否大于盒子高度
            if(vm.boxHeight > vm.textHeight){ vm.copy =1; return false};
            vm.copy =2 ;
            // 更新
            vm.textHeight = gtridBody[gtridBody.length-1].offsetHeight;
            vm.timer = requestAnimationFrame(function toScrollLeft(){
                //  如果body高度大于滚动条距离，则递归拖动
                if(vm.textHeight > vm.box.scrollTop){
                    vm.box.scrollTop+= vm.data.scrollSpeed;
                }else{
                    vm.box.scrollTop = 0;
                }
                
                if(vm.copy == 2){
                    vm.timer = requestAnimationFrame(toScrollLeft);
                }
            }); 
        },
        init(){
            let vm = this;
            let scrollGridBody = document.getElementsByClassName('scrollGridBody');
            let gtridBody = document.getElementsByClassName('gtridBody');
            [vm.box, vm.content] = [scrollGridBody[scrollGridBody.length-1],gtridBody[gtridBody.length-1]];
            if(!vm.innerHTML){
                vm.innerHTML = vm.content.innerHTML;
            }
            [vm.textHeight, vm.boxHeight] = [gtridBody[gtridBody.length-1].offsetHeight, vm.box.offsetHeight];
            vm.headerStyle = {width:`${'calc((100% - 80px) / '+(vm.header.length)+")"}`};
            if(vm.data.basicSwitch){
                vm.checkScrollLeft();
            }
        },
        stopLamp(){
            let vm = this;
            vm.copy =1;
            vm.box.scrollTop = 0;
            cancelAnimationFrame(vm.timer);
        },
        
        getSeriesData(){
            let vm = this;
            let data = JSON.parse(JSON.stringify(this.data));
            let currentDisplayData = store.getters.getDisplayData;
            let pageConfig = JSON.parse(JSON.stringify(store.getters.getPageConfig));
            if(vm.box){
                vm.copy =1;
                cancelAnimationFrame(vm.timer);
            }
            // isPublish==1 当前看板已发布  ；dateType==2 当前组件动态数据 currentMode==3 预览模式
            if(currentDisplayData.isPublish==1&&data.dataConfig.dateType==2&&vm.currentMode==3){
                vm.getDataByAxios().then(res=>{
                    // 将接口返回数据，处理成 configData中的exampleData数据格式即可
                    let result = vm.handlerData(res);
                    vm.header = result.header;
                    vm.gridData = result.exampleData;
                    vm.$nextTick(()=>{
                        vm.init();
                    })
                });
            }else{
                vm.header = JSON.parse(JSON.stringify(vm.headerCopy));
                vm.gridData = data.dataConfig.exampleData;
                vm.$nextTick(()=>{
                    vm.init();
                })
            }
        },
        // 数据处理
        handlerData(result){
            let vm= this;
            let exampleData = [],header=[];
            let headerList = JSON.parse(JSON.stringify(vm.data)).headerList;
            headerList.map(item=>{
                header.push({label:item.viewName,value:item.seriesName})
            })
            result.map(item=>{
                let obj={};
                header.map(headerItem=>{
                    obj[headerItem.value] = item[headerItem.value]
                })
                exampleData.push(obj);
            })
            return {exampleData,header};
        },
        // 动态获取数据
        getDataByAxios(){
            let vm = this;
            return new Promise((resolve,reject)=>{
                let dataConfig = JSON.parse(JSON.stringify(this.data)).dataConfig;
                api.getDataByAxios(dataConfig.url,{},dataConfig.axiosType).then(res=>{
                    // TODO  数据处理...
                    // res.code=0;
                    // res.result = vm.count%2==0?vm.aa:vm.bb;
                    // vm.count++;
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

    },
}
</script>
<style lang="scss" scoped>
#scrollGrid{
    width: 100%;
    height: 100%;
    .scrollGridHeader{
        width: 100%;
        height: 35px;
        line-height: 35px;
        >span{
            float: left;
            height: 35px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
            &:nth-child(1){
                width: 80px;
                text-align: center;
            }
        }
    }
    .scrollGridBody{
        width: 100%;
        height: calc(100% - 35px);
        overflow: hidden;
        &.unLoop{
            overflow-y: auto;
        }
        .gtridBody{
            width: 100%;
            .bodyList{
                width: 100%;
                height: 35px;
                line-height: 35px;
                >span{
                    float: left;
                    height: 35px;
                    overflow: hidden;
                    text-overflow: ellipsis;
                    white-space: nowrap;
                    &:nth-child(1){
                        width: 80px;
                        text-align: center;
                    }
                }
            }
        }
    }
}
</style>
<style lang="scss">
#scrollGrid{

}
</style>