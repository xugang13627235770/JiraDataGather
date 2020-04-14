<template>
  <div class="searchform" ref="searchform">
    <transition v-for="(obj,index) in searchBoxs" :key="index" :name="obj.id">
      <div class="itemPanel" v-show="obj.show">
        <!-- 时间 -->
        <div v-if="obj.type&&obj.type==='datetime'" class="itemBox date">
          <div class="dateBox fl">
            <span class="itemLabel fl">{{obj.title}}</span>
            <c-datepicker
              v-model="item[index].dateTime"
              width="340px"
              :timePicker="true"
              format="YYYY-MM-DD HH:mm:ss"
              :readonly="true"
              @change="changeDateTime($event,obj.dateTime,item[index],index,obj)"
            ></c-datepicker>
            <a @click="chooseTime" class="btn">确定 </a>
          </div>
          <div class="item" :class="[item[index].expand ?'active':'']">
            <div class="myBox" :ref="obj.id">
              <a
                :class="[obj.checkAll ?'on':'']"
                v-if="obj.isShowAll"
                @click="getRadioValue(index,null,item,obj,'date')"
              >全部</a>
              <a
                :class="[((singelCheckIndex[index] === $index) || (defalutFlag&&searchBoxs[index].defalut[0]===key.value))?'on':'']"
                v-for="(key,$index) in obj.data"
                :id="key.value"
                :key="$index"
                @click="getRadioValue(index,$index,key,obj,'date')"
              >{{key.text}}</a>
            </div>
          </div>
          <!-- 单项收缩按钮 -->
          <div
            class="arrow"
            @click="expand(index,item,item[index].expand)"
            v-show="item[index].shoWIcon"
          >
            <i
              class="iconfont"
              :class="[item[index].expand ?'icon-jiantou_yemian_xiangxia':'icon-jiantou_yemian_xiangyou']"
            ></i>
          </div>
        </div>
        <!-- 车牌 -->
        <div v-else-if="obj.type&&obj.type==='plate'" class="itemBox">
          <div class="dateBox fl">
            <span class="itemLabel fl">{{obj.title}}</span>
            <c-platectr  v-model="obj.plate" style="width: 200px;" :plateBelong="plateBelong"></c-platectr>
            <a @click="choosePlate" class="btn">确定</a>
          </div>
          <div class="item" :class="[item[index].expand ?'active':'']">
            <div class="myBox" :ref="obj.id">
            </div>
          </div>
        </div>
        <!-- 其他 -->
        <div v-else class="itemBox">
          <span class="itemLabel fl">{{obj.title}}</span>
          <!-- 单选样式 -->
          <div v-if="!obj.isMultiple" class="item" :class="[item[index].expand ?'active':'']">
            <div class="myBox" :ref="obj.id">
              <a
                :class="[obj.checkAll ?'on':'']"
                v-if="obj.isShowAll"
                @click="getRadioValue(index,null,item,obj)"
              >全部</a>
              <a
                :class="[((singelCheckIndex[index] === $index) )?'on':'']"
                v-for="(key,$index) in obj.data"
                :id="key.value"
                :key="$index"
                @click="getRadioValue(index,$index,key,obj)"
              >{{key.text}}</a>
            </div>
          </div>
          <!-- 复选样式 -->
          <div v-else class="item" :class="[item[index].expand ?'active':'']">
            <div class="myBox" :ref="obj.id">
              <c-checkbox-group v-model="item[index].check">
                <a v-if="obj.isShowAll" class="checkAll" @click="chooseAll(obj.id,item)">
                  <span class="c-checkbox-inner" :class="[item[index].checkAll ?'on':'']">
                    <i
                      :class="[item[index].checkAll ?'c-fa-check':'']"
                      class="c-fa c-fa-heckbox-checked c-checkbox-inner-check"
                    ></i>
                  </span>
                  <span class="txt">全部</span>
                </a>
                <c-checkbox   @change="change"
                  v-for="(key,index) in obj.data"
                  :label="key.value"
                  :key="index"
                >{{key.text}} </c-checkbox>
                
              </c-checkbox-group>
            </div>
          </div>
          <!-- 单项收缩按钮 -->
          <div
            class="arrow"
            @click="expand(index,item,item[index].expand)"
            v-show="item[index].shoWIcon"
          >
            <i
              class="iconfont"
              :class="[item[index].expand ?'icon-jiantou_yemian_xiangxia':'icon-jiantou_yemian_xiangyou']"
            ></i>
          </div>
        </div>
      </div>
    </transition>
    <!-- 按钮 -->
    <p class="btnBg"  v-if="expandRow<searchBoxs.length">
      <a class="arrowDown" @click="handerSilder">
        <i class="iconfont" :class="[expandAll ?'icon-shouqi1':'icon-zhankai2']"></i>
      </a>
    </p>
  </div>
