package com.br.dynamic.loglevelspringbootstarter.config;

import com.br.dynamic.loglevelspringbootstarter.properties.LogConfigProperties;
import com.br.dynamic.loglevelspringbootstarter.service.LoglevelSettingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author shuyun.cheng
 */
@Configuration
@EnableConfigurationProperties(LogConfigProperties.class)
@ConditionalOnProperty(
        prefix = "dynamic.log",
        name = "enable",
        havingValue = "true"
)
public class LogLevelConfiguration {
    @Resource
    private LogConfigProperties logConfigProperties;

    @Value("${spring.application.name}")
    private String appName;

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "spring.application.name")
    public LoglevelSettingService loggerLevelSettingService() {
        if (null != appName && !"".equals(appName.trim())) {
            logConfigProperties.setLogName(appName);
        }
        return new LoglevelSettingService(logConfigProperties);
    }

}