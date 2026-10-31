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

import com.alibaba.druid.proxy.jdbc.CallableStatementProxy;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Calendar;

/**
 * @author wenshao [szujobs@hotmail.com]
 */
public class DruidPooledCallableStatement extends DruidPooledPreparedStatement implements CallableStatement {

    private CallableStatement stmt;

    public DruidPooledCallableStatement(DruidPooledConnection conn, PreparedStatementHolder holder) throws SQLException {
        super(conn, holder);
        // TODO
    }

    public CallableStatement getCallableStatementRaw() {
        // TODO
        return null;
    }

    @Override
    public void registerOutParameter(int parameterIndex, int sqlType) throws SQLException {
        // TODO
    }

    @Override
    public void registerOutParameter(int parameterIndex, int sqlType, int scale) throws SQLException {
        // TODO
    }

    @Override
    public boolean wasNull() throws SQLException {
        // TODO
        return false;
    }

    @Override
    public String getString(int parameterIndex) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public boolean getBoolean(int parameterIndex) throws SQLException {
        // TODO
        return false;
    }

    @Override
    public byte getByte(int parameterIndex) throws SQLException {
        // TODO
        return 0;
    }

    @Override
    public short getShort(int parameterIndex) throws SQLException {
        // TODO
        return 0;
    }

    @Override
    public int getInt(int parameterIndex) throws SQLException {
        // TODO
        return 0;
    }

    @Override
    public long getLong(int parameterIndex) throws SQLException {
        // TODO
        return 0L;
    }

    @Override
    public float getFloat(int parameterIndex) throws SQLException {
        // TODO
        return 0.0f;
    }

    @Override
    public double getDouble(int parameterIndex) throws SQLException {
        // TODO
        return 0.0;
    }

    @Override
    @Deprecated
    public BigDecimal getBigDecimal(int parameterIndex, int scale) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public byte[] getBytes(int parameterIndex) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public java.sql.Date getDate(int parameterIndex) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public java.sql.Time getTime(int parameterIndex) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public java.sql.Timestamp getTimestamp(int parameterIndex) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public Object getObject(int parameterIndex) throws SQLException {
        // TODO
        return null;
    }

    private Object wrapObject(Object obj) {
        if (obj instanceof ResultSet) {
            ResultSet rs = (ResultSet) obj;
            DruidPooledResultSet poolableResultSet = new DruidPooledResultSet(this, rs);
            addResultSetTrace(poolableResultSet);
            obj = poolableResultSet;
        }
        return obj;
    }

    @Override
    public BigDecimal getBigDecimal(int parameterIndex) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public Object getObject(int parameterIndex, java.util.Map<String, Class<?>> map) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public Ref getRef(int parameterIndex) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public Blob getBlob(int parameterIndex) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public Clob getClob(int parameterIndex) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public Array getArray(int parameterIndex) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public java.sql.Date getDate(int parameterIndex, Calendar cal) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public java.sql.Time getTime(int parameterIndex, Calendar cal) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public java.sql.Timestamp getTimestamp(int parameterIndex, Calendar cal) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public void registerOutParameter(int parameterIndex, int sqlType, String typeName) throws SQLException {
        // TODO
    }

    @Override
    public void registerOutParameter(String parameterName, int sqlType) throws SQLException {
        // TODO
    }

    @Override
    public void registerOutParameter(String parameterName, int sqlType, int scale) throws SQLException {
        // TODO
    }

    @Override
    public void registerOutParameter(String parameterName, int sqlType, String typeName) throws SQLException {
        // TODO
    }

    @Override
    public java.net.URL getURL(int parameterIndex) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public void setURL(String parameterName, java.net.URL val) throws SQLException {
        // TODO
    }

    @Override
    public void setNull(String parameterName, int sqlType) throws SQLException {
        // TODO
    }

    @Override
    public void setBoolean(String parameterName, boolean x) throws SQLException {
        // TODO
    }

    @Override
    public void setByte(String parameterName, byte x) throws SQLException {
        // TODO
    }

    @Override
    public void setShort(String parameterName, short x) throws SQLException {
        // TODO
    }

    @Override
    public void setInt(String parameterName, int x) throws SQLException {
        // TODO
    }

    @Override
    public void setLong(String parameterName, long x) throws SQLException {
        // TODO
    }

    @Override
    public void setFloat(String parameterName, float x) throws SQLException {
        // TODO
    }

    @Override
    public void setDouble(String parameterName, double x) throws SQLException {
        // TODO
    }

    @Override
    public void setBigDecimal(String parameterName, BigDecimal x) throws SQLException {
        // TODO
    }

    @Override
    public void setString(String parameterName, String x) throws SQLException {
        // TODO
    }

    @Override
    public void setBytes(String parameterName, byte[] x) throws SQLException {
        // TODO
    }

    @Override
    public void setDate(String parameterName, java.sql.Date x) throws SQLException {
        // TODO
    }

    @Override
    public void setTime(String parameterName, java.sql.Time x) throws SQLException {
        // TODO
    }

    @Override
    public void setTimestamp(String parameterName, java.sql.Timestamp x) throws SQLException {
        // TODO
    }

    @Override
    public void setAsciiStream(String parameterName, java.io.InputStream x, int length) throws SQLException {
        // TODO
    }

    @Override
    public void setBinaryStream(String parameterName, java.io.InputStream x, int length) throws SQLException {
        // TODO
    }

    @Override
    public void setObject(String parameterName, Object x, int targetSqlType, int scale) throws SQLException {
        // TODO
    }

    @Override
    public void setObject(String parameterName, Object x, int targetSqlType) throws SQLException {
        // TODO
    }

