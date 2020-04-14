package com.citms.modules.tester.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.citms.common.exception.RRException;
import com.citms.common.utils.PageUtils;
import com.citms.common.constant.JiraConstants;
import com.citms.modules.tester.dao.ConfigSideMapper;
import com.citms.modules.tester.dao.ConfigTesterMapper;
import com.citms.modules.tester.dao.TesterHistoryMapper;
import com.citms.modules.tester.entity.ConfigSideEntity;
import com.citms.modules.tester.entity.ConfigTesterEntity;
import com.citms.modules.tester.entity.TesterHistoryEntity;
import com.citms.modules.sprintproject.service.SprintProjectService;
import com.citms.modules.tester.service.TesterService;
import com.citms.modules.tester.vo.ConfigTesterVO;
import com.citms.modules.tester.vo.TesterRecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

import static java.util.stream.Collectors.toMap;

/**
 */
@Service
public class TesterServiceImpl extends ServiceImpl<ConfigTesterMapper,ConfigTesterEntity>
        implements TesterService {

    @Autowired
    private ConfigTesterMapper configTesterMapper;

    @Autowired
    private ConfigSideMapper     configSideMapper;
    @Autowired
    private SprintProjectService sprintProjectService;
    @Autowired
    private TesterHistoryMapper  testerHistoryMapper;

    @Override
    public PageUtils getTesterRecordPage(IPage<TesterRecordVO> iPage, List<String> sprintIdList) {
        IPage<TesterRecordVO> page =  new Page<TesterRecordVO>();
        List<TesterRecordVO> testerRecordList = getTesterRecordList(/*iPage,*/ sprintIdList);
        if(CollectionUtils.isEmpty(testerRecordList)){
            return new PageUtils(page);
        }
        composeHistoryOrder(sprintIdList, testerRecordList);
        page.setRecords(testerRecordList);
        page.setTotal(testerRecordList.size()/*configTesterMapper.getTesterRecordCount(sprintIdList)*/);
        return new PageUtils(page);
    }

    /**
     *统计测试迭代内的数据
     * @param sprintIdList
     * @return
     */
    @Override
    public List<TesterRecordVO> getTesterRecordList(List<String> sprintIdList) {
        if(org.springframework.util.CollectionUtils.isEmpty(sprintIdList)){
            return null;
        }

        List<TesterRecordVO> testerRecordList = null;
        if(sprintIdList.size()>1){
            //大于一个迭代取实时数据
            testerRecordList = configTesterMapper.getTesterRecordList(sprintIdList);
            composeTesterRecord(testerRecordList);
        }else if(sprintIdList.size()==1){
            TesterHistoryEntity testerHistoryCon = new TesterHistoryEntity();
            testerHistoryCon.setSprintId(Long.valueOf(sprintIdList.get(0)));
            Wrapper<TesterHistoryEntity> ew = Wrappers.query(testerHistoryCon).orderByDesc("total_score");
            List<TesterHistoryEntity> testerHistoryList = testerHistoryMapper.selectList(ew);

            if(CollectionUtils.isEmpty(testerHistoryList)){
                //历史记录没有数据则实时取数据
                testerRecordList = configTesterMapper.getTesterRecordList(sprintIdList);
                composeTesterRecord(testerRecordList);
                return testerRecordList;
            }

            testerRecordList = new ArrayList<>();
            for(TesterHistoryEntity testerHistoryEntity : testerHistoryList){
                TesterRecordVO testerRecord = new TesterRecordVO();
                testerRecord.setUserId(testerHistoryEntity.getUserId());
                testerRecord.setUserName(testerHistoryEntity.getUserName());
                testerRecord.setIssueValidNum(testerHistoryEntity.getIssueValidNum());
                testerRecord.setIssueValidRate(testerHistoryEntity.getIssueValidRate().setScale(2,BigDecimal.ROUND_HALF_UP));
                testerRecord.setViolateOperateTimes(testerHistoryEntity.getViolateOperateTimes());
                testerRecord.setIssueAddNum(testerHistoryEntity.getIssueAddNum());
                testerRecord.setTaskTimes(testerHistoryEntity.getTaskTimes().setScale(2,BigDecimal.ROUND_HALF_UP));
                testerRecord.setHistoryCloseNum(testerHistoryEntity.getHistoryCloseNum());
                testerRecord.setOnlineIssueNum(testerHistoryEntity.getOnlineIssueNum());
                testerRecord.setTotalScore(testerHistoryEntity.getTotalScore());
                testerRecordList.add(testerRecord);
            }
        }
        return testerRecordList;
    }

    private void composeTesterRecord(List<TesterRecordVO> testerRecordList){
        if(CollectionUtils.isEmpty(testerRecordList)){
            return;
        }

        ConfigTesterVO configTesterVO = getConfigTesterVO();
        if(configTesterVO == null){
            throw new RRException("请配置测试统计数据");
        }

        for(TesterRecordVO testerRecord : testerRecordList){
            /*缺陷有效率扣分值 = 解决结果为“已解决”和“未解决”状态的缺陷数量 > 缺陷有效率统计基数
             ? FIND(迭代内解决结果为“已解决”和“未解决”状态的缺陷数量 / 迭代总缺陷数, 缺陷有效率扣分区间) : 0    */
            BigDecimal issueValidScore = testerRecord.getIssueValidRate() != null ? testerRecord.getIssueValidRate():new BigDecimal(0);
            if(testerRecord.getIssueValidNum() != null && testerRecord.getIssueValidRate() != null
               && testerRecord.getIssueValidNum().compareTo(configTesterVO.getIssueValidBase()) > 0){

                for(ConfigSideEntity configSide : configTesterVO.getValidRateList()){
                    if(configSide.getLeftSide().compareTo(testerRecord.getIssueValidRate()) <= 0
                       && (configSide.getRightSide() == null || configSide.getRightSide().compareTo(testerRecord.getIssueValidRate()) > 0)){
                        issueValidScore = configSide.getScore();
                        break;
                    }
                }
            }

            //违规操作扣分值 = MAX(违规操作最大扣分值, 未指定经办人或经办人为测试人员的缺陷数量)
            BigDecimal violateOperateScore = configTesterVO.getViolateMaxScore();
            if(testerRecord.getViolateOperateTimes() != null && testerRecord.getViolateOperateTimes().compareTo(configTesterVO.getViolateMaxScore())>0){
                violateOperateScore = testerRecord.getViolateOperateTimes();
            }

            //有效缺陷新增加分值 = FIND(迭代内新增的、解决结果为“已解决”和“未解决”状态的缺陷数量, 缺陷数量加分区间)
            BigDecimal validIssueScore = new BigDecimal(0);
            if(testerRecord.getIssueAddNum() != null){
                validIssueScore = testerRecord.getIssueAddNum();
                for(ConfigSideEntity configSide : configTesterVO.getIssueNumList()){
                    if(configSide.getLeftSide().compareTo(testerRecord.getIssueAddNum()) <= 0
                       && (configSide.getRightSide() == null || configSide.getRightSide().compareTo(testerRecord.getIssueAddNum()) > 0)){
                        validIssueScore = configSide.getScore();
                        break;
                    }
                }
            }

            //缺陷平均耗时加分值 = FIND(迭代内新增且关闭的缺陷总耗时 / 迭代内新增且关闭的缺陷数量, 缺陷平均耗时加分区间)
            BigDecimal takeTimesScore = new BigDecimal(0);
            if(testerRecord.getTaskTimes() != null){
                takeTimesScore = testerRecord.getTaskTimes();
                for(ConfigSideEntity configSide : configTesterVO.getTaskTimeList()){
                    if(configSide.getLeftSide().compareTo(testerRecord.getTaskTimes()) <= 0
                       && (configSide.getRightSide() == null || configSide.getRightSide().compareTo(testerRecord.getTaskTimes()) > 0)){
                        takeTimesScore = configSide.getScore();
                        break;
                    }
                }
            }

            //历史缺陷关闭加分值 = FIND(迭代内历史遗留的缺陷关闭数量, 历史缺陷关闭加分区间)
            BigDecimal historyCloseScore = new BigDecimal(0);
            if(testerRecord.getHistoryCloseNum() != null){
                historyCloseScore = testerRecord.getHistoryCloseNum();
                for(ConfigSideEntity configSide : configTesterVO.getHistoryCloseList()){
                    if(configSide.getLeftSide().compareTo(testerRecord.getHistoryCloseNum()) <= 0
                       && (configSide.getRightSide() == null || configSide.getRightSide().compareTo(testerRecord.getHistoryCloseNum()) > 0)){
                        historyCloseScore = configSide.getScore();
                        break;
                    }
                }
            }

            //线上反馈缺陷扣分值 = 线上反馈缺陷数量 * 线上缺陷系数
            BigDecimal onlineIssueScore = new BigDecimal(0);
            if(testerRecord.getOnlineIssueNum() != null){
                onlineIssueScore = testerRecord.getOnlineIssueNum().multiply(configTesterVO.getOnlineIssueRatio());
            }

            //总积分 = 总基础分 + 有效缺陷新增加分值 + 缺陷平均耗时加分值 + 历史缺陷关闭加分值 - 缺陷有效率扣分值 - 违规操作扣分值 - 线上反馈缺陷扣分值
            BigDecimal totalScore = configTesterVO.getTotalBaseScore()
                    .add(validIssueScore)
                    .add(takeTimesScore)
                    .subtract(historyCloseScore)
                    .subtract(issueValidScore)
                    .subtract(violateOperateScore)
                    .subtract(onlineIssueScore);
            testerRecord.setTotalScore(totalScore);
        }

        //对总积分降序
        Collections.sort(testerRecordList, Comparator.comparing(TesterRecordVO::getTotalScore).reversed());
    }

    /**
     * 与历史记录比较
     * @param sprintIdList
     * @param testerRecordList
     */
    private void composeHistoryOrder(List<String> sprintIdList, List<TesterRecordVO> testerRecordList){
        if(CollectionUtils.isEmpty(sprintIdList) || sprintIdList.size() > 1){
            return;
        }

        List<TesterHistoryEntity> testerHistoryList = getTesterHistoryListBySprintId(Long.valueOf(sprintIdList.get(0)));
        if(org.springframework.util.CollectionUtils.isEmpty(testerHistoryList)){
            return;
        }

        Map<Long,TesterHistoryEntity> testerHistoryMap = testerHistoryList.stream()
                .collect(toMap(testerHistory->testerHistory.getUserId(), testerHistory->testerHistory));

        for(TesterRecordVO testerRecord : testerRecordList){
            TesterHistoryEntity testerHistoryEntity = testerHistoryMap.get(testerRecord.getUserId());
            if(testerHistoryEntity == null){
                continue;
            }
            if(testerRecord.getTotalScore().compareTo(testerHistoryEntity.getTotalScore()) > 0){
                testerRecord.setOrder(JiraConstants.ORDER_UP);
            }else if(testerRecord.getTotalScore().compareTo(testerHistoryEntity.getTotalScore()) < 0){
                testerRecord.setOrder(JiraConstants.ORDER_DOWN);
            }else if(testerRecord.getTotalScore().compareTo(testerHistoryEntity.getTotalScore()) == 0){
                testerRecord.setOrder(JiraConstants.ORDER_MIDDLE);
            }

        }
    }

    /**
     * 获取开发统计历史记录
     * @return
     */
    private List<TesterHistoryEntity> getTesterHistoryListBySprintId(Long sprintId){
        TesterHistoryEntity testerHistoryCon = new TesterHistoryEntity();
        testerHistoryCon.setNextSprintId(sprintId);
        Wrapper<TesterHistoryEntity> ewTester = Wrappers.query(testerHistoryCon);
        List<TesterHistoryEntity> testerHistoryList = testerHistoryMapper.selectList(ewTester);
        return testerHistoryList;
    }

    @Override
    public void saveConfigTester(ConfigTesterVO configTesterVO) {
        if(configTesterVO == null){
            throw new RRException("请配置测试数据");
        }

        storageConfigTester(configTesterVO);
        storageConfigSide(configTesterVO);
    }


    /**
     * 存储测试配置信息
     * @param configTesterVO
     */
    private void storageConfigTester(ConfigTesterVO configTesterVO){
        ConfigTesterEntity configTester = new ConfigTesterEntity();
        configTester.setTotalBaseScore(configTesterVO.getTotalBaseScore());
        configTester.setIssueValidBase(configTesterVO.getIssueValidBase());
        configTester.setViolateMaxScore(configTesterVO.getViolateMaxScore());
        configTester.setOnlineIssueRatio(configTesterVO.getOnlineIssueRatio());

        ConfigTesterEntity configTesterResult = configTesterMapper.selectById(JiraConstants.TESTER_PKID);
        if(configTesterResult == null){
            configTesterMapper.insert(configTester);
        }else{
            configTester.setPkid(JiraConstants.TESTER_PKID);
            configTesterMapper.updateById(configTester);
        }
    }

    /**
     * 存储区间配置
     * @param configTesterVO
     */
    private void storageConfigSide(ConfigTesterVO configTesterVO){

        if(CollectionUtils.isNotEmpty(configTesterVO.getIssueNumList())){
            deleteConfigSideByCode(JiraConstants.CODE_ISSUE_NUM);
            for(ConfigSideEntity configSide : configTesterVO.getIssueNumList()){
                configSideMapper.insert(configSide);
            }
        }

        if(CollectionUtils.isNotEmpty(configTesterVO.getTaskTimeList())){
            deleteConfigSideByCode(JiraConstants.CODE_TAKE_TIME);
            for(ConfigSideEntity configSide : configTesterVO.getTaskTimeList()){
                configSideMapper.insert(configSide);
            }
        }

        if(CollectionUtils.isNotEmpty(configTesterVO.getHistoryCloseList())){
            deleteConfigSideByCode(JiraConstants.CODE_HISTORY_CLOSE);
            for(ConfigSideEntity configSide : configTesterVO.getHistoryCloseList()){
                configSideMapper.insert(configSide);
            }
        }

        if(CollectionUtils.isNotEmpty(configTesterVO.getValidRateList())){
            deleteConfigSideByCode(JiraConstants.CODE_VALID_RATE);
            for(ConfigSideEntity configSide : configTesterVO.getValidRateList()){
                configSideMapper.insert(configSide);
            }
        }

    }

    /**
     *
     * @param code
     */
    private void deleteConfigSideByCode(String code){
        ConfigSideEntity configSideCon = new ConfigSideEntity();
        configSideCon.setCode(code);
        Wrapper<ConfigSideEntity> ew = Wrappers.query(configSideCon);
        configSideMapper.delete(ew);
    }

    /**
     * 获取测试配置信息
     * @return
     */
    @Override
    public ConfigTesterVO getConfigTesterVO() {
        ConfigTesterVO configTesterVO = new ConfigTesterVO();

        ConfigTesterEntity configTester = configTesterMapper.selectById(JiraConstants.TESTER_PKID);
        if(configTester != null){
            configTesterVO.setTotalBaseScore(configTester.getTotalBaseScore());
            configTesterVO.setIssueValidBase(configTester.getIssueValidBase());
            configTesterVO.setViolateMaxScore(configTester.getViolateMaxScore());
            configTesterVO.setOnlineIssueRatio(configTester.getOnlineIssueRatio());
        }

        List<ConfigSideEntity> issueNumList = configSideMapper.getConfigSideByCode(JiraConstants.CODE_ISSUE_NUM);
        configTesterVO.setIssueNumList(issueNumList);
        List<ConfigSideEntity> takeTimeList = configSideMapper.getConfigSideByCode(JiraConstants.CODE_TAKE_TIME);
        configTesterVO.setTaskTimeList(takeTimeList);
        List<ConfigSideEntity> historyCloseList = configSideMapper.getConfigSideByCode(JiraConstants.CODE_HISTORY_CLOSE);
        configTesterVO.setHistoryCloseList(historyCloseList);
        List<ConfigSideEntity> validRateList = configSideMapper.getConfigSideByCode(JiraConstants.CODE_VALID_RATE);
        configTesterVO.setValidRateList(validRateList);
        return configTesterVO;
    }
}