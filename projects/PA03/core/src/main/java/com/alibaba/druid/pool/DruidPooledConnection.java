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

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.FilterChain;
import com.alibaba.druid.filter.FilterChainImpl;
import com.alibaba.druid.proxy.jdbc.TransactionInfo;
import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.alibaba.druid.util.JdbcUtils;
import javax.sql.ConnectionEvent;
import javax.sql.ConnectionEventListener;
import javax.sql.StatementEventListener;
import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author wenshao [szujobs@hotmail.com]
 */
public class DruidPooledConnection extends PoolableWrapper implements javax.sql.PooledConnection, Connection {

    private static final Log LOG = LogFactory.getLog(DruidPooledConnection.class);

    public static final int MAX_RECORD_SQL_COUNT = 10;

    protected Connection conn;

    protected volatile DruidConnectionHolder holder;

    protected TransactionInfo transactionInfo;

    private final boolean dupCloseLogEnable;

    protected volatile boolean traceEnable;

    protected volatile boolean disable;

    protected volatile boolean closed;

    static AtomicIntegerFieldUpdater CLOSING_UPDATER = AtomicIntegerFieldUpdater.newUpdater(DruidPooledConnection.class, "closing");

    protected final Thread ownerThread;

    private long connectedTimeMillis;

    private long connectedTimeNano;

    private volatile boolean running;

    private volatile boolean abandoned;

    protected StackTraceElement[] connectStackTrace;

    protected Throwable disableError;

    final ReentrantLock lock;

    protected volatile int closing;

    protected FilterChain filterChain;

    public DruidPooledConnection(DruidConnectionHolder holder) {
        super(holder.getConnection());
        // TODO
    }

    public long getConnectedTimeMillis() {
        return connectedTimeMillis;
    }

    public Thread getOwnerThread() {
        // TODO
        return null;
    }

    public StackTraceElement[] getConnectStackTrace() {
        return connectStackTrace;
    }

    public void setConnectStackTrace(StackTraceElement[] connectStackTrace) {
        this.connectStackTrace = connectStackTrace;
    }

    public long getConnectedTimeNano() {
        return connectedTimeNano;
    }

    public void setConnectedTimeNano() {
        if (connectedTimeNano <= 0) {
            this.setConnectedTimeNano(System.nanoTime());
        }
    }

    public void setConnectedTimeNano(long connectedTimeNano) {
        this.connectedTimeNano = connectedTimeNano;
    }

    public boolean isTraceEnable() {
        return traceEnable;
    }

    public void setTraceEnable(boolean traceEnable) {
        this.traceEnable = traceEnable;
    }

    public SQLException handleException(Throwable t) throws SQLException {
        // TODO
        return null;
    }

    public SQLException handleException(Throwable t, String sql) throws SQLException {
        // TODO
        return null;
    }

    public boolean isOracle() {
        return holder.getDataSource().isOracle();
    }

    public void closePoolableStatement(DruidPooledPreparedStatement stmt) throws SQLException {
        // TODO
    }

    public DruidConnectionHolder getConnectionHolder() {
        // TODO
        return null;
    }

    @Override
    public Connection getConnection() {
        // TODO
        return null;
    }

    public void disable() {
        // TODO
    }

    public void disable(Throwable error) {
        // TODO
    }

    public boolean isDisable() {
        // TODO
        return false;
    }

    @Override
    public void close() throws SQLException {
        // TODO
    }

    public void syncClose() throws SQLException {
        lock.lock();
        try {
            if (this.disable || CLOSING_UPDATER.get(this) != 0) {
                return;
            }
            DruidConnectionHolder holder = this.holder;
            if (holder == null) {
                if (dupCloseLogEnable) {
                    LOG.error("dup close");
                }
                return;
            }
            if (!CLOSING_UPDATER.compareAndSet(this, 0, 1)) {
                return;
            }
            for (ConnectionEventListener listener : holder.connectionEventListeners) {
                listener.connectionClosed(new ConnectionEvent(this));
            }
            DruidAbstractDataSource dataSource = holder.getDataSource();
            List<Filter> filters = dataSource.getProxyFilters();
            if (filters.size() > 0) {
                FilterChainImpl filterChain = new FilterChainImpl(dataSource);
                filterChain.dataSource_recycle(this);
            } else {
                recycle();
            }
            this.disable = true;
        } finally {
            CLOSING_UPDATER.set(this, 0);
            lock.unlock();
        }
    }

    public void recycle() throws SQLException {
        // TODO
    }

