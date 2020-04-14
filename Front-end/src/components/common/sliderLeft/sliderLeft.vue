<template>
  <div class="sliderWrap">
    <!-- 左 -->
    <div class="left-box ordered-r block-bg bordered-r" :class="{'slider-left-end':flod,'slider-left-start':!flod}">
      <slot name="left"></slot>
    </div>
    <!-- 伸缩按钮 -->
    <div class="flod inblock block-bg  bordered-r  bordered-t bordered-b" :class="{'flodend':flod,'flodstart':!flod}"  v-show="showFilter" ref="flod"  @click="flodFn">
      <c-icon :type="!flod?'chevron-left':'chevron-right'" :color="'#dbe4f2'"></c-icon>
    </div>
    <!-- 右 -->
    <div id="rightWarp" class="result-right" :class="{'result-right-end':flod,'result-right-start':!flod}">
      <slot name="right"></slot>
    </div>
    <!-- 全屏 -->
    <div class="full-page">
      <slot name="full">
      </slot>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      flod: false, // 控制左侧菜单显示隐藏
      showFilter: true // 控制左侧菜单显示隐藏
    };
  },
  methods: {
    callFunc(){
      let length = arguments.length;
      if(length<1){
        return ;
      }else{
        let funcName = arguments[0];
        let returnVal = null;
        let codeStr = 'returnVal = this.$parent[funcName](';
        for(let i=1;i<length;i++){
          codeStr += 'arguments['+i+']';
          if(i<=length-1){
            codeStr += ',';
          }
        }
        codeStr += ');';
        eval(codeStr);
        return returnVal;
      }
    },
    flodFn() {
      this.flod = !this.flod;
    }
  }
};
</script>
<style scoped lang="scss">

.full-page{
  position: relative;
  top: 0;
  left: 0;
}

.sliderWrap {
  position: relative;
  width: 100%;
  float: left;
  height: 100%;
  overflow: hidden;
}
.left-box {
  width: 370px;
  position: absolute;
  left: 0;
  top: 0;
  height: calc(100%);
  transition: all 0.2s;
  &.slider-left-start {
    // transform: translateX(0);
  }
  &.slider-left-end {
    transform: translateX(-369px);
  }
  &>div{
    height: 100%;
  }
  
}
.flod {
  position: absolute;
  width: 15px;
  height: 40px;
  text-align: center;
  line-height: 40px;
  z-index: 2;
  top: calc(50% - 15px);
  left: 0px;
  cursor: pointer;
  border-top-right-radius: 3px;
  border-bottom-right-radius: 3px;
  transition: all 0.2s;
  &.flodstart {
    transform: translateX(369px);
  }
  &.flodend {
    transform: translateX(0);
  }
  
}
.result-right {
  overflow-y:auto;
  height: calc(100%);
  width: calc(100% - 370px);
  position: absolute;
  left: 0px;
  transition: all 0.2s;
  &.result-right-start {
    transform: translateX(370px);
    // width: calc(100% - 370px);
    animation: result-right-start 0.3s;
  }
  &.result-right-end {
    
    transform: translateX(0);
    width: 100%;
  }
  &>div{
    height: 100%;
  }
  
}

@keyframes result-right-start {
  0% {
    // transform: translateX(0);
    width: 100%;
  }
  99%{
    width: calc(100% - 370px);
  }
  100% {
    // transform: translateX(370px);
    width: calc(100% - 370px);
  }
}
</style>