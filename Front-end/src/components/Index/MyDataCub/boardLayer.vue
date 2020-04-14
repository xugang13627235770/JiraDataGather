<template>
    <div id="boardLayer" class="bordered" :class="{'reWidth':toggle}">
        <h5 class="inblock3">图层 <c-icon type='chevron-left' class="handleIcon" @click.native='closeLayer'></c-icon></h5>
        <div class="layerArea">
            <div class="modeContent">
                <c-checkbox-group v-model="layerFilters">
                    <c-checkbox :label="1">只看锁定</c-checkbox>
                    <c-checkbox :label="3">只看隐藏</c-checkbox>
                </c-checkbox-group>
            </div>
            <div class="layerContent" :class="{'empty':elementData.length==0}">
                <p class="emptyText" v-if="elementData.length==0">暂无数据</p>
                <vuedraggable class="wrapper" v-model="elementData" v-bind="dragOptions" @end="parentMoveEnd">
                    <transition-group type="transition">
                        <div class="layerList c-panel c-panel-default" v-for="(item , index) in elementData" :key="item.uuid" 
                            :class="{'active':item.active,'selected':currentIds.indexOf(item.uuid)!=-1&&currentMode!=3}">
                            <div class="layerHeader ex" @click='openIt($event,item,index)' v-if="layerFilterFun(item)"
                            @mouseover="mouseActive(item,index)" @mouseout="removeActive(item,index)">
                                <c-icon :type='item.openLayer?"caret-down":"caret-right"' v-if="item.children&&item.children.length>0"></c-icon>
                                <span class="remark">{{item.titleName}}</span>
                                <i class="iconfont fr ml10" :class="[item.isView==4?'icon-liulan':'icon-yincang']" @click.stop='openEye(item , index)'></i>
                                <i class="iconfont fr" :class="[item.isLock==1?'icon-suoding':'icon-weisuoding']" @click.stop='openLock(item , index)'></i>
                            </div>
                            <div class="layerBody" v-if="item.children&&item.children.length>0&&layerFilterFun(item)" 
                                :class="{'openLayer':!item.openLayer}" :style="{'height':item.openLayer?30*item.children.length + 20+'px':0}">
                                <vuedraggable class="wrapper" v-model="item.children" v-bind="dragOptions">
                                    <transition-group type="transition">
                                        <p v-for="(itemChild , indexChild) in item.children" :key="itemChild.uuid" class="ex"
                                        @click='openIt($event,item,index,itemChild , indexChild)'>{{itemChild.titleName}}</p>
                                    </transition-group>
                                </vuedraggable>
                            </div>
                        </div>
                    </transition-group>
                </vuedraggable>
            </div>
        </div>
    </div>
