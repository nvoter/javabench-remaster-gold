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

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.visitor.SchemaStatVisitor;
import com.alibaba.druid.support.http.stat.WebAppStatManager;
import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.alibaba.druid.support.spring.stat.SpringStatManager;
import com.alibaba.druid.util.MapComparator;
import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 注意：避免直接调用Druid相关对象例如DruidDataSource等，相关调用要到DruidStatManagerFacade里用反射实现
 *
 * @author sandzhang[sandzhangtoo@gmail.com]
 */
public final class DruidStatService implements DruidStatServiceMBean {

    private static final Log LOG = LogFactory.getLog(DruidStatService.class);

    public static final String MBEAN_NAME = "com.alibaba.druid:type=DruidStatService";

    private static final DruidStatService instance = new DruidStatService();

    private static DruidStatManagerFacade statManagerFacade = DruidStatManagerFacade.getInstance();

    public static final int RESULT_CODE_SUCCESS = 1;

    public static final int RESULT_CODE_ERROR = -1;

    private static final int DEFAULT_PAGE = 1;

    private static final int DEFAULT_PER_PAGE_COUNT = Integer.MAX_VALUE;

    private static final String ORDER_TYPE_DESC = "desc";

    private static final String ORDER_TYPE_ASC = "asc";

    private static final String DEFAULT_ORDER_TYPE = ORDER_TYPE_ASC;

    private static final String DEFAULT_ORDERBY = "SQL";

    private DruidStatService() {
        // TODO
    }

    public static DruidStatService getInstance() {
        // TODO
        return null;
    }

    public boolean isResetEnable() {
        // TODO
        return false;
    }

    public void setResetEnable(boolean value) {
        // TODO
    }

    public String service(String url) {
        // TODO
        return null;
    }

    private List<Map<String, Object>> getSpringStatDataList(Map<String, String> parameters) {
        List<Map<String, Object>> array = SpringStatManager.getInstance().getMethodStatData();
        return comparatorOrderBy(array, parameters);
    }

    private List<Map<String, Object>> getWebURIStatDataList(Map<String, String> parameters) {
        List<Map<String, Object>> array = WebAppStatManager.getInstance().getURIStatData();
        return comparatorOrderBy(array, parameters);
    }

    private Map<String, Object> getWebURIStatData(String uri) {
        return WebAppStatManager.getInstance().getURIStatData(uri);
    }

    private Map<String, Object> getWebSessionStatData(String sessionId) {
        return WebAppStatManager.getInstance().getSessionStat(sessionId);
    }

    private Map<String, Object> getSpringMethodStatData(String clazz, String method) {
        return SpringStatManager.getInstance().getMethodStatData(clazz, method);
    }

    private List<Map<String, Object>> getWebSessionStatDataList(Map<String, String> parameters) {
        List<Map<String, Object>> array = WebAppStatManager.getInstance().getSessionStatData();
        return comparatorOrderBy(array, parameters);
    }

    private List<Map<String, Object>> getWebAppStatDataList(Map<String, String> parameters) {
        List<Map<String, Object>> array = WebAppStatManager.getInstance().getWebAppStatData();
        return comparatorOrderBy(array, parameters);
    }

    private List<Map<String, Object>> comparatorOrderBy(List<Map<String, Object>> array, Map<String, String> parameters) {
        // when open the stat page before executing some sql
        if (array == null || array.isEmpty()) {
            return null;
        }
        // when parameters is null
        String orderBy, orderType = null;
        Integer page = DEFAULT_PAGE;
        Integer perPageCount = DEFAULT_PER_PAGE_COUNT;
        if (parameters == null) {
            orderBy = DEFAULT_ORDERBY;
            orderType = DEFAULT_ORDER_TYPE;
            page = DEFAULT_PAGE;
            perPageCount = DEFAULT_PER_PAGE_COUNT;
        } else {
            orderBy = parameters.get("orderBy");
            orderType = parameters.get("orderType");
            String pageParam = parameters.get("page");
            if (pageParam != null && pageParam.length() != 0) {
                page = Integer.parseInt(pageParam);
            }
            String pageCountParam = parameters.get("perPageCount");
            if (pageCountParam != null && pageCountParam.length() > 0) {
                perPageCount = Integer.parseInt(pageCountParam);
            }
        }
        // others,such as order
        orderBy = orderBy == null ? DEFAULT_ORDERBY : orderBy;
        orderType = orderType == null ? DEFAULT_ORDER_TYPE : orderType;
        if (!ORDER_TYPE_DESC.equals(orderType)) {
            orderType = ORDER_TYPE_ASC;
        }
        // orderby the statData array
        if (orderBy.trim().length() != 0) {
            Collections.sort(array, new MapComparator<String, Object>(orderBy, ORDER_TYPE_DESC.equals(orderType)));
        }
        // page
        int fromIndex = (page - 1) * perPageCount;
        int toIndex = page * perPageCount;
        if (toIndex > array.size()) {
            toIndex = array.size();
        }
        return array.subList(fromIndex, toIndex);
    }

