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
import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

public class DruidDataSourceC3P0Adapter implements DataSource, DruidDataSourceC3P0AdapterMBean {

    private DruidDataSource dataSource;

    public DruidDataSourceC3P0Adapter() {
        // TODO
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        // TODO
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        // TODO
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        // TODO
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        // TODO
        return 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        // TODO
        return false;
    }

    @Override
    public Connection getConnection() throws SQLException {
        // TODO
        return null;
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        // TODO
        return null;
    }

    public String getDriverClass() {
        // TODO
        return null;
    }

    public void setDriverClass(String driverClass) {
        // TODO
    }

    public String getJdbcUrl() {
        // TODO
        return null;
    }

    public Driver getDriver() {
        // TODO
        return null;
    }

    public void setDriver(Driver driver) {
        // TODO
    }

    public void setJdbcUrl(String jdbcUrl) {
        // TODO
    }

    public Properties getProperties() {
        // TODO
        return null;
    }

    public void setProperties(Properties properties) {
        // TODO
    }

    public String getUser() {
        // TODO
        return null;
    }

    public void setUser(String user) {
        // TODO
    }

    public String getPassword() {
        // TODO
        return null;
    }

    public void setPassword(String password) {
        // TODO
    }

    public int getCheckoutTimeout() {
        // TODO
        return 0;
    }

    public void setCheckoutTimeout(int checkoutTimeout) {
        // TODO
    }

    public boolean isAutoCommitOnClose() {
        // TODO
        return false;
    }

    public void setAutoCommitOnClose(boolean autoCommitOnClose) {
        // TODO
    }

    public int getIdleConnectionTestPeriod() {
        // TODO
        return 0;
    }

    public void setIdleConnectionTestPeriod(int idleConnectionTestPeriod) {
        // TODO
    }

    public int getInitialPoolSize() {
        // TODO
        return 0;
    }

    public void setInitialPoolSize(int initialPoolSize) {
        // TODO
    }

    public int getMaxIdleTime() {
        // TODO
        return 0;
    }

    public void setMaxIdleTime(int maxIdleTime) {
        // TODO
    }

    public int getMaxPoolSize() {
        // TODO
        return 0;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        // TODO
    }

    public int getMinPoolSize() {
        // TODO
        return 0;
    }

    public void setMinPoolSize(int minPoolSize) {
        // TODO
    }

    public boolean isTestConnectionOnCheckout() {
        // TODO
        return false;
    }

    public void setTestConnectionOnCheckout(boolean testConnectionOnCheckout) {
        // TODO
    }

    public boolean isTestConnectionOnCheckin() {
        // TODO
        return false;
    }

    public void setTestConnectionOnCheckin(boolean testConnectionOnCheckin) {
        // TODO
    }

    public String getPreferredTestQuery() {
        // TODO
        return null;
    }

    public void setPreferredTestQuery(String preferredTestQuery) {
        // TODO
    }

    public void setFilters(String filters) throws SQLException {
        // TODO
    }

    public List<Filter> getProxyFilters() {
        // TODO
        return null;
    }

    public void setProxyFilters(List<Filter> filters) {
        // TODO
    }

    public String getDataSourceName() {
        // TODO
        return null;
    }

    public int getNumConnections() {
        // TODO
        return 0;
    }

    public int getNumIdleConnections() {
        // TODO
        return 0;
    }

    public int getNumBusyConnections() {
        // TODO
        return 0;
    }

    public int getNumUnclosedOrphanedConnections() {
        // TODO
        return 0;
    }

    public int getNumConnectionsDefaultUser() {
        // TODO
        return 0;
    }

    public int getNumIdleConnectionsDefaultUser() {
        // TODO
        return 0;
    }

    public int getNumBusyConnectionsDefaultUser() {
        // TODO
        return 0;
    }

    public int getMaxStatementsPerConnection() {
        // TODO
        return 0;
    }

    public void setMaxStatementsPerConnection(int maxStatementsPerConnection) {
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(maxStatementsPerConnection);
    }

    public int getMaxStatements() {
        // TODO
        return 0;
    }

    public void setMaxStatements(int maxStatements) {
        // TODO
    }

    public int getUnreturnedConnectionTimeout() {
        // TODO
        return 0;
    }

    public void setUnreturnedConnectionTimeout(int unreturnedConnectionTimeout) {
        // TODO
    }

