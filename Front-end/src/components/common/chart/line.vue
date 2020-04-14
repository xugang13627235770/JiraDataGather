<template>
<div class="chartPanel">
   <div  class="modal" ref="lineChart"></div>
</div>
</template>

<script>
export default {
    data(){
        return {
           lineChart:null,
           lineOption:{
              title: {
                    text: '总问题累计（仅显示仅30日）',
                    x:'left',
                    top:10,
                    textStyle:{
                       color:"#57c6f4",
                       fontSize:14
                    }
                },
                tooltip: {
                    trigger: 'axis'
                },
                legend: {
                     textStyle:{
                        color:"#57c6f4"
                    },
                    bottom:0,
                    data:[]
                },
                grid: {
                    bottom: 40,
                    containLabel: true
                },
                xAxis: {
                    type: 'category',
                    boundaryGap: false,
                    axisLabel: {
                        // X轴文本
                        inside: false,
                        interval:0,
                        textStyle: {
                            color: '#57c6f4'
                        }
                    
                    },
                    splitLine: {
                        // grid 区域中的分隔线
                        show: false
                    },
                    axisTick: {
                        // 刻度
                        show: false,
                        inside:true

                    },
                    axisLine: {
                        show: true,
                        lineStyle:{
                            color:'#57c6f4'
                        }
                    },
                    data: []
                },
                yAxis: {
                    type: 'value',
                    axisLabel: {
                        // X轴文本
                        inside: false,
                        interval:0,
                        textStyle: {
                            color: '#57c6f4'
                        }
                    
                    },
                    splitLine: {
                        // grid 区域中的分隔线
                        show: false
                    },
                    axisTick: {
                        // 刻度
                        show: false,
                        inside:true

                    },
                    axisLine: {
                        show: true,
                        lineStyle:{
                            color:'#57c6f4'
                        }
                    }
                },
                series: []
           }
                
        }
    },
    props: {
        chartData: {
        type: [Object],
        default:{}
        }
    },
    methods: {
        // 基础配置
        setLineOption(){
             if(this.lineChart){
                this.lineChart.clear();
                this.lineChart.setOption(this.lineOption,true);
                this.lineChart.resize();
            }
        },
        setlineChart() {
            this.$nextTick(()=>{
                let data=this.chartData,yAxisData=[],xAxisData=[],legendData=[],seriesData=[],list=[];
                if(data){
                   xAxisData=data.timeArr
                   list=data.list
                   list.forEach(item=>{
                       legendData.push(item.type)
                       let obj={
                           name:item.type,
                           type:'line',
                           data:item.data
                       }
                      seriesData.push(obj)
                   })
                 
                   this.$set(this.lineOption.legend,"data",legendData)
                   this.$set(this.lineOption.xAxis,"data",xAxisData)
                   this.$set(this.lineOption,"series",seriesData)
                   this.setLineOption()
                }
            })
        }

    },
    created(){
    },
    mounted(){
        this.$nextTick(() => {
        setTimeout(() => {
            this.lineChart =this.$echarts.init(this.$refs.lineChart);
            this.setlineChart()
            this.lineChart.resize();
            window.addEventListener("resize", function() {
                vm.chart.resize();
            });
        }, 500);
        });
    }
}

</script>
<style lang="scss" scoped>
.chartPanel{
    height: calc(100%);
    .modal{
        height: calc(100%);
    }
}

</style>