<template>
  <div id="app">
    <!-- <keep-alive>
      <router-view v-if="$route.meta.keepAlive" style="height:100%;" :key="$route.path">
      </router-view>
    </keep-alive> -->
    <router-view></router-view>
  </div>
</template>

<script>
// import './skin/skin.scss'
export default {
  name: 'App',
  data(){
    return{
      
    }
  },
  mounted(){
    this.$nextTick(()=>{
      
    })
  }
}
</script>

<style lang="scss">
@import './skin/common.scss';
*{
  margin: 0;
  padding: 0;
}
html,body,#app{
height: 100%;
overflow:hidden ;
}
#app>div{
height: 100%;
}
</style>
