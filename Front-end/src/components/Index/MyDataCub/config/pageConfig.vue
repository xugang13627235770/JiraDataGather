<template>
    <div id="pageConfig">
        <div class="configTab inblock3">页面设置</div>
        <div class="configContent">
            <c-form ref='pageConfigForm'>
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
                <c-form-item
                    label="背景颜色"
                    class="fontColorInput"
                    :label-col="3">
                    <c-input
                        name="pageBGColor"
                        v-model="model.pageBGColor"
                        width='100%'>
                    </c-input>
                    <span class="colorView" :style="{'background':model.pageBGColor}"></span>
                    <span class="colorChoose" @click='toggleColorPick("pageBGColor")'></span>
                    <transition name="fadeDown">
                        <div class="pickContent" v-if="currentColorKey==='pageBGColor'" >
                            <sketch-picker v-model="colorPick" class="colorPick"  @input="updateFormValue"></sketch-picker>
                        </div>
                    </transition>
                </c-form-item>
                <c-form-item
                    label="背景图"
                    :label-col="3">
                    <div class="uploadContent bordered">
                        <input type="file" name='files' accept="image/*" id="uploadBGImages" title="" @change="imgDropFun($event)">
                        <label class="exampleIcon" for='uploadBGImages' @drop="imgDrop" @dragover.prevent :style="exampleIconBg">
                            <i class="iconfont icon-tupian" v-if="!model.bgImage"></i>
                            <p v-if="!model.bgImage">点击或拖拽图片到这里更换</p>
                        </label>
                        <i class="iconfont icon-shanchu" v-if="model.bgImage" @click.stop="deleteBG"></i>
                    </div>
                </c-form-item>
                <c-form-item
                    :label="'缩放比 ( '+ scaleFun() +' )'"
                    class="numberRange"
                    :label-col="3">
                    <c-slider class="numberRangeSlider" v-model="model.scale" width='' :min='0' :max='2' :step='step'></c-slider>
                    <div class="scaleControl">
                        <c-icon type='minus' @click.native='model.scale-=step'></c-icon>
                        <c-icon type='plus' @click.native='model.scale+=step'></c-icon>
                    </div>
                </c-form-item>
            </c-form>
        </div>
    </div>
