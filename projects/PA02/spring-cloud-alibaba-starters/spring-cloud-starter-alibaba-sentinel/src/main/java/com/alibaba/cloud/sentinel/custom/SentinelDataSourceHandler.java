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
package com.alibaba.cloud.sentinel.custom;

import java.util.Arrays;
import java.util.List;
import com.alibaba.cloud.sentinel.SentinelProperties;
import com.alibaba.cloud.sentinel.datasource.config.AbstractDataSourceProperties;
import com.alibaba.csp.sentinel.datasource.AbstractDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.env.Environment;

/**
 * Sentinel {@link ReadableDataSource} Handler Handle the configurations of
 * 'spring.cloud.sentinel.datasource'.
 *
 * @author <a href="mailto:fangjian0423@gmail.com">Jim</a>
 * @see SentinelProperties#datasource
 * @see JsonConverter
 * @see XmlConverter
 */
public class SentinelDataSourceHandler implements SmartInitializingSingleton {

    private static final Logger log = LoggerFactory.getLogger(SentinelDataSourceHandler.class);

    private final List<String> dataTypeList = Arrays.asList("json", "xml");

    private final String DATA_TYPE_FIELD = "dataType";

    private final String CUSTOM_DATA_TYPE = "custom";

    private final String CONVERTER_CLASS_FIELD = "converterClass";

    private final DefaultListableBeanFactory beanFactory;

    private final SentinelProperties sentinelProperties;

    private final Environment env;

    public SentinelDataSourceHandler(DefaultListableBeanFactory beanFactory, SentinelProperties sentinelProperties, Environment env) {
        this.beanFactory = beanFactory;
        this.sentinelProperties = sentinelProperties;
        this.env = env;
    }

    @Override
    public void afterSingletonsInstantiated() {
        sentinelProperties.getDatasource().forEach((dataSourceName, dataSourceProperties) -> {
            try {
                List<String> validFields = dataSourceProperties.getValidField();
                if (validFields.size() != 1) {
                    log.error("[Sentinel Starter] DataSource " + dataSourceName + " multi datasource active and won't loaded: " + dataSourceProperties.getValidField());
                    return;
                }
                AbstractDataSourceProperties abstractDataSourceProperties = dataSourceProperties.getValidDataSourceProperties();
                if (abstractDataSourceProperties == null) {
                    log.error("[Sentinel Starter] DataSource " + dataSourceName + " has no valid data source properties");
                    return;
                }
                abstractDataSourceProperties.setEnv(env);
                abstractDataSourceProperties.preCheck(dataSourceName);
                registerBean(abstractDataSourceProperties, dataSourceName + "-sentinel-" + validFields.get(0) + "-datasource");
            } catch (Exception e) {
                log.error("[Sentinel Starter] DataSource " + dataSourceName + " build error: " + e.getMessage(), e);
            }
        });
    }

    protected BeanDefinitionBuilder parseBeanDefinition(final AbstractDataSourceProperties dataSourceProperties, String dataSourceName) {
        // TODO
        return null;
    }

    private void registerBean(final AbstractDataSourceProperties dataSourceProperties, String dataSourceName) {
        BeanDefinitionBuilder builder = parseBeanDefinition(dataSourceProperties, dataSourceName);
        this.beanFactory.registerBeanDefinition(dataSourceName, builder.getBeanDefinition());
        // init in Spring
        AbstractDataSource newDataSource = (AbstractDataSource) this.beanFactory.getBean(dataSourceName);
        // register property in RuleManager
        dataSourceProperties.postRegister(newDataSource);
    }
}