    @Override
    public void setObject(String parameterName, Object x) throws SQLException {
        // TODO
    }

    @Override
    public void setCharacterStream(String parameterName, java.io.Reader reader, int length) throws SQLException {
        // TODO
    }

    @Override
    public void setDate(String parameterName, java.sql.Date x, Calendar cal) throws SQLException {
        // TODO
    }

    @Override
    public void setTime(String parameterName, java.sql.Time x, Calendar cal) throws SQLException {
        // TODO
    }

    @Override
    public void setTimestamp(String parameterName, java.sql.Timestamp x, Calendar cal) throws SQLException {
        // TODO
    }

    @Override
    public void setNull(String parameterName, int sqlType, String typeName) throws SQLException {
        // TODO
    }

    @Override
    public String getString(String parameterName) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public boolean getBoolean(String parameterName) throws SQLException {
        // TODO
        return false;
    }

    @Override
    public byte getByte(String parameterName) throws SQLException {
        // TODO
        return 0;
    }

    @Override
    public short getShort(String parameterName) throws SQLException {
        // TODO
        return 0;
    }

    @Override
    public int getInt(String parameterName) throws SQLException {
        // TODO
        return 0;
    }

    @Override
    public long getLong(String parameterName) throws SQLException {
        // TODO
        return 0L;
    }

    @Override
    public float getFloat(String parameterName) throws SQLException {
        // TODO
        return 0.0f;
    }

    @Override
    public double getDouble(String parameterName) throws SQLException {
        // TODO
        return 0.0;
    }

    @Override
    public byte[] getBytes(String parameterName) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public java.sql.Date getDate(String parameterName) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public java.sql.Time getTime(String parameterName) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public java.sql.Timestamp getTimestamp(String parameterName) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public Object getObject(String parameterName) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public BigDecimal getBigDecimal(String parameterName) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public Object getObject(String parameterName, java.util.Map<String, Class<?>> map) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public Ref getRef(String parameterName) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public Blob getBlob(String parameterName) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public Clob getClob(String parameterName) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public Array getArray(String parameterName) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public java.sql.Date getDate(String parameterName, Calendar cal) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public java.sql.Time getTime(String parameterName, Calendar cal) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public java.sql.Timestamp getTimestamp(String parameterName, Calendar cal) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public java.net.URL getURL(String parameterName) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public RowId getRowId(int parameterIndex) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public RowId getRowId(String parameterName) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public void setRowId(String parameterName, RowId x) throws SQLException {
        // TODO
    }

    @Override
    public void setNString(String parameterName, String value) throws SQLException {
        // TODO
    }

    @Override
    public void setNCharacterStream(String parameterName, Reader value, long length) throws SQLException {
        // TODO
    }

    @Override
    public void setNClob(String parameterName, NClob value) throws SQLException {
        // TODO
    }

    @Override
    public void setClob(String parameterName, Reader reader, long length) throws SQLException {
        // TODO
    }

    @Override
    public void setBlob(String parameterName, InputStream inputStream, long length) throws SQLException {
        // TODO
    }

    @Override
    public void setNClob(String parameterName, Reader reader, long length) throws SQLException {
        // TODO
    }

    @Override
    public NClob getNClob(int parameterIndex) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public NClob getNClob(String parameterName) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public void setSQLXML(String parameterName, SQLXML xmlObject) throws SQLException {
        // TODO
    }

    @Override
    public SQLXML getSQLXML(int parameterIndex) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public SQLXML getSQLXML(String parameterName) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public String getNString(int parameterIndex) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public String getNString(String parameterName) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public java.io.Reader getNCharacterStream(int parameterIndex) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public java.io.Reader getNCharacterStream(String parameterName) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public java.io.Reader getCharacterStream(int parameterIndex) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public java.io.Reader getCharacterStream(String parameterName) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public void setBlob(String parameterName, Blob x) throws SQLException {
        // TODO
    }

    @Override
    public void setClob(String parameterName, Clob x) throws SQLException {
        // TODO
    }

    @Override
    public void setAsciiStream(String parameterName, java.io.InputStream x, long length) throws SQLException {
        // TODO
    }

    @Override
    public void setBinaryStream(String parameterName, java.io.InputStream x, long length) throws SQLException {
        // TODO
    }

    @Override
    public void setCharacterStream(String parameterName, java.io.Reader reader, long length) throws SQLException {
        // TODO
    }

    @Override
    public void setAsciiStream(String parameterName, java.io.InputStream x) throws SQLException {
        // TODO
    }

    @Override
    public void setBinaryStream(String parameterName, java.io.InputStream x) throws SQLException {
        // TODO
    }

    @Override
    public void setCharacterStream(String parameterName, java.io.Reader reader) throws SQLException {
        // TODO
    }

    @Override
    public void setNCharacterStream(String parameterName, Reader value) throws SQLException {
        // TODO
    }

    @Override
    public void setClob(String parameterName, Reader reader) throws SQLException {
        // TODO
    }

    @Override
    public void setBlob(String parameterName, InputStream inputStream) throws SQLException {
        // TODO
    }

    @Override
    public void setNClob(String parameterName, Reader reader) throws SQLException {
        // TODO
    }

    public <T> T getObject(int parameterIndex, Class<T> type) throws SQLException {
        // TODO
        return null;
    }

    public <T> T getObject(String parameterName, Class<T> type) throws SQLException {
        // TODO
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T> T unwrap(Class<T> iface) throws SQLException {
        if (iface == CallableStatement.class || iface == PreparedStatement.class) {
            if (stmt instanceof CallableStatementProxy) {
                return stmt.unwrap(iface);
            }
            return (T) stmt;
        }
        return super.unwrap(iface);
    }
}
