<template>
    <div id="leftStaff">
        <canvas id="leftStaffCanvas"></canvas>
    </div>
</template>
<script>
export default {
    name:"leftStaff",
    data(){
        return{
            begin:50,
            end:0,
            canvas:null,
            ticket:10,
            drawCount:0,
        }
    },
    methods:{
        calcFun(){
            let vm = this;
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
            ctx.moveTo(30,p);
            ctx.lineTo(lineEnd,p);
            ctx.strokeStyle="#454a52";
            ctx.stroke();
            ctx.restore(); 
            vm.drawCount++;
        },
        drawText(p){
            let vm = this;
            let ctx= vm.canvas.getContext("2d");
            ctx.font="12px Arial";
            ctx.fillStyle="#585E68";
            ctx.fillText(p,p+2,15);
        },
    },
    mounted(){
        let vm = this;
        vm.$nextTick(()=>{
            vm.end = document.getElementById('leftStaff').offsetHeight;
            vm.canvas = document.getElementById("leftStaffCanvas");
            vm.canvas.width =30;
            vm.canvas.height = vm.end;
            vm.calcFun();
        })
    },
}
</script>
<style lang="scss" scoped>
#leftStaff{
    position: absolute;
    top: 30px;
    left: 0;
    width: 30px;
    height: calc(100% - 30px);
    z-index: 99;
    padding-top: 50px;
}
</style>