</template>
<script>
import moment from "moment";
export default {
  data() {
    return {
      flag:false, //防止多次发送请求
      singelCheckIndex: [], //存储单选框状态
      itemHeight: [],
      item: [],
      expandAll: false,
      expandRow: 2,
      searchBoxs: [],
      dataTime: [], //存储时间数组
      plate: {
          plateNo: "",
          plateColor: ""
      },
      plateBelong: this.$store.state.com.systemConfig.plateBelong,
      defalutFlag:true,

    };
  },
  props: ["data"],
  watch: {
    // 复选框全选/反选
    item: {
      handler: function(val, oldVal) {
  
        let _this = this;
        this.searchBoxs.forEach(function(item, index) {
          if (item.data.length === val[index].check.length) {
            _this.$set(val[index], "checkAll", true);
        
          } else {
            _this.$set(val[index], "checkAll", false);   
          }
        });

      },
      deep: true
    },
    searchBoxs(val) {
        var _item = [];
        if (this.data) {
        this.expandRow = this.data.expandRow;
        this.searchBoxs = this.data.searchBoxs;
        var searchBoxs = val;
        for (var i = 0; i < searchBoxs.length; i++) {
          if (searchBoxs[i].defalut.length > 0) {
            searchBoxs[i].checkAll = false;
          }
          if (searchBoxs[i].type === "datetime") {     
            searchBoxs[i].customTime=false   
            this.getDate(searchBoxs[i], i, true); //时间控件初始默认值  
          }
          if (i < this.expandRow) {
            searchBoxs[i].show = true;
          }
          _item.push({
            id: searchBoxs[i].id, //列表ID
            check: searchBoxs[i].defalut, //是否选择
            dateTime: searchBoxs[i].dateTime, //时间
    	      plate:searchBoxs[i].plate,
            checkAll: searchBoxs[i].checkAll, // 全选
            isMultiple: searchBoxs[i].isMultiple, // 单选/多选
            expand: false, //是否展开
            show: searchBoxs[i].show , //列表是否显示
            shoWIcon: false, //展开按钮是否显示
            customTime:searchBoxs[i].customTime
          });
        }
       
      }
      this.item = _item

    }
  },
  methods: {
    getStyle(element, style) {
      let val = "";
      if (element.currentStyle) {
        val = element.currentStyle[style];
      } else {
        val = getComputedStyle(element, false)[style];
      }

      if (val === "auto" && (style === "width" || style === "height")) {
          
        var rect = element.getBoundingClientRect();
        if (style === "width") {
          val =  rect.right - rect.left + "px";
        } else {
          val = rect.bottom - rect.top + "px";
        }
       
      }
      
      return val;
    },
    getElOutHeight(el) {
      
      let h = this.getStyle(el, "height");
      return h.split("px")[0];
    },
    getParam() {
          // 刷新时间
          for (var i = 0; i <this.searchBoxs.length; i++) {
            if (this.searchBoxs[i].type === "datetime"&&!this.searchBoxs[i].customTime) {        
              this.getDate(this.searchBoxs[i], i, true); //时间控件初始默认值  
            }
          }
        
      //构建筛选数据
      let filterArray = this.item;
      let filterData = {};
      if (filterArray) {
        for (let i = 0, len = filterArray.length; i < len; i++) {
          let obj = filterArray[i];
          if (obj.dateTime) {
            let dataTimeArr = obj.dateTime.split(" ~ ");
            filterData[obj.id + "Start"] = dataTimeArr[0];
            filterData[obj.id + "End"] = dataTimeArr[1];
           } else if (obj.plate) {
              if(obj.plate.plateNo){
                filterData[obj.id] = obj.plate;
              }
          } else {
            if (obj.check && obj.check.length > 0 && obj.checkAll == false) {
              obj.isMultiple ? filterData[obj.id] = obj.check : filterData[obj.id] = obj.check.join("");
            }
          }
        }
      }
      return filterData;
    },
    change(currentValue) { 
        this.$nextTick(()=>{
            this.$emit("search", this.getParam());
        }); 
    },
    // 复选框全选
    chooseAll(id, item) {
      var searchBoxs = this.searchBoxs;
      for (var i = 0; i < searchBoxs.length; i++) {
        var arr = [];
        if (!this.item[i].checkAll && this.item[i].id === id) {
          for (var j = 0; j < searchBoxs[i].data.length; j++) {
            arr.push(searchBoxs[i].data[j].value);
          }
          this.$set(this.item[i], "check", arr);
          this.$set(this.item[i], "checkAll", true);
        } else {
          if (this.item[i].id === id) {
            this.$set(this.item[i], "check", []);
            this.$set(this.item[i], "checkAll", false);
          }
        }
      }
      this.change();
    },
    //单选点击事件
    getRadioValue(parentIndex, currentIndex, current, parent,type) {

      var arr = [];
      parent.customTime=false; //标识当前不是自定义时间
      if (currentIndex || currentIndex === 0) {
        //非全选的时候
        arr.push(current.value);
        parent.checkAll = false;
        this.$set(this.singelCheckIndex, parentIndex, currentIndex);
        if(type){
          this.getDate(current, parentIndex,true);
         // this.$set(this.item[parentIndex], "dateTime", parent.dateTime);
        }
      } else {
        parent.checkAll = true;
        this.$set(this.singelCheckIndex, parentIndex, "");
        if(type){
          
          this.$set(this.item[parentIndex], "dateTime", "");
          this.$set(this.searchBoxs[parentIndex], "dateTime", "");
        }
      }
      this.defalutFlag=false
      this.$set(this.item[parentIndex], "check", arr);    
      this.$emit("search", this.getParam());
    },
    changeDateTime(currentValue,val, item, index,obj) {
      // currentValue为当前选取时间，val为选取之前时间，item是组件获取参数item,index为索引，obj是searchBox
      if(currentValue!=val){
        obj.customTime=true
        if(obj.checkAll){
            obj.checkAll=false
        }
        this.defalutFlag=false
      }
      item.check = [];
      item.dateTime = currentValue;
      this.$set(this.singelCheckIndex, index, "");
    },
    chooseTime(){
      this.$emit("search", this.getParam());
    },
    choosePlate() {
        this.$emit("choosePlate", this.getParam());
    },
    //当前列表展开事件
    expand(index, obj, flag) {
      this.$set(obj[index], "expand", !flag);
    },
    // 收缩事件
    handerSilder() {
      let _this = this;
      let flag=false
      let h=this.$refs.searchform.offsetHeight
      this.expandAll = !this.expandAll  
       if (this.expandAll) {
        this.searchBoxs.forEach(item => {
          item.show = true;
        });
        h=this.$refs.searchform.offsetHeight
        flag=true
      } else {
        this.searchBoxs.forEach((item, index) => {
          if (index < this.expandRow) {
            item.show = true;
          } else {
            item.show = false;
          }
        h=this.$refs.searchform.offsetHeight
         flag=false
        });
      }
      this.$nextTick(() => {
         this.getObjHeight(); 
      });
      if(this.data.handerSilderEvent&&typeof this.data.handerSilderEvent==="function"){
          this.data.handerSilderEvent(h,flag)
      }
    },
    //获取列表项实际高度
    getObjHeight() {
      this.itemHeight = [];
      for (var i = 0; i < this.item.length; i++) {
        var id = this.item[i].id;
        //var obj=$(this.$refs[id][0])
        //this.itemHeight.push({"id":id,"height":$(obj).outerHeight()})
        var el = this.$refs[id][0];
        this.itemHeight.push({ id: id, height: this.getElOutHeight(el) });
      }
      if (this.itemHeight.length > 0) {
        this.itemHeight.forEach((obj, index) => {
          if (obj.height > 30) {
            this.$set(this.item[index], "shoWIcon", true);
          }
        });
      }
    },
    //计算时间
    getDate(obj, index) {
       if(!obj.checkAll){
         let time=""
          // 间隔分钟
          if (obj.time === "m") {
           time = `${moment()
              .subtract(obj.num, "minute")
              .format("YYYY-MM-DD HH:mm:ss")} ~ ${moment().format(
              "YYYY-MM-DD HH:mm:ss"
            )}`
          }
          // 间隔小时
          if (obj.time === "h") {
           time = `${moment()
              .subtract(obj.num, "hours")
              .format("YYYY-MM-DD HH:mm:ss")} ~ ${moment().format(
              "YYYY-MM-DD HH:mm:ss"
            )}`;
          }
          // 今天
          if (obj.time === "now") {
           time = `${moment()
              .startOf("day")
              .format("YYYY-MM-DD HH:mm:ss")} ~ ${moment()
              .endOf("day")
              .format("YYYY-MM-DD HH:mm:ss")}`;
          }
          //昨天
          if (obj.time === "yd") {
            if(obj.num===0){
            
               time = `${moment()
              .subtract(1, "day")
              .startOf("day")
              .format("YYYY-MM-DD HH:mm:ss")} ~ ${moment()
              .subtract(1, "day")
              .endOf("day")
              .format("YYYY-MM-DD HH:mm:ss")}`;
            }else{
             time = `${moment()
              .subtract(obj.num+1, "day")
              .format("YYYY-MM-DD HH:mm:ss")} ~ ${moment()
              .subtract(1,"day")
              .format("YYYY-MM-DD HH:mm:ss")}`;
            }
          }
          // 间隔天
          if (obj.time === "d") {
           time = `${moment()
              .subtract(obj.num, "day")
              .format("YYYY-MM-DD HH:mm:ss")} ~ ${moment()
              .format("YYYY-MM-DD HH:mm:ss")}`;
          }
          // 间隔月
          if (obj.time === "month") {
           time = `${moment()
              .subtract(obj.num, "month")
              .startOf("day")
              .format("YYYY-MM-DD HH:mm:ss")} ~ ${moment()
              .endOf("day")
              .format("YYYY-MM-DD HH:mm:ss")}`;
          }

        this.$set(this.searchBoxs[index],"dateTime",time)
        this.$set(this.searchBoxs[index],"time",obj.time)
        this.$set(this.searchBoxs[index],"num",obj.num)
        if(this.item.length>0&&this.item[index]){
            this.$set(this.item[index],"dateTime",time)
            this.$set(this.item[index],"time",obj.time)
            this.$set(this.item[index],"time",obj.num)
        }
       }else{
         this.searchBoxs[index].dateTime=""
       }
    }
  },
  created() {
    if (this.data) {

      this.expandRow = this.data.expandRow;
      this.searchBoxs = this.data.searchBoxs;
      var searchBoxs = this.searchBoxs;
      for (var i = 0; i < searchBoxs.length; i++) {
        if (searchBoxs[i].defalut.length > 0) {
          searchBoxs[i].checkAll = false;
        }
        if (searchBoxs[i].type === "datetime") {
          searchBoxs[i].customTime=false
          this.getDate(searchBoxs[i], i, true); //时间控件初始默认值
   
        }
        this.item.push({
          id: searchBoxs[i].id, //列表ID
          check: searchBoxs[i].defalut, //是否选择
          dateTime: searchBoxs[i].dateTime, //时间
	        plate:searchBoxs[i].plate,
          checkAll: searchBoxs[i].checkAll, // 全选
          isMultiple: searchBoxs[i].isMultiple, // 单选/多选
          expand: false, //是否展开
          show: false, //列表是否显示
          shoWIcon: false, //展开按钮是否显示
          customTime:searchBoxs[i].customTime
        });
        
      
        if (i < this.expandRow) {
          this.$set(this.item[i], "show", true);
        }
      }
    }
  },
  mounted() {
    this.$nextTick(() => {
      //this.getObjHeight();
      setTimeout(this.getObjHeight,500);
      this.flag=true
      let _this = this;
      setTimeout(function(){
        _this.singelCheckIndex = [];
        for (var i = 0; i < _this.searchBoxs.length; i++) {      

              let defalutValue=_this.searchBoxs[i].defalut;
              let data=_this.searchBoxs[i].data;
              // 单选项目有默认值时
              if(defalutValue.length>0){
                let index = data.findIndex((d)=>{
                  return d.value == defalutValue[0];
                });
                _this.singelCheckIndex.push(index)   
              }else{
                 _this.singelCheckIndex.push("")   
              }
              
           

          }
      },500);
      
    });
   

  }
};
</script>
<style lang="scss" scoped>
p{
  margin-bottom: 0;
}
.fl {
  float: left;
}

