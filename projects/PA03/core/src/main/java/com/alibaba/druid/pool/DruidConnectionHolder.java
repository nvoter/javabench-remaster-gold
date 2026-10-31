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
package com.alibaba.druid.pool;

import com.alibaba.druid.filter.FilterChainImpl;
import com.alibaba.druid.pool.DruidAbstractDataSource.PhysicalConnectionInfo;
import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.alibaba.druid.util.JdbcUtils;
import javax.sql.ConnectionEventListener;
import javax.sql.StatementEventListener;
import java.lang.reflect.Field;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author wenshao [szujobs@hotmail.com]
 */
public final class DruidConnectionHolder {

    private static final Log LOG = LogFactory.getLog(DruidConnectionHolder.class);

    static volatile boolean ORACLE_SOCKET_FIELD_ERROR;

    static volatile Field ORACLE_FIELD_NET;

    static volatile Field ORACLE_FIELD_S_ATTS;

    static volatile Field ORACLE_FIELD_NT;

    static volatile Field ORACLE_FIELD_SOCKET;

    public static boolean holdabilityUnsupported;

    protected final DruidAbstractDataSource dataSource;

    protected final long connectionId;

    protected final Connection conn;

    protected final List<ConnectionEventListener> connectionEventListeners = new CopyOnWriteArrayList<ConnectionEventListener>();

    protected final List<StatementEventListener> statementEventListeners = new CopyOnWriteArrayList<StatementEventListener>();

    protected final long connectTimeMillis;

    protected volatile long lastActiveTimeMillis;

    protected volatile long lastExecTimeMillis;

    protected volatile long lastKeepTimeMillis;

    protected volatile long lastValidTimeMillis;

    protected long useCount;

    private long keepAliveCheckCount;

    private long lastNotEmptyWaitNanos;

    private final long createNanoSpan;

    protected PreparedStatementPool statementPool;

    protected final List<Statement> statementTrace = new ArrayList<Statement>(2);

    protected final boolean defaultReadOnly;

    protected final int defaultHoldability;

    protected final int defaultTransactionIsolation;

    protected final boolean defaultAutoCommit;

    protected boolean underlyingReadOnly;

    protected int underlyingHoldability;

    protected int underlyingTransactionIsolation;

    protected boolean underlyingAutoCommit;

    protected volatile boolean discard;

    protected volatile boolean active;

    protected final Map<String, Object> variables;

    protected final Map<String, Object> globalVariables;

    final ReentrantLock lock = new ReentrantLock();

    protected String initSchema;

    protected Socket socket;

    protected final long userPasswordVersion;

    volatile FilterChainImpl filterChain;

    public DruidConnectionHolder(DruidAbstractDataSource dataSource, PhysicalConnectionInfo pyConnectInfo) throws SQLException {
        this(dataSource, pyConnectInfo.getPhysicalConnection(), pyConnectInfo.getConnectNanoSpan(), pyConnectInfo.getVairiables(), pyConnectInfo.getGlobalVairiables());
        // TODO
    }

    public DruidConnectionHolder(DruidAbstractDataSource dataSource, Connection conn, long connectNanoSpan) throws SQLException {
        this(dataSource, conn, connectNanoSpan, null, null);
        // TODO
    }

    public DruidConnectionHolder(DruidAbstractDataSource dataSource, Connection conn, long connectNanoSpan, Map<String, Object> variables, Map<String, Object> globalVariables) throws SQLException {
        // TODO
    }

    protected FilterChainImpl createChain() {
        FilterChainImpl chain = this.filterChain;
        if (chain == null) {
            chain = new FilterChainImpl(dataSource);
        } else {
            this.filterChain = null;
        }
        return chain;
    }

    protected void recycleFilterChain(FilterChainImpl chain) {
        chain.reset();
        this.filterChain = chain;
    }

    public long getConnectTimeMillis() {
        return connectTimeMillis;
    }

    public boolean isUnderlyingReadOnly() {
        return underlyingReadOnly;
    }

    public void setUnderlyingReadOnly(boolean underlyingReadOnly) {
        this.underlyingReadOnly = underlyingReadOnly;
    }

    public int getUnderlyingHoldability() {
        return underlyingHoldability;
    }

    public void setUnderlyingHoldability(int underlyingHoldability) {
        this.underlyingHoldability = underlyingHoldability;
    }

    public int getUnderlyingTransactionIsolation() {
        return underlyingTransactionIsolation;
    }

