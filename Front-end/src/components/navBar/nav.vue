<template>
  <div id="dataCub">
     <div class="top-menu bordered-b" >
      <div class="headerIcon">
        <i class="iconfont icon-shujumofang1"></i>
        <span>研发量化管理平台</span>
      </div>
     <c-nav class="nav-top" mode="horizontal" @select="handleSelect" style="margin-left: 50px;">
       <template v-for="menu in menuData">
         <c-nav-item :key="menu.id" v-if="!menu.children" :index="menu.id">{{menu.name}}</c-nav-item>
         <c-sub-nav v-if="menu.children" :key="menu.id" :index="menu.id">
           <template slot="title">{{menu.name}}</template>
           <c-nav-item v-for="child in menu.children" :index="child.id" :key="child.id">{{child.name}}</c-nav-item>
         </c-sub-nav>
       </template>
       <div style="float: right;padding-right: 30px">
         <c-button size="sm" type="linear" @click.native="logout">退出</c-button>
       </div>
     </c-nav>
    </div>
    <div style="clear: both;"></div>

    <div class="dataCubContent">
        <router-view></router-view>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
  import api from "../../api/api"
  import tool from "../../util/tools"
  export default {
    name: 'dataCub',
    data () {
      return {
        menuData:[
          {name:"首页",path:'/app/jdg/home',id:1},
          {name:"开发统计",path:'/app/jdg/developerStatistics',id:2},
          {name:"测试统计",path:'/app/jdg/testerStatistics',id:3},
          {name:"项目统计",path:'/app/jdg/projectStatistics',id:4},
          {name:"迭代关联",path:'/app/jdg/iteration',id:5},
          {name:"人员分类",path:'/app/jdg/distributePeople',id:6},
        ],
      }
    },
    methods:{
      handleSelect(index) {
        let menus = this.menuData;
        let pMenu = menus.filter(item => item.id === index);
        if (pMenu.length > 0) {
          this.$router.push(pMenu[0].path);
          return;
        }
        menus.forEach(item => {
          if (item.children) {
            let cMenu = item.children.filter(ntem => ntem.id === index);
            if (cMenu.length > 0) {
              this.$router.push(cMenu[0].path);
            }
          }
        });
      },
      //退出
      logout(){
        let token = window.localStorage.getItem("token") ? window.localStorage.getItem("token") : ""
        this.api.logout({"token":token}).then(res=>{
          if(res.code==0){
            window.localStorage.setItem("token","");
            this.$router.replace({ name: 'login' })
          }
        });
      }
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
  .top-menu{
    position: relative;
    .headerIcon{
      position: absolute;
      height: 50px;
      line-height: 50px;
      color: #fff;
      left: 15px;
      bottom:0;
      z-index: 999;
      >i{
        font-size: 25px;
      }
      >span{
        font-size: 18px;
        margin-left: 5px;
      }
    }
  }
  .dataCubContent{
    width: 100%;
    height: calc(100% - 51px);
  }
}
</style>
