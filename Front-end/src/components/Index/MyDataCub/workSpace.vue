<template>
    <div id='workSpace' class="workSpace" v-if="upDateBK" :style="[basicStyle]" :class="{'preView':currentMode==3}">
        <div class="ex" :id="item.uuid" v-for="(item , index) in elementData" :key="index" :style="exStyle(item,index)" v-show="item.isView==4||currentMode==3" 
        :class="{'selected':currentIds.indexOf(item.uuid)!=-1&&currentMode!=3,'exGroups':item.isGroup,'actived':item.active&&currentMode!=3}">
            <div class="exList" v-if="!item.isGroup">
                <!-- 图表区域 -->
                <chart-area :type='item.type' :data="item"></chart-area>

                <c-icon type='expand' class="expand" @mousedown.native.stop='handlerTop($event,item,index)' v-if="!(item.isLock==1)&&currentMode!=3" v-show="currentIds.indexOf(item.uuid)!=-1"></c-icon>
                <div class="dragMask" @mousedown.stop='moveEx($event,item,index)' @mouseover="mouseOverEx($event,item,index)" @mouseleave.stop="removeActiveEx"
                 :style="{'cursor':item.isLock==1?'unset':'move'}" v-if="currentMode!=3"></div>
            </div>
            <div class="exGroup" v-if="item.isGroup">
                <div class="exChild" :id="exChildItem.uuid" v-for="(exChildItem , exChildIndex) in item.children" :key="exChildIndex" 
                    :style="exStyle(exChildItem)" :class="{'selected':currentIds.indexOf(exChildItem.uuid)!=-1&&currentMode!=3}">
                    <!-- 图表区域 -->
                    <chart-area :type='exChildItem.type' :data="exChildItem"></chart-area>
                    
                    <c-icon type='expand' class="expand" @mousedown.native.stop='handlerTop($event,item,index,exChildItem,exChildIndex)' v-if="!(item.isLock==1)" v-show="currentIds.indexOf(exChildItem.uuid)!=-1"></c-icon>
                    <div class="dragMask"  @mousedown.stop='moveEx($event,item,index,exChildItem,exChildIndex)'  
                    @mouseover="mouseOverEx($event,item,index,exChildItem,exChildIndex)"  v-if="currentMode!=3"  @mouseleave.stop="removeActiveEx"
                     :style="{'cursor':item.isLock==1?'unset':'move'}"></div>
                </div>
                <c-icon type='expand' class="expand" @mousedown.native.stop='handlerTop($event,item,index)' v-if="!(item.isLock==1)&&currentMode!=3"></c-icon>
                <div class="dragMask"  @mousedown.stop='moveEx($event,item,index)'  
                :style="{'cursor':item.isLock==1?'unset':'move'}" v-if="currentMode!=3"></div>
            </div>
        </div>
    </div>
