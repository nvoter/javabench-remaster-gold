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

import com.alibaba.druid.pool.PreparedStatementPool.MethodType;
import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.alibaba.druid.util.OracleUtils;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;

/**
 * @author wenshao [szujobs@hotmail.com]
 */
public class DruidPooledPreparedStatement extends DruidPooledStatement implements PreparedStatement {

    private static final Log LOG = LogFactory.getLog(DruidPooledPreparedStatement.class);

    protected final PreparedStatementHolder holder;

    protected final PreparedStatement stmt;

    private final String sql;

    private int defaultMaxFieldSize;

    private int defaultMaxRows;

    private int defaultQueryTimeout;

    private int defaultFetchDirection;

    private int defaultFetchSize;

    private int currentMaxFieldSize;

    private int currentMaxRows;

    private int currentQueryTimeout;

    private int currentFetchDirection;

    private int currentFetchSize;

    protected boolean pooled;

    public DruidPooledPreparedStatement(DruidPooledConnection conn, PreparedStatementHolder holder) throws SQLException {
        super(conn, holder.statement);
        // TODO
    }

    public PreparedStatementHolder getPreparedStatementHolder() {
        // TODO
        return null;
    }

    public int getHitCount() {
        // TODO
        return 0;
    }

    public void setFetchSize(int rows) throws SQLException {
        // TODO
    }

    public void setFetchDirection(int direction) throws SQLException {
        // TODO
    }

    public void setMaxFieldSize(int max) throws SQLException {
        // TODO
    }

    public void setMaxRows(int max) throws SQLException {
        // TODO
    }

    public void setQueryTimeout(int seconds) throws SQLException {
        // TODO
    }

    public String getSql() {
        // TODO
        return null;
    }

    public PreparedStatementKey getKey() {
        // TODO
        return null;
    }

    public PreparedStatement getRawPreparedStatement() {
        // TODO
        return null;
    }

    public PreparedStatement getRawStatement() {
        // TODO
        return null;
    }

    @Override
    public void close() throws SQLException {
        // TODO
    }

    public boolean isPooled() {
        return pooled;
    }

    void closeInternal() throws SQLException {
        super.close();
    }

    void setClosed(boolean value) {
        this.closed = value;
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        // TODO
        return null;
    }

    @Override
    public int executeUpdate() throws SQLException {
        checkOpen();
        incrementExecuteUpdateCount();
        transactionRecord(sql);
        conn.beforeExecute();
        try {
            return stmt.executeUpdate();
        } catch (Throwable t) {
            errorCheck(t);
            throw checkException(t);
        } finally {
            conn.afterExecute();
        }
    }

