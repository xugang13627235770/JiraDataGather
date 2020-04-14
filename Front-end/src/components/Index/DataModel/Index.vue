<template>
    <div id="DataModel">
        <!-- 左边 -->
        <div class="leftBox">
            <c-tabs @change="change" :value="0" :justified="true">
                <c-tab header="数据资源">
                    <!-- 资源库 -->
                    <c-accordion  @change="change" :one-at-time="false">
                        <c-panel :is-open="true" index="1" @change="change">
                            <div slot="header" class="title">
                                <i class="iconfont icon-weizhankai"></i>
                                <i class="iconfont icon-shuxiebianji fr"></i>
                                <i class="iconfont icon-shuaxin fr"></i>
                                资源库目录
                            </div>
                            
                        </c-panel>
                        <c-panel index="2" :is-open="true"  style="">
                           <div slot="header" class="title">
                               <i class="iconfont icon-weizhankai"></i>
                                <i class="iconfont icon-shuxiebianji fr"></i>
                                <i class="iconfont icon-shuaxin fr"></i>
                               本地库目录
                            </div>
                        </c-panel>
                    </c-accordion>
                    <!-- 本地库 -->
                </c-tab>
                <c-tab header="结果表">
                    
                </c-tab>
                
            </c-tabs>
        </div>
        <!-- 右边 -->
         <div class="rightBox">
             <!-- 切换按钮 -->
             <div class="btnBox">
                 <c-button-group>
                    <c-button type="primary">面板建模</c-button>
                    <c-button >SQL建模</c-button>
                </c-button-group>
             </div>
             <!-- sql建模 -->
             <div class="content">

             </div>
         </div>
    </div>
</template>
<script>
export default {
    name:"DataModel",
    data(){
        return{
            
            
        }
    },
    methods:{
        change(){}
    },
    mounted(){

    },
}
</script>
<style lang="scss" scoped>
@import "./style.scss";
#DataModel{
    width: 100%;
    height: 100%;
}
</style>