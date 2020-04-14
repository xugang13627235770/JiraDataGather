package com.citms.modules.user.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.citms.common.utils.PageUtils;
import com.citms.modules.jira.enums.DictionaryEnum;
import com.citms.modules.jira.dao.IssueMapper;
import com.citms.modules.user.dao.UserMapper;
import com.citms.modules.jira.entity.DictionaryEntity;
import com.citms.modules.user.entity.UserEntity;
import com.citms.modules.jira.service.DictionaryService;
import com.citms.modules.user.service.UserService;
import com.citms.modules.user.vo.UserVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,UserEntity> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private IssueMapper issueMapper;
    /**
     * 查询用户列表
     * @param iPage
     * @param key
     * @param userType
     * @return
     */
    @Override
    public PageUtils getUserList(IPage<UserEntity> iPage, String key, String userType) {

        IPage<UserVO> page =  new Page<UserVO>();
        List<UserEntity> userList = userMapper.getUserListByCon(iPage, key, userType);
        if(CollectionUtils.isEmpty(userList)){
            return new PageUtils(page);
        }

        List<Long> userIdList = userList.stream().map(UserEntity::getId).collect(toList());
        List<UserVO> totalStoryList = issueMapper.getTotalStoryByUserIds(userIdList);
        Map<Long, Long> totalStoryMap = null;
        if(CollectionUtils.isNotEmpty(totalStoryList)){
            totalStoryMap = totalStoryList.stream().collect(toMap(user->user.getId(), user->user.getTotalStory()));
        }
        Map<Long, Long> totalDefectMap = null;
        List<UserVO> totalDefectList = issueMapper.getTotalDefectByUserIds(userIdList);
        if(CollectionUtils.isNotEmpty(totalDefectList)){
            totalDefectMap = totalDefectList.stream().collect(toMap(user->user.getId(), user->user.getTotalDefect()));
        }

        List<UserVO> list = new ArrayList<>();
        for(UserEntity userEntity : userList){
            UserVO userVO = new UserVO();
            userVO.setId(userEntity.getId());
            userVO.setName(userEntity.getName());
            userVO.setDisplayName(userEntity.getDisplayName());
            userVO.setEmailAddress(userEntity.getEmailAddress());
            if(totalStoryMap != null){
                userVO.setTotalStory(totalStoryMap.get(userEntity.getId()));
            }
            if(totalDefectMap != null){
                userVO.setTotalDefect(totalDefectMap.get(userEntity.getId()));
            }
            userVO.setUserTypeStr(getUserTypeValueByType(userEntity.getUserType()));
            userVO.setUpdateTime(userEntity.getUpdateTime());
            list.add(userVO);
        }
        page.setRecords(list);
        page.setTotal(userMapper.getUserListCount(key, userType));
        return new PageUtils(page);
    }

    /**
     * 根据type获取用户类型
     * @param userType
     * @return
     */
    private String getUserTypeValueByType(Integer userType){
        if(userType == null){
            return null;
        }
        DictionaryEntity dictionaryEntity = dictionaryService.getDictionaryByCodeAndKey(DictionaryEnum.USER_TYPE.getName(),String.valueOf(userType));
        if(dictionaryEntity.getParentId() == null || dictionaryEntity.getParentId() == 0){
            return dictionaryEntity.getDictValue();
        }else{
            DictionaryEntity parentDictionary = dictionaryService.getById(dictionaryEntity.getParentId());
            if(parentDictionary != null){
                return String.join("-",new String[]{parentDictionary.getDictValue(),dictionaryEntity.getDictValue()});
            }
        }
        return null;
    }

    /**
     * 批量修改用户类型
     * @param ids
     * @param userType
     */
    @Override
    public void updateUserTypeByIds(List<String> ids, String userType) {
        if(CollectionUtils.isEmpty(ids) || StringUtils.isEmpty(userType)){
            return;
        }

        userMapper.updateUserTypeByIds(ids, userType);

    }
}