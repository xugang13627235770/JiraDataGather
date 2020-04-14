import api from '../../../../api/api.js'
import store from '../../../../vuex/store'
import citmsUI from 'citms-ui-package'

const dataBoard={
    addBoard(vm){
        vm.$refs.addBoardModel.open();
    },
    closeAddBoard(vm){
        vm.showAddBoard = false;
    },
    starAdd(vm){
        let that = this;
        vm.$refs.addBoardModelForm.validateFields(result => {
            if(result.isvalid){
                let pageConfig = store.getters.getPageConfig;
                Object.assign(pageConfig,vm.model);
                that.calcelAdd(vm);
                that.upDateDisplayData(vm);
                vm.showAddBoard = true;
                store.dispatch('setCurrentMode', 1);
            }
        })
    },
    calcelAdd(vm){
        vm.$refs.addBoardModelForm.$emit("closeValidate");
        vm.model = {width:1920,height:1080};
        vm.$refs.addBoardModel.close();
    },
    upDateDisplayData(vm){
        let paramData = {
            elementData:[],
            pageConfig : JSON.parse(JSON.stringify(store.getters.getPageConfig))
        };
        let dispalyData = {
            displayData:JSON.stringify(paramData),
            displayName:paramData.pageConfig.displayName
        };
        store.dispatch('setDisplayData', dispalyData);
    },
    getDeviceByDepater(vm){
        store.dispatch('setLoading', true);
        api.searchDisplayList({}).then(res=>{
            store.dispatch('setLoading', false);
            if(res.code==0){
                vm.boardData = res.result;
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
    deleteBoard(vm,item){
        vm.cModal.confirm({
            title: '提示信息',
            message: '确定删除吗？',
            effect: 'zoom',
            type: 'danger',
            width:"240px",
            onConfirm () {
                api.deleteDisplay([item.id]).then(res=>{
                    if(res.code==0){
                        citmsUI.cAlertMethod({
                            content: '删除成功!',
                            type: 'success',
                            placement: 'top',
                            duration: 2000,
                            width:'220px'
                        });
                        vm.getDeviceByDepater();
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
        })
    },
    editBoard(vm,item){
        vm.showAddBoard = true;
        store.dispatch('setCurrentMode', 2);
        vm.elementData = item.displayData?JSON.parse(item.displayData).elementData:[];
        store.dispatch('setElementData', vm.elementData);
        store.dispatch('setDisplayData',JSON.parse(JSON.stringify(item)));
        store.dispatch('setPageConfig', JSON.parse(JSON.parse(JSON.stringify(item)).displayData).pageConfig);
    },
    cleanData(vm){
        store.dispatch('setCurrentMode', 1);
        store.dispatch('setPageConfig', JSON.parse(JSON.stringify(store.getters.getPageConfigCopy)));
        vm.elementData = [];
        store.dispatch('setDisplayData',{});
        store.dispatch('setElementData',[]);
    },
    preViewFun(vm,item,form){
        vm.source = form;
        vm.showPreView = true;
        if(vm.source=='in'){
            
        }else{
            vm.elementData = JSON.parse(item.displayData).elementData;
            let itemCopy = JSON.parse(JSON.stringify(item));
            store.dispatch('setDisplayData',itemCopy);
            store.dispatch('setPageConfig', JSON.parse(itemCopy.displayData).pageConfig);
            store.dispatch('setElementData', vm.elementData);
        }
        store.dispatch('setCurrentMode', 3);
    },
    closePreView(vm){
        vm.showPreView = false;
        store.dispatch('setCurrentMode', 1);
        if(vm.source=='out'){
            vm.elementData = [];
            store.dispatch('setElementData', []);
            store.dispatch('setDisplayData',{});
            store.dispatch('setPageConfig', JSON.parse(JSON.stringify(store.getters.getPageConfigCopy)));
        }
        vm.source = '';
    },
    publishDisplay(vm,item){
        if(item.isPublish==1){
            let {foreignUrl,passWord,date} = {
                foreignUrl:location.origin+"/#/app/cdw/externalLink?id="+item.foreignUrl+"&pw="+item.passWord,
                passWord:item.passWord,
                date:item.startTime?(item.startTime+" ~ "+item.endTime):''
            };
            Object.assign(vm.publishData,{foreignUrl,passWord,date});
        }
        vm.currentBoardData = item;
        vm.$refs.publishModel.open();
    },
    publishSave(vm){
        let paramData = {
            isPublish:1,
            id:vm.currentBoardData.id,
        };
        if(vm.publishData.date){
            let dateArr = vm.publishData.date.split('~');
            paramData.startTime = dateArr[0];
            paramData.endTime = dateArr[1];
        }
        if(vm.publishData.uuid){
            paramData.isForeign =1;
            paramData.foreignUrl =vm.publishData.uuid;
        }
        if(vm.publishData.passWord){
            paramData.passWord =vm.publishData.passWord;
        }
        api.publishDisplay(paramData).then(res=>{
            if(res.code==0){
                citmsUI.cAlertMethod({
                    content: '发布成功!',
                    type: 'success',
                    placement: 'top',
                    duration: 2000,
                    width:'220px'
                });
                vm.canCopy = true;
                vm.$set(vm.publishData,'foreignUrl',location.origin+"/#/app/cdw/externalLink?id="+vm.publishData.uuid+"&pw="+paramData.passWord);
                // vm.calcelPublish();
                // vm.getDeviceByDepater();
            }else{
                citmsUI.cAlertMethod({
                    content: res.message || '发布失败！',
                    type: 'warning',
                    placement: 'top',
                    duration: 2000,
                    width:'220px'
                })
            }
        })
    },
    calcelPublish(vm){
        vm.canCopy = false;
        vm.publishData={foreignUrl:'',passWord:"",date:""};
        vm.getDeviceByDepater();
        vm.showPassWord=false;
        vm.currentBoardData={};
        vm.$refs.publishModel.close();
    },
    getUrl(vm){
        api.getUuid().then(res=>{
            if(res.code==0){
                citmsUI.cAlertMethod({
                    content: '获取链接成功!',
                    type: 'success',
                    placement: 'top',
                    duration: 2000,
                    width:'220px'
                });
                // vm.$set(vm.publishData,'foreignUrl','/dcm/dcmDisplay/foreign/'+res.result)
                vm.$set(vm.publishData,'foreignUrl',location.origin+"/#/app/cdw/externalLink?id="+res.result);
                vm.$set(vm.publishData,'uuid',res.result);
            }else{
                citmsUI.cAlertMethod({
                    content: res.message || '获取链接失败！',
                    type: 'warning',
                    placement: 'top',
                    duration: 2000,
                    width:'220px'
                })
            }
        })
    },
    onCopy(vm,e){
        citmsUI.cAlertMethod({
            content: '复制成功!',
            type: 'success',
            placement: 'top',
            duration: 2000,
            width:'220px'
        });
    },
    onError(vm,e){
        citmsUI.cAlertMethod({
            content: '复制失败！',
            type: 'warning',
            placement: 'top',
            duration: 2000,
            width:'220px'
        });
    },
};

export default dataBoard;