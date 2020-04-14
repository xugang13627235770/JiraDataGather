<template>
    <div id="ferrisWheel">
        <div class="circle"></div>
        <div class="middle"></div>
    </div>
</template>
<script>
export default {
    name:"ferrisWheel",
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
#ferrisWheel{
    width: 100%;
    height: 100%;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50% , -50%);
    >.circle{
        position: absolute;
        width: 1038px;
        height: 1038px;
        background: url("../../../../static/images/out.png");
        top:50%;
        left:50%;
        transform: translate(-50% , -50%);
        transform-origin: 0 0;
        animation: xuanzhuan 30s cubic-bezier(0.33,0,0.66,1) infinite alternate;
    }
    >.middle{
        position: absolute;
        width: 659px;
        height: 659px;
        background: url("../../../../static/images/inner.png");
        top:50%;
        left:50%;
        transform: translate(-50% , -50%);
        transform-origin: 0 0;
        animation: xuanzhuan2 30s cubic-bezier(0.33,0,0.66,1) infinite alternate;
    }
    @keyframes xuanzhuan {
        0%,100%{
            transform: rotate(0deg) translate(-50% , -50%);
        }
        50%{
            transform: rotate(50deg) translate(-50% , -50%);
        }
    }
    @keyframes xuanzhuan2 {
        0%,100%{
            transform: rotate(0deg) translate(-50% , -50%);
        }
        50%{
            transform: rotate(-50deg) translate(-50% , -50%);
        }
    }
}
</style>
<style lang="scss">
#ferrisWheel{

}
</style>