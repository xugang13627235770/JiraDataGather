<template>
<div class="chartPanel">
   <div  class="modal" ref="pieChart"></div>
</div>
</template>

<script>
export default {
    data(){
        return {
           pieChart:null,
           pieOption:{
              title : {
                    text: '迭代故事点数人员占比',
                    x:'left',
                    top:10,
                    textStyle:{
                         color:"#57c6f4",
                         fontSize:14,
                    }
                },
                tooltip : {
                    trigger: 'item',
                    formatter: "{a} <br/>{b} : {c} ({d}%)"
                },
                legend: {
                    type: 'scroll',
                    orient: 'horizontal',
                    textStyle:{
                        color:"#57c6f4"
                    },
                    bottom: 20,
                    data: []

                    // selected: data.selected
                },
                series : [
                    {
                        name: '姓名',
                        type: 'pie',
                        radius : '55%',
                        center: ['40%', '50%'],
                        data: [],
                        itemStyle: {
                            emphasis: {
                                shadowBlur: 10,
                                shadowOffsetX: 0,
                                shadowColor: 'rgba(0, 0, 0, 0.5)'
                            }
                        }
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
        setPieOptionn(){
             if(this.pieChart){
                this.pieChart.clear();
                this.pieChart.setOption(this.pieOption,true);
                this.pieChart.resize();
            }
        },
        setPieChart() {
            this.$nextTick(()=>{
                let result=null;
                result=this.getData(this.chartData);
                if(result&&result.legendData.length&&result.seriesData.length){
                   this.$set(this.pieOption.legend,"data",result.legendData)
                   this.$set(this.pieOption.series[0],"data",result.seriesData)
                   this.setPieOptionn()
                }
            })
        },
        // 处理数据
        getData(data){
            var legendData = [];
            var seriesData = [];
            var selected = {};
            for (var i = 0; i < data.length; i++) {

                legendData.push(data[i].name);
                seriesData.push({
                    name: data[i].name,
                    value: data[i].value
                });
                // selected[nameList[i].name] = i < 6;
            }
            return {
                legendData: legendData,
                seriesData: seriesData,
                // selected: [selected]
            };
        }

    },
    created(){
    },
    mounted(){
        this.$nextTick(() => {
        setTimeout(() => {
            this.pieChart =this.$echarts.init(this.$refs.pieChart);
            this.setPieChart()
            this.pieChart.resize();
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
