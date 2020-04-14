<template>
    <div id="numAnimation">
        <div class="textRemark">
            这里是描述
        </div>
        <div class="numArea">
            <span class="numAreaPonit">£</span>
            <num-scroll v-if="animate==1" :begin='begin' :end='parseInt(item)' :time='time' :width='width' :height='height' v-for="(item , index) in scrollNum" :key='index'></num-scroll>
            <num-transform v-if="animate!=1" :begin='begin' :end='end' :time='time' :width='width' :height='height'></num-transform>
            <span class="SymbolText">元</span>
        </div>
    </div>
</template>
<script>
import configData from '../configData.js'
import numScroll from './numScroll.vue'
import numTransform from './numTransform.vue'
export default {
    name:"numberAnimation",
    components:{
        numScroll,
        numTransform,
    },
    props:{
        data:{
            type:Object,
            default(){
                return { }
            }
        },
        animate:{
            type:Number,
            default:1,
        },
        begin:{
            type:Number,
            default:0,
        },
        end:{
            type:Number,
            default:9,
        },
        //时长 毫秒
        time:{
            type:Number,
            default:2000,
        },
        width:{
            type:Number,
            default:15,
        },
        height:{
            type:Number,
            default:30,
        }
    },
    data(){
        return{
            scrollNum:[],
        }
    },
    watch:{
        data:{
            deep:true,
            handler(cur){
                this.scrollNum = this.data.seriesData.split('');
            }
        },
    },
    methods:{

    },
    mounted(){

    },
}
</script>
<style lang="scss" scoped>
#numAnimation{
    .textRemark{
        line-height: 30px;
        // padding: 0 3px;
    }
    .numArea{
        >span{
            line-height: 30px;
            display: inline-block;
            vertical-align: top;
            &.numAreaPonit{
                font-size: 30px;
            }
            &.SymbolText{
                vertical-align: top;
                vertical-align: bottom;
                font-size: 16px;
            }
        }
    }
}
</style>
