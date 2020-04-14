package com.citms.modules.jira.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.citms.common.exception.RRException;
import com.citms.common.constant.JiraConstants;
import com.citms.modules.jira.enums.DictionaryEnum;
import com.citms.modules.jira.dao.DictionaryMapper;
import com.citms.modules.jira.entity.DictionaryEntity;
import com.citms.modules.jira.service.DictionaryService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 */
@Service
public class DictionaryServiceImpl extends ServiceImpl<DictionaryMapper,DictionaryEntity> implements
                                                                                    DictionaryService {

    @Autowired
    private DictionaryMapper dictionaryMapper;

    @Override
    public DictionaryEntity getDictionaryByCodeAndValue(String code, String value) {
        if(StringUtils.isEmpty(code) || StringUtils.isEmpty(value)){
            throw new RRException("code或者value为空");
        }

        DictionaryEntity dictionaryCon = new DictionaryEntity();
        dictionaryCon.setCode(code);
        dictionaryCon.setDictValue(value);
        dictionaryCon.setIsDeleted(JiraConstants.VALID);
        Wrapper<DictionaryEntity> ew = Wrappers.query(dictionaryCon);
        DictionaryEntity dictionaryEntity = dictionaryMapper.selectOne(ew);
        return dictionaryEntity;
    }

    /**
     *根据code 和key查找字典
     * @param code
     * @param key
     * @return
     */
    @Override
    public DictionaryEntity getDictionaryByCodeAndKey(String code, String key) {
        if(StringUtils.isEmpty(code) || StringUtils.isEmpty(key)){
            throw new RRException("code或者key为空");
        }

        DictionaryEntity dictionaryCon = new DictionaryEntity();
        dictionaryCon.setCode(code);
        dictionaryCon.setDictKey(key);
        dictionaryCon.setIsDeleted(JiraConstants.VALID);
        Wrapper<DictionaryEntity> ew = Wrappers.query(dictionaryCon);
        DictionaryEntity dictionaryEntity = dictionaryMapper.selectOne(ew);
        return dictionaryEntity;
    }

    /**
     * 根据code查找字典
     * @param code
     * @return
     */
    @Override
    public List<DictionaryEntity> getDictionaryByCode(String code) {
        if(StringUtils.isEmpty(code)){
            throw new RRException("code为空");
        }

        DictionaryEntity dictionaryCon = new DictionaryEntity();
        dictionaryCon.setCode(code);
        dictionaryCon.setIsDeleted(JiraConstants.VALID);
        Wrapper<DictionaryEntity> ew = Wrappers.query(dictionaryCon).orderByAsc("dict_key");
        return dictionaryMapper.selectList(ew);
    }

    /**
     * 获取人员类型
     * @return
     */
    @Override
    public List<DictionaryEntity> getUserTypeList() {
        List<DictionaryEntity> parentUserTypeList = getUserTypeListByParent(0L);
        if(CollectionUtils.isEmpty(parentUserTypeList)){
            return null;
        }

        List<DictionaryEntity> list = new ArrayList<>();
        for(DictionaryEntity parentEntity : parentUserTypeList){
            List<DictionaryEntity> childList = getUserTypeListByParent(parentEntity.getId());
            if(CollectionUtils.isEmpty(childList)){
                list.add(parentEntity);
                continue;
            }
            for(DictionaryEntity childDictionary : childList){
                DictionaryEntity dictionaryEntity = new DictionaryEntity();
                dictionaryEntity.setId(childDictionary.getId());
                dictionaryEntity.setDictKey(childDictionary.getDictKey());
                dictionaryEntity.setParentId(childDictionary.getParentId());
                String dictValue = String.join("-",new String[]{ parentEntity.getDictValue(), childDictionary.getDictValue()});
                dictionaryEntity.setDictValue(dictValue);
                list.add(dictionaryEntity);
            }
        }
        return list;
    }

    private List<DictionaryEntity> getUserTypeListByParent(Long parentId){
        DictionaryEntity dictionaryCon = new DictionaryEntity();
        dictionaryCon.setCode(DictionaryEnum.USER_TYPE.getName());
        dictionaryCon.setParentId(parentId);
        dictionaryCon.setIsDeleted(JiraConstants.VALID);
        Wrapper<DictionaryEntity> ew = Wrappers.query(dictionaryCon).orderByAsc("dict_key");
        return dictionaryMapper.selectList(ew);
    }



}