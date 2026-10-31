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
import com.alibaba.csp.sentinel.datasource.apollo.ApolloDataSource;
import org.springframework.beans.factory.FactoryBean;

/**
 * A {@link FactoryBean} for creating {@link ApolloDataSource} instance.
 *
 * @author <a href="mailto:fangjian0423@gmail.com">Jim</a>
 * @see ApolloDataSource
 */
public class ApolloDataSourceFactoryBean implements FactoryBean<ApolloDataSource> {

    @Nullable
    private String namespaceName;

    @Nullable
    private String flowRulesKey;

    @Nullable
    private String defaultFlowRuleValue;

    @Nullable
    private Converter converter;

    @Override
    public ApolloDataSource getObject() throws Exception {
        // TODO
        return null;
    }

    @Override
    public Class<?> getObjectType() {
        return ApolloDataSource.class;
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

    @Nullable
    public Converter getConverter() {
        // TODO
        return null;
    }

    public void setConverter(@Nullable Converter converter) {
        this.converter = converter;
    }
}
