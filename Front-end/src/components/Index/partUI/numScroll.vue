<template>
    <div class="numScroll" :style="nameScrollStyle">
        <div class="listNum listNumOne" v-show="oneShow" :style="[listOneStyle,listNumOneStyle]">
            <ul>
                <li :style="liStyle" v-for="(item , index) in numbers" :key="index">
                    <span class="liNum">{{item}}</span>
                    <span class="liTop"></span>
                    <span class="liBottom"></span>
                </li>
            </ul>
        </div>
        <div class="listNum listNumTwo" v-show="twoShow" :style="[listTwoStyle,listNumTwoStyle]">
            <ul>
                <li :style="liStyle" v-for="(item , index) in numbers" :key="index">
                    <span class="liNum">{{item}}</span>
                    <span class="liTop"></span>
                    <span class="liBottom"></span>
                </li>
            </ul>
        </div>
    </div>
</template>

<script type="text/ecmascript-6">
  import configData from '../configData.js'
  export default {
    name: 'numScroll',
    props:{
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
            default:50,
        }
    },
    data () {
      return {
        numbers:[0,1,2,3,4,5,6,7,8,9],
        difference:0,
        timer1:null,
        ticket:0,
        nowNums:0,
        beginVal:0,
        endVal:0,
        liStyle:{
            height:'18px',
            lineHeight:'18px'
        },
        liStyleHeight:0,
        listOneStyle:{
            top:'0px'
        },
        listTwoStyle:{
            top:'0px'
        },
        nameScrollStyle:{
            width:'0px',
            height:'0px',
        },
        activeOne:true,
        oneShow:true,
        twoShow:false,
        listNumOneStyle:{transition:'top  0.5s linear'},
        listNumTwoStyle:{transition:'top  0.5s linear'},
      }
    },
    watch:{
        begin(val){
            this.beginVal = val;
        },
        end(val){
            this.endVal=val;
            this.run();
        },
    },
    methods:{
        initNum(){
            let vm = this;
            this.beginVal = JSON.parse(JSON.stringify(this.begin))
            this.endVal = JSON.parse(JSON.stringify(this.end))
            this.listOneStyle.top = '-'+(this.beginVal * vm.liStyleHeight)+"px";
            let endTop = (9-this.beginVal) * vm.liStyleHeight;
            this.listTwoStyle.top = endTop+"px";
        },
        run(){
            let vm = this;
            vm.twoShow = true;
            if(vm.endVal<vm.beginVal){
                let nums = 9 - this.beginVal;
                vm.difference = this.endVal + nums + 1;
            }else{
                vm.difference = vm.endVal - vm.beginVal;//要执行动画的次数
            }
            if(this.activeOne){
                //第一个在动
                let numCopy = JSON.parse(JSON.stringify(this.listOneStyle));
                let beginCopy =Math.abs(parseInt(numCopy.top)) / vm.liStyleHeight;
                if(beginCopy+vm.difference<=9){
                    setTimeout(()=>{
                        vm.listOneStyle.top = '-'+vm.liStyleHeight * Number(beginCopy+vm.difference)+"px";
                        vm.listTwoStyle.top =(parseInt(vm.listOneStyle.top) + (10 * vm.liStyleHeight))+"px";
                        
                        vm.beginVal = JSON.parse(JSON.stringify(vm.endVal))
                    },)
                }else{
                    let oneNum = 9 - beginCopy;
                    vm.listOneStyle.top = '-'+vm.liStyleHeight * Number(beginCopy+vm.difference)+"px"; 
                    vm.listTwoStyle.top ="-"+ ((vm.difference - oneNum -1) * vm.liStyleHeight) +"px";
                    setTimeout(()=>{
                        vm.oneShow = false;
                        vm.listOneStyle.top = (vm.liStyleHeight*vm.numbers.length - Math.abs(parseInt(vm.listTwoStyle.top))) +"px";
                        vm.beginVal = JSON.parse(JSON.stringify(vm.endVal))
                        setTimeout(()=>{
                            vm.oneShow = true;
                            vm.activeOne = false;
                        },500)
                    },500);
                }
            }else{
                //第二个在动
                let numCopyTwo = JSON.parse(JSON.stringify(vm.listTwoStyle));
                let beginCopyTwo =Math.abs(parseInt(numCopyTwo.top)) / vm.liStyleHeight;
                if(beginCopyTwo+vm.difference<=9){
                    setTimeout(()=>{
                        vm.listTwoStyle.top = '-'+vm.liStyleHeight * Number(beginCopyTwo+vm.difference)+"px";
                        vm.listOneStyle.top =(parseInt(vm.listTwoStyle.top) + (10 * vm.liStyleHeight))+"px";
                        vm.beginVal = JSON.parse(JSON.stringify(vm.endVal))
                    },)
                }else{
                    let oneNumCopy = 9 - beginCopyTwo;
                    vm.listTwoStyle.top = '-'+vm.liStyleHeight * Number(beginCopyTwo+vm.difference)+"px"; 
                    vm.listOneStyle.top ="-"+ ((vm.difference - oneNumCopy-1) * vm.liStyleHeight) +"px";
                    setTimeout(()=>{
                        vm.twoShow = false;
                        vm.listTwoStyle.top = (vm.liStyleHeight*vm.numbers.length - Math.abs(parseInt(vm.listOneStyle.top))) +"px";
                        vm.beginVal = JSON.parse(JSON.stringify(vm.endVal))
                        setTimeout(()=>{
                            vm.twoShow = true;
                            this.activeOne = true;
                        },500)
                    },500);
                }
            }
        },
        initStyle(){
            let vm = this;
            vm.nameScrollStyle = {width:vm.width+"px",height:vm.height+"px"};
            vm.liStyleHeight = vm.height; 
            vm.liStyle.height =  vm.height+"px";
            vm.liStyle.lineHeight= vm.height+"px";
        },
    },
    mounted(){
        let vm =this;
        this.$nextTick(()=>{
            vm.initStyle();
            vm.initNum();
            vm.run();
            window.addEventListener('resize',function(){
                vm.initStyle();
                vm.initNum();
                vm.run();
            });
        })
    }
  }
