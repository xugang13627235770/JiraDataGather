package com.citms.modules.developer.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.citms.modules.developer.dao.DeveloperHistoryMapper;
import com.citms.modules.developer.entity.DeveloperHistoryEntity;
import com.citms.modules.sprintproject.entity.SprintProjectEntity;
import com.citms.modules.developer.service.DeveloperHistoryService;
import com.citms.modules.developer.service.DeveloperService;
import com.citms.modules.sprintproject.service.SprintProjectService;
import com.citms.modules.developer.vo.DeveloperRecordVO;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 */
@Service
public class DeveloperHistoryServiceImpl extends ServiceImpl<DeveloperHistoryMapper,DeveloperHistoryEntity>
        implements DeveloperHistoryService {

    @Autowired
    private DeveloperService developerService;
    @Autowired
    private DeveloperHistoryMapper developerHistoryMapper;
    @Autowired
    private SprintProjectService sprintProjectService;


    /**
     * 保存开发统计历史记录信息
     * @param sprintId
     */
    @Override
    public void saveDeveloperHistory(Long sprintId) {
        if(sprintId == null){
            return;
        }
        List<String> sprintIdList = Lists.newArrayList(String.valueOf(sprintId));
        List<DeveloperRecordVO> developerRecordList = developerService.getDeveloperRecordList(sprintIdList);
        if(CollectionUtils.isEmpty(developerRecordList)){
            return;
        }

        SprintProjectEntity sprintProjectEntity = sprintProjectService.getSprintProjectEntityBySprintId(sprintId);
        if(sprintProjectEntity == null){
            return;
        }

        //1、删除重复记录
        deleteDeveloperHistory(sprintProjectEntity.getProjectId(), sprintId);

        //2、插入当前关闭的迭代信息
        List<DeveloperHistoryEntity> developerHistoryList = new ArrayList<>();
        for(DeveloperRecordVO developerRecord : developerRecordList){
            DeveloperHistoryEntity developerHistoryEntity = new DeveloperHistoryEntity();
            developerHistoryEntity.setProjectId(sprintProjectEntity.getProjectId());
            developerHistoryEntity.setSprintId(sprintId);
            developerHistoryEntity.setUserId(developerRecord.getUserId());
            developerHistoryEntity.setDeveloperName(developerRecord.getDeveloperName());
            developerHistoryEntity.setCloseStoryNum(developerRecord.getCloseStoryNum());
            developerHistoryEntity.setTestDelayDays(developerRecord.getTestDelayDays());
            developerHistoryEntity.setTestBackTimes(developerRecord.getTestBackTimes());
            developerHistoryEntity.setIssueReturnTimes(developerRecord.getIssueReturnTimes());
            developerHistoryEntity.setRedIssue(developerRecord.getRedIssue());
            developerHistoryEntity.setNoredIssue(developerRecord.getNoredIssue());
            developerHistoryEntity.setValidIssueNum(developerRecord.getValidIssueNum());
            developerHistoryEntity.setIssueCloseRate(developerRecord.getIssueCloseRate());
            developerHistoryEntity.setOnlineIssue(developerRecord.getOnlineIssue());
            developerHistoryEntity.setTotalScore(developerRecord.getTotalScore());
            developerHistoryList.add(developerHistoryEntity);
        }
        developerHistoryMapper.batchSaveDeveloperHistory(developerHistoryList);
    }

    /**
     * 删除历史重复记录
     * @param projectId
     * @param sprintId
     */
    private void deleteDeveloperHistory(Long projectId, Long sprintId){
        if(projectId == null || sprintId == null){
            return;
        }

        DeveloperHistoryEntity developerHistoryCon = new DeveloperHistoryEntity();
        developerHistoryCon.setProjectId(projectId);
        developerHistoryCon.setSprintId(sprintId);
        Wrapper<DeveloperHistoryEntity> ew = Wrappers.query(developerHistoryCon);
        developerHistoryMapper.delete(ew);
    }

    /**
     * 更新下个迭代信息
     * @param sprintId
     */
    @Override
    public void updateNextSprintId(Long sprintId) {
        SprintProjectEntity lastSprintProject = sprintProjectService.getLastSprintProjectBySprintId(sprintId);
        if(lastSprintProject == null){
            return;
        }

        DeveloperHistoryEntity developerHistoryCon = new DeveloperHistoryEntity();
        developerHistoryCon.setSprintId(lastSprintProject.getSprintId());
        Wrapper<DeveloperHistoryEntity> ew = Wrappers.query(developerHistoryCon);

        DeveloperHistoryEntity developerHistoryEntity = new DeveloperHistoryEntity();
        developerHistoryEntity.setNextSprintId(sprintId);
        developerHistoryMapper.update(developerHistoryEntity,ew);
    }
}