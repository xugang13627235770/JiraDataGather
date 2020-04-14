package com.citms.modules.sprintproject.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.citms.modules.sprintproject.dao.SprintProjectMapper;
import com.citms.modules.sprintproject.entity.SprintProjectEntity;
import com.citms.modules.sprintproject.service.SprintProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 */
@Service
public class SprintProjectServiceImpl extends ServiceImpl<SprintProjectMapper,SprintProjectEntity> implements
                                                                                                   SprintProjectService {

    @Autowired
    private SprintProjectMapper sprintProjectMapper;

    /**
     * 获取所有迭代项目关联信息
     * @return
     */
    @Override
    public List<SprintProjectEntity> getAllSprintProjectList() {
        return sprintProjectMapper.getAllSprintProjectList();
    }

    @Override
    public SprintProjectEntity getSprintProjectEntityBySprintId(Long sprintId) {
        if(sprintId == null){
            return null;
        }
        SprintProjectEntity sprintProjectCon = new SprintProjectEntity();
        sprintProjectCon.setSprintId(sprintId);
        Wrapper<SprintProjectEntity> ew = Wrappers.query(sprintProjectCon);
        SprintProjectEntity sprintProjectEntity = sprintProjectMapper.selectOne(ew);
        return sprintProjectEntity;
    }

    @Override
    public SprintProjectEntity getNextSprintProjectBySprintId(Long sprintId) {
        return getSprintProjectBySprintId(sprintId, true);
    }

    @Override
    public SprintProjectEntity getLastSprintProjectBySprintId(Long sprintId) {
        return getSprintProjectBySprintId(sprintId, false);
    }

    private SprintProjectEntity getSprintProjectBySprintId(Long sprintId, boolean isNext){
        List<SprintProjectEntity> sprintProjectList = sprintProjectMapper.getSprintProjectBySprintId(sprintId);
        if(CollectionUtils.isEmpty(sprintProjectList)){
            return null;
        }
        int index = -1;
        for(int i=0;i<sprintProjectList.size();i++){
            if(sprintProjectList.get(i).getSprintId().equals(sprintId)){
                if(isNext){
                    index = i+1;
                }else{
                    index = i-1;
                }
                break;
            }
        }
        if(index >= 0 && index < sprintProjectList.size()){
            return sprintProjectList.get(index);
        }
        return null;
    }
}