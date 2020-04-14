package com.citms.modules.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.citms.common.constant.JiraConstants;
import com.citms.modules.developer.dao.ConfigDeveloperMapper;
import com.citms.modules.home.vo.IssueTypeVO;
import com.citms.modules.jira.dao.IssueMapper;
import com.citms.modules.project.dao.IssueTargetMapper;
import com.citms.modules.project.dao.ProjectMapper;
import com.citms.modules.project.dao.SprintStatisticsMapper;
import com.citms.modules.project.service.ConfigProjectService;
import com.citms.modules.project.vo.*;
import com.citms.modules.developer.entity.ConfigDeveloperEntity;
import com.citms.modules.project.entity.ProjectEntity;
import com.citms.modules.project.entity.SprintStatisticsEntity;
import com.citms.modules.project.service.ProjectService;
import com.citms.modules.tester.entity.ConfigSideEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 */
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper,ProjectEntity> implements ProjectService {

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private IssueMapper issueMapper;

    @Autowired
    private IssueTargetMapper issueTargetMapper;

    @Autowired
    private ConfigDeveloperMapper configDeveloperMapper;

    @Autowired
    private SprintStatisticsMapper sprintStatisticsMapper;

    @Autowired
    private ConfigProjectService configProjectService;

    /**
     * 获取所有项目信息-根据项目名称
     * @return
     * @param projectName
     */
    @Override
    public List<ProjectEntity> getProjectListByName(String projectName) {

        ProjectEntity projectCon = new ProjectEntity();
        QueryWrapper<ProjectEntity> ew = Wrappers.query(projectCon);
        if(StringUtils.isNotEmpty(projectName)){
            ew.like("name",projectName);
        }
        return projectMapper.selectList(ew);
    }

    /**
     * 根据项目Id获取项目信息
     * @param id
     * @return
     */
    @Override
    public ProjectEntity getProjectById(Long id) {
        ProjectEntity projectCon = new ProjectEntity();
        projectCon.setId(id);
        QueryWrapper<ProjectEntity> ew = Wrappers.query(projectCon);
        ProjectEntity projectEntity = projectMapper.selectOne(ew);
        return projectEntity;
    }

    /**
     * 迭代故事点数人员占比
     * @param sprintId
     * @return
     */
    @Override
    public List<StoryPointScaleVO> getStoryPointRate(Long sprintId) {
        if(sprintId == null){
            return null;
        }
        return issueMapper.getStoryPointRate(sprintId);
    }

    /**
     * 迭代总缺陷数量人员占比
     * @param sprintId
     * @return
     */
    @Override
    public List<DefectScaleVO> getDefectRate(Long sprintId) {
        if(sprintId == null){
            return null;
        }
        return issueMapper.getDefectRate(sprintId);
    }

    /**
     * 迭代新增问题类型统计
     * @param sprintId
     * @return
     */
    @Override
    public List<IssueTypeVO> getAddIssueTypeList(Long sprintId) {
        if(sprintId == null){
            return null;
        }
        return issueMapper.getAddIssueTypeList(sprintId);
    }

//    /**
//     * 迭代内各项指标
//     * @param sprintId
//     * @return
//     */
//    @Override
//    public SprintTargetVO getSprintTargetVO(Long sprintId) {
//        if(sprintId == null){
//            return null;
//        }
//        SprintTargetVO sprintTargetVO = new SprintTargetVO();
//
//        //迭代目标完成率=新增故事关闭率*0.6+历史遗留故事关闭率*0.4
//        BigDecimal addStoryPointRate = issueTargetMapper.getAddStoryPointRate(sprintId);
//        BigDecimal remainStoryPointRate = issueTargetMapper.getRemainStoryPointRate(sprintId);
//        if(addStoryPointRate != null && remainStoryPointRate != null){
//            BigDecimal sprintTargetRate = addStoryPointRate.multiply(new BigDecimal(0.6))
//                    .add(remainStoryPointRate.multiply(new BigDecimal(0.4)));
//            sprintTargetVO.setSprintTargetRate(sprintTargetRate.setScale(4, BigDecimal.ROUND_HALF_UP));
//        }else{
//            sprintTargetVO.setSprintTargetRate(new BigDecimal(0));
//        }
//
//        //人均关闭故事点数
//        BigDecimal avgStoryPoint = issueTargetMapper.getAvgStoryPoint(sprintId);
//        sprintTargetVO.setAvgStoryPoint(avgStoryPoint != null ? avgStoryPoint.setScale(2, BigDecimal.ROUND_HALF_UP):new BigDecimal(0));
//
//        //平均故事关闭时长
//        BigDecimal avgStoryTime = issueTargetMapper.getAvgStoryTime(sprintId);
//        sprintTargetVO.setAvgStoryTime(avgStoryTime != null ? avgStoryTime.setScale(2, BigDecimal.ROUND_HALF_UP):new BigDecimal(0));
//
//       //平均故事延期时长
//        BigDecimal avgStoryDelay = issueTargetMapper.getAvgStoryDelay(sprintId);
//        sprintTargetVO.setAvgStoryDelay(avgStoryDelay != null ? avgStoryDelay.setScale(2, BigDecimal.ROUND_HALF_UP):new BigDecimal(0));
//
//        //重新打开故事次数
//        BigDecimal reopenStory = issueTargetMapper.getReopenStory(sprintId);
//        sprintTargetVO.setReopenStory(reopenStory != null ? reopenStory:new BigDecimal(0));
//
//        //迭代遗留故事占比
//        BigDecimal remainStroy = issueTargetMapper.getRemainStroy(sprintId);
//        sprintTargetVO.setRemainStroy(remainStroy != null ? remainStroy.setScale(4, BigDecimal.ROUND_HALF_UP):new BigDecimal(0));
//
//        //逾期完成故事点数
//        BigDecimal overdueStoryPoint = issueTargetMapper.getOverdueStoryPoint(sprintId);
//        sprintTargetVO.setOverdueStoryPoint(overdueStoryPoint != null ? overdueStoryPoint.setScale(2, BigDecimal.ROUND_HALF_UP):new BigDecimal(0));
//
//        //缺陷关闭率=关闭缺陷数/当前迭代的总缺陷数
//        BigDecimal defectCloseRate = issueTargetMapper.getDefectCloseRate(sprintId);
//        sprintTargetVO.setDefectCloseRate(defectCloseRate !=null ? defectCloseRate.setScale(4, BigDecimal.ROUND_HALF_UP): new BigDecimal(0));
//
//        //缺陷密度=新增缺陷数/关闭故事点数
//        BigDecimal defectDensity = issueTargetMapper.getDefectDensity(sprintId);
//        sprintTargetVO.setDefectDensity(defectDensity != null ? defectDensity.setScale(2, BigDecimal.ROUND_HALF_UP):new BigDecimal(0));
//
//        //平均修复时长=SUM（缺陷关闭时间-缺陷创建时间）/关闭缺陷个数
//        BigDecimal avgRepairTime = issueTargetMapper.getAvgRepairTime(sprintId);
//        sprintTargetVO.setAvgRepairTime(avgRepairTime != null ? avgRepairTime.setScale(2, BigDecimal.ROUND_HALF_UP):new BigDecimal(0));
//
//        //遗留缺陷比例=未关闭缺陷个数/当前迭代的总缺陷
//        BigDecimal remainDefectRate = issueTargetMapper.getRemainDefectRate(sprintId);
//        sprintTargetVO.setRemainDefectRate(remainDefectRate != null ? remainDefectRate.setScale(4, BigDecimal.ROUND_HALF_UP):new BigDecimal(0));
//
//        //本迭代内线上反馈的缺陷
//        BigDecimal onlineDefect = issueTargetMapper.getOnlineDefect(sprintId);
//        sprintTargetVO.setOnlineDefect(onlineDefect != null ? onlineDefect : new BigDecimal(0));
//
//        //平均线上缺陷修复时长
//        BigDecimal onlineRepairTime = issueTargetMapper.getOnlineRepairTime(sprintId);
//        sprintTargetVO.setOnlineRepairTime(onlineRepairTime != null ? onlineRepairTime.setScale(2, BigDecimal.ROUND_HALF_UP): new BigDecimal(0));
//
//        return sprintTargetVO;
//    }

    /**
     * 导出迭代内各项指标
     * @param sprintId
     * @return
     */
    @Override
    public List<SprintTargetExportVO> getSprintTargetExportList(Long sprintId) {
        List<SprintTargetExportVO> list = new ArrayList<>();
        SprintStatisticsEntity sprintStatisticsEntity = getSprintStatisticsEntityBySprintId(sprintId);
        if(sprintStatisticsEntity == null){

            return list;
        }

        SprintTargetExportVO sprintTargetExportVO = new SprintTargetExportVO();
        sprintTargetExportVO.setName("迭代目标完成率");
        sprintTargetExportVO.setTargetValue(sprintStatisticsEntity.getSprintTargetRate());
        sprintTargetExportVO.setTargetScore(sprintStatisticsEntity.getSprintTargetRateScore());
        list.add(sprintTargetExportVO);

        SprintTargetExportVO sprintTargetExportVO1 = new SprintTargetExportVO();
        sprintTargetExportVO1.setName("人均关闭故事点数");
        sprintTargetExportVO1.setTargetValue(sprintStatisticsEntity.getAvgStoryPoint());
        sprintTargetExportVO1.setTargetScore(sprintStatisticsEntity.getAvgStoryPointScore());
        list.add(sprintTargetExportVO1);

        SprintTargetExportVO sprintTargetExportVO2 = new SprintTargetExportVO();
        sprintTargetExportVO2.setName("平均故事关闭天数");
        sprintTargetExportVO2.setTargetValue(sprintStatisticsEntity.getAvgStoryTime());
        sprintTargetExportVO2.setTargetScore(sprintStatisticsEntity.getAvgStoryTimeScore());
        list.add(sprintTargetExportVO2);

        SprintTargetExportVO sprintTargetExportVO3 = new SprintTargetExportVO();
        sprintTargetExportVO3.setName("平均故事提测延时天数");
        sprintTargetExportVO3.setTargetValue(sprintStatisticsEntity.getAvgStoryDelay());
        sprintTargetExportVO3.setTargetScore(sprintStatisticsEntity.getAvgStoryDelayScore());
        list.add(sprintTargetExportVO3);

        SprintTargetExportVO sprintTargetExportVO4 = new SprintTargetExportVO();
        sprintTargetExportVO4.setName("重新打开故事次数");
        sprintTargetExportVO4.setTargetValue(sprintStatisticsEntity.getReopenStory());
        sprintTargetExportVO4.setTargetScore(sprintStatisticsEntity.getReopenStoryScore());
        list.add(sprintTargetExportVO4);

        SprintTargetExportVO sprintTargetExportVO5 = new SprintTargetExportVO();
        sprintTargetExportVO5.setName("遗留故事占比");
        sprintTargetExportVO5.setTargetValue(sprintStatisticsEntity.getRemainStory());
        sprintTargetExportVO5.setTargetScore(sprintStatisticsEntity.getRemainStoryScore());
        list.add(sprintTargetExportVO5);

        SprintTargetExportVO sprintTargetExportVO6 = new SprintTargetExportVO();
        sprintTargetExportVO6.setName("逾期完成故事点数");
        sprintTargetExportVO6.setTargetValue(sprintStatisticsEntity.getOverdueStoryPoint());
        sprintTargetExportVO6.setTargetScore(sprintStatisticsEntity.getOverdueStoryPointScore());
        list.add(sprintTargetExportVO6);

        SprintTargetExportVO sprintTargetExportVO7 = new SprintTargetExportVO();
        sprintTargetExportVO7.setName("缺陷关闭率");
        sprintTargetExportVO7.setTargetValue(sprintStatisticsEntity.getDefectCloseRate());
        sprintTargetExportVO7.setTargetScore(sprintStatisticsEntity.getDefectCloseRateScore());
        list.add(sprintTargetExportVO7);

        SprintTargetExportVO sprintTargetExportVO8 = new SprintTargetExportVO();
        sprintTargetExportVO8.setName("缺陷密度");
        sprintTargetExportVO8.setTargetValue(sprintStatisticsEntity.getDefectDensity());
        sprintTargetExportVO8.setTargetScore(sprintStatisticsEntity.getDefectDensityScore());
        list.add(sprintTargetExportVO8);

        SprintTargetExportVO sprintTargetExportVO9 = new SprintTargetExportVO();
        sprintTargetExportVO9.setName("遗留缺陷比例");
        sprintTargetExportVO9.setTargetValue(sprintStatisticsEntity.getRemainDefectRate());
        sprintTargetExportVO9.setTargetScore(sprintStatisticsEntity.getRemainDefectRateScore());
        list.add(sprintTargetExportVO9);

        SprintTargetExportVO sprintTargetExportVO10 = new SprintTargetExportVO();
        sprintTargetExportVO10.setName("线上缺陷数量");
        sprintTargetExportVO10.setTargetValue(sprintStatisticsEntity.getOnlineDefect());
        sprintTargetExportVO10.setTargetScore(sprintStatisticsEntity.getOnlineDefectScore());
        list.add(sprintTargetExportVO10);

        SprintTargetExportVO sprintTargetExportVO11 = new SprintTargetExportVO();
        sprintTargetExportVO11.setName("线上缺陷平均修复天数");
        sprintTargetExportVO11.setTargetValue(sprintStatisticsEntity.getOnlineRepairTime());
        sprintTargetExportVO11.setTargetScore(sprintStatisticsEntity.getOnlineRepairTimeScore());
        list.add(sprintTargetExportVO11);

        SprintTargetExportVO sprintTargetExportVO12 = new SprintTargetExportVO();
        sprintTargetExportVO12.setName("迭代期间新增故事数");
        sprintTargetExportVO12.setTargetValue(sprintStatisticsEntity.getAddStory());
        sprintTargetExportVO12.setTargetScore(sprintStatisticsEntity.getAddStoryScore());
        list.add(sprintTargetExportVO12);

        return list;
    }

    /**
     * 迭代目标完成率=关闭故事点数/迭代总故事点数
     * @param sprintId
     * @return
     */
    @Override
    public String getSprintTargetRate(Long sprintId) {
        //迭代目标完成率=关闭故事点数/迭代总故事点数
        BigDecimal sprintTargetRate = issueTargetMapper.getStoryPointRate(sprintId);
        sprintTargetRate = sprintTargetRate !=null ? sprintTargetRate : new BigDecimal(0);
        return String.valueOf(sprintTargetRate
                .multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP))+"%";
    }

    /**
     *人均关闭故事点数
     * @param sprintId
     * @return
     */
    @Override
    public String getAvgStoryPoint(Long sprintId) {
        SprintTargetVO sprintTargetVO = new SprintTargetVO();
        //人均关闭故事点数
        BigDecimal avgStoryPoint = issueTargetMapper.getAvgStoryPoint(sprintId);
        sprintTargetVO.setAvgStoryPoint(avgStoryPoint != null ? avgStoryPoint.setScale(2, BigDecimal.ROUND_HALF_UP):new BigDecimal(0));
        return String.valueOf(sprintTargetVO.getAvgStoryPoint());
    }

    /**
     * 平均故事关闭时长
     * @param sprintId
     * @return
     */
    @Override
    public String getAvgStoryTime(Long sprintId) {
        SprintTargetVO sprintTargetVO = new SprintTargetVO();
        //平均故事关闭时长
        BigDecimal avgStoryTime = issueTargetMapper.getAvgStoryTime(sprintId);
        sprintTargetVO.setAvgStoryTime(avgStoryTime != null ? avgStoryTime.setScale(2, BigDecimal.ROUND_HALF_UP):new BigDecimal(0));
        return String.valueOf(sprintTargetVO.getAvgStoryTime());
    }

    /**
     * 平均故事延期时长
     * @param sprintId
     * @return
     */
    @Override
    public String getAvgStoryDelay(Long sprintId) {
        SprintTargetVO sprintTargetVO = new SprintTargetVO();
        //平均故事延期时长
        BigDecimal avgStoryDelay = issueTargetMapper.getAvgStoryDelay(sprintId);
        sprintTargetVO.setAvgStoryDelay(avgStoryDelay != null ? avgStoryDelay.setScale(2, BigDecimal.ROUND_HALF_UP):new BigDecimal(0));
        return String.valueOf(sprintTargetVO.getAvgStoryDelay());
    }

    /**
     *重新打开故事次数
     * @param sprintId
     * @return
     */
    @Override
    public String getReopenStory(Long sprintId) {
        SprintTargetVO sprintTargetVO = new SprintTargetVO();
        //重新打开故事次数
        BigDecimal reopenStory = issueTargetMapper.getReopenStory(sprintId);
        sprintTargetVO.setReopenStory(reopenStory != null ? reopenStory:new BigDecimal(0));
        return String.valueOf(sprintTargetVO.getReopenStory());
    }

    /**
     *迭代遗留故事占比
     * @param sprintId
     * @return
     */
    @Override
    public String getRemainStory(Long sprintId) {
        SprintTargetVO sprintTargetVO = new SprintTargetVO();
        //迭代遗留故事占比
        BigDecimal remainStroy = issueTargetMapper.getRemainStroy(sprintId);
        sprintTargetVO.setRemainStroy(remainStroy != null ? remainStroy.setScale(4, BigDecimal.ROUND_HALF_UP):new BigDecimal(0));
        return String.valueOf(sprintTargetVO.getRemainStroy()
                .multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP))+"%";
    }

    /**
     *逾期完成故事点数
     * @param sprintId
     * @return
     */
    @Override
    public String getOverdueStoryPoint(Long sprintId) {
        SprintTargetVO sprintTargetVO = new SprintTargetVO();
        //逾期完成故事点数
        BigDecimal overdueStoryPoint = issueTargetMapper.getOverdueStoryPoint(sprintId);
        sprintTargetVO.setOverdueStoryPoint(overdueStoryPoint != null ? overdueStoryPoint.setScale(2, BigDecimal.ROUND_HALF_UP):new BigDecimal(0));
        return String.valueOf(sprintTargetVO.getOverdueStoryPoint());
    }

    /**
     *缺陷关闭率=关闭缺陷数/当前迭代的总缺陷数
     * @param sprintId
     * @return
     */
    @Override
    public String getDefectCloseRate(Long sprintId) {
        SprintTargetVO sprintTargetVO = new SprintTargetVO();
        //缺陷关闭率=关闭缺陷数/当前迭代的总缺陷数
        BigDecimal defectCloseRate = issueTargetMapper.getDefectCloseRate(sprintId);
        sprintTargetVO.setDefectCloseRate(defectCloseRate !=null ? defectCloseRate.setScale(4, BigDecimal.ROUND_HALF_UP): new BigDecimal(0));
        return String.valueOf(sprintTargetVO.getDefectCloseRate()
                .multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP))+"%";
    }

    /**
     * 缺陷密度=新增缺陷数/关闭故事点数
     * @param sprintId
     * @return
     */
    @Override
    public String getDefectDensity(Long sprintId) {
        SprintTargetVO sprintTargetVO = new SprintTargetVO();
        //缺陷密度=新增缺陷数/关闭故事点数
        BigDecimal defectDensity = issueTargetMapper.getDefectDensity(sprintId);
        sprintTargetVO.setDefectDensity(defectDensity != null ? defectDensity.setScale(2, BigDecimal.ROUND_HALF_UP):new BigDecimal(0));
        return String.valueOf(sprintTargetVO.getDefectDensity());
    }

    /**
     *遗留缺陷比例=未关闭缺陷个数/当前迭代的总缺陷
     * @param sprintId
     * @return
     */
    @Override
    public String getRemainDefectRate(Long sprintId) {
        SprintTargetVO sprintTargetVO = new SprintTargetVO();
        //遗留缺陷比例=未关闭缺陷个数/当前迭代的总缺陷
        BigDecimal remainDefectRate = issueTargetMapper.getRemainDefectRate(sprintId);
        sprintTargetVO.setRemainDefectRate(remainDefectRate != null ? remainDefectRate.setScale(4, BigDecimal.ROUND_HALF_UP):new BigDecimal(0));
        return String.valueOf(sprintTargetVO.getRemainDefectRate()
                .multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP))+"%";
    }

    /**
     * 本迭代内线上反馈的缺陷
     * @param sprintId
     * @return
     */
    @Override
    public String getOnlineDefect(Long sprintId) {
        SprintTargetVO sprintTargetVO = new SprintTargetVO();
        //本迭代内线上反馈的缺陷
        BigDecimal onlineDefect = issueTargetMapper.getOnlineDefect(sprintId);
        sprintTargetVO.setOnlineDefect(onlineDefect != null ? onlineDefect : new BigDecimal(0));
        return String.valueOf(sprintTargetVO.getOnlineDefect());
    }

    /**
     * 平均线上缺陷修复时长
     * @param sprintId
     * @return
     */
    @Override
    public String getOnlineRepairTime(Long sprintId) {
        SprintTargetVO sprintTargetVO = new SprintTargetVO();
        //平均线上缺陷修复时长
        BigDecimal onlineRepairTime = issueTargetMapper.getOnlineRepairTime(sprintId);
        sprintTargetVO.setOnlineRepairTime(onlineRepairTime != null ? onlineRepairTime.setScale(2, BigDecimal.ROUND_HALF_UP): new BigDecimal(0));
        return String.valueOf(sprintTargetVO.getOnlineRepairTime());
    }


    /**
     * 统计存储迭代信息
     * @param sprintId
     */
    public void saveSprintStatistics(Long sprintId){
        if(sprintId == null){
            return;
        }

        SprintStatisticsEntity sprintStatisticsEntity = getSprintStatistics(sprintId,false);

        if (sprintStatisticsEntity != null) {

            SprintStatisticsEntity sprintStatisticsObj = sprintStatisticsMapper.querySprintStatisticsById(sprintId);
            if(sprintStatisticsObj == null){
                sprintStatisticsMapper.insert(sprintStatisticsEntity);
            }else{
                SprintStatisticsEntity sprintStatisticsCon = new SprintStatisticsEntity();
                sprintStatisticsCon.setSprintId(sprintId);
                Wrapper<SprintStatisticsEntity> ew = Wrappers.query(sprintStatisticsCon);
                sprintStatisticsMapper.delete(ew);
                sprintStatisticsMapper.insert(sprintStatisticsEntity);
            }
        }
    }

    /**
     * 查找目标值在哪个配置区间,并且返回区间分数
     * @param rangeList
     * @param target
     * @return
     */
    private BigDecimal getScoreByRange(List<ConfigSideEntity> rangeList,BigDecimal target) {

        if (!CollectionUtils.isEmpty(rangeList) && target != null) {

            // 根据左区间升序
            Collections.sort(rangeList, (v1, v2) -> v1.getLeftSide().compareTo(v2.getLeftSide()));

            for (ConfigSideEntity range : rangeList) {

                if (target.compareTo(range.getLeftSide()) != -1
                        && (range.getRightSide() == null || target.compareTo(range.getRightSide()) == -1)) {

                    return range.getScore() == null ? new BigDecimal(0) : range.getScore();
                }
            }
        }

        return new BigDecimal(0);
    }

    private void convertSprintTargetRate(SprintStatisticsEntity sprintStatisticsEntity, ConfigProjectVO configProjectVO) {

        // 保存计算该数据的对应issue，便于调试和debug问题
        String sprintNewStory = issueTargetMapper.getSprintNewStory(sprintStatisticsEntity.getSprintId());
        String sprintNewStoryFinish = issueTargetMapper.getSprintNewStoryFinish(sprintStatisticsEntity.getSprintId());
        String sprintOldStory = issueTargetMapper.getSprintOldStory(sprintStatisticsEntity.getSprintId());
        String sprintOldStoryFinish = issueTargetMapper.getSprintOldStoryFinish(sprintStatisticsEntity.getSprintId());
        sprintStatisticsEntity.setSprintNewStory(sprintNewStory);
        sprintStatisticsEntity.setSprintNewStoryFinish(sprintNewStoryFinish);
        sprintStatisticsEntity.setSprintOldStory(sprintOldStory);
        sprintStatisticsEntity.setSprintOldStoryFinish(sprintOldStoryFinish);

        //迭代目标完成率=新增故事关闭率*[新增故事关闭权重]+历史遗留故事关闭率*[历史遗留故事关闭权重]
        BigDecimal sprintAddStoryCloseRate = issueTargetMapper.getSprintAddStoryCloseRate(sprintStatisticsEntity.getSprintId());
        BigDecimal historyStoryCloseRate = issueTargetMapper.getHistoryStoryCloseRate(sprintStatisticsEntity.getSprintId());
        sprintAddStoryCloseRate = sprintAddStoryCloseRate == null ? new BigDecimal(0) : sprintAddStoryCloseRate;

        // 当历史遗留故事点数为0时，新增故事关闭权重的比例默认为100%
        // 新增故事关闭权重
        BigDecimal newStoryCloseWeight = configProjectVO.getNewStoryCloseWeight() == null ? new BigDecimal(0) : configProjectVO.getNewStoryCloseWeight();
        if (historyStoryCloseRate == null) {

            newStoryCloseWeight = new BigDecimal(1);
            historyStoryCloseRate = new BigDecimal(0);
        }

        // 历史遗留故事关闭权重
        BigDecimal historyStoryCloseWeight = configProjectVO.getHistoryStoryCloseWeight() == null ? new BigDecimal(0) : configProjectVO.getHistoryStoryCloseWeight();

        // 计算迭代目标完成率
        BigDecimal sprintTargetRate = sprintAddStoryCloseRate.multiply(newStoryCloseWeight).add(historyStoryCloseRate.multiply(historyStoryCloseWeight));
        sprintTargetRate = sprintTargetRate != null ? sprintTargetRate : new BigDecimal(0);

        //迭代目标完成率得分=[迭代目标完成率基础分]*迭代目标完成率/[迭代目标完成率基线值]
        BigDecimal sprintTargetRateScore = new BigDecimal(0);
        if(configProjectVO.getSprintCompletionRateBaseLine() != null){

            // 迭代目标完成率基础分
            BigDecimal sprintCompletionRateBaseScore = configProjectVO.getSprintCompletionRateBaseScore() == null ? new BigDecimal(0) : configProjectVO.getSprintCompletionRateBaseScore();
            sprintTargetRateScore = sprintCompletionRateBaseScore.multiply(sprintTargetRate).divide(configProjectVO.getSprintCompletionRateBaseLine(),2, BigDecimal.ROUND_HALF_DOWN);
        }

        sprintStatisticsEntity.setSprintTargetRate(String.valueOf(sprintTargetRate.multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP))+"%");
        sprintStatisticsEntity.setSprintTargetRateScore(String.valueOf(sprintTargetRateScore));
    }

    private void convertAvgStoryPoint(SprintStatisticsEntity sprintStatisticsEntity, ConfigProjectVO configProjectVO) {
        //人均关闭故事点数
        BigDecimal avgStoryPoint = issueTargetMapper.getAvgStoryPoint(sprintStatisticsEntity.getSprintId());
        avgStoryPoint = avgStoryPoint != null ? avgStoryPoint:new BigDecimal(0);

        //人均关闭故事点数得分=[人均关闭故事点数基础分]*人均关闭故事点数/[人均关闭故事点数基线值]
        BigDecimal personClosePointBaseScore = configProjectVO.getPersonClosePointBaseScore() == null ? new BigDecimal(0) : configProjectVO.getPersonClosePointBaseScore();
        BigDecimal personClosePointBaseLine = configProjectVO.getPersonClosePointBaseLine() == null ? new BigDecimal(0) : configProjectVO.getPersonClosePointBaseLine();

        // 计算分数
        BigDecimal avgStoryPointScore = personClosePointBaseScore.multiply(avgStoryPoint).divide(personClosePointBaseLine, 2, BigDecimal.ROUND_HALF_UP);

        sprintStatisticsEntity.setAvgStoryPoint(String.valueOf(avgStoryPoint.setScale(2, BigDecimal.ROUND_HALF_UP)));
        sprintStatisticsEntity.setAvgStoryPointScore(String.valueOf(avgStoryPointScore));
    }

    private void convertAvgStoryTime(SprintStatisticsEntity sprintStatisticsEntity, ConfigProjectVO configProjectVO) {
        //平均故事关闭时长
        BigDecimal avgStoryTime = issueTargetMapper.getAvgStoryTime(sprintStatisticsEntity.getSprintId());
        avgStoryTime = avgStoryTime != null ? avgStoryTime : new BigDecimal(0);

        // 根据[平均故事关闭时长得分区间]得分
        BigDecimal score = getScoreByRange(configProjectVO.getAvgStoryCloseTimeRange(), avgStoryTime);

        sprintStatisticsEntity.setAvgStoryTime(String.valueOf(avgStoryTime.setScale(2, BigDecimal.ROUND_HALF_UP)));
        sprintStatisticsEntity.setAvgStoryTimeScore(String.valueOf(score));
    }

    private void convertAvgStoryDelay(SprintStatisticsEntity sprintStatisticsEntity, ConfigProjectVO configProjectVO) {
        //平均故事延期时长
        BigDecimal avgStoryDelay = issueTargetMapper.getAvgStoryDelay(sprintStatisticsEntity.getSprintId());
        avgStoryDelay = avgStoryDelay != null ? avgStoryDelay : new BigDecimal(0);

        // 根据[平均故事延期时长得分区间]得分
        BigDecimal score = getScoreByRange(configProjectVO.getAvgStoryDelayTimeRange(), avgStoryDelay);

        sprintStatisticsEntity.setAvgStoryDelay(String.valueOf(avgStoryDelay.setScale(2, BigDecimal.ROUND_HALF_UP)));
        sprintStatisticsEntity.setAvgStoryDelayScore(String.valueOf(score));
    }

    private void convertReopenStory(SprintStatisticsEntity sprintStatisticsEntity, ConfigProjectVO configProjectVO) {
        //重新打开故事次数
        BigDecimal reopenStory = issueTargetMapper.getReopenStory(sprintStatisticsEntity.getSprintId());
        reopenStory = reopenStory != null ? reopenStory : new BigDecimal(0);

        // 根据[故事重新打开次数得分区间]得分
        BigDecimal score = getScoreByRange(configProjectVO.getStoryReopenNumRange(), reopenStory);

        sprintStatisticsEntity.setReopenStory(String.valueOf(reopenStory));
        sprintStatisticsEntity.setReopenStoryScore(String.valueOf(score));
    }

    private void convertRemainStory(SprintStatisticsEntity sprintStatisticsEntity, ConfigProjectVO configProjectVO) {
        //迭代遗留故事占比
        BigDecimal remainStory = issueTargetMapper.getRemainStroy(sprintStatisticsEntity.getSprintId());
        remainStory = remainStory != null ? remainStory:new BigDecimal(0);

        // 根据[迭代遗留故事占比得分区间]得分
        BigDecimal score = getScoreByRange(configProjectVO.getLeftOverStoryRateRange(), remainStory);

        sprintStatisticsEntity.setRemainStory(String.valueOf(remainStory.multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP))+"%");
        sprintStatisticsEntity.setRemainStoryScore(String.valueOf(score));

    }

    private void convertOverdueStoryPoint(SprintStatisticsEntity sprintStatisticsEntity, ConfigProjectVO configProjectVO) {
        //逾期完成故事点数
        BigDecimal overdueStoryPoint = issueTargetMapper.getOverdueStoryPoint(sprintStatisticsEntity.getSprintId());
        overdueStoryPoint = overdueStoryPoint != null ? overdueStoryPoint:new BigDecimal(0);

        // 根据[逾期完成故事点数得分区间]得分
        BigDecimal score = getScoreByRange(configProjectVO.getOverdueCompletePointRange(), overdueStoryPoint);

        sprintStatisticsEntity.setOverdueStoryPoint(String.valueOf(overdueStoryPoint.setScale(2, BigDecimal.ROUND_HALF_UP)));
        sprintStatisticsEntity.setOverdueStoryPointScore(String.valueOf(score));
    }

    private void convertDefectCloseRate(SprintStatisticsEntity sprintStatisticsEntity, ConfigProjectVO configProjectVO) {
        //缺陷关闭率=关闭缺陷数/当前迭代的总缺陷数
        BigDecimal defectCloseRate = issueTargetMapper.getDefectCloseRate(sprintStatisticsEntity.getSprintId());
        defectCloseRate = defectCloseRate != null ? defectCloseRate : new BigDecimal(0);

        //缺陷关闭率得分=[缺陷关闭率基础分]*缺陷关闭率/[迭代缺陷关闭率基线值]
        BigDecimal defectCloseRateBaseScore = configProjectVO.getDefectCloseRateBaseScore() == null ? new BigDecimal(0) : configProjectVO.getDefectCloseRateBaseScore();
        BigDecimal defectCloseRateBaseLine = configProjectVO.getDefectCloseRateBaseLine() == null ? new BigDecimal(0) : configProjectVO.getDefectCloseRateBaseLine();

        // 计算分数
        BigDecimal defectCloseRateScore = defectCloseRateBaseScore.multiply(defectCloseRate).divide(defectCloseRateBaseLine,2, BigDecimal.ROUND_HALF_UP);

        sprintStatisticsEntity.setDefectCloseRate(String.valueOf(defectCloseRate.multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP))+"%");
        sprintStatisticsEntity.setDefectCloseRateScore(String.valueOf(defectCloseRateScore));
    }

    private void convertDefectDensity(SprintStatisticsEntity sprintStatisticsEntity, ConfigProjectVO configProjectVO) {
        //缺陷密度=当前迭代总缺陷数/关闭故事点数
        BigDecimal defectDensity = issueTargetMapper.getDefectDensity(sprintStatisticsEntity.getSprintId());
        defectDensity = defectDensity != null ? defectDensity : new BigDecimal(0);

        // 缺陷密度得分=[缺陷密度基础分]*[缺陷密度基线值]/bug密度   新增缺陷数为0，得[缺陷密度基础分]
        BigDecimal defectDensityBaseScore = configProjectVO.getDefectDensityBaseScore() == null ? new BigDecimal(0) : configProjectVO.getDefectDensityBaseScore();
        BigDecimal defectDensityBaseLine = configProjectVO.getDefectDensityBaseLine() == null ? new BigDecimal(0) : configProjectVO.getDefectDensityBaseLine();

        // 计算分数
        BigDecimal defectDensityScore = defectDensityBaseScore;
        if (defectDensity.compareTo(new BigDecimal(0)) == 1) {

            defectDensityScore = defectDensityBaseScore.multiply(defectDensityBaseLine).divide(defectDensity,2, BigDecimal.ROUND_HALF_UP);
        }

        sprintStatisticsEntity.setDefectDensity(String.valueOf(defectDensity.setScale(2, BigDecimal.ROUND_HALF_UP)));
        sprintStatisticsEntity.setDefectDensityScore(String.valueOf(defectDensityScore));
    }

    private void convertRemainDefectRate(SprintStatisticsEntity sprintStatisticsEntity, ConfigProjectVO configProjectVO) {
        //遗留缺陷比例=未关闭缺陷个数/当前迭代的总缺陷
        BigDecimal remainDefectRate = issueTargetMapper.getRemainDefectRate(sprintStatisticsEntity.getSprintId());
        remainDefectRate = remainDefectRate != null ? remainDefectRate : new BigDecimal(0);

        // 根据[遗留缺陷比例得分区间]得分
        BigDecimal score = getScoreByRange(configProjectVO.getLeftOverDefectRateRange(), remainDefectRate);

        sprintStatisticsEntity.setRemainDefectRate(String.valueOf(remainDefectRate.multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP))+"%");
        sprintStatisticsEntity.setRemainDefectRateScore(String.valueOf(score));
    }

    private void convertOnlineDefect(SprintStatisticsEntity sprintStatisticsEntity, ConfigProjectVO configProjectVO) {
        //本迭代内线上反馈的缺陷
        BigDecimal onlineDefect = issueTargetMapper.getOnlineDefect(sprintStatisticsEntity.getSprintId());
        onlineDefect = onlineDefect != null ? onlineDefect : new BigDecimal(0);

        // 根据[线上缺陷数量得分区间]得分
        BigDecimal score = getScoreByRange(configProjectVO.getOnlineDefectNumRange(), onlineDefect);

        sprintStatisticsEntity.setOnlineDefect(String.valueOf(onlineDefect));
        sprintStatisticsEntity.setOnlineDefectScore(String.valueOf(score));
    }

    private void convertOnlineRepairTime(SprintStatisticsEntity sprintStatisticsEntity, ConfigProjectVO configProjectVO) {
        //平均线上缺陷修复时长
        BigDecimal onlineRepairTime = issueTargetMapper.getOnlineRepairTime(sprintStatisticsEntity.getSprintId());
        onlineRepairTime = onlineRepairTime != null ? onlineRepairTime : new BigDecimal(0);

        // 根据[线上缺陷平均修复时长得分区间]得分
        BigDecimal score = getScoreByRange(configProjectVO.getOnlineDefectAvgRepairTimeRange(), onlineRepairTime);

        sprintStatisticsEntity.setOnlineRepairTime(String.valueOf(onlineRepairTime.setScale(2, BigDecimal.ROUND_HALF_UP)));
        sprintStatisticsEntity.setOnlineRepairTimeScore(String.valueOf(score));
    }

    private void convertAddStoryScore(SprintStatisticsEntity sprintStatisticsEntity, ConfigProjectVO configProjectVO) {
        //迭代期间新增故事数
        BigDecimal addStory = issueTargetMapper.getAddStory(sprintStatisticsEntity.getSprintId());
        addStory = addStory != null ? addStory : new BigDecimal(0);

        // 迭代期间新增故事数扣分，一个故事扣[迭代新增故事扣分基数]
        BigDecimal sprintAddStoryDeductionBase = configProjectVO.getSprintAddStoryDeductionBase() == null ? new BigDecimal(0) : configProjectVO.getSprintAddStoryDeductionBase();

        // 计算分数
        BigDecimal addStoryScore = addStory.multiply(sprintAddStoryDeductionBase).negate();

        sprintStatisticsEntity.setAddStory(String.valueOf(addStory));
        sprintStatisticsEntity.setAddStoryScore(String.valueOf(addStoryScore.setScale(2,BigDecimal.ROUND_HALF_UP)));
    }

    /**
     * 根据迭代ID获取迭代统计信息
     * @param sprintId
     * @return
     */
    @Override
    public SprintStatisticsEntity getSprintStatisticsEntityBySprintId(Long sprintId) {

        return getSprintStatistics(sprintId,true);
    }

    /**
     * 获取迭代统计数据
     * @param sprintId
     * @param findHistory
     * @return
     */
    private SprintStatisticsEntity getSprintStatistics(Long sprintId,boolean findHistory) {

        if(sprintId == null){
            return null;
        }

        SprintStatisticsEntity result = null;

        if (findHistory) {

            SprintStatisticsEntity sprintStatisticsCon = new SprintStatisticsEntity();
            sprintStatisticsCon.setSprintId(sprintId);
            Wrapper<SprintStatisticsEntity> ew = Wrappers.query(sprintStatisticsCon);
            result = sprintStatisticsMapper.selectOne(ew);
        }

        if (result == null) {

            // 临时统计
            ConfigProjectVO configProjectVO = configProjectService.getConfigProjectRuleInfo();
            if(configProjectVO.getPkid() == null){

                return null;
            }

            SprintStatisticsEntity sprintStatisticsEntity = new SprintStatisticsEntity();
            sprintStatisticsEntity.setSprintId(sprintId);
            //迭代目标完成率
            convertSprintTargetRate(sprintStatisticsEntity, configProjectVO);
            //人均关闭故事点数
            convertAvgStoryPoint(sprintStatisticsEntity, configProjectVO);
            //平均故事关闭时长
            convertAvgStoryTime(sprintStatisticsEntity, configProjectVO);
            //平均故事延期时长
            convertAvgStoryDelay(sprintStatisticsEntity, configProjectVO);
            //重新打开故事次数
            convertReopenStory(sprintStatisticsEntity, configProjectVO);
            //迭代遗留故事占比
            convertRemainStory(sprintStatisticsEntity, configProjectVO);
            //逾期完成故事点数
            convertOverdueStoryPoint(sprintStatisticsEntity, configProjectVO);
            //缺陷关闭率
            convertDefectCloseRate(sprintStatisticsEntity, configProjectVO);
            //缺陷密度
            convertDefectDensity(sprintStatisticsEntity, configProjectVO);
            //遗留缺陷比例
            convertRemainDefectRate(sprintStatisticsEntity, configProjectVO);
            //本迭代内线上反馈的缺陷
            convertOnlineDefect(sprintStatisticsEntity, configProjectVO);
            //平均线上缺陷修复时长
            convertOnlineRepairTime(sprintStatisticsEntity, configProjectVO);
            //迭代期间新增故事数
            convertAddStoryScore(sprintStatisticsEntity, configProjectVO);
            sprintStatisticsEntity.setCreatedTime(new Date());

            result = sprintStatisticsEntity;
        }

        return result;
    }
}