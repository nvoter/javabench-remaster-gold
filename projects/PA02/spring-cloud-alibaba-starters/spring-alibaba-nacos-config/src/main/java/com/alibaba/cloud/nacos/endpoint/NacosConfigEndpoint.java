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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.cloud.nacos.refresh.NacosRefreshHistory;

/**
 * Endpoint for Nacos, contains config data and refresh history.
 *
 * @author xiaojing
 */
@Endpoint(id = "nacosconfig")
public class NacosConfigEndpoint {

    private final NacosConfigProperties properties;

    private final NacosRefreshHistory refreshHistory;

    private final ThreadLocal<DateFormat> dateFormat = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    public NacosConfigEndpoint(NacosConfigProperties properties, NacosRefreshHistory refreshHistory) {
        // TODO
    }

    @ReadOperation
    public Map<String, Object> invoke() {
        // TODO
        return null;
    }
}
