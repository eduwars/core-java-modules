package com.eduwars.corejava.common.logger;

import java.util.HashMap;

/**
 * @author Ravi Singh
 * @date 03/06/20-06-2020
 * @project eduwars
 */
public class LoggerContext {

    private HashMap<String, String> logMap;

    private LoggerContext() {
        this.logMap = new HashMap<>();
    }

    public static LoggerContext builder() {
        return new LoggerContext();
    }

    public LoggerContext add(LoggerConstants.KEYS key, String  value) {
        this.logMap.put(key.value(), value);
        return this;
    }

    public HashMap<String, String> build() {
        return logMap;
    }
}