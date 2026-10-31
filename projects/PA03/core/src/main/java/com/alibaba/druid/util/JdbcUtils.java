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
package com.alibaba.druid.util;

import com.alibaba.druid.DbType;
import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import javax.sql.DataSource;
import java.io.Closeable;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import java.sql.*;
import java.util.*;

/**
 * @author wenshao [szujobs@hotmail.com]
 */
public final class JdbcUtils implements JdbcConstants {

    private static final Log LOG = LogFactory.getLog(JdbcUtils.class);

    private static final Properties DRIVER_URL_MAPPING = new Properties();

    private static Boolean mysql_driver_version_6;

    private static Boolean clickhouse_driver_version_new;

    static {
        try {
            ClassLoader ctxClassLoader = Thread.currentThread().getContextClassLoader();
            if (ctxClassLoader != null) {
                for (Enumeration<URL> e = ctxClassLoader.getResources("META-INF/druid-driver.properties"); e.hasMoreElements(); ) {
                    URL url = e.nextElement();
                    Properties property = new Properties();
                    InputStream is = null;
                    try {
                        is = url.openStream();
                        property.load(is);
                    } finally {
                        JdbcUtils.close(is);
                    }
                    DRIVER_URL_MAPPING.putAll(property);
                }
            }
        } catch (Exception e) {
            LOG.error("load druid-driver.properties error", e);
        }
    }

    public static void close(Connection x) {
        // TODO
    }

    public static void close(Statement x) {
        // TODO
    }

    public static void close(ResultSet x) {
        // TODO
    }

    public static void close(Closeable x) {
        // TODO
    }

    public static void close(Blob x) {
        // TODO
    }

    public static void close(Clob x) {
        // TODO
    }

    public static void printResultSet(ResultSet rs) throws SQLException {
        // TODO
    }

    public static void printResultSet(ResultSet rs, PrintStream out) throws SQLException {
        // TODO
    }

    public static void printResultSet(ResultSet rs, PrintStream out, boolean printHeader, String seperator) throws SQLException {
        // TODO
    }

    public static String getTypeName(int sqlType) {
        // TODO
        return null;
    }

    public static String getDriverClassName(String rawUrl) throws SQLException {
        // TODO
        return null;
    }

    public static DbType getDbTypeRaw(String rawUrl, String driverClassName) {
        // TODO
        return null;
    }

    public static String getDbType(String rawUrl, String driverClassName) {
        // TODO
        return null;
    }

    public static Driver createDriver(String driverClassName) throws SQLException {
        // TODO
        return null;
    }

    public static Driver createDriver(ClassLoader classLoader, String driverClassName) throws SQLException {
        // TODO
        return null;
    }

    public static int executeUpdate(DataSource dataSource, String sql, Object... parameters) throws SQLException {
        // TODO
        return 0;
    }

    public static int executeUpdate(DataSource dataSource, String sql, List<Object> parameters) throws SQLException {
        // TODO
        return 0;
    }

    public static int executeUpdate(Connection conn, String sql, List<Object> parameters) throws SQLException {
        // TODO
        return 0;
    }

    public static void execute(DataSource dataSource, String sql, Object... parameters) throws SQLException {
        // TODO
    }

    public static void execute(DataSource dataSource, String sql, List<Object> parameters) throws SQLException {
        // TODO
    }

    public static void execute(Connection conn, String sql) throws SQLException {
        // TODO
    }

    public static void execute(Connection conn, String sql, List<Object> parameters) throws SQLException {
        // TODO
    }

    public static List<Map<String, Object>> executeQuery(DataSource dataSource, String sql, Object... parameters) throws SQLException {
        // TODO
        return null;
    }

    public static List<Map<String, Object>> executeQuery(DataSource dataSource, String sql, List<Object> parameters) throws SQLException {
        // TODO
        return null;
    }

    public static List<Map<String, Object>> executeQuery(Connection conn, String sql, List<Object> parameters) throws SQLException {
        // TODO
        return null;
    }

    private static void setParameters(PreparedStatement stmt, List<Object> parameters) throws SQLException {
        for (int i = 0, size = parameters.size(); i < size; ++i) {
            Object param = parameters.get(i);
            stmt.setObject(i + 1, param);
        }
    }

    public static void insertToTable(DataSource dataSource, String tableName, Map<String, Object> data) throws SQLException {
        // TODO
    }

    public static void insertToTable(Connection conn, String tableName, Map<String, Object> data) throws SQLException {
        // TODO
    }

    public static String makeInsertToTableSql(String tableName, Collection<String> names) {
        StringBuilder sql = //
        new StringBuilder().append(//
        "insert into ").append(//
        tableName).append("(");
        int nameCount = 0;
        for (String name : names) {
            if (nameCount > 0) {
                sql.append(",");
            }
            sql.append(name);
            nameCount++;
        }
        sql.append(") values (");
        for (int i = 0; i < nameCount; ++i) {
            if (i != 0) {
                sql.append(",");
            }
            sql.append("?");
        }
        sql.append(")");
        return sql.toString();
    }

    public static <T> void executeQuery(DataSource dataSource, ResultSetConsumer<T> consumer, String sql, Object... parameters) throws SQLException {
        // TODO
    }

    public static List<String> showTables(Connection conn, DbType dbType) throws SQLException {
        // TODO
        return null;
    }

    public static String getCreateTableScript(Connection conn, DbType dbType) throws SQLException {
        // TODO
        return null;
    }

    public static String getCreateTableScript(Connection conn, DbType dbType, boolean sorted, boolean simplify) throws SQLException {
        // TODO
        return null;
    }

    public static boolean isMySqlDriver(String driverClassName) {
        // TODO
        return false;
    }

    public static boolean isOracleDbType(String dbType) {
        return //
        DbType.oracle.name().equals(dbType) || //
        DbType.oceanbase_oracle.name().equals(dbType) || DbType.ali_oracle.name().equalsIgnoreCase(dbType);
    }

    public static boolean isOracleDbType(DbType dbType) {
        return //
        DbType.oracle == dbType || //
        DbType.oceanbase_oracle == dbType || DbType.ali_oracle == dbType;
    }

    public static boolean isMysqlDbType(String dbTypeName) {
        // TODO
        return false;
    }

    public static boolean isMysqlDbType(DbType dbType) {
        // TODO
        return false;
    }

    public static boolean isPgsqlDbType(String dbTypeName) {
        return isPgsqlDbType(DbType.of(dbTypeName));
    }

    public static boolean isPgsqlDbType(DbType dbType) {
        if (dbType == null) {
            return false;
        }
        switch(dbType) {
            case postgresql:
            case edb:
            case polardb:
            case greenplum:
            case gaussdb:
            case hologres:
                return true;
            default:
                return false;
        }
    }

    public static boolean isSqlserverDbType(String dbTypeName) {
        return isSqlserverDbType(DbType.of(dbTypeName));
    }

    public static boolean isSqlserverDbType(DbType dbType) {
        if (dbType == null) {
            return false;
        }
        switch(dbType) {
            case sqlserver:
            case jtds:
                return true;
            default:
                return false;
        }
    }
}
