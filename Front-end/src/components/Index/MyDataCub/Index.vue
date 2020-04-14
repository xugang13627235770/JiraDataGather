<template>
    <div id="myDataCub" :class="{'empty':boardData.length==0}">
        <p class="emptyText" v-if="boardData.length==0">暂无数据</p>
        <div class="cubArea">
            <div class="boardArea addMyData">
                <div class="btnArea" @click='addBoard'>
                    <i class="iconfont icon-jia1"></i>
                    <span>新建看板</span>
                </div>
            </div>
            <div class="boardArea board bordered" v-for="(item , index) in boardData" :key="index">
                <div class="imgContent">
                    <img src="../../../../static/images/boardView.png" alt="">
                    <div class="mask">
                        <c-button type='primary' class="editBtn" @click.native='editBoard(item)'>编辑</c-button>
                        <i class="iconfont icon-shanchu deleteBtn" @click='deleteBoard(item)'></i>
                    </div>
                </div>
                <div class="toolContent inblock3">
                    <span>{{item.displayName}}</span>
                    <i class="iconfont icon-huaban imgIcon" :title="item.isPublish==1?'已发布':'发布'" @click='publishDisplay(item)' :class="{'alreadyPublish':item.isPublish==1}"></i>
                    <i class="iconfont icon-yulan imgIcon" title="预览" @click="preViewFun(item,'out')"></i>
                </div>
            </div>
        </div>

        <transition name="scale">
            <add-board ref="addBoard" v-if="showAddBoard" @close='closeAddBoard' @search='getDeviceByDepater' @cleanData='cleanData' @preViewFun='preViewFun'
                ></add-board>
        </transition>

        <!-- 预览 -->
        <transition name="scale">
            <pre-view ref="preView" v-if="showPreView" @closePreView='closePreView'></pre-view>
        </transition>

        <!-- 新建弹出层 -->
        <c-modal title=""  effect="fade" width="510px" height='310px' ref="addBoardModel" id="addBoardModel">
            <div slot="header">
                <h5 class="inblock3">新建看板 <i class="iconfont icon-guanbi" @click='calcelAdd'></i> </h5>
            </div>
            <div slot="body" class="addBoardModelBody">
                <p> <c-icon type='info-circle'></c-icon> 看板名称和尺寸创建后可修改 </p>
                <c-form ref='addBoardModelForm'>
                    <c-form-item
                        need
                        label="看板名称"
                        :label-col="3">
                        <c-input
                            name="displayName"
                            width='100%'
                            v-model="model.displayName"
                            :rules="[{type:'required'}]">
                        </c-input>
                    </c-form-item>
                    <c-form-item
                        need
                        label="看板尺寸"
                        :label-col="3">
                        <div class="areaSize mr10">
                            <c-input-number
                                name="width"
                                v-model="model.width"
                                width='100%'
                                :rules="[{type:'required'}]">
                            </c-input-number>
                            <span>宽度</span>
                        </div>
                        <div class="areaSize">
                            <c-input-number
                                name="height"
                                v-model="model.height"
                                width='100%'
                                :rules="[{type:'required'}]">
                            </c-input-number>
                            <span>高度</span>
                        </div>
                    </c-form-item>
                </c-form>
            </div>
            <div slot="footer" class="addBoardModelFooter">
                <c-button type='default' @click.native='calcelAdd'>取消</c-button>
                <c-button type='primary' class="ml10" @click.native='starAdd'>创建</c-button>
            </div>
        </c-modal>

        
        <!-- 发布弹出层 -->
        <c-modal title=""  effect="fade" width="510px" height='330px' ref="publishModel" id="publishModel">
            <div slot="header">
                <h5 class="inblock3">发布 <i class="iconfont icon-guanbi" @click='calcelPublish'></i></h5>
            </div>
            <div slot="body" class="addBoardModelBody">
                <p> <c-icon type='info-circle'></c-icon> 生成链接后，点击发布，复制链接到浏览器地址栏，可演示数据看版 </p>
                <div class="urlRelation">
                    <c-input v-model="publishData.foreignUrl"  width='calc(100% - 110px)'></c-input>
                    <c-button type='linear' @click.native='getUrl'>生成链接</c-button>
                    <i class="iconfont icon-fuzhi" v-show="canCopy || currentBoardData.isPublish==1" v-clipboard:copy="publishData.foreignUrl" v-clipboard:success="onCopy" v-clipboard:error="onError"></i>
                </div>
                <c-form ref='publishModelForm'>
                    <c-form-item
                        label="访问密码"
                        class="publishModelPassword"
                        :label-col="3">
                        <c-input v-model="publishData.passWord" ref="password" type='password' width='100%' v-if="!showPassWord"></c-input>
                        <c-input v-model="publishData.passWord" ref="input" width='100%' v-if="showPassWord"></c-input>
                        <i class="iconfont" :class="[!showPassWord?'icon-liulan':'icon-yincang']" @click="showPassWord=!showPassWord"></i>
                    </c-form-item>
                    <c-form-item
                        label="访问期限"
                        :label-col="3">
                        <c-datepicker
                            v-model="publishData.date"
                            width='100%'
                            :timePicker="true"
                            format="YYYY-MM-DD HH:mm:ss" >
                        </c-datepicker>
                    </c-form-item>
                </c-form>
            </div>
            <div slot="footer" class="addBoardModelFooter">
                <c-button type='default' @click.native='calcelPublish'>关闭</c-button>
                <c-button type='primary' class="ml10" @click.native='publishSave'>发布</c-button>
            </div>
        </c-modal>
        
        <c-page-loading color="primary" v-if="loading"></c-page-loading>
    </div>
