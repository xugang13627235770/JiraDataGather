/***
 * isGroup： 是否为组
 * isLock: 1 锁定 2 解锁
 * isView: 3 隐藏 4 显示
 * tabData :componentConfig组件的tab
 * needAxis：　数据配置页字段映射Ｘ、Ｙ项
 */

const config = {
    // group
    group:['titleName','basicBGColor','basicArea','basicPosition'],
    groupOptions:{
        basicArea:[
            {label:"宽度",modeKey:"width"},
            {label:"高度",modeKey:"height"},
        ],
        basicPosition:[
            {label:"Left",modeKey:"left"},
            {label:"Top",modeKey:"top"},
        ],
    },
    groupBasicData:{
        type:'group',
        width:0,
        height:0,
        top:0,
        left:0,
        tabData:['configTabArea'],
        isGroup:true,
        isView:4,
        isLock:2,
        titleName: "组",
        basicBGColor:"rgba(15,29,76,0)",
        active:false,
    },
    // 饼图
    pie:['titleName','titleFontColor','titleFontSize','titleTextAlign','basicSwitch','basicBGColor','basicShape','basicArea','basicPosition','numberFontSize',
    'numberFontColor','legendSwitch','legendPosition','legendFontSize','legendFontColor','legendIcon','addSeries'],
    pieOptions:{
        titleTextAlign:[
            {label:"居中",value:"center"},
            {label:"左对齐",value:"left"},
            {label:"右对齐",value:"right"},
        ],
        basicShape:[
            {label:"实心",value:"70%"},
            {label:"环状",value:"annular"},
            {label:"南丁格尔玫瑰",value:"radius"},
        ],
        legendPosition:[
            {label:"居中",value:"center"},
            {label:"左对齐",value:"left"},
            {label:"右对齐",value:"right"},
        ],
        basicArea:[
            {label:"宽度",modeKey:"width"},
            {label:"高度",modeKey:"height"},
        ],
        basicPosition:[
            {label:"Left",modeKey:"left"},
            {label:"Top",modeKey:"top"},
        ],
        legendIcon:[
            {label:"圆形",value:"circle"},
            {label:"方形",value:"rect"},
            {label:"圆角方形",value:"roundRect"},
            {label:"菱形",value:"diamond"},
        ],
    },
    // 页面新增时的实体
    pieBasicData:{
        type:'pie',
        width:300,
        height:300,
        top:50,
        left:100,
        tabData:['configTabArea','dataTabArea'],
        basicBGColor:"rgba(15,29,76,0)",
        legendFontSize: 12,
        legendFontColor:"rgba(255,255,255,1)",
        legendPosition: "center",
        legendSwitch: true,
        numberFontSize: 12,
        titleFontSize: 16,
        titleFontColor:"rgba(255,255,255,1)",
        titleName: "饼图",
        titleTextAlign: "center",
        basicShape: "70%",
        basicSwitch: true,
        numberFontColor:'rgba(255,255,255,1)',
        dataConfig:{
            dateType:"1",
            exampleData:[
                {value:335, name:'示例1'},
                {value:310, name:'示例2'},
                {value:234, name:'示例3'},
                {value:135, name:'示例4'},
            ]
        },//数据配置
        isLock:2,
        isView:4,
        active:false,
        legendIcon:"circle",
        serieslist:[],
    },

    // 折线图
    line:['titleName','titleFontColor','titleFontSize','titleTextAlign','basicSwitch','basicViewModel','lineModel','areaStackSwitch','pointSize','basicArea','basicPosition','basicBGColor','SplitColor',
            'XSplitLine','YSplitLine','axisFontSize','axisFontColor','axisName','legendSwitch','legendPosition','legendFontSize','legendFontColor','legendIcon','addSeries'],
    lineOptions:{
        basicViewModel:[
            {label:'竖模式',value:true},
            {label:'横模式',value:false},
        ],
        lineModel:[
            {label:'直线连接',value:false},
            {label:'曲线连接',value:true},
        ],
        basicArea:[
            {label:"宽度",modeKey:"width"},
            {label:"高度",modeKey:"height"},
        ],
        basicPosition:[
            {label:"Left",modeKey:"left"},
            {label:"Top",modeKey:"top"},
        ],
        titleTextAlign:[
            {label:"居中",value:"center"},
            {label:"左对齐",value:"left"},
            {label:"右对齐",value:"right"},
        ],
        legendPosition:[
            {label:"居中",value:"center"},
            {label:"左对齐",value:"left"},
            {label:"右对齐",value:"right"},
        ],
        axisName:[
            {label:"X轴",modeKey:"xAxisName"},
            {label:"Y轴",modeKey:"yAxisName"},
        ],
        legendIcon:[
            {label:"圆形",value:"circle"},
            {label:"方形",value:"rect"},
            {label:"圆角方形",value:"roundRect"},
            {label:"菱形",value:"diamond"},
        ],
    },
    // 页面新增时的实体
    lineBasicData:{
        type:'line',
        width:300,
        height:300,
        top:50,
        left:100,
        tabData:['configTabArea','dataTabArea'],
        basicBGColor:"rgba(15,29,76,0)",
        legendFontSize: 12,
        legendFontColor:"rgba(255,255,255,1)",
        legendPosition: "center",
        legendSwitch: true,
        titleFontSize: 16,
        titleFontColor:"rgba(255,255,255,1)",
        titleName: "折线图",
        titleTextAlign: "center",
        lineModel: true,
        pointSize:4,
        basicSwitch: true,
        basicViewModel:true,
        areaStackSwitch:false,
        XSplitLine:false,
        YSplitLine:true,
        axisFontSize:12,
        axisFontColor:"rgba(0,197,246,1)",
        SplitColor:'rgba(17,56,101,1)',
        xAxisName:"",
        yAxisName:"",
        dataConfig:{
            dateType:"1",
            exampleData:[
                {
                  name:'示例1',
                  type:'line',
                  stack: '总量',
                  data:[120, 132, 101, 134, 90, 230, 210]
                },
                {
                  name:'示例2',
                  type:'line',
                  stack: '总量',
                  data:[220, 182, 191, 234, 290, 330, 310]
                },
              ]
        },//数据配置
        isLock:2,
        isView:4,
        active:false,
        legendIcon:"circle",
        serieslist:[],
        needAxis:true,
    },

    // 柱状图
    bar:['titleName','titleFontColor','titleFontSize','titleTextAlign','basicSwitch','basicViewModel','basicBarWidth','basicArea','basicPosition','basicBGColor',
        'axisName','axisFontSize','axisFontColor','legendSwitch','legendPosition','legendFontSize','legendFontColor','barStackSwitch','numberSwitch','numberFontColor',
        'numberFontSize','legendIcon','addSeries'],
    barOptions:{
        basicViewModel:[
            {label:'竖模式',value:true},
            {label:'横模式',value:false},
        ],
        titleTextAlign:[
            {label:"居中",value:"center"},
            {label:"左对齐",value:"left"},
            {label:"右对齐",value:"right"},
        ],
        legendPosition:[
            {label:"居中",value:"center"},
            {label:"左对齐",value:"left"},
            {label:"右对齐",value:"right"},
        ],
        basicArea:[
            {label:"宽度",modeKey:"width"},
            {label:"高度",modeKey:"height"},
        ],
        basicPosition:[
            {label:"Left",modeKey:"left"},
            {label:"Top",modeKey:"top"},
        ],
        axisName:[
            {label:"X轴",modeKey:"xAxisName"},
            {label:"Y轴",modeKey:"yAxisName"},
        ],
        legendIcon:[
            {label:"圆形",value:"circle"},
            {label:"方形",value:"rect"},
            {label:"圆角方形",value:"roundRect"},
            {label:"菱形",value:"diamond"},
        ],
    },
    // 页面新增时的实体
    barBasicData:{
        type:'bar',
        width:300,
        height:300,
        top:50,
        left:100,
        tabData:['configTabArea','dataTabArea'],
        basicBGColor:"rgba(15,29,76,0)",
        legendFontSize: 12,
        legendFontColor:"rgba(255,255,255,1)",
        legendPosition: "center",
        legendSwitch: true,
        titleFontSize: 16,
        titleFontColor:"rgba(255,255,255,1)",
        titleName: "柱状图",
        titleTextAlign: "center",
        basicBarWidth:"",
        basicViewModel:true,
        barStackSwitch:false,
        basicSwitch: true,
        numberSwitch:false,
        numberFontColor:"rgba(255,255,255,1)",
        numberFontSize:12,
        axisFontSize:12,
        axisFontColor:"rgba(0,197,246,1)",
        xAxisName:"",
        yAxisName:"",
        dataConfig:{
            dateType:"1",
            exampleData:[
                {
                  name:'示例1',
                  type:'bar',
                  data:[10, 52, 200, 334, 390, 330, 220]
                },
                {
                  name:'示例2',
                  type:'bar',
                  data:[10, 52, 200, 334, 390, 330, 220]
                },
            ]
        },//数据配置
        isLock:2,
        isView:4,
        active:false,
        legendIcon:"circle",
        serieslist:[],
        needAxis:true,
    },

    // 词云
    textCloud:['basicArea','basicPosition','basicSwitch','minFontSize','maxFontSize','addSeries'],
    textCloudOptions:{
        basicArea:[
            {label:"宽度",modeKey:"width"},
            {label:"高度",modeKey:"height"},
        ],
        basicPosition:[
            {label:"Left",modeKey:"left"},
            {label:"Top",modeKey:"top"},
        ],
    },
    textCloudBasicData:{
        type:'textCloud',
        width:300,
        height:300,
        top:50,
        left:100,
        tabData:['configTabArea','dataTabArea'],
        basicBGColor:"rgba(15,29,76,0)",
        titleName: "词云",
        isLock:2,
        isView:4,
        active:false,
        serieslist:[],
        minFontSize:15,
        maxFontSize:40,
        basicSwitch: true,
        dataConfig:{
            dateType:"1",
            exampleData:[
                {value:335, name:'示例1'},
                {value:310, name:'示例2'},
                {value:234, name:'示例3'},
                {value:135, name:'示例4'},
              ]
        },//数据配置
    },

    // 跑马灯
    textLamp:['basicArea','basicPosition','textContent','basicFontColor','basicFontSize','basicfontWeight','lampSwitch','scrollSpeed','letterSpacing'],
    textLampOptions:{
        basicArea:[
            {label:"宽度",modeKey:"width"},
            {label:"高度",modeKey:"height"},
        ],
        basicPosition:[
            {label:"Left",modeKey:"left"},
            {label:"Top",modeKey:"top"},
        ],
        basicfontWeight:[
            {label:"normal",value:"normal"},
            {label:"bold",value:"bold"},
        ],
    },
    textLampBasicData:{
        type:'textLamp',
        width:300,
        height:30,
        top:50,
        left:100,
        tabData:['configTabArea'],
        textContent:'示例文本示例文本示例文本示例文本',
        basicfontWeight:"normal",
        titleName: "跑马灯",
        isLock:2,
        isView:4,
        lampSwitch:false,
        basicFontColor:"rgba(255,255,255,1)",
        basicFontSize: 12,
        scrollSpeed:1,
        letterSpacing:0,
    },

    // 时间器
    timer:['basicArea','basicPosition','timeFormat','letterSpacing','basicFontColor','basicFontSize'],
    timerOptions:{
        basicArea:[
            {label:"宽度",modeKey:"width"},
            {label:"高度",modeKey:"height"},
        ],
        basicPosition:[
            {label:"Left",modeKey:"left"},
            {label:"Top",modeKey:"top"},
        ],
        timeFormat:[
            {label:"日期",value:"YYYY-MM-DD"},
            {label:"日期（时分秒）",value:"YYYY-MM-DD HH:mm:ss"},
            {label:"时分秒",value:"HH:mm:ss"},
        ],
    },
    timerBasicData:{
        type:'timer',
        width:150,
        height:30,
        top:50,
        left:100,
        tabData:['configTabArea'],
        titleName: "时间器",
        isLock:2,
        isView:4,
        basicFontColor:"rgba(255,255,255,1)",
        basicFontSize: 12,
        letterSpacing:0,
        timeFormat:"YYYY-MM-DD HH:mm:ss"
    },

    
    // 进度图
    pieProgress:['titleName','titleFontColor','titleFontSize','titleTextAlign','basicArea','basicPosition','numberFontSize',
    'numberFontColor','barBGColor','progressType'],
    pieProgressOptions:{
        titleTextAlign:[
            {label:"居中",value:"center"},
            {label:"左对齐",value:"left"},
            {label:"右对齐",value:"right"},
        ],
        basicArea:[
            {label:"宽度",modeKey:"width"},
            {label:"高度",modeKey:"height"},
        ],
        basicPosition:[
            {label:"Left",modeKey:"left"},
            {label:"Top",modeKey:"top"},
        ],
        progressType:[
            {label:'条状',value:true},
            {label:'环状',value:false},
        ],
    },
    // 页面新增时的实体
    pieProgressBasicData:{
        type:'pieProgress',
        width:300,
        height:300,
        top:50,
        left:100,
        tabData:['configTabArea','dataTabArea'],
        numberFontSize: 25,
        titleFontSize: 25,
        titleFontColor:"rgba(255,255,255,1)",
        titleName: "进度图",
        titleTextAlign: "center",
        // basicSwitch: true,
        numberFontColor:'rgba(255,255,255,1)',
        barBGColor:"rgba(30,180,117,1)",
        isLock:2,
        isView:4,
        active:false,
        progressType:true,
        dataConfig:{
            dateType:"1",
            exampleData:90
        },//数据配置
    },

    // 图片
    imageComp:['basicArea','basicPosition','basicImage'],
    imageCompOptions:{
        basicArea:[
            {label:"宽度",modeKey:"width"},
            {label:"高度",modeKey:"height"},
        ],
        basicPosition:[
            {label:"Left",modeKey:"left"},
            {label:"Top",modeKey:"top"},
        ],
    },
    imageCompBasicData:{
        type:'imageComp',
        titleName: "图片模块",
        width:300,
        height:300,
        top:50,
        left:100,
        tabData:['configTabArea'],
        isLock:2,
        isView:4,
        active:false,
        basicImage:'',
    },
    // 视频
    videoComp:['basicArea','basicPosition','basicVideo','autoPlaySwitch','loopPLaySwitch'],
    videoCompOptions:{
        basicArea:[
            {label:"宽度",modeKey:"width"},
            {label:"高度",modeKey:"height"},
        ],
        basicPosition:[
            {label:"Left",modeKey:"left"},
            {label:"Top",modeKey:"top"},
        ],
    },
    videoCompBasicData:{
        type:'videoComp',
        titleName: "视频模块",
        width:300,
        height:300,
        top:50,
        left:100,
        tabData:['configTabArea'],
        isLock:2,
        isView:4,
        active:false,
        videoUrl:'../../../../static/images/video.mp4',
        autoPlaySwitch:true,
        loopPLaySwitch:true,
    },
    // 数字翻盘器
    numberAnimate:['basicArea','basicPosition','titleName','titleFontColor','titleFontSize','titlefontWeight','titleLetterSpacing','prefixName','prefixFontColor',
                    'prefixFontSize','prefixfontWeight','prefixLetterSpacing','SuffixName','SuffixFontColor','SuffixFontSize','SuffixfontWeight','SuffixLetterSpacing',
                'loopNumFontColor','loopNumFontSize','loopNumfontWeight','loopNumSeparator','titleArrayType'],
    numberAnimateOptions:{
        basicArea:[
            {label:"宽度",modeKey:"width"},
            {label:"高度",modeKey:"height"},
        ],
        basicPosition:[
            {label:"Left",modeKey:"left"},
            {label:"Top",modeKey:"top"},
        ],
        titlefontWeight:[
            {label:"normal",value:"normal"},
            {label:"bold",value:"bold"},
        ],
        prefixfontWeight:[
            {label:"normal",value:"normal"},
            {label:"bold",value:"bold"},
        ],
        SuffixfontWeight:[
            {label:"normal",value:"normal"},
            {label:"bold",value:"bold"},
        ],
        loopNumfontWeight:[
            {label:"normal",value:"normal"},
            {label:"bold",value:"bold"},
        ],
        titleArrayType:[
            {label:"标题在上",value:"top"},
            {label:"标题在左",value:"left"},
            {label:"标题在下",value:"bottom"},
        ],
    },
    numberAnimateBasicData:{
        type:'numberAnimate',
        width:150,
        height:80,
        top:50,
        left:100,
        tabData:['configTabArea','dataTabArea'],
        isLock:2,
        isView:4,
        active:false,
        dataConfig:{
            dateType:"1",
            exampleData:120500
        },//数据配置
        titleName: "数字翻盘器",
        titleFontColor:"rgba(255,255,255,1)",
        titleFontSize: 14,
        titlefontWeight:"normal",
        titleLetterSpacing:0,
        titleArrayType:"top",
        prefixName:"￥",
        prefixFontColor:"rgba(255,255,255,1)",
        prefixFontSize: 25,
        prefixfontWeight:"normal",
        prefixLetterSpacing:0,
        SuffixName:"元",
        SuffixFontColor:"rgba(255,255,255,1)",
        SuffixFontSize: 15,
        SuffixfontWeight:"normal",
        SuffixLetterSpacing:0,
        loopNumFontColor:"rgba(255,255,255,1)",
        loopNumFontSize: 25,
        loopNumfontWeight:"normal",
        loopNumSeparator:true,
        serieslist:[
            {seriesName:"值",seriesColor:"",seriesKey:"",seriesRemark:"数值"}
        ],
    },

    // 表格
    scrollGrid:['basicArea','basicPosition','basicSwitch','infoTextAlign','infoFontColor','infoFontSize','infoLetterSpacing','addheader','headerFontColor','headerFontSize',
                'headerLetterSpacing'],
    scrollGridOptions:{
        basicArea:[
            {label:"宽度",modeKey:"width"},
            {label:"高度",modeKey:"height"},
        ],
        basicPosition:[
            {label:"Left",modeKey:"left"},
            {label:"Top",modeKey:"top"},
        ],
        infoTextAlign:[
            {label:"居中",value:"center"},
            {label:"左对齐",value:"left"},
            {label:"右对齐",value:"right"},
        ],
    },
    scrollGridBasicData:{
        type:'scrollGrid',
        width:600,
        height:300,
        top:50,
        left:100,
        tabData:['configTabArea','dataTabArea'],
        isLock:2,
        isView:4,
        active:false,
        titleName: "表格",
        basicSwitch:true,
        scrollSpeed:1,
        infoTextAlign:"left",
        infoFontColor:"rgba(255,255,255,1)",
        infoFontSize:12,
        infoLetterSpacing:0,
        headerList:[],
        headerFontColor:"rgba(255,255,255,1)",
        headerFontSize:12,
        headerLetterSpacing:0,
        dataConfig:{
            dateType:"1",
            exampleData:[
                {key1:"11",key2:"22",key3:"33",key4:"44",key5:"55"},
                {key1:"11",key2:"22",key3:"33",key4:"44",key5:"55"},
                {key1:"11",key2:"22",key3:"33",key4:"44",key5:"55"},
                {key1:"11",key2:"22",key3:"33",key4:"44",key5:"55"},
                {key1:"11",key2:"22",key3:"33",key4:"44",key5:"55"},
                {key1:"11",key2:"22",key3:"33",key4:"44",key5:"55"},
            ]
        },//数据配置
        isScrollGrid:true,
    },

    // 边框
    // 右上
    borderTr:['basicArea','basicPosition'],
    borderTrOptions:{
        basicArea:[
            {label:"宽度",modeKey:"width"},
            {label:"高度",modeKey:"height"},
        ],
        basicPosition:[
            {label:"Left",modeKey:"left"},
            {label:"Top",modeKey:"top"},
        ],
    },
    borderTrBasicData:{
        type:'borderTr',
        width:200,
        height:100,
        top:50,
        left:100,
        isLock:2,
        isView:4,
        active:false,
        tabData:['configTabArea'],
        titleName: "边框(右上)",
    },
    // 右下
    borderRb:['basicArea','basicPosition'],
    borderRbOptions:{
        basicArea:[
            {label:"宽度",modeKey:"width"},
            {label:"高度",modeKey:"height"},
        ],
        basicPosition:[
            {label:"Left",modeKey:"left"},
            {label:"Top",modeKey:"top"},
        ],
    },
    borderRbBasicData:{
        type:'borderRb',
        width:200,
        height:100,
        top:50,
        left:100,
        isLock:2,
        isView:4,
        active:false,
        tabData:['configTabArea'],
        titleName: "边框(右下)",
    },
    // 左上
    borderTl:['basicArea','basicPosition'],
    borderTlOptions:{
        basicArea:[
            {label:"宽度",modeKey:"width"},
            {label:"高度",modeKey:"height"},
        ],
        basicPosition:[
            {label:"Left",modeKey:"left"},
            {label:"Top",modeKey:"top"},
        ],
    },
    borderTlBasicData:{
        type:'borderTl',
        width:200,
        height:100,
        top:50,
        left:100,
        isLock:2,
        isView:4,
        active:false,
        tabData:['configTabArea'],
        titleName: "边框(左上)",
    },
    // 左下
    borderLb:['basicArea','basicPosition'],
    borderLbOptions:{
        basicArea:[
            {label:"宽度",modeKey:"width"},
            {label:"高度",modeKey:"height"},
        ],
        basicPosition:[
            {label:"Left",modeKey:"left"},
            {label:"Top",modeKey:"top"},
        ],
    },
    borderLbBasicData:{
        type:'borderLb',
        width:200,
        height:100,
        top:50,
        left:100,
        isLock:2,
        isView:4,
        active:false,
        tabData:['configTabArea'],
        titleName: "边框(左下)",
    },
    // 旋转轮盘
    ferrisWheel:[],
    ferrisWheelOptions:{
        basicArea:[
            {label:"宽度",modeKey:"width"},
            {label:"高度",modeKey:"height"},
        ],
        basicPosition:[
            {label:"Left",modeKey:"left"},
            {label:"Top",modeKey:"top"},
        ],
    },
    ferrisWheelBasicData:{
        type:'ferrisWheel',
        width:700,
        height:1080,
        top:50,
        left:100,
        isLock:2,
        isView:4,
        active:false,
        tabData:[],
        titleName: "旋转轮盘",
    },
};
export default config;