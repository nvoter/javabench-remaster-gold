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
package com.alibaba.druid.stat;

import com.alibaba.druid.VERSION;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.stat.WebAppStatManager;
import com.alibaba.druid.support.spring.stat.SpringStatManager;
import com.alibaba.druid.util.DruidDataSourceUtils;
import com.alibaba.druid.util.JdbcSqlStatUtils;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.druid.util.Utils;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 监控相关的对外数据暴露
 * <p>
 * 1. 为了支持jndi数据源本类内部调用druid相关对象均需要反射调用,返回值也应该是Object,List&lt;Object&gt;,Map&lt;String,Object&gt;等无关于druid的类型
 * 2. 对外暴露的public方法都应该先调用init()，应该有更好的方式，暂时没想到
 *
 * @author sandzhang[sandzhangtoo@gmail.com]
 */
public final class DruidStatManagerFacade {

    private static final DruidStatManagerFacade instance = new DruidStatManagerFacade();

    private boolean resetEnable = true;

    private final AtomicLong resetCount = new AtomicLong();

    private DruidStatManagerFacade() {
        // TODO
    }

    public static DruidStatManagerFacade getInstance() {
        // TODO
        return null;
    }

    private Set<Object> getDruidDataSourceInstances() {
        return DruidDataSourceStatManager.getInstances().keySet();
    }

    public Object getDruidDataSourceByName(String name) {
        for (Object o : this.getDruidDataSourceInstances()) {
            String itemName = DruidDataSourceUtils.getName(o);
            if (StringUtils.equals(name, itemName)) {
                return o;
            }
        }
        return null;
    }

    public void resetDataSourceStat() {
        DruidDataSourceStatManager.getInstance().reset();
    }

    public void resetSqlStat() {
        JdbcStatManager.getInstance().reset();
    }

    public void resetAll() {
        if (!isResetEnable()) {
            return;
        }
        SpringStatManager.getInstance().resetStat();
        WebAppStatManager.getInstance().resetStat();
        resetSqlStat();
        resetDataSourceStat();
        resetCount.incrementAndGet();
    }

    public void logAndResetDataSource() {
        if (!isResetEnable()) {
            return;
        }
        DruidDataSourceStatManager.getInstance().logAndResetDataSource();
    }

    public boolean isResetEnable() {
        return resetEnable;
    }

    public void setResetEnable(boolean resetEnable) {
        this.resetEnable = resetEnable;
    }

    public Object getSqlStatById(Integer id) {
        for (Object ds : getDruidDataSourceInstances()) {
            Object sqlStat = DruidDataSourceUtils.getSqlStat(ds, id);
            if (sqlStat != null) {
                return sqlStat;
            }
        }
        return null;
    }

    public Map<String, Object> getDataSourceStatData(Integer id) {
        if (id == null) {
            return null;
        }
        Object datasource = getDruidDataSourceById(id);
        return datasource == null ? null : dataSourceToMapData(datasource, false);
    }

    public Object getDruidDataSourceById(Integer identity) {
        if (identity == null) {
            return null;
        }
        for (Object datasource : getDruidDataSourceInstances()) {
            if (System.identityHashCode(datasource) == identity) {
                return datasource;
            }
        }
        return null;
    }