</template>
<script>
import store from '../../../vuex/store'
import api from '../../../api/api.js'
import board from './js/board.js'
import addBoard from './addBoard'
import preView from './preview'
export default {
    name:"myDataCub",
    components:{addBoard,preView},
    data(){
        return{
            showMaskIndex:'',
            boardData:[],
            showAddBoard:false,
            elementData:[],
            showPreView:false,
            source:"",
            model:{
                width:1920,
                height:1080
            },
            publishData:{
                foreignUrl:'',
                passWord:"",
                date:""
            },
            showPassWord:false,
            currentBoardData:{},
            canCopy:false,
        }
    },
    computed: {
        loading(){
            return store.getters.getLoading;
        },
    },
    watch:{

    },
    methods:{
        addBoard(){
            board.addBoard(this);
        },
        closeAddBoard(){
            board.closeAddBoard(this);
        },
        // 获取所有看板
        getDeviceByDepater(){
            board.getDeviceByDepater(this);
        },
        deleteBoard(item){
            board.deleteBoard(this,item);
        },
        editBoard(item){
            board.editBoard(this,item);
        },
        cleanData(){
            board.cleanData(this);
        },
        publishDisplay(item){
            board.publishDisplay(this,item);
        },
        preViewFun(item,form){
            board.preViewFun(this,item,form);
        },
        closePreView(){
            board.closePreView(this);
        },
        starAdd(){
            board.starAdd(this);
        },
        calcelAdd(){
            board.calcelAdd(this);
        },
        publishSave(){
            board.publishSave(this);
        },
        calcelPublish(){
            board.calcelPublish(this);
        },
        getUrl(){
            board.getUrl(this);
        },
        onCopy(e){
            board.onCopy(this,e);
        },
        onError(e) {
            board.onError(this,e);
        }
    },
    mounted(){
        let vm = this;
        vm.$nextTick(()=>{
            vm.getDeviceByDepater();
        })
    },
}
</script>
<style lang="scss" scoped>
#myDataCub{
    width: 100%;
    height: 100%;
    padding: 50px 60px;
    position: relative;
    &.empty{
        background: url('../../../../static/images/empty.png') center center no-repeat;
    }
    .emptyText{
        position: absolute;
        left: 50%;
        top:calc(50% + 130px);
        transform: translate(-50% , -50%);
        font-size: 16px;
        margin: 0;
    }
    .cubArea{
        overflow: hidden;
        overflow-y: auto;
        width:100%;
        height: 100%;
        >div.boardArea{
            width: calc(20% - 10px);
            height: 260px;
            float: left;
            margin-right: 10px;
            margin-bottom: 10px;
            &.addMyData{
                border:1px transparent solid;
                border-image:linear-gradient(to top,rgba(38,87,227,.7),rgba(4,163,176,.7)) 1 10;
                background: linear-gradient(rgba(4,163,176,.4) ,rgba(38,87,227,.3));
                position: relative;
                text-align: center;
                .btnArea{
                    width:95px;
                    height: 95px;
                    position: absolute;
                    top: 50%;
                    left: 50%;
                    transform: translate(-50% , -50%);
                    color: #fff;
                    cursor: pointer;
                    >i{
                        font-size: 45px;
                        font-weight: bold;
                    }
                    >span{
                        font-size: 16px;
                        display: block;
                    }
                }
            }
            &.board{
                position: relative;
                .imgContent{
                    width: 100%;
                    height: calc(100% - 35px);
                    img{
                        width: 100%;
                        height: 100%;
                    }
                    .mask{
                        width: 100%;
                        height: calc(100% - 35px);
                        position: absolute;
                        left: 0;
                        top: 0;
                        background: rgba(0,0,0,.2);
                        overflow: hidden;
                        z-index: -1;
                        opacity: 0;
                        transition: all .3s;
                        transform: scale(0.1,0.1);
                        .editBtn{
                            position: absolute;
                            top: 50%;
                            left: 50%;
                            transform: translate(-50% , -50%);
                        }
                        .deleteBtn{
                            position: absolute;
                            top: 10px;
                            right: 10px;
                            color: #fff;
                            cursor: pointer;
                        }
                    }
                    &:hover{
                        .mask{
                            z-index: 99;
                            opacity: 1;
                            transform: scale(1,1);
                        }
                    }
                }
                .toolContent{
                    width: 100%;
                    height: 35px;
                    line-height: 35px;
                    padding: 0 10px;
                    font-size: 15px;
                    color: #fff;
                    >span{
                        float: left;
                        width: calc(100% - 60px);
                        text-overflow: ellipsis;
                        overflow: hidden;
                        white-space: nowrap;
                    }
                    >.imgIcon{
                        float: right;
                        margin-left: 10px;
                        cursor: pointer;
                        &:hover{
                            color: #12A3D1;
                        }
                        &.alreadyPublish{
                            // color: #12A3D1;
                        }
                        &.icon-yulan{
                            font-size: 14px;
                        }
                    }
                }
            }
        }
    }
}
</style>
<style lang="scss">
.mr10{
    margin-right: 10px;
}
.fr{
    float: right;
}
.ml10{
    margin-left: 10px;
}
.mr20{
    margin-right: 20px;
}
.mb5{
    margin-bottom: 5px;
}
#addBoardModel,
#publishModel{
    .c-modal-content{
        background: #141b38 !important;
    }
    h5{
        margin: 0;
        height: 35px;
        line-height: 35px;
        text-align: center;
        padding: 0 10px;
        i{
            cursor: pointer;
            float: right;
        }
    }
    .addBoardModelFooter{
        height: 60px;
        text-align: center;
    }
    .addBoardModelBody{
        width: 100%;
        >p{
            margin-bottom: 20px;
            color: rgba(255,255,255,.5);
            >i{
                font-size: 14px;
            }
        }
    }
    .c-modal-body{
        height: calc(100% - 120px) !important;
        background: transparent !important;
    }
    .areaSize {
        float: left;
        width: calc(50% - 5px);
        .c-input-number-input-wrap{
            >.inline{
                width: 100% !important;
            }
        }
        > span{
            color: rgba(255,255,255,.3);
        }
    }
}
#publishModel{
    .c-modal-body{
        padding: 15px;
    }
    i{
        color: #5bceff !important;
    }
    .urlRelation{
        overflow: hidden;
        margin-bottom: 10px;
        padding: 0 10px;
        position: relative;
        .c-btn-linear{
            float: right;
            margin-right: 10px;
        }
        >.inline{
            float: right;
        }
        input{
            padding-right: 25px;
        }
        >.iconfont {
            position: absolute;
            right: 20px;
            top:5px;
            cursor: pointer;
            color: #5bceff !important;
        }
    }
    .publishModelPassword{
        >.inline{
            position: relative;
            input{
                padding-right: 25px;
            }
            >.iconfont {
                position: absolute;
                right: 15px;
                top: 0;
                cursor: pointer;
                transition: all .3s;
                color: #5bceff !important;
            }
        }
    }
}
</style>