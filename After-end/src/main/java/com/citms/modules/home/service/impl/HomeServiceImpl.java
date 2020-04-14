package com.citms.modules.home.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.citms.common.constant.JiraConstants;
import com.citms.modules.home.service.HomeService;
import com.citms.modules.home.vo.*;
import com.citms.modules.jira.enums.DictionaryEnum;
import com.citms.modules.project.entity.ProjectEntity;
import com.citms.modules.project.service.ProjectService;
import com.citms.modules.jira.dao.IssueMapper;
import com.citms.modules.jira.entity.DictionaryEntity;
import com.citms.modules.jira.entity.IssueEntity;
import com.citms.modules.jira.service.DictionaryService;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.stream.Collectors.toMap;

/**
 */
@Service
public class HomeServiceImpl implements HomeService {

    @Autowired
    private IssueMapper    issueMapper;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private DictionaryService dictionaryService;

    /**
     * 获取首页问题统计
     * @return
     */
    @Override
    public Map<String, Object> getHomeIssue(Long projectId) {
        Map<String, Object> map = new HashMap<>();
        if(projectId == null){
            return map;
        }

        //问题类型统计
        List<IssueTypeVO> issueTypeList = issueMapper.getIssueTypeList(projectId);
        map.put("issueTypeList", issueTypeList);
        //人员统计
        List<IssueScaleVO> issueScaleList = issueMapper.getIssueScaleList(projectId);
        map.put("issueScaleList", issueScaleList);
        //总问题累计
        map.put("issueDateChart", computeIssueDate(projectId));

        //总的故事数缺陷数
        IssueStatisticsVO issueStatistics = new IssueStatisticsVO();
        ProjectEntity projectEntity = projectService.getProjectById(projectId);
        if(projectEntity != null){
            issueStatistics.setProjectName(projectEntity.getName());
            issueStatistics.setProjectId(projectEntity.getId());
        }
        issueStatistics.setTotalIssue(getTotalIssueByProjectId(projectId, JiraConstants.ISSUE_TYPE_2));
        issueStatistics.setTotalStory(getTotalIssueByProjectId(projectId, JiraConstants.ISSUE_TYPE_1));
        issueStatistics.setTotalStoryPoint(issueMapper.getTotalStoryPoint(projectId));
        issueStatistics.setTotalDeveloper(issueMapper.getTotalDeveloper(projectId));
        issueStatistics.setTotalTester(issueMapper.getTotalTester(projectId));
        issueStatistics.setTotalDesigner(issueMapper.getTotalDesigner(projectId));
        map.put("issueStatistics", issueStatistics);
        return map;
    }

    /**
     * {
     *               timeArr:["11/1","11/2","11/3"],
     *               list:[
     *                 {type:"代办",data:[8,1,2]},
     *                 {type:"进行中",data:[1,4,5]},
     *                 {type:"测试",data:[12,4,5]}
     *               ]
     *             }
     */
    private IssueDateChartVO computeIssueDate(Long projectId){
        IssueDateChartVO issueDateChart = new IssueDateChartVO();
        List<String> dateList = new ArrayList<String>();
        Date now = new Date();
        for(int i=0;i<=30;i+=3){
            Date startDate = DateUtils.addDays(now, i-30);
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
            dateList.add(sdf.format(startDate));
        }
        issueDateChart.setTimeArr(dateList);
        issueDateChart.setList(new ArrayList<IssueDateObj>());

//        List<IssueDateVO> issueDateList = issueMapper.getIssueDateList(projectId);
//        if(CollectionUtils.isEmpty(issueDateList)){
//            return issueDateChart;
//        }
        List<DictionaryEntity> dictionaryEntityList = dictionaryService.getDictionaryByCode(DictionaryEnum.ISSUE_STATUS.getName());
        if(CollectionUtils.isEmpty(dictionaryEntityList)){
            return issueDateChart;
        }

//        Map<String,List<IssueDateVO>> issueDateListMap = issueDateList.stream().collect(groupingBy(issueDateVO->issueDateVO.getIssueDate()));
        List<IssueDateObj> list = new ArrayList<>();
        for(DictionaryEntity dictionaryEntity : dictionaryEntityList){
            IssueDateObj issueDateObj = new IssueDateObj();
            issueDateObj.setIssueStatus(Integer.valueOf(dictionaryEntity.getDictKey()));
            issueDateObj.setType(dictionaryEntity.getDictValue());

            List<Integer> issueNumList = new ArrayList<>();
            for(int i=0;i<=30;i+=3){
                Date startDate = DateUtils.addDays(now, i-30);
//                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
                List<IssueDateVO> issueDateList = issueMapper.getIssueDateListByDate(projectId, startDate);
//                List<IssueDateVO>  issueDateVOList = issueDateListMap.get(sdf.format(startDate));
                Map<Integer,IssueDateVO> issueDateMap = null;
                if(CollectionUtils.isNotEmpty(issueDateList)){
                    issueDateMap = issueDateList.stream().collect(toMap(issueDateVO->issueDateVO.getIssueStatus(), issueDateVO->issueDateVO));
                }
                if(issueDateMap != null && issueDateMap.get(Integer.valueOf(dictionaryEntity.getDictKey())) != null){
                    IssueDateVO issueDateVO = issueDateMap.get(Integer.valueOf(dictionaryEntity.getDictKey()));
                    issueNumList.add(issueDateVO.getIssueNum());
                }else{
                    issueNumList.add(0);
                }
            }
            issueDateObj.setData(issueNumList);
            list.add(issueDateObj);
        }
        issueDateChart.setList(list);
        return issueDateChart;
    }

    //总的问题数
    private Integer getTotalIssueByProjectId(Long projectId, Integer type){
        IssueEntity issueCon = new IssueEntity();
        issueCon.setProjectId(projectId);
        issueCon.setIssuetype(type);
        QueryWrapper<IssueEntity> ew = Wrappers.query(issueCon);
        return issueMapper.selectCount(ew);
    }
}