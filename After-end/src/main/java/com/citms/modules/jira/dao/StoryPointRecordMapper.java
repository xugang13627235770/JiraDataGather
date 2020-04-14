package com.citms.modules.jira.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.citms.modules.jira.entity.StoryPointRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author copyright by
 * @since 2018/03
 * @version R1.0
 * @category 故事点数记录表
 */
@Mapper
public interface StoryPointRecordMapper extends BaseMapper<StoryPointRecord> {

    /**
     * 获取当前迭代故事点数
     * @return
     */
    List<StoryPointRecord> getCurrentStoryPointRecord();

    /**
     * 批量保存故事点数记录表
     * @return
     */
    boolean batchSaveStoryPointRecord(@Param("storyPointRecordList") List<StoryPointRecord> storyPointRecordList);
}