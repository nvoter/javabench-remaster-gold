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

import java.util.List;

/**
 * Using By ConfigurationProperties.
 *
 * @author <a href="mailto:fangjian0423@gmail.com">Jim</a>
 * @see NacosDataSourceProperties
 * @see ApolloDataSourceProperties
 * @see ZookeeperDataSourceProperties
 * @see FileDataSourceProperties
 * @see RedisDataSourceProperties
 * @see ConsulDataSourceProperties
 */
public class DataSourcePropertiesConfiguration {

    @Nullable
    private FileDataSourceProperties file;

    @Nullable
    private NacosDataSourceProperties nacos;

    @Nullable
    private ZookeeperDataSourceProperties zk;

    @Nullable
    private ApolloDataSourceProperties apollo;

    @Nullable
    private RedisDataSourceProperties redis;

    @Nullable
    private ConsulDataSourceProperties consul;

    public DataSourcePropertiesConfiguration() {
        // TODO
    }

    public DataSourcePropertiesConfiguration(ConsulDataSourceProperties consul) {
        // TODO
    }

    @Nullable
    public ConsulDataSourceProperties getConsul() {
        return consul;
    }

    public void setConsul(@Nullable ConsulDataSourceProperties consul) {
        this.consul = consul;
    }

    public DataSourcePropertiesConfiguration(FileDataSourceProperties file) {
        // TODO
    }

    public DataSourcePropertiesConfiguration(NacosDataSourceProperties nacos) {
        // TODO
    }

    public DataSourcePropertiesConfiguration(ZookeeperDataSourceProperties zk) {
        // TODO
    }

    public DataSourcePropertiesConfiguration(ApolloDataSourceProperties apollo) {
        // TODO
    }

    public DataSourcePropertiesConfiguration(RedisDataSourceProperties redis) {
        // TODO
    }

    @Nullable
    public FileDataSourceProperties getFile() {
        // TODO
        return null;
    }

    public void setFile(@Nullable FileDataSourceProperties file) {
        this.file = file;
    }

    @Nullable
    public NacosDataSourceProperties getNacos() {
        // TODO
        return null;
    }

    public void setNacos(@Nullable NacosDataSourceProperties nacos) {
        this.nacos = nacos;
    }

    @Nullable
    public ZookeeperDataSourceProperties getZk() {
        // TODO
        return null;
    }

    public void setZk(@Nullable ZookeeperDataSourceProperties zk) {
        this.zk = zk;
    }

    @Nullable
    public ApolloDataSourceProperties getApollo() {
        // TODO
        return null;
    }

    public void setApollo(@Nullable ApolloDataSourceProperties apollo) {
        // TODO
    }

    @Nullable
    public RedisDataSourceProperties getRedis() {
        return redis;
    }

    public void setRedis(@Nullable RedisDataSourceProperties redis) {
        this.redis = redis;
    }

    @JsonIgnore
    public List<String> getValidField() {
        // TODO
        return null;
    }

    @JsonIgnore
    @Nullable
    public AbstractDataSourceProperties getValidDataSourceProperties() {
        List<String> invalidFields = getValidField();
        if (invalidFields.size() == 1) {
            try {
                this.getClass().getDeclaredField(invalidFields.get(0)).setAccessible(true);
                return (AbstractDataSourceProperties) this.getClass().getDeclaredField(invalidFields.get(0)).get(this);
            } catch (IllegalAccessException e) {
                // won't happen
            } catch (NoSuchFieldException e) {
                // won't happen
            }
        }
        return null;
    }
}
