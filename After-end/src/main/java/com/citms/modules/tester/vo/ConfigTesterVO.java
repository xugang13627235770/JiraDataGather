package com.citms.modules.tester.vo;

import com.citms.modules.tester.entity.ConfigSideEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 类ConfigTesterVO的实现描述：TODO 类实现描述 
 * @author DELL 2019/11/20 15:09\
 * 测试配置信息
 */
@Data
public class ConfigTesterVO {

    private Long pkid;

    private BigDecimal totalBaseScore;//总基础分

    private BigDecimal issueValidBase;//缺陷有效率统计基数

    private BigDecimal violateMaxScore;//违规操作最大扣分值

    private BigDecimal onlineIssueRatio;//线上缺陷系数

    private List<ConfigSideEntity> issueNumList;//缺陷数量加分区间

    private List<ConfigSideEntity> taskTimeList;//缺陷平均耗时(天数)加分区间

    private List<ConfigSideEntity> historyCloseList;//历史缺陷关闭加分区间

    private List<ConfigSideEntity> validRateList;//缺陷有效率扣分区间
}
