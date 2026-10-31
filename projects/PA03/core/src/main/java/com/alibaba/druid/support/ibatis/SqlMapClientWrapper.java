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
package com.alibaba.druid.support.ibatis;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapSession;
import com.ibatis.sqlmap.engine.execution.SqlExecutor;
import com.ibatis.sqlmap.engine.impl.ExtendedSqlMapClient;
import com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate;
import com.ibatis.sqlmap.engine.mapping.result.ResultObjectFactory;
import com.ibatis.sqlmap.engine.mapping.statement.MappedStatement;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SuppressWarnings("deprecation")
public class SqlMapClientWrapper extends SqlMapExecutorWrapper implements SqlMapClient, ExtendedSqlMapClient {

    protected final ExtendedSqlMapClient client;

    public SqlMapClientWrapper(ExtendedSqlMapClient client) {
        super(client, client);
        this.client = client;
    }

    public ExtendedSqlMapClient getClient() {
        // TODO
        return null;
    }

    public void startTransaction() throws SQLException {
        // TODO
    }

    public void startTransaction(int transactionIsolation) throws SQLException {
        // TODO
    }

    public void commitTransaction() throws SQLException {
        // TODO
    }

    public void endTransaction() throws SQLException {
        // TODO
    }

    public void setUserConnection(Connection connection) throws SQLException {
        // TODO
    }

    public Connection getUserConnection() throws SQLException {
        // TODO
        return null;
    }

    public Connection getCurrentConnection() throws SQLException {
        // TODO
        return null;
    }

    public DataSource getDataSource() {
        // TODO
        return null;
    }

    public SqlMapSession openSession() {
        // TODO
        return null;
    }

    public SqlMapSession openSession(Connection conn) {
        // TODO
        return null;
    }

    public SqlMapSession getSession() {
        // TODO
        return null;
    }

    public void flushDataCache() {
        // TODO
    }

    public void flushDataCache(String cacheId) {
        // TODO
    }

    public MappedStatement getMappedStatement(String id) {
        // TODO
        return null;
    }

    public boolean isLazyLoadingEnabled() {
        // TODO
        return false;
    }

    public boolean isEnhancementEnabled() {
        // TODO
        return false;
    }

    public SqlExecutor getSqlExecutor() {
        // TODO
        return null;
    }

    public SqlMapExecutorDelegate getDelegate() {
        // TODO
        return null;
    }

    public ResultObjectFactory getResultObjectFactory() {
        // TODO
        return null;
    }
}
