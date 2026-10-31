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
package com.alibaba.druid.stat;

import javax.management.JMException;
import javax.management.openmbean.*;
import java.util.concurrent.atomic.AtomicLong;

public final class JdbcStatManager implements JdbcStatManagerMBean {

    private final AtomicLong sqlIdSeed = new AtomicLong(1000);

    private static final JdbcStatManager instance = new JdbcStatManager();

    private final JdbcConnectionStat connectionStat = new JdbcConnectionStat();

    private final JdbcResultSetStat resultSetStat = new JdbcResultSetStat();

    private final JdbcStatementStat statementStat = new JdbcStatementStat();

    private final AtomicLong resetCount = new AtomicLong();

    public final ThreadLocal<JdbcStatContext> contextLocal = new ThreadLocal<JdbcStatContext>();

    private JdbcStatManager() {
        // TODO
    }

    public JdbcStatContext getStatContext() {
        // TODO
        return null;
    }

    public void setStatContext(JdbcStatContext context) {
        // TODO
    }

    public JdbcStatContext createStatContext() {
        // TODO
        return null;
    }

    public long generateSqlId() {
        return sqlIdSeed.incrementAndGet();
    }

    public static JdbcStatManager getInstance() {
        // TODO
        return null;
    }

    public JdbcStatementStat getStatementStat() {
        return statementStat;
    }

    public JdbcResultSetStat getResultSetStat() {
        return resultSetStat;
    }

    public JdbcConnectionStat getConnectionStat() {
        // TODO
        return null;
    }

    private static CompositeType COMPOSITE_TYPE;

    public static CompositeType getDataSourceCompositeType() throws JMException {
        if (COMPOSITE_TYPE != null) {
            return COMPOSITE_TYPE;
        }
        OpenType<?>[] indexTypes = new OpenType<?>[] { // 0 - 4
        //
        SimpleType.LONG, //
        SimpleType.STRING, //
        SimpleType.STRING, //
        new ArrayType<SimpleType<String>>(SimpleType.STRING, false), //
        SimpleType.DATE, // 5 - 9
        //
        SimpleType.STRING, //
        SimpleType.STRING, //
        SimpleType.INTEGER, //
        SimpleType.INTEGER, //
        SimpleType.STRING, // 10 - 14
        //
        SimpleType.LONG, //
        SimpleType.INTEGER, //
        SimpleType.LONG, //
        SimpleType.LONG, //
        SimpleType.LONG, // 14 - 19
        //
        SimpleType.DATE, //
        SimpleType.LONG, //
        SimpleType.DATE, //
        SimpleType.STRING, //
        SimpleType.STRING, // 20 - 24
        //
        SimpleType.LONG, //
        SimpleType.LONG, //
        SimpleType.LONG, //
        SimpleType.LONG, //
        SimpleType.INTEGER, // 25 - 29
        //
        SimpleType.INTEGER, //
        SimpleType.LONG, //
        SimpleType.LONG, //
        SimpleType.DATE, //
        SimpleType.STRING, // 30 - 34
        //
        SimpleType.STRING, //
        SimpleType.LONG, //
        SimpleType.INTEGER, //
        SimpleType.DATE, //
        SimpleType.LONG, // 35 - 39
        //
        SimpleType.LONG, //
        SimpleType.INTEGER, //
        SimpleType.INTEGER, //
        SimpleType.LONG, SimpleType.DATE, // 40 - 44
        //
        SimpleType.LONG, //
        SimpleType.LONG, //
        SimpleType.DATE, //
        SimpleType.STRING, SimpleType.STRING, // 45 - 49
        //
        SimpleType.LONG, //
        SimpleType.STRING, //
        SimpleType.STRING, //
        SimpleType.LONG, //
        SimpleType.INTEGER, // 50 - 54
        //
        SimpleType.LONG, //
        SimpleType.DATE, //
        SimpleType.LONG, //
        SimpleType.LONG, //
        new ArrayType<Long>(SimpleType.LONG, true), // 55 -
        new ArrayType<Long>(SimpleType.LONG, true) //
        //
        };
        String[] indexNames = { // 0 - 4
        "ID", "URL", "Name", "FilterClasses", //
        "CreatedTime", // 5 - 9
        "RawUrl", "RawDriverClassName", "RawDriverMajorVersion", "RawDriverMinorVersion", "Properties", // 10 - 14
        "ConnectionActiveCount", "ConnectionActiveCountMax", "ConnectionCloseCount", "ConnectionCommitCount", "ConnectionRollbackCount", // 15 - 19
        "ConnectionConnectLastTime", "ConnectionConnectErrorCount", "ConnectionConnectErrorLastTime", "ConnectionConnectErrorLastMessage", "ConnectionConnectErrorLastStackTrace", // 20 - 24
        "StatementCreateCount", "StatementPrepareCount", "StatementPreCallCount", "StatementExecuteCount", "StatementRunningCount", // 25 - 29
        "StatementConcurrentMax", "StatementCloseCount", "StatementErrorCount", "StatementLastErrorTime", "StatementLastErrorMessage", // 30 - 34
        "StatementLastErrorStackTrace", "StatementExecuteMillisTotal", "ConnectionConnectingCount", "StatementExecuteLastTime", "ResultSetCloseCount", // 35 -39
        "ResultSetOpenCount", "ResultSetOpenningCount", "ResultSetOpenningMax", "ResultSetFetchRowCount", "ResultSetLastOpenTime", // 40 - 44
        "ResultSetErrorCount", "ResultSetOpenningMillisTotal", "ResultSetLastErrorTime", "ResultSetLastErrorMessage", "ResultSetLastErrorStackTrace", // 45 - 49
        "ConnectionConnectCount", "ConnectionErrorLastMessage", "ConnectionErrorLastStackTrace", "ConnectionConnectMillisTotal", "ConnectionConnectingCountMax", // 50 - 54
        //
        "ConnectionConnectMillisMax", //
        "ConnectionErrorLastTime", //
        "ConnectionAliveMillisMax", //
        "ConnectionAliveMillisMin", //
        "ConnectionHistogram", // 55 -
        "StatementHistogram" //
        };
        COMPOSITE_TYPE = new CompositeType("DataSourceStatistic", "DataSource Statistic", indexNames, indexNames, indexTypes);
        return COMPOSITE_TYPE;
    }

    @Override
    public TabularData getDataSourceList() throws JMException {
        // TODO
        return null;
    }

    @Override
    public TabularData getSqlList() throws JMException {
        // TODO
        return null;
    }

    public TabularData getConnectionList() throws JMException {
        // TODO
        return null;
    }

    @Override
    public void reset() {
        // TODO
    }

    @Override
    public long getResetCount() {
        return resetCount.get();
    }
}
