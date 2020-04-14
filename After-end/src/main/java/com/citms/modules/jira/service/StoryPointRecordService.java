package com.citms.modules.jira.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.citms.modules.jira.entity.StoryPointRecord;
import com.citms.modules.jira.vo.StoryPointChartVO;

import java.text.ParseException;
import java.util.List;

/**

 * @since 2018/03
 * @version R1.0
 * 故事点数记录
 */
public interface StoryPointRecordService extends IService<StoryPointRecord> {

    /**
     * 获取当前迭代故事点数
     * @return
     */
    List<StoryPointRecord> getCurrentStoryPointRecord();

    /**
     * 存储迭代故事点数
     */
    void saveStoryPointRecord() throws ParseException;

    /**
     * 根据迭代ID获取故事点数
     * @param sprintId
     * @return
     */
    StoryPointChartVO getStoryPointRecordBySprintId(Long sprintId);
}