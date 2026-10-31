/*
 * Copyright 1999-2017 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License") {
        return true;
    }
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

import com.alibaba.druid.sql.ast.*;
import com.alibaba.druid.sql.ast.expr.*;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.bigquery.visitor.BigQueryVisitor;
import com.alibaba.druid.sql.dialect.hive.ast.HiveInputOutputFormat;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlKillStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.SQLAlterResourceGroupStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.SQLCreateResourceGroupStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.SQLListResourceGroupStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.dialect.odps.visitor.OdpsASTVisitor;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alibaba.druid.sql.dialect.postgresql.visitor.PGASTVisitor;
import com.alibaba.druid.sql.dialect.starrocks.ast.StarRocksIndexDefinition;
import com.alibaba.druid.sql.dialect.starrocks.ast.statement.StarRocksCreateResourceStatement;
import com.alibaba.druid.sql.template.SQLSelectQueryTemplate;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface SQLASTVisitor {

    default void endVisit(SQLAllColumnExpr x) {
    }

    default void endVisit(SQLBetweenExpr x) {
    }

    default void endVisit(SQLBinaryOpExpr x) {
    }

    default void endVisit(SQLCaseExpr x) {
    }

    default void endVisit(SQLCaseExpr.Item x) {
    }

    default void endVisit(SQLCaseStatement x) {
    }

    default void endVisit(SQLCaseStatement.Item x) {
    }

    default void endVisit(SQLCharExpr x) {
    }

    default void endVisit(SQLIdentifierExpr x) {
    }

    default void endVisit(SQLInListExpr x) {
    }

    default void endVisit(SQLIntegerExpr x) {
    }

    default void endVisit(SQLSmallIntExpr x) {
    }

    default void endVisit(SQLBigIntExpr x) {
    }

    default void endVisit(SQLTinyIntExpr x) {
    }

    default void endVisit(SQLExistsExpr x) {
    }

    default void endVisit(SQLNCharExpr x) {
    }

    default void endVisit(SQLNotExpr x) {
    }

    default void endVisit(SQLNullExpr x) {
    }

    default void endVisit(SQLNumberExpr x) {
    }

    default void endVisit(SQLRealExpr x) {
    }

    default void endVisit(SQLPropertyExpr x) {
    }

    default void endVisit(SQLSelectGroupByClause x) {
    }

    default void endVisit(SQLSelectItem x) {
    }

    default void endVisit(SQLSelectStatement x) {
    }

    default void postVisit(SQLObject x) {
    }

    default void preVisit(SQLObject x) {
    }

    /**
     * Compatibility-first default entry for table-source visit paths.
     * Specific table-source visit methods delegate here unless overridden.
     */
    default boolean visitTableSource(SQLTableSource x) {
        return true;
    }

    /**
     * Compatibility-first default exit for table-source visit paths.
     * Specific table-source endVisit methods delegate here unless overridden.
     */
    default void endVisitTableSource(SQLTableSource x) {
    }

    default boolean visit(SQLAllColumnExpr x) {
        // TODO
        return false;
    }

    default boolean visit(SQLTableSourceImpl x) {
        // TODO
        return false;
    }

    default void endVisit(SQLTableSourceImpl x) {
        endVisitTableSource(x);
    }

    default boolean visit(SQLBetweenExpr x) {
        // TODO
        return false;
    }

    default boolean visit(SQLBinaryOpExpr x) {
        // TODO
        return false;
    }

    default boolean visit(SQLCaseExpr x) {
        // TODO
        return false;
    }

    default boolean visit(SQLCaseExpr.Item x) {
        // TODO
        return false;
    }

    default boolean visit(SQLCaseStatement x) {
        // TODO
        return false;
    }

    default boolean visit(SQLCaseStatement.Item x) {
        // TODO
        return false;
    }

    default boolean visit(SQLCastExpr x) {
        // TODO
        return false;
    }

    default boolean visit(SQLCharExpr x) {
        // TODO
        return false;
    }

    default boolean visit(SQLExistsExpr x) {
        // TODO
        return false;
    }

    default boolean visit(SQLIdentifierExpr x) {
        // TODO
        return false;
    }

    default boolean visit(SQLInListExpr x) {
        // TODO
        return false;
    }

    default boolean visit(SQLIntegerExpr x) {
        // TODO
        return false;
    }

    default boolean visit(SQLSmallIntExpr x) {
        // TODO
        return false;
    }

    default boolean visit(SQLBigIntExpr x) {
        // TODO
        return false;
    }

    default boolean visit(SQLTinyIntExpr x) {
        // TODO
        return false;
    }

    default boolean visit(SQLNCharExpr x) {
        // TODO
        return false;
    }

    default boolean visit(SQLNotExpr x) {
        // TODO
        return false;
    }

    default boolean visit(SQLNullExpr x) {
        // TODO
        return false;
    }

    default boolean visit(SQLNumberExpr x) {
        // TODO
        return false;
    }

    default boolean visit(SQLRealExpr x) {
        // TODO
        return false;
    }

    default boolean visit(SQLPropertyExpr x) {
        // TODO
        return false;
    }

    default boolean visit(SQLSelectGroupByClause x) {
        // TODO
        return false;
    }

    default boolean visit(SQLSelectItem x) {
        // TODO
        return false;
    }

    default boolean visit(SQLStructExpr x) {
        // TODO
        return false;
    }

    default void endVisit(SQLStructExpr x) {
    }

    default boolean visit(SQLAliasedExpr x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAliasedExpr x) {
    }

    default void endVisit(SQLCastExpr x) {
    }

    default boolean visit(SQLSelectStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAggregateExpr x) {
    }

    default boolean visit(SQLAggregateExpr x) {
        // TODO
        return false;
    }

    default boolean visit(SQLVariantRefExpr x) {
        // TODO
        return false;
    }

    default void endVisit(SQLVariantRefExpr x) {
    }

    default boolean visit(SQLQueryExpr x) {
        // TODO
        return false;
    }

    default void endVisit(SQLQueryExpr x) {
    }

    default boolean visit(SQLUnaryExpr x) {
        // TODO
        return false;
    }

    default void endVisit(SQLUnaryExpr x) {
    }

    default boolean visit(SQLHexExpr x) {
        // TODO
        return false;
    }

    default void endVisit(SQLHexExpr x) {
    }

    default boolean visit(SQLSelect x) {
        // TODO
        return false;
    }

    default void endVisit(SQLSelect select) {
    }

    default boolean visit(SQLSelectQueryBlock x) {
        // TODO
        return false;
    }

    default void endVisit(SQLSelectQueryBlock x) {
    }

    default boolean visit(SQLExprTableSource x) {
        // TODO
        return false;
    }

    default void endVisit(SQLExprTableSource x) {
        endVisitTableSource(x);
    }

    default boolean visit(SQLOrderBy x) {
        // TODO
        return false;
    }

    default void endVisit(SQLOrderBy x) {
    }

    default boolean visit(SQLZOrderBy x) {
        // TODO
        return false;
    }

    default void endVisit(SQLZOrderBy x) {
    }

    default boolean visit(SQLSelectOrderByItem x) {
        // TODO
        return false;
    }

    default void endVisit(SQLSelectOrderByItem x) {
    }

    default boolean visit(SQLDropTableStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLDropTableStatement x) {
    }

    default boolean visit(SQLCreateTableStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLCreateTableStatement x) {
    }

    default boolean visit(SQLColumnDefinition x) {
        // TODO
        return false;
    }

    default void endVisit(SQLColumnDefinition x) {
    }

    default boolean visit(SQLColumnDefinition.Identity x) {
        // TODO
        return false;
    }

    default void endVisit(SQLColumnDefinition.Identity x) {
    }

    default boolean visit(SQLDataType x) {
        // TODO
        return false;
    }

    default void endVisit(SQLDataType x) {
    }

    default boolean visit(SQLCharacterDataType x) {
        // TODO
        return false;
    }

    default void endVisit(SQLCharacterDataType x) {
    }

    default boolean visit(SQLDeleteStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLDeleteStatement x) {
    }

    default boolean visit(SQLCurrentOfCursorExpr x) {
        // TODO
        return false;
    }

    default void endVisit(SQLCurrentOfCursorExpr x) {
    }

    default boolean visit(SQLInsertStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLInsertStatement x) {
    }

    default boolean visit(SQLInsertStatement.ValuesClause x) {
        // TODO
        return false;
    }

    default void endVisit(SQLInsertStatement.ValuesClause x) {
    }

    default boolean visit(SQLUpdateSetItem x) {
        // TODO
        return false;
    }

    default void endVisit(SQLUpdateSetItem x) {
    }

    default boolean visit(SQLUpdateStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLUpdateStatement x) {
    }

    default boolean visit(SQLGetDiagnosticsStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLGetDiagnosticsStatement x) {
    }

    default boolean visit(SQLCreateViewStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLCreateViewStatement x) {
    }

    default boolean visit(SQLCreateViewStatement.Column x) {
        // TODO
        return false;
    }

    default void endVisit(SQLCreateViewStatement.Column x) {
    }

    default boolean visit(SQLNotNullConstraint x) {
        // TODO
        return false;
    }

    default void endVisit(SQLNotNullConstraint x) {
    }

    default void endVisit(SQLMethodInvokeExpr x) {
    }

    default boolean visit(SQLMethodInvokeExpr x) {
        // TODO
        return false;
    }

    default void endVisit(SQLUnionQuery x) {
    }

    default boolean visit(SQLUnionQuery x) {
        // TODO
        return false;
    }

    default void endVisit(SQLSetStatement x) {
    }

    default boolean visit(SQLSetStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAssignItem x) {
    }

    default boolean visit(SQLAssignItem x) {
        // TODO
        return false;
    }

    default void endVisit(SQLCallStatement x) {
    }

    default boolean visit(SQLCallStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLJoinTableSource x) {
        endVisitTableSource(x);
    }

    default boolean visit(SQLJoinTableSource x) {
        // TODO
        return false;
    }

    default void endVisit(SQLJoinTableSource.UDJ x) {
    }

    default boolean visit(SQLJoinTableSource.UDJ x) {
        // TODO
        return false;
    }

    default void endVisit(SQLSomeExpr x) {
    }

    default boolean visit(SQLSomeExpr x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAnyExpr x) {
    }

    default boolean visit(SQLAnyExpr x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAllExpr x) {
    }

    default boolean visit(SQLAllExpr x) {
        // TODO
        return false;
    }

    default void endVisit(SQLInSubQueryExpr x) {
    }

    default boolean visit(SQLInSubQueryExpr x) {
        // TODO
        return false;
    }

    default void endVisit(SQLListExpr x) {
    }

    default boolean visit(SQLListExpr x) {
        // TODO
        return false;
    }

    default void endVisit(SQLSubqueryTableSource x) {
        endVisitTableSource(x);
    }

    default boolean visit(SQLSubqueryTableSource x) {
        // TODO
        return false;
    }

    default void endVisit(SQLTruncateStatement x) {
    }

    default boolean visit(SQLTruncateStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLDefaultExpr x) {
    }

    default boolean visit(SQLDefaultExpr x) {
        // TODO
        return false;
    }

    default void endVisit(SQLCommentStatement x) {
    }

    default boolean visit(SQLCommentStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLBeginStatement x) {
    }

    default boolean visit(SQLBeginStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLUseStatement x) {
    }

    default boolean visit(SQLUseStatement x) {
        // TODO
        return false;
    }

    default boolean visit(SQLAlterTableAddColumn x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableAddColumn x) {
    }

    default boolean visit(SQLAlterTableDeleteByCondition x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableDeleteByCondition x) {
    }

    default boolean visit(SQLAlterTableModifyClusteredBy x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableModifyClusteredBy x) {
    }

    default boolean visit(SQLAlterTableDropColumnItem x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableDropColumnItem x) {
    }

    default boolean visit(SQLAlterTableDropIndex x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableDropIndex x) {
    }

    default boolean visit(SQLAlterTableGroupStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableGroupStatement x) {
    }

    default boolean visit(SQLAlterSystemSetConfigStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterSystemSetConfigStatement x) {
    }

    default boolean visit(SQLAlterSystemGetConfigStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterSystemGetConfigStatement x) {
    }

    default boolean visit(SQLDropIndexStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLDropIndexStatement x) {
    }

    default boolean visit(SQLDropViewStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLDropViewStatement x) {
    }

    default boolean visit(SQLSavePointStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLSavePointStatement x) {
    }

    default boolean visit(SQLRollbackStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLRollbackStatement x) {
    }

    default boolean visit(SQLReleaseSavePointStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLReleaseSavePointStatement x) {
    }

    default void endVisit(SQLCommentHint x) {
    }

    default boolean visit(SQLCommentHint x) {
        // TODO
        return false;
    }

    default void endVisit(SQLCreateDatabaseStatement x) {
    }

    default boolean visit(SQLCreateDatabaseStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLOver x) {
    }

    default boolean visit(SQLOver x) {
        // TODO
        return false;
    }

    default void endVisit(SQLKeep x) {
    }

    default boolean visit(SQLKeep x) {
        // TODO
        return false;
    }

    default void endVisit(SQLColumnPrimaryKey x) {
    }

    default boolean visit(SQLColumnPrimaryKey x) {
        // TODO
        return false;
    }

    default boolean visit(SQLColumnUniqueKey x) {
        // TODO
        return false;
    }

    default void endVisit(SQLColumnUniqueKey x) {
    }

    default void endVisit(SQLWithSubqueryClause x) {
    }

    default boolean visit(SQLWithSubqueryClause x) {
        // TODO
        return false;
    }

    default void endVisit(SQLWithSubqueryClause.Entry x) {
        endVisitTableSource(x);
    }

    default boolean visit(SQLWithSubqueryClause.Entry x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableAlterColumn x) {
    }

    default boolean visit(SQLAlterTableAlterColumn x) {
        // TODO
        return false;
    }

    default boolean visit(SQLCheck x) {
        // TODO
        return false;
    }

    default void endVisit(SQLCheck x) {
    }

    default boolean visit(SQLDefault x) {
        // TODO
        return false;
    }

    default void endVisit(SQLDefault x) {
    }

    default boolean visit(SQLAlterTableDropForeignKey x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableDropForeignKey x) {
    }

    default boolean visit(SQLAlterTableDropPrimaryKey x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableDropPrimaryKey x) {
    }

    default boolean visit(SQLAlterTableDisableKeys x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableDisableKeys x) {
    }

    default boolean visit(SQLAlterTableEnableKeys x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableEnableKeys x) {
    }

    default boolean visit(SQLAlterTableStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableStatement x) {
    }

    default boolean visit(SQLAlterTableDisableConstraint x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableDisableConstraint x) {
    }

    default boolean visit(SQLAlterTableEnableConstraint x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableEnableConstraint x) {
    }

    default boolean visit(SQLColumnCheck x) {
        // TODO
        return false;
    }

    default void endVisit(SQLColumnCheck x) {
    }

    default boolean visit(SQLColumnDefault x) {
        // TODO
        return false;
    }

    default void endVisit(SQLColumnDefault x) {
    }

    default boolean visit(SQLExprHint x) {
        // TODO
        return false;
    }

    default void endVisit(SQLExprHint x) {
    }

    default boolean visit(SQLAlterTableDropConstraint x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableDropConstraint x) {
    }

    default boolean visit(SQLAlterTableDropCheck x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableDropCheck x) {
    }

    default boolean visit(SQLAlterTableValidateConstraint x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableValidateConstraint x) {
    }

    default boolean visit(SQLUnique x) {
        // TODO
        return false;
    }

    default void endVisit(SQLUnique x) {
    }

    default boolean visit(SQLPrimaryKeyImpl x) {
        // TODO
        return false;
    }

    default void endVisit(SQLPrimaryKeyImpl x) {
    }

    default boolean visit(SQLCreateIndexStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLCreateIndexStatement x) {
    }

    default boolean visit(SQLAlterTableRenameColumn x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableRenameColumn x) {
    }

    default boolean visit(SQLAlterTableRenameConstraint x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableRenameConstraint x) {
    }

    default boolean visit(SQLColumnReference x) {
        // TODO
        return false;
    }

    default void endVisit(SQLColumnReference x) {
    }

    default boolean visit(SQLForeignKeyImpl x) {
        // TODO
        return false;
    }

    default void endVisit(SQLForeignKeyImpl x) {
    }

    default boolean visit(SQLDropSequenceStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLDropSequenceStatement x) {
    }

    default boolean visit(SQLDropTriggerStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLDropTriggerStatement x) {
    }

    default void endVisit(SQLDropUserStatement x) {
    }

    default boolean visit(SQLDropUserStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLExplainStatement x) {
    }

    default boolean visit(SQLExplainStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLGrantStatement x) {
    }

    default boolean visit(SQLGrantStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLDropDatabaseStatement x) {
    }

    default boolean visit(SQLDropDatabaseStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLIndexOptions x) {
    }

    default boolean visit(SQLIndexOptions x) {
        // TODO
        return false;
    }

    default void endVisit(SQLIndexDefinition x) {
    }

    default boolean visit(SQLIndexDefinition x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableAddIndex x) {
    }

    default boolean visit(SQLAlterTableAddIndex x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableAlterIndex x) {
    }

    default boolean visit(SQLAlterTableAlterIndex x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableAddConstraint x) {
    }

    default boolean visit(SQLAlterTableAddConstraint x) {
        // TODO
        return false;
    }

    default void endVisit(SQLCreateTriggerStatement x) {
    }

    default boolean visit(SQLCreateTriggerStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLDropFunctionStatement x) {
    }

    default boolean visit(SQLDropFunctionStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLDropTableSpaceStatement x) {
    }

    default boolean visit(SQLDropTableSpaceStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLDropProcedureStatement x) {
    }

    default boolean visit(SQLDropProcedureStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLBooleanExpr x) {
    }

    default boolean visit(SQLBooleanExpr x) {
        // TODO
        return false;
    }

    default void endVisit(SQLUnionQueryTableSource x) {
        endVisitTableSource(x);
    }

    default boolean visit(SQLUnionQueryTableSource x) {
        // TODO
        return false;
    }

    default void endVisit(SQLTimestampExpr x) {
    }

    default boolean visit(SQLTimestampExpr x) {
        // TODO
        return false;
    }

    default void endVisit(SQLTimestampNTZExpr x) {
    }

    default boolean visit(SQLTimestampNTZExpr x) {
        // TODO
        return false;
    }

    default void endVisit(SQLDateTimeExpr x) {
    }

    default boolean visit(SQLDateTimeExpr x) {
        // TODO
        return false;
    }

    default void endVisit(SQLDoubleExpr x) {
    }

    default boolean visit(SQLDoubleExpr x) {
        // TODO
        return false;
    }

    default void endVisit(SQLFloatExpr x) {
    }

    default boolean visit(SQLFloatExpr x) {
        // TODO
        return false;
    }

    default void endVisit(SQLRevokeStatement x) {
    }

    default boolean visit(SQLRevokeStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLBinaryExpr x) {
    }

    default boolean visit(SQLBinaryExpr x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableRename x) {
    }

    default boolean visit(SQLAlterTableRename x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterViewRenameStatement x) {
    }

    default boolean visit(SQLAlterViewRenameStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLShowTablesStatement x) {
    }

    default boolean visit(SQLShowTablesStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableAddPartition x) {
    }

    default boolean visit(SQLAlterTableAddPartition x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableAddExtPartition x) {
    }

    default boolean visit(SQLAlterTableAddExtPartition x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableDropExtPartition x) {
    }

    default boolean visit(SQLAlterTableDropExtPartition x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableDropPartition x) {
    }

    default boolean visit(SQLAlterTableDropPartition x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableRenamePartition x) {
    }

    default boolean visit(SQLAlterTableRenamePartition x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableSetComment x) {
    }

    default boolean visit(SQLAlterTableSetComment x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableSetTableSpace x) {
    }

    default boolean visit(SQLAlterTableSetTableSpace x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableSetSchema x) {
    }

    default boolean visit(SQLAlterTableSetSchema x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableSetLifecycle x) {
    }

    default boolean visit(SQLPrivilegeItem x) {
        // TODO
        return false;
    }

    default void endVisit(SQLPrivilegeItem x) {
    }

    default boolean visit(SQLAlterTableSetLifecycle x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableEnableLifecycle x) {
    }

    default boolean visit(SQLAlterTableSetLocation x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableSetLocation x) {
    }

    default boolean visit(SQLAlterTableEnableLifecycle x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTablePartition x) {
    }

    default boolean visit(SQLAlterTableArchive x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableArchive x) {
    }

    default boolean visit(SQLAlterTablePartition x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTablePartitionSetProperties x) {
    }

    default boolean visit(SQLAlterTablePartitionSetProperties x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableDisableLifecycle x) {
    }

    default boolean visit(SQLAlterTableDisableLifecycle x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableTouch x) {
    }

    default boolean visit(SQLAlterTableTouch x) {
        // TODO
        return false;
    }

    default void endVisit(SQLArrayExpr x) {
    }

    default boolean visit(SQLArrayExpr x) {
        // TODO
        return false;
    }

    default void endVisit(SQLOpenStatement x) {
    }

    default boolean visit(SQLOpenStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLFetchStatement x) {
    }

    default boolean visit(SQLFetchStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLCloseStatement x) {
    }

    default boolean visit(SQLCloseStatement x) {
        // TODO
        return false;
    }

    default boolean visit(SQLGroupingSetExpr x) {
        // TODO
        return false;
    }

    default void endVisit(SQLGroupingSetExpr x) {
    }

    default boolean visit(SQLIfStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLIfStatement x) {
    }

    default boolean visit(SQLIfStatement.ElseIf x) {
        // TODO
        return false;
    }

    default void endVisit(SQLIfStatement.ElseIf x) {
    }

    default boolean visit(SQLIfStatement.Else x) {
        // TODO
        return false;
    }

    default void endVisit(SQLIfStatement.Else x) {
    }

    default boolean visit(SQLLoopStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLLoopStatement x) {
    }

    default boolean visit(SQLParameter x) {
        // TODO
        return false;
    }

    default void endVisit(SQLParameter x) {
    }

    default boolean visit(SQLCreateProcedureStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLCreateProcedureStatement x) {
    }

    default boolean visit(SQLCreateFunctionStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLCreateFunctionStatement x) {
    }

    default boolean visit(SQLBlockStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLBlockStatement x) {
    }

    default boolean visit(SQLAlterTableDropKey x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableDropKey x) {
    }

    default boolean visit(SQLDeclareItem x) {
        // TODO
        return false;
    }

    default void endVisit(SQLDeclareItem x) {
    }

    default boolean visit(SQLPartitionValue x) {
        // TODO
        return false;
    }

    default void endVisit(SQLPartitionValue x) {
    }

    default boolean visit(SQLPartitionSingle x) {
        // TODO
        return false;
    }

    default void endVisit(SQLPartitionSingle x) {
    }

    default boolean visit(SQLPartitionBatch x) {
        // TODO
        return false;
    }

    default void endVisit(SQLPartitionBatch x) {
    }

    default boolean visit(SQLPartitionByRange x) {
        // TODO
        return false;
    }

    default void endVisit(SQLPartitionByRange x) {
    }

    default boolean visit(SQLPartitionByHash x) {
        // TODO
        return false;
    }

    default void endVisit(SQLPartitionByHash x) {
    }

    default boolean visit(SQLPartitionByList x) {
        // TODO
        return false;
    }

    default void endVisit(SQLPartitionByList x) {
    }

    default boolean visit(SQLPartitionOf x) {
        // TODO
        return false;
    }

    default void endVisit(SQLPartitionOf x) {
    }

    default boolean visit(SQLSubPartition x) {
        // TODO
        return false;
    }

    default void endVisit(SQLSubPartition x) {
    }

    default boolean visit(SQLSubPartitionByHash x) {
        // TODO
        return false;
    }

    default void endVisit(SQLSubPartitionByHash x) {
    }

    default boolean visit(SQLSubPartitionByRange x) {
        // TODO
        return false;
    }

    default void endVisit(SQLSubPartitionByRange x) {
    }

    default boolean visit(SQLSubPartitionByList x) {
        // TODO
        return false;
    }

    default void endVisit(SQLSubPartitionByList x) {
    }

    default boolean visit(SQLAlterDatabaseStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterDatabaseStatement x) {
    }

    default boolean visit(SQLAlterTableConvertCharSet x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableConvertCharSet x) {
    }

    default boolean visit(SQLAlterTableReOrganizePartition x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableReOrganizePartition x) {
    }

    default boolean visit(SQLAlterTableCoalescePartition x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableCoalescePartition x) {
    }

    default boolean visit(SQLAlterTableTruncatePartition x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableTruncatePartition x) {
    }

    default boolean visit(SQLAlterTableDiscardPartition x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableDiscardPartition x) {
    }

    default boolean visit(SQLAlterTableImportPartition x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableImportPartition x) {
    }

    default boolean visit(SQLAlterTableAnalyzePartition x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableAnalyzePartition x) {
    }

    default boolean visit(SQLAlterTableCheckPartition x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableCheckPartition x) {
    }

    default boolean visit(SQLAlterTableOptimizePartition x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableOptimizePartition x) {
    }

    default boolean visit(SQLAlterTableRebuildPartition x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableRebuildPartition x) {
    }

    default boolean visit(SQLAlterTableRepairPartition x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableRepairPartition x) {
    }

    default boolean visit(SQLSequenceExpr x) {
        // TODO
        return false;
    }

    default void endVisit(SQLSequenceExpr x) {
    }

    default boolean visit(SQLMergeStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLMergeStatement x) {
    }

    default boolean visit(SQLMergeStatement.WhenUpdate x) {
        // TODO
        return false;
    }

    default void endVisit(SQLMergeStatement.WhenUpdate x) {
    }

    default boolean visit(SQLMergeStatement.WhenInsert x) {
        // TODO
        return false;
    }

    default boolean visit(SQLMergeStatement.WhenDelete x) {
        // TODO
        return false;
    }

    default void endVisit(SQLMergeStatement.WhenDelete x) {
    }

    default boolean visit(SQLMergeStatement.WhenDoNothing x) {
        // TODO
        return false;
    }

    default void endVisit(SQLMergeStatement.WhenDoNothing x) {
    }

    default void endVisit(SQLMergeStatement.WhenInsert x) {
    }

    default boolean visit(SQLErrorLoggingClause x) {
        // TODO
        return false;
    }

    default void endVisit(SQLErrorLoggingClause x) {
    }

    default boolean visit(SQLNullConstraint x) {
        // TODO
        return false;
    }

    default void endVisit(SQLNullConstraint x) {
    }

    default boolean visit(SQLCreateSequenceStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLCreateSequenceStatement x) {
    }

    default boolean visit(SQLDateExpr x) {
        // TODO
        return false;
    }

    default void endVisit(SQLDateExpr x) {
    }

    default boolean visit(SQLLimit x) {
        // TODO
        return false;
    }

    default void endVisit(SQLLimit x) {
    }

    default void endVisit(SQLStartTransactionStatement x) {
    }

    default boolean visit(SQLStartTransactionStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLDescribeStatement x) {
    }

    default boolean visit(SQLDescribeStatement x) {
        // TODO
        return false;
    }

    /**
     * support procedure
     */
    default boolean visit(SQLWhileStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLWhileStatement x) {
    }

    default boolean visit(SQLDeclareStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLDeclareStatement x) {
    }

    default boolean visit(SQLReturnStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLReturnStatement x) {
    }

    default boolean visit(SQLArgument x) {
        // TODO
        return false;
    }

    default void endVisit(SQLArgument x) {
    }

    default boolean visit(SQLCommitStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLCommitStatement x) {
    }

    default boolean visit(SQLFlashbackExpr x) {
        // TODO
        return false;
    }

    default void endVisit(SQLFlashbackExpr x) {
    }

    default boolean visit(SQLCreateMaterializedViewStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLCreateMaterializedViewStatement x) {
    }

    default boolean visit(SQLShowCreateMaterializedViewStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLShowCreateMaterializedViewStatement x) {
    }

    default boolean visit(SQLBinaryOpExprGroup x) {
        // TODO
        return false;
    }

    default void endVisit(SQLBinaryOpExprGroup x) {
    }

    default boolean visit(SQLScriptCommitStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLScriptCommitStatement x) {
    }

    default boolean visit(SQLReplaceStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLReplaceStatement x) {
    }

    default boolean visit(SQLCreateUserStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLCreateUserStatement x) {
    }

    default boolean visit(SQLAlterFunctionStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterFunctionStatement x) {
    }

    default boolean visit(SQLAlterTypeStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTypeStatement x) {
    }

    default boolean visit(SQLIntervalExpr x) {
        // TODO
        return false;
    }

    default void endVisit(SQLIntervalExpr x) {
    }

    default boolean visit(SQLLateralViewTableSource x) {
        // TODO
        return false;
    }

    default void endVisit(SQLLateralViewTableSource x) {
        endVisitTableSource(x);
    }

    default boolean visit(SQLShowErrorsStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLShowErrorsStatement x) {
    }

    default boolean visit(SQLShowGrantsStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLShowGrantsStatement x) {
    }

    default boolean visit(SQLShowPackagesStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLShowPackagesStatement x) {
    }

    default boolean visit(SQLShowRecylebinStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLShowRecylebinStatement x) {
    }

    default boolean visit(SQLAlterCharacter x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterCharacter x) {
    }

    default boolean visit(SQLExprStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLExprStatement x) {
    }

    default boolean visit(SQLAlterProcedureStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterProcedureStatement x) {
    }

    default boolean visit(SQLAlterViewStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterViewStatement x) {
    }

    default boolean visit(SQLDropEventStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLDropEventStatement x) {
    }

    default boolean visit(SQLDropLogFileGroupStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLDropLogFileGroupStatement x) {
    }

    default boolean visit(SQLDropServerStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLDropServerStatement x) {
    }

    default boolean visit(SQLDropSynonymStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLDropSynonymStatement x) {
    }

    default boolean visit(SQLRecordDataType x) {
        // TODO
        return false;
    }

    default void endVisit(SQLRecordDataType x) {
    }

    default boolean visit(SQLDropTypeStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLDropTypeStatement x) {
    }

    default boolean visit(SQLExternalRecordFormat x) {
        // TODO
        return false;
    }

    default void endVisit(SQLExternalRecordFormat x) {
    }

    default boolean visit(SQLArrayDataType x) {
        // TODO
        return false;
    }

    default void endVisit(SQLArrayDataType x) {
    }

    default boolean visit(SQLMapDataType x) {
        // TODO
        return false;
    }

    default void endVisit(SQLMapDataType x) {
    }

    default boolean visit(SQLStructDataType x) {
        // TODO
        return false;
    }

    default void endVisit(SQLStructDataType x) {
    }

    default boolean visit(SQLRowDataType x) {
        // TODO
        return false;
    }

    default void endVisit(SQLRowDataType x) {
    }

    default boolean visit(SQLStructDataType.Field x) {
        // TODO
        return false;
    }

    default void endVisit(SQLStructDataType.Field x) {
    }

    default boolean visit(SQLDropMaterializedViewStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLDropMaterializedViewStatement x) {
    }

    default boolean visit(SQLShowMaterializedViewStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLShowMaterializedViewStatement x) {
    }

    default boolean visit(SQLRefreshMaterializedViewStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLRefreshMaterializedViewStatement x) {
    }

    default boolean visit(SQLAlterMaterializedViewStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterMaterializedViewStatement x) {
    }

    default boolean visit(SQLCreateTableGroupStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLCreateTableGroupStatement x) {
    }

    default boolean visit(SQLDropTableGroupStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLDropTableGroupStatement x) {
    }

    default boolean visit(SQLAlterTableSubpartitionAvailablePartitionNum x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableSubpartitionAvailablePartitionNum x) {
    }

    default void endVisit(SQLShowDatabasesStatement x) {
    }

    default boolean visit(SQLShowDatabasesStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLShowTableGroupsStatement x) {
    }

    default boolean visit(SQLShowTableGroupsStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLShowColumnsStatement x) {
    }

    default boolean visit(SQLShowColumnsStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLShowCreateTableStatement x) {
    }

    default boolean visit(SQLShowCreateTableStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLShowProcessListStatement x) {
    }

    default boolean visit(SQLShowProcessListStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableSetOption x) {
    }

    default boolean visit(SQLAlterTableSetOption x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableSetSerdeProperties x) {
    }

    default boolean visit(SQLAlterTableSetSerdeProperties x) {
        // TODO
        return false;
    }

    default boolean visit(SQLShowCreateViewStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLShowCreateViewStatement x) {
    }

    default boolean visit(SQLShowViewsStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLShowViewsStatement x) {
    }

    default boolean visit(SQLAlterTableRenameIndex x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableRenameIndex x) {
    }

    default boolean visit(SQLAlterSequenceStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterSequenceStatement x) {
    }

    default boolean visit(SQLAlterTableExchangePartition x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableExchangePartition x) {
    }

    default boolean visit(SQLCreateRoleStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLCreateRoleStatement x) {
    }

    default boolean visit(SQLDropRoleStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLDropRoleStatement x) {
    }

    default boolean visit(SQLAlterTableReplaceColumn x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableReplaceColumn x) {
    }

    default boolean visit(SQLMatchAgainstExpr x) {
        // TODO
        return false;
    }

    default void endVisit(SQLMatchAgainstExpr x) {
    }

    default boolean visit(SQLTimeExpr x) {
        // TODO
        return false;
    }

    default void endVisit(SQLTimeExpr x) {
    }

    default boolean visit(SQLDropCatalogStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLDropCatalogStatement x) {
    }

    default void endVisit(SQLShowPartitionsStmt x) {
    }

    default boolean visit(SQLShowPartitionsStmt x) {
        // TODO
        return false;
    }

    default void endVisit(SQLValuesExpr x) {
    }

    default boolean visit(SQLValuesExpr x) {
        // TODO
        return false;
    }

    default void endVisit(SQLContainsExpr x) {
    }

    default boolean visit(SQLContainsExpr x) {
        // TODO
        return false;
    }

    default void endVisit(SQLDumpStatement x) {
    }

    default boolean visit(SQLDumpStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLValuesTableSource x) {
        endVisitTableSource(x);
    }

    default boolean visit(SQLValuesTableSource x) {
        // TODO
        return false;
    }

    default void endVisit(SQLExtractExpr x) {
    }

    default boolean visit(SQLExtractExpr x) {
        // TODO
        return false;
    }

    default void endVisit(SQLWindow x) {
    }

    default boolean visit(SQLWindow x) {
        // TODO
        return false;
    }

    default void endVisit(SQLJSONExpr x) {
    }

    default boolean visit(SQLJSONExpr x) {
        // TODO
        return false;
    }

    default void endVisit(SQLDecimalExpr x) {
    }

    default boolean visit(SQLDecimalExpr x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAnnIndex x) {
    }

    default boolean visit(SQLAnnIndex x) {
        // TODO
        return false;
    }

    default void endVisit(SQLUnionDataType x) {
    }

    default boolean visit(SQLUnionDataType x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableRecoverPartitions x) {
    }

    default boolean visit(SQLAlterTableRecoverPartitions x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterIndexStatement x) {
    }

    default boolean visit(SQLAlterIndexStatement x) {
        // TODO
        return false;
    }

    default boolean visit(SQLAlterIndexStatement.Rebuild x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterIndexStatement.Rebuild x) {
    }

    default boolean visit(SQLShowIndexesStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLShowIndexesStatement x) {
    }

    default boolean visit(SQLAnalyzeTableStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAnalyzeTableStatement x) {
    }

    default boolean visit(SQLExportTableStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLExportTableStatement x) {
    }

    default boolean visit(SQLImportTableStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLImportTableStatement x) {
    }

    default boolean visit(SQLTableSampling x) {
        // TODO
        return false;
    }

    default void endVisit(SQLTableSampling x) {
    }

    default boolean visit(SQLSizeExpr x) {
        // TODO
        return false;
    }

    default void endVisit(SQLSizeExpr x) {
    }

    default boolean visit(SQLAlterTableArchivePartition x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableArchivePartition x) {
    }

    default boolean visit(SQLAlterTableUnarchivePartition x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableUnarchivePartition x) {
    }

    default boolean visit(SQLCreateOutlineStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLCreateOutlineStatement x) {
    }

    default boolean visit(SQLDropOutlineStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLDropOutlineStatement x) {
    }

    default boolean visit(SQLDropOfflineModelStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLDropOfflineModelStatement x) {
    }

    default boolean visit(SQLAlterOutlineStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterOutlineStatement x) {
    }

    default boolean visit(SQLShowOutlinesStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLShowOutlinesStatement x) {
    }

    default boolean visit(SQLPurgeTableStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLPurgeTableStatement x) {
    }

    default boolean visit(SQLPurgeTemporaryOutputStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLPurgeTemporaryOutputStatement x) {
    }

    default boolean visit(SQLPurgeLogsStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLPurgeLogsStatement x) {
    }

    default boolean visit(SQLPurgeRecyclebinStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLPurgeRecyclebinStatement x) {
    }

    default boolean visit(SQLShowStatisticStmt x) {
        // TODO
        return false;
    }

    default void endVisit(SQLShowStatisticStmt x) {
    }

    default boolean visit(SQLShowStatisticListStmt x) {
        // TODO
        return false;
    }

    default void endVisit(SQLShowStatisticListStmt x) {
    }

    default boolean visit(SQLAlterTableAddSupplemental x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableAddSupplemental x) {
    }

    default boolean visit(SQLShowCatalogsStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLShowCatalogsStatement x) {
    }

    default boolean visit(SQLShowFunctionsStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLShowFunctionsStatement x) {
    }

    default boolean visit(SQLShowSessionStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLShowSessionStatement x) {
    }

    default boolean visit(SQLDbLinkExpr x) {
        // TODO
        return false;
    }

    default void endVisit(SQLDbLinkExpr x) {
    }

    default boolean visit(SQLCurrentTimeExpr x) {
        // TODO
        return false;
    }

    default void endVisit(SQLCurrentTimeExpr x) {
    }

    default boolean visit(SQLCurrentUserExpr x) {
        // TODO
        return false;
    }

    default void endVisit(SQLCurrentUserExpr x) {
    }

    default boolean visit(SQLShowQueryTaskStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLShowQueryTaskStatement x) {
    }

    default boolean visit(SQLAdhocTableSource x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAdhocTableSource x) {
        endVisitTableSource(x);
    }

    default boolean visit(HiveInputOutputFormat x) {
        // TODO
        return false;
    }

    default void endVisit(HiveInputOutputFormat x) {
    }

    default boolean visit(SQLExplainAnalyzeStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLExplainAnalyzeStatement x) {
    }

    default boolean visit(SQLPartitionRef x) {
        // TODO
        return false;
    }

    default void endVisit(SQLPartitionRef x) {
    }

    default boolean visit(SQLPartitionRef.Item x) {
        // TODO
        return false;
    }

    default void endVisit(SQLPartitionRef.Item x) {
    }

    default boolean visit(SQLWhoamiStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLWhoamiStatement x) {
    }

    default boolean visit(SQLDropResourceStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLDropResourceStatement x) {
    }

    default boolean visit(SQLForStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLForStatement x) {
    }

    default boolean visit(SQLUnnestTableSource x) {
        // TODO
        return false;
    }

    default void endVisit(SQLUnnestTableSource x) {
        endVisitTableSource(x);
    }

    default boolean visit(SQLGeneratedTableSource x) {
        // TODO
        return false;
    }

    default void endVisit(SQLGeneratedTableSource x) {
        endVisitTableSource(x);
    }

    default boolean visit(SQLCopyFromStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLCopyFromStatement x) {
    }

    default boolean visit(SQLShowUsersStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLShowUsersStatement x) {
    }

    default boolean visit(SQLSubmitJobStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLSubmitJobStatement x) {
    }

    default boolean visit(SQLTableLike x) {
        // TODO
        return false;
    }

    default void endVisit(SQLTableLike x) {
    }

    default boolean visit(SQLSyncMetaStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLSyncMetaStatement x) {
    }

    default void endVisit(SQLValuesQuery x) {
    }

    default boolean visit(SQLValuesQuery x) {
        // TODO
        return false;
    }

    default void endVisit(SQLDataTypeRefExpr x) {
    }

    default boolean visit(SQLDataTypeRefExpr x) {
        // TODO
        return false;
    }

    default void endVisit(SQLArchiveTableStatement x) {
    }

    default boolean visit(SQLArchiveTableStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLBackupStatement x) {
    }

    default boolean visit(SQLBackupStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLRestoreStatement x) {
    }

    default boolean visit(SQLRestoreStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLBuildTableStatement x) {
    }

    default boolean visit(SQLBuildTableStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLCancelJobStatement x) {
    }

    default boolean visit(SQLCancelJobStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLExportDatabaseStatement x) {
    }

    default boolean visit(SQLExportDatabaseStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLImportDatabaseStatement x) {
    }

    default boolean visit(SQLImportDatabaseStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLRenameUserStatement x) {
    }

    default boolean visit(SQLRenameUserStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLPartitionByValue x) {
    }

    default boolean visit(SQLPartitionByValue x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTablePartitionCount x) {
    }

    default boolean visit(SQLAlterTablePartitionCount x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableBlockSize x) {
    }

    default boolean visit(SQLAlterTableBlockSize x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableCompression x) {
    }

    default boolean visit(SQLAlterTableCompression x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTablePartitionLifecycle x) {
    }

    default boolean visit(SQLAlterTablePartitionLifecycle x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableSubpartitionLifecycle x) {
    }

    default boolean visit(SQLAlterTableSubpartitionLifecycle x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableDropSubpartition x) {
    }

    default boolean visit(SQLAlterTableDropSubpartition x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableDropClusteringKey x) {
    }

    default boolean visit(SQLAlterTableDropClusteringKey x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableAddClusteringKey x) {
    }

    default boolean visit(SQLAlterTableAddClusteringKey x) {
        // TODO
        return false;
    }

    default void endVisit(MySqlKillStatement x) {
    }

    default boolean visit(MySqlKillStatement x) {
        // TODO
        return false;
    }

    default boolean visit(SQLCreateResourceGroupStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLCreateResourceGroupStatement x) {
    }

    default boolean visit(SQLAlterResourceGroupStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterResourceGroupStatement x) {
    }

    default void endVisit(SQLDropResourceGroupStatement x) {
    }

    default boolean visit(SQLDropResourceGroupStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLListResourceGroupStatement x) {
    }

    default boolean visit(SQLListResourceGroupStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableMergePartition x) {
    }

    default boolean visit(SQLAlterTableMergePartition x) {
        // TODO
        return false;
    }

    default void endVisit(SQLPartitionSpec x) {
    }

    default boolean visit(SQLPartitionSpec x) {
        // TODO
        return false;
    }

    default void endVisit(SQLPartitionSpec.Item x) {
    }

    default boolean visit(SQLPartitionSpec.Item x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableChangeOwner x) {
    }

    default boolean visit(SQLAlterTableChangeOwner x) {
        // TODO
        return false;
    }

    default void endVisit(SQLTableDataType x) {
    }

    default boolean visit(SQLTableDataType x) {
        // TODO
        return false;
    }

    default void endVisit(SQLCloneTableStatement x) {
    }

    default boolean visit(SQLCloneTableStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLShowHistoryStatement x) {
    }

    default boolean visit(SQLShowHistoryStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLShowRoleStatement x) {
    }

    default boolean visit(SQLShowRoleStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLShowRolesStatement x) {
    }

    default boolean visit(SQLShowRolesStatement x) {
        // TODO
        return false;
    }

    default boolean visit(SQLShowVariantsStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLShowVariantsStatement x) {
    }

    default boolean visit(SQLShowACLStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLShowACLStatement x) {
    }

    default boolean visit(SQLOptimizeStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLOptimizeStatement x) {
    }

    default boolean visit(SQLAlterTableAttachPartition x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableAttachPartition x) {
    }

    default boolean visit(SQLAlterTableDetachPartition x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableDetachPartition x) {
    }

    default boolean visit(SQLPivot x) {
        // TODO
        return false;
    }

    default void endVisit(SQLPivot x) {
    }

    default boolean visit(SQLUnpivot x) {
        // TODO
        return false;
    }

    default void endVisit(SQLUnpivot x) {
    }

    default void preVisit(StarRocksCreateResourceStatement x) {
    }

    default boolean visit(StarRocksCreateResourceStatement x) {
        // TODO
        return false;
    }

    default void endVisit(StarRocksCreateResourceStatement x) {
    }

    default boolean visit(SQLCostStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLCostStatement x) {
    }

    default boolean visit(StarRocksIndexDefinition x) {
        // TODO
        return false;
    }

    default void endVisit(StarRocksIndexDefinition x) {
    }

    default boolean visit(SQLSelectQueryTemplate x) {
        // TODO
        return false;
    }

    default void endVisit(SQLSelectQueryTemplate x) {
    }

    default boolean visit(SQLAlterTableSetSerde x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableSetSerde x) {
    }

    default boolean visit(SQLAlterTableSetFileFormat x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAlterTableSetFileFormat x) {
    }

    default boolean visit(SQLTop x) {
        // TODO
        return false;
    }

    default void endVisit(SQLTop x) {
    }

    default boolean visit(SQLPatternExpr x) {
        // TODO
        return false;
    }

    default void endVisit(SQLPatternExpr x) {
    }

    default boolean visit(SQLCommitTransactionStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLCommitTransactionStatement x) {
    }

    default boolean visit(SQLRaiseStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLRaiseStatement x) {
    }

    default boolean visit(SQLAtTimeZoneExpr x) {
        // TODO
        return false;
    }

    default void endVisit(SQLAtTimeZoneExpr x) {
    }

    default boolean visit(SQLRollbackTransactionStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLRollbackTransactionStatement x) {
    }

    default boolean visit(SQLExceptionStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLExceptionStatement x) {
    }

    default boolean visit(SQLExceptionStatement.Item x) {
        // TODO
        return false;
    }

    default void endVisit(SQLExceptionStatement.Item x) {
    }

    default boolean visit(SQLContinueStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLContinueStatement x) {
    }

    default boolean visit(SQLBreakStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLBreakStatement x) {
    }

    default boolean visit(SQLLeaveStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLLeaveStatement x) {
    }

    default boolean visit(SQLExecuteImmediateStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLExecuteImmediateStatement x) {
    }

    default boolean visit(SQLRefreshTableStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLRefreshTableStatement x) {
    }

    default boolean visit(SQLTypeExpr x) {
        // TODO
        return false;
    }

    default void endVisit(SQLTypeExpr x) {
    }

    default boolean visit(SQLComputeIncrementalStatsStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLComputeIncrementalStatsStatement x) {
    }

    default boolean visit(SQLResetStatement x) {
        // TODO
        return false;
    }

    default void endVisit(SQLResetStatement x) {
    }

    static SQLASTVisitor ofMethodInvoke(Consumer<SQLMethodInvokeExpr> p) {
        return ofMethodInvoke(null, p);
    }

    static SQLASTVisitor ofMethodInvoke(Predicate<String> filter, Consumer<SQLMethodInvokeExpr> p) {
        class MethodInvokeVisitor implements SQLASTVisitor, BigQueryVisitor, OdpsASTVisitor, OracleASTVisitor, MySqlASTVisitor, PGASTVisitor {

            final Predicate<String> filter;

            final Consumer<SQLMethodInvokeExpr> p;

            public MethodInvokeVisitor(Predicate<String> filter, Consumer<SQLMethodInvokeExpr> p) {
                this.filter = filter;
                this.p = p;
            }

            public boolean visit(SQLMethodInvokeExpr x) {
                if (filter == null || filter.test(x.getMethodName())) {
                    p.accept(x);
                }
                return true;
            }

            public boolean visit(SQLAggregateExpr x) {
                if (filter == null || filter.test(x.getMethodName())) {
                    p.accept(x);
                }
                return true;
            }
        }
        return new MethodInvokeVisitor(filter, p);
    }

    static SQLASTVisitor ofAggregate(Consumer<SQLAggregateExpr> p) {
        return new SQLASTVisitor() {

            public boolean visit(SQLAggregateExpr x) {
                p.accept(x);
                return true;
            }
        };
    }

    static SQLASTVisitor ofAggregate(Predicate<String> filter, Consumer<SQLAggregateExpr> p) {
        return new SQLASTVisitor() {

            public boolean visit(SQLAggregateExpr x) {
                if (filter == null || filter.test(x.getMethodName())) {
                    p.accept(x);
                }
                return true;
            }
        };
    }

    static SQLASTVisitor ofCast(Consumer<SQLCastExpr> h) {
        return new SQLASTVisitor() {

            public boolean visit(SQLCastExpr x) {
                h.accept(x);
                return true;
            }
        };
    }

    static SQLASTVisitor ofUnnest(Consumer<SQLUnnestTableSource> h) {
        return new SQLASTVisitor() {

            public boolean visit(SQLUnnestTableSource x) {
                h.accept(x);
                return true;
            }
        };
    }

    static SQLASTVisitor ofExprTableSource(Consumer<SQLExprTableSource> h) {
        return new SQLASTVisitor() {

            public boolean visit(SQLExprTableSource x) {
                h.accept(x);
                return true;
            }
        };
    }

    static SQLASTVisitor ofDelete(Consumer<SQLDeleteStatement> h) {
        return new SQLASTVisitor() {

            public boolean visit(SQLDeleteStatement x) {
                h.accept(x);
                return true;
            }
        };
    }

    static SQLASTVisitor ofMerge(Consumer<SQLMergeStatement> h) {
        return new SQLASTVisitor() {

            public boolean visit(SQLMergeStatement x) {
                h.accept(x);
                return true;
            }
        };
    }

    static SQLASTVisitor ofDeclare(Consumer<SQLDeclareStatement> h) {
        return new SQLASTVisitor() {

            public boolean visit(SQLDeclareStatement x) {
                h.accept(x);
                return true;
            }
        };
    }

    static SQLASTVisitor ofIf(Consumer<SQLIfStatement> h) {
        return new SQLASTVisitor() {

            public boolean visit(SQLIfStatement x) {
                h.accept(x);
                return true;
            }
        };
    }

    static SQLASTVisitor ofPropertyExpr(Consumer<SQLPropertyExpr> h) {
        return new SQLASTVisitor() {

            public boolean visit(SQLPropertyExpr x) {
                h.accept(x);
                return true;
            }
        };
    }

    static SQLASTVisitor ofIdentifier(Consumer<SQLIdentifierExpr> h) {
        return new SQLASTVisitor() {

            public boolean visit(SQLIdentifierExpr x) {
                h.accept(x);
                return true;
            }
        };
    }
}
