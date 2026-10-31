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
package com.alibaba.cloud.sentinel.datasource.config;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.cloud.sentinel.datasource.factorybean.NacosDataSourceFactoryBean;
import org.springframework.core.env.Environment;

/**
 * Nacos Properties class Using by {@link DataSourcePropertiesConfiguration} and
 * {@link NacosDataSourceFactoryBean}.
 *
 * @author <a href="mailto:fangjian0423@gmail.com">Jim</a>
 */
public class NacosDataSourceProperties extends AbstractDataSourceProperties {

    @Nullable
    private String serverAddr;

    @Nullable
    private String contextPath;

    @Nullable
    private String username;

    @Nullable
    private String password;

    @NotEmpty
    private String groupId = "DEFAULT_GROUP";

    @NotEmpty
    @Nullable
    private String dataId;

    @Nullable
    private String endpoint;

    @Nullable
    private String namespace;

    @Nullable
    private String accessKey;

    @Nullable
    private String secretKey;

    public NacosDataSourceProperties() {
        super(NacosDataSourceFactoryBean.class.getName());
        // TODO
    }

    @Override
    public void preCheck(String dataSourceName) {
        if (StringUtils.isEmpty(serverAddr)) {
            Environment env = this.getEnv();
            if (env != null) {
                serverAddr = env.getProperty("spring.cloud.sentinel.datasource.nacos.server-addr", "127.0.0.1:8848");
            } else {
                serverAddr = "127.0.0.1:8848";
            }
        }
    }

    @Nullable
    public String getServerAddr() {
        return serverAddr;
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

    public String getGroupId() {
        // TODO
        return null;
    }

    public void setGroupId(String groupId) {
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
