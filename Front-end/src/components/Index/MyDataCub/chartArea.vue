<template>
    <div id="chartArea">
        <pie ref="pie"  v-if="type=='pie'" :data='data'></pie>
        <line-chart ref="lineChart" v-if="type=='line'" :data='data'></line-chart>
        <bar ref="bar" v-if="type=='bar'" :data='data'></bar>
        <text-cloud ref="textCloud" v-if="type=='textCloud'" :data='data'></text-cloud>
        <text-lamp ref="textLamp" v-if="type=='textLamp'" :data='data'></text-lamp>
        <timer ref="timer" v-if="type=='timer'" :data='data'></timer>
        <pie-progress ref="pieProgress" v-if="type=='pieProgress'" :data='data'></pie-progress>
        <image-comp ref="imageComp" v-if="type=='imageComp'" :data='data'></image-comp>
        <video-comp ref="videoComp" v-if="type=='videoComp'" :data='data'></video-comp>
        <number-animate ref="numberAnimate" v-if="type=='numberAnimate'" :data='data'></number-animate>
        <scroll-grid ref="scrollGrid" v-if="type=='scrollGrid'" :data='data'></scroll-grid>
        <!-- 装饰组件 -->
        <border-tr ref="borderTr" v-if="type=='borderTr'"></border-tr>
        <border-tl ref="borderTl" v-if="type=='borderTl'"></border-tl>
        <border-lb ref="borderLb" v-if="type=='borderLb'"></border-lb>
        <border-rb ref="borderRb" v-if="type=='borderRb'"></border-rb>
        <ferris-wheel ref="ferrisWheel" v-if="type=='ferrisWheel'"></ferris-wheel>
    </div>
</template>
<script>

import pie from '../partUI/pie'
import lineChart from '../partUI/line'
import bar from '../partUI/bar'
import textCloud from '../partUI/textCloud'
import textLamp from '../partUI/textLamp'
import timer from '../partUI/timer'
import pieProgress from '../partUI/pieProgress'
import imageComp from '../partUI/imageComp'
import videoComp from '../partUI/videoComp'
import numberAnimate from '../partUI/numberAnimate'
import scrollGrid from '../partUI/scrollGrid'
import borderTr from '../partUI/borderTR'
import borderTl from '../partUI/borderTL'
import borderLb from '../partUI/borderLB'
import borderRb from '../partUI/borderRB'
import ferrisWheel from '../partUI/ferrisWheel'
export default {
    name:"chartArea",
    props:{
        type:{
            type:String,
            default:""
        },
        data:{
            type:Object,
            default(){return {}}
        },
    },
    components:{pie,lineChart,bar,textCloud,textLamp,timer,pieProgress,imageComp,videoComp,numberAnimate,scrollGrid,borderTr,borderTl,borderLb,borderRb,ferrisWheel},
    data(){
        return{

        }
    },
    methods:{

    },
    mounted(){

    },
}
</script>
<style lang="scss" scoped>
#chartArea{
    width:100%;
    height: 100%;
}
</style>