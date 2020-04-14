import store from '../../../../vuex/store'
import Bus from '../../bus.js'

const workAreaJs = {
    // 设置基准线样式
    setLineStyle(vm){
        let currentIndex = JSON.parse(JSON.stringify(store.getters.getCurrentItemIndex));
        let pageConfig = store.getters.getPageConfig;
        if(currentIndex.length>0){
            let parentNode = document.getElementById(vm.currentIds[0]);
            if(!parentNode){
                return
            }
            let offWidth = 0 ,offHeight = 0;
            if(vm.isGroupIn){
                let fatherId = vm.elementData[currentIndex[0]].uuid;
                offWidth = parentNode.offsetLeft + document.getElementById(fatherId).offsetLeft;
                offHeight = parentNode.offsetTop + document.getElementById(fatherId).offsetTop;
            }else{
                offWidth = parentNode.offsetLeft;
                offHeight = parentNode.offsetTop;
            }
            let top= offHeight * pageConfig.scale + 50 +"px";
            let width = offWidth * pageConfig.scale + 50 +"px";
            vm.XLineStyle = {width,top};
    
            let height = offHeight * pageConfig.scale + 50 +"px";
            let left = offWidth * pageConfig.scale + 50 +"px";
            vm.YlineStyle = {height,left};
    
            this.upDataCompSize(vm,parentNode);
        }
    },
    // 更新组件宽高和定位 / 与右侧栏联动
    upDataCompSize(vm,parentNode){
        let that = this;
        if(vm.moveFlag){
            clearTimeout(vm.moveFlag);
        }
        vm.moveFlag = setTimeout(()=>{
            let currentIndex = JSON.parse(JSON.stringify(store.getters.getCurrentItemIndex));
            if(currentIndex.length>0){
                let {width ,height , left , top} = {width:parentNode.offsetWidth,height:parentNode.offsetHeight,left:parentNode.offsetLeft,top:parentNode.offsetTop};
                let obj = {left , top , width , height};
                if(currentIndex.length==1){
                    Object.assign(vm.elementData[currentIndex[0]],obj);
                }else if(currentIndex.length==2){
                    Object.assign(vm.elementData[[currentIndex[0]]].children[currentIndex[1]],obj);
                }
            }
        },150)
    },
};
export default workAreaJs;