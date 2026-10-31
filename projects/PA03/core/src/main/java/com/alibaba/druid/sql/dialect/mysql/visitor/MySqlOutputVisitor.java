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
package com.alibaba.druid.sql.dialect.mysql.visitor;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.*;
import com.alibaba.druid.sql.ast.expr.*;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.mysql.MySQL;
import com.alibaba.druid.sql.dialect.mysql.ast.*;
import com.alibaba.druid.sql.dialect.mysql.ast.clause.*;
import com.alibaba.druid.sql.dialect.mysql.ast.clause.MySqlCaseStatement.MySqlWhenStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.expr.*;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.*;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement.TableSpaceOption;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateUserStatement.UserSpecification;
import com.alibaba.druid.sql.visitor.ExportParameterVisitorUtils;
import com.alibaba.druid.sql.visitor.SQLASTOutputVisitor;
import java.util.ArrayList;
import java.util.List;

public class MySqlOutputVisitor extends SQLASTOutputVisitor implements MySqlASTVisitor {

    {
        this.dbType = DbType.mysql;
        this.shardingSupport = true;
    }

    public MySqlOutputVisitor(StringBuilder appender) {
        super(appender, DbType.mysql, MySQL.DIALECT);
        // TODO
    }

    public MySqlOutputVisitor(StringBuilder appender, boolean parameterized) {
        super(appender, DbType.mysql, MySQL.DIALECT, parameterized);
        // TODO
    }

    private static boolean shardingSupportChecked;

    public void configFromProperty() {
        if (this.parameterized && !shardingSupportChecked) {
            shardingSupportChecked = true;
            String property = System.getProperties().getProperty("fastsql.parameterized.shardingSupport");
            if ("true".equals(property)) {
                this.setShardingSupport(true);
            } else if ("false".equals(property)) {
                this.setShardingSupport(false);
            }
        }
    }

    public boolean isShardingSupport() {
        return this.parameterized && shardingSupport;
    }

    public void setShardingSupport(boolean shardingSupport) {
        // TODO
    }

    @Override
    public boolean visit(SQLSelectQueryBlock select) {
        // TODO
        return false;
    }

    public boolean visit(MySqlSelectQueryBlock x) {
        // TODO
        return false;
    }

    @Override
    protected void printFrom(SQLSelectQueryBlock x) {
        SQLTableSource from = x.getFrom();
        if (from == null) {
            return;
        }
        println();
        print0(ucase ? "FROM " : "from ");
        if (x.getCommentsAfterFrom() != null) {
            printAfterComments(x.getCommentsAfterFrom());
            println();
        }
        printTableSource(from);
    }

    public boolean visit(SQLColumnDefinition x) {
        // TODO
        return false;
    }

    public boolean visit(SQLDataType x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(SQLCharacterDataType x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlTableIndex x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlKey x) {
        // TODO
        return false;
    }

    public boolean visit(SQLCharExpr x, boolean parameterized) {
        // TODO
        return false;
    }

    public boolean visit(SQLVariantRefExpr x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlPrepareStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlExecuteStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlExecuteForAdsStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlManageInstanceGroupStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlRaftMemberChangeStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlRaftLeaderTransferStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlMigrateStatement x) {
        // TODO
        return false;
    }

    @Override
    public void endVisit(MySqlMigrateStatement x) {
        print0(ucase ? "MIGRATE DATABASE " : "migrate database ");
        x.getSchema().accept(this);
        print0(ucase ? " SHARDS=" : "shards= ");
        x.getShardNames().accept(this);
        print0(" ");
        if (x.getMigrateType().getNumber().intValue() == 0) {
            print0(ucase ? "GROUP " : "group ");
        } else if (x.getMigrateType().getNumber().intValue() == 1) {
            print0(ucase ? "HOST " : "host ");
        }
        print0(ucase ? "FROM " : "from ");
        x.getFromInsId().accept(this);
        if (x.getFromInsIp() != null) {
            print(":");
            x.getFromInsIp().accept(this);
            print(":");
            x.getFromInsPort().accept(this);
            print(":");
            x.getFromInsStatus().accept(this);
        }
        print0(ucase ? " TO " : " to ");
        x.getToInsId().accept(this);
        if (x.getToInsIp() != null) {
            print(":");
            x.getToInsIp().accept(this);
            print(":");
            x.getToInsPort().accept(this);
            print(":");
            x.getToInsStatus().accept(this);
        }
    }

    @Override
    public boolean visit(MySqlShowClusterNameStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlShowJobStatusStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlShowMigrateTaskStatusStatement x) {
        // TODO
        return false;
    }

    public boolean visit(MysqlDeallocatePrepareStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlDeleteStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlInsertStatement x) {
        // TODO
        return false;
    }

    protected void printValuesList(List<SQLInsertStatement.ValuesClause> valuesList) {
        if (this.parameterized && valuesList.size() > 1 && !this.parameterizedQuesUnMergeValuesList) {
            print0(ucase ? "VALUES " : "values ");
            this.indentCount++;
            boolean allConst = true;
            if (valuesList.size() > 1) {
                for (int index = 0; index < valuesList.size(); index++) {
                    List<SQLExpr> values = valuesList.get(index).getValues();
                    for (int i = 0; i < values.size(); i++) {
                        SQLExpr value = values.get(i);
                        if (value instanceof SQLLiteralExpr || value instanceof SQLVariantRefExpr) {
                            continue;
                        } else if (value instanceof SQLMethodInvokeExpr && ((SQLMethodInvokeExpr) value).getArguments().isEmpty()) {
                            continue;
                        }
                        allConst = false;
                        break;
                    }
                    if (!allConst) {
                        break;
                    }
                }
            }
            if (!allConst) {
                for (int index = 0; index < valuesList.size(); index++) {
                    if (index != 0) {
                        print(',');
                        println();
                    }
                    visit(valuesList.get(index), this.parameters);
                }
            } else if (valuesList.size() > 1 && this.parameters != null) {
                SQLInsertStatement.ValuesClause first = valuesList.get(0);
                List<Object> valuesParameters = new ArrayList<Object>(first.getValues().size());
                visit(first, valuesParameters);
                this.parameters.add(valuesParameters);
                for (int index = 1; index < valuesList.size(); index++) {
                    List<SQLExpr> values = valuesList.get(index).getValues();
                    valuesParameters = new ArrayList<Object>(values.size());
                    for (int i = 0, size = values.size(); i < size; ++i) {
                        SQLExpr expr = values.get(i);
                        if (expr instanceof SQLIntegerExpr || expr instanceof SQLBooleanExpr || expr instanceof SQLNumberExpr || expr instanceof SQLCharExpr || expr instanceof SQLNCharExpr || expr instanceof SQLTimestampExpr || expr instanceof SQLDateExpr || expr instanceof SQLTimeExpr) {
                            incrementReplaceCunt();
                            ExportParameterVisitorUtils.exportParameter(valuesParameters, expr);
                        } else if (expr instanceof SQLNullExpr) {
                            incrementReplaceCunt();
                            valuesParameters.add(null);
                        } else {
                            // skip
                        }
                    }
                    this.parameters.add(valuesParameters);
                }
                this.incrementReplaceCunt();
            } else {
                if (valuesList.size() > 1) {
                    this.incrementReplaceCunt();
                }
                visit(valuesList.get(0), this.parameters);
            }
            this.indentCount--;
            return;
        }
        print0(ucase ? "VALUES " : "values ");
        if (valuesList.size() > 1) {
            this.indentCount++;
        }
        for (int i = 0, size = valuesList.size(); i < size; ++i) {
            if (i != 0) {
                print(',');
                println();
            }
            SQLInsertStatement.ValuesClause item = valuesList.get(i);
            visit(item, parameters);
        }
        if (valuesList.size() > 1) {
            this.indentCount--;
        }
    }

    @Override
    public boolean visit(MySqlLoadDataInFileStatement x) {
        // TODO
        return false;
    }

    public boolean visit(SQLReplaceStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(SQLStartTransactionStatement x) {
        // TODO
        return false;
    }

    public boolean visit(SQLRollbackStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(SQLShowTablesStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlShowHMSMetaStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlShowDatabaseStatusStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlShowWarningsStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlShowStatusStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlLoadXmlStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(CobarShowStatus x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(DrdsShowDDLJobs x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(DrdsCancelDDLJob x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(DrdsRecoverDDLJob x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(DrdsRollbackDDLJob x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(DrdsRemoveDDLJob x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(DrdsInspectDDLJobCache x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(DrdsClearDDLJobCache x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(DrdsChangeDDLJob x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(DrdsBaselineStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(DrdsShowGlobalIndex x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(DrdsShowMetadataLock x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlBinlogStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlCreateUserStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(UserSpecification x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlPartitionByKey x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlShowPlanCacheStatusStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlClearPlanCacheStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlDisabledPlanCacheStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlExplainPlanCacheStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlUpdatePlanCacheStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlOutFileExpr x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlExplainStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlUpdateStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlSetTransactionStatement x) {
        // TODO
        return false;
    }

    //
    //    @Override
    //    public boolean visit(MySqlSetNamesStatement x) {
    //        print0(ucase ? "SET NAMES " : "set names ");
    //        if (x.isDefault()) {
    //            print0(ucase ? "DEFAULT" : "default");
    //        } else {
    //            print0(x.getCharSet());
    //            if (x.getCollate() != null) {
    //                print0(ucase ? " COLLATE " : " collate ");
    //                print0(x.getCollate());
    //            }
    //        }
    //        return false;
    //    }
    //    public boolean visit(MySqlSetCharSetStatement x) {
    //        print0(ucase ? "SET CHARACTER SET " : "set character set ");
    //        if (x.isDefault()) {
    //            print0(ucase ? "DEFAULT" : "default");
    //        } else {
    //            print0(x.getCharSet());
    //            if (x.getCollate() != null) {
    //                print0(ucase ? " COLLATE " : " collate ");
    //                print0(x.getCollate());
    //            }
    //        }
    //        return false;
    //    }
    @Override
    public boolean visit(MySqlShowAuthorsStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlShowBinaryLogsStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlShowMasterLogsStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlShowCollationStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlShowBinLogEventsStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlShowCharacterSetStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlShowContributorsStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlShowCreateDatabaseStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlShowCreateEventStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlShowCreateFunctionStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlShowCreateProcedureStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlShowCreateTriggerStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlShowEngineStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlShowEventsStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlShowFunctionCodeStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlShowFunctionStatusStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlShowEnginesStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlShowErrorsStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlShowGrantsStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlUserName x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlShowMasterStatusStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlShowOpenTablesStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlShowPluginsStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlShowPartitionsStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(SQLShowPartitionsStmt x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlShowPrivilegesStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlShowProcedureCodeStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlShowProcedureStatusStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlShowProcessListStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlShowProfileStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlShowProfilesStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlShowRelayLogEventsStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlShowSlaveHostsStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlShowSlowStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlShowSequencesStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlShowSlaveStatusStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlShowTableStatusStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MysqlShowDbLockStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MysqlShowHtcStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MysqlShowStcStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlShowTriggersStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlShowRuleStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlShowRuleStatusStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlShowPhysicalProcesslistStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlRenameSequenceStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlCheckTableStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MysqlCreateFullTextCharFilterStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MysqlShowFullTextStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MysqlShowCreateFullTextStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MysqlAlterFullTextStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(SQLAlterTableDropClusteringKey x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MysqlDropFullTextStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MysqlCreateFullTextTokenizerStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MysqlCreateFullTextTokenFilterStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MysqlCreateFullTextAnalyzerStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MysqlCreateFullTextDictionaryStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlAlterTableAlterFullTextIndex x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlExtPartition x) {
        // TODO
        return false;
    }

    public boolean visit(MySqlExtPartition.Item x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlShowTraceStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlShowBroadcastsStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlShowDatasourcesStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlShowNodeStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlShowHelpStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlFlashbackStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlShowConfigStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlShowPlanCacheStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlShowDdlStatusStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlShowDsStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlShowTopologyStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(SQLShowVariantsStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(SQLAlterTableStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(SQLAlterTableAddColumn x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlRenameTableStatement.Item x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlRenameTableStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlUseIndexHint x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlIgnoreIndexHint x) {
        // TODO
        return false;
    }

    public boolean visit(SQLExprTableSource x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlLockTableStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlLockTableStatement.Item x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlUnlockTablesStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlForceIndexHint x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlAlterTableChangeColumn x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlAlterTableModifyColumn x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlAlterTableOption x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlAlterDatabaseSetOption x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlAlterDatabaseKillJob x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlHelpStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlCharExpr x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlUnique x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MysqlForeignKey x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlAlterTableDiscardTablespace x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlCreateExternalCatalogStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlAlterTableImportTablespace x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(SQLAssignItem x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(TableSpaceOption x) {
        // TODO
        return false;
    }

    protected void visitAggregateRest(SQLAggregateExpr x) {
        boolean withGroup = x.isWithinGroup();
        if (withGroup) {
            print0(ucase ? ") WITHIN GROUP (" : ") within group (");
        }
        {
            SQLOrderBy value = x.getOrderBy();
            if (value != null) {
                print(' ');
                ((SQLObject) value).accept(this);
            }
        }
        {
            Object value = x.getAttribute("SEPARATOR");
            if (value != null) {
                print0(ucase ? " SEPARATOR " : " separator ");
                ((SQLObject) value).accept(this);
            }
        }
    }

    @Override
    public boolean visit(MySqlAnalyzeStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlOptimizeStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlAlterUserStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(SQLSetStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlHintStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlOrderingExpr x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(SQLBlockStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(SQLIfStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(SQLIfStatement.ElseIf x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(SQLIfStatement.Else x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlCaseStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlDeclareStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlSelectIntoStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlWhenStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(SQLLoopStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(SQLWhileStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlLeaveStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlIterateStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlRepeatStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlCursorDeclareStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlUpdateTableSource x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlAlterTableAlterColumn x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlAlterTableForce x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlAlterTableLock x) {
        // TODO
        return false;
    }

    public boolean visit(MySqlAlterTableAlgorithm x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlAlterTableOrderBy x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlAlterTableValidation x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlSubPartitionByKey x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlSubPartitionByValue x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(SQLAlterTablePartitionCount x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(SQLAlterTableBlockSize x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(SQLAlterTableCompression x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlSubPartitionByList x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlDeclareHandlerStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlDeclareConditionStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(SQLAlterTableDropColumnItem x) {
        // TODO
        return false;
    }

    /**
     * visit procedure create node
     */
    @Override
    public boolean visit(SQLCreateProcedureStatement x) {
        // TODO
        return false;
    }

    protected void printCreateFunctionBody(SQLCreateFunctionStatement x) {
        printCreateFunctionReturns(x);
        String comment = x.getComment();
        if (comment != null) {
            print(ucase ? " COMMENT " : " comment ");
            print(ucase ? comment.toUpperCase() : comment.toLowerCase());
        }
        if (x.isDeterministic()) {
            print(ucase ? " DETERMINISTIC" : " deterministic");
        }
        String language = x.getLanguage();
        if (language != null) {
            print(ucase ? " LANGUAGE " : " language ");
            print(ucase ? language.toUpperCase() : language.toLowerCase());
        }
        SQLStatement block = x.getBlock();
        println();
        block.accept(this);
    }

    protected void printCreateFunctionReturns(SQLCreateFunctionStatement x) {
        println();
        print(ucase ? "RETURNS " : "returns ");
        x.getReturnDataType().accept(this);
    }

    @Override
    public boolean visit(SQLCommentStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlFlushStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlEventSchedule x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlCreateEventStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlCreateAddLogFileGroupStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlCreateServerStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlCreateTableSpaceStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlAlterEventStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlAlterLogFileGroupStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlAlterServerStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlAlterTablespaceStatement x) {
        // TODO
        return false;
    }

    @Override
    public boolean visit(MySqlChecksumTableStatement x) {
        // TODO
        return false;
    }

    protected void printQuery(SQLSelectQuery x) {
        Class<?> clazz = x.getClass();
        if (clazz == MySqlSelectQueryBlock.class) {
            visit((MySqlSelectQueryBlock) x);
        } else if (clazz == SQLSelectQueryBlock.class) {
            visit((SQLSelectQueryBlock) x);
        } else if (clazz == SQLUnionQuery.class) {
            visit((SQLUnionQuery) x);
        } else {
            x.accept(this);
        }
    }

    public void printInsertColumns(List<SQLExpr> columns) {
        final int size = columns.size();
        if (size > 0) {
            if (size > 5) {
                this.indentCount++;
                print(' ');
            }
            print('(');
            for (int i = 0; i < size; ++i) {
                if (i != 0) {
                    if (i % 5 == 0) {
                        println();
                    }
                    print0(", ");
                }
                SQLExpr column = columns.get(i);
                if (column instanceof SQLIdentifierExpr) {
                    visit((SQLIdentifierExpr) column);
                } else {
                    printExpr(column, parameterized);
                }
                String dataType = (String) column.getAttribute("dataType");
                if (dataType != null) {
                    print(' ');
                    print(dataType);
                }
            }
            print(')');
            if (size > 5) {
                this.indentCount--;
            }
        }
    }

    @Override
    public boolean visit(SQLValuesTableSource x) {
        // TODO
        return false;
    }

    public boolean visit(SQLExternalRecordFormat x) {
        // TODO
        return false;
    }

    public boolean visit(MySqlJSONTableExpr x) {
        // TODO
        return false;
    }

    public boolean visit(TidbSplitTableStatement x) {
        // TODO
        return false;
    }

    public boolean visit(MySqlJSONTableExpr.Column x) {
        // TODO
        return false;
    }

    //    public boolean visit(SQLSelectItem x) {
    //        SQLExpr expr = x.getExpr();
    //
    //        if (expr instanceof SQLIdentifierExpr) {
    //            print0(((SQLIdentifierExpr) expr).getName());
    //        } else if (expr instanceof SQLPropertyExpr) {
    //            visit((SQLPropertyExpr) expr);
    //        } else {
    //            printExpr(expr);
    //        }
    //
    //        String alias = x.getAlias();
    //        if (alias != null && alias.length() > 0) {
    //            print0(ucase ? " AS " : " as ");
    //
    //            boolean hasSpecial = false;
    //            for (int i = 1; i < alias.length() - 1; ++i) {
    //                char ch = alias.charAt(i);
    //                if (ch == ' ' || ch == '\"' || ch == '\n') {
    //                    hasSpecial = true;
    //                }
    //            }
    //            char c0 = alias.charAt(0);
    //            if (!hasSpecial) {
    //                print0(alias);
    //            } else {
    //                print('"');
    //
    //                for (int i = 0; i < alias.length(); ++i) {
    //                    char ch = alias.charAt(i);
    //                    if (ch == '\"') {
    //                        print('\\');
    //                        print(ch);
    //                    } else if (ch == '\n') {
    //                        print0("\\n");
    //                    } else {
    //                        print(ch);
    //                    }
    //                }
    //
    //                print('"');
    //            }
    //        }
    //        return false;
    //    }
    public boolean visit(MysqlAlterTableAlterCheck x) {
        // TODO
        return false;
    }

    public boolean visit(MySqlXAStatement x) {
        // TODO
        return false;
    }

    @Override
    protected void printTableOptionsPrefix(SQLCreateTableStatement x) {
    }

    @Override
    protected void printTableOptionsPostfix(SQLCreateTableStatement x) {
    }

    @Override
    protected void printTableOption(SQLExpr name, SQLExpr value, int index) {
        println();
        name.accept(this);
        print0(" = ");
        value.accept(this);
    }

    @Override
    public boolean visit(SQLPartitionSingle x) {
        // TODO
        return false;
    }

    public boolean visit(MysqlPartitionSingle x) {
        // TODO
        return false;
    }
}
