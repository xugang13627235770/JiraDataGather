package com.citms.modules.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.citms.modules.home.vo.IssueTypeVO;
import com.citms.modules.project.entity.ProjectEntity;
import com.citms.modules.project.entity.SprintStatisticsEntity;
import com.citms.modules.project.vo.DefectScaleVO;
import com.citms.modules.project.vo.SprintTargetExportVO;
import com.citms.modules.project.vo.StoryPointScaleVO;

import java.util.List;

/**

 * @since 2018/03
 * @version R1.0
 * @category 变更日志表
 */
public interface ProjectService extends IService<ProjectEntity> {

    /**
     * 获取所有项目信息-根据项目名称
     * @return
     */
    List<ProjectEntity> getProjectListByName(String projectName);

    /**
     * 根据项目Id获取项目信息
     * @param id
     * @return
     */
    ProjectEntity getProjectById(Long id);

    /**
     * 迭代故事点数人员占比
     * @param sprintId
     * @return
     */
    List<StoryPointScaleVO> getStoryPointRate(Long sprintId);

    /**
     * 迭代总缺陷数量人员占比
     * @param sprintId
     * @return
     */
    List<DefectScaleVO> getDefectRate(Long sprintId);

    /**
     * 迭代新增问题类型统计
     * @param sprintId
     * @return
     */
    List<IssueTypeVO> getAddIssueTypeList(Long sprintId);

//    /**
//     * 迭代内各项指标
//     * @param sprintId
//     * @return
//     */
//    SprintTargetVO getSprintTargetVO(Long sprintId);

    /**
     * 导出迭代内各项指标
     * @param sprintId
     * @return
     */
    List<SprintTargetExportVO> getSprintTargetExportList(Long sprintId);

    /**
     * 迭代目标完成率=新增故事关闭率*0.6+历史遗留故事关闭率*0.4
     * @param sprintId
     * @return
     */
    String getSprintTargetRate(Long sprintId);

    /**
     *人均关闭故事点数
     * @param sprintId
     * @return
     */
    String getAvgStoryPoint(Long sprintId);

    /**
     * 平均故事关闭时长
     * @param sprintId
     * @return
     */
    String getAvgStoryTime(Long sprintId);

    /**
     * 平均故事延期时长
     * @param sprintId
     * @return
     */
    String getAvgStoryDelay(Long sprintId);

    /**
     *重新打开故事次数
     * @param sprintId
     * @return
     */
    String getReopenStory(Long sprintId);

    /**
     *迭代遗留故事占比
     * @param sprintId
     * @return
     */
    String getRemainStory(Long sprintId);

    /**
     *逾期完成故事点数
     * @param sprintId
     * @return
     */
    String getOverdueStoryPoint(Long sprintId);

    /**
     *缺陷关闭率=关闭缺陷数/当前迭代的总缺陷数
     * @param sprintId
     * @return
     */
    String getDefectCloseRate(Long sprintId);

    /**
     * 缺陷密度=新增缺陷数/关闭故事点数
     * @param sprintId
     * @return
     */
    String getDefectDensity(Long sprintId);

    /**
     *遗留缺陷比例=未关闭缺陷个数/当前迭代的总缺陷
     * @param sprintId
     * @return
     */
    String getRemainDefectRate(Long sprintId);

    /**
     * 本迭代内线上反馈的缺陷
     * @param sprintId
     * @return
     */
    String getOnlineDefect(Long sprintId);

    /**
     * 平均线上缺陷修复时长
     * @param sprintId
     * @return
     */
    String getOnlineRepairTime(Long sprintId);

    /**
     *统计存储迭代信息
     * @param sprintId
     */
    void saveSprintStatistics(Long sprintId);

    /**
     * 根据迭代ID获取迭代统计信息
     * @param sprintId
     * @return
     */
    SprintStatisticsEntity getSprintStatisticsEntityBySprintId(Long sprintId);
}