<template>
  <div class="full-modal" id="wrap">
    <c-modal
      title="数据表预览"
      effect="fade"
      width="100%"
      ref="dataBase"
      class="dataBase-wrap"
    >
      <div slot="body">
        <div class="dataBase-box">
          <c-container class="search-box">
            <c-row>
              <c-column :col="3" class="context">
                <label for="">资源库</label>
                <c-select
                  width="200px"
                  :options="dictList.resourceList"
                  v-model="searchData.id"
                  name="datasourceName"
                  @change="selResource"
                ></c-select>
              </c-column>
              <c-column :col="3" class="context">
                <label for="">数据表</label>
                <c-select
                  width="200px"
                  :options="dictList.dataBaseList"
                  v-model="searchData.table"
                  name="table"
                ></c-select>
              </c-column>
              <c-column :col="6" class="context">
                <c-button class="rf" type="primary" @click.native="searchEventFun">查询</c-button>
              </c-column>
            </c-row>
          </c-container>
          <c-data-table
            fixed-columns
            id="id"
            :height="calcTableHeight"
            :url="baseUrl"
            :token="token"
            :appNo="appNo"
            :async="true"
            :filter="false"
            :pagination="pagination"
            :selectCols="false"
            :dataNumber="true"
            idFiled="id"
            :search="false"
            @change="changeGridEvent"
            :source="sourceGrid"
            :columns="columns"
          ></c-data-table>
          <c-page-loading v-if="loading"></c-page-loading>
        </div>
        
      </div>
      <div slot="footer">

      </div>
      
    </c-modal>
  </div>
</template>

<script>
export default {
  data() {
    return {
      loading:false,
      appNo: appNo,
      baseUrl: baseURL,
      token: "Basic " + Token,
      Token: window.Token,
      calcTableHeight:"400px",
      //table分页配置
      pagination: {
        isPagination: true,
        current: 1,
        total: 10,
        pagesize: 20
      },
      sourceGrid:[],
      columns:[],
      dictList:{
        resourceList:[],
        dataBaseList:[],
      },
      searchData:{
        id:"",
        table:"",
      }
    };
  },
  methods: {
    //表格计算高度
    calcTableHeightFun() {
      this.calcTableHeight = document.getElementById("wrap").offsetHeight - 150 + "px";
    },
    openModal(id) {
      this.$refs.dataBase.open();
      this.searchData.id = id;
      this.getDataBaseList(id);
      if(id){
        
      }
    },
    searchEventFun(){
      this.loading = true;
      let params = Object.assign({},this.pagination,this.searchData);
      this.api.getTableDataDcmDatasource(params).then(res=>{
        this.loading = false;
        if(res.code===0){
          this.sourceGrid = res.result.resultList;
          res.result.columns.forEach(item=>{
            this.columns.push(
              {
                title: item.name,
                show: true,
                dataIndex: item.name,
                align: "left",
                width: 150,
                fixed: true
              },
            )
          })
          this.pagination.total = res.result.totalCount
        }else{
          this.sourceGrid = [];
          this.columns = [];
          this.pagination.total = 0;
        }
      }).catch(err=>{
        this.loading = false;
        this.sourceGrid = [];
        this.columns = [];
        this.pagination.total = 0;
      })
    },
    changeGridEvent(index, size) {
      if (index) {
        this.pagination.pageIndex = index;
        this.pagination.pageSize = size;
        this.checkFlag = true;
        this.searchEventFun();
      }
    },
    selResource(id){
     if(id){
       this.getDataBaseList(id);
     }else{
       this.dictList.dataBaseList = [];
     }

    },
    getResourceList(){
      let params = {
        isPagination: false,
      }
      this.api.searchDcmDatasource(params).then(res=>{
        if(res.code === 0){
          this.dictList.resourceList = res.result.map(item=>{
            return {
              value:item.id,
              label:item.datasourceName
            }
          })
        }
      })
    },
    getDataBaseList(id){
      let params = {
        id:id,
      }
      this.api.getTablesDcmDatasource(params).then(res=>{
        if(res.code === 0){
          this.dictList.dataBaseList = res.result.map(item=>{
            return {
              value:item.name,
              label:item.name,
            }
          })
        }
      })
    }
  },
  mounted() {
    this.calcTableHeightFun();
    this.getResourceList()
  },
};
</script>
<style lang="scss" scoped>
@import "../resource.scss";
</style>
