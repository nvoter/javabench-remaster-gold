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
package com.alibaba.druid.mock;

import com.alibaba.druid.util.jdbc.PreparedStatementBase;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MockPreparedStatement extends PreparedStatementBase implements MockStatementBase, PreparedStatement {

    private final String sql;

    public MockPreparedStatement(MockConnection conn, String sql) {
        super(conn);
        // TODO
    }

    public String getSql() {
        // TODO
        return null;
    }

    public MockConnection getConnection() throws SQLException {
        // TODO
        return null;
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        // TODO
        return null;
    }

    @Override
    public int executeUpdate() throws SQLException {
        checkOpen();
        if (getConnection() != null) {
            getConnection().handleSleep();
        }
        return 0;
    }

    @Override
    public boolean execute() throws SQLException {
        checkOpen();
        if (getConnection() != null) {
            getConnection().handleSleep();
        }
        return false;
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        checkOpen();
        if (resultSet == null) {
            resultSet = this.getConnection().getDriver().createResultSet(this);
        }
        return resultSet;
    }
}
