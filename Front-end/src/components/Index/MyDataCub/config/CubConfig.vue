<template>
    <div id="CubConfig" class="bordered block-bg">
        <!-- 组件设置 -->
        <div class="componentConfig" v-if="!isPageConfig">
            <component-config ref="componentConfig"></component-config>
        </div>
        <!-- 页面设置 -->
        <div class="pageConfig" v-show="isPageConfig">
            <page-config ref="pageConfig"></page-config>
        </div>
        <div class="controlBtn">
            <c-button type='linear' @click.native="saveFun">保存</c-button>
        </div>
    </div>
</template>
<script>
import store from '../../../../vuex/store'
import pageConfig from './pageConfig'
import componentConfig from './componentConfig'
export default {
    name:"CubConfig",
    components:{pageConfig,componentConfig},
    props:{
        
    },
    data(){
        return{
            isPageConfig:true,
        }
    },
    computed: {
        currentNode(){
            return store.getters.getCurrentNode;
        },
    },
    watch:{
        currentNode:{
            deep:true,
            handler(){
                this.isPageConfig = this.currentNode?false:true;
            },
        },
    },
    methods:{
        saveFun(){
            this.$emit('toggle');
        },
    },
    mounted(){

    },
}
</script>
<style lang="scss" scoped>
#CubConfig{
    width: 400px;
    height: 100%;
    position: absolute;
    right: 0;
    top: 0;
    z-index: 99;
    .componentConfig{
        width:100%;
        height: calc(100% - 50px);
    }
    .pageConfig{
        width: 100%;
        height: calc(100% - 50px);
    }
    .controlBtn{
        width: 100%;
        height: 50px;
        text-align: center;
        padding: 10px 0;
    }
}
</style>
<style lang="scss">
#CubConfig{
    .configTab{
        .icon-shezhi{
            font-size: 12px !important;
        }
    }
}
</style>