    // ////////////////////
    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        // TODO
        return null;
    }

    private void initStatement(PreparedStatementHolder stmtHolder) throws SQLException {
        stmtHolder.incrementInUseCount();
        holder.getDataSource().initStatement(this, stmtHolder.statement);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        // TODO
        return null;
    }

    // ////////////////////
    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        // TODO
        return null;
    }

    // ////////////////////
    @Override
    public Statement createStatement() throws SQLException {
        // TODO
        return null;
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        // TODO
    }

    protected void transactionRecord(String sql) throws SQLException {
        if (holder != null) {
            holder.setLastExecTimeMillis(System.currentTimeMillis());
        }
        if (transactionInfo == null && (!conn.getAutoCommit())) {
            DruidAbstractDataSource dataSource = holder.getDataSource();
            dataSource.incrementStartTransactionCount();
            transactionInfo = new TransactionInfo(dataSource.createTransactionId());
        }
        if (transactionInfo != null) {
            List<String> sqlList = transactionInfo.getSqlList();
            if (sqlList.size() < MAX_RECORD_SQL_COUNT) {
                sqlList.add(sql);
            }
        }
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        // TODO
        return false;
    }

    @Override
    public void commit() throws SQLException {
        // TODO
    }

    public TransactionInfo getTransactionInfo() {
        // TODO
        return null;
    }

    protected void createTransactionInfo() {
        DruidAbstractDataSource dataSource = holder.getDataSource();
        dataSource.incrementStartTransactionCount();
        transactionInfo = new TransactionInfo(dataSource.createTransactionId());
    }

    @Override
    public void rollback() throws SQLException {
        // TODO
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        // TODO
    }

    private void handleEndTransaction(DruidAbstractDataSource dataSource, Savepoint savepoint) {
        if (transactionInfo != null && savepoint == null) {
            transactionInfo.setEndTimeMillis();
            long transactionMillis = transactionInfo.getEndTimeMillis() - transactionInfo.getStartTimeMillis();
            dataSource.getTransactionHistogram().record(transactionMillis);
            dataSource.logTransaction(transactionInfo);
            transactionInfo = null;
        }
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        // TODO
    }

    @Override
    public Clob createClob() throws SQLException {
        // TODO
        return null;
    }

    @Override
    public boolean isClosed() throws SQLException {
        // TODO
        return false;
    }

    public boolean isAbandonded() {
        // TODO
        return false;
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        // TODO
        return null;
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        // TODO
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        // TODO
        return false;
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        // TODO
    }

    @Override
    public String getCatalog() throws SQLException {
        // TODO
        return null;
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        // TODO
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        // TODO
        return 0;
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        // TODO
        return null;
    }

    @Override
    public void clearWarnings() throws SQLException {
        // TODO
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        // TODO
        return null;
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        // TODO
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        // TODO
    }

    @Override
    public int getHoldability() throws SQLException {
        // TODO
        return 0;
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        // TODO
        return null;
    }

    @Override
    public Blob createBlob() throws SQLException {
        // TODO
        return null;
    }

    @Override
    public NClob createNClob() throws SQLException {
        // TODO
        return null;
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        // TODO
        return null;
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        // TODO
        return false;
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        // TODO
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        // TODO
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        // TODO
        return null;
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public void addConnectionEventListener(ConnectionEventListener listener) {
        // TODO
    }

    @Override
    public void removeConnectionEventListener(ConnectionEventListener listener) {
        // TODO
    }

    @Override
    public void addStatementEventListener(StatementEventListener listener) {
        // TODO
    }

    @Override
    public void removeStatementEventListener(StatementEventListener listener) {
        // TODO
    }

    public Throwable getDisableError() {
        return disableError;
    }

    public void checkState() throws SQLException {
        // TODO
    }

    private void checkStateInternal() throws SQLException {
        if (closed) {
            if (disableError != null) {
                throw new SQLException("connection closed", disableError);
            } else {
                throw new SQLException("connection closed");
            }
        }
        if (disable) {
            if (disableError != null) {
                throw new SQLException("connection disabled", disableError);
            } else {
                throw new SQLException("connection disabled");
            }
        }
        if (holder == null) {
            if (disableError != null) {
                throw new SQLException("connection holder is null", disableError);
            } else {
                throw new SQLException("connection holder is null");
            }
        }
    }

    public String toString() {
        // TODO
        return null;
    }

    public void setSchema(String schema) throws SQLException {
        if (JdbcUtils.isMysqlDbType(holder.dataSource.getDbType())) {
            if (holder.initSchema == null) {
                holder.initSchema = conn.getSchema();
            }
            conn.setSchema(schema);
            if (holder.statementPool != null) {
                holder.clearStatementCache();
            }
            return;
        }
        conn.setSchema(schema);
    }

    public String getSchema() throws SQLException {
        return conn.getSchema();
    }

    public void abort(Executor executor) throws SQLException {
        conn.abort(executor);
    }

    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        conn.setNetworkTimeout(executor, milliseconds);
    }

    public int getNetworkTimeout() throws SQLException {
        return conn.getNetworkTimeout();
    }

    final void beforeExecute() {
        final DruidConnectionHolder holder = this.holder;
        if (holder != null && holder.dataSource.removeAbandoned) {
            running = true;
        }
    }

    final void afterExecute() {
        final DruidConnectionHolder holder = this.holder;
        if (holder != null) {
            DruidAbstractDataSource dataSource = holder.dataSource;
            if (dataSource.removeAbandoned) {
                running = false;
                holder.lastActiveTimeMillis = System.currentTimeMillis();
            }
            dataSource.onFatalError = false;
        }
    }

    boolean isRunning() {
        return running;
    }

    public void abandond() {
        this.abandoned = true;
    }

    /**
     * @since 1.0.17
     */
    public long getPhysicalConnectNanoSpan() {
        // TODO
        return 0L;
    }

    /**
     * @since 1.0.17
     */
    public long getPhysicalConnectionUsedCount() {
        // TODO
        return 0L;
    }

    /**
     * @since 1.0.17
     */
    public long getConnectNotEmptyWaitNanos() {
        // TODO
        return 0L;
    }

    /**
     * @since 1.0.28
     */
    public Map<String, Object> getVariables() {
        // TODO
        return null;
    }

    /**
     * @since 1.0.28
     */
    public Map<String, Object> getGloablVariables() {
        // TODO
        return null;
    }
}