    public void setUnderlyingTransactionIsolation(int underlyingTransactionIsolation) {
        this.underlyingTransactionIsolation = underlyingTransactionIsolation;
    }

    public boolean isUnderlyingAutoCommit() {
        return underlyingAutoCommit;
    }

    public void setUnderlyingAutoCommit(boolean underlyingAutoCommit) {
        this.underlyingAutoCommit = underlyingAutoCommit;
    }

    public long getLastActiveTimeMillis() {
        // TODO
        return 0L;
    }

    public void setLastActiveTimeMillis(long lastActiveMillis) {
        // TODO
    }

    public long getLastExecTimeMillis() {
        return lastExecTimeMillis;
    }

    public void setLastExecTimeMillis(long lastExecTimeMillis) {
        this.lastExecTimeMillis = lastExecTimeMillis;
    }

    public void addTrace(DruidPooledStatement stmt) {
        lock.lock();
        try {
            statementTrace.add(stmt);
        } finally {
            lock.unlock();
        }
    }

    public void removeTrace(DruidPooledStatement stmt) {
        lock.lock();
        try {
            statementTrace.remove(stmt);
        } finally {
            lock.unlock();
        }
    }

    public List<ConnectionEventListener> getConnectionEventListeners() {
        return connectionEventListeners;
    }

    public List<StatementEventListener> getStatementEventListeners() {
        return statementEventListeners;
    }

    public PreparedStatementPool getStatementPool() {
        // TODO
        return null;
    }

    public PreparedStatementPool getStatementPoolDirect() {
        return statementPool;
    }

    public void clearStatementCache() {
        if (this.statementPool == null) {
            return;
        }
        this.statementPool.clear();
    }

    public DruidAbstractDataSource getDataSource() {
        return dataSource;
    }

    public boolean isPoolPreparedStatements() {
        return dataSource.isPoolPreparedStatements();
    }

    public Connection getConnection() {
        return conn;
    }

    public long getTimeMillis() {
        // TODO
        return 0L;
    }

    public long getUseCount() {
        // TODO
        return 0L;
    }

    public long getConnectionId() {
        return connectionId;
    }

    public void incrementUseCount() {
        useCount++;
    }

    public long getKeepAliveCheckCount() {
        return keepAliveCheckCount;
    }

    public void incrementKeepAliveCheckCount() {
        keepAliveCheckCount++;
    }

    public void reset() throws SQLException {
        // reset default settings
        if (underlyingReadOnly != defaultReadOnly) {
            conn.setReadOnly(defaultReadOnly);
            underlyingReadOnly = defaultReadOnly;
        }
        if (underlyingHoldability != defaultHoldability) {
            conn.setHoldability(defaultHoldability);
            underlyingHoldability = defaultHoldability;
        }
        if (!dataSource.isKeepConnectionUnderlyingTransactionIsolation() && underlyingTransactionIsolation != defaultTransactionIsolation) {
            conn.setTransactionIsolation(defaultTransactionIsolation);
            underlyingTransactionIsolation = defaultTransactionIsolation;
        }
        if (underlyingAutoCommit != defaultAutoCommit) {
            conn.setAutoCommit(defaultAutoCommit);
            underlyingAutoCommit = defaultAutoCommit;
        }
        if (!connectionEventListeners.isEmpty()) {
            connectionEventListeners.clear();
        }
        if (!statementEventListeners.isEmpty()) {
            statementEventListeners.clear();
        }
        lock.lock();
        try {
            if (!statementTrace.isEmpty()) {
                Object[] items = statementTrace.toArray();
                for (int i = 0; i < items.length; i++) {
                    Object item = items[i];
                    Statement stmt = (Statement) item;
                    JdbcUtils.close(stmt);
                }
                statementTrace.clear();
            }
        } finally {
            lock.unlock();
        }
        conn.clearWarnings();
    }

    public boolean isDiscard() {
        return discard;
    }

    public void setDiscard(boolean discard) {
        this.discard = discard;
    }

    public long getCreateNanoSpan() {
        return createNanoSpan;
    }

    public long getLastNotEmptyWaitNanos() {
        return lastNotEmptyWaitNanos;
    }

    protected void setLastNotEmptyWaitNanos(long lastNotEmptyWaitNanos) {
        this.lastNotEmptyWaitNanos = lastNotEmptyWaitNanos;
    }

    public String toString() {
        // TODO
        return null;
    }

    public long getUserPasswordVersion() {
        // TODO
        return 0L;
    }
}
