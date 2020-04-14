package com.citms.modules.developer.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.citms.common.exception.RRException;
import com.citms.common.utils.PageUtils;
import com.citms.common.constant.JiraConstants;
import com.citms.modules.developer.dao.ConfigDeveloperMapper;
import com.citms.modules.developer.dao.DeveloperHistoryMapper;
import com.citms.modules.developer.entity.ConfigDeveloperEntity;
import com.citms.modules.developer.entity.DeveloperHistoryEntity;
import com.citms.modules.developer.service.DeveloperService;
import com.citms.modules.sprintproject.service.SprintProjectService;
import com.citms.modules.developer.vo.DeveloperRecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;

import static java.util.stream.Collectors.toMap;

/**
 */
@Service
public class DeveloperServiceImpl extends ServiceImpl<ConfigDeveloperMapper,ConfigDeveloperEntity>
        implements DeveloperService {

    @Autowired
    private ConfigDeveloperMapper configDeveloperMapper;

    @Autowired
    private DeveloperHistoryMapper developerHistoryMapper;
    @Autowired
    private SprintProjectService sprintProjectService;

    /**
     * 统计开发迭代内的数据
     * @return
     */
    @Override
    public PageUtils getDeveloperRecordPage(IPage<DeveloperRecordVO> iPage, List<String> sprintIdList) {
        IPage<DeveloperRecordVO> page =  new Page<DeveloperRecordVO>();
        List<DeveloperRecordVO> developerRecordList = getDeveloperRecordList(sprintIdList);
        composeHistoryOrder(sprintIdList, developerRecordList);
        page.setRecords(developerRecordList);
        page.setTotal(developerRecordList.size());
        return new PageUtils(page);
    }

    private void composeDeveloperRecordList(List<DeveloperRecordVO> developerRecordList){
        if(CollectionUtils.isEmpty(developerRecordList)){
            return;
        }

        ConfigDeveloperEntity configDeveloper = configDeveloperMapper.selectById(JiraConstants.DEVELOPER_PKID);
        if(configDeveloper == null){
            throw new RRException("请配置开发者统计数据");
        }

        for(DeveloperRecordVO developerRecord : developerRecordList){
            //关闭故事加分值 = MAX(故事基础分 * 关闭故事点数 / 基线关闭故事点数, 关闭故事最大加分值)
            BigDecimal closeStoryScore = new BigDecimal(0L);
            if(configDeveloper.getCloseStory()!= null && developerRecord.getCloseStoryNum() != null){
                closeStoryScore = configDeveloper.getStoryBaseScore()
                        .multiply(developerRecord.getCloseStoryNum()).divide(configDeveloper.getCloseStory(), 2, BigDecimal.ROUND_HALF_DOWN);
            }
            if(configDeveloper.getMaxScore() != null && closeStoryScore.compareTo(configDeveloper.getMaxScore()) > 0){
                closeStoryScore = configDeveloper.getMaxScore();
            }

            //提测延期扣分值 = SUM(实际提测日期-预计提测日期) * 提测延期系数
            BigDecimal testDelayScore = new BigDecimal(0);
            if(developerRecord.getTestDelayDays() != null){
                testDelayScore = new BigDecimal(developerRecord.getTestDelayDays()).multiply(configDeveloper.getTestDelayRatio());
            }

            //提测打回扣分值 = 迭代内故事、改进和任务重新打开次数 * 提测打回系数
            BigDecimal testBackScore = new BigDecimal(0);
            if(developerRecord.getTestBackTimes() != null){
                testBackScore = new BigDecimal(developerRecord.getTestBackTimes()).multiply(configDeveloper.getTestBackRatio());
            }

            //缺陷回归扣分值 = 迭代内缺陷重新打开次数 * 缺陷回归系数
            BigDecimal issueReturnScore = new BigDecimal(0);
            if(developerRecord.getIssueReturnTimes() != null){
                issueReturnScore = new BigDecimal(developerRecord.getIssueReturnTimes()).multiply(configDeveloper.getIssueReturnRatio());
            }

            //缺陷遗留扣分值 = 未关闭的不紧急、较急优先级缺陷数量 * 非红色缺陷系数 + 未关闭的紧急、致命、阻碍优先级缺陷数量 * 红色缺陷系数
            BigDecimal noRedIssueScore = new BigDecimal(0);
            if(developerRecord.getNoredIssue() != null){
                noRedIssueScore = new BigDecimal(developerRecord.getNoredIssue()).multiply(configDeveloper.getNoredIssueRatio());
            }
            BigDecimal redIssueScore = new BigDecimal(0);
            if(developerRecord.getRedIssue() != null){
                redIssueScore = new BigDecimal(developerRecord.getRedIssue()).multiply(configDeveloper.getRedIssueRatio());
            }
            BigDecimal issueScore = noRedIssueScore.add(redIssueScore);

            //有效缺陷扣分值 = (迭代内解决结果为“已解决”和“未解决”状态的总缺陷数 - 按工种缺陷密度*关闭故事点数) * 有效缺陷系数
            BigDecimal densityScore = new BigDecimal(0);
            BigDecimal validScore = new BigDecimal(0);
            if(developerRecord.getCloseStoryNum() != null && developerRecord.getValidIssueNum() != null){
                densityScore = convertDensity(developerRecord.getUserType(),configDeveloper).multiply(developerRecord.getCloseStoryNum());
                validScore = (new BigDecimal(developerRecord.getValidIssueNum()).subtract(densityScore)).multiply(configDeveloper.getValidIssueRatio());
            }

            //缺陷关闭率扣分值 = (缺陷关闭数量 / 迭代内所有缺陷数量) < 缺陷关闭率阈值 ? 缺陷未关闭系数 : 0
            BigDecimal closeRateScore = new BigDecimal(0);
            if(developerRecord.getIssueCloseRate() != null && developerRecord.getIssueCloseRate().compareTo(configDeveloper.getIssueCloseValue()) == -1 ){
                closeRateScore = configDeveloper.getNocloseIssueRatio();
            }

            //线上缺陷扣分值 = 线上反馈缺陷数量 * 线上缺陷系数
            BigDecimal onlineIssueScore = new BigDecimal(0);
            if(developerRecord.getOnlineIssue() != null){
                onlineIssueScore = new BigDecimal(developerRecord.getOnlineIssue()).multiply(configDeveloper.getOnlineIssueRatio());
            }

            /*总积分 = 总基础分 + 关闭故事加分值 - 提测延期扣分值 - 提测打回扣分值 - 缺陷回归扣分值 - 缺陷遗留扣分值
                    - 有效缺陷扣分值 - 缺陷关闭率扣分值 -线上反馈缺陷扣分值*/
            BigDecimal total = configDeveloper.getTotalBaseScore().add(closeStoryScore)
                    .subtract(testDelayScore)
                    .subtract(testBackScore)
                    .subtract(issueReturnScore)
                    .subtract(issueScore)
                    .subtract(validScore)
                    .subtract(closeRateScore)
                    .subtract(onlineIssueScore);
            developerRecord.setTotalScore(total.setScale(2, BigDecimal.ROUND_HALF_UP));

            if(developerRecord.getIssueCloseRate() != null){
                developerRecord.setIssueCloseRateStr(String.valueOf(developerRecord.getIssueCloseRate().multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP))+"%");
            }
        }

        //对总积分降序
        Collections.sort(developerRecordList, Comparator.comparing(DeveloperRecordVO::getTotalScore).reversed());
    }

    /**
     * 与历史记录比较
     * @param sprintIdList
     * @param developerRecordList
     */
    private void composeHistoryOrder(List<String> sprintIdList, List<DeveloperRecordVO> developerRecordList){
        if(CollectionUtils.isEmpty(sprintIdList) || sprintIdList.size()>1 || CollectionUtils.isEmpty(developerRecordList)){
            return;
        }

        List<DeveloperHistoryEntity> developerHistoryList = getDeveloperHistoryListBySprintId(Long.valueOf(sprintIdList.get(0)));
        if(CollectionUtils.isEmpty(developerHistoryList)){
            return;
        }

        Map<Long,DeveloperHistoryEntity> developerHistoryMap = developerHistoryList.stream()
                .collect(toMap(developerHistory->developerHistory.getUserId(), developerHistory->developerHistory));

        for(DeveloperRecordVO developerRecord : developerRecordList){
            DeveloperHistoryEntity developerHistoryEntity = developerHistoryMap.get(developerRecord.getUserId());
            if(developerHistoryEntity == null){
                continue;
            }
            if(developerRecord.getTotalScore().compareTo(developerHistoryEntity.getTotalScore()) > 0){
                developerRecord.setOrder(JiraConstants.ORDER_UP);
            }else if(developerRecord.getTotalScore().compareTo(developerHistoryEntity.getTotalScore()) < 0){
                developerRecord.setOrder(JiraConstants.ORDER_DOWN);
            }else if(developerRecord.getTotalScore().compareTo(developerHistoryEntity.getTotalScore()) == 0){
                developerRecord.setOrder(JiraConstants.ORDER_MIDDLE);
            }

        }
    }

    /**
     * 获取开发统计历史记录
     * @return
     */
    private List<DeveloperHistoryEntity> getDeveloperHistoryListBySprintId(Long sprintId){
        DeveloperHistoryEntity developerHistoryCon = new DeveloperHistoryEntity();
        developerHistoryCon.setNextSprintId(sprintId);
        Wrapper<DeveloperHistoryEntity> ewDeveloper = Wrappers.query(developerHistoryCon);
        List<DeveloperHistoryEntity> developerHistoryList = developerHistoryMapper.selectList(ewDeveloper);
        return developerHistoryList;
    }

    /**
     * 获取开发迭代内的数据
     * @param sprintIdList
     * @return
     */
    @Override
    public List<DeveloperRecordVO> getDeveloperRecordList(List<String> sprintIdList) {
        if(CollectionUtils.isEmpty(sprintIdList)){
            return null;
        }

        List<DeveloperRecordVO> developerRecordList = null;
        if(sprintIdList.size()>1){
            //大于一个迭代取实时数据
            developerRecordList = configDeveloperMapper.getDeveloperRecordList(sprintIdList);
            composeDeveloperRecordList(developerRecordList);
        }else if(sprintIdList.size()==1){
            DeveloperHistoryEntity developerHistoryCon = new DeveloperHistoryEntity();
            developerHistoryCon.setSprintId(Long.valueOf(sprintIdList.get(0)));
            Wrapper<DeveloperHistoryEntity> ew = Wrappers.query(developerHistoryCon).orderByDesc("total_score");
            List<DeveloperHistoryEntity> developerHistoryList = developerHistoryMapper.selectList(ew);
            //历史记录没有数据则实时取数据
            if(CollectionUtils.isEmpty(developerHistoryList)){
                developerRecordList = configDeveloperMapper.getDeveloperRecordList(sprintIdList);
                composeDeveloperRecordList(developerRecordList);
                return developerRecordList;
            }

            //历史记录有数据则取历史数据
            developerRecordList = new ArrayList<>();
            for(DeveloperHistoryEntity developerHistoryEntity : developerHistoryList){
                DeveloperRecordVO developerRecord = new DeveloperRecordVO();
                developerRecord.setUserId(developerHistoryEntity.getUserId());
                developerRecord.setDeveloperName(developerHistoryEntity.getDeveloperName());
                developerRecord.setCloseStoryNum(developerHistoryEntity.getCloseStoryNum().setScale(2,BigDecimal.ROUND_HALF_UP));
                developerRecord.setTestDelayDays(developerHistoryEntity.getTestDelayDays());
                developerRecord.setTestBackTimes(developerHistoryEntity.getTestBackTimes());
                developerRecord.setIssueReturnTimes(developerHistoryEntity.getIssueReturnTimes());
                developerRecord.setRedIssue(developerHistoryEntity.getRedIssue());
                developerRecord.setNoredIssue(developerHistoryEntity.getNoredIssue());
                developerRecord.setValidIssueNum(developerHistoryEntity.getValidIssueNum());
                developerRecord.setIssueCloseRate(developerHistoryEntity.getIssueCloseRate());
                developerRecord.setOnlineIssue(developerHistoryEntity.getOnlineIssue());
                developerRecord.setTotalScore(developerHistoryEntity.getTotalScore());
                if(developerHistoryEntity.getIssueCloseRate() != null){
                    developerRecord.setIssueCloseRateStr(String.valueOf(developerHistoryEntity.getIssueCloseRate().multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP))+"%");
                }
                developerRecordList.add(developerRecord);
            }
        }
        return developerRecordList;
    }

    /**
     * 用户类型转换相应的扣分值
     * @param userType
     * @param configDeveloper
     * @return
     */
    private BigDecimal convertDensity(Integer userType, ConfigDeveloperEntity configDeveloper){
        if(userType == null){
            return configDeveloper.getDensityDefault();
        }

        if(JiraConstants.DEVELOPER_JAVA.equals(userType)){
            return configDeveloper.getDensityJava();
        }else if(JiraConstants.DEVELOPER_FRONT.equals(userType)){
            return configDeveloper.getDensityFront();
        }else if(JiraConstants.DEVELOPER_BIGDATA.equals(userType)){
            return configDeveloper.getDensityBigdata();
        }else{
            return configDeveloper.getDensityDefault();
        }
    }

    /**
     * 保存开发配置文档
     * @param configDeveloper
     */
    @Override
    public void saveConfigDeveloper(ConfigDeveloperEntity configDeveloper) {
        ConfigDeveloperEntity configDeveloperCon = new ConfigDeveloperEntity();
        configDeveloperCon.setPkid(JiraConstants.DEVELOPER_PKID);
        Wrapper<ConfigDeveloperEntity> ew = Wrappers.query(configDeveloperCon);
        int count = configDeveloperMapper.selectCount(ew);
        if(count == 0){
            configDeveloperMapper.insert(configDeveloper);
        }else{
            configDeveloper.setPkid(JiraConstants.DEVELOPER_PKID);
            configDeveloperMapper.updateById(configDeveloper);
        }

    }
}