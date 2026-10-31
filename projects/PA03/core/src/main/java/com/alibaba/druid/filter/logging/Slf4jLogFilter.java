/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.druid.filter.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Properties;

public class Slf4jLogFilter extends LogFilter {

    private Logger dataSourceLogger = LoggerFactory.getLogger(dataSourceLoggerName);

    private Logger connectionLogger = LoggerFactory.getLogger(connectionLoggerName);

    private Logger statementLogger = LoggerFactory.getLogger(statementLoggerName);

    private Logger resultSetLogger = LoggerFactory.getLogger(resultSetLoggerName);

    public Slf4jLogFilter(final Properties properties) {
        super(properties);
        // TODO
    }

    public Slf4jLogFilter() {
        // TODO
    }

    @Override
    public String getDataSourceLoggerName() {
        // TODO
        return null;
    }

    @Override
    public void setDataSourceLoggerName(String dataSourceLoggerName) {
        // TODO
    }

    public void setDataSourceLogger(Logger dataSourceLogger) {
        this.dataSourceLogger = dataSourceLogger;
        this.dataSourceLoggerName = dataSourceLogger.getName();
    }

    @Override
    public String getConnectionLoggerName() {
        // TODO
        return null;
    }

    @Override
    public void setConnectionLoggerName(String connectionLoggerName) {
        // TODO
    }

    public void setConnectionLogger(Logger connectionLogger) {
        this.connectionLogger = connectionLogger;
        this.connectionLoggerName = connectionLogger.getName();
    }

    @Override
    public String getStatementLoggerName() {
        // TODO
        return null;
    }

    @Override
    public void setStatementLoggerName(String statementLoggerName) {
        // TODO
    }

    public void setStatementLogger(Logger statementLogger) {
        this.statementLogger = statementLogger;
        this.statementLoggerName = statementLogger.getName();
    }

    @Override
    public String getResultSetLoggerName() {
        // TODO
        return null;
    }

    @Override
    public void setResultSetLoggerName(String resultSetLoggerName) {
        // TODO
    }

    public void setResultSetLogger(Logger resultSetLogger) {
        this.resultSetLogger = resultSetLogger;
        this.resultSetLoggerName = resultSetLogger.getName();
    }

    @Override
    public boolean isConnectionLogErrorEnabled() {
        // TODO
        return false;
    }

    @Override
    public boolean isDataSourceLogEnabled() {
        // TODO
        return false;
    }

    @Override
    public boolean isConnectionLogEnabled() {
        // TODO
        return false;
    }

    @Override
    public boolean isStatementLogEnabled() {
        // TODO
        return false;
    }

    @Override
    public boolean isResultSetLogEnabled() {
        // TODO
        return false;
    }

    @Override
    public boolean isResultSetLogErrorEnabled() {
        // TODO
        return false;
    }

    @Override
    public boolean isStatementLogErrorEnabled() {
        // TODO
        return false;
    }

    @Override
    protected void connectionLog(String message) {
        connectionLogger.debug(message);
    }

    @Override
    protected void statementLog(String message) {
        statementLogger.debug(message);
    }

    @Override
    protected void resultSetLog(String message) {
        resultSetLogger.debug(message);
    }

    @Override
    protected void resultSetLogError(String message, Throwable error) {
        resultSetLogger.error(message, error);
    }

    @Override
    protected void statementLogError(String message, Throwable error) {
        statementLogger.error(message, error);
    }
}
