<template>
    <div id="baseConfig">
        <c-form ref='configForm'>
            <div class="titleSelection" v-for="(item , index) in formData" :key="index">
                <h5 v-if="showH5(item)">
                    {{item.label}} 
                    <c-icon type='plus' v-if="item.type=='series'" class="addSeries" @click.native='addSeries' title="点击添加系列"></c-icon> 
                    <c-icon type='plus' v-if="item.type=='gridHeader'" class="addHeader" @click.native='addHeader' title="点击添加列"></c-icon> 
                </h5>
                <div class="selectionChild" v-for="(itemSelect , indexSelect) in item.child" :key="indexSelect">
                    <c-form-item
                        v-if="itemSelect.type=='input'&&currentConfig.indexOf(itemSelect.modeKey)!=-1"
                        :need='itemSelect.need'
                        :label="itemSelect.label"
                        :label-col="3">
                        <c-input
                            :name="itemSelect.modeKey"
                            v-model="model[itemSelect.modeKey]"
                            width='100%'
                            :rules="itemSelect.need?[{type:'required'}]:[]">
                        </c-input>
                    </c-form-item>
                    <c-form-item
                        v-if="itemSelect.type=='fontInput'&&currentConfig.indexOf(itemSelect.modeKey)!=-1"
                        :need='itemSelect.need'
                        :label="itemSelect.label"
                        class="fontColorInput"
                        :label-col="3">
                        <c-input
                            :name="itemSelect.modeKey"
                            v-model="model[itemSelect.modeKey]"
                            width='100%'
                            :rules="itemSelect.need?[{type:'required'}]:[]">
                        </c-input>
                        <span class="colorView" :style="{'background':model[itemSelect.modeKey]}"></span>
                        <span class="colorChoose" @click='toggleColorPick(itemSelect.modeKey,indexSelect)'></span>
                        <transition name="fadeDown">
                            <div class="pickContent" v-if="currentColorKey===itemSelect.modeKey&&cueerntTargetIndex===indexSelect" >
                                <sketch-picker v-model="colorPick" class="colorPick"  @input="updateFormValue"></sketch-picker>
                            </div>
                        </transition>
                    </c-form-item>
                    <c-form-item
                        v-if="itemSelect.type=='number'&&currentConfig.indexOf(itemSelect.modeKey)!=-1"
                        :need='itemSelect.need'
                        :label="itemSelect.label"
                        :label-col="3">
                        <c-input-number
                            :name="itemSelect.modeKey"
                            v-model="model[itemSelect.modeKey]"
                            width='100%'
                            :rules="itemSelect.need?[{type:'required'}]:[]">
                        </c-input-number>
                    </c-form-item>
                    <c-form-item
                        v-if="itemSelect.type=='select'&&currentConfig.indexOf(itemSelect.modeKey)!=-1"
                        :need='itemSelect.need'
                        :label="itemSelect.label"
                        :label-col="3">
                        <c-select :name="itemSelect.modeKey" v-model="model[itemSelect.modeKey]" :options="currentConfigOptions(itemSelect.modeKey)" 
                            :cancelled='false' :multiple='itemSelect.multiple' width="100%"></c-select>
                    </c-form-item>
                    <c-form-item
                        v-if="itemSelect.type=='radioGroup'&&currentConfig.indexOf(itemSelect.modeKey)!=-1"
                        :need='itemSelect.need'
                        :label="itemSelect.label"
                        :label-col="3">
                        <c-radio-group
                            :name="itemSelect.modeKey"
                            v-model="model[itemSelect.modeKey]"
                            :rules="itemSelect.need?[{type:'required'}]:[]">
                            <c-radio :label="itemRadio.value" v-for="(itemRadio , indexRadio) in currentConfigOptions(itemSelect.modeKey)" :key="indexRadio">{{itemRadio.label}}</c-radio>
                        </c-radio-group>
                    </c-form-item>
                    <c-form-item
                        v-if="itemSelect.type=='sliderRange'&&currentConfig.indexOf(itemSelect.modeKey)!=-1"
                        :need='itemSelect.need'
                        :label="itemSelect.label"
                        class="numberRange"
                        :label-col="3">
                        <c-slider class="numberRangeSlider" v-model="model[itemSelect.modeKey]" width='' :max='20'></c-slider>
                        <c-input-number
                            class="numberRangeSliderView"
                            :name="itemSelect.modeKey"
                            v-model="model[itemSelect.modeKey]"
                            width='80px'
                            :max='20'
                            :rules="itemSelect.need?[{type:'required'}]:[]">
                        </c-input-number>
                    </c-form-item>
                    <c-form-item
                        v-if="itemSelect.type=='areaSize'&&currentConfig.indexOf(itemSelect.modeKey)!=-1"
                        :need='itemSelect.need'
                        :label="itemSelect.label"
                        :label-col="3">
                        <div class="areaSize" :class="{'mr10':indexSize!=currentConfigOptions(itemSelect.modeKey).length-1}" 
                            v-for="(itemSize , indexSize) in currentConfigOptions(itemSelect.modeKey)" :key="indexSize">
                            <c-input-number
                                :name="itemSize.modeKey"
                                v-model="model[itemSize.modeKey]"
                                class="mr10"
                                width='100%'
                                :rules="itemSelect.need?[{type:'required'}]:[]">
                            </c-input-number>
                            <span v-if="itemSize.label">{{itemSize.label}}</span>
                        </div>
                    </c-form-item>
                    <c-form-item
                        v-if="itemSelect.type=='inputList'&&currentConfig.indexOf(itemSelect.modeKey)!=-1"
                        :need='itemSelect.need'
                        :label="itemSelect.label"
                        :label-col="3">
                        <div class="nameArea" v-for="(itemChart , indexChart) in currentConfigOptions(itemSelect.modeKey)" :key="indexChart">
                            <c-input
                                class="mb5"
                                :name="itemChart.modeKey"
                                v-model="model[itemChart.modeKey]"
                                width='100%'
                                :rules="itemSelect.need?[{type:'required'}]:[]">
                            </c-input>
                            <span class="nameText bordered-r">{{itemChart.label}}</span>
                        </div>
                    </c-form-item>
                    <c-form-item
                        v-if="itemSelect.type=='switch'&&currentConfig.indexOf(itemSelect.modeKey)!=-1"
                        :need='itemSelect.need'
                        :label="itemSelect.label"
                        :label-col="3">
                        <c-switch :name="itemSelect.modeKey" v-model="model[itemSelect.modeKey]" :rules="itemSelect.need?[{type:'required'}]:[]"></c-switch>
                    </c-form-item>
                    <div class="seriesClass" v-if="item.type=='series'">
                        <c-form-item
                            v-for="(seriesItem , seriesIndex) in model['serieslist']" :key="seriesIndex"
                            :label="'系列'+(seriesIndex+1)"
                            :label-col="3">
                            <div class="seriesChild">
                                <c-input
                                    class="mb5"
                                    name="seriesName"
                                    v-model="seriesItem.seriesName"
                                    width='100%'>
                                </c-input>
                            </div>
                            <div class="fontColorInput seriesChild">
                                <div class="inline">
                                    <c-input
                                        name="seriesColor"
                                        v-model="seriesItem.seriesColor"
                                        width='100%'>
                                    </c-input>
                                    <span class="colorView" :style="{'background':seriesItem.seriesColor}"></span>
                                    <span class="colorChoose" @click='toggleColorPick("seriesColor",seriesIndex)'></span>
                                    <transition name="fadeDown">
                                        <div class="pickContent" v-if="currentColorKey==='seriesColor'&&cueerntTargetIndex===seriesIndex" >
                                            <sketch-picker v-model="colorPick" class="colorPick"  @input="updateFormValue"></sketch-picker>
                                        </div>
                                    </transition>
                                </div>
                            </div>
                            <i class="iconfont icon-shanchu" title="删除系列" @click="deleteSeries(seriesIndex)"></i>
                        </c-form-item>
                    </div>
                    <c-form-item
                        v-if="itemSelect.type=='addImage'&&currentConfig.indexOf(itemSelect.modeKey)!=-1"
                        :need='itemSelect.need'
                        :label="itemSelect.label"
                        :label-col="3">
                        <div class="uploadContent bordered">
                            <input type="file" name='files' accept="image/*" id="uploadBGImages" title="" @change="imgDropFun($event)">
                            <label class="exampleIcon" for='uploadBGImages' @drop="imgDrop" @dragover.prevent :style="exampleIconBg">
                                <i class="iconfont icon-tupian" v-if="!model.basicImage"></i>
                                <p v-if="!model.basicImage">点击或拖拽图片到这里更换</p>
                            </label>
                            <i class="iconfont icon-shanchu" v-if="model.basicImage" @click.stop="deleteBG"></i>
                        </div>
                    </c-form-item>
                    
                    <div class="seriesClass" v-if="item.type=='gridHeader'">
                        <c-form-item
                            v-for="(headerItem , headerIndex) in model['headerList']" :key="headerIndex"
                            :label="'列'+(headerIndex+1)"
                            :label-col="3">
                            <div class="seriesChild">
                                <c-input
                                    class="mb5"
                                    name="seriesName"
                                    placeholder='列显示名'
                                    v-model="headerItem.viewName"
                                    width='100%'>
                                </c-input>
                            </div>
                            <div class="seriesChild">
                                <c-input
                                    class="mb5"
                                    name="seriesName"
                                    placeholder='列字段名'
                                    v-model="headerItem.seriesName"
                                    width='100%'>
                                </c-input>
                            </div>
                            <i class="iconfont icon-shanchu" title="删除系列" @click="deleteHeader(headerIndex)"></i>
                        </c-form-item>
                    </div>
                </div>
            </div>
        </c-form>
        <input type="hidden" v-model="currentItemIndex">
    </div>
