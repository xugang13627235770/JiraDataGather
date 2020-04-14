<template>
    <div id="timer">
        <p :style="timerStyle"> <i class="iconfont icon-shijian1"></i> {{currentTime}}</p>
    </div>
</template>
<script>
import configData from '../configData.js'
import moment from 'moment'
export default {
    name:"timer",
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
            timerStyle:{},
            currentTime:"",
            timeFlg:null,
            sum:0,
        }
    },
    watch:{
        data:{
            handler(cur){
                let vm = this;
                vm.$set(vm,'timerStyle',{'letterSpacing':vm.data.letterSpacing+'px','fontSize':vm.data.basicFontSize+"px",'color':vm.data.basicFontColor});
            },
            deep:true
        }
    },
    methods:{
        timeFun(){
            let vm = this;
            vm.currentTime = moment().format(vm.data.timeFormat);
        },
        starTime(){
            let vm = this;
            vm.timeFlg = setInterval(() => {
                vm.timeFun();
                vm.sum++;
                if(vm.sum==3){
                    clearInterval(vm.timeFlg);
                    vm.starTime();
                    vm.sum=0;
                }
            }, 1000);
        },
    },
    mounted(){
        let vm = this;
        vm.$nextTick(()=>{
            vm.timeFun();
            vm.starTime();
        })
    },
    beforeDestroy(){
        let vm = this;
        if(vm.timeFlg){
            clearInterval(vm.timeFlg);
        }
    },
    destroyed(){

    },
}
</script>
<style lang="scss" scoped>
#timer{
    width: 100%;
    height: 100%;
    position: relative;
    p{
        margin: 0;
        width: 100%;
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translate(-50% , -50%);
        text-align: center;
        font-size: 12px;
    }
}
</style>
<style lang="scss">
#timer{

}
</style>