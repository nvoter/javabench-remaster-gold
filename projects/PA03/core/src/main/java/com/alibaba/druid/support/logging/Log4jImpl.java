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
package com.alibaba.druid.support.logging;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class Log4jImpl implements Log {

    private static final String callerFQCN = Log4jImpl.class.getName();

    private Logger log;

    private int errorCount;

    private int warnCount;

    private int infoCount;

    private int debugCount;

    /**
     * @param log the Log instance to be used for logging
     * @since 0.2.21
     */
    public Log4jImpl(Logger log) {
        this.log = log;
    }

    public Log4jImpl(String loggerName) {
        // TODO
    }

    public Logger getLog() {
        // TODO
        return null;
    }

    public boolean isDebugEnabled() {
        // TODO
        return false;
    }

    public void error(String s, Throwable e) {
        // TODO
    }

    public void error(String s) {
        // TODO
    }

    public void debug(String s) {
        // TODO
    }

    public void debug(String s, Throwable e) {
        // TODO
    }

    public void warn(String s) {
        // TODO
    }

    public void warn(String s, Throwable e) {
        // TODO
    }

    public int getWarnCount() {
        // TODO
        return 0;
    }

    public int getErrorCount() {
        // TODO
        return 0;
    }

    public void resetStat() {
        errorCount = 0;
        warnCount = 0;
        infoCount = 0;
        debugCount = 0;
    }

    public int getDebugCount() {
        return debugCount;
    }

    public boolean isInfoEnabled() {
        // TODO
        return false;
    }

    public void info(String msg) {
        // TODO
    }

    public boolean isWarnEnabled() {
        // TODO
        return false;
    }

    public boolean isErrorEnabled() {
        return log.isEnabledFor(Level.ERROR);
    }

    public int getInfoCount() {
        // TODO
        return 0;
    }

    public String toString() {
        return log.toString();
    }
}