</template>
<script>
import Bus from '../../bus.js'
import store from '../../../../vuex/store'
import baseConfigFormData from './baseConfigFormData.js'
import configData from '../../configData.js'
import { Sketch } from 'vue-color'
export default {
    name:"baseConfig",
    components:{'sketch-picker': Sketch,},
    props:{
        
    },
    data(){
        return{
            baseConfigFormData:JSON.parse(JSON.stringify(baseConfigFormData)),
            formData:[],
            colorPick:{
                hex: '#194d33',
                hsl: { h: 150, s: 0.5, l: 0.2, a: 1 },
                hsv: { h: 150, s: 0.66, v: 0.30, a: 1 },
                rgba: { r: 25, g: 77, b: 51, a: 1 },
                a: 1
            },
            currentColorKey:"",
            cueerntTargetIndex:"",
            model:{},
            currentConfig:[],
            currentIndex:[],
            eventChangeFlag:null,
            exampleIconBg:{},
        }
    },
    computed: {
        currentItemIndex(){
            let vm = this;
            vm.currentIndex = store.getters.getCurrentItemIndex;
            return store.getters.getCurrentItemIndex;
        },
        elementData(){
            return store.getters.getElement
        },
    },
    watch:{
        currentIndex(){
            let vm = this;
            let ElementData = vm.elementData;
            if(vm.currentIndex.length==1){
                vm.model = ElementData[vm.currentIndex[0]];
            }else if(vm.currentIndex.length==2){
                vm.model = ElementData[vm.currentIndex[0]].children[vm.currentIndex[1]];
            }
            vm.currentConfig = configData[vm.model.type];
        },
        'model.basicImage'(newVal,oldVal){
            this.exampleIconBg = {backgroundImage:`${'url('+ newVal +') '}`,backgroundRepeat:'no-repeat', backgroundPosition:'center center', backgroundSize: 'contain'};
        },
    },
    methods:{
        currentConfigOptions(modeKey){
            let vm = this;
            if(vm.currentIndex.length>0){
                let ElementData = vm.elementData;
                let currentItem = {};
                if(vm.currentIndex.length==1){
                    currentItem = ElementData[vm.currentIndex[0]];
                }else if(vm.currentIndex.length==2){
                    currentItem = ElementData[vm.currentIndex[0]].children[vm.currentIndex[1]];
                }
                return configData[currentItem.type+"Options"][modeKey];
            }
            return [];
        },
        toggleColorPick(modeKey,indexSelect){
            this.cueerntTargetIndex = this.cueerntTargetIndex+""?'':indexSelect;
            this.currentColorKey = this.currentColorKey?'':modeKey;
            // debugger
            // this.colorPick.hex = this.currentColorKey&&this.model[modeKey]?this.model[modeKey]:'#194d33';
        },
        updateFormValue(color){
            let rgba = "rgba("+color.rgba.r+","+ color.rgba.g+","+color.rgba.b+","+color.rgba.a+")";
            if(this.currentColorKey!='seriesColor'){
                this.$set(this.model,this.currentColorKey,rgba);
            }else{
                this.$set(this.model.serieslist[this.cueerntTargetIndex],'seriesColor',rgba);
            }
        },
        showH5(item){
            let vm = this;
            let filter = item.child.filter(child=>vm.currentConfig.indexOf(child.modeKey)!=-1);
            return filter.length>0;
        },
        addSeries(){
            let newObj = {seriesName:"",seriesColor:"",seriesKey:"",seriesRemark:"系列"};
            this.$set(this.model.serieslist,this.model.serieslist.length,newObj);
        },
        deleteSeries(seriesIndex){
            this.model.serieslist.splice(seriesIndex,1);
        },
        addHeader(){
            let newObj = {seriesName:"",viewName:""};
            this.$set(this.model.headerList,this.model.headerList.length,newObj);
        },
        deleteHeader(headerIndex){
            this.model.headerList.splice(headerIndex,1);
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
                vm.$set(vm.model,'basicImage',this.result);
            };
        },
        deleteBG(){
            let vm = this;
            vm.$set(vm.model,'basicImage','');
        },
    },
    mounted(){
        let vm = this;
        vm.$nextTick(()=>{
            vm.formData = JSON.parse(JSON.stringify(baseConfigFormData));
            document.addEventListener("click",function(e){
	            if(e.target.className && e.target.className.includes("colorChoose"))return
                let colorPickTarget = document.querySelectorAll(".colorPick");
                for(let i = 0;i<colorPickTarget.length;i++){
                    let item = colorPickTarget[i];
                    if(!item.contains(e.target)){
                        vm.cueerntTargetIndex = '';
                        vm.currentColorKey = '';
                    }
                }
            })
        })
    },
}
</script>
<style lang="scss" scoped>
#baseConfig{
    width: 100%;
    height: 100%;
    .titleSelection{
        h5{
            color: rgba(255,255,255,.5);
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
}
</style>
<style lang="scss">
#baseConfig{
    .addSeries {
        cursor: pointer;
        margin-left: 10px;
    }
    .seriesClass{
        min-height: 20px;
        .seriesChild{
            width: 90%;
        }
        .c-col-sm-9.inline{
            position: relative;
            .icon-shanchu{
                position: absolute;
                right: 0;
                top: 50%;
                transform: translate(0 , -50%);
                cursor: pointer;
            }
        }
    }
}
</style>