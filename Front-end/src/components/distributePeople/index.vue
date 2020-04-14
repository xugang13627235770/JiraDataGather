<template>
  <div id="containerWeb">
    <div id="search-box" class="block-shadow-bg bordered">
      <div>
        <label for="">关键字：</label>
        <div>
          <c-input
            width="230px"
            v-model="searchData.key"
            type="text"
            placeholder="可输入用户代码、用户名称、邮箱搜索"
          />
        </div>
      </div>
      <div>
        <label for="">人员类型：</label>
        <div>
            <c-select
            width="230px"
          v-model="searchData.userType"
          :options="peopleTypeList"
          menu-width="230px"
        ></c-select>
        </div>

      </div>
      <div>
        <c-button size="sm" type="linear" @click.native="searchEvent">查询</c-button>
      </div>
    </div>
    <div class="table-box">
      <div class="table-tool">
        <c-dropdown >
          <div slot="trigger" >
            <c-button type="linear">
              设置类型
              <c-icon :type="showDrop ? 'angle-up' : 'angle-down'"></c-icon>
            </c-button>
          </div>
          <li v-for="(item, index) in peopleSetList" :key="index" @click="setUserType(item.value)"  style="width: 190px;">
            <a>{{ item.label }}</a>
          </li>
        </c-dropdown>
      </div>
      <c-data-table
        :url="baseUrl"
        :token="token"
        :appNo="appNo"
        fixed-columns
        tableId="Table"
        :height="tableHeight"
        :async="true"
        :filter="false"
        :selection="selection"
        :pagination="pagination"
        :selectCols="false"
        :dataNumber="true"
        :search="false"
        :source="sourceGrid"
        :columns="columns"
        @change="changeGridEvent"
        idFiled="id"
      ></c-data-table>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      searchData: {
        key:"",
        userType: ""
      },
      showDrop: false,
      peopleType:{
        label:"全部",
        value:"-1"
      },
      peopleTypeList: [],
      peopleSetList: [],
      appNo: appNo,
      baseUrl: baseURL,
      token: "Basic " + Token,
      loading: false, // 加载动效
      tableHeight: "500px",
      columns: [
        {
          title: "姓名",
          width: "100",
          align: "left",
          fixed: true,
          dataIndex: "displayName"
        },
        {
          title: "人员代码",
          width: "100",
          align: "left",
          fixed: true,
          dataIndex: "name"
        },
        {
          title: "邮箱",
          width: "150",
          align: "left",
          fixed: true,
          dataIndex: "emailAddress"
        },
        {
          title: "人员类型",
          width: "160",
          align: "left",
          fixed: true,
          dataIndex: "userTypeStr"
        },
        {
          title: "最后操作时间",
          width: "150",
          align: "left",
          fixed: true,
          dataIndex: "updateTime"
        },
        {
          title: "累计故事",
          width: "100",
          align: "left",
          fixed: true,
          dataIndex: "totalStory"
        },
        {
          title: "累计缺陷",
          width: "100",
          align: "left",
          fixed: true,
          dataIndex: "totalDefect"
        }
      ],
      sourceGrid: [],
      selection: {
        checkRows: []
      },
      pagination: {
        current: 1,
        total: 10,
        pagesize: 20,
        pagesizeOpts: [10, 20, 40, 100]
      }
    };
  },
  methods: {
    calcTableHeight() {
      this.tableHeight =
        document.getElementById("containerWeb").offsetHeight -
        document.getElementById("search-box").offsetHeight -
        130 +
        "px";
    },
    // 人员类型初始化
    getUserTypeList(){
      this.loading = true;
      this.api
        .getUserTypeList()
        .then(res => {
          this.loading = false;
          if (res.code === 0) {
            this.peopleSetList = res.userTypeList.map(item => {
              return {
                label: item.dictValue,
                value: item.dictKey
              };
            });
            //添加第一个元素
            this.peopleTypeList = res.userTypeList.map(item => {
              return {
                label: item.dictValue,
                value: item.dictKey
              };
            });
            this.peopleTypeList.unshift(this.peopleType);
          }else{
            this.peopleTypeList.unshift(this.peopleType);
          }
        })
        .catch(err => {
          this.loading = false;
        });
    },
    searchEventFun() {
      let params = {
        key: this.searchData.key,
        userType: this.searchData.userType,
        limit: this.pagination.pageSize,
        page: this.pagination.pageIndex
      };
      this.loading = true;
      this.api
        .getUserList(params)
        .then(res => {
          this.loading = false;
          if (res.code === 0) {
            this.sourceGrid = res.page.list;
            this.pagination.total = res.page.totalCount;
          }
        })
        .catch(err => {
          this.loading = false;
        });
    },
    // 统计方法
    searchEvent() {
      this.pagination.pageIndex = 1;
      this.searchEventFun();
    },
    changeGridEvent(obj) {
      if (obj.current) {
        this.pagination.pageIndex = obj.current;
        this.pagination.pageSize = obj.pagesize;
        this.checkFlag = true;
        this.searchEventFun();
      }
    },
    // 设置人员
    setUserType(value) {
      let checkRows = this.selection.checkRows;
      if(checkRows == null || checkRows.length < 1){
        this.api.warnTip(this, "请选择至少一条数据");
        return;
      }

      let idArray = [];
      checkRows.forEach(value => {
        idArray.push(value.id);
      });
      let params = {
        idStr: idArray.join(","),
        userType: value
      };

      this.loading = true;
      this.api
        .updateUserTypeByIds(params)
        .then(res => {
          this.loading = false;
          if (res.code === 0) {
            this.searchEventFun();
          }
        })
        .catch(err => {
          this.loading = false;
        });

    },
  },
  mounted() {
    this.calcTableHeight();
    this.getUserTypeList();
    this.searchEvent();
  }
};
</script>

<style scoped lang="scss">
#containerWeb {
  height: 100%;
  #search-box {
      margin: 18px 18px 0 18px;
    height: 60px;
    display: flex;
    align-items: center;
    &>div{
        margin-right: 15px;
        display: inline-block;
        label{
            display: inline-block;
            width: 80px;
            text-align: right;
        }
        &>div{
            display: inline-block
        }
    }
    .c-row {
      height: 100%;
      display: flex;
      align-items: center;
    }
  }
}
</style>