</template>
<script>
import store from '../../../../vuex/store'
import { Sketch } from 'vue-color'
export default {
    name:"pageConfig",
    components:{'sketch-picker': Sketch,},
    data(){
        return{
            currentColorKey:"",
            colorPick:{
                hex: '#194d33',
                hsl: { h: 150, s: 0.5, l: 0.2, a: 1 },
                hsv: { h: 150, s: 0.66, v: 0.30, a: 1 },
                rgba: { r: 25, g: 77, b: 51, a: 1 },
                a: 1
            },
            modelFlag:null,
            step:0.1,
        }
    },
    computed: {
        model(){
            return store.getters.getPageConfig;
        },
        exampleIconBg(){
            return  {backgroundImage:`${'url('+ store.getters.getPageConfig.bgImage +') '}`,backgroundRepeat:'no-repeat', backgroundPosition:'center center', backgroundSize: 'contain'};
        },  
    },
    watch:{
        
    },
    methods:{
        scaleFun(){
            return this.model.scale.toFixed(1);
        },
        toggleColorPick(modeKey,indexSelect){
            this.currentColorKey = this.currentColorKey?'':modeKey;
        },
        updateFormValue(color){
            let rgba = "rgba("+color.rgba.r+","+ color.rgba.g+","+color.rgba.b+","+color.rgba.a+")";
            this.$set(this.model,this.currentColorKey,rgba);
            let pageConfig = store.getters.getPageConfig;
            pageConfig.pageBGColor = rgba;
            store.dispatch('setPageConfig', JSON.parse(JSON.stringify(pageConfig)));
        },
        imgDropFun(e){
            let vm = this;
            vm.getBase64(e.target.files[0]);
        },
        imgDrop(e){
            let vm = this;
            e.preventDefault();
            e.stopPropagation();
            if (e.type === 'drop') {
                let files = e.dataTransfer.files || {};
                vm.getBase64(files[0]);
            }
        },
        getBase64(file_){
            let vm = this;
            let reader = new FileReader();
            if (file_) {
                reader.readAsDataURL(file_, "UFT-8");
            }
            reader.onload = function(file) {
                let pageConfig = store.getters.getPageConfig;
                pageConfig.bgImage = this.result;
                store.dispatch('setPageConfig', JSON.parse(JSON.stringify(pageConfig)));
            };
        },
        deleteBG(){
            let vm = this;
            let pageConfig = store.getters.getPageConfig;
            pageConfig.bgImage = '';
            store.dispatch('setPageConfig', JSON.parse(JSON.stringify(pageConfig)));
            // vm.$set(vm,'exampleIconBg',null);
        },
    },
    mounted(){
        let vm = this;
        vm.$nextTick(()=>{
            document.addEventListener("click",function(e){
                if(e.target.className && e.target.className.includes("colorChoose"))return
                let colorPickTarget = document.querySelectorAll(".colorPick");
                for(let i = 0;i<colorPickTarget.length;i++){
                    let item = colorPickTarget[i];
                    if(!item.contains(e.target)){
                        vm.currentColorKey = '';
                    }
                }
            })
        })
    },
}
</script>
<style lang="scss" scoped>
#pageConfig{
    width: 100%;
    height: 100%;
    .configTab{
        width: 100%;
        height: 36px;
        position: relative;
        text-align: center;
        line-height: 36px;
        color: #fff;
        font-size: 14px;
    }
    .configContent{
        width: 100%;
        height: calc(100% - 36px);
        padding: 20px 10px 0 0;
        overflow: hidden;
        overflow-y: auto;
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
        .uploadContent{
            width: 100%;
            height: 175px;
            position: relative;
            border-radius: 4px;
            #uploadBGImages{
                width: 100%;
                height: 100%;
                opacity: 0;
                position: absolute;
                top: 0;
                left: 0;
                z-index: 1;
            }
            .icon-shanchu{
                position: absolute;
                top: 5px;
                right: 10px;
                z-index: 3;
                cursor: pointer;
            }
            .exampleIcon{
                width: 100%;
                height: 100%;
                position: absolute;
                display: block;
                z-index: 2;
                top: 0;
                left: 0;
                cursor: pointer;
                transition: all .3s;
                &:hover{
                    background: rgba(35,45,80,.3);
                }
                .iconfont.icon-tupian{
                    font-size: 70px;
                    position: absolute;
                    top:calc(50% - 20px);
                    left: 50%;
                    transform: translate(-50% , -50%);
                    z-index: 1;
                    color: #2f3b63;
                }
                p{
                    position: absolute;
                    bottom: 30px;
                    right: 0;
                    width: 100%;
                    height: 30px;
                    z-index: 1;
                    text-align: center;
                    color: #3F5C86;
                }
            }
        }
        .scaleControl{
            display: inline-block;
            width: 60px;
            >i{
                margin-right: 10px;
                cursor: pointer;
                &:nth-child(1){
                    margin-left: 10px;
                }
            }
        }
        .scaleVal{
            display: inline-block;
        }
    }
    
}
</style>
<style lang="scss">
#pageConfig{
    .fontColorInput {
        input[type=text]{
            padding-left: 43px;
            padding-right: 30px;
        }
        .inline{
            position: relative;
            .colorView{
                position: absolute;
                width: 30px;
                height: 20px;
                // background: #0899B6;
                top: 6px;
                left: 12px;
                z-index: 99;
            }
            .colorChoose{
                background: url('../../../../../static/images/colorPickIcon.png') center center no-repeat;
                position: absolute;
                width: 20px;
                height: 20px;
                border-radius: 50%;
                top: 6px;
                right: 12px;
                z-index: 99;
                cursor: pointer;
            }
        }
    }
    .numberRangeSlider{
        width: calc(100% - 100px);
    }
    .numberRangeSliderView{
        float: right;
    }
    .c-upload-drag-container{
        width: 273px !important;
    }
    .fontColorInput .inline .pickContent {
        padding: 5px;
        background: #101417;
        position: absolute !important;
        right: 10px;
        top: 32px;
        z-index: 100;
        .colorPick {
            background: #101417;
            border: 1px solid #1A62BE !important;
            border-radius: 0 !important;
            .vc-sketch-presets {
                border-top: 1px solid #1A62BE !important;
            }
            .vc-input__input {
                background: #262C32 !important;
                border: none !important;
                -webkit-box-shadow: unset !important;
                box-shadow: unset !important;
                height: 25px;
            }
            .vc-input__label{
                color: #fff !important;
                height: 25px;
                line-height: 20px;
            }
            .vc-sketch-presets{
                border-top: 1px solid #1A62BE !important;
            }
        }
    }
}
</style>