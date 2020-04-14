<template>
    <div id="componentConfig">
        <div class="configTab">
            <div class="tabList" :class="{'activeTab':tabIndex===index}" v-for="(item , index) in tabData" :key="index" @click='toggleTab(item , index)'>
                <i class="iconfont" :class="[item.icon]"></i>
                {{item.label}}
            </div>
            <div class="maskActive inblock3" :class="['tab'+anotherIndex]"></div>
            <div class="lineActive" :class="['tab'+tabIndex]"></div>
        </div>
        <div class="configContent">
            <!-- 配置 -->
            <div class="configTabArea" v-show="tabIndex===0&&currentTabs.indexOf('configTabArea')!=-1">
                <base-config ref="baseConfig"></base-config>
            </div>
            <!-- 数据 -->
            <div class="dataTabArea" v-show="tabIndex===1&&!isGroup&&currentTabs.indexOf('dataTabArea')!=-1">
                <data-config ref="dataConfig"></data-config>
            </div>
        </div>
        <input type="hidden" v-model="currentItemIndex">
    </div>
</template>
<script>
import store from '../../../../vuex/store'
import baseConfig from './baseConfig'
import dataConfig from './dataConfig'
export default {
    name:"componentConfig",
    components:{baseConfig,dataConfig},
    props:{
        
    },
    data(){
        return{
            tabData:[
                {label:"配置",icon:"icon-shezhi"},
                {label:"数据",icon:"icon-wulumuqishigongandashujuguanlipingtai-ico-"},
            ],
            tabIndex:0,
            anotherIndex:1,
            isGroup:false,
            currentTabs:[],
            currentIndex:[],
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
        currentIndex:{
            deep:true,
            handler(){
                let vm = this;
                let ElementData = vm.elementData;
                vm.$set(vm,'currentTabs', ['configTabArea','dataTabArea'])
                if(vm.currentIndex.length==1){
                    let currentData = ElementData[vm.currentIndex[0]];
                    vm.isGroup = currentData.isGroup;
                    vm.$set(vm,'currentTabs', currentData.tabData)
                }else if(vm.currentIndex.length==2){
                    vm.isGroup = false;
                }
            },
        },
    },
    methods:{
        toggleTab(item , index){
            this.tabIndex = index;
            this.anotherIndex = index==1?0:1;
        },
    },
    mounted(){

    },
}
</script>
<style lang="scss" scoped>
#componentConfig{
    width: 100%;
    height: 100%;
    .configTab{
        width: 100%;
        height: 36px;
        position: relative;
        >div.tabList{
            position: absolute;
            width: 50%;
            height: 36px;
            line-height: 36px;
            text-align: center;
            cursor: pointer;
            color: rgba(255,255,255,.3);
            font-size: 14px;
            z-index: 100;
            top: 0;
            transition: color .3s;
            &.activeTab{
                color: #fff;
            }
            &:nth-child(1){
                left: 0;
            }
            &:nth-child(2){
                left: 50%;
            }
        }
        .maskActive,
        .lineActive{
            position: absolute;
            width: 50%;
            height: 100%;
            top: 0;
            z-index: 99;
            transition: all .3s;
            &.tab0{
                left: 0;
                // animation: move0 1.3s;
            }
            &.tab1{
                left: 50%;
                // animation: move50 1.3s;
            }
        }
        .lineActive{
            border-top: 3px solid transparent;
            border-image: linear-gradient(to right, #2161DC, #0899B6) 1 10;
        }
    }
    .configContent{
        width: 100%;
        height: calc(100% - 36px);
        overflow: hidden;
        overflow-y: auto;
        >div{
            width: 100%;
            height: 100%;
            padding: 10px 10px 0 10px;
            color: #fff;   
            &.configTabArea{
                h5{
                    margin: 0;
                    height: 35px;
                    line-height: 35px;
                    padding-left: 15px;
                    color: rgba(255,255,255,.5);
                    font-weight: bold;
                    // font-size: 16px;
                }
                .titleSelection{
                    clear: both;
                }
            } 
        }
    }
    
    @keyframes move0 {
        from { left:0; }
        to   { left:50%;}
    }
    @keyframes move50 {
        from { left:50%; }
        to   { left:0;}
    }
}
</style>
<style lang="scss">
#componentConfig{
    .fontColorInput {
        input[type=text]{
            padding-left: 43px;
            padding-right: 30px;
        }
        .inline{
            position: relative;
            .colorView{
                position: absolute;
                width: 30px;
                height: 20px;
                // background: #0899B6;
                top: 6px;
                left: 12px;
                z-index: 99;
            }
            .colorChoose{
                background: url('../../../../../static/images/colorPickIcon.png') center center no-repeat;
                position: absolute;
                width: 20px;
                height: 20px;
                border-radius: 50%;
                top: 6px;
                right: 12px;
                z-index: 99;
                cursor: pointer;
            }
            .pickContent{
                padding:5px;
                background: #101417;
                position: absolute !important;
                right: 10px;
                top: 32px;
                z-index: 100;
                .colorPick{
                    background: #101417;
                    border: 1px solid #1A62BE !important;
                    border-radius: 0 !important;
                    .vc-input__input{
                        background: #262C32 !important;
                        border: none !important;
                        box-shadow: unset !important;
                        height: 25px;
                    }
                    .vc-input__label{
                        color: #fff !important;
                        height: 25px;
                        line-height: 20px;
                    }
                    .vc-sketch-presets{
                        border-top: 1px solid #1A62BE !important;
                    }
                }
            }
        }
    }
    .numberRangeSlider{
        width: calc(100% - 100px);
    }
    .numberRangeSliderView{
        float: right;
    }
    .areaSize {
        float: left;
        width: calc(50% - 5px);
        .c-input-number-input-wrap{
            >.inline{
                width: 100% !important;
            }
        }
        > span{
            color: rgba(255,255,255,.3);
        }
    }
    .nameArea{
        position: relative;
        .nameText{
            position: absolute;
            width: 40px;
            height: 26px;
            line-height: 26px;
            left:0;
            top :3px;
            z-index: 99;
            text-align: center;
            // border-right: 1px solid #fff;
            font-size: 14px;
        }
        input[type=text]{
            padding-left: 45px;
        }
    }
}
</style>