</script>
<style scoped lang='scss'>
.numScroll{
    overflow: hidden;
    position: relative;
    display:inline-block;
    .listNum{
        width: 100%;
        min-width: 10px;
        overflow: hidden;
        position: absolute;
        top: 0;
        left: 0;
        z-index: 999;
        li {
            display: block;
            width: calc(100% - 0px);
            height: 60px;
            line-height: 60px;
            text-align: center;
            color: #fff;
            font-size: 45px;
            position: relative;
            .liTop,
            .liBottom{
                float: left;
                width: 100%;
                height: 50%;
                display: block;
            }
            .liNum{
                position: absolute;
                top:50%;
                left: 50%;
                transform: translate(-50% , -50%);
                z-index: 999;
            }
            .liTop{
                // background: #237CFA !important;
            }
            .liBottom{
                // background: linear-gradient(rgba(21,76,153,.8),#237CFA) !important;
            }
            .liTop{
                // background: #FD7829 !important;
            }
            .liBottom{
                // background: linear-gradient(rgba(179,85,29,.8),#FD7829) !important;
            }
        }
    }
}
// #numScroll .listNum{
//     width: 100%;
//     min-width: 10px;
//     overflow: hidden;
//     position: absolute;
//     top: 0;
//     left: 0;
//     z-index: 999;
// }
// #numScroll .listNum li {
//     display: block;
//     width: calc(100% - 0px);
//     height: 60px;
//     line-height: 60px;
//     text-align: center;
//     color: #fff;
//     font-size: 45px;
//     position: relative;
// }
// #numScroll .listNum li .liTop,
// #numScroll .listNum li .liBottom{
//     float: left;
//     width: 100%;
//     height: 50%;
//     display: block;
// }
// #numScroll .listNum li .liNum{
//     position: absolute;
//     top:50%;
//     left: 50%;
//     transform: translate(-50% , -50%);
//     z-index: 999;
// }
// #numScroll .listNum li .liTop{
//     background: #237CFA !important;
// }
// #numScroll .listNum li .liBottom{
//     background: linear-gradient(rgba(21,76,153,.8),#237CFA) !important;
// }
// #numScroll .listNum li .liTop{
//     background: #FD7829 !important;
// }
// #numScroll .listNum li .liBottom{
//     background: linear-gradient(rgba(179,85,29,.8),#FD7829) !important;
// }
/* @font-face{
    font-family: 'number';
    src: url('../../static/font/UnidreamLED.ttf');
} */
/* #numScroll .listNum li .liNum{
    font-family: 'number'
} */
</style>
