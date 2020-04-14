<template>
    <div id="addBoard" class="block-bg">
        <div class="heaberBoard">
            <!-- 图层按钮 -->
            <c-button type='primary' class="mr20 layerBtn" @click.native='toggleLayer' title="图层">
                <i class="iconfont icon-icon-tuceng"></i>
            </c-button>
            <!-- 组件按钮 -->
            <c-dropdown class="mr10" v-for="(item , index) in btnData" :key="index">
                <div slot="trigger">
                    <c-button type='linear'>
                        <i class="iconfont" :class="[item.icon]" :style="{'font-size':item.fontSize?item.fontSize:'16px'}"></i>
                        <c-icon type='sort-desc' class="toggleIcon" v-if="item.menu.length>0"></c-icon>
                    </c-button>
                </div>
                <li v-for="(menuItme , menuIndex) in item.menu" :key="menuIndex" @click='chooseCompn(menuItme)' draggable="true" 
                    @dragstart="dragstart($event, menuItme)" @dragend="dragend">
                    <a href="javascript:;"> <i class="iconfont" :class="[menuItme.icon]"></i> {{menuItme.label}}</a>
                    <img :src="'../../../../static/images/drop/'+ menuItme.value +'.png'" alt="" class="dragImage" :id="'img'+menuItme.value">
                </li>
            </c-dropdown>
            <!-- 右侧按钮 -->
            <c-button type='linear' class="fr ml10" @click.native='closeAddBoard'>
                <i class="iconfont icon-guanbi"></i>
            </c-button>
            <c-button type='linear' class="fr ml10" title="保存" @click.native='saveBoard'>
                <i class="iconfont icon-baocun"></i>
            </c-button>
            <c-button type='linear' class="fr ml10" title="预览" @click.native='preViewFun'>
                <i class="iconfont icon-yulan"></i>
            </c-button>
            <c-button type='linear' class="fr ml10" title="右侧面板" @click.native='toggleConfigPanel'>
                <i class="iconfont icon-youce"></i>
            </c-button>
        </div>
        <div class="workArea">
            <!-- 图层 -->
            <board-layer ref="boardLayer" :toggle='toggle' @close='toggleLayer'></board-layer>
            <!-- 工作区 -->
            <work-area ref="workArea" :toggle='toggle'></work-area>
            <!-- 配置面板 -->
            <transition name="fadeLeft">
                <cub-config ref="CubConfig" v-show='showConfigPanel' @toggle='toggleConfigPanel'></cub-config>
            </transition>
        </div>
        <!-- 右键菜单 -->
        <context-menu ref="contextMenu" @change='menuChange'></context-menu>

        <input type="hidden" v-model="currentModes">
    </div>
</template>
<script>
import Bus from '../bus'
import store from '../../../vuex/store'
import contextMenu from './contextMenu'

