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
import com.alibaba.csp.sentinel.datasource.nacos.NacosDataSource;
import org.springframework.beans.factory.FactoryBean;

/**
 * A {@link FactoryBean} for creating {@link NacosDataSource} instance.
 *
 * @author <a href="mailto:fangjian0423@gmail.com">Jim</a>
 * @see NacosDataSource
 */
public class NacosDataSourceFactoryBean implements FactoryBean<NacosDataSource> {

    @Nullable
    private String serverAddr;

    @Nullable
    private String contextPath;

    @Nullable
    private String username;

    @Nullable
    private String password;

    @Nullable
    private String groupId;

    @Nullable
    private String dataId;

    @Nullable
    private Converter converter;

    @Nullable
    private String endpoint;

    @Nullable
    private String namespace;

    @Nullable
    private String accessKey;

    @Nullable
    private String secretKey;

    @Override
    public NacosDataSource getObject() throws Exception {
        // TODO
        return null;
    }

    @Override
    public Class<?> getObjectType() {
        return NacosDataSource.class;
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
    public String getContextPath() {
        // TODO
        return null;
    }

    public void setContextPath(@Nullable String contextPath) {
        // TODO
    }

    @Nullable
    public String getUsername() {
        return username;
    }

    public void setUsername(@Nullable String username) {
        this.username = username;
    }

    @Nullable
    public String getPassword() {
        return password;
    }

    public void setPassword(@Nullable String password) {
        this.password = password;
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

    @Nullable
    public String getEndpoint() {
        // TODO
        return null;
    }

    public void setEndpoint(@Nullable String endpoint) {
        // TODO
    }

    @Nullable
    public String getNamespace() {
        // TODO
        return null;
    }

    public void setNamespace(@Nullable String namespace) {
        // TODO
    }

    @Nullable
    public String getAccessKey() {
        // TODO
        return null;
    }

    public void setAccessKey(@Nullable String accessKey) {
        // TODO
    }

    @Nullable
    public String getSecretKey() {
        // TODO
        return null;
    }

    public void setSecretKey(@Nullable String secretKey) {
        // TODO
    }
}
