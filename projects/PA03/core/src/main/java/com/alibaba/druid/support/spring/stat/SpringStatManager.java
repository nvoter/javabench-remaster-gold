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
package com.alibaba.druid.support.spring.stat;

import com.alibaba.druid.stat.DruidDataSourceStatManager;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

public class SpringStatManager {

    public static final String SYS_PROP_INSTANCES = "druid.spring.springStat";

    private static final SpringStatManager instance = new SpringStatManager();

    private Set<Object> springStatSet;

    public static SpringStatManager getInstance() {
        // TODO
        return null;
    }

    public Set<Object> getSpringStatSet() {
        if (springStatSet == null) {
            if (DruidDataSourceStatManager.isRegisterToSystemProperty()) {
                springStatSet = getSpringStatSetFromSysProperty();
            } else {
                springStatSet = new CopyOnWriteArraySet<Object>();
            }
        }
        return springStatSet;
    }

    public void addSpringStat(Object springStat) {
        getSpringStatSet().add(springStat);
    }

    @SuppressWarnings("unchecked")
    static Set<Object> getSpringStatSetFromSysProperty() {
        Properties properties = System.getProperties();
        Set<Object> webAppStats = (Set<Object>) properties.get(SYS_PROP_INSTANCES);
        if (webAppStats == null) {
            synchronized (properties) {
                webAppStats = (Set<Object>) properties.get(SYS_PROP_INSTANCES);
                if (webAppStats == null) {
                    webAppStats = new CopyOnWriteArraySet<Object>();
                    properties.put(SYS_PROP_INSTANCES, webAppStats);
                }
            }
        }
        return webAppStats;
    }

    public List<Map<String, Object>> getMethodStatData() {
        // TODO
        return null;
    }

    public Map<String, Object> getMethodStatData(String clazz, String method) {
        // TODO
        return null;
    }

    public void resetStat() {
        Set<Object> stats = getSpringStatSet();
        for (Object stat : stats) {
            SpringStatUtils.reset(stat);
        }
    }
}
