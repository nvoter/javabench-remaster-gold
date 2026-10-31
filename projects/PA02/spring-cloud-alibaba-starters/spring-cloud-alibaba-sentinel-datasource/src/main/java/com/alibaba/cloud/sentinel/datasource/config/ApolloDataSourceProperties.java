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

import com.alibaba.cloud.sentinel.datasource.factorybean.ApolloDataSourceFactoryBean;

/**
 * Apollo Properties class Using by {@link DataSourcePropertiesConfiguration} and
 * {@link ApolloDataSourceFactoryBean}.
 *
 * @author <a href="mailto:fangjian0423@gmail.com">Jim</a>
 */
public class ApolloDataSourceProperties extends AbstractDataSourceProperties {

    @Nullable
    private String namespaceName;

    @Nullable
    private String flowRulesKey;

    @Nullable
    private String defaultFlowRuleValue;

    public ApolloDataSourceProperties() {
        super(ApolloDataSourceFactoryBean.class.getName());
        // TODO
    }

    @Nullable
    public String getNamespaceName() {
        // TODO
        return null;
    }

    public void setNamespaceName(@Nullable String namespaceName) {
        // TODO
    }

    @Nullable
    public String getFlowRulesKey() {
        // TODO
        return null;
    }

    public void setFlowRulesKey(@Nullable String flowRulesKey) {
        // TODO
    }

    @Nullable
    public String getDefaultFlowRuleValue() {
        // TODO
        return null;
    }

    public void setDefaultFlowRuleValue(@Nullable String defaultFlowRuleValue) {
        // TODO
    }
}
