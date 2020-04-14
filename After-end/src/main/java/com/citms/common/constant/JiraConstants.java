package com.citms.common.constant;

/**
 * 类Constants的实现描述：TODO 类实现描述
 * @author DELL 2019/11/12 10:54
 */
public class JiraConstants {

    public static final Integer VALID = 0; //0：未删除   1：已删除
    public static final Integer IN_VALID = 1;// 0：未删除   1：已删除

    public static final String STATUS_TESTED = "待测试";
    public static final String STATUS_CLOSE = "已关闭";

    public static final String ISSUE_FLAG = "已标记";

    public static final Long DEVELOPER_PKID = 1l;
    public static final Long TESTER_PKID = 1l;

    public static final Integer DEVELOPER_JAVA = 4;//Java/C#开发工程师
    public static final Integer DEVELOPER_FRONT = 5;//前端工程师
    public static final Integer DEVELOPER_BIGDATA = 6;//大数据工程师

    public static final String CODE_ISSUE_NUM = "CODE_ISSUE_NUM";//缺陷数量加分区间
    public static final String CODE_TAKE_TIME = "CODE_TAKE_TIME";//缺陷平均耗时(天数)加分区间
    public static final String CODE_HISTORY_CLOSE = "CODE_HISTORY_CLOSE";//历史缺陷关闭加分区间
    public static final String CODE_VALID_RATE = "CODE_VALID_RATE";//缺陷有效率扣分区间

    public static final Integer ISSUE_TYPE_1 = 1;//类型-故事
    public static final Integer ISSUE_TYPE_2 = 2;//类型-缺陷

    public static final Integer IS_ADD_YES= 0;//是
    public static final Integer IS_ADD_NO = 1;//否

    public static final String STORY_POINT_STR = "剩余故事点数";//

    public static final String SPRINT_STATE_CLOSED = "CLOSED";//迭代状态为关闭
    public static final String SPRINT_STATE_ACTIVE = "ACTIVE";//迭代状态为关闭
    public static final String RESOLUTION_UNSOLVED = "未解决";//解决结果默认未解决
    public static final String LOG_REOPEN = "重新打开";//解决结果默认未解决

    public static final Integer ORDER_UP = 1;
    public static final Integer ORDER_MIDDLE = 0;
    public static final Integer ORDER_DOWN = -1;

    /**
     * 项目统计区间动态规则code
     */
    public static interface ProjectConfigRangeCode{

        /**
         * 平均故事关闭时长得分区间
         */
        public static final String AVG_STORY_CLOSE_TIME = "AVG_STORY_CLOSE_TIME";

        /**
         * 平均故事延期时长得分区间
         */
        public static final String AVG_STORY_DELAY_TIME = "AVG_STORY_DELAY_TIME";

        /**
         * 故事重新打开次数得分区间
         */
        public static final String STORY_REOPEN_NUM = "STORY_REOPEN_NUM";

        /**
         * 迭代遗留故事占比得分区间
         */
        public static final String LEFT_OVER_STORY_RATE = "LEFT_OVER_STORY_RATE";

        /**
         * 逾期完成故事点数得分区间
         */
        public static final String OVERDUE_COMPLETE_POINT = "OVERDUE_COMPLETE_POINT";

        /**
         * 遗留缺陷比例得分区间
         */
        public static final String LEFT_OVER_DEFECT_RATE = "LEFT_OVER_DEFECT_RATE";

        /**
         * 线上缺陷数量得分区间
         */
        public static final String ONLINE_DEFECT_NUM = "ONLINE_DEFECT_NUM";

        /**
         * 线上缺陷平均修复时长得分区间
         */
        public static final String ONLINE_DEFECT_AVG_REPAIR_TIME = "ONLINE_DEFECT_AVG_REPAIR_TIME";
    }
}
