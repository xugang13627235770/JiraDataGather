<template>
    <div id="topStaff">
        <canvas id="topStaffCanvas" width="1600" height="30"></canvas>
    </div>
</template>
<script>
import store from '../../../../vuex/store.js'
export default {
    name:"topStaff",
    data(){
        return{
            begin:50,
            end:0,
            canvas:null,
            ticket:10,
            drawCount:0,
            timeFlag:null,
        }
    },
    computed: {
        toggle(){
            return store.getters.getToggle;
        },
        scale(){
            return store.getters.getPageConfig.scale;
        },
        currentMode(){
            return store.getters.getCurrentMode;
        },
    },
    watch:{
        toggle:{
            deep:true,
            handler(){
                if(this.timeFlag){
                    clearTimeout(this.timeFlag);
                }
                this.timeFlag = setTimeout(()=>{
                    this.init();
                },300);
            },
        },
        scale(){
            this.init();
        },
    },
    methods:{
        calcFun(){
            let vm = this;
            vm.drawCount = 0;
            let ctx= vm.canvas.getContext("2d");
            ctx.clearRect(0, 0, vm.canvas.width, vm.canvas.height);
            for(let p=0;p<vm.end;p+=vm.ticket){
                vm.drawTicket(p);
            } 
        },
        drawTicket(p){
            let vm = this;
            let ctx= vm.canvas.getContext("2d");
            ctx.save(); 
            ctx.lineWidth = 1;
            ctx.beginPath(); 
            ctx.translate(0.5,0.5); 
            let lineEnd = 26;
            if(vm.drawCount%10===0){
                if(vm.drawCount%20===0){
                    lineEnd = 0;
                }else{
                    lineEnd = 22;
                }
                vm.drawText(p);
            }
            ctx.moveTo(p,30);
            ctx.lineTo(p,lineEnd);
            ctx.strokeStyle="#454a52";
            ctx.stroke();
            ctx.restore(); 
            vm.drawCount++;
        },
        drawText(p){
            let vm = this;
            let pageConfig = store.getters.getPageConfig;
            let ctx= vm.canvas.getContext("2d");
            ctx.font="12px Arial";
            ctx.fillStyle="#585E68";
            ctx.fillText(Math.round(p / pageConfig.scale),p+2,15);
        },
        init(){
            let vm = this;
            if(vm.currentMode!=3){
                vm.end = document.getElementById('topStaff').offsetWidth;
                vm.canvas = document.getElementById("topStaffCanvas");
                vm.canvas.width = vm.end - 50;
                vm.canvas.height = 30;
                vm.calcFun();
            }
        },
    },
    mounted(){
        let vm = this;
        vm.$nextTick(()=>{
            vm.init();
        });
        window.addEventListener('resize',function(){
            vm.init();
        });
    },
}
</script>
<style lang="scss" scoped>
#topStaff{
    position: absolute;
    top: 0;
    left: 30px;
    width: calc(100% - 30px);
    height: 30px;
    z-index: 98;
    padding-left: 50px;
}
</style>