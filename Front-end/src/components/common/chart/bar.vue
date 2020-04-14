<template>
<div class="chartPanel">
   <div  class="modal" ref="barChart"></div>
</div>
</template>

<script>
export default {
    data(){
        return {
           barChart:null,
           barOption:{
               title: {
                    text: '问题类型统计',
                    x:'left',
                    top:10,
                    textStyle:{
                       color:"#57c6f4",
                       fontSize:14
                    }
                },
               tooltip : {
                    trigger: 'axis',
                    axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                        type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                    }         
                },
                legend: {
                    textStyle:{
                        color:"#57c6f4"
                    },
                    bottom:0,
                    data: ['关闭', '未关闭']
                },
                grid: {
                    show: false,
                    bottom: 60
                },
                xAxis:  {
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
                yAxis: {
                    type: 'category',
                    axisLabel: {
                        color: "#57c6f4"
                    },
                    splitLine: {
                        // grid 区域中的分隔线
                        show: false
                    },
                    axisLine: {
                        show: true,
                        lineStyle:{
                            color:'#57c6f4'
                        }
                    },
                    data: []
                },
                series: [
                    {
                        name: '关闭',
                        type: 'bar',
                        stack: '总量',
                        label: {
                            normal: {
                                show: true,
                                position: 'insideRight'
                            }
                        },
                        data: []
                    },
                    {
                        name: '未关闭',
                        type: 'bar',
                        stack: '总量',
                        label: {
                            normal: {
                                show: true,
                                position: 'insideRight'
                            }
                        },
                        data:[]
                    }
                ]

           }
                
        }
    },
    props: {
        chartData: {
        type: [Array],
        default: []
        }
    },
    methods: {
        // 基础配置
        setBarOption(){
             if(this.barChart){
                this.barChart.clear();
                this.barChart.setOption(this.barOption,true);
                this.barChart.resize();
            }
        },
        setBarChart() {
            this.$nextTick(()=>{
                let data=this.chartData,yAxisData=[],closeData=[],unCloseData=[],seriesData=[];
                if(data.length){
                    data.forEach(item => {
                        yAxisData.push(item.name)
                        closeData.push(item.closeNum)
                        unCloseData.push(item.unCloseNum)
                    });
                   this.$set(this.barOption.yAxis,"data",yAxisData)
                   this.$set(this.barOption.series[0],"data",closeData)
                   this.$set(this.barOption.series[1],"data",unCloseData)
                   this.setBarOption()
                }
            })
        }

    },
    created(){
    },
    mounted(){
        this.$nextTick(() => {
        setTimeout(() => {
            this.barChart =this.$echarts.init(this.$refs.barChart);
            this.setBarChart()
            this.barChart.resize();
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