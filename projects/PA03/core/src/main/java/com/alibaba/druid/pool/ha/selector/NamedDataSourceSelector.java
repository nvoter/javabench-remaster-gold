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
package com.alibaba.druid.pool.ha.selector;

import com.alibaba.druid.pool.ha.HighAvailableDataSource;
import javax.sql.DataSource;

/**
 * Use the given name in ThreadLocal variable to choose DataSource.
 *
 * @author DigitalSonic
 */
public class NamedDataSourceSelector implements DataSourceSelector {

    public static final String DEFAULT_NAME = "default";

    private HighAvailableDataSource highAvailableDataSource;

    private ThreadLocal<String> targetDataSourceName = new ThreadLocal<String>();

    private String defaultName = DEFAULT_NAME;

    public NamedDataSourceSelector(HighAvailableDataSource highAvailableDataSource) {
        // TODO
    }

    @Override
    public void init() {
    }

    @Override
    public void destroy() {
    }

    @Override
    public String getName() {
        return DataSourceSelectorEnum.BY_NAME.getName();
    }

    @Override
    public DataSource get() {
        // TODO
        return null;
    }

    @Override
    public void setTarget(String name) {
        // TODO
    }

    public String getTarget() {
        return targetDataSourceName.get();
    }

    public void resetDataSourceName() {
        // TODO
    }

    public String getDefaultName() {
        return defaultName;
    }

    public void setDefaultName(String defaultName) {
        // TODO
    }
}
