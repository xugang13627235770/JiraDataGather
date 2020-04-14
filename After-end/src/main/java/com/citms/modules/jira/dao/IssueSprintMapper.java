package com.citms.modules.jira.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.citms.modules.jira.entity.IssueSprintEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 */
@Mapper
public interface IssueSprintMapper extends BaseMapper<IssueSprintEntity> {

    IssueSprintEntity queryIssueSprintById(@Param("issueId") Long issueId, @Param("sprintId") Long sprintId);

    void deleteByIssueId(Long issueId);
}