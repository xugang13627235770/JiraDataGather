package com.citms.modules.jira.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.citms.common.constant.JiraConstants;
import com.citms.modules.jira.dao.StoryPointRecordMapper;
import com.citms.modules.jira.entity.StoryPointRecord;
import com.citms.modules.jira.service.StoryPointRecordService;
import com.citms.modules.jira.vo.StoryPointChartVO;
import com.citms.modules.jira.vo.StoryPointObj;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

/**
 */
@Service
public class StoryPointRecordServiceImpl extends ServiceImpl<StoryPointRecordMapper,StoryPointRecord> implements
                                                                                    StoryPointRecordService {

    @Autowired
    private StoryPointRecordMapper storyPointRecordMapper;

    /**
     * 获取当前迭代故事点数
     * @return
     */
    @Override
    public List<StoryPointRecord> getCurrentStoryPointRecord() {
        return storyPointRecordMapper.getCurrentStoryPointRecord();
    }

    /**
     * 存储迭代故事点数
     */
    @Override
    public void saveStoryPointRecord() throws ParseException {
        List<StoryPointRecord> storyPointRecordList = getCurrentStoryPointRecord();
        if(CollectionUtils.isEmpty(storyPointRecordList)){
            return;
        }

        //删除当天的迭代故事点数记录
        StoryPointRecord storyPointRecordCon = new StoryPointRecord();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String s = sdf.format(new Date());
        storyPointRecordCon.setSprintDate(s);
        Wrapper<StoryPointRecord> ew = Wrappers.query(storyPointRecordCon);
        storyPointRecordMapper.delete(ew);

        //批量插入当天的迭代故事点数
        storyPointRecordMapper.batchSaveStoryPointRecord(storyPointRecordList);
    }

    /**
     * 根据迭代ID获取故事点数
     * /**
     *      * {
     *      *               timeArr:["11/1","11/2","11/3"],
     *      *               list:[
     *      *                 {type:"剩余故事点数",data:[8,1,2]},
     *      *               ]
     *      *             }
     *
     */
    @Override
    public StoryPointChartVO getStoryPointRecordBySprintId(Long sprintId) {
        if(sprintId == null){
            return null;
        }

        StoryPointChartVO storyPointChart = new StoryPointChartVO();
        List<String> dateList = new ArrayList<String>();
        Date now = new Date();
        for(int i=0;i<=30;i+=2){
            Date startDate = DateUtils.addDays(now, i - 30);
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
            dateList.add(sdf.format(startDate));
        }
        storyPointChart.setTimeArr(dateList);
        storyPointChart.setList(new ArrayList<StoryPointObj>());

        StoryPointRecord storyPointRecordCon = new StoryPointRecord();
        storyPointRecordCon.setSprintId(sprintId);
        Wrapper<StoryPointRecord> ew = Wrappers.query(storyPointRecordCon).orderByAsc("sprint_date");;
        List<StoryPointRecord> storyPointRecordList =  storyPointRecordMapper.selectList(ew);
        if(CollectionUtils.isEmpty(storyPointRecordList)){
            return storyPointChart;
        }

        Map<String,StoryPointRecord> storyPointRecordMap = storyPointRecordList.stream().collect(
                toMap(storyPointRecord->storyPointRecord.getSprintDate(), storyPointRecord->storyPointRecord));

        List<StoryPointObj> list = new ArrayList<>();
        List<BigDecimal> issueNumList = new ArrayList<>();
        for(int i=0;i<=30;i+=2){
            Date startDate = DateUtils.addDays(now, i-30);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String startDateStr = sdf.format(startDate);
            if(storyPointRecordMap != null && storyPointRecordMap.get(startDateStr) != null){
                StoryPointRecord storyPointRecord = storyPointRecordMap.get(startDateStr);
                issueNumList.add(storyPointRecord.getStoryPoint());
            }else{
                issueNumList.add(new BigDecimal(0));
            }
        }
        StoryPointObj storyPointObj = new StoryPointObj();
        storyPointObj.setType(JiraConstants.STORY_POINT_STR);
        storyPointObj.setData(issueNumList);
        list.add(storyPointObj);
        storyPointChart.setList(list);
        return storyPointChart;

    }
}