.searchform {
  height: auto;
  background: #181e3e;
  position: relative;
}
.itemBox {
  border-bottom: 1px dashed #303455;
  padding: 10px 20px;
  font-size: 14px;
  line-height: 26px;
  font-family: "微软雅黑";
  height: auto;
  position: relative;
}
.dateBox {
  display: flex;
  margin-right: 20px;
}
.itemBox span.itemLabel {
  display: block;
  width: 80px;
  margin-right: 10px;
  text-align: right;
}
.item {
  margin-left: 10px;
  font-size: 12px;
  height: 28px;
  overflow: hidden;
}
.item.active {
  height: auto;
}
.item a {
  padding: 0px 10px;
  display: inline-block;
  color: #fff;
  line-height: 28px;
  top: 1px;
  position: relative;
  border-radius: 2px;
  height: 28px;
}
.item a.on {
  background: linear-gradient(#089ab6, #2261dc);
}
.item a span.txt {
  position: relative;
  top: -2px;
}
.searchform >>> .c-checkbox-inner {
  top: -2px;
}
.c-checkbox-label {
  margin-top: -5px;
}
.searchform >>> .item .c-checkbox-inner-check {
  top: -5px;
}
.searchform >>> .item .c-checkbox-label span {
  vertical-align: text-bottom;
}
.searchform .checkAll .c-checkbox-inner {
  top: 6px;
  margin-right: 5px;
  margin-left: -10px;
}
.searchform .item .checkAll .c-checkbox-inner-check {
  top: -8px;
}
.c-checkbox-inner.on {
  background-color: #1dabe9;
}
.arrow {
  position: absolute;
  right: 10px;
  top: 12px;
  color: #1ca3de;
}
.dateBox a.btn{
 background: linear-gradient(#089ab6, #2261dc);
 display:inline-block;
 padding:0 10px;
 color:#fff;
 line-height: 34px;
}

.btnBg {
  height: 20px;
  position: absolute;
  bottom:-20px;
  margin-top: -1px;
  z-index: 99;
  left: 50%;

}
.arrowDown {
  display: block;
  width: 40px;
  height: 20px;
  background: #131737;
  margin: 0 auto;
  text-align: center;
}
.arrowDown:hover {
  color: #06c;
}
.itemBox {
  border-bottom: 1px dashed #303455;
  padding: 10px 20px;
  font-size: 14px;
  line-height: 26px;
  font-family: "微软雅黑";
  height: auto;
  position: relative;
}
.dateBox {
  display: flex;
  margin-right: 20px;
}
.itemBox span.itemLabel {
  color: #5782b2;
  display: block;
  width: 90px;
  margin-right: 10px;
  text-align: right;
}
.item {
  margin-left: 10px;
  font-size: 12px;
  height: 28px;
  overflow: hidden;
}
.item.active {
  height: auto;
}
.item a {
  margin-right: 10px;
  padding: 0px 10px;
  display: inline-block;
  color: #fff;
  line-height: 28px;
  top: 1px;
  position: relative;
  border-radius: 2px;
  height: 28px;
}
.item a.on {
  background: linear-gradient(#089ab6, #2261dc);
}
.item a span.txt {
  position: relative;
  top: -2px;
}
.searchform >>> .c-checkbox-inner {
  top: -2px;
}
.c-checkbox-label {
  margin-top: -5px;
  margin-right: 20px;
}
.searchform >>> .item .c-checkbox-inner-check {
  top: -5px;
}
.searchform >>> .item .c-checkbox-label span {
  vertical-align: text-bottom;
}
.searchform .checkAll .c-checkbox-inner {
  top: 6px;
  margin-right: 5px;
  margin-left: -10px;
}
.searchform .item .checkAll .c-checkbox-inner-check {
  top: -8px;
}
.c-checkbox-inner.on {
  background-color: #1dabe9;
}
.arrow {
  position: absolute;
  right: 10px;
  top: 12px;
  color: #1ca3de;
}
.arrowDown {
  display: block;
  width: 40px;
  height: 20px;
  background: #131737;
  margin: 0 auto;
  text-align: center;
}
.arrowDown:hover {
  color: #06c;
}

</style>


