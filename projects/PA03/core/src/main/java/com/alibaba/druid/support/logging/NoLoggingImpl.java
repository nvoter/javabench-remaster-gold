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

public class NoLoggingImpl implements Log {

    private int infoCount;

    private int errorCount;

    private int warnCount;

    private int debugCount;

    private String loggerName;

    private boolean debugEnable;

    private boolean infoEnable = true;

    private boolean warnEnable = true;

    private boolean errorEnable = true;

    public NoLoggingImpl(String loggerName) {
        // TODO
    }

    public String getLoggerName() {
        return this.loggerName;
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

    public int getErrorCount() {
        // TODO
        return 0;
    }

    @Override
    public int getWarnCount() {
        // TODO
        return 0;
    }

    @Override
    public void resetStat() {
        errorCount = 0;
        warnCount = 0;
        infoCount = 0;
        debugCount = 0;
    }

    @Override
    public boolean isInfoEnabled() {
        // TODO
        return false;
    }

    @Override
    public void info(String s) {
        // TODO
    }

    @Override
    public boolean isWarnEnabled() {
        // TODO
        return false;
    }

    public int getInfoCount() {
        // TODO
        return 0;
    }

    public int getDebugCount() {
        return debugCount;
    }

    public boolean isErrorEnabled() {
        return errorEnable;
    }

    public void setErrorEnabled(boolean value) {
        // TODO
    }
}
