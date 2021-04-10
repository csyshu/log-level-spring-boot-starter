package com.csy.dynamic.loglevel.service;

import com.csy.dynamic.loglevel.properties.LogConfigProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggerConfiguration;
import org.springframework.boot.logging.LoggingSystem;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.boot.logging.LoggingSystem.ROOT_LOGGER_NAME;

/**
 * 日志级别设置服务类
 *
 * @author shuyun.cheng
 */
public class LoglevelSettingService {

    @Resource
    private LoggingSystem loggingSystem;

    private final String defaultLevel = LogLevel.INFO.name();

    private LogConfigProperties logConfigProperties;

    public LoglevelSettingService() {
    }

    public LoglevelSettingService(LogConfigProperties logConfigProperties) {
        this.logConfigProperties = logConfigProperties;
    }

    @PostConstruct
    public void defaultLoglevel() {
        if (logConfigProperties != null) {
            this.setLogLevelByLogNameAndLevel(logConfigProperties.getLogName(), logConfigProperties.getLevel());
        } else {
            this.setRootLoggerLevel(defaultLevel);
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(LoglevelSettingService.class);

    /**
     * 根据配置依次设置日志级别
     *
     * @param configList 日志配置list
     */
    public void setLoggerLevel(List<LogConfigProperties> configList) {
        Optional.ofNullable(configList).orElse(Collections.emptyList()).forEach(
                config -> this.setLogLevelByLogNameAndLevel(config.getLogName(), config.getLevel())
        );
    }

    /**
     * 设置root的日志级别
     *
     * @param level 日志级别
     */
    public void setRootLoggerLevel(String level) {
        this.setLogLevelByLogNameAndLevel(ROOT_LOGGER_NAME, level);
    }

    /**
     * 设置日志服务日志级别
     *
     * @param level 日志级别
     */
    public void setLogServerLoggerLevel(String level) {
        this.setLogLevelByLogNameAndLevel(this.getClass().getName(), level);
    }

    private void setLogLevelByLogNameAndLevel(String logName, String logLevel) {
        LoggerConfiguration loggerConfiguration = loggingSystem.getLoggerConfiguration(logName);

        if (loggerConfiguration == null) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn("no loggerConfiguration with loggerName:{},logLevel:{}", logName, logLevel);
            }
            return;
        }

        if (!supportLevels().contains(logLevel)) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("current Level is not support : " + logLevel);
            }
            return;
        }

        if (!loggerConfiguration.getEffectiveLevel().equals(LogLevel.valueOf(logLevel))) {
            loggingSystem.setLogLevel(logName, LogLevel.valueOf(logLevel));
        }
    }

    private List<String> supportLevels() {
        return loggingSystem.getSupportedLogLevels().stream().map(Enum::name).collect(Collectors.toList());
    }
}