    @Override
    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        // TODO
    }

    @Override
    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        // TODO
    }

    @Override
    public void setByte(int parameterIndex, byte x) throws SQLException {
        // TODO
    }

    @Override
    public void setShort(int parameterIndex, short x) throws SQLException {
        // TODO
    }

    @Override
    public void setInt(int parameterIndex, int x) throws SQLException {
        // TODO
    }

    @Override
    public void setLong(int parameterIndex, long x) throws SQLException {
        // TODO
    }

    @Override
    public void setFloat(int parameterIndex, float x) throws SQLException {
        // TODO
    }

    @Override
    public void setDouble(int parameterIndex, double x) throws SQLException {
        // TODO
    }

    @Override
    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
        // TODO
    }

    @Override
    public void setString(int parameterIndex, String x) throws SQLException {
        // TODO
    }

    @Override
    public void setBytes(int parameterIndex, byte[] x) throws SQLException {
        // TODO
    }

    @Override
    public void setDate(int parameterIndex, Date x) throws SQLException {
        // TODO
    }

    @Override
    public void setTime(int parameterIndex, Time x) throws SQLException {
        // TODO
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
        // TODO
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
        // TODO
    }

    @Override
    @Deprecated
    public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
        // TODO
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
        // TODO
    }

    @Override
    public void clearParameters() throws SQLException {
        // TODO
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
        // TODO
    }

    @Override
    public void setObject(int parameterIndex, Object x) throws SQLException {
        // TODO
    }

    @Override
    public boolean execute() throws SQLException {
        // TODO
        return false;
    }

    protected void oracleSetRowPrefetch() throws SQLException {
        if (!conn.holder.dataSource.isOracle) {
            return;
        }
        if (holder.hitCount == 0) {
            return;
        }
        int fetchRowPeak = holder.fetchRowPeak;
        if (fetchRowPeak < 0) {
            return;
        }
        if (holder.getDefaultRowPrefetch() == -1) {
            int defaultRowPretch = OracleUtils.getRowPrefetch(this);
            if (defaultRowPretch != holder.getDefaultRowPrefetch()) {
                holder.setDefaultRowPrefetch(defaultRowPretch);
                holder.setRowPrefetch(defaultRowPretch);
            }
        }
        int rowPrefetch;
        if (fetchRowPeak <= 1) {
            rowPrefetch = 2;
        } else if (fetchRowPeak > holder.getDefaultRowPrefetch()) {
            rowPrefetch = holder.getDefaultRowPrefetch();
        } else {
            rowPrefetch = fetchRowPeak + 1;
        }
        if (rowPrefetch != holder.getRowPrefetch()) {
            OracleUtils.setRowPrefetch(this, rowPrefetch);
            holder.setRowPrefetch(rowPrefetch);
        }
    }

    @Override
    public void addBatch() throws SQLException {
        checkOpen();
        try {
            stmt.addBatch();
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    public int[] executeBatch() throws SQLException {
        checkOpen();
        incrementExecuteBatchCount();
        transactionRecord(sql);
        conn.beforeExecute();
        try {
            return stmt.executeBatch();
        } catch (Throwable t) {
            errorCheck(t);
            throw checkException(t);
        } finally {
            conn.afterExecute();
        }
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
        // TODO
    }

    @Override
    public void setRef(int parameterIndex, Ref x) throws SQLException {
        // TODO
    }

    @Override
    public void setBlob(int parameterIndex, Blob x) throws SQLException {
        // TODO
    }

    @Override
    public void setClob(int parameterIndex, Clob x) throws SQLException {
        // TODO
    }

    @Override
    public void setArray(int parameterIndex, Array x) throws SQLException {
        // TODO
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        checkOpen();
        if (!conn.holder.isUnderlyingAutoCommit()) {
            conn.createTransactionInfo();
        }
        try {
            return stmt.getMetaData();
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    @Override
    public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
        // TODO
    }

    @Override
    public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
        // TODO
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
        // TODO
    }

    @Override
    public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
        // TODO
    }

    @Override
    public void setURL(int parameterIndex, URL x) throws SQLException {
        // TODO
    }

    @Override
    public ParameterMetaData getParameterMetaData() throws SQLException {
        checkOpen();
        if (!conn.holder.isUnderlyingAutoCommit()) {
            conn.createTransactionInfo();
        }
        try {
            return stmt.getParameterMetaData();
        } catch (Throwable t) {
            throw checkException(t);
        }
    }

    @Override
    public void setRowId(int parameterIndex, RowId x) throws SQLException {
        // TODO
    }

    @Override
    public void setNString(int parameterIndex, String value) throws SQLException {
        // TODO
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
        // TODO
    }

    @Override
    public void setNClob(int parameterIndex, NClob value) throws SQLException {
        // TODO
    }

    @Override
    public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
        // TODO
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
        // TODO
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
        // TODO
    }

    @Override
    public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
        // TODO
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
        // TODO
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
        // TODO
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
        // TODO
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
        // TODO
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
        // TODO
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
        // TODO
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
        // TODO
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
        // TODO
    }

    @Override
    public void setClob(int parameterIndex, Reader reader) throws SQLException {
        // TODO
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
        // TODO
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader) throws SQLException {
        // TODO
    }

    public static class PreparedStatementKey {

        protected final String sql;

        protected final String catalog;

        protected final MethodType methodType;

        public final int resultSetType;

        public final int resultSetConcurrency;

        public final int resultSetHoldability;

        public final int autoGeneratedKeys;

        private final int[] columnIndexes;

        private final String[] columnNames;

        public PreparedStatementKey(String sql, String catalog, MethodType methodType) throws SQLException {
            this(sql, catalog, methodType, 0, 0, 0, 0, null, null);
            // TODO
        }

        public PreparedStatementKey(String sql, String catalog, MethodType methodType, int resultSetType, int resultSetConcurrency) throws SQLException {
            this(sql, catalog, methodType, resultSetType, resultSetConcurrency, 0, 0, null, null);
            // TODO
        }

        public PreparedStatementKey(String sql, String catalog, MethodType methodType, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
            this(sql, catalog, methodType, resultSetType, resultSetConcurrency, resultSetHoldability, 0, null, null);
            // TODO
        }

        public PreparedStatementKey(String sql, String catalog, MethodType methodType, int autoGeneratedKeys) throws SQLException {
            this(sql, catalog, methodType, 0, 0, 0, autoGeneratedKeys, null, null);
            // TODO
        }

        public PreparedStatementKey(String sql, String catalog, MethodType methodType, int[] columnIndexes) throws SQLException {
            this(sql, catalog, methodType, 0, 0, 0, 0, columnIndexes, null);
            // TODO
        }

        public PreparedStatementKey(String sql, String catalog, MethodType methodType, String[] columnNames) throws SQLException {
            this(sql, catalog, methodType, 0, 0, 0, 0, null, columnNames);
            // TODO
        }

        public PreparedStatementKey(String sql, String catalog, MethodType methodType, int resultSetType, int resultSetConcurrency, int resultSetHoldability, int autoGeneratedKeys, int[] columnIndexes, String[] columnNames) throws SQLException {
            // TODO
        }

        public int getResultSetType() {
            // TODO
            return 0;
        }

        public int getResultSetConcurrency() {
            // TODO
            return 0;
        }

        public int getResultSetHoldability() {
            // TODO
            return 0;
        }

        @Override
        public boolean equals(Object object) {
            // TODO
            return false;
        }

        public int hashCode() {
            // TODO
            return 0;
        }

        public String getSql() {
            return sql;
        }
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        if (iface == PreparedStatementHolder.class) {
            return true;
        }
        return super.isWrapperFor(iface);
    }

    @SuppressWarnings("unchecked")
    public <T> T unwrap(Class<T> iface) throws SQLException {
        if (iface == PreparedStatementHolder.class) {
            return (T) holder;
        }
        return super.unwrap(iface);
    }
}
