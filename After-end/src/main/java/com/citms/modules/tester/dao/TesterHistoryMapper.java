package com.citms.modules.tester.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.citms.modules.tester.entity.TesterHistoryEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author copyright by
 * @since 2018/03
 * @version R1.0
 * @category 测试统计记录
 */
@Mapper
public interface TesterHistoryMapper extends BaseMapper<TesterHistoryEntity> {

    /**
     * 批量保存测试统计历史记录
     * @param testerHistoryList
     * @return
     */
    boolean batchSaveTesterHistory(@Param("testerHistoryList") List<TesterHistoryEntity> testerHistoryList);
}