</template>
<script>
import store from '../../../vuex/store'
import vuedraggable from 'vuedraggable';
import boardLayerJs from './js/boardLayer.js'
export default {
    name:"boardLayer",
    components: {vuedraggable},
    props:{
        toggle:{
            type:Boolean,
            default:false,
        },
    },
    data(){
        return{
            layerFilters:[],
            ctrlKey:false,
        }
    },
    computed: {
        dragOptions() {
            return {
                animation: 200,
            };
        },
        elementData:{
            get: function () {
                return store.getters.getElement
            },
            set: function (v) {
                store.dispatch('setElementData', v); 
            }
        },
        currentIds(){
            return store.getters.getCurrentIds;
        },
        currentMode(){
            return store.getters.getCurrentMode;
        },
    },
    watch:{
        
    },
    methods:{
        closeLayer(){
            this.$emit('close')
        },
        openIt($event,item,index,itemChild , indexChild){
            boardLayerJs.openIt($event,this,item,index,itemChild , indexChild);
        },
        mouseActive(item,index){
            boardLayerJs.mouseActive(this,item,index);
        },
        removeActive(item,index){
            boardLayerJs.removeActive(this,item,index);
        },
        // 图层是否可见
        openEye(item,index){
            boardLayerJs.openEye(this,item,index);
        },
        // 图标是否锁定
        openLock(item,index){
            boardLayerJs.openLock(this,item,index);
        },
        layerFilterFun(item){
            return boardLayerJs.layerFilterFun(this,item);
        },
        parentMoveEnd(evt){
            boardLayerJs.parentMoveEnd(this);
        },
        // 键盘方向键控制组件位置偏移（单位：1px）
        targetKeyDown(event){
            boardLayerJs.targetKeyDown(this,event);
        },
        targetKeyUp(event){
            boardLayerJs.targetKeyUp(this,event);
        },
    },
    mounted(){
        let vm = this;
        vm.$nextTick(()=>{
            document.addEventListener("keydown",vm.targetKeyDown,true);
            document.addEventListener("keyup",vm.targetKeyUp,true);
        })
    },
}
</script>
<style lang="scss" scoped>
#boardLayer{
    width: 265px;
    height: 100%;
    position: absolute;
    left: 0;
    top: 0;
    z-index: 99;
    transition:all .3s;
    &.reWidth{
        left: -265px;
    }
    >h5{
        margin: 0;
        height: 35px;
        line-height: 35px;
        text-align: center;
        color: #fff;
        position: relative;
        .handleIcon{
            position: absolute;
            right: 10px;
            top: 10px;
            z-index:99;
            cursor: pointer;
        }
    }
    .layerArea{
        width: 100%;
        height: calc(100% - 35px);
        padding: 0 0 15px 5px;
        .modeContent{
            width: 100%;
            height: 50px;
            line-height: 50px;
            text-align: right;
            padding-right: 10px;
        }
        .layerContent{
            width: 100%;
            height: calc(100% - 50px);
            overflow: hidden;
            overflow-y: auto;
            padding-right: 5px;
            &.empty{
                background: url('../../../../static/images/empty.png') center center no-repeat;
                background-size: 35%;
            }
            .emptyText{
                position: absolute;
                left: 50%;
                top:calc(50% + 110px);
                transform: translate(-50% , -50%);
                margin: 0;
            }
            .layerList{
                width: 100%;
                border-radius: 0 !important;
                cursor: pointer;
                margin-bottom: 10px !important;
                border:1px solid transparent;
                &.active{
                    border: 1px solid rgba(0,197,246,1);
                }
                &.selected{
                    border: 1px solid blue;
                }
                .layerHeader{    
                    padding: 13px 15px;
                    height: 42px;
                    background: linear-gradient(#1e2746,#2a3356) !important;
                    >i{
                        font-size: 16px;
                    }
                    .remark{
                        display: inline-block;
                        width: calc(100% - 60px);
                        overflow: hidden;
                        text-overflow: ellipsis;
                        white-space: nowrap;
                        vertical-align: middle;
                    }
                }
                .layerBody{
                    padding: 10px 20px;
                    transition: all .3s;
                    p{
                        margin: 0 !important;
                        height: 30px;
                        line-height: 30px;
                        text-overflow: ellipsis;
                        overflow: hidden;
                        white-space: nowrap;
                        color: #fff;
                        transition: opacity .8s;
                        opacity: 1;
                    }
                    &.openLayer{
                        overflow: hidden;
                        padding:0;
                        p{
                            opacity: 0;
                        }
                    }
                }
            }
        }
    }
}
</style>
<style lang="scss">
#boardLayer{
    .layerHeader{
        width: 100%;
        height: 20px;
        color: #fff;
        .iconfont{
            transition:all .8s;
            cursor: pointer;
        }
        .icon-weisuoding,
        .icon-yincang{
            color: rgba(255,255,255,.5);
        }
    }
    
    .layerArea{
        .modeContent{
            .c-checkbox-label span{
                vertical-align: bottom !important;
            }
        }
    }
}
</style>