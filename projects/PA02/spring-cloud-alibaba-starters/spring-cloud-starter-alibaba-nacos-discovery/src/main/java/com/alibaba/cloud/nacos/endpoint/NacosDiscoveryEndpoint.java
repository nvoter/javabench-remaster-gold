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
package com.alibaba.cloud.nacos.endpoint;

import java.util.Map;
import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Endpoint for nacos discovery, get nacos properties and subscribed services.
 *
 * @author xiaojing
 */
@Endpoint(id = "nacosdiscovery")
public class NacosDiscoveryEndpoint {

    private static final Logger log = LoggerFactory.getLogger(NacosDiscoveryEndpoint.class);

    private NacosServiceManager nacosServiceManager;

    private NacosDiscoveryProperties nacosDiscoveryProperties;

    public NacosDiscoveryEndpoint(NacosServiceManager nacosServiceManager, NacosDiscoveryProperties nacosDiscoveryProperties) {
        // TODO
    }

    /**
     * @return nacos discovery endpoint
     */
    @ReadOperation
    public Map<String, Object> nacosDiscovery() {
        // TODO
        return null;
    }
}