    private List<Map<String, Object>> getSqlStatDataList(Map<String, String> parameters) {
        Integer dataSourceId = null;
        String dataSourceIdParam = parameters.get("dataSourceId");
        if (dataSourceIdParam != null && dataSourceIdParam.length() > 0) {
            dataSourceId = Integer.parseInt(dataSourceIdParam);
        }
        List<Map<String, Object>> array = statManagerFacade.getSqlStatDataList(dataSourceId);
        List<Map<String, Object>> sortedArray = comparatorOrderBy(array, parameters);
        return sortedArray;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getWallStatMap(Map<String, String> parameters) {
        // TODO
        return null;
    }

    private String getSqlStat(Integer id) {
        Map<String, Object> map = statManagerFacade.getSqlStatData(id);
        if (map == null) {
            return returnJSONResult(RESULT_CODE_ERROR, null);
        }
        DbType dbType = DbType.of((String) map.get("DbType"));
        String sql = (String) map.get("SQL");
        map.put("formattedSql", SQLUtils.format(sql, dbType));
        List<SQLStatement> statementList = SQLUtils.parseStatements(sql, dbType);
        if (!statementList.isEmpty()) {
            SQLStatement sqlStmt = statementList.get(0);
            SchemaStatVisitor visitor = SQLUtils.createSchemaStatVisitor(dbType);
            sqlStmt.accept(visitor);
            map.put("parsedTable", visitor.getTables().toString());
            map.put("parsedFields", visitor.getColumns().toString());
            map.put("parsedConditions", visitor.getConditions().toString());
            map.put("parsedRelationships", visitor.getRelationships().toString());
            map.put("parsedOrderbycolumns", visitor.getOrderByColumns().toString());
        }
        DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS");
        Date maxTimespanOccurTime = (Date) map.get("MaxTimespanOccurTime");
        if (maxTimespanOccurTime != null) {
            map.put("MaxTimespanOccurTime", format.format(maxTimespanOccurTime));
        }
        return returnJSONResult(map == null ? RESULT_CODE_ERROR : RESULT_CODE_SUCCESS, map);
    }

    private String returnJSONActiveConnectionStackTrace(Integer id) {
        List<String> result = statManagerFacade.getActiveConnectionStackTraceByDataSourceId(id);
        if (result == null) {
            return returnJSONResult(RESULT_CODE_ERROR, "require set removeAbandoned=true");
        }
        return returnJSONResult(RESULT_CODE_SUCCESS, result);
    }

    public static String returnJSONResult(int resultCode, Object content) {
        Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
        dataMap.put("ResultCode", resultCode);
        dataMap.put("Content", content);
        return JSONUtils.toJSONString(dataMap);
    }

    public static void registerMBean() {
        MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
        try {
            ObjectName objectName = new ObjectName(MBEAN_NAME);
            if (!mbeanServer.isRegistered(objectName)) {
                mbeanServer.registerMBean(instance, objectName);
            }
        } catch (JMException ex) {
            LOG.error("register mbean error", ex);
        }
    }

    public static void unregisterMBean() {
        MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
        try {
            mbeanServer.unregisterMBean(new ObjectName(MBEAN_NAME));
        } catch (JMException ex) {
            LOG.error("unregister mbean error", ex);
        }
    }

    public static Map<String, String> getParameters(String url) {
        // TODO
        return null;
    }
}
