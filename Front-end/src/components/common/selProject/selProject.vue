<template>
  <div class="sel_project_wrap">
    <div class="sel_project_title">
      项目选择
      <i class="rf c-fa c-fa-caret-down"></i>
      </div>

    <c-select
      multiple
      v-model="selProjectData"
      search
      :options="projectList"
      @change="selProjectFun"
      width="150px"
      selected-max-height="140px">
    </c-select>
  </div>
</template>

<script>
export default {
  data () {
    return {
      selProjectDataCache:[]
    };
  },
  props:{
    selProject:{
      type:[Array,[]]
    },
    projectList:{
      type:[Array,[]]
    }
  },
  methods: {
    selProjectFun(value,obj){
      this.$emit("selProjectFun",value,obj)
    },
  },
  mounted() {
    // this.$nextTick(()=>{
    //   document.getElementsByClassName("c-dropdown-toggle")[0].children[0].innerHTML = "项目选择"
    // })
    
  },
  computed: {
    selProjectData:function(){
      return this.selProject
    }
  },
}

</script>
<style lang='scss' scoped>
/deep/ .c-select-group{
 opacity: 0;
 height: 32px;
//  z-index: 99;
}
.sel_project_wrap{
  height: 35px;
  position: relative;
  .sel_project_title{
    position: absolute;
    // z-index: 88;
    height: 32px;
    line-height: 32px;
    width: 150px;
    border: 1px solid rgba(24,117,192,.5);
    border-radius: 3px;
    padding: 0 10px;
    i{
      line-height: 32px;
    }
  }
  
}
/deep/ .c-selected-tag{
    display: none!important;
  }
</style>