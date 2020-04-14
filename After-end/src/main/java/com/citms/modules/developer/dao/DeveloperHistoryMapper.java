package com.citms.modules.developer.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.citms.modules.developer.entity.DeveloperHistoryEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author copyright by
 * @since 2018/03
 * @version R1.0
 * @category 开发统计记录
 */
@Mapper
public interface DeveloperHistoryMapper extends BaseMapper<DeveloperHistoryEntity> {

    /**
     * 批量保存开发统计历史记录
     * @param developerHistoryList
     * @return
     */
    boolean batchSaveDeveloperHistory(@Param("developerHistoryList") List<DeveloperHistoryEntity> developerHistoryList);

}