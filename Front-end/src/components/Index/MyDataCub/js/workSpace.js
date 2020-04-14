
import store from '../../../../vuex/store'
import Bus from '../../bus.js'
import configData from '../../configData.js'

const workSpaceJs = {
    basicStyle(vm){
        let pageConfig = store.getters.getPageConfig;
        let scale = vm.currentMode==3?1:pageConfig.scale;
        return {
            width:pageConfig.width+"px",
            height:pageConfig.height+"px",
            transform: `${'scale('+ scale +') translate(0px, 0px)'}`,
            backgroundColor:pageConfig.pageBGColor,
            backgroundImage:`${'url('+ pageConfig.bgImage +') '}`,
            backgroundRepeat:'no-repeat', 
            backgroundPosition:'center center', 
            backgroundSize: 'contain',
        };
    },
    handlerTop(vm,el,item,index,exChildItem,exChildIndex){
        let oevent = el || event;
        let currentUUID = '',indexs = [];
        if(item.isGroup&&exChildItem){
            currentUUID = exChildItem.uuid;
            vm.isGroupIn = true;
            indexs = [index,exChildIndex];
        }else{
            currentUUID = item.uuid;
            vm.isGroupIn = false;
            indexs = [index];
        }
        store.dispatch('setCurrentNode', oevent);
        store.dispatch('setCurrentIds', [currentUUID]);
        store.dispatch('setCurrentItemIndex', indexs);
        vm.parentNode = document.getElementById([currentUUID][0]);
        //按下元素后，计算当前鼠标与对象计算后的坐标 
        vm.x = el.clientX - vm.parentNode.offsetWidth,
        vm.y = el.clientY - vm.parentNode.offsetHeight;
        el.target.setCapture ? (
            //捕捉焦点 
            el.target.setCapture(),
            //设置事件 
            el.target.onmousemove = function (ev) {
                vm.mouseMove(ev || event)
            },
            el.target.onmouseup = vm.mouseUp
        ) : (
                //绑定事件 
                document.addEventListener("mousemove", vm.mouseMove),
                document.addEventListener("mouseup", vm.mouseUp)
            )
        //防止默认事件发生 
        el.preventDefault()
    },
    mouseMove(vm,e){
        vm.$parent.showTips = true;
        vm.$parent.tipsStyle.width = e.clientX - vm.x;
        vm.$parent.tipsStyle.height = e.clientY - vm.y;
        vm.$parent.tipsStyle.top = e.clientY - 100 + 'px';
        vm.$parent.tipsStyle.left = e.clientX+ 'px';
        vm.parentNode.style.width = e.clientX - vm.x + 'px';
        vm.parentNode.style.height = e.clientY - vm.y + 'px';
        this.upDataCompSize(vm,vm.parentNode);
    },
    mouseUp(vm,el){
        //在支持 releaseCapture 做些东东 
        el.releaseCapture ? (
            //释放焦点 
            el.releaseCapture(),
            //移除事件 
            el.onmousemove = el.onmouseup = null
        ) : (
                //卸载事件 
                document.removeEventListener("mousemove", vm.mouseMove),
                document.removeEventListener("mouseup", vm.mouseUp),
                vm.$parent.tipsStyle = {width:0,height:0,top:0,left:0},
                vm.$parent.showTips = false
            )
    },
    moveEx(vm,ev,item,index,exChildItem,exChildIndex){
        let oevent = ev || event;
        let pageConfig = store.getters.getPageConfig;
        // oevent.button 0 鼠标左键 1 鼠标右键
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
                    vm.isGroupIn = true;
                    indexs = [index,exChildIndex];
                }else{
                    currentUUID = item.uuid;
                    vm.isGroupIn = false;
                    indexs = [index];
                }
                let parentNode = document.getElementById(currentUUID);
                store.dispatch('setCurrentIds', [currentUUID]);
                store.dispatch('setCurrentNode', oevent);
                store.dispatch('setCurrentItemIndex', indexs);
                store.dispatch('setConfigPanel', true);
                // 鼠标离左侧最小距离
                vm.distanceX = oevent.clientX - parentNode.offsetLeft;
                // 鼠标离顶部最小距离
                vm.distanceY = oevent.clientY - parentNode.offsetTop;
                vm.currentTargetWidth = parentNode.offsetWidth ;
                vm.currentTargetHeight = parentNode.offsetHeight ;

                // 移动组内元素
                if(vm.isGroupIn){
                    vm.distanceX = oevent.clientX - oevent.offsetX - parentNode.offsetLeft;
                    vm.distanceY = oevent.clientY - oevent.offsetY - parentNode.offsetTop;
                    vm.offsetXParent = oevent.offsetX;
                    vm.offsetYParent= oevent.offsetY;
                }
                let target = document.getElementById('workSpace');
                target.addEventListener("mousemove",vm.moveExDocument,true);
                target.addEventListener("mouseup",vm.moveUpExDocument,true);
            }
            this.setLineStyle(vm);
        }else{

        }
    },
    mouseOverEx(vm,ev,item,index,exChildItem,exChildIndex){
        this.removeActiveEx(vm);
        if(item.isGroup&&exChildItem){
            vm.elementData[index].children[exChildIndex].active=true;
        }
        vm.elementData[index].active=true;
    },
    // 取消数据鼠标滑过状态
    removeActiveEx(vm){
        vm.elementData.map((item,indexs)=>{
            vm.$set(vm.elementData[indexs],'active',false);
            if(item.children&&item.children.length>0){
                item.children.map((itemC,indexC)=>{
                    vm.$set(vm.elementData[indexs].children[indexC],'active',false);
                })
            }
        });
    },
    // 拖动组件定位
    moveExDocument(vm,ev){
        let oevent = ev || event;
        let parentNode = document.getElementById(vm.currentIds[0]);
        let indexs = store.getters.getCurrentItemIndex;
        let compareWidth = 0,compareHeight = 0;
        let pageConfig = store.getters.getPageConfig;
        if(indexs.length==1){
            compareWidth = vm.areaWidth;
            compareHeight = vm.areaHeight ;
        }else if(indexs.length==2){
            let uuid = vm.elementData[indexs[0]].uuid;
            let currentParentNode = document.getElementById(uuid);
            compareWidth = currentParentNode.offsetWidth  - 2;
            compareHeight = currentParentNode.offsetHeight  - 2;
        }
        if(vm.isGroupIn){
            this.areaTestInGroup(vm,parentNode,oevent,compareWidth,compareHeight);
        }else{
            this.areaTestOutGroup(vm,parentNode,oevent,compareWidth,compareHeight);
        }
        this.setLineStyle(vm);
    },
    // 组内成员边界判定
    areaTestInGroup(vm,parentNode,oevent,compareWidth,compareHeight){
        // 左侧超出
        if(parentNode.offsetLeft <= 0){
            parentNode.style.left = 0 + 'px';
        }else{
            if(vm.currentNode.clientX > oevent.clientX){
                parentNode.style.left = oevent.clientX - vm.distanceX - vm.offsetXParent + 'px';
            }
        }
        // 右侧超出
        if(compareWidth-vm.currentTargetWidth <= parentNode.offsetLeft){
            parentNode.style.left = compareWidth-vm.currentTargetWidth + 'px';
        }else{
            // 向右移动
            if(vm.currentNode.clientX < oevent.clientX){
                parentNode.style.left = oevent.clientX - vm.distanceX - vm.offsetXParent + 'px';
            }
        }
        // 底部超出
        if(compareHeight-vm.currentTargetHeight <= parentNode.offsetTop){
            parentNode.style.top = compareHeight-vm.currentTargetHeight + 'px';
        }else{
            // 向下移动
            if(vm.currentNode.clientY < oevent.clientY){
                parentNode.style.top = oevent.clientY - vm.distanceY - vm.offsetYParent + 'px';
            }
        }
        // 顶部超出
        if(parentNode.offsetTop <= 0){
            parentNode.style.top = 0 + 'px';
        }else{
            // 向上移动
            if(vm.currentNode.clientY > oevent.clientY){
                parentNode.style.top = oevent.clientY - vm.distanceY - vm.offsetYParent + 'px';
            }
        }
    },
    // 非组成员边界判定
    areaTestOutGroup(vm,parentNode,oevent,compareWidth,compareHeight){
        let top = '',left = '';
        // 左侧超出
        if(parentNode.offsetLeft <= 0){
            parentNode.style.left = 0 + 'px';
        }else{
            // 向左移动
            if(vm.currentNode.clientX > oevent.clientX){
                parentNode.style.left = oevent.clientX - vm.distanceX + 'px';
            }
        }
        // 右侧超出
        if(compareWidth-vm.currentTargetWidth <= parentNode.offsetLeft){
            parentNode.style.left = compareWidth-vm.currentTargetWidth + 'px';
        }else{
            // 向右移动
            if(vm.currentNode.clientX < oevent.clientX){
                parentNode.style.left = oevent.clientX - vm.distanceX + 'px';
            }
        }
        // 底部超出
        if(compareHeight-vm.currentTargetHeight <= parentNode.offsetTop){
            parentNode.style.top = compareHeight-vm.currentTargetHeight + 'px';
        }else{
            // 向下移动
            if(vm.currentNode.clientY < oevent.clientY){
                parentNode.style.top = oevent.clientY - vm.distanceY + 'px';
            }
        }
        // 顶部超出
        if(parentNode.offsetTop <= 0){
            parentNode.style.top = 0 + 'px';
        }else{
            // 向上移动
            if(vm.currentNode.clientY > oevent.clientY){
                parentNode.style.top = oevent.clientY - vm.distanceY + 'px';
            }
        }
    },
    moveUpExDocument(vm,$event){
        vm.distanceX = 0;
        vm.distanceY = 0;
        vm.offsetXParent = 0;
        vm.offsetYParent= 0;
        vm.currentTargetWidth =0;
        vm.currentTargetHeight =0;
        //卸载事件 
        let target = document.getElementById('workSpace');
        target.removeEventListener("mousemove", vm.moveExDocument,true);
        target.removeEventListener("mouseup", vm.moveUpExDocument,true);
    },
    // 键盘事件
    targetKeyDown(vm,event){
        let e = event || window.event,that = this;
        vm.ctrlKey = e.ctrlKey;
        if (!vm.currentNode) {
            return;
        }
        let parentNode = document.getElementById(vm.currentIds[0]);
        let indexs = store.getters.getCurrentItemIndex;
        let compareWidth = 0,compareHeight = 0;
        if(indexs.length==1){
            compareWidth = vm.areaWidth;
            compareHeight = vm.areaHeight;
        }else if(indexs.length==2){
            let uuid = vm.elementData[indexs[0]].uuid;
            let currentParentNode = document.getElementById(uuid);
            compareWidth = currentParentNode.offsetWidth - 2;
            compareHeight = currentParentNode.offsetHeight - 2;
        }
        switch (e.keyCode) {
            case 37://左
                e.preventDefault();
                if(parentNode.offsetLeft - 1 >= 0){
                    parentNode.style.left =parentNode.offsetLeft - 1 +"px";
                    that.setLineStyle(vm);
                }
                break;
            case 38://上
                e.preventDefault();
                if(parentNode.offsetTop - 1 >= 0){
                    parentNode.style.top = parentNode.offsetTop - 1 +"px";
                    that.setLineStyle(vm);
                }
                break;
            case 39://右
                e.preventDefault();
                if(compareWidth - parentNode.offsetWidth > parentNode.offsetLeft){
                    parentNode.style.left = parentNode.offsetLeft + 1 +"px";
                }else{
                    parentNode.style.left = compareWidth - parentNode.offsetWidth +"px";
                }
                that.setLineStyle(vm);
                break;
            case 40://下
                e.preventDefault();
                if(compareHeight - parentNode.offsetHeight > parentNode.offsetTop){
                    parentNode.style.top = parentNode.offsetTop + 1 +"px";
                }else{
                    parentNode.style.top = compareHeight - parentNode.offsetHeight +"px";
                }
                that.setLineStyle(vm);
                break;
        }
    },
    targetKeyUp(vm,event){
        let e = event || window.event;
        vm.ctrlKey = false;
    },
    documentClick(vm,e){
        if(e.target.className && e.target.className.includes("ex")){
            return
        }
        let exTarget = document.querySelectorAll(".ex");
        let componentConfig = document.getElementById("componentConfig");
        let showIt = false;
        for(let i = 0;i<exTarget.length;i++){
            let item = exTarget[i];
            if(item.contains(e.target) || (componentConfig&&componentConfig.contains(e.target))){
                showIt = true;
            }
        }
        if(!showIt){
            vm.parentNode = null;
            store.dispatch('setCurrentIds', []);
            store.dispatch('setCurrentNode', null);
            store.dispatch('setCurrentItemIndex', []);
        }
    },
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
            vm.$parent.XLineStyle = {width,top};
    
            let height = offHeight * pageConfig.scale + 50 +"px";
            let left = offWidth * pageConfig.scale + 50 +"px";
            vm.$parent.YlineStyle = {height,left};
    
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
    addElementItem(vm,item,x,y){
        let basecData = JSON.parse(JSON.stringify(configData[item.value+"BasicData"]));
        basecData.uuid = vm.uuid() ;
        basecData.index = vm.elementData.length + 1;
        basecData.top = y >=0 ? y : 0;
        basecData.left = x >= 0 ? x : 0;
        vm.elementData.unshift(basecData);
        Bus.$emit('initMenu');
    },
};
export default workSpaceJs;