package com.csy.dynamic.loglevel.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.logging.LogLevel;

import static org.springframework.boot.logging.LoggingSystem.ROOT_LOGGER_NAME;

/**
 * @author shuyun.cheng
 */
@ConfigurationProperties(prefix = "dynamic.log")
public class LogConfigProperties {
    /**
     * the name of the logger
     */
    private String logName = ROOT_LOGGER_NAME;
    /**
     * the log level
     *
     * @see LogLevel
     */
    private String level = LogLevel.INFO.name();

    public String getLogName() {
        return logName;
    }

    public void setLogName(String logName) {
        this.logName = logName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}