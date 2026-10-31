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
package com.alibaba.cloud.sentinel.gateway.scg;

import com.alibaba.cloud.sentinel.gateway.ConfigConstants;
import com.alibaba.cloud.sentinel.gateway.FallbackProperties;
import org.springframework.core.Ordered;

/**
 * @author <a href="mailto:fangjian0423@gmail.com">Jim</a>
 */
@ConfigurationProperties(prefix = ConfigConstants.GATEWAY_PREFIX)
public class SentinelGatewayProperties {

    @NestedConfigurationProperty
    @Nullable
    private FallbackProperties fallback;

    private Integer order = Ordered.HIGHEST_PRECEDENCE;

    @Nullable
    public FallbackProperties getFallback() {
        // TODO
        return null;
    }

    public SentinelGatewayProperties setFallback(@Nullable FallbackProperties fallback) {
        // TODO
        return null;
    }

    public Integer getOrder() {
        // TODO
        return null;
    }

    public void setOrder(Integer order) {
        // TODO
    }
}
