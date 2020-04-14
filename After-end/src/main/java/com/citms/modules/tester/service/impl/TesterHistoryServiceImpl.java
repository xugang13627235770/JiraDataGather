package com.citms.modules.tester.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.citms.modules.tester.dao.TesterHistoryMapper;
import com.citms.modules.sprintproject.entity.SprintProjectEntity;
import com.citms.modules.tester.entity.TesterHistoryEntity;
import com.citms.modules.sprintproject.service.SprintProjectService;
import com.citms.modules.tester.service.TesterHistoryService;
import com.citms.modules.tester.service.TesterService;
import com.citms.modules.tester.vo.TesterRecordVO;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 */
@Service
public class TesterHistoryServiceImpl extends ServiceImpl<TesterHistoryMapper,TesterHistoryEntity>
        implements TesterHistoryService {

    @Autowired
    private TesterService        testerService;
    @Autowired
    private TesterHistoryMapper  testerHistoryMapper;
    @Autowired
    private SprintProjectService sprintProjectService;

    /**
     * 保存测试统计历史记录信息
     * @param sprintId
     */
    @Override
    public void saveTesterHistory(Long sprintId) {
        if(sprintId == null){
            return;
        }
        List<String> sprintIdList = Lists.newArrayList(String.valueOf(sprintId));
        List<TesterRecordVO> testerRecordList = testerService.getTesterRecordList(sprintIdList);
        if(CollectionUtils.isEmpty(testerRecordList)){
            return;
        }

        SprintProjectEntity sprintProjectEntity = sprintProjectService.getSprintProjectEntityBySprintId(sprintId);
        if(sprintProjectEntity == null){
            return;
        }

        //1、删除重复记录
        deleteTesterHistory(sprintProjectEntity.getProjectId(), sprintId);
        //3、插入当前关闭的迭代信息
        List<TesterHistoryEntity> developerHistoryList = new ArrayList<>();
        for(TesterRecordVO testerRecord : testerRecordList){
            TesterHistoryEntity testerHistoryEntity = new TesterHistoryEntity();
            testerHistoryEntity.setProjectId(sprintProjectEntity.getProjectId());
            testerHistoryEntity.setSprintId(sprintId);
            testerHistoryEntity.setUserId(testerRecord.getUserId());
            testerHistoryEntity.setUserName(testerRecord.getUserName());
            testerHistoryEntity.setIssueValidNum(testerRecord.getIssueValidNum());
            testerHistoryEntity.setIssueValidRate(testerRecord.getIssueValidRate());
            testerHistoryEntity.setViolateOperateTimes(testerRecord.getViolateOperateTimes());
            testerHistoryEntity.setIssueAddNum(testerRecord.getIssueAddNum());
            testerHistoryEntity.setTaskTimes(testerRecord.getTaskTimes());
            testerHistoryEntity.setHistoryCloseNum(testerRecord.getHistoryCloseNum());
            testerHistoryEntity.setOnlineIssueNum(testerRecord.getOnlineIssueNum());
            testerHistoryEntity.setTotalScore(testerRecord.getTotalScore());
            developerHistoryList.add(testerHistoryEntity);
        }
        testerHistoryMapper.batchSaveTesterHistory(developerHistoryList);
    }

    /**
     * 删除历史重复记录
     * @param projectId
     * @param sprintId
     */
    private void deleteTesterHistory(Long projectId, Long sprintId){
        if(projectId == null || sprintId == null){
            return;
        }

        TesterHistoryEntity testerHistoryCon = new TesterHistoryEntity();
        testerHistoryCon.setProjectId(projectId);
        testerHistoryCon.setSprintId(sprintId);
        Wrapper<TesterHistoryEntity> ew = Wrappers.query(testerHistoryCon);
        testerHistoryMapper.delete(ew);
    }

    @Override
    public void updateNextSprintId(Long sprintId) {
        SprintProjectEntity lastSprintProject = sprintProjectService.getLastSprintProjectBySprintId(sprintId);
        if(lastSprintProject == null){
            return;
        }

        TesterHistoryEntity testerHistoryCon = new TesterHistoryEntity();
        testerHistoryCon.setSprintId(lastSprintProject.getSprintId());
        Wrapper<TesterHistoryEntity> ew = Wrappers.query(testerHistoryCon);

        TesterHistoryEntity testerHistoryEntity = new TesterHistoryEntity();
        testerHistoryEntity.setNextSprintId(sprintId);
        testerHistoryMapper.update(testerHistoryEntity, ew);
    }
}