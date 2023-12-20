package com.nmw.ocrapi.config;

import com.alibaba.csp.sentinel.annotation.aspectj.SentinelResourceAspect;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * @author :ljq
 * @date :2023/9/15
 * @description: Sentinel熔断限流相关配置
 */
@Data
@Configuration
public class SentinelConfig {
    /**
     * sentinel资源名称
     */
    @Value("${sentinel-rule.resource}")
    private String sentinelRuleSource;

    /**
     * 流控限流阈值类型 QPS 或 THREAD(线程数)
     */
    @Value("${sentinel-rule.flow-grade}")
    private Integer sentinelRuleFlowGrade;

    /**
     * 限流阈值
     */
    @Value("${sentinel-rule.flow-count}")
    private Double sentinelRuleFlowCount;

    /**
     * 熔断策略 支持慢调用比例 SLOW_REQUEST_RATIO:0 /异常比例 ERROR_RATIO:1 /异常数策略 ERROR_COUNT:2
     */
    @Value("${sentinel-rule.degrade-grade}")
    private Integer sentinelRuleDegradeGrade;

    /**
     * 熔断策略阈值
     */
    @Value("${sentinel-rule.degrade-count}")
    private Double sentinelRuleDegradeCount;

    /**
     * 熔断触发的最小请求数，请求数小于该值时即使异常比率超出阈值也不会熔断
     */
    @Value("${sentinel-rule.degrade-min-request-amount}")
    private Integer sentinelRuleDegradeMinRequestAmount;

    /**
     * 熔断时间
     */
    @Value("${sentinel-rule.time-window}")
    private Integer timeWindow;

    /**
     * 统计时长（单位为 ms）
     */
    @Value("${sentinel-rule.degrade-stat-interval-ms}")
    private Integer sentinelRuleDegradeStatIntervalMs;

    @Bean
    public SentinelResourceAspect sentinelResourceAspect() {
        return new SentinelResourceAspect();
    }

    @Bean
    public FlowRule loadFlowRule() {
        //sentinel限流加载
        FlowRule flowRule = new FlowRule();
        flowRule.setResource(sentinelRuleSource);
        flowRule.setCount(sentinelRuleFlowCount);
        flowRule.setGrade(sentinelRuleFlowGrade);
        FlowRuleManager.loadRules(Arrays.asList(flowRule));
        return flowRule;
    }


    @Bean
    public DegradeRule loadDegradeRule() {
        //sentinel熔断规则加载
        DegradeRule degradeRule = new DegradeRule(sentinelRuleSource);
        degradeRule.setGrade(sentinelRuleDegradeGrade);
        degradeRule.setCount(sentinelRuleDegradeCount);
        degradeRule.setMinRequestAmount(sentinelRuleDegradeMinRequestAmount);
        degradeRule.setTimeWindow(timeWindow);
        degradeRule.setStatIntervalMs(sentinelRuleDegradeStatIntervalMs);
        DegradeRuleManager.loadRules(Arrays.asList(degradeRule));
        return degradeRule;
    }

}
