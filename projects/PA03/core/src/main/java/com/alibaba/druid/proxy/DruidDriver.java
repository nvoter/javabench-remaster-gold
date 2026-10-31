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
package com.alibaba.druid.proxy;

import com.alibaba.druid.VERSION;
import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.FilterManager;
import com.alibaba.druid.proxy.jdbc.DataSourceProxy;
import com.alibaba.druid.proxy.jdbc.DataSourceProxyConfig;
import com.alibaba.druid.proxy.jdbc.DataSourceProxyImpl;
import com.alibaba.druid.stat.JdbcStatManager;
import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.util.JMXUtils;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.sql.*;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

/**
 * @author wenshao [szujobs@hotmail.com]
 */
public class DruidDriver implements Driver, DruidDriverMBean {

    // lazy init
    private static Log LOG;

    private static final DruidDriver instance = new DruidDriver();

    private static final ConcurrentMap<String, DataSourceProxyImpl> proxyDataSources = new ConcurrentHashMap<String, DataSourceProxyImpl>(16, 0.75f, 1);

    private static final AtomicInteger dataSourceIdSeed = new AtomicInteger(0);

    private static final AtomicInteger sqlStatIdSeed = new AtomicInteger(0);

    public static final String DEFAULT_PREFIX = "jdbc:wrap-jdbc:";

    public static final String DRIVER_PREFIX = "driver=";

    public static final String PASSWORD_CALLBACK_PREFIX = "passwordCallback=";

    public static final String NAME_PREFIX = "name=";

    public static final String JMX_PREFIX = "jmx=";

    public static final String FILTERS_PREFIX = "filters=";

    private final AtomicLong connectCount = new AtomicLong(0);

    private String acceptPrefix = DEFAULT_PREFIX;

    private int majorVersion = 4;

    private int minorVersion;

    private static final String MBEAN_NAME = "com.alibaba.druid:type=DruidDriver";

    static {
        AccessController.doPrivileged(new PrivilegedAction<Object>() {

            @Override
            public Object run() {
                registerDriver(instance);
                return null;
            }
        });
    }

    public static boolean registerDriver(Driver driver) {
        // TODO
        return false;
    }

    public DruidDriver() {
        // TODO
    }

    public static DruidDriver getInstance() {
        // TODO
        return null;
    }

    public static int createDataSourceId() {
        return dataSourceIdSeed.incrementAndGet();
    }

    public static int createSqlStatId() {
        return sqlStatIdSeed.incrementAndGet();
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        // TODO
        return false;
    }

    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        // TODO
        return null;
    }

    /**
     * 参数定义： com.alibaba.druid.log.LogFilter=filter com.alibaba.druid.log.LogFilter.p1=prop-value
     * com.alibaba.druid.log.LogFilter.p2=prop-value
     *
     * @param url
     * @return
     * @throws SQLException
     */
    public static DataSourceProxyImpl getDataSource(String url, Properties info) throws SQLException {
        DataSourceProxyImpl dataSource = proxyDataSources.get(url);
        if (dataSource == null) {
            DataSourceProxyConfig config = parseConfig(url, info);
            Driver rawDriver = createDriver(config.getRawDriverClassName());
            DataSourceProxyImpl newDataSource = new DataSourceProxyImpl(rawDriver, config);
            {
                String property = System.getProperty("druid.filters");
                if (property != null && property.length() > 0) {
                    for (String filterItem : property.split(",")) {
                        FilterManager.loadFilter(config.getFilters(), filterItem);
                    }
                }
            }
            {
                int dataSourceId = createDataSourceId();
                newDataSource.setId(dataSourceId);
                for (Filter filter : config.getFilters()) {
                    filter.init(newDataSource);
                }
            }
            DataSourceProxy oldDataSource = proxyDataSources.putIfAbsent(url, newDataSource);
            if (oldDataSource == null) {
                if (config.isJmxOption()) {
                    JMXUtils.register("com.alibaba.druid:type=JdbcStat", JdbcStatManager.getInstance());
                }
            }
            dataSource = proxyDataSources.get(url);
        }
        return dataSource;
    }

    public static DataSourceProxyConfig parseConfig(String url, Properties info) throws SQLException {
        // TODO
        return null;
    }

    public static Driver createDriver(final String className) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public int getMajorVersion() {
        // TODO
        return 0;
    }

    @Override
    public int getMinorVersion() {
        // TODO
        return 0;
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        // TODO
        return null;
    }

    @Override
    public boolean jdbcCompliant() {
        // TODO
        return false;
    }

    @Override
    public long getConnectCount() {
        // TODO
        return 0L;
    }

    public String getAcceptPrefix() {
        // TODO
        return null;
    }

    @Override
    public String[] getDataSourceUrls() {
        // TODO
        return null;
    }

    public static ConcurrentMap<String, DataSourceProxyImpl> getProxyDataSources() {
        // TODO
        return null;
    }

    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public void resetStat() {
        connectCount.set(0);
    }

    @Override
    public String getDruidVersion() {
        return VERSION.getVersionNumber();
    }
}
