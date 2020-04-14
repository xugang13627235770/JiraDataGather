<template>
  <div>
    <c-modal
      :title="modeText[mode]"
      effect="fade"
      width="600px"
      ref="dataBase"
      class="dataBase-wrap"
      @hide="hideModal"
    >
      <div slot="body">
        <c-form ref="detailData">
          <c-form-item need label="资源库名称" :label-col="4">
            <c-input width="220px" v-model="detailData.name" name="nameText"></c-input>
          </c-form-item>
          <c-form-item need label="说明" :label-col="4">
            <c-input width="220px" v-model="detailData.comment" name="comment" :rules="[{type:'required'}]"></c-input>
          </c-form-item>
          <c-form-item need label="数据库类型" :label-col="4">
            <c-select
              width="220px"
              :options="dictList.datasourceTypeList"
              v-model="detailData.datasourceType"
              name="heartbeatDetection"
            ></c-select>
          </c-form-item>
          <c-form-item need label="数据库地址" :label-col="4">
            <c-input width="220px" v-model="detailData.datasouceAddress" name="datasouceAddress" :rules="[{type:'required'}]"></c-input>
          </c-form-item>
          <c-form-item need label="端口号" :label-col="4">
            <c-input width="220px" v-model="detailData.datasourcePort" name="datasourcePort" :rules="[{type:'required'}]"></c-input>
          </c-form-item>
          <c-form-item need label="数据库名" :label-col="4">
            <c-input width="220px" v-model="detailData.datasourceName" name="datasourceName" :rules="[{type:'required'}]"></c-input>
          </c-form-item>
          <c-form-item need label="用户名" :label-col="4">
            <c-input width="220px" v-model="detailData.datasourceUsername" name="datasourceUsername" :rules="[{type:'required'}]"></c-input>
          </c-form-item>
          <c-form-item need label="密码" :label-col="4">
            <c-input width="220px" v-model="detailData.datasourcePassword" name="datasourcePassword" :rules="[{type:'required'}]"></c-input>
          </c-form-item>
        </c-form>
      </div>
       <div slot="footer">
         <div class="btn-box">
          <c-button type="primary" @click.native="test">测试连接</c-button>
          <c-button type="primary" @click.native="save">保存</c-button>
          <c-button type="gray">关闭</c-button>
        </div>
       </div> 
    </c-modal>
    <c-page-loading v-if="loading"></c-page-loading>
  </div>
</template>

<script>
const detailDataCache = {
  datasourceName:"",
  datasourcePassword:"",
  datasourceUsername:"",
  datasourcePort:"",
  datasouceAddress:"",
  datasourceType:"",
  comment:"",
  name:"",
}
export default {
  data() {
    return {
      loading:false,
      title: "",
      mode: "add",
      modeText: {
        add: "新增",
        edit: "编辑"
      },
      detailData:{
        datasourceName:"",
        datasourcePassword:"",
        datasourceUsername:"",
        datasourcePort:"",
        datasouceAddress:"",
        datasourceType:"",
        comment:"",
        name:"",
      }
    };
  },
  props:{
    dictList:{
      type:Object,
      default:{
        datasourceTypeList:[{
          label:"mysql",
          value:"mysql"
        }]
      }
    }
  },
  methods: {
    openModal(mode,id){
      this.mode = mode;
      this.$refs.dataBase.open();
      if(id){
        this.getDetail(id);
      }
    },
    getDetail(id){
      this.api.getDcmDatasource({id:id}).then(res=>{
        if(res.code===0){
          Object.assign(this.detailData,res.result)
        }
      })
    },
    hideModal(){
      this.detailData = Object.assign({},detailDataCache)
    },
    save(){
      this.loading = true;
      this.$refs.detailData.validateFields(result => {
          if(result.isvalid){
            this.$refs.detailData.$emit("closeValidate");
            if(this.mode==="add"){
              this.api.saveDcmDatasource(this.detailData).then(res=>{
                this.loading = false;
                if(res.code===0){
                  this.cToast({
                    text: "保存成功",
                    type: "success",
                    placement: "top",
                    duration: 2000,
                    closeOnClick: true,
                    hide: () =>{
                      this.$refs.dataBase.close();
                      this.$emit("update")
                    }
                  });
                }
              }).catch(err=>{
                this.loading = false;
              })
            }else{
              this.api.updateDcmDatasource(this.detailData).then(res=>{
                this.loading = false;
                if(res.code===0){
                  this.cToast({
                    text: "修改成功",
                    type: "success",
                    placement: "top",
                    duration: 2000,
                    closeOnClick: true,
                    hide: () =>{
                      this.$refs.dataBase.close();
                      this.$emit("update")
                    }
                  });
                }
              }).catch(err=>{
                this.loading = false;
              })
            }
            
          }
      })
      
    },
    test(){

    },
  }
};
</script>
<style lang="scss" scoped></style>
