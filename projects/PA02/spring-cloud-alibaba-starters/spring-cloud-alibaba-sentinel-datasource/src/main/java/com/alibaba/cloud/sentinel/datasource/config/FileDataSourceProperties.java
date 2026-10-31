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

import com.alibaba.cloud.sentinel.datasource.factorybean.FileRefreshableDataSourceFactoryBean;

/**
 * File Properties class Using by {@link DataSourcePropertiesConfiguration} and
 * {@link FileRefreshableDataSourceFactoryBean}.
 *
 * @author <a href="mailto:fangjian0423@gmail.com">Jim</a>
 */
public class FileDataSourceProperties extends AbstractDataSourceProperties {

    @NotEmpty
    @Nullable
    private String file;

    private String charset = "utf-8";

    private long recommendRefreshMs = 3000L;

    private int bufSize = 1024 * 1024;

    public FileDataSourceProperties() {
        super(FileRefreshableDataSourceFactoryBean.class.getName());
        // TODO
    }

    @Nullable
    public String getFile() {
        // TODO
        return null;
    }

    public void setFile(@Nullable String file) {
        // TODO
    }

    public String getCharset() {
        // TODO
        return null;
    }

    public void setCharset(String charset) {
        // TODO
    }

    public long getRecommendRefreshMs() {
        // TODO
        return 0L;
    }

    public void setRecommendRefreshMs(long recommendRefreshMs) {
        // TODO
    }

    public int getBufSize() {
        // TODO
        return 0;
    }

    public void setBufSize(int bufSize) {
        // TODO
    }

    @Override
    public void preCheck(String dataSourceName) {
        // TODO
    }
}
