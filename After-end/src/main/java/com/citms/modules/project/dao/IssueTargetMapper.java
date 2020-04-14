package com.citms.modules.project.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.citms.modules.jira.entity.IssueEntity;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;

/**
 */
@Mapper
public interface IssueTargetMapper extends BaseMapper<IssueEntity> {

//    /**
//     * 新增故事关闭率=关闭新增故事点数/本迭代计划故事点数
//     * @param sprintId
//     * @return
//     */
//    BigDecimal getAddStoryPointRate(Long sprintId);
//
//    /**
//     *历史遗留故事关闭率=关闭历史遗留故事点数/历史遗留故事点数
//     * @param sprintId
//     * @return
//     */
//    BigDecimal getRemainStoryPointRate(Long sprintId);

    /**
     * 迭代目标完成率 = 关闭故事点数/迭代总故事点数
     * @param sprintId
     * @return
     */
    BigDecimal getStoryPointRate(Long sprintId);

    /**
     *人均关闭故事点数 = 关闭故事点数之和/参与迭代的人数。
     * @param sprintId
     * @return
     */
    BigDecimal getAvgStoryPoint(Long sprintId);

    /**
     * 平均故事关闭时长=SUM（故事关闭时间-故事开始处理时间）/关闭故事点数之和
     * @param sprintId
     * @return
     */
    BigDecimal getAvgStoryTime(Long sprintId);

    /**
     *平均故事延期时长=SUM（实际提测日期-预计提测日期）/延期故事点数之和
     * @param sprintId
     * @return
     */
    BigDecimal getAvgStoryDelay(Long sprintId);

    /**
     * 重新打开故事次数
     * @param sprintId
     * @return
     */
    BigDecimal getReopenStory(Long sprintId);

    /**
     * 迭代遗留故事占比=（总故事点数-完成故事点数）/总故事点数
     * @param sprintId
     * @return
     */
    BigDecimal getRemainStroy(Long sprintId);

    /**
     * 逾期完成故事点数=逾期指故事关闭日期晚于预计关闭日期
     * @param sprintId
     * @return
     */
    BigDecimal getOverdueStoryPoint(Long sprintId);

    /**
     * 缺陷关闭率=关闭缺陷数/当前迭代的总缺陷数
     * @param sprintId
     * @return
     */
    BigDecimal getDefectCloseRate(Long sprintId);

    /**
     *缺陷密度=新增缺陷数/关闭故事点数
     * @param sprintId
     * @return
     */
    BigDecimal getDefectDensity(Long sprintId);

    /**
     *平均修复时长=SUM（缺陷关闭时间-缺陷创建时间）/关闭缺陷个数
     * @param sprintId
     * @return
     */
    BigDecimal getAvgRepairTime(Long sprintId);

    /**
     * 遗留缺陷比例=未关闭缺陷个数/当前迭代的总缺陷
     * @param sprintId
     * @return
     */
    BigDecimal getRemainDefectRate(Long sprintId);

    /**
     * 统计本迭代内线上反馈的缺陷
     * @param sprintId
     * @return
     */
    BigDecimal getOnlineDefect(Long sprintId);

    /**
     * 线上缺陷修复时长=SUM（线上缺陷关闭时间-创建时间）/线上缺陷关闭个数
     * @param sprintId
     * @return
     */
    BigDecimal getOnlineRepairTime(Long sprintId);

    /**
     * 迭代期间新增故事数
     * @param sprintId
     * @return
     */
    BigDecimal getAddStory(Long sprintId);

    /**
     * 迭代新增故事关闭率
     * @param sprintId
     * @return
     */
    BigDecimal getSprintAddStoryCloseRate(Long sprintId);

    /**
     * 迭代历史故事关闭率
     * @param sprintId
     * @return
     */
    BigDecimal getHistoryStoryCloseRate(Long sprintId);

    /**
     * 查询迭代新任务
     * @param sprintId
     * @return
     */
    String getSprintNewStory(Long sprintId);

    /**
     * 查询迭代已关闭的新任务
     * @param sprintId
     * @return
     */
    String getSprintNewStoryFinish(Long sprintId);

    /**
     * 查询迭代遗留任务
     * @param sprintId
     * @return
     */
    String getSprintOldStory(Long sprintId);

    /**
     * 查询迭代遗留已关闭任务
     * @param sprintId
     * @return
     */
    String getSprintOldStoryFinish(Long sprintId);
}