import store from '../../../../vuex/store'
import citmsUI from 'citms-ui-package'
import configData from '../../configData.js'
import api from '../../../../api/api';

const addBoard ={
    upDateDisplayData(vm){
        let dispalyData={};
        let DisplayData = store.getters.getDisplayData;
        let paramData = {
            elementData:JSON.parse(JSON.stringify(vm.elementData)),
            pageConfig : JSON.parse(JSON.stringify(store.getters.getPageConfig))
        };
        paramData.elementData.map((item , index)=>{
            item.index = paramData.elementData.length-index;
            if(item.isGroup){
                item.children.map((childItem , childIndex)=>{
                    childItem.index = item.children.length - childIndex
                })
            }
        })
        paramData.pageConfig.scale = 1;
        if(DisplayData.id){
            dispalyData = JSON.parse(JSON.stringify(DisplayData));
            dispalyData.displayName = paramData.pageConfig.displayName;
            dispalyData.displayData = JSON.stringify(paramData);
        }else{
            dispalyData = {
                displayData:JSON.stringify(paramData),
                displayName:paramData.pageConfig.displayName
            };
        }
        store.dispatch('setDisplayData', dispalyData);
    },
    closeAddBoard(vm){
        vm.$emit('cleanData');
        vm.$emit('close');
    },
    toggleLayer(vm){
        vm.toggle = !vm.toggle;
        store.dispatch('setToggle', vm.toggle);
    },
    toggleConfigPanel(vm){
        store.dispatch('setConfigPanel', !vm.showConfigPanel);
    },
    chooseCompn(vm,item){
        let basecData = JSON.parse(JSON.stringify(configData[item.value+"BasicData"]));
        basecData.uuid = vm.uuid() ;
        basecData.index = vm.elementData.length+1;
        vm.elementData.unshift(basecData);
        this.initMenu(vm)
    },
    initMenu(vm){
        vm.$nextTick(()=>{
            vm.$refs.contextMenu.initMenu();
        });
    },
    // 右键菜单选中事件
    menuChange(vm,item){
        // 1：成组 2：取消组
        if(item.value==1){
            this.subGroup(vm);
        }else if(item.value==2){
            this.splitGroup(vm);
        }else if(item.value==3){
            this.deleteComp(vm);
        }
        this.initMenu(vm)
    },
    subGroup(vm){
        let currentids = store.getters.getCurrentIds;
        let elementData = JSON.parse(JSON.stringify(vm.elementData));
        let groupInData = elementData.filter(item=>currentids.indexOf(item.uuid)!=-1);
        let groupOutData = elementData.filter(item=>currentids.indexOf(item.uuid)==-1);
        let lefts = [],tops = [],newElementData = [];
        currentids.map(item=>{
            let element = document.getElementById(item);
            lefts.push(element.offsetLeft,element.offsetLeft+element.offsetWidth);
            tops.push(element.offsetTop,element.offsetTop+element.offsetHeight);
        });
        lefts.sort((a,b)=>{return a-b});
        tops.sort((a,b)=>{return a-b});
        let newObj = configData['groupBasicData'];
        let newObjExtend={
            width:lefts[lefts.length-1] - lefts[0] + 2,
            height:tops[tops.length-1] - tops[0]  + 2,
            left:lefts[0],
            top:tops[0],
            uuid:vm.uuid(),
        };
        Object.assign(newObj,newObjExtend);
        groupInData.map(item=>{
            let element = document.getElementById(item.uuid);
            item.left = element.offsetLeft - newObj.left;
            item.top = element.offsetTop - newObj.top;
        });
        newObj['children'] = JSON.parse(JSON.stringify(groupInData));
        newElementData.push(newObj);
        newElementData = newElementData.concat(groupOutData);
        newElementData = this.upDateIndex(newElementData);
        vm.elementData = JSON.parse(JSON.stringify(newElementData));
    },
    splitGroup(vm){
        let currentIndexs = store.getters.getCurrentItemIndex;
        let elementData = JSON.parse(JSON.stringify(vm.elementData));
        let currentids = elementData[currentIndexs[0]].uuid;
        let groupInData = elementData.filter(item=>currentids.indexOf(item.uuid)!=-1)[0];
        let groupOutData = elementData.filter(item=>currentids.indexOf(item.uuid)==-1);
        let parentNode = document.getElementById(groupInData.uuid);
        let newElementData = [];
        groupInData.children.map(item=>{
            item.left += parentNode.offsetLeft;
            item.top += parentNode.offsetTop;
            newElementData.push(item);
        })
        newElementData = newElementData.concat(groupOutData);
        newElementData = this.upDateIndex(newElementData);
        vm.$set(vm,'elementData',newElementData);
    },
    deleteComp(vm){
        // vm.cModal.confirm({
        //     title: '提示信息',
        //     message: '确认删除吗 ?',
        //     effect: 'zoom',
        //     type: 'warning',
        //     onConfirm () {
        //       console.log('点击确定按钮时触发')
        //     },
        // })
        let currentids = store.getters.getCurrentIds;
        let currentIndexs = store.getters.getCurrentItemIndex;
        // let elementData = JSON.parse(JSON.stringify(vm.elementData));
        if(currentIndexs.length==0&&currentids.length>1){

            return
        }
        if(currentIndexs.length==1&&currentids.length==1){
            store.dispatch('setCurrentIds', []);
            store.dispatch('setCurrentNode', null);
            store.dispatch('setCurrentItemIndex', []);
            vm.elementData.splice(currentIndexs,1);
            vm.elementData = this.upDateIndex(vm.elementData);
            return
        }
        if(currentIndexs.length==2&&currentids.length==1){
            store.dispatch('setCurrentIds', []);
            store.dispatch('setCurrentNode', null);
            store.dispatch('setCurrentItemIndex', []);
            vm.elementData[currentIndexs[0]].children.splice(currentIndexs[1],1);
            vm.elementData = this.upDateIndex(vm.elementData);
            return
        }
    },
    saveBoard(vm){
        let pageConfigForm = vm.$refs.CubConfig.$refs.pageConfig.$refs.pageConfigForm;
        pageConfigForm.validateFields(result => {
            if(result.isvalid){
                let currentDisplayData = store.getters.getDisplayData;
                store.dispatch('setLoading', true);
                if(currentDisplayData.id){
                    this.updateFun(vm,currentDisplayData);
                }else{
                    this.saveFun(vm,currentDisplayData);
                }
            }
        })
    },
    saveFun(vm,dispalyData){
        api.saveDisplay(dispalyData).then(res=>{
            store.dispatch('setLoading', false);
            if(res.code==0){
                citmsUI.cAlertMethod({
                    content: res.message || '保存成功！',
                    type: 'success',
                    placement: 'top',
                    duration: 2000,
                    width:'220px'
                });
                vm.closeAddBoard();
                vm.$emit('search');
            }else{
                citmsUI.cAlertMethod({
                    content: res.message || '请求出错！',
                    type: 'warning',
                    placement: 'top',
                    duration: 2000,
                    width:'220px'
                })
            }
        })
    },
    updateFun(vm,dispalyData){
        api.updateDisplay(dispalyData).then(res=>{
            store.dispatch('setLoading', false);
            if(res.code==0){
                citmsUI.cAlertMethod({
                    content: '更新成功！',
                    type: 'success',
                    placement: 'top',
                    duration: 2000,
                    width:'220px'
                });
                vm.closeAddBoard();
                vm.$emit('search');
            }else{
                citmsUI.cAlertMethod({
                    content: res.message || '请求出错！',
                    type: 'warning',
                    placement: 'top',
                    duration: 2000,
                    width:'220px'
                })
            }
        })
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
    dragstart(vm,event, data){
        // image.style.width='30px';
        // image.style.height='30px';
        store.dispatch('setSpaceMask', true);
        event.dataTransfer.effectAllowed = "move";
        event.dataTransfer.setData('item', JSON.stringify(data));
        let img = document.getElementById('img'+data.value).cloneNode(true);
        event.dataTransfer.setDragImage(img, 0, 0);
    },
    dragend(vm,event){
        store.dispatch('setSpaceMask', false);
        event.dataTransfer.clearData();
    },
};
export default addBoard;