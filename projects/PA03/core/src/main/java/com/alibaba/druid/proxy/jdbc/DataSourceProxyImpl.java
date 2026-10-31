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
package com.alibaba.druid.proxy.jdbc;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.stat.JdbcDataSourceStat;
import javax.management.JMException;
import javax.management.openmbean.CompositeDataSupport;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author wenshao [szujobs@hotmail.com]
 */
public class DataSourceProxyImpl implements DataSourceProxy, DataSourceProxyImplMBean {

    private final Driver rawDriver;

    private final DataSourceProxyConfig config;

    private long id;

    private final long createdTimeMillis = System.currentTimeMillis();

    private Properties properties;

    private String dbType;

    private final AtomicLong connectionIdSeed = new AtomicLong(10000);

    private final AtomicLong statementIdSeed = new AtomicLong(20000);

    private final AtomicLong resultSetIdSeed = new AtomicLong(50000);

    private final AtomicLong metaDataIdSeed = new AtomicLong(100000);

    private final AtomicLong transactionIdSeed = new AtomicLong(0);

    private final JdbcDataSourceStat dataSourceStat;

    public DataSourceProxyImpl(Driver rawDriver, DataSourceProxyConfig config) {
        super();
        // TODO
    }

    public String getDbType() {
        return dbType;
    }

    public Driver getRawDriver() {
        return this.rawDriver;
    }

    public String getRawUrl() {
        // TODO
        return null;
    }

    public ConnectionProxy connect(Properties info) throws SQLException {
        // TODO
        return null;
    }

    public DataSourceProxyConfig getConfig() {
        return config;
    }

    public long getDataSourceId() {
        return getId();
    }

    public long getId() {
        // TODO
        return 0L;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return this.config.getName();
    }

    @Override
    public String getUrl() {
        // TODO
        return null;
    }

    public List<Filter> getProxyFilters() {
        // TODO
        return null;
    }

    @Override
    public String[] getFilterClasses() {
        // TODO
        return null;
    }

    @Override
    public String getRawDriverClassName() {
        // TODO
        return null;
    }

    @Override
    public Date getCreatedTime() {
        // TODO
        return null;
    }

    @Override
    public int getRawDriverMajorVersion() {
        // TODO
        return 0;
    }

    @Override
    public int getRawDriverMinorVersion() {
        // TODO
        return 0;
    }

    public String getDataSourceMBeanDomain() {
        // TODO
        return null;
    }

    public String getProperties() {
        // TODO
        return null;
    }

    public Properties getConnectProperties() {
        return properties;
    }

    public CompositeDataSupport getCompositeData() throws JMException {
        // TODO
        return null;
    }

    @Override
    public String getRawJdbcUrl() {
        return config.getRawUrl();
    }

    public long createConnectionId() {
        return connectionIdSeed.incrementAndGet();
    }

    public long createStatementId() {
        return statementIdSeed.getAndIncrement();
    }

    public long createResultSetId() {
        return resultSetIdSeed.getAndIncrement();
    }

    public long createMetaDataId() {
        return metaDataIdSeed.getAndIncrement();
    }

    @Override
    public long createTransactionId() {
        return transactionIdSeed.getAndIncrement();
    }

    public JdbcDataSourceStat getDataSourceStat() {
        // TODO
        return null;
    }
}
