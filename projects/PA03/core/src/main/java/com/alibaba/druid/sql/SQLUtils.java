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
package com.alibaba.druid.sql;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.*;
import com.alibaba.druid.sql.ast.expr.*;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.athena.visitor.AthenaOutputVisitor;
import com.alibaba.druid.sql.dialect.bigquery.visitor.BigQueryOutputVisitor;
import com.alibaba.druid.sql.dialect.blink.vsitor.BlinkOutputVisitor;
import com.alibaba.druid.sql.dialect.clickhouse.visitor.CKOutputVisitor;
import com.alibaba.druid.sql.dialect.databricks.visitor.DatabricksOutputASTVisitor;
import com.alibaba.druid.sql.dialect.db2.visitor.DB2OutputVisitor;
import com.alibaba.druid.sql.dialect.dm.visitor.DmOutputVisitor;
import com.alibaba.druid.sql.dialect.doris.visitor.DorisOutputVisitor;
import com.alibaba.druid.sql.dialect.gaussdb.visitor.GaussDbOutputVisitor;
import com.alibaba.druid.sql.dialect.h2.visitor.H2OutputVisitor;
import com.alibaba.druid.sql.dialect.hive.ast.HiveInsert;
import com.alibaba.druid.sql.dialect.hive.ast.HiveInsertStatement;
import com.alibaba.druid.sql.dialect.hive.visitor.HiveASTVisitorAdapter;
import com.alibaba.druid.sql.dialect.hive.visitor.HiveOutputVisitor;
import com.alibaba.druid.sql.dialect.hologres.visitor.HologresOutputVisitor;
import com.alibaba.druid.sql.dialect.impala.visitor.ImpalaOutputVisitor;
import com.alibaba.druid.sql.dialect.informix.visitor.InformixOutputVisitor;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitorAdapter;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlOutputVisitor;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsCreateTableStatement;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsSelectQueryBlock;
import com.alibaba.druid.sql.dialect.odps.visitor.OdpsASTVisitorAdapter;
import com.alibaba.druid.sql.dialect.odps.visitor.OdpsOutputVisitor;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleOutputVisitor;
import com.alibaba.druid.sql.dialect.oscar.visitor.OscarOutputVisitor;
import com.alibaba.druid.sql.dialect.postgresql.visitor.PGOutputVisitor;
import com.alibaba.druid.sql.dialect.presto.visitor.PrestoOutputVisitor;
import com.alibaba.druid.sql.dialect.redshift.visitor.RedshiftOutputVisitor;
import com.alibaba.druid.sql.dialect.spark.visitor.SparkOutputASTVisitor;
import com.alibaba.druid.sql.dialect.sqlite.visitor.SQLiteOutputVisitor;
import com.alibaba.druid.sql.dialect.sqlserver.visitor.SQLServerOutputVisitor;
import com.alibaba.druid.sql.dialect.starrocks.visitor.StarRocksOutputVisitor;
import com.alibaba.druid.sql.dialect.supersql.visitor.SuperSqlOutputVisitor;
import com.alibaba.druid.sql.dialect.synapse.visitor.SynapseOutputVisitor;
import com.alibaba.druid.sql.dialect.teradata.visitor.TDOutputVisitor;
import com.alibaba.druid.sql.parser.*;
import com.alibaba.druid.sql.repository.SchemaRepository;
import com.alibaba.druid.sql.visitor.*;
import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.alibaba.druid.util.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class SQLUtils {

    private static final SQLParserFeature[] FORMAT_DEFAULT_FEATURES = { SQLParserFeature.KeepComments, SQLParserFeature.EnableSQLBinaryOpExprGroup };

    public static FormatOption DEFAULT_FORMAT_OPTION = new FormatOption(true, true);

    public static FormatOption DEFAULT_LCASE_FORMAT_OPTION = new FormatOption(false, true);

    private static final Log LOG = LogFactory.getLog(SQLUtils.class);

    public static String toSQLString(SQLObject sqlObject, String dbType) {
        // TODO
        return null;
    }

    public static String toSQLString(SQLObject sqlObject, DbType dbType) {
        // TODO
        return null;
    }

    public static String toSQLString(SQLObject sqlObject, DbType dbType, FormatOption option) {
        // TODO
        return null;
    }

    public static String toSQLString(SQLObject sqlObject, DbType dbType, FormatOption option, VisitorFeature... features) {
        // TODO
        return null;
    }

    public static String toSQLString(SQLObject obj) {
        // TODO
        return null;
    }

    public static String toOdpsString(SQLObject sqlObject) {
        // TODO
        return null;
    }

    public static String toHiveString(SQLObject sqlObject) {
        // TODO
        return null;
    }

    public static String toOdpsString(SQLObject sqlObject, FormatOption option) {
        // TODO
        return null;
    }

    public static String toAntsparkString(SQLObject sqlObject) {
        return toAntsparkString(sqlObject, null);
    }

    public static String toAntsparkString(SQLObject sqlObject, FormatOption option) {
        return toSQLString(sqlObject, DbType.spark, option);
    }

    public static String toMySqlString(SQLObject sqlObject) {
        // TODO
        return null;
    }

    public static String toMySqlStringIfNotNull(SQLObject sqlObject, String defaultStr) {
        if (sqlObject == null) {
            return defaultStr;
        }
        return toMySqlString(sqlObject, (FormatOption) null);
    }

    public static String toMySqlString(SQLObject sqlObject, VisitorFeature... features) {
        // TODO
        return null;
    }

    public static String toNormalizeMysqlString(SQLObject sqlObject) {
        if (sqlObject != null) {
            return SQLUtils.normalize(toSQLString(sqlObject, DbType.mysql));
        }
        return null;
    }

    public static String toMySqlString(SQLObject sqlObject, FormatOption option) {
        // TODO
        return null;
    }

    public static SQLExpr toMySqlExpr(String sql) {
        return toSQLExpr(sql, DbType.mysql);
    }

    public static String formatMySql(String sql) {
        // TODO
        return null;
    }

    public static String formatMySql(String sql, FormatOption option) {
        // TODO
        return null;
    }

    public static String formatOracle(String sql) {
        // TODO
        return null;
    }

    public static String formatOracle(String sql, FormatOption option) {
        // TODO
        return null;
    }

    public static String formatOdps(String sql) {
        // TODO
        return null;
    }

    public static String formatPresto(String sql) {
        return formatPresto(sql, null);
    }

    public static String formatPresto(String sql, FormatOption option) {
        SQLParserFeature[] features = { SQLParserFeature.KeepComments, SQLParserFeature.EnableSQLBinaryOpExprGroup, SQLParserFeature.KeepNameQuotes };
        return format(sql, DbType.mysql, null, option, features);
    }

    public static String formatHive(String sql) {
        // TODO
        return null;
    }

    public static String formatOdps(String sql, FormatOption option) {
        // TODO
        return null;
    }

    public static String formatHive(String sql, FormatOption option) {
        // TODO
        return null;
    }

    public static String formatSQLServer(String sql) {
        return format(sql, DbType.sqlserver);
    }

    public static String toOracleString(SQLObject sqlObject) {
        // TODO
        return null;
    }

    public static String toOracleString(SQLObject sqlObject, FormatOption option) {
        // TODO
        return null;
    }

    public static String toPGString(SQLObject sqlObject) {
        // TODO
        return null;
    }

    public static String toPGString(SQLObject sqlObject, FormatOption option) {
        // TODO
        return null;
    }

    public static String toDB2String(SQLObject sqlObject) {
        // TODO
        return null;
    }

    public static String toDB2String(SQLObject sqlObject, FormatOption option) {
        // TODO
        return null;
    }

    public static String toSQLServerString(SQLObject sqlObject) {
        return toSQLServerString(sqlObject, null);
    }

    public static String toSQLServerString(SQLObject sqlObject, FormatOption option) {
        return toSQLString(sqlObject, DbType.sqlserver, option);
    }

    public static String formatPGSql(String sql, FormatOption option) {
        return format(sql, DbType.postgresql, option);
    }

    public static SQLExpr toSQLExpr(String sql, DbType dbType) {
        // TODO
        return null;
    }

    public static SQLExpr toSQLExpr(String sql, DbType dbType, SQLParserFeature... features) {
        // TODO
        return null;
    }

    public static SQLSelectOrderByItem toOrderByItem(String sql, DbType dbType) {
        SQLExprParser parser = SQLParserUtils.createExprParser(sql, dbType);
        SQLSelectOrderByItem orderByItem = parser.parseSelectOrderByItem();
        if (parser.getLexer().token() != Token.EOF) {
            throw new ParserException("illegal sql expr : " + sql + ", " + parser.getLexer().info());
        }
        return orderByItem;
    }

    public static SQLUpdateSetItem toUpdateSetItem(String sql, DbType dbType) {
        SQLExprParser parser = SQLParserUtils.createExprParser(sql, dbType);
        SQLUpdateSetItem updateSetItem = parser.parseUpdateSetItem();
        if (parser.getLexer().token() != Token.EOF) {
            throw new ParserException("illegal sql expr : " + sql + ", " + parser.getLexer().info());
        }
        return updateSetItem;
    }

    public static SQLSelectItem toSelectItem(String sql, DbType dbType) {
        SQLExprParser parser = SQLParserUtils.createExprParser(sql, dbType);
        SQLSelectItem selectItem = parser.parseSelectItem();
        if (parser.getLexer().token() != Token.EOF) {
            throw new ParserException("illegal sql expr : " + sql + ", " + parser.getLexer().info());
        }
        return selectItem;
    }

    public static List<SQLStatement> toStatementList(String sql, DbType dbType) {
        // TODO
        return null;
    }

    public static SQLExpr toSQLExpr(String sql) {
        // TODO
        return null;
    }

    public static String format(String sql, String dbType) {
        // TODO
        return null;
    }

    public static String format(String sql, DbType dbType) {
        // TODO
        return null;
    }

    public static String format(String sql, DbType dbType, FormatOption option) {
        // TODO
        return null;
    }

    public static String format(String sql, DbType dbType, List<Object> parameters) {
        // TODO
        return null;
    }

    public static String format(String sql, DbType dbType, List<Object> parameters, FormatOption option) {
        // TODO
        return null;
    }

    public static String format(String sql, DbType dbType, List<Object> parameters, FormatOption option, SQLParserFeature[] features) {
        // TODO
        return null;
    }

    public static String toSQLString(List<SQLStatement> statementList, DbType dbType) {
        // TODO
        return null;
    }

    public static String toSQLString(List<SQLStatement> statementList, DbType dbType, FormatOption option) {
        // TODO
        return null;
    }

    public static String toSQLString(List<SQLStatement> statementList, DbType dbType, List<Object> parameters) {
        // TODO
        return null;
    }

    public static String toSQLString(List<SQLStatement> statementList, DbType dbType, List<Object> parameters, FormatOption option) {
        // TODO
        return null;
    }

    public static String toSQLString(List<SQLStatement> statementList, DbType dbType, SQLASTOutputVisitor visitor) {
        // TODO
        return null;
    }

    public static String toSQLString(List<SQLStatement> statementList, DbType dbType, List<Object> parameters, FormatOption option, Map<String, String> tableMapping) {
        // TODO
        return null;
    }

    public static String toSQLString(List<SQLStatement> statementList, DbType dbType, List<Object> parameters, Map<String, String> tableMapping, SQLASTOutputVisitor visitor) {
        // TODO
        return null;
    }

    public static SQLASTOutputVisitor createOutputVisitor(StringBuilder out, DbType dbType) {
        // TODO
        return null;
    }

    public static SQLASTOutputVisitor createFormatOutputVisitor(StringBuilder out, List<SQLStatement> statementList, DbType dbType) {
        if (dbType == null) {
            if (statementList != null && statementList.size() > 0) {
                dbType = statementList.get(0).getDbType();
            }
            if (dbType == null) {
                dbType = DbType.other;
            }
        }
        switch(dbType) {
            case oracle:
            case oceanbase_oracle:
            case polardb2:
                if (statementList == null || statementList.size() == 1) {
                    return new OracleOutputVisitor(out, false);
                } else {
                    return new OracleOutputVisitor(out, true);
                }
            case mysql:
            case mariadb:
            case tidb:
            case polardbx:
                return new MySqlOutputVisitor(out);
            case postgresql:
            case greenplum:
            case edb:
                return new PGOutputVisitor(out);
            case gaussdb:
                return new GaussDbOutputVisitor(out);
            case hologres:
                return new HologresOutputVisitor(out);
            case redshift:
                return new RedshiftOutputVisitor(out);
            case sqlserver:
            case jtds:
                return new SQLServerOutputVisitor(out);
            case synapse:
                return new SynapseOutputVisitor(out);
            case db2:
                return new DB2OutputVisitor(out);
            case odps:
                return new OdpsOutputVisitor(out);
            case h2:
            case lealone:
                return new H2OutputVisitor(out);
            case informix:
                return new InformixOutputVisitor(out);
            case hive:
                return new HiveOutputVisitor(out);
            case blink:
                return new BlinkOutputVisitor(out);
            case spark:
                return new SparkOutputASTVisitor(out);
            case databricks:
                return new DatabricksOutputASTVisitor(out);
            case presto:
            case trino:
                return new PrestoOutputVisitor(out);
            case supersql:
                return new SuperSqlOutputVisitor(out);
            case athena:
                return new AthenaOutputVisitor(out);
            case clickhouse:
                return new CKOutputVisitor(out);
            case oscar:
                return new OscarOutputVisitor(out);
            case dm:
                return new DmOutputVisitor(out);
            case starrocks:
                return new StarRocksOutputVisitor(out);
            case bigquery:
                return new BigQueryOutputVisitor(out);
            case impala:
                return new ImpalaOutputVisitor(out);
            case doris:
                return new DorisOutputVisitor(out);
            case teradata:
                return new TDOutputVisitor(out);
            case sqlite:
                return new SQLiteOutputVisitor(out);
            default:
                return new SQLASTOutputVisitor(out, dbType);
        }
    }

    @Deprecated
    public static SchemaStatVisitor createSchemaStatVisitor(List<SQLStatement> statementList, DbType dbType) {
        // TODO
        return null;
    }

    public static SchemaStatVisitor createSchemaStatVisitor(DbType dbType) {
        // TODO
        return null;
    }

    public static SchemaStatVisitor createSchemaStatVisitor(SchemaRepository repository) {
        // TODO
        return null;
    }

    public static SchemaStatVisitor createSchemaStatVisitor(SchemaRepository repository, DbType dbType) {
        // TODO
        return null;
    }

    public static List<SQLStatement> parseStatements(String sql, String dbType, SQLParserFeature... features) {
        // TODO
        return null;
    }

    public static List<SQLStatement> parseStatements(String sql, DbType dbType, SQLParserFeature... features) {
        // TODO
        return null;
    }

    public static List<SQLStatement> parseStatements(String sql, DbType dbType, boolean keepComments) {
        // TODO
        return null;
    }

    public static List<SQLStatement> parseStatements(String sql, String dbType) {
        // TODO
        return null;
    }

    public static List<SQLStatement> parseStatements(String sql, DbType dbType) {
        // TODO
        return null;
    }

    public static SQLStatement parseSingleStatement(String sql, DbType dbType, boolean keepComments) {
        // TODO
        return null;
    }

    public static SQLStatement parseSingleStatement(String sql, String dbType, SQLParserFeature... features) {
        // TODO
        return null;
    }

    public static SQLStatement parseSingleStatement(String sql, DbType dbType, SQLParserFeature... features) {
        // TODO
        return null;
    }

    public static SQLStatement parseSingleMysqlStatement(String sql) {
        // TODO
        return null;
    }

    /**
     * @param columnName
     * @param tableAlias
     * @param pattern    if pattern is null,it will be set {%Y-%m-%d %H:%i:%s} as mysql default value and set {yyyy-mm-dd
     *                   hh24:mi:ss} as oracle default value
     * @param dbType     {@link DbType} if dbType is null ,it will be set the mysql as a default value
     * @author owenludong.lud
     */
    public static String buildToDate(String columnName, String tableAlias, String pattern, DbType dbType) {
        StringBuilder sql = new StringBuilder();
        if (StringUtils.isEmpty(columnName)) {
            return "";
        }
        if (dbType == null) {
            dbType = DbType.mysql;
        }
        String formatMethod = "";
        if (JdbcUtils.isMysqlDbType(dbType)) {
            formatMethod = "STR_TO_DATE";
            if (StringUtils.isEmpty(pattern)) {
                pattern = "%Y-%m-%d %H:%i:%s";
            }
        } else if (DbType.oracle == dbType) {
            formatMethod = "TO_DATE";
            if (StringUtils.isEmpty(pattern)) {
                pattern = "yyyy-mm-dd hh24:mi:ss";
            }
        } else {
            return "";
            // expand date's handle method for other database
        }
        sql.append(formatMethod).append("(");
        if (!StringUtils.isEmpty(tableAlias)) {
            sql.append(tableAlias).append(".");
        }
        sql.append(columnName).append(",");
        sql.append("'");
        sql.append(pattern);
        sql.append("')");
        return sql.toString();
    }

    public static List<SQLExpr> split(SQLBinaryOpExpr x) {
        return SQLBinaryOpExpr.split(x);
    }

    public static String translateOracleToMySql(String sql) {
        // TODO
        return null;
    }

    public static String addCondition(String sql, String condition, DbType dbType) {
        // TODO
        return null;
    }

    public static String addCondition(String sql, String condition, SQLBinaryOperator op, boolean left, DbType dbType) {
        // TODO
        return null;
    }

    public static void addCondition(SQLStatement stmt, SQLBinaryOperator op, SQLExpr condition, boolean left) {
        // TODO
    }

    public static SQLExpr buildCondition(SQLBinaryOperator op, SQLExpr condition, boolean left, SQLExpr where) {
        if (where == null) {
            return condition;
        }
        SQLBinaryOpExpr newCondition;
        if (left) {
            newCondition = new SQLBinaryOpExpr(condition, op, where);
        } else {
            newCondition = new SQLBinaryOpExpr(where, op, condition);
        }
        return newCondition;
    }

    public static String addSelectItem(String selectSql, String expr, String alias, DbType dbType) {
        // TODO
        return null;
    }

    public static void acceptBooleanOr(String sql, DbType dbType, Consumer<SQLBinaryOpExprGroup> consumer) {
        acceptBinaryOpExprGroup(sql, dbType, consumer, e -> e.getOperator() == SQLBinaryOperator.BooleanOr);
    }

    public static void acceptBinaryOpExprGroup(String sql, DbType dbType, Consumer<SQLBinaryOpExprGroup> consumer, Predicate<SQLBinaryOpExprGroup> filter) {
        if (sql == null || sql.isEmpty()) {
            return;
        }
        List<SQLStatement> stmtList = new ArrayList<>();
        try {
            SQLStatementParser parser = SQLParserUtils.createSQLStatementParser(sql, dbType, SQLParserFeature.EnableMultiUnion, SQLParserFeature.KeepComments, SQLParserFeature.EnableSQLBinaryOpExprGroup);
            parser.parseStatementList(stmtList, -1, null);
        } catch (Exception ignored) {
            return;
        }
        SQLASTVisitor visitor;
        switch(dbType) {
            case odps:
                visitor = new OdpsASTVisitorAdapter() {

                    @Override
                    public boolean visit(SQLBinaryOpExprGroup x) {
                        if (filter == null || filter.test(x)) {
                            consumer.accept(x);
                        }
                        return super.visit(x);
                    }
                };
                break;
            default:
                visitor = new SQLASTVisitorAdapter() {

                    @Override
                    public boolean visit(SQLBinaryOpExprGroup x) {
                        if (filter == null || filter.test(x)) {
                            consumer.accept(x);
                        }
                        return super.visit(x);
                    }
                };
                break;
        }
        for (SQLStatement stmt : stmtList) {
            stmt.accept(visitor);
        }
    }

    public static void acceptBinaryOpExpr(String sql, DbType dbType, Consumer<SQLBinaryOpExpr> consumer, Predicate<SQLBinaryOpExpr> filter) {
        if (sql == null || sql.isEmpty()) {
            return;
        }
        List<SQLStatement> stmtList = new ArrayList<>();
        try {
            SQLStatementParser parser = SQLParserUtils.createSQLStatementParser(sql, dbType, SQLParserFeature.EnableMultiUnion, SQLParserFeature.KeepComments, SQLParserFeature.EnableSQLBinaryOpExprGroup);
            parser.parseStatementList(stmtList, -1, null);
        } catch (Exception ignored) {
            return;
        }
        SQLASTVisitor visitor;
        switch(dbType) {
            case odps:
                visitor = new OdpsASTVisitorAdapter() {

                    @Override
                    public boolean visit(SQLBinaryOpExpr x) {
                        if (filter == null || filter.test(x)) {
                            consumer.accept(x);
                        }
                        return super.visit(x);
                    }
                };
                break;
            default:
                visitor = new SQLASTVisitorAdapter() {

                    @Override
                    public boolean visit(SQLBinaryOpExpr x) {
                        if (filter == null || filter.test(x)) {
                            consumer.accept(x);
                        }
                        return super.visit(x);
                    }
                };
                break;
        }
        for (SQLStatement stmt : stmtList) {
            stmt.accept(visitor);
        }
    }

    public static void acceptTableSource(String sql, DbType dbType, Consumer<SQLTableSource> consumer, Predicate<SQLTableSource> filter) {
        // TODO
    }

    public static void acceptSelectQueryBlock(String sql, DbType dbType, Consumer<SQLSelectQueryBlock> consumer, Predicate<SQLSelectQueryBlock> filter) {
        if (sql == null || sql.isEmpty()) {
            return;
        }
        List<SQLStatement> stmtList = new ArrayList<>();
        try {
            SQLStatementParser parser = SQLParserUtils.createSQLStatementParser(sql, dbType, SQLParserFeature.EnableMultiUnion, SQLParserFeature.KeepComments, SQLParserFeature.EnableSQLBinaryOpExprGroup);
            parser.parseStatementList(stmtList, -1, null);
        } catch (Exception ignored) {
            return;
        }
        SQLASTVisitor visitor;
        switch(dbType) {
            case odps:
                visitor = new OdpsASTVisitorAdapter() {

                    @Override
                    public boolean visit(SQLSelectQueryBlock x) {
                        if (filter == null || filter.test(x)) {
                            consumer.accept(x);
                        }
                        return true;
                    }

                    @Override
                    public boolean visit(OdpsSelectQueryBlock x) {
                        if (filter == null || filter.test(x)) {
                            consumer.accept(x);
                        }
                        return true;
                    }
                };
                break;
            case mysql:
                visitor = new MySqlASTVisitorAdapter() {

                    @Override
                    public boolean visit(SQLSelectQueryBlock x) {
                        if (filter == null || filter.test(x)) {
                            consumer.accept(x);
                        }
                        return true;
                    }

                    @Override
                    public boolean visit(MySqlSelectQueryBlock x) {
                        if (filter == null || filter.test(x)) {
                            consumer.accept(x);
                        }
                        return true;
                    }
                };
                break;
            default:
                visitor = new SQLASTVisitorAdapter() {

                    @Override
                    public boolean visit(SQLSelectQueryBlock x) {
                        if (filter == null || filter.test(x)) {
                            consumer.accept(x);
                        }
                        return true;
                    }
                };
                break;
        }
        for (SQLStatement stmt : stmtList) {
            stmt.accept(visitor);
        }
    }

    public static void acceptAggregateFunction(String sql, DbType dbType, Consumer<SQLAggregateExpr> consumer, Predicate<SQLAggregateExpr> filter) {
        // TODO
    }

    public static void acceptFunction(String sql, DbType dbType, Consumer<SQLMethodInvokeExpr> consumer, Predicate<SQLMethodInvokeExpr> filter) {
        // TODO
    }

    public static void acceptInsertInto(String sql, DbType dbType, Consumer<SQLInsertInto> consumer, Predicate<SQLInsertInto> filter) {
        if (sql == null || sql.isEmpty()) {
            return;
        }
        List<SQLStatement> stmtList = new ArrayList<>();
        try {
            SQLStatementParser parser = SQLParserUtils.createSQLStatementParser(sql, dbType, SQLParserFeature.EnableMultiUnion, SQLParserFeature.KeepComments, SQLParserFeature.EnableSQLBinaryOpExprGroup);
            parser.parseStatementList(stmtList, -1, null);
        } catch (Exception ignored) {
            return;
        }
        SQLASTVisitor visitor;
        switch(dbType) {
            case odps:
                visitor = new OdpsASTVisitorAdapter() {

                    @Override
                    public boolean visit(SQLSelectStatement x) {
                        return false;
                    }

                    @Override
                    public boolean visit(SQLCreateTableStatement x) {
                        return false;
                    }

                    @Override
                    public boolean visit(OdpsCreateTableStatement x) {
                        return false;
                    }

                    @Override
                    public boolean visit(SQLSelectQueryBlock x) {
                        return false;
                    }

                    @Override
                    public boolean visit(OdpsSelectQueryBlock x) {
                        return false;
                    }

                    @Override
                    public boolean visit(SQLInsertStatement x) {
                        if (filter == null || filter.test(x)) {
                            consumer.accept(x);
                        }
                        return false;
                    }

                    @Override
                    public boolean visit(HiveInsertStatement x) {
                        if (filter == null || filter.test(x)) {
                            consumer.accept(x);
                        }
                        return false;
                    }

                    @Override
                    public boolean visit(HiveInsert x) {
                        if (filter == null || filter.test(x)) {
                            consumer.accept(x);
                        }
                        return false;
                    }
                };
                break;
            case hive:
                visitor = new HiveASTVisitorAdapter() {

                    @Override
                    public boolean visit(SQLSelectStatement x) {
                        return false;
                    }

                    @Override
                    public boolean visit(SQLCreateTableStatement x) {
                        return false;
                    }

                    @Override
                    public boolean visit(SQLInsertStatement x) {
                        if (filter == null || filter.test(x)) {
                            consumer.accept(x);
                        }
                        return false;
                    }

                    @Override
                    public boolean visit(HiveInsertStatement x) {
                        if (filter == null || filter.test(x)) {
                            consumer.accept(x);
                        }
                        return false;
                    }

                    @Override
                    public boolean visit(HiveInsert x) {
                        if (filter == null || filter.test(x)) {
                            consumer.accept(x);
                        }
                        return false;
                    }
                };
                break;
            default:
                visitor = new SQLASTVisitorAdapter() {

                    @Override
                    public boolean visit(SQLSelectStatement x) {
                        return false;
                    }

                    @Override
                    public boolean visit(SQLCreateTableStatement x) {
                        return false;
                    }

                    @Override
                    public boolean visit(SQLInsertStatement x) {
                        if (filter == null || filter.test(x)) {
                            consumer.accept(x);
                        }
                        return true;
                    }
                };
                break;
        }
        for (SQLStatement stmt : stmtList) {
            stmt.accept(visitor);
        }
    }

    public static String addSelectItem(String selectSql, String expr, String alias, boolean first, DbType dbType) {
        // TODO
        return null;
    }

    public static void addSelectItem(SQLStatement stmt, SQLExpr expr, String alias, boolean first) {
        // TODO
    }

    public static void addSelectItem(SQLSelectQueryBlock queryBlock, SQLExpr expr, String alias, boolean first) {
        // TODO
    }

    public static class FormatOption {

        private int features = VisitorFeature.of(VisitorFeature.OutputUCase, VisitorFeature.OutputPrettyFormat);

        public FormatOption() {
            // TODO
        }

        public FormatOption(VisitorFeature... features) {
            // TODO
        }

        public FormatOption(boolean ucase) {
            this(ucase, true);
            // TODO
        }

        public FormatOption(boolean ucase, boolean prettyFormat) {
            this(ucase, prettyFormat, false);
            // TODO
        }

        public FormatOption(boolean ucase, boolean prettyFormat, boolean parameterized) {
            // TODO
        }

        public boolean isDesensitize() {
            return isEnabled(VisitorFeature.OutputDesensitize);
        }

        public void setDesensitize(boolean val) {
            // TODO
        }

        public boolean isUppCase() {
            return isEnabled(VisitorFeature.OutputUCase);
        }

        public void setUppCase(boolean val) {
            // TODO
        }

        public boolean isPrettyFormat() {
            return isEnabled(VisitorFeature.OutputPrettyFormat);
        }

        public void setPrettyFormat(boolean prettyFormat) {
            // TODO
        }

        public boolean isParameterized() {
            return isEnabled(VisitorFeature.OutputParameterized);
        }

        public void setParameterized(boolean parameterized) {
            // TODO
        }

        public void config(VisitorFeature feature, boolean state) {
            features = VisitorFeature.config(features, feature, state);
        }

        public void configTo(SQLASTOutputVisitor v) {
            v.setFeatures(features);
        }

        public final boolean isEnabled(VisitorFeature feature) {
            return VisitorFeature.isEnabled(this.features, feature);
        }
    }

    public static String refactor(String sql, DbType dbType, Map<String, String> tableMapping) {
        // TODO
        return null;
    }

    public static long hash(String sql, DbType dbType) {
        Lexer lexer = SQLParserUtils.createLexer(sql, dbType);
        StringBuilder buf = new StringBuilder(sql.length());
        for (; ; ) {
            lexer.nextToken();
            Token token = lexer.token();
            if (token == Token.EOF) {
                break;
            }
            if (token == Token.ERROR) {
                return Utils.fnv_64(sql);
            }
            if (buf.length() != 0) {
                // skip
            }
        }
        return buf.hashCode();
    }

    public static SQLExpr not(SQLExpr expr) {
        if (expr instanceof SQLBinaryOpExpr) {
            SQLBinaryOpExpr binaryOpExpr = (SQLBinaryOpExpr) expr;
            SQLBinaryOperator op = binaryOpExpr.getOperator();
            SQLBinaryOperator notOp = null;
            switch(op) {
                case Equality:
                    notOp = SQLBinaryOperator.LessThanOrGreater;
                    break;
                case LessThanOrEqualOrGreaterThan:
                    notOp = SQLBinaryOperator.Equality;
                    break;
                case LessThan:
                    notOp = SQLBinaryOperator.GreaterThanOrEqual;
                    break;
                case LessThanOrEqual:
                    notOp = SQLBinaryOperator.GreaterThan;
                    break;
                case GreaterThan:
                    notOp = SQLBinaryOperator.LessThanOrEqual;
                    break;
                case GreaterThanOrEqual:
                    notOp = SQLBinaryOperator.LessThan;
                    break;
                case Is:
                    notOp = SQLBinaryOperator.IsNot;
                    break;
                case IsNot:
                    notOp = SQLBinaryOperator.Is;
                    break;
                default:
                    break;
            }
            if (notOp != null) {
                return new SQLBinaryOpExpr(binaryOpExpr.getLeft(), notOp, binaryOpExpr.getRight());
            }
        }
        if (expr instanceof SQLInListExpr) {
            SQLInListExpr inListExpr = (SQLInListExpr) expr;
            SQLInListExpr newInListExpr = new SQLInListExpr(inListExpr);
            newInListExpr.getTargetList().addAll(inListExpr.getTargetList());
            newInListExpr.setNot(!inListExpr.isNot());
            return newInListExpr;
        }
        return new SQLUnaryExpr(SQLUnaryOperator.Not, expr);
    }

    public static String normalize(String name) {
        return normalize(name, null);
    }

    public static String normalize(String name, boolean isTrimmed) {
        return _normalize(name, null, false, isTrimmed);
    }

    public static String normalize(String name, DbType dbType) {
        return _normalize(name, dbType, false);
    }

    private static String _normalize(String name, DbType dbType, boolean isForced) {
        return _normalize(name, dbType, isForced, true);
    }

    private static String _normalize(String name, DbType dbType, boolean isForced, boolean isTrimmed) {
        if (name == null) {
            return null;
        }
        if (name.length() > 2) {
            char c0 = name.charAt(0);
            char x0 = name.charAt(name.length() - 1);
            if ((c0 == '[' && x0 == ']') || (c0 == '"' && x0 == '"') || (c0 == '`' && x0 == '`') || (c0 == '\'' && x0 == '\'')) {
                String normalizeName = name.substring(1, name.length() - 1);
                if (isTrimmed) {
                    normalizeName = normalizeName.trim();
                }
                int dotIndex = normalizeName.indexOf('.');
                if (dotIndex > 0) {
                    if (c0 == '`') {
                        normalizeName = normalizeName.replaceAll("`\\.`", ".");
                    }
                }
                if (!isForced) {
                    if (DbType.oracle == dbType) {
                        if (OracleUtils.isKeyword(normalizeName)) {
                            return name;
                        }
                    } else if (JdbcUtils.isMysqlDbType(dbType)) {
                        if (MySqlUtils.isKeyword(normalizeName)) {
                            return name;
                        }
                    } else if (DbType.postgresql == dbType || DbType.db2 == dbType) {
                        if (PGUtils.isKeyword(normalizeName)) {
                            return name;
                        }
                    }
                }
                return normalizeName;
            }
        }
        return name;
    }

    public static String forcedNormalize(String name, DbType dbType) {
        return _normalize(name, dbType, true);
    }

    public static boolean nameEquals(SQLName a, SQLName b) {
        if (a == b) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        return a.nameHashCode64() == b.nameHashCode64();
    }

    public static boolean nameEquals(String a, String b) {
        if (a == b) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        if (a.equalsIgnoreCase(b)) {
            return true;
        }
        String normalize_a = normalize(a);
        String normalize_b = normalize(b);
        return normalize_a.equalsIgnoreCase(normalize_b);
    }

    public static boolean isValue(SQLExpr expr) {
        if (expr instanceof SQLLiteralExpr) {
            return true;
        }
        if (expr instanceof SQLVariantRefExpr) {
            return true;
        }
        if (expr instanceof SQLBinaryOpExpr) {
            SQLBinaryOpExpr binaryOpExpr = (SQLBinaryOpExpr) expr;
            SQLBinaryOperator op = binaryOpExpr.getOperator();
            if (op == SQLBinaryOperator.Add || op == SQLBinaryOperator.Subtract || op == SQLBinaryOperator.Multiply) {
                return isValue(binaryOpExpr.getLeft()) && isValue(binaryOpExpr.getRight());
            }
        }
        return false;
    }

    public static boolean replaceInParent(SQLDataType expr, SQLDataType target) {
        // TODO
        return false;
    }

    public static boolean replaceInParent(SQLExpr expr, SQLExpr target) {
        // TODO
        return false;
    }

    public static boolean replaceInParent(SQLSelect cmp, SQLSelect dest) {
        // TODO
        return false;
    }

    public static boolean replaceInParent(SQLTableSource cmp, SQLTableSource dest) {
        // TODO
        return false;
    }

    public static boolean replaceInParent(SQLSelectQuery cmp, SQLSelectQuery dest) {
        // TODO
        return false;
    }

    public static boolean replaceInParent(SQLStatement cmp, SQLStatement dest) {
        // TODO
        return false;
    }

    public static String desensitizeTable(String tableName) {
        // TODO
        return null;
    }

    /**
     * 重新排序建表语句，解决建表语句的依赖关系
     *
     * @param sql
     * @param dbType
     */
    public static String sort(String sql, DbType dbType) {
        // TODO
        return null;
    }

    /**
     * @param query
     * @param dbType
     * @return 0：sql.toString, 1:
     */
    public static Object[] clearLimit(String query, DbType dbType) {
        // TODO
        return null;
    }

    private static SQLLimit clearLimit(SQLSelectQueryBlock queryBlock) {
        // TODO
        return null;
    }

    public static SQLLimit getLimit(SQLStatement statement, DbType dbType) {
        // TODO
        return null;
    }

    public static SQLLimit getLimit(String query, DbType dbType) {
        // TODO
        return null;
    }

    public static String convertTimeZone(String sql, TimeZone from, TimeZone to) {
        SQLStatement statement = parseSingleMysqlStatement(sql);
        statement.accept(new TimeZoneVisitor(from, to));
        return statement.toString();
    }

    public static SQLStatement convertTimeZone(SQLStatement stmt, TimeZone from, TimeZone to) {
        stmt.accept(new TimeZoneVisitor(from, to));
        return stmt;
    }

    public static List<SQLInsertStatement> splitInsertValues(DbType dbType, String insertSql, int size) {
        SQLStatement statement = SQLUtils.parseStatements(insertSql, dbType, false).get(0);
        if (!(statement instanceof SQLInsertStatement)) {
            throw new IllegalArgumentException("The SQL must be insert statement.");
        }
        List<SQLInsertStatement> insertLists = new ArrayList<SQLInsertStatement>();
        SQLInsertStatement insertStatement = (SQLInsertStatement) statement;
        List<SQLInsertStatement.ValuesClause> valuesList = insertStatement.getValuesList();
        int totalSize = valuesList.size();
        if (totalSize <= size) {
            insertLists.add(insertStatement);
        } else {
            SQLInsertStatement insertTemplate = new SQLInsertStatement();
            insertStatement.cloneTo(insertTemplate);
            insertTemplate.getValuesList().clear();
            int batchCount = 0;
            if (totalSize % size == 0) {
                batchCount = totalSize / size;
            } else {
                batchCount = (totalSize / size) + 1;
            }
            for (int i = 0; i < batchCount; i++) {
                SQLInsertStatement subInsertStatement = new SQLInsertStatement();
                insertTemplate.cloneTo(subInsertStatement);
                int fromIndex = i * size;
                int toIndex = (fromIndex + size) > totalSize ? totalSize : fromIndex + size;
                List<SQLInsertStatement.ValuesClause> subValuesList = valuesList.subList(fromIndex, toIndex);
                subInsertStatement.getValuesList().addAll(subValuesList);
                insertLists.add(subInsertStatement);
            }
        }
        return insertLists;
    }

    public static boolean isQuoteChar(char c) {
        return c == '`' || c == '\'' || c == '"';
    }

    public static String removeQuote(String str) {
        return str.replace("`", "").replace("'", "").replace("\"", "");
    }

    /**
     * TokenInfo class to hold token, its corresponding string value, and position
     */
    public static class TokenInfo {

        private final Token token;

        private final String stringVal;

        private final int pos;

        public TokenInfo(Token token, String stringVal, int pos) {
            this.token = token;
            this.stringVal = stringVal;
            this.pos = pos;
        }

        public Token getToken() {
            // TODO
            return null;
        }

        public String getStringVal() {
            // TODO
            return null;
        }

        public int getPos() {
            // TODO
            return 0;
        }

        @Override
        public String toString() {
            if (stringVal == null) {
                return token + "@" + pos;
            }
            return token + "(" + stringVal + ")@" + pos;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            TokenInfo tokenInfo = (TokenInfo) o;
            if (token != tokenInfo.token) {
                return false;
            }
            if (pos != tokenInfo.pos) {
                return false;
            }
            return stringVal != null ? stringVal.equals(tokenInfo.stringVal) : tokenInfo.stringVal == null;
        }

        @Override
        public int hashCode() {
            int result = token != null ? token.hashCode() : 0;
            result = 31 * result + (stringVal != null ? stringVal.hashCode() : 0);
            result = 31 * result + pos;
            return result;
        }
    }

    /**
     * Get all tokens from SQL parsing based on dialect type, including their string values
     *
     * @param sql    SQL statement to parse
     * @param dbType Database type (dialect)
     * @return List of TokenInfo objects containing token and corresponding stringVal pairs (excluding comments)
     */
    public static List<TokenInfo> getAllTokens(String sql, DbType dbType) {
        // TODO
        return null;
    }

    /**
     * Get all tokens from SQL parsing based on dialect type, including their string values
     *
     * @param sql    SQL statement to parse
     * @param dbType Database type (dialect)
     * @param keepComments Whether to keep comment tokens (LINE_COMMENT, MULTI_LINE_COMMENT, HINT)
     * @return List of TokenInfo objects containing token and corresponding stringVal pairs
     */
    public static List<TokenInfo> getAllTokens(String sql, DbType dbType, boolean keepComments) {
        // TODO
        return null;
    }

    /**
     * Create a TokenInfo object from the current lexer state
     */
    private static TokenInfo createTokenInfo(Token token, Lexer lexer) {
        int pos = lexer.pos();
        String stringVal;
        // Get stringVal - use numberString() for numeric literals, stringVal() for others
        if (token == Token.LITERAL_INT || token == Token.LITERAL_FLOAT || token == Token.LITERAL_HEX) {
            stringVal = lexer.numberString();
        } else {
            stringVal = lexer.stringVal();
        }
        // Create a defensive copy to prevent mutation
        if (stringVal != null) {
            stringVal = new String(stringVal.toCharArray());
        }
        return new TokenInfo(token, stringVal, pos);
    }

    /**
     * Get all tokens from SQL parsing based on dialect type (overloaded method with String dbType)
     *
     * @param sql    SQL statement to parse
     * @param dbType Database type string (will be converted to DbType)
     * @return List of TokenInfo objects containing token and corresponding stringVal pairs
     */
    public static List<TokenInfo> getAllTokens(String sql, String dbType) {
        // TODO
        return null;
    }

    /**
     * Get all tokens from SQL parsing based on dialect type (overloaded method with String dbType)
     *
     * @param sql    SQL statement to parse
     * @param dbType Database type string (will be converted to DbType)
     * @param keepComments Whether to keep comment tokens (LINE_COMMENT, MULTI_LINE_COMMENT, HINT)
     * @return List of TokenInfo objects containing token and corresponding stringVal pairs
     */
    public static List<TokenInfo> getAllTokens(String sql, String dbType, boolean keepComments) {
        // TODO
        return null;
    }

    /**
     * Calculate Levenshtein distance between two SQL statements based on their token sequences.
     * For IDENTIFIER, PROPERTY, and LITERAL_INT tokens, the stringVal must also match for tokens to be considered equal.
     *
     * @param sql1 First SQL statement
     * @param dbType1 Database type for first SQL
     * @param sql2 Second SQL statement
     * @param dbType2 Database type for second SQL
     * @return Levenshtein distance between the two token sequences
     */
    public static int calculateTokenLevenshteinDistance(String sql1, DbType dbType1, String sql2, DbType dbType2) {
        // TODO
        return 0;
    }

    /**
     * Calculate Levenshtein distance between two token sequences
     */
    private static int calculateLevenshteinDistance(List<TokenInfo> tokens1, List<TokenInfo> tokens2) {
        int len1 = tokens1.size();
        int len2 = tokens2.size();
        // Create DP matrix
        int[][] dp = new int[len1 + 1][len2 + 1];
        // Initialize base cases
        for (int i = 0; i <= len1; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= len2; j++) {
            dp[0][j] = j;
        }
        // Fill DP matrix
        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                TokenInfo tokenInfo1 = tokens1.get(i - 1);
                TokenInfo tokenInfo2 = tokens2.get(j - 1);
                int cost;
                if (areTokensEqual(tokenInfo1, tokenInfo2)) {
                    // Tokens are equal
                    cost = 0;
                } else {
                    // Tokens are different
                    cost = 1;
                }
                dp[i][j] = Math.min(Math.min(// Deletion
                dp[i - 1][j] + 1, // Insertion
                dp[i][j - 1] + 1), // Substitution
                dp[i - 1][j - 1] + cost);
            }
        }
        return dp[len1][len2];
    }

    /**
     * Check if two tokens are equal.
     * For IDENTIFIER, PROPERTY, and LITERAL_INT tokens, stringVal must also match.
     */
    private static boolean areTokensEqual(TokenInfo tokenInfo1, TokenInfo tokenInfo2) {
        // First check if token types are the same
        if (tokenInfo1.getToken() != tokenInfo2.getToken()) {
            return false;
        }
        // For IDENTIFIER, PROPERTY, and LITERAL_INT tokens, check stringVal
        if (tokenInfo1.getToken() == Token.IDENTIFIER || tokenInfo1.getToken() == Token.LITERAL_INT || tokenInfo1.getToken() == Token.LITERAL_FLOAT || tokenInfo1.getToken() == Token.LITERAL_CHARS || tokenInfo1.getToken() == Token.LITERAL_NCHARS || tokenInfo1.getToken() == Token.LITERAL_HEX) {
            String stringVal1 = tokenInfo1.getStringVal();
            String stringVal2 = tokenInfo2.getStringVal();
            // Both should have string values
            if (stringVal1 == null && stringVal2 == null) {
                return true;
            }
            if (stringVal1 == null || stringVal2 == null) {
                return false;
            }
            return stringVal1.equals(stringVal2);
        }
        // For other token types, just matching the token type is enough
        return true;
    }

    /**
     * Helper class to hold token sequence and string value map
     */
    static class TimeZoneVisitor extends SQLASTVisitorAdapter {

        private TimeZone from;

        private TimeZone to;

        public TimeZoneVisitor(TimeZone from, TimeZone to) {
            this.from = from;
            this.to = to;
        }

        public boolean visit(SQLTimestampExpr x) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String newTime = format.format(x.getDate(from));
            x.setValue(newTime);
            return true;
        }
    }
}