    public List<Map<String, Object>> getSqlStatDataList(Integer dataSourceId) {
        Set<Object> dataSources = getDruidDataSourceInstances();
        if (dataSourceId == null) {
            JdbcDataSourceStat globalStat = JdbcDataSourceStat.getGlobal();
            List<Map<String, Object>> sqlList = new ArrayList<Map<String, Object>>();
            DruidDataSource globalStatDataSource = null;
            for (Object datasource : dataSources) {
                if (datasource instanceof DruidDataSource) {
                    if (((DruidDataSource) datasource).getDataSourceStat() == globalStat) {
                        if (globalStatDataSource == null) {
                            globalStatDataSource = (DruidDataSource) datasource;
                        } else {
                            continue;
                        }
                    }
                }
                sqlList.addAll(getSqlStatDataList(datasource));
            }
            return sqlList;
        }
        for (Object datasource : dataSources) {
            if (dataSourceId != null && dataSourceId.intValue() != System.identityHashCode(datasource)) {
                continue;
            }
            return getSqlStatDataList(datasource);
        }
        return new ArrayList<Map<String, Object>>();
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getWallStatMap(Integer dataSourceId) {
        Set<Object> dataSources = getDruidDataSourceInstances();
        if (dataSourceId == null) {
            Map<String, Object> map = new HashMap<String, Object>();
            for (Object datasource : dataSources) {
                Map<String, Object> wallStat = DruidDataSourceUtils.getWallStatMap(datasource);
                map = mergeWallStat(map, wallStat);
            }
            return map;
        }
        for (Object datasource : dataSources) {
            if (dataSourceId != null && dataSourceId.intValue() != System.identityHashCode(datasource)) {
                continue;
            }
            return DruidDataSourceUtils.getWallStatMap(datasource);
        }
        return new HashMap<String, Object>();
        //
    }

    /**
     * Merges two wall stat maps into a single map.
     *
     * @param mapA the first wall stat map to be merged
     * @param mapB the second wall stat map to be merged
     * @return a new map that contains the merged wall stats from mapA and mapB
     * @deprecated
     */
    public static Map mergWallStat(Map mapA, Map mapB) {
        return mergeWallStat(mapA, mapB);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Map mergeWallStat(Map mapA, Map mapB) {
        // TODO
        return null;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static List<Map<String, Object>> mergeNamedList(List listA, List listB) {
        Map<String, Map<String, Object>> mapped = new HashMap<String, Map<String, Object>>();
        for (Object item : (List) listA) {
            Map<String, Object> map = (Map<String, Object>) item;
            String name = (String) map.get("name");
            mapped.put(name, map);
        }
        List<Map<String, Object>> mergedList = new ArrayList<Map<String, Object>>();
        for (Object item : (List) listB) {
            Map<String, Object> mapB = (Map<String, Object>) item;
            String name = (String) mapB.get("name");
            Map<String, Object> mapA = mapped.get(name);
            Map<String, Object> mergedMap = mergeWallStat(mapA, mapB);
            mergedList.add(mergedMap);
        }
        return mergedList;
    }

    public List<Map<String, Object>> getSqlStatDataList(Object datasource) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        Map<?, ?> sqlStatMap = DruidDataSourceUtils.getSqlStatMap(datasource);
        for (Object sqlStat : sqlStatMap.values()) {
            Map<String, Object> data = JdbcSqlStatUtils.getData(sqlStat);
            long executeCount = (Long) data.get("ExecuteCount");
            long runningCount = (Long) data.get("RunningCount");
            if (executeCount == 0 && runningCount == 0) {
                continue;
            }
            result.add(data);
        }
        return result;
    }

    public Map<String, Object> getSqlStatData(Integer id) {
        if (id == null) {
            return null;
        }
        Object sqlStat = getSqlStatById(id);
        if (sqlStat == null) {
            return null;
        }
        return JdbcSqlStatUtils.getData(sqlStat);
    }

    public List<Map<String, Object>> getDataSourceStatDataList() {
        // TODO
        return null;
    }

    public List<Map<String, Object>> getDataSourceStatDataList(boolean includeSqlList) {
        // TODO
        return null;
    }

    public List<List<String>> getActiveConnStackTraceList() {
        List<List<String>> traceList = new ArrayList<List<String>>();
        for (Object dataSource : getDruidDataSourceInstances()) {
            List<String> stacks = ((DruidDataSource) dataSource).getActiveConnectionStackTrace();
            if (stacks.size() > 0) {
                traceList.add(stacks);
            }
        }
        return traceList;
    }

    public Map<String, Object> returnJSONBasicStat() {
        Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
        dataMap.put("Version", VERSION.getVersionNumber());
        dataMap.put("Drivers", getDriversData());
        dataMap.put("ResetEnable", isResetEnable());
        dataMap.put("ResetCount", getResetCount());
        dataMap.put("JavaVMName", System.getProperty("java.vm.name"));
        dataMap.put("JavaVersion", System.getProperty("java.version"));
        dataMap.put("JavaClassPath", System.getProperty("java.class.path"));
        dataMap.put("StartTime", Utils.getStartTime());
        return dataMap;
    }

    public long getResetCount() {
        return resetCount.get();
    }

    private List<String> getDriversData() {
        List<String> drivers = new ArrayList<String>();
        for (Enumeration<Driver> e = DriverManager.getDrivers(); e.hasMoreElements(); ) {
            Driver driver = e.nextElement();
            drivers.add(driver.getClass().getName());
        }
        return drivers;
    }

    public List<Map<String, Object>> getPoolingConnectionInfoByDataSourceId(Integer id) {
        Object datasource = getDruidDataSourceById(id);
        if (datasource == null) {
            return null;
        }
        return DruidDataSourceUtils.getPoolingConnectionInfo(datasource);
    }

    public List<String> getActiveConnectionStackTraceByDataSourceId(Integer id) {
        Object datasource = getDruidDataSourceById(id);
        if (datasource == null || !DruidDataSourceUtils.isRemoveAbandoned(datasource)) {
            return null;
        }
        return DruidDataSourceUtils.getActiveConnectionStackTrace(datasource);
    }

    private Map<String, Object> dataSourceToMapData(Object dataSource, boolean includeSql) {
        Map<String, Object> map = DruidDataSourceUtils.getStatData(dataSource);
        if (includeSql) {
            List<Map<String, Object>> sqlList = getSqlStatDataList(dataSource);
            map.put("SQL", sqlList);
        }
        return map;
    }
}
