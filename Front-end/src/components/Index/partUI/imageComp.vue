<template>
    <div id="imageComp" :style="exampleIconBg">
      <span v-if="!data.basicImage">请添加图片</span>
    </div>
</template>
<script>
import configData from '../configData.js'
export default {
    name:"imageComp",
    props:{
      data:{
        type:Object,
        default(){
          return { }
        }
      },
    },
    computed: {
      exampleIconBg(){
        return {backgroundImage:`${'url('+ this.data.basicImage +') '}`,backgroundRepeat:'no-repeat', backgroundPosition:'center center', backgroundSize: 'contain'};
      },
    },
    watch:{
      data:{
        handler(cur){
          
        },
        deep:true
      },
    },
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
#imageComp{
  width: 100%;
  height: 100%;
  position: relative;
  >span{
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50% , -50%);
  }
}
</style>
<style lang="scss">
#imageComp{

}
</style>