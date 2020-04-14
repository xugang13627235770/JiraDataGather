<template>
    <div id="videoComp">
      <span v-if="!data.videoUrl">请添加视频</span>
        <video v-if="data.videoUrl" id="videoPlayer" controls class='video-js vjs-default-skin vjs-big-play-centered' 
        :autoplay='data.autoPlaySwitch' :loop='data.loopPLaySwitch' preload='auto' height="100%" width='100%'>
            <source type='video/mp4' :src="data.videoUrl">
        </video>
    </div>
</template>
<script>
import configData from '../configData.js'
export default {
    name:"videoComp",
    props:{
      data:{
        type:Object,
        default(){
          return { }
        }
      },
    },
    watch:{
      data:{
        handler(cur){
          
        //   this.exampleIconBg = {backgroundImage:`${'url('+ this.data.bgImage +') '}`,backgroundRepeat:'no-repeat', backgroundPosition:'center center', backgroundSize: 'contain'};
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
#videoComp{
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
#videoComp{

}
</style>