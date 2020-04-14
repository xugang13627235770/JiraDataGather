<template>
    <div id="textLamp">
        <div class="box">
            <div class="content noselect">
                <p class="textLamp" v-for="(item , index) in copy" :key="index" :style="lampStyle"
                :class="{'padding':item==1&&data.lampSwitch}">{{data.textContent}}</p>
            </div>
        </div>
    </div>
</template>
<script>
import store from '../../../vuex/store'
import configData from '../configData.js'
export default {
    name:"textLamp",
    props:{
      data:{
        type:Object,
        default(){
          return { }
        }
      },
    },
    data(){
        return{
            timer:null,
            textWidth:0,
            boxWidth:0,
            box:null,
            content:null,
            text:null,
            innerHTML:'',
            copy:1,
            lampStyle:{},
        }
    },
    watch:{
        data:{
            deep:true,
            immediate: true,
            handler(cur){
                let vm = this;
                if(vm.box){
                    vm.stopLamp();
                }
                vm.$nextTick(()=>{
                    vm.init();
                })
                vm.$set(vm,'lampStyle',{'letterSpacing':vm.data.letterSpacing+'px','fontWeight':vm.data.basicfontWeight,'fontSize':vm.data.basicFontSize+"px",'color':vm.data.basicFontColor});
            },
        },
        'data.lampSwitch':function(nweVal,oldVew){
            let vm = this;
            if(nweVal){
                vm.$nextTick(()=>{
                    vm.init();
                })
            }else{
                vm.stopLamp();
            }
        },
    },
    computed: {
        
    },
    methods:{
        checkScrollLeft(){
            let vm = this;
            let textLamp = document.getElementsByClassName('textLamp');
            // 判断文字长度是否大于盒子长度
            if(vm.boxWidth > vm.textWidth){ return false};
            vm.copy = 2 ;
            // 更新
            vm.textWidth = textLamp[textLamp.length-1].offsetWidth;
            vm.timer = requestAnimationFrame(function toScrollLeft(){
                //  如果文字长度大于滚动条距离，则递归拖动
                if(vm.textWidth > vm.box.scrollLeft){
                    vm.box.scrollLeft+= vm.data.scrollSpeed;
                }else{
                    vm.box.scrollLeft = 0;
                }
                if(vm.copy == 2){
                    vm.timer = requestAnimationFrame(toScrollLeft);
                }
            }); 
        },
        init(){
            let vm = this;
            let boxs = document.getElementsByClassName('box');
            let contents = document.getElementsByClassName('content');
            let textLamp = document.getElementsByClassName('textLamp');
            [vm.box, vm.content] = [boxs[boxs.length-1],contents[contents.length-1]];
            if(!vm.innerHTML){
                vm.innerHTML = vm.content.innerHTML;
            }
            [vm.textWidth, vm.boxWidth] = [textLamp[textLamp.length-1].offsetWidth, vm.box.offsetWidth];
            if(vm.data.lampSwitch){
                vm.checkScrollLeft();
            }
        },
        stopLamp(){
            let vm = this;
            vm.copy =1;
            vm.box.scrollLeft = 0;
            cancelAnimationFrame(vm.timer);
        },
    },
    mounted(){
        let vm = this;
        vm.$nextTick(()=>{
            
        })
    },
}
</script>
<style lang="scss" scoped>
#textLamp{
    width: 100%;
    height: 100%;
    position: relative;
    .box{
        color: #FFF;
        white-space: nowrap;
        overflow: hidden;
        width: 100%;
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translate(-50% , -50%);
    }
    .content p{
        display:inline-block;
        height: 30px;
        // line-height: 30px;
        margin: 0;
    }
    .content p.padding{
        padding-right: 50px;
        
    }
}
</style>
<style lang="scss">
#textLamp{
    .noselect {
    -webkit-touch-callout: none; /* iOS Safari */
    -webkit-user-select: none; /* Chrome/Safari/Opera */
    -khtml-user-select: none; /* Konqueror */
    -moz-user-select: none; /* Firefox */
    -ms-user-select: none; /* Internet Explorer/Edge */
    user-select: none; /* Non-prefixed version, currently
    not supported by any browser */
    }
}
</style>