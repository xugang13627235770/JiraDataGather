<template>
  <div id="dataCub">
     
  </div>
</template>

<script type="text/ecmascript-6">
  import api from "../../api/api"
  import tool from "../../util/tools"
  import navBar from '../navBar/nav'
  export default {
    name: 'dataCub',
    components:{navBar},
    data () {
      return {
        
      }
    },
    methods:{
      
    },
    mounted(){
      this.$nextTick(()=>{
        
      })
    }
  }
</script>
<style lang='scss' scoped>
#dataCub{
  width: 100%;
  height: 100%;
  .dataCubContent{
    width: 100%;
    height: calc(100% - 45px);
    // background: red;
  }
}
</style>
