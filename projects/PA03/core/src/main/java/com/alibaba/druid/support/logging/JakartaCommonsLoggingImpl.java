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

import org.apache.commons.logging.Log;

public class JakartaCommonsLoggingImpl implements com.alibaba.druid.support.logging.Log {

    private Log log;

    private int errorCount;

    private int warnCount;

    private int infoCount;

    private int debugCount;

    /**
     * @param log the Log instance to be used for logging
     * @since 0.2.1
     */
    public JakartaCommonsLoggingImpl(Log log) {
        // TODO
    }

    public JakartaCommonsLoggingImpl(String loggerName) {
        // TODO
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

    @Override
    public void warn(String s, Throwable e) {
        // TODO
    }

    @Override
    public int getWarnCount() {
        // TODO
        return 0;
    }

    public int getErrorCount() {
        // TODO
        return 0;
    }

    @Override
    public void resetStat() {
        errorCount = 0;
        warnCount = 0;
        infoCount = 0;
        debugCount++;
    }

    @Override
    public boolean isInfoEnabled() {
        // TODO
        return false;
    }

    @Override
    public void info(String msg) {
        // TODO
    }

    @Override
    public int getInfoCount() {
        // TODO
        return 0;
    }

    @Override
    public boolean isWarnEnabled() {
        // TODO
        return false;
    }

    public int getDebugCount() {
        return debugCount;
    }

    @Override
    public boolean isErrorEnabled() {
        return log.isErrorEnabled();
    }
}