import addBoard from './js/addBoard.js'
import boardLayer from './boardLayer'
import workArea from './workArea'
import CubConfig from './config/CubConfig'
export default {
    name:"addBoard",
    components:{boardLayer,workArea,CubConfig,contextMenu},
    data(){
        return{
            btnData:[
                {
                    icon:"icon-jurassic_border-cuxia",
                    menu:[
                        {label:"边框(右上)",value:"borderTr",icon:"icon"},
                        {label:"边框(右下)",value:"borderRb",icon:"icon"},
                        {label:"边框(左上)",value:"borderTl",icon:"icon"},
                        {label:"边框(左下)",value:"borderLb",icon:"icon"},
                        {label:"旋转轮盘",value:"ferrisWheel",icon:"icon"},
                    ]
                },
                {
                    icon:"icon-62",
                    fontSize:"20px",
                    menu:[
                        {label:"柱形图",value:"bar",icon:"icon-zhuxingtu1"},
                        {label:"折线图",value:"line",icon:"icon-zhexiantu"},
                        {label:"饼状图",value:"pie",icon:"icon-tubiaobingzhuangtu"},
                    ]
                },
                {
                    icon:"icon-wenzi",
                    menu:[
                        {label:"文字",value:"textLamp",icon:"icon-ziti"},
                        {label:"数字翻盘器",value:"numberAnimate",icon:"icon-shuzi"},
                    ]
                },
                {
                    icon:"icon-picture",
                    fontSize:"20px",
                    menu:[
                        {label:"图片",value:"imageComp",icon:"icon-picture"},
                        {label:"视频",value:"videoComp",icon:"icon-shipin"},
                    ]
                },
                {
                    icon:"icon-ditu1",
                    menu:[]
                },
                {
                    icon:"icon-rili1",
                    menu:[
                        {label:"词云",value:"textCloud",icon:"icon-ciyun"},
                        {label:"时间器",value:"timer",icon:"icon-shijian1"},
                        {label:"表格",value:"scrollGrid",icon:"icon-biaoge"},
                        {label:"进度条",value:"pieProgress",icon:"icon-jindutiao"},
                    ]
                }
            ],
            toggle:false,
            currentMode:'',
        }
    },
    computed: {
        showConfigPanel(){
            return store.getters.getConfigPanel;
        },
        currentModes(){
            let vm = this;
            vm.currentMode = store.getters.getCurrentMode;
            return store.getters.getCurrentMode;
        },
        pageConfig(){
            return store.getters.getPageConfig;
        },
        elementData:{
            get: function () {
                return store.getters.getElement
            },
            set: function (v) {
                store.dispatch('setElementData', v); 
            }
        },
    },
    watch:{
        currentMode:{
            deep:true,
            handler(){
                addBoard.initMenu(this);
            },
        },
        elementData:{
            deep:true,
            immediate:true,
            handler(){
                this.upDateDisplayData();
            },
        },
        pageConfig:{
            deep:true,
            handler(){
                this.upDateDisplayData();
            },
        },
    },
    methods:{
        upDateDisplayData(){
            addBoard.upDateDisplayData(this);
        },
        closeAddBoard(){
            addBoard.closeAddBoard(this);
        },
        toggleLayer(){
            addBoard.toggleLayer(this);
        },
        toggleConfigPanel(){
            addBoard.toggleConfigPanel(this);
        },
        chooseCompn(item){
            addBoard.chooseCompn(this,item);
        },
        menuChange(item){
            addBoard.menuChange(this,item);
        },
        saveBoard(){
            addBoard.saveBoard(this);
        },
        preViewFun(){
            this.$emit('preViewFun','','in');
        },
        dragstart(event, data){
            addBoard.dragstart(this,event, data);
        },
        dragend(event) {
            addBoard.dragend(this,event);
        },
    },
    mounted(){
        let vm = this;
        Bus.$on('initMenu', content => { 
            addBoard.initMenu(vm);
        });
    },
}
</script>
<style lang="scss" scoped>
#addBoard{
    width: 100%;
    height: 100%;
    position: absolute;
    left: 0;
    top: 0;
    z-index: 100;
    .heaberBoard{
        width: 100%;
        height: 50px;
        padding: 10px 20px;
        .layerBtn{
            padding: 5px 8px;
            background: linear-gradient(to left ,#04a3b0, #2657e3) !important;
        }
    }
    .workArea{
        width: 100%;
        height: calc(100% - 50px);
        position: relative;
    }
}
</style>
<style lang="scss">
#addBoard{
    .dragImage{
        width: 0px;
        height: 0px;
        display: none;
    }
    .heaberBoard{
        .c-btn-linear{
            min-width: unset !important;
            >span{
                padding: 0 12px  !important;
                position: relative;
                ul{
                    position: absolute;
                    top: 28px;

                }
            }
        }
        .toggleIcon{
            position: absolute !important;
            bottom: 0;
            right: 2px;
            top: unset !important;
            left: unset !important;
            transform: rotate(-45deg) !important;
        }
    }
    .c-dropdown-menu li>a{
        padding: 7px 10px !important;
        white-space: nowrap !important;
    }
}
</style>