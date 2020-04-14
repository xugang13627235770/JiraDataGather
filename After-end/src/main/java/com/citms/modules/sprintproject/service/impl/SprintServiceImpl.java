package com.citms.modules.sprintproject.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.citms.common.exception.RRException;
import com.citms.modules.sprintproject.dao.SprintMapper;
import com.citms.modules.sprintproject.dao.SprintProjectMapper;
import com.citms.modules.project.entity.ProjectEntity;
import com.citms.modules.sprintproject.entity.SprintEntity;
import com.citms.modules.sprintproject.entity.SprintProjectEntity;
import com.citms.modules.project.service.ProjectService;
import com.citms.modules.sprintproject.service.SprintService;
import com.citms.modules.sprintproject.vo.SprintInfoVO;
import com.citms.modules.sprintproject.vo.SprintProjectVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 */
@Service
public class SprintServiceImpl extends ServiceImpl<SprintMapper,SprintEntity> implements SprintService {

    @Autowired
    private SprintMapper sprintMapper;

    @Autowired
    private ProjectService projectService;
    @Autowired
    private SprintProjectMapper sprintProjectMapper;
    /**
     * 根据项目ID获取迭代信息
     * @return
     */
    @Override
    public List<SprintEntity> getSprintListByProjectId(Long projectId) {
        if(projectId == null){
            throw new RRException("projectId信息为空");
        }
        return sprintMapper.getSprintListByProjectId(projectId);
    }

    /**
     * 查询未关联迭代信息
     * @return
     */
    @Override
    public List<SprintEntity> getUnrelatedSprintList() {
        return sprintMapper.getUnrelatedSprintList();
    }

    /**
     * 获取所有的项目和关联的迭代信息
     * @return
     */
    @Override
    public List<SprintProjectVO> getRelatedSprintList(List<Long> projectIdList) {
        if(CollectionUtils.isEmpty(projectIdList)){
            return null;
        }

        List<SprintInfoVO> sprintList = sprintMapper.getSprintListByProjectIds(projectIdList);
        Map<Long,List<SprintInfoVO>> sprintMap = null;
        if(CollectionUtils.isNotEmpty(sprintList)){
            sprintMap = sprintList.stream().collect(groupingBy(sprintInfoVO->sprintInfoVO.getProjectId()));
        }

        List<SprintProjectVO> sprintProjectList = new ArrayList<>();
        for(Long projectId : projectIdList){
            SprintProjectVO sprintProjectVO = new SprintProjectVO();
            sprintProjectVO.setProjectId(projectId);
            ProjectEntity projectEntity = projectService.getProjectById(projectId);
            if(projectEntity != null){
                sprintProjectVO.setProjectName(projectEntity.getName());
            }
            if(sprintMap != null && CollectionUtils.isNotEmpty(sprintMap.get(projectId))){
                List<SprintInfoVO> sprintEntityList = sprintMap.get(projectId);
                sprintProjectVO.setSprintList(sprintEntityList);
            }
            sprintProjectList.add(sprintProjectVO);
        }
        return sprintProjectList;
    }

    /**
     * 保存迭代关联信息
     * @param sprintProjectVOList
     */
    @Override
    public void saveSprintRelatedInfo(List<SprintInfoVO> sprintProjectVOList) {
        if(CollectionUtils.isEmpty(sprintProjectVOList)){
            throw new RRException("参数错误");
        }
        List<Long> projectIdList = sprintProjectVOList.stream().map(SprintInfoVO::getProjectId).collect(toList());
        sprintProjectMapper.batchDeleteByProjectIds(projectIdList);

        List<SprintProjectEntity> sprintProjectList = new ArrayList<>();
        for(SprintInfoVO sprintInfoVO : sprintProjectVOList){
            if(StringUtils.isNotEmpty(sprintInfoVO.getSprintIdStr())){
                String[] sprintIdArray = sprintInfoVO.getSprintIdStr().split(",");
                for(String sprintId : sprintIdArray){
                    SprintProjectEntity sprintProjectEntity = new SprintProjectEntity();
                    sprintProjectEntity.setProjectId(sprintInfoVO.getProjectId());
                    sprintProjectEntity.setSprintId(Long.valueOf(sprintId));
                    sprintProjectList.add(sprintProjectEntity);
                }
            }
        }
        sprintProjectMapper.batchSaveSprintProject(sprintProjectList);
    }
}