</template>
<script>
import Bus from '../bus'
import store from '../../../vuex/store'
import chartArea from './chartArea'
import workSpace from './js/workSpace.js'
export default {
    name:"workSpace",
    components:{chartArea},
    props:{
        
    },
    data(){
        let self = this;
        return{
            x:0,
            y:0,
            parentNode:null,
            areaWidth:0,
            areaHeight:0,
            distanceX:0,
            distanceY:0,
            offsetXParent:0,
            offsetYParent:0,
            currentTargetWidth:0,
            currentTargetHeight:0,
            showDragMask:false,
            moveFlag:null,
            pageConfigs:'',
            upDateBK:true,
            ctrlKey:false,
            isGroupIn:false,
            setLineStyleFlag:null,
            elementDataT:this.elementData
        }
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
            return workSpace.basicStyle(this);
        },
        screenShotStyle(){
            let pageConfig = store.getters.getPageConfig;
            return {width:2320 * pageConfig.scale+"px",height:1480*pageConfig.scale+"px"};
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
        elementData:{
            deep:true,
            immediate:true,
            handler(){
                store.dispatch('setElementData', this.elementData);  
            },
        },
    },
    methods:{
        // 控制组件大小
        handlerTop(el,item,index,exChildItem,exChildIndex){
            workSpace.handlerTop(this,el,item,index,exChildItem,exChildIndex);
        },
        //改变组件大小时的鼠标移动事件
        mouseMove(e){
            workSpace.mouseMove(this,e);
        },
        //改变组件大小动作停止后，卸载事件
        mouseUp(el){
            workSpace.mouseUp(this,el);
        },
        // 移动组件位置（开始）
        moveEx(ev,item,index,exChildItem,exChildIndex){
            workSpace.moveEx(this,ev,item,index,exChildItem,exChildIndex);
        },
        // 移动组件位置（移动中）
        moveExDocument(ev){
            workSpace.moveExDocument(this,ev);
        },
        //移动组件位置（结束）卸载事件 
        moveUpExDocument($event){
            workSpace.moveUpExDocument(this,$event);
        },
        mouseOverEx(ev,item,index,exChildItem,exChildIndex){
            workSpace.mouseOverEx(this,ev,item,index,exChildItem,exChildIndex);
        },
        removeActiveEx(){
            workSpace.removeActiveEx(this);
        },
        // 键盘方向键控制组件位置偏移（单位：1px）
        targetKeyDown(event){
            workSpace.targetKeyDown(this,event);
        },
        targetKeyUp(event){
            workSpace.targetKeyUp(this,event);
        },
        // document点击事件（清除当前选中节点）
        documentClick(e){
            workSpace.documentClick(this,e);
        },
        exStyle(item,index){
            let pageConfig = store.getters.getPageConfig;
            return {'width':item.width+'px','height':item.height+'px',top:item.top+'px',left:item.left+'px',background:item.basicBGColor,zIndex:item.index};
        },
        dropFun(data,x,y){
            let vm = this;
            if(data){
                vm.addElementItem(JSON.parse(data),x,y);
            }
        },
        // x,y为中心点的位置
        addElementItem(item,x,y){
            workSpace.addElementItem(this,item,x,y);
        },
    },
    mounted(){
        let vm = this;
        vm.$nextTick(()=>{
            vm.areaWidth = document.getElementsByClassName('workSpace')[0].offsetWidth;
            vm.areaHeight = document.getElementsByClassName('workSpace')[0].offsetHeight;
            document.addEventListener("click",function(e){
                vm.documentClick(e);
            });
            document.addEventListener("keydown",vm.targetKeyDown,true);
            document.addEventListener("keyup",vm.targetKeyUp,true);
            
        })
    },
}
</script>
<style lang="scss" scoped>
#workSpace{
    &.workSpace{
        position: absolute;
        transform-origin: 0 0;
        width: calc(100% - 100px);
        height: calc(100% - 100px);
        top: 50px;
        left: 50px;
        z-index: 2;
    }
    &.preView{
        top: 0;
        left: 0;
        .exGroups{
            &:hover{
                border: 1px solid transparent !important;
            }
        }
    }
    .ex{
        width: 300px;
        height: 300px;
        position: absolute;
        top: 50px;
        left: 100px;
        // transform: translate(100px , 50px);
        border: 1px solid transparent;
        cursor: pointer;
        box-sizing: border-box;
        padding:5px;
        &.exGroups{
            padding:0;
            &:hover{
                border: 1px solid blue;
            }
        }
        &.actived{
            border: 1px solid rgba(0,197,246,1);
        }
        &.selected{
            z-index: 999 !important;
            border: 1px solid blue;
        }
        .exList{
            width: 100%;
            height: 100%;
            position: absolute;
            top: 0;
            left: 0;
            .dragMask{
                position: absolute;
                width: 100%;
                height: 100%;
                background: rgba(0,0,0,0);
                top: 0;
                left: 0;
                z-index: 1;
                // display: none;
                cursor: move;
            }
            .expand{
                transform: rotateX(-180deg);
                margin-left: 5px;
                position: absolute;
                z-index: 2;
                bottom: 0;
                right: 0;
                cursor: pointer;
            }
        }
        .exGroup{
            position: absolute;
            width:calc(100% - 0px);
            height: calc(100% - 0px);
            .exChild{
                border: 1px solid transparent;
                position: absolute;
                z-index: 2;
                &.selected{
                    z-index: 3;
                    border: 1px solid blue;
                }
            }
            .dragMask{
                position: absolute;
                width: 100%;
                height: 100%;
                background: rgba(0,0,0,0);
                top: 0;
                left: 0;
                z-index: 1;
                // display: none;
                cursor: move;
            }
            .expand{
                transform: rotateX(-180deg);
                margin-left: 5px;
                position: absolute;
                z-index: 2;
                bottom: 0;
                right: 0;
                cursor: pointer;
            }
        }
    }
}
</style>
<style lang="scss">
#workSpace{

}
</style>