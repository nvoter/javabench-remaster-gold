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
package com.alibaba.cloud.nacos.refresh;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: ruansheng
 * @date: 2024-01-22
 */
public final class NacosSnapshotConfigManager {

    private NacosSnapshotConfigManager() {
    }

    private static final Logger log = LoggerFactory.getLogger(NacosSnapshotConfigManager.class);

    private static final Map<String, String> CONFIG_INFO_SNAPSHOT_MAP = new ConcurrentHashMap<>(8);

    private static final int MAX_SNAPSHOT_COUNT = 100;

    private static String formatConfigSnapshotKey(String dataId, String group) {
        return dataId + "@" + group;
    }

    private static String formatConfigSnapshotKey(@Nullable String namespace, String dataId, String group) {
        if (namespace == null || namespace.isEmpty() || NacosConfigProperties.DEFAULT_NAMESPACE.equals(namespace)) {
            return formatConfigSnapshotKey(dataId, group);
        }
        return namespace + "@" + dataId + "@" + group;
    }

    @Nullable
    public static String getAndRemoveConfigSnapshot(String dataId, String group) {
        // TODO
        return null;
    }

    @Nullable
    public static String getAndRemoveConfigSnapshot(@Nullable String namespace, String dataId, String group) {
        // TODO
        return null;
    }

    public static void putConfigSnapshot(String dataId, String group, String configInfo) {
        // TODO
    }

    public static void putConfigSnapshot(@Nullable String namespace, String dataId, String group, String configInfo) {
        // TODO
    }

    public static void removeConfigSnapshot(String dataId, String group) {
        // TODO
    }

    public static void removeConfigSnapshot(@Nullable String namespace, String dataId, String group) {
        // TODO
    }
}
