<template>
    <div id="previewMode" :class="{'preClass':currentMode==3}">
        <work-space ref="workSpace"></work-space>
        <c-icon type='times' id="closePreView" @click.native="closePreView" v-if="!isExternalLink"></c-icon>
    </div>
</template>
<script>
import store from '../../../vuex/store'
import workSpace from './workSpace'
import * as tool from '../../../util/tools'
import api from '../../../api/api'
export default {
    name:"previewMode",
    components:{workSpace},
    computed: {
        currentMode(){
            return store.getters.getCurrentMode;
        },
    },
    data(){
        return{
            isExternalLink:false,
            currentId:'',
        }
    },
    methods:{
        closePreView(){
            this.$emit('closePreView');
        },
        // 获取数据
        foreignData(id,pw){
            let vm = this;
            api.foreignData(id,pw).then(res=>{
                if(res.code==0){
                    let item = res.result;
                    // 外链访问
                    let itemCopy = JSON.parse(JSON.stringify(item));
                    store.dispatch('setDisplayData',itemCopy);
                    store.dispatch('setElementData',JSON.parse(itemCopy.displayData).elementData);
                    store.dispatch('setPageConfig', JSON.parse(itemCopy.displayData).pageConfig);
                    store.dispatch('setCurrentMode', 3);
                    vm.isExternalLink = true;
                }
            })
        },
    },
    mounted(){
        let vm = this;
        vm.$nextTick(()=>{
            let id = tool.getQueryString('id');
            let pw = tool.getQueryString('pw');
            if(id){
                vm.foreignData(id,pw);
            }else{
                // setTimeout(()=>{
                //     window.close();
                // },3000);
            }
        })
    },
}
</script>
<style lang="scss" scoped>
#previewMode{
    width: 100%;
    height: 100%;
    position: fixed;
    top: 0;
    left: 0;
    z-index: 9999;
    overflow: auto;
    &.preClass{
        overflow: hidden;
    }
    #workArea{
        width: 100% !important;
    }
    #closePreView{
        position: fixed;
        bottom:20px;
        right: 20px;
        z-index: 999;
        color: #fff;
        opacity: .3;
        cursor: pointer;
        font-size: 26px;
        transition: opacity .5s;
        animation: scaleAnimation .5s infinite;
        &:hover {
            opacity: .8;
        }
        @keyframes scaleAnimation {
            0% {
                transform: scale(1);
                opacity: .3;
            }

            50% {
                transform: scale(1.1);
                opacity: .8;
            }

            100% {
                transform: scale(1);
                opacity: .3;
            }
        }
    }
}
</style>
<style lang="scss">
#previewMode{

}
</style>