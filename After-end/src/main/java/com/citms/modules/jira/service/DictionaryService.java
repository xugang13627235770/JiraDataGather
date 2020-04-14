package com.citms.modules.jira.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.citms.modules.jira.entity.DictionaryEntity;

import java.util.List;

/**
 */
public interface DictionaryService extends IService<DictionaryEntity> {

    /**
     * 根据code 和value查找字典
     * @return
     */
    DictionaryEntity getDictionaryByCodeAndValue(String code, String value);

    /**
     *根据code 和key查找字典
     * @param code
     * @param key
     * @return
     */
    DictionaryEntity getDictionaryByCodeAndKey(String code, String key);

    /**
     * 根据code查找字典
     * @param code
     * @return
     */
    List<DictionaryEntity> getDictionaryByCode(String code);

    /**
     * 获取人员类型
     * @return
     */
    List<DictionaryEntity> getUserTypeList();

}