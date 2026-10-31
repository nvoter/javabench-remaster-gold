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
package com.alibaba.druid.wall;

import com.alibaba.druid.support.monitor.annotation.AggregateType;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@MTable(name = "druid_wall")
public class WallProviderStatValue {

    @MField(aggregate = AggregateType.None)
    private String name;

    @MField(aggregate = AggregateType.Sum)
    private long checkCount;

    @MField(aggregate = AggregateType.Sum)
    private long hardCheckCount;

    @MField(aggregate = AggregateType.Sum)
    private long violationCount;

    @MField(aggregate = AggregateType.Sum)
    private long whiteListHitCount;

    @MField(aggregate = AggregateType.Sum)
    private long blackListHitCount;

    @MField(aggregate = AggregateType.Sum)
    private long syntaxErrorCount;

    @MField(aggregate = AggregateType.Sum)
    private long violationEffectRowCount;

    private final List<WallTableStatValue> tables = new ArrayList<WallTableStatValue>();

    private final List<WallFunctionStatValue> functions = new ArrayList<WallFunctionStatValue>();

    private final List<WallSqlStatValue> whiteList = new ArrayList<WallSqlStatValue>();

    private final List<WallSqlStatValue> blackList = new ArrayList<WallSqlStatValue>();

    public WallProviderStatValue() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCheckCount() {
        // TODO
        return 0L;
    }

    public void setCheckCount(long checkCount) {
        this.checkCount = checkCount;
    }

    public long getHardCheckCount() {
        // TODO
        return 0L;
    }

    public void setHardCheckCount(long hardCheckCount) {
        this.hardCheckCount = hardCheckCount;
    }

    public long getViolationCount() {
        // TODO
        return 0L;
    }

    public void setViolationCount(long violationCount) {
        this.violationCount = violationCount;
    }

    public long getWhiteListHitCount() {
        // TODO
        return 0L;
    }

    public void setWhiteListHitCount(long whiteListHitCount) {
        this.whiteListHitCount = whiteListHitCount;
    }

    public long getBlackListHitCount() {
        // TODO
        return 0L;
    }

    public void setBlackListHitCount(long blackListHitCount) {
        this.blackListHitCount = blackListHitCount;
    }

    public long getSyntaxErrorCount() {
        // TODO
        return 0L;
    }

    public void setSyntaxErrorCount(long syntaxErrorCount) {
        this.syntaxErrorCount = syntaxErrorCount;
    }

    public long getViolationEffectRowCount() {
        return violationEffectRowCount;
    }

    public void setViolationEffectRowCount(long violationEffectRowCount) {
        this.violationEffectRowCount = violationEffectRowCount;
    }

    public List<WallTableStatValue> getTables() {
        // TODO
        return null;
    }

    public List<WallFunctionStatValue> getFunctions() {
        // TODO
        return null;
    }

    public List<WallSqlStatValue> getWhiteList() {
        // TODO
        return null;
    }

    public List<WallSqlStatValue> getBlackList() {
        // TODO
        return null;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> info = new LinkedHashMap<String, Object>();
        info.put("checkCount", this.getCheckCount());
        info.put("hardCheckCount", this.getHardCheckCount());
        info.put("violationCount", this.getViolationCount());
        info.put("violationEffectRowCount", this.getViolationEffectRowCount());
        info.put("blackListHitCount", this.getBlackListHitCount());
        info.put("blackListSize", this.getBlackList().size());
        info.put("whiteListHitCount", this.getWhiteListHitCount());
        info.put("whiteListSize", this.getWhiteList().size());
        info.put("syntaxErrorCount", this.getSyntaxErrorCount());
        {
            List<Map<String, Object>> tables = new ArrayList<Map<String, Object>>(this.tables.size());
            for (WallTableStatValue tableStatValue : this.tables) {
                Map<String, Object> statMap = tableStatValue.toMap();
                tables.add(statMap);
            }
            info.put("tables", tables);
        }
        {
            List<Map<String, Object>> functions = new ArrayList<Map<String, Object>>();
            for (WallFunctionStatValue funStatValue : this.functions) {
                Map<String, Object> statMap = funStatValue.toMap();
                functions.add(statMap);
            }
            info.put("functions", functions);
        }
        {
            List<Map<String, Object>> blackList = new ArrayList<Map<String, Object>>(this.blackList.size());
            for (WallSqlStatValue sqlStatValue : this.blackList) {
                blackList.add(sqlStatValue.toMap());
            }
            info.put("blackList", blackList);
        }
        {
            List<Map<String, Object>> whiteList = new ArrayList<Map<String, Object>>(this.whiteList.size());
            for (WallSqlStatValue sqlStatValue : this.whiteList) {
                whiteList.add(sqlStatValue.toMap());
            }
            info.put("whiteList", whiteList);
        }
        return info;
    }
}
