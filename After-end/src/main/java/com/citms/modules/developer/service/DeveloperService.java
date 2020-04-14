package com.citms.modules.developer.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.citms.common.utils.PageUtils;
import com.citms.modules.developer.entity.ConfigDeveloperEntity;
import com.citms.modules.developer.vo.DeveloperRecordVO;

import java.util.List;

/**
 * 开发人员统计
 */
public interface DeveloperService extends IService<ConfigDeveloperEntity> {

    /**
     * 统计开发迭代内的数据
     * @return
     */
    PageUtils getDeveloperRecordPage(IPage<DeveloperRecordVO> iPage, List<String> sprintIdList);

    /**
     * 获取开发迭代内的数据
     * @param sprintIdList
     * @return
     */
    List<DeveloperRecordVO> getDeveloperRecordList(List<String> sprintIdList);

    /**
     *保存开发配置文档
     */
    void saveConfigDeveloper(ConfigDeveloperEntity configDeveloper);

}