<template>
    <div class="dataStatistics" id="dataStatistics" :style="dataStatisticsStyle">
      <div class="digit_set">
        <div class="digit" v-if="nowNums===0" :class="[nowNums-1==-1?'previous':'']">
            <div class="digit_top">
				<span class="digit_wrap" :style="digitWrapStyle">9</span>
			</div>
			<div class="shadow_top"></div>
			<div class="digit_bottom">
				<span class="digit_wrap" :style="[digitWrapStyle,digitBottomStyle]">9</span>
			</div>
			<div class="shadow_bottom"></div>
        </div>
        <div class="digit" v-for="(items , indexs) in numbers" :key="indexs" :class="[indexs==nowNums?'active':'',indexs==nowNums-1?'previous':'']">
            <div class="digit_top">
				<span class="digit_wrap" :style="digitWrapStyle">{{items}}</span>
			</div>
			<div class="shadow_top"></div>
			<div class="digit_bottom">
				<span class="digit_wrap" :style="[digitWrapStyle,digitBottomStyle]">{{items}}</span>
			</div>
			<div class="shadow_bottom"></div>
        </div>
      </div>
    </div>
</template>

<script type="text/ecmascript-6">
  import '../../../../static/styles/styleScroll.css'
  export default {
    name: 'numTransform',
    props:{
        begin:{
            type:Number,
            default:0,
        },
        end:{
            type:Number,
            default:5,
        },
        //时长 毫秒
        time:{
            type:Number,
            default:2000,
        },
        width:{
            type:Number,
            default:40,
        },
        height:{
            type:Number,
            default:50,
        }
    },
    data () {
      return {
        numbers:[0,1,2,3,4,5,6,7,8,9],
        len:1, //数字是几位数
        difference:0,
        timer1:null,
        ticket:0,
        nowNums:0,
        beginVal:0,
        endVal:0,
        digitWrapStyle:{
            lineHeight:'46px'
        },
        digitBottomStyle:{
            marginTop:"-23px"
        },
        dataStatisticsStyle:{
            width:'0px',
            height:'0px',
        },
      }
    },
    watch:{
        begin(val){
            this.beginVal = val;
        },
        end(val){
            this.endVal=val;
            this.run();
        }
    },
    methods:{
        zfill(num, size){
            var s = "000000000" + num;
            this.nowNums = parseInt((s.substr(s.length-size)).toString());
        },
        run(){
            let vm = this;
            if(this.endVal>=this.beginVal){
            this.difference =this.endVal-this.beginVal;//要执行动画的次数
            }else{
            let calcNum = 9 - this.beginVal;
            this.difference = this.endVal + calcNum + 1;
            }
            //每次要执行动画的时间
            this.ticket = this.time/this.difference;
            vm.timer1 = setInterval(vm.increase,vm.ticket);
        },
        increase(){
            let vm = this;
            //执行次数为0时,停止执行
            if (this.difference<1) {
                this.beginVal = JSON.parse(JSON.stringify(this.endVal))
                clearInterval(vm.timer1);
                return false;
            }
            this.difference--;
            //翻页动画
            this.nowNums =1+this.nowNums;
            if(this.nowNums>9){
            this.nowNums=0;
            }
        },
        initNum(){
            this.beginVal = JSON.parse(JSON.stringify(this.begin))
            this.endVal = JSON.parse(JSON.stringify(this.end))
        },
        initStyle(){
            let vm = this;
            vm.dataStatisticsStyle = {width:vm.width+"px",height:vm.height+"px"};
            let digit_setHeight =  vm.height -2 ;
            vm.digitWrapStyle.lineHeight = vm.height+"px";
            vm.digitBottomStyle.marginTop = "-"+ Math.ceil((vm.height / 2))+"px";
        },
    },
    mounted(){
        let vm =this;
        this.$nextTick(()=>{
            vm.initStyle();
            vm.initNum();
            vm.zfill(this.beginVal, this.len);
            vm.run();
            window.addEventListener('resize',function(){
                vm.initStyle();
            })
        })
    }
  }
</script>
<style scoped>
.hello{
    width: 100%;
    height: 100%;
}
/* @media screen and (max-width: 1690px){
    .dataStatistics#dataStatistics{
        font-size: 35px !important;
    }
}
@media screen and (max-width: 1490px){
    .dataStatistics#dataStatistics{
        font-size: 23px !important;
    }
} */
</style>

