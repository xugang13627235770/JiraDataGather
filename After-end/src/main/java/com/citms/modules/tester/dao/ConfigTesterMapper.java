package com.citms.modules.tester.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.citms.modules.tester.entity.ConfigTesterEntity;
import com.citms.modules.tester.vo.TesterRecordVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author copyright by
 * @since 2018/03
 * @version R1.0
 * @category 测试参数配置
 */
@Mapper
public interface ConfigTesterMapper extends BaseMapper<ConfigTesterEntity> {

    List<TesterRecordVO> getTesterRecordPage(/*IPage<TesterRecordVO> iPage, */@Param("ids") List<String> ids);

    List<TesterRecordVO> getTesterRecordList(@Param("ids") List<String> ids);

    /**
     *测试统计的数据
     * @return
     */
    Integer  getTesterRecordCount(@Param("ids") List<String> ids);

}