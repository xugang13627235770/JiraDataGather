import store from '../../../../vuex/store'
import Bus from '../../bus'
const boardLayerJs={
    openIt($event,vm,item,index,exChildItem,exChildIndex){
        let oevent = $event || event;
        if(item.isGroup&&!exChildItem){
            vm.$set(vm.elementData[index],'openLayer',!item.openLayer);
        }
        if(item.isLock==1){
            return 
        }
        if(oevent.button==0){
            if(vm.ctrlKey){
                vm.currentIds.push(item.uuid);
                store.dispatch('setCurrentIds', JSON.parse(JSON.stringify(vm.currentIds)));
                store.dispatch('setCurrentNode', null);
                store.dispatch('setCurrentItemIndex', []);
                store.dispatch('setConfigPanel', true);
            }else{
                let currentUUID = '',indexs = [];
                if(item.isGroup&&exChildItem){
                    currentUUID = exChildItem.uuid;
                    indexs = [index,exChildIndex];
                }else{
                    currentUUID = item.uuid;
                    indexs = [index];
                }
                store.dispatch('setCurrentIds', [currentUUID]);
                store.dispatch('setCurrentItemIndex', indexs);
                store.dispatch('setConfigPanel', true);
            }
        }
    },
    mouseActive(vm,item,index){
        this.removeActive(vm);
        vm.$set(vm.elementData[index],'active',!item.active);
    },
    removeActive(vm,item,index){
        vm.elementData.map((item,indexs)=>{
            vm.$set(vm.elementData[indexs],'active',false);
            if(item.children&&item.children.length>0){
                item.children.map((itemC,indexC)=>{
                    vm.$set(vm.elementData[indexs].children[indexC],'active',false);
                })
            }
        });
    },
    openEye(vm,item,index){
        store.dispatch('setCurrentIds', []);
        store.dispatch('setCurrentNode', null);
        vm.$set(vm.elementData[index],'isView',item.isView==4?3:4);
    },
    openLock(vm,item,index){
        store.dispatch('setCurrentIds', []);
        store.dispatch('setCurrentNode', null);
        vm.$set(vm.elementData[index],'isLock',item.isLock==1?2:1);
    },
    layerFilterFun(vm,item){
        if(vm.layerFilters.length>0){
            return vm.layerFilters.indexOf(item.isView)!=-1||vm.layerFilters.indexOf(item.isLock)!=-1;
        }else{
            return true;
        }
    },
    parentMoveEnd(vm){
        let elementDataCopy = JSON.parse(JSON.stringify(vm.elementData));
        elementDataCopy=this.upDateIndex(elementDataCopy);
        vm.$set(vm,'elementData',elementDataCopy);
    },
    upDateIndex(data){
        data.map((item,index)=>{
            item.index = data.length - index;
            if(item.children&&item.children.length>0){
                item.children.map((itemC,indexC)=>{
                    itemC.index = item.children.length - indexC;
                })
            }
        })
        return data;
    },
    // 键盘事件
    targetKeyDown(vm,event){
        let e = event || window.event;
        vm.ctrlKey = e.ctrlKey;
    },
    targetKeyUp(vm,event){
        let e = event || window.event;
        vm.ctrlKey = false;
    },
};
export default boardLayerJs;