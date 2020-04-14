<template>
    <div id="contextMenu" class="inblock3 bordered" :style="{'height':menuData.length * 30 + 2 +'px'}">
        <ul id="list">
            <li v-for="(item , index) in menuData" :key="index" @click="selectMenu(item)" :class="{'enable':item.enable}">
                <i class="iconfont" :class="[item.icon]"></i>
                {{item.label}}
            </li>
        </ul>
        <input type="hidden" v-model="currentids">
        <input type="hidden" v-model="currentIndex">
    </div>
</template>
<script>
import store from '../../../vuex/store'
export default {
    name:"contextMenu",
    props:{
        
    },
    data(){
        return{
            menuData:[
                {label:"成组",value:1,icon:'icon-group',enable:true},
                {label:"取消组",value:2,icon:'icon-ungroup',enable:true},
                {label:"删除",value:3,icon:'icon-shanchu',enable:true},
            ]
        }
    },
    computed: {
        currentids(){
            return store.getters.getCurrentIds;
        },
        currentIndex(){
            return store.getters.getCurrentItemIndex;
        },
        elementDataCo(){
            return JSON.parse(JSON.stringify(store.getters.getElement));
        },
    },
    watch:{
        // 监听ID集合变化，更改右键菜单状态
        currentids:{
            deep:true,
            handler(){
                let vm = this;
                if(vm.currentids.length>0){
                    vm.menuData.map(item=>{
                        if(item.value==3){item.enable = true}
                    });
                    if(vm.currentids.length==1){
                        let data = vm.elementDataCo.filter(item=>item.uuid == vm.currentids[0])[0];
                        if((!data&&vm.currentIndex.length==2)||data.isGroup){
                            vm.menuData.map(item=>{
                                if(item.value==1){item.enable = false};
                                if(item.value==2){item.enable = true};
                            })
                        }else{
                            vm.menuData.map(item=>{
                                if(item.value==1){item.enable = false};
                                if(item.value==2){item.enable = false};
                            })
                        }
                    }else{
                        let hasGroup = false;
                        vm.elementDataCo.map(item=>{
                            if(vm.currentids.indexOf(item.uuid)!=-1&&item.isGroup){
                                hasGroup = true;
                            }
                        })
                        if(hasGroup){
                            vm.menuData.map(item=>{
                                if(item.value==1||item.value==2){item.enable = false}
                            });
                        }else{
                            vm.menuData.map(item=>{
                                if(item.value==1){item.enable = true};
                                if(item.value==2){item.enable = false};
                            })
                        }
                    }
                }else{
                    vm.menuData.map(item=>{
                        // if(item.value==1||item.value==2){item.enable = false}
                        item.enable = false
                    });
                }
            },
        },
    },
    methods:{
        initMenu(){
            let listBox = document.getElementById('contextMenu');
            let dragMasks = document.querySelectorAll('.dragMask');
            let layerList = document.querySelectorAll('.layerList');
            this.bindEvent(listBox,dragMasks);
            this.bindEvent(listBox,layerList);
        },
        bindEvent(menuNode,nodeList){
            for(let p=0;p<nodeList.length;p++){
                let dragMaskC = nodeList[p];
                dragMaskC.addEventListener('contextmenu',function(ev){
                    let eve = ev || event;
                    //阻止默认行为
                    eve.preventDefault();
                    let x = eve.clientX;
                    let y = eve.clientY;
                    
                    menuNode.style.display = 'block';
                    menuNode.style.left = x + 'px';
                　  menuNode.style.top = y + 'px';
                    document.onclick = function(){
                        menuNode.style.display = 'none';
                    }
                },true);
            }
        },
        selectMenu(item){
            if(item.enable){
                this.$emit('change',item);
            }
        },
    },
    mounted(){
        let vm = this;
        vm.$nextTick(()=>{
            
        })
    },
}
</script>
<style lang="scss" scoped>
#contextMenu{
    width: 100px;
    position: fixed;
    z-index: 9999;
    display: none;
    #list{
        width:100%;
        height:100%;
    }
    #list li{
        height:30px;
        line-height:30px;
        font-size:12px;
        text-align:left;
        color:rgba(255,255,255,.3);
        padding-left: 10px;
        cursor: pointer;
        &.enable{
            color:#fff;
        }
        &:hover{
            background: #141B38;
        }
    }
}
</style>