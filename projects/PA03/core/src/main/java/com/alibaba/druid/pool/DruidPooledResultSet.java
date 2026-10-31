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

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Calendar;

/**
 * @author wenshao [szujobs@hotmail.com]
 */
public final class DruidPooledResultSet extends PoolableWrapper implements ResultSet {

    private final ResultSet rs;

    private final DruidPooledStatement stmt;

    protected boolean closed;

    protected int cursorIndex;

    protected int fetchRowCount;

    public DruidPooledResultSet(DruidPooledStatement stmt, ResultSet rs) {
        super(rs);
        // TODO
    }

    protected SQLException checkException(Throwable error) throws SQLException {
        return stmt.checkException(error);
    }

    public DruidPooledStatement getPoolableStatement() {
        // TODO
        return null;
    }

    public ResultSet getRawResultSet() {
        // TODO
        return null;
    }

    @Override
    public boolean next() throws SQLException {
        // TODO
        return false;
    }

    @Override
    public void close() throws SQLException {
        // TODO
    }

    public int getFetchRowCount() {
        // TODO
        return 0;
    }

    @Override
    public boolean wasNull() throws SQLException {
        // TODO
        return false;
    }

    @Override
    public String getString(int columnIndex) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public boolean getBoolean(int columnIndex) throws SQLException {
        // TODO
        return false;
    }

    @Override
    public byte getByte(int columnIndex) throws SQLException {
        // TODO
        return 0;
    }

    @Override
    public short getShort(int columnIndex) throws SQLException {
        // TODO
        return 0;
    }

    @Override
    public int getInt(int columnIndex) throws SQLException {
        // TODO
        return 0;
    }

    @Override
    public long getLong(int columnIndex) throws SQLException {
        // TODO
        return 0L;
    }

    @Override
    public float getFloat(int columnIndex) throws SQLException {
        // TODO
        return 0.0f;
    }

    @Override
    public double getDouble(int columnIndex) throws SQLException {
        // TODO
        return 0.0;
    }

    @Override
    @Deprecated
    public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public byte[] getBytes(int columnIndex) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public java.sql.Date getDate(int columnIndex) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public java.sql.Time getTime(int columnIndex) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public java.sql.Timestamp getTimestamp(int columnIndex) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public java.io.InputStream getAsciiStream(int columnIndex) throws SQLException {
        // TODO
        return null;
    }

    @Override
    @Deprecated
    public java.io.InputStream getUnicodeStream(int columnIndex) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public java.io.InputStream getBinaryStream(int columnIndex) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public String getString(String columnLabel) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public boolean getBoolean(String columnLabel) throws SQLException {
        // TODO
        return false;
    }

    @Override
    public byte getByte(String columnLabel) throws SQLException {
        // TODO
        return 0;
    }

    @Override
    public short getShort(String columnLabel) throws SQLException {
        // TODO
        return 0;
    }

    @Override
    public int getInt(String columnLabel) throws SQLException {
        // TODO
        return 0;
    }

    @Override
    public long getLong(String columnLabel) throws SQLException {
        // TODO
        return 0L;
    }

    @Override
    public float getFloat(String columnLabel) throws SQLException {
        // TODO
        return 0.0f;
    }

    @Override
    public double getDouble(String columnLabel) throws SQLException {
        // TODO
        return 0.0;
    }

    @Override
    @Deprecated
    public BigDecimal getBigDecimal(String columnLabel, int scale) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public byte[] getBytes(String columnLabel) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public java.sql.Date getDate(String columnLabel) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public java.sql.Time getTime(String columnLabel) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public java.sql.Timestamp getTimestamp(String columnLabel) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public java.io.InputStream getAsciiStream(String columnLabel) throws SQLException {
        // TODO
        return null;
    }