    public boolean isDebugUnreturnedConnectionStackTraces() {
        // TODO
        return false;
    }

    public void setDebugUnreturnedConnectionStackTraces(boolean debugUnreturnedConnectionStackTraces) {
        dataSource.setLogAbandoned(debugUnreturnedConnectionStackTraces);
    }

    public int getAcquireRetryAttempts() {
        // TODO
        return 0;
    }

    public void setAcquireRetryAttempts(int acquireRetryAttempts) {
        // TODO
    }

    public int getAcquireRetryDelay() {
        // TODO
        return 0;
    }

    public void setAcquireRetryDelay(int acquireRetryDelay) {
        // TODO
    }

    public boolean isBreakAfterAcquireFailure() {
        // TODO
        return false;
    }

    public void setBreakAfterAcquireFailure(boolean breakAfterAcquireFailure) {
        // TODO
    }

    // /////////////////
    @Override
    public boolean isEnable() {
        // TODO
        return false;
    }

    @Override
    public void shrink() {
        // TODO
    }

    @Override
    public int getWaitThreadCount() {
        // TODO
        return 0;
    }

    @Override
    public int getLockQueueLength() {
        // TODO
        return 0;
    }

    @Override
    public void close() {
        // TODO
    }

    // ///////////////
    private String overrideDefaultUser;

    private String overrideDefaultPassword;

    private int propertyCycle;

    private boolean usesTraditionalReflectiveProxies;

    private String userOverridesAsString;

    private int maxAdministrativeTaskTime;

    private int maxIdleTimeExcessConnections;

    private int maxConnectionAge;

    private String connectionCustomizerClassName;

    private String factoryClassLocation;

    private int acquireIncrement = 1;

    private String connectionTesterClassName;

    private String automaticTestTable;

    public String getConnectionTesterClassName() {
        // TODO
        return null;
    }

    public void setConnectionTesterClassName(String connectionTesterClassName) {
        // TODO
    }

    public String getAutomaticTestTable() {
        // TODO
        return null;
    }

    public void setAutomaticTestTable(String automaticTestTable) {
        // TODO
    }

    private boolean forceIgnoreUnresolvedTransactions;

    public boolean isForceIgnoreUnresolvedTransactions() {
        // TODO
        return false;
    }

    public void setForceIgnoreUnresolvedTransactions(boolean forceIgnoreUnresolvedTransactions) {
        // TODO
    }

    public boolean isUsesTraditionalReflectiveProxies() {
        // TODO
        return false;
    }

    public void setUsesTraditionalReflectiveProxies(boolean usesTraditionalReflectiveProxies) {
        // TODO
    }

    public String getUserOverridesAsString() {
        // TODO
        return null;
    }

    public void setUserOverridesAsString(String userOverridesAsString) {
        // TODO
    }

    public int getMaxAdministrativeTaskTime() {
        // TODO
        return 0;
    }

    public void setMaxAdministrativeTaskTime(int maxAdministrativeTaskTime) {
        // TODO
    }

    public int getMaxIdleTimeExcessConnections() {
        // TODO
        return 0;
    }

    public void setMaxIdleTimeExcessConnections(int maxIdleTimeExcessConnections) {
        // TODO
    }

    public int getMaxConnectionAge() {
        // TODO
        return 0;
    }

    public void setMaxConnectionAge(int maxConnectionAge) {
        // TODO
    }

    public String getConnectionCustomizerClassName() {
        // TODO
        return null;
    }

    public void setConnectionCustomizerClassName(String connectionCustomizerClassName) {
        // TODO
    }

    public String getFactoryClassLocation() {
        // TODO
        return null;
    }

    public void setFactoryClassLocation(String factoryClassLocation) {
        // TODO
    }

    public int getAcquireIncrement() {
        // TODO
        return 0;
    }

    public void setAcquireIncrement(int acquireIncrement) {
        // TODO
    }

    public String getOverrideDefaultUser() {
        // TODO
        return null;
    }

    public void setOverrideDefaultUser(String overrideDefaultUser) {
        // TODO
    }

    public String getOverrideDefaultPassword() {
        // TODO
        return null;
    }

    public void setOverrideDefaultPassword(String overrideDefaultPassword) {
        // TODO
    }

    public int getPropertyCycle() {
        // TODO
        return 0;
    }

    public void setPropertyCycle(int propertyCycle) {
        // TODO
    }

    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        // TODO
        return null;
    }
}
