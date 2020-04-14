<template>
    <div id="workArea" class="bordered" :class="{'reWidth':toggle}">
        <div class="LTIcon"></div>
        <top-staff ref="topStaff"></top-staff>
        <left-staff ref="leftStaff"></left-staff>
        <div class="realArea" id="realArea" @scroll='scrollFun'>
            <div class="screenShot" :style="screenShotStyle"></div>

            <work-space ref="workSpace"></work-space>

            <div class="XLine" :style="XLineStyle" v-if="currentNode"> <span>{{YlineStyle.height | filterY}}</span> </div>
            <div class="YLine" :style="YlineStyle" v-if="currentNode"> <span>{{XLineStyle.width | filterX}}</span> </div>

            <div class="workSpaceMask" :style="[basicStyle]" @drop="dropFun" @dragover.prevent v-if="showSpaceMask"></div>
        </div>
        <div class="workAreaTips" :style="tipsStyle" v-if="showTips">
            <p>宽：{{tipsStyle.width}}</p>
            <p>高：{{tipsStyle.height}}</p>
        </div>
        <div class="scalBar block-bg" :class="{'showConfigPanel':showConfigPanel}">
            <span>缩放比 ( {{scaleFun()}} )</span>
            <c-icon type='minus' @click.native='pageConfig.scale-=step' class="mr10 ml10"></c-icon>
            <c-slider class="numberRangeSlider" v-model="pageConfig.scale" width='' :min='0' :max='2' :step='step'></c-slider>
            <c-icon type='plus' @click.native='pageConfig.scale+=step' class="ml10"></c-icon>
        </div>
    </div>
</template>
<script>
import Bus from '../bus.js'
import store from '../../../vuex/store'
import workArea from './js/workArea.js'
import topStaff from './staff/topStaff'
import leftStaff from './staff/leftStaff'
import workSpace from './workSpace'

export default {
    name:"workArea",
    components:{topStaff,leftStaff,workSpace},
    props:{
        toggle:{
            type:Boolean,
            default:false,
        },
    },
    data(){
        let self = this;
        return{
            XLineStyle:{width:0,top:0},
            YlineStyle:{height:0,left:0},
            tipsStyle:{width:0,height:0,top:0,left:0},
            showTips:false,
            step:0.1,
        }
    },
    filters:{
        filterX(X){
            let pageConfig = store.getters.getPageConfig;
            return Math.round((parseInt(X) - 50) / pageConfig.scale);
        },
        filterY(Y){
            let pageConfig = store.getters.getPageConfig;
            return Math.round((parseInt(Y) - 50) / pageConfig.scale);
        },
    },
    computed: {
        currentNode(){
            return store.getters.getCurrentNode;
        },
        currentIds(){
            return store.getters.getCurrentIds;
        },
        currentMode(){
            return store.getters.getCurrentMode;
        },
        basicStyle(){
            let pageConfig = store.getters.getPageConfig;
            return {width:pageConfig.width+"px",height:pageConfig.height+"px",transform: `${'scale('+ pageConfig.scale +') translate(0px, 0px)'}`};
        },
        screenShotStyle(){
            let pageConfig = store.getters.getPageConfig;
            return {width:2320 * pageConfig.scale+"px",height:1480*pageConfig.scale+"px"};
        },
        pageConfig(){
            return store.getters.getPageConfig;
        },
        showConfigPanel(){
            return store.getters.getConfigPanel;
        },
        elementData(){
            return store.getters.getElement
        },
        showSpaceMask(){
            return store.getters.getSpaceMask;
        },
    },
    watch:{
        elementData:{
            deep:true,
            immediate:true,
            handler(){
                store.dispatch('setElementData', this.elementData);  
            },
        },
    },
    methods:{
        scrollFun(e){
            let scrollTop = document.getElementById('realArea').scrollTop;
            let leftStaff = document.getElementById('leftStaff');
            leftStaff.style.top = 30-scrollTop +"px";
        },
        scaleFun(){
            return this.pageConfig.scale.toFixed(1);
        },
        dropFun(event){
            let vm = this;
            let data = event.dataTransfer.getData('item');
            if(data){
                vm.$refs.workSpace.dropFun(data,event.offsetX,event.offsetY);
            }
        },
    },
    mounted(){

    },
}
</script>
<style lang="scss" scoped>
#workArea{
    width: calc(100% - 265px);
    height: 100%;
    border-left:none;
    position: absolute;
    right: 0;
    bottom: 0;
    z-index: 99;
    transition:all .3s;
    padding: 30px 0 0 30px;
    background: #000;
    overflow: hidden;
    .ml10{
        margin-left: 10px;
    }
    .mr10{
        margin-right: 10px;
    }
    &.reWidth{
        width: 100%;
    }
    .LTIcon{
        position: absolute;
        top: 0;
        left: 0;
        z-index: 99;
        width: 30px;
        height: 30px;
        background-color: #000;
        background-image: url('../../../../static/images/LT.png');
        background-position: center center;
        background-repeat: no-repeat;
        border-right: 1px solid #5D646B;
        border-bottom: 1px solid #5D646B;
        cursor: pointer;
    }
    .realArea{
        width: 100%;
        height: calc(100% - 30px);
        background: url('../../../../static/images/bg.png') 0 0 repeat;
        position: relative;
        overflow: hidden;
        overflow-y: auto;
        .screenShot{
            position: absolute;
            top: 0;
            left: 0;
            z-index: 1;
        }
        .XLine{
            position: absolute;
            z-index: 100;
            left: 0;
            border-top:1px  dashed #5bceff;
        }
        .YLine{
            position: absolute;
            z-index: 100;
            top: 0;
            border-left:1px  dashed #5bceff;
        }
        .workSpaceMask{
            position: absolute;
            width: calc(100% - 100px);
            height: calc(100% - 100px);
            top: 50px;
            left: 50px;
            z-index: 999;
        }
    }
    .workAreaTips{
        position: fixed;
        z-index: 9999;
        padding:5px;
        background: #000;
        color: #fff;
        border-radius: 5px;
        width: unset !important;
        height: unset !important;
        p{
            margin: 0 !important;
        }
    }
    .scalBar{
        width: 100%;
        height: 30px;
        position: absolute;
        bottom: 0;
        left: 0;
        z-index: 99;
        text-align: right;
        padding: 0 20px;
        transition: width .3s;
        &.showConfigPanel{
            width: calc(100% - 399px);
        }
        .numberRangeSlider{
            width: 200px;
        }
        >i{
            cursor: pointer;
        }
    }
}
</style>