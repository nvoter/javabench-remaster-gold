/*
 * Copyright 2013-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.cloud.sentinel.datasource.factorybean;

import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.datasource.zookeeper.ZookeeperDataSource;
import org.springframework.beans.factory.FactoryBean;

/**
 * A {@link FactoryBean} for creating {@link ZookeeperDataSource} instance.
 *
 * @author <a href="mailto:fangjian0423@gmail.com">Jim</a>
 * @see ZookeeperDataSource
 */
public class ZookeeperDataSourceFactoryBean implements FactoryBean<ZookeeperDataSource> {

    @Nullable
    private String serverAddr;

    @Nullable
    private String path;

    @Nullable
    private String groupId;

    @Nullable
    private String dataId;

    @Nullable
    private Converter converter;

    @Override
    public ZookeeperDataSource getObject() throws Exception {
        // TODO
        return null;
    }

    @Override
    public Class<?> getObjectType() {
        return ZookeeperDataSource.class;
    }

    @Nullable
    public String getServerAddr() {
        // TODO
        return null;
    }

    public void setServerAddr(@Nullable String serverAddr) {
        // TODO
    }

    @Nullable
    public String getPath() {
        // TODO
        return null;
    }

    public void setPath(@Nullable String path) {
        // TODO
    }

    @Nullable
    public String getGroupId() {
        // TODO
        return null;
    }

    public void setGroupId(@Nullable String groupId) {
        // TODO
    }

    @Nullable
    public String getDataId() {
        // TODO
        return null;
    }

    public void setDataId(@Nullable String dataId) {
        // TODO
    }

    @Nullable
    public Converter getConverter() {
        // TODO
        return null;
    }

    public void setConverter(@Nullable Converter converter) {
        this.converter = converter;
    }
}
