package com.citms.modules.project.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 类SprintTargeVO的实现描述：TODO 类实现描述 
 * @author DELL 2019/11/29 9:49
 * 迭代指标
 */
@Data
public class SprintTargetVO {

    private BigDecimal sprintTargetScore;//迭代目标完成率得分
    private BigDecimal sprintTargetRate;//迭代目标完成率=新增故事关闭率*0.6+历史遗留故事关闭率*0.4
    private BigDecimal addStoryPointRate;//新增故事关闭率=关闭新增故事点数/本迭代计划故事点数
    private BigDecimal remainStoryPointRate;//历史遗留故事关闭率=关闭历史遗留故事点数/历史遗留故事点数

    private BigDecimal avgStoryPointScore;//人均关闭故事点数得分=基础分10分*人均关闭故事点数/baseline人均关闭故事点数
    private BigDecimal avgStoryPoint;//人均关闭故事点数

    /*
    平均故事关闭时长得分：
    1）时长＜1天，5分
    2）时长 ≥ 1天，0分
    * */
    private BigDecimal avgStoryTimeScore;
    private BigDecimal avgStoryTime;//平均故事关闭时长=SUM（故事关闭时间-故事开始处理时间）/关闭故事点数之和

    /*
    * 平均故事延期时长得分：
    1）时长＜1天，5分
    2）时长 ≥ 1天，0分
    * */
    private BigDecimal avgStoryDelayScore;
    private BigDecimal avgStoryDelay;//平均故事延期时长=SUM（实际提测日期-预计提测日期）/延期故事点数之和

    /**
     * 重新打开故事次数得分：
     *     1）次数=0，5分
     *     2）次数≥1，0分
     */
    private BigDecimal reopenStoryScore;
    private BigDecimal reopenStory;//重新打开故事次数

    /**
     * 迭代遗留故事占比得分：
     *     1）占比＜10%，5分
     *     2）占比≥10%，0分
     */
    private BigDecimal remainStroyScore;
    private BigDecimal remainStroy;//迭代遗留故事占比=（总故事点数-完成故事点数）/总故事点数

    /**
     * 逾期完成故事点数得分：
     *     1）故事点数＜5，5分
     *     2）故事点数 ≥ 5，0分
     */
    private BigDecimal overdueStoryPointScore;
    private BigDecimal overdueStoryPoint;//逾期完成故事点数=逾期指故事关闭日期晚于预计关闭日期

    private BigDecimal defectCloseScore;//缺陷关闭率得分=基础分15分*缺陷关闭率/基线缺陷关闭率
    private BigDecimal defectCloseRate;//缺陷关闭率=关闭缺陷数/当前迭代的总缺陷数

    /**
     * 缺陷密度得分=基础分10分*baseline基线bug密度/bug密度
     * 新增缺陷数为0，得10分
     */
    private BigDecimal defectDensityScore;
    private BigDecimal defectDensity;//缺陷密度=新增缺陷数/关闭故事点数

    /**
     * 平均修复时长得分：
     *     1）时长＜1天，5分
     *     2）时长 ≥ 1天，0分
     */
    private BigDecimal avgRepairTimeScore;
    private BigDecimal avgRepairTime;//平均修复时长=SUM（缺陷关闭时间-缺陷创建时间）/关闭缺陷个数

    /**
     * 遗留缺陷比例得分：
     *     1）比例＜10%，5分
     *     2）比例 ≥ 10%，0分
     */
    private BigDecimal remainDefectScore;
    private BigDecimal remainDefectRate;//遗留缺陷比例=未关闭缺陷个数/当前迭代的总缺陷

    /**
     * 线上缺陷得分：
     *     1）线上缺陷个数 = 0，5分
     *     2）线上缺陷个数 ≥ 0，0分
     */
    private BigDecimal onlineDefectScore;
    private BigDecimal onlineDefect;//统计本迭代内线上反馈的缺陷

    /**
     * 线上缺陷修复时长得分：
     *     1）时长＜1天，5分
     *     2）时长 ≥ 1天，0分
     */
    private BigDecimal onlineRepairScore;
    private BigDecimal onlineRepairTime;//线上缺陷修复时长=SUM（线上缺陷关闭时间-创建时间）/线上缺陷关闭个数

    private BigDecimal addStory;//迭代期间新增故事数
    private BigDecimal addStoryScore;//迭代期间新增故事数扣分=故事数*0.5

}
