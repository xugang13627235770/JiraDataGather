package com.citms.modules.tester.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.citms.common.utils.PageUtils;
import com.citms.modules.tester.entity.ConfigTesterEntity;
import com.citms.modules.tester.vo.ConfigTesterVO;
import com.citms.modules.tester.vo.TesterRecordVO;

import java.util.List;

/**
 * 开发人员统计
 */
public interface TesterService extends IService<ConfigTesterEntity> {

    /**
     * 统计测试迭代内的数据-分页
     * @param sprintIdList
     * @return
     */
    PageUtils getTesterRecordPage(IPage<TesterRecordVO> iPage, List<String> sprintIdList);

    /**
     *统计测试迭代内的数据
     * @param sprintIdList
     * @return
     */
    List<TesterRecordVO> getTesterRecordList(List<String> sprintIdList);

    /**
     *保存测试配置文档
     */
    void saveConfigTester(ConfigTesterVO configTesterVO);

    /**
     *保存测试者配置信息
     * @return
     */
    ConfigTesterVO getConfigTesterVO();
}