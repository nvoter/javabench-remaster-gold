/*
 * Copyright 1999-2017 Alibaba Group Holding Ltd.
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
package com.alibaba.druid.sql.visitor;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLExprUtils;
import com.alibaba.druid.sql.ast.expr.SQLVariantRefExpr;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import com.alibaba.druid.sql.parser.SQLSelectListCache;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class ParameterizedOutputVisitorUtils {

    private static final SQLParserFeature[] defaultFeatures = { SQLParserFeature.EnableSQLBinaryOpExprGroup, SQLParserFeature.UseInsertColumnsCache, SQLParserFeature.OptimizedForParameterized };

    private static final SQLParserFeature[] defaultFeatures2 = { SQLParserFeature.EnableSQLBinaryOpExprGroup, SQLParserFeature.UseInsertColumnsCache, SQLParserFeature.OptimizedForParameterized, SQLParserFeature.OptimizedForForParameterizedSkipValue };

    private static final SQLParserFeature[] defaultFeatures_tddl = { SQLParserFeature.EnableSQLBinaryOpExprGroup, SQLParserFeature.UseInsertColumnsCache, SQLParserFeature.OptimizedForParameterized, SQLParserFeature.TDDLHint };

    private static final SQLParserFeature[] defaultFeatures2_tddl = { SQLParserFeature.EnableSQLBinaryOpExprGroup, SQLParserFeature.UseInsertColumnsCache, SQLParserFeature.OptimizedForParameterized, SQLParserFeature.OptimizedForForParameterizedSkipValue, SQLParserFeature.TDDLHint };

    public static String parameterize(String sql, DbType dbType) {
        // TODO
        return null;
    }

    public static String parameterize(String sql, DbType dbType, VisitorFeature... features) {
        // TODO
        return null;
    }

    public static String parameterize(String sql, DbType dbType, SQLSelectListCache selectListCache) {
        // TODO
        return null;
    }

    public static String parameterize(String sql, DbType dbType, List<Object> outParameters) {
        // TODO
        return null;
    }

    public static String parameterize(String sql, DbType dbType, List<Object> outParameters, VisitorFeature... features) {
        // TODO
        return null;
    }

    public static String parameterizeForTDDL(String sql, DbType dbType, List<Object> outParameters, VisitorFeature... features) {
        // TODO
        return null;
    }

    private static void configVisitorFeatures(ParameterizedVisitor visitor, VisitorFeature... features) {
        if (features != null) {
            for (VisitorFeature feature : features) {
                visitor.config(feature, true);
            }
        }
    }

    public static String parameterize(String sql, DbType dbType, SQLSelectListCache selectListCache, List<Object> outParameters, VisitorFeature... visitorFeatures) {
        // TODO
        return null;
    }

    public static String parameterizeForTDDL(String sql, DbType dbType, SQLSelectListCache selectListCache, List<Object> outParameters, VisitorFeature... visitorFeatures) {
        // TODO
        return null;
    }

    public static String parameterize(String sql, DbType dbType, SQLSelectListCache selectListCache, List<Object> outParameters, SQLParserFeature[] features, VisitorFeature... visitorFeatures) {
        // TODO
        return null;
    }

    public static long parameterizeHash(String sql, DbType dbType, List<Object> outParameters) {
        // TODO
        return 0L;
    }

    public static long parameterizeHash(String sql, DbType dbType, SQLSelectListCache selectListCache, List<Object> outParameters) {
        // TODO
        return 0L;
    }

    public static long parameterizeHash(String sql, DbType dbType, SQLSelectListCache selectListCache, List<Object> outParameters, VisitorFeature... visitorFeatures) {
        // TODO
        return 0L;
    }

    public static String parameterize(List<SQLStatement> statementList, DbType dbType) {
        // TODO
        return null;
    }

    public static String parameterize(SQLStatement stmt, DbType dbType) {
        // TODO
        return null;
    }

    public static SQLStatement parameterizeOf(String sql, DbType dbType) {
        // TODO
        return null;
    }

    public static SQLStatement parameterizeOf(String sql, List<Object> outParameters, DbType dbType) {
        // TODO
        return null;
    }

    public static ParameterizedVisitor createParameterizedOutputVisitor(StringBuilder out, DbType dbType) {
        // TODO
        return null;
    }

    public static String restore(String sql, DbType dbType, List<Object> parameters) {
        // TODO
        return null;
    }

    public static String restore(String sql, DbType dbType, Map<String, Object> parameters) {
        // TODO
        return null;
    }

    private static class RestoreVisitor extends SQLASTVisitorAdapter {

        Map<String, Object> parameters;

        TimeZone timeZone;

        public RestoreVisitor(Map<String, Object> parameters) {
            this.parameters = parameters;
        }

        public boolean visit(SQLVariantRefExpr x) {
            String name = x.getName();
            if (name.length() > 3) {
                char c0 = name.charAt(0);
                char c1 = name.charAt(1);
                char c1x = name.charAt(name.length() - 1);
                if (c0 == '#' && c1 == '{' && c1x == '}') {
                    String key = name.substring(2, name.length() - 1);
                    Object value = parameters.get(key);
                    SQLExpr expr = SQLExprUtils.fromJavaObject(value, timeZone);
                    SQLUtils.replaceInParent(x, expr);
                }
            }
            return true;
        }
    }
}
