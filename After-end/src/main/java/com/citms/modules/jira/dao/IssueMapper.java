package com.citms.modules.jira.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.citms.modules.home.vo.IssueDateVO;
import com.citms.modules.home.vo.IssueScaleVO;
import com.citms.modules.home.vo.IssueTypeVO;
import com.citms.modules.project.vo.DefectScaleVO;
import com.citms.modules.project.vo.StoryPointScaleVO;
import com.citms.modules.jira.entity.IssueEntity;
import com.citms.modules.user.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 */
@Mapper
public interface IssueMapper extends BaseMapper<IssueEntity> {

    IssueEntity queryIssueEntityById(Long id);

    /**
     *根据项目id获取问题类型统计
     * @param projectId
     * @return
     */
    List<IssueTypeVO> getIssueTypeList(Long projectId);

    /**
     *根据项目id获取人员总问题占比
     * @param projectId
     * @return
     */
    List<IssueScaleVO> getIssueScaleList(Long projectId);

    /**
     * 根据项目id获取人员总问题累计
     * @param projectId
     * @return
     */
//    List<IssueDateVO> getIssueDateList(Long projectId);

    /**
     * 根据项目id+日期获取人员总问题累计
     * @param projectId
     * @param date
     * @return
     */
    List<IssueDateVO> getIssueDateListByDate(@Param("projectId") Long projectId, @Param("date")Date date);

    /**
     * 根据项目ID获取总的问题统计
     * @param projectId
     * @return
     */
    BigDecimal getTotalStoryPoint(Long projectId);

    /**
     * 获取总的开发人数
     * @param projectId
     * @return
     */
    Long getTotalDeveloper(Long projectId);

    /**
     * 获取总的测试人数
     * @param projectId
     * @return
     */
    Long getTotalTester(Long projectId);

    /**
     *获取总的设计人数
     * @param projectId
     * @return
     */
    Long getTotalDesigner(Long projectId);

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

    /**
     * 获取总的故事数
     * @return
     */
    List<UserVO> getTotalStoryByUserIds(@Param("ids") List<Long> ids);

    /**
     * 获取总的缺陷数
     * @return
     */
    List<UserVO> getTotalDefectByUserIds(@Param("ids")List<Long> ids);

}