    @Override
    @Deprecated
    public java.io.InputStream getUnicodeStream(String columnLabel) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public java.io.InputStream getBinaryStream(String columnLabel) throws SQLException {
        // TODO
        return null;
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
    public String getCursorName() throws SQLException {
        // TODO
        return null;
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        // TODO
        return null;
    }

    @Override
    public Object getObject(int columnIndex) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public Object getObject(String columnLabel) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public int findColumn(String columnLabel) throws SQLException {
        // TODO
        return 0;
    }

    @Override
    public java.io.Reader getCharacterStream(int columnIndex) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public java.io.Reader getCharacterStream(String columnLabel) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public boolean isBeforeFirst() throws SQLException {
        // TODO
        return false;
    }

    @Override
    public boolean isAfterLast() throws SQLException {
        // TODO
        return false;
    }

    @Override
    public boolean isFirst() throws SQLException {
        // TODO
        return false;
    }

    @Override
    public boolean isLast() throws SQLException {
        // TODO
        return false;
    }

    @Override
    public void beforeFirst() throws SQLException {
        // TODO
    }

    @Override
    public void afterLast() throws SQLException {
        // TODO
    }

    @Override
    public boolean first() throws SQLException {
        // TODO
        return false;
    }

    @Override
    public boolean last() throws SQLException {
        // TODO
        return false;
    }

    @Override
    public int getRow() throws SQLException {
        // TODO
        return 0;
    }

    @Override
    public boolean absolute(int row) throws SQLException {
        // TODO
        return false;
    }

    @Override
    public boolean relative(int rows) throws SQLException {
        // TODO
        return false;
    }

    @Override
    public boolean previous() throws SQLException {
        // TODO
        return false;
    }

    @Override
    public void setFetchDirection(int direction) throws SQLException {
        // TODO
    }

    @Override
    public int getFetchDirection() throws SQLException {
        // TODO
        return 0;
    }

    @Override
    public void setFetchSize(int rows) throws SQLException {
        // TODO
    }

    @Override
    public int getFetchSize() throws SQLException {
        // TODO
        return 0;
    }

    @Override
    public int getType() throws SQLException {
        // TODO
        return 0;
    }

    @Override
    public int getConcurrency() throws SQLException {
        // TODO
        return 0;
    }

    @Override
    public boolean rowUpdated() throws SQLException {
        // TODO
        return false;
    }

    @Override
    public boolean rowInserted() throws SQLException {
        // TODO
        return false;
    }

    @Override
    public boolean rowDeleted() throws SQLException {
        // TODO
        return false;
    }

    @Override
    public void updateNull(int columnIndex) throws SQLException {
        // TODO
    }

    @Override
    public void updateBoolean(int columnIndex, boolean x) throws SQLException {
        // TODO
    }

    @Override
    public void updateByte(int columnIndex, byte x) throws SQLException {
        // TODO
    }

    @Override
    public void updateShort(int columnIndex, short x) throws SQLException {
        // TODO
    }

    @Override
    public void updateInt(int columnIndex, int x) throws SQLException {
        // TODO
    }

    @Override
    public void updateLong(int columnIndex, long x) throws SQLException {
        // TODO
    }

    @Override
    public void updateFloat(int columnIndex, float x) throws SQLException {
        // TODO
    }

    @Override
    public void updateDouble(int columnIndex, double x) throws SQLException {
        // TODO
    }

    @Override
    public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
        // TODO
    }

    @Override
    public void updateString(int columnIndex, String x) throws SQLException {
        // TODO
    }

    @Override
    public void updateBytes(int columnIndex, byte[] x) throws SQLException {
        // TODO
    }

    @Override
    public void updateDate(int columnIndex, java.sql.Date x) throws SQLException {
        // TODO
    }

    @Override
    public void updateTime(int columnIndex, java.sql.Time x) throws SQLException {
        // TODO
    }

    @Override
    public void updateTimestamp(int columnIndex, java.sql.Timestamp x) throws SQLException {
        // TODO
    }

    @Override
    public void updateAsciiStream(int columnIndex, java.io.InputStream x, int length) throws SQLException {
        // TODO
    }

    @Override
    public void updateBinaryStream(int columnIndex, java.io.InputStream x, int length) throws SQLException {
        // TODO
    }

    @Override
    public void updateCharacterStream(int columnIndex, java.io.Reader x, int length) throws SQLException {
        // TODO
    }

    @Override
    public void updateObject(int columnIndex, Object x, int scaleOrLength) throws SQLException {
        // TODO
    }

    @Override
    public void updateObject(int columnIndex, Object x) throws SQLException {
        // TODO
    }

    @Override
    public void updateNull(String columnLabel) throws SQLException {
        // TODO
    }

    @Override
    public void updateBoolean(String columnLabel, boolean x) throws SQLException {
        // TODO
    }

    @Override
    public void updateByte(String columnLabel, byte x) throws SQLException {
        // TODO
    }

    @Override
    public void updateShort(String columnLabel, short x) throws SQLException {
        // TODO
    }

    @Override
    public void updateInt(String columnLabel, int x) throws SQLException {
        // TODO
    }

    @Override
    public void updateLong(String columnLabel, long x) throws SQLException {
        // TODO
    }

    @Override
    public void updateFloat(String columnLabel, float x) throws SQLException {
        // TODO
    }

    @Override
    public void updateDouble(String columnLabel, double x) throws SQLException {
        // TODO
    }

    @Override
    public void updateBigDecimal(String columnLabel, BigDecimal x) throws SQLException {
        // TODO
    }

    @Override
    public void updateString(String columnLabel, String x) throws SQLException {
        // TODO
    }

    @Override
    public void updateBytes(String columnLabel, byte[] x) throws SQLException {
        // TODO
    }

    @Override
    public void updateDate(String columnLabel, java.sql.Date x) throws SQLException {
        // TODO
    }

    @Override
    public void updateTime(String columnLabel, java.sql.Time x) throws SQLException {
        // TODO
    }

    @Override
    public void updateTimestamp(String columnLabel, java.sql.Timestamp x) throws SQLException {
        // TODO
    }

    @Override
    public void updateAsciiStream(String columnLabel, java.io.InputStream x, int length) throws SQLException {
        // TODO
    }

    @Override
    public void updateBinaryStream(String columnLabel, java.io.InputStream x, int length) throws SQLException {
        // TODO
    }

    @Override
    public void updateCharacterStream(String columnLabel, java.io.Reader reader, int length) throws SQLException {
        // TODO
    }

    @Override
    public void updateObject(String columnLabel, Object x, int scaleOrLength) throws SQLException {
        // TODO
    }

    @Override
    public void updateObject(String columnLabel, Object x) throws SQLException {
        // TODO
    }

    @Override
    public void insertRow() throws SQLException {
        // TODO
    }

    @Override
    public void updateRow() throws SQLException {
        // TODO
    }

    @Override
    public void deleteRow() throws SQLException {
        // TODO
    }

    @Override
    public void refreshRow() throws SQLException {
        // TODO
    }

    @Override
    public void cancelRowUpdates() throws SQLException {
        // TODO
    }

    @Override
    public void moveToInsertRow() throws SQLException {
        // TODO
    }

    @Override
    public void moveToCurrentRow() throws SQLException {
        // TODO
    }

    @Override
    public Statement getStatement() {
        // TODO
        return null;
    }

    @Override
    public Object getObject(int columnIndex, java.util.Map<String, Class<?>> map) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public Ref getRef(int columnIndex) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public Blob getBlob(int columnIndex) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public Clob getClob(int columnIndex) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public Array getArray(int columnIndex) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public Object getObject(String columnLabel, java.util.Map<String, Class<?>> map) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public Ref getRef(String columnLabel) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public Blob getBlob(String columnLabel) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public Clob getClob(String columnLabel) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public Array getArray(String columnLabel) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public java.sql.Date getDate(int columnIndex, Calendar cal) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public java.sql.Date getDate(String columnLabel, Calendar cal) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public java.sql.Time getTime(int columnIndex, Calendar cal) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public java.sql.Time getTime(String columnLabel, Calendar cal) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public java.sql.Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public java.sql.Timestamp getTimestamp(String columnLabel, Calendar cal) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public java.net.URL getURL(int columnIndex) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public java.net.URL getURL(String columnLabel) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public void updateRef(int columnIndex, java.sql.Ref x) throws SQLException {
        // TODO
    }

    @Override
    public void updateRef(String columnLabel, java.sql.Ref x) throws SQLException {
        // TODO
    }

    @Override
    public void updateBlob(int columnIndex, java.sql.Blob x) throws SQLException {
        // TODO
    }

    @Override
    public void updateBlob(String columnLabel, java.sql.Blob x) throws SQLException {
        // TODO
    }

    @Override
    public void updateClob(int columnIndex, java.sql.Clob x) throws SQLException {
        // TODO
    }

    @Override
    public void updateClob(String columnLabel, java.sql.Clob x) throws SQLException {
        // TODO
    }

    @Override
    public void updateArray(int columnIndex, java.sql.Array x) throws SQLException {
        // TODO
    }

    @Override
    public void updateArray(String columnLabel, java.sql.Array x) throws SQLException {
        // TODO
    }

    @Override
    public RowId getRowId(int columnIndex) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public RowId getRowId(String columnLabel) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public void updateRowId(int columnIndex, RowId x) throws SQLException {
        // TODO
    }

    @Override
    public void updateRowId(String columnLabel, RowId x) throws SQLException {
        // TODO
    }

    @Override
    public int getHoldability() throws SQLException {
        // TODO
        return 0;
    }

    @Override
    public boolean isClosed() throws SQLException {
        return this.closed;
    }

    @Override
    public void updateNString(int columnIndex, String nString) throws SQLException {
        // TODO
    }

    @Override
    public void updateNString(String columnLabel, String nString) throws SQLException {
        // TODO
    }

    @Override
    public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
        // TODO
    }

    @Override
    public void updateNClob(String columnLabel, NClob nClob) throws SQLException {
        // TODO
    }

    @Override
    public NClob getNClob(int columnIndex) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public NClob getNClob(String columnLabel) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public SQLXML getSQLXML(int columnIndex) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public SQLXML getSQLXML(String columnLabel) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
        // TODO
    }

    @Override
    public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {
        // TODO
    }

    @Override
    public String getNString(int columnIndex) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public String getNString(String columnLabel) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public java.io.Reader getNCharacterStream(int columnIndex) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public java.io.Reader getNCharacterStream(String columnLabel) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public void updateNCharacterStream(int columnIndex, java.io.Reader x, long length) throws SQLException {
        // TODO
    }

    @Override
    public void updateNCharacterStream(String columnLabel, java.io.Reader reader, long length) throws SQLException {
        // TODO
    }

    @Override
    public void updateAsciiStream(int columnIndex, java.io.InputStream x, long length) throws SQLException {
        // TODO
    }

    @Override
    public void updateBinaryStream(int columnIndex, java.io.InputStream x, long length) throws SQLException {
        // TODO
    }

    @Override
    public void updateCharacterStream(int columnIndex, java.io.Reader x, long length) throws SQLException {
        // TODO
    }

    @Override
    public void updateAsciiStream(String columnLabel, java.io.InputStream x, long length) throws SQLException {
        // TODO
    }

    @Override
    public void updateBinaryStream(String columnLabel, java.io.InputStream x, long length) throws SQLException {
        // TODO
    }

    @Override
    public void updateCharacterStream(String columnLabel, java.io.Reader reader, long length) throws SQLException {
        // TODO
    }

    @Override
    public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {
        // TODO
    }

    @Override
    public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException {
        // TODO
    }

    @Override
    public void updateClob(int columnIndex, Reader reader, long length) throws SQLException {
        // TODO
    }

    @Override
    public void updateClob(String columnLabel, Reader reader, long length) throws SQLException {
        // TODO
    }

    @Override
    public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {
        // TODO
    }

    @Override
    public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {
        // TODO
    }

    @Override
    public void updateNCharacterStream(int columnIndex, java.io.Reader x) throws SQLException {
        // TODO
    }

    @Override
    public void updateNCharacterStream(String columnLabel, java.io.Reader reader) throws SQLException {
        // TODO
    }

    @Override
    public void updateAsciiStream(int columnIndex, java.io.InputStream x) throws SQLException {
        // TODO
    }

    @Override
    public void updateBinaryStream(int columnIndex, java.io.InputStream x) throws SQLException {
        // TODO
    }

    @Override
    public void updateCharacterStream(int columnIndex, java.io.Reader x) throws SQLException {
        // TODO
    }

    @Override
    public void updateAsciiStream(String columnLabel, java.io.InputStream x) throws SQLException {
        // TODO
    }

    @Override
    public void updateBinaryStream(String columnLabel, java.io.InputStream x) throws SQLException {
        // TODO
    }

    @Override
    public void updateCharacterStream(String columnLabel, java.io.Reader reader) throws SQLException {
        // TODO
    }

    @Override
    public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {
        // TODO
    }

    @Override
    public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {
        // TODO
    }

    @Override
    public void updateClob(int columnIndex, Reader reader) throws SQLException {
        // TODO
    }

    @Override
    public void updateClob(String columnLabel, Reader reader) throws SQLException {
        // TODO
    }

    @Override
    public void updateNClob(int columnIndex, Reader reader) throws SQLException {
        // TODO
    }

    @Override
    public void updateNClob(String columnLabel, Reader reader) throws SQLException {
        // TODO
    }

    @Override
    public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
        // TODO
        return null;
    }
}
