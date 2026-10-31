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
package com.alibaba.druid.sql.repository;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLAllColumnExpr;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitorAdapter;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCreateTableStatement;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitorAdapter;
import com.alibaba.druid.sql.dialect.sqlserver.visitor.SQLServerASTVisitorAdapter;
import com.alibaba.druid.sql.repository.function.Function;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitorAdapter;
import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.alibaba.druid.util.FnvHash;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by wenshao on 03/06/2017.
 */
public class SchemaRepository {

    private static Log LOG = LogFactory.getLog(SchemaRepository.class);

    private Schema defaultSchema;

    protected DbType dbType;

    protected DbType schemaDbType;

    protected SQLASTVisitor consoleVisitor;

    protected Map<String, Schema> schemas = new LinkedHashMap<String, Schema>();

    protected final Map<Long, Function> internalFunctions = new ConcurrentHashMap<Long, Function>(16, 0.75f, 1);

    protected SchemaLoader schemaLoader;

    public SchemaRepository() {
        // TODO
    }

    public SchemaRepository(DbType dbType) {
        this(dbType, dbType);
        // TODO
    }

    public SchemaRepository(DbType dbType, DbType schemaDbType) {
        // TODO
    }

    public DbType getDbType() {
        return dbType;
    }

    public String getDefaultSchemaName() {
        return getDefaultSchema().getName();
    }

    public void setDefaultSchema(String name) {
        // TODO
    }

    public Schema findSchema(String schema) {
        // TODO
        return null;
    }

    protected Schema findSchema(String name, boolean create) {
        // TODO
        return null;
    }

    public Schema getDefaultSchema() {
        if (defaultSchema == null) {
            defaultSchema = new Schema(this);
        }
        return defaultSchema;
    }

    public void setDefaultSchema(Schema schema) {
        // TODO
    }

    public SchemaObject findTable(String tableName) {
        // TODO
        return null;
    }

    public SchemaObject findView(String viewName) {
        // TODO
        return null;
    }

    public SchemaObject findTable(long tableNameHash) {
        // TODO
        return null;
    }

    public SchemaObject findTableOrView(String tableName) {
        return findTableOrView(tableName, true);
    }

    public SchemaObject findTableOrView(String tableName, boolean onlyCurrent) {
        Schema schema = getDefaultSchema();
        SchemaObject object = schema.findTableOrView(tableName);
        if (object != null) {
            return object;
        }
        for (Schema s : this.schemas.values()) {
            if (s == schema) {
                continue;
            }
            object = schema.findTableOrView(tableName);
            if (object != null) {
                return object;
            }
        }
        String ddl = loadDDL(tableName);
        if (ddl == null) {
            return null;
        }
        acceptDDL(ddl);
        // double check
        object = schema.findTableOrView(tableName);
        if (object != null) {
            return object;
        }
        for (Schema s : this.schemas.values()) {
            if (s == schema) {
                continue;
            }
            object = schema.findTableOrView(tableName);
            if (object != null) {
                return object;
            }
        }
        return null;
    }

    public Collection<Schema> getSchemas() {
        // TODO
        return null;
    }

    public SchemaObject findFunction(String functionName) {
        // TODO
        return null;
    }

    public void acceptDDL(String ddl) {
        // TODO
    }

    public SchemaObject acceptDDL(String ddl, DbType dbType) {
        // TODO
        return null;
    }

    public void accept(SQLStatement stmt) {
        // TODO
    }

    public boolean isSequence(String name) {
        return getDefaultSchema().isSequence(name);
    }

    public SchemaObject findTable(SQLTableSource tableSource, String alias) {
        // TODO
        return null;
    }

    public SQLColumnDefinition findColumn(SQLTableSource tableSource, SQLSelectItem selectItem) {
        return getDefaultSchema().findColumn(tableSource, selectItem);
    }

    public SQLColumnDefinition findColumn(SQLTableSource tableSource, SQLExpr expr) {
        return getDefaultSchema().findColumn(tableSource, expr);
    }

    public SchemaObject findTable(SQLTableSource tableSource, SQLSelectItem selectItem) {
        // TODO
        return null;
    }

    public SchemaObject findTable(SQLTableSource tableSource, SQLExpr expr) {
        // TODO
        return null;
    }

    public Map<String, SchemaObject> getTables(SQLTableSource x) {
        return getDefaultSchema().getTables(x);
    }

    public boolean removeTable(SQLName name) {
        return getDefaultSchema().removeObject(name.nameHashCode64());
    }

    public int getTableCount() {
        return getDefaultSchema().getTableCount();
    }

    public Collection<SchemaObject> getObjects() {
        return getDefaultSchema().getObjects();
    }

    public int getViewCount() {
        return getDefaultSchema().getViewCount();
    }

    public void resolve(SQLSelectStatement stmt, SchemaResolveVisitor.Option... options) {
        // TODO
    }

    public void resolve(SQLSelect select, SchemaResolveVisitor.Option... options) {
        // TODO
    }

    public void resolve(SQLSelectQueryBlock queryBlock, SchemaResolveVisitor.Option... options) {
        // TODO
    }

    public void resolve(SQLStatement stmt, SchemaResolveVisitor.Option... options) {
        // TODO
    }

    private SchemaResolveVisitor createResolveVisitor(SchemaResolveVisitor.Option... options) {
        int optionsValue = SchemaResolveVisitor.Option.of(options);
        SchemaResolveVisitor resolveVisitor;
        switch(dbType) {
            case mysql:
            case mariadb:
            case tidb:
            case sqlite:
            case polardbx:
                resolveVisitor = new SchemaResolveVisitorFactory.MySqlResolveVisitor(this, optionsValue);
                break;
            case oracle:
                resolveVisitor = new SchemaResolveVisitorFactory.OracleResolveVisitor(this, optionsValue);
                break;
            case db2:
                resolveVisitor = new SchemaResolveVisitorFactory.DB2ResolveVisitor(this, optionsValue);
                break;
            case odps:
                resolveVisitor = new SchemaResolveVisitorFactory.OdpsResolveVisitor(this, optionsValue);
                break;
            case hive:
                resolveVisitor = new SchemaResolveVisitorFactory.HiveResolveVisitor(this, optionsValue);
                break;
            case postgresql:
            case greenplum:
            case edb:
                resolveVisitor = new SchemaResolveVisitorFactory.PGResolveVisitor(this, optionsValue);
                break;
            case sqlserver:
                resolveVisitor = new SchemaResolveVisitorFactory.SQLServerResolveVisitor(this, optionsValue);
                break;
            default:
                resolveVisitor = new SchemaResolveVisitorFactory.SQLResolveVisitor(this, optionsValue);
                break;
        }
        return resolveVisitor;
    }

    public String resolve(String input) {
        // TODO
        return null;
    }

    public String console(String input) {
        // TODO
        return null;
    }

    public SchemaObject findTable(SQLName name) {
        // TODO
        return null;
    }

    public SchemaObject findView(SQLName name) {
        // TODO
        return null;
    }

    private boolean renameTable(SQLName name, SQLName to) {
        Schema schema;
        if (name instanceof SQLPropertyExpr) {
            String schemaName = ((SQLPropertyExpr) name).getOwnernName();
            schema = findSchema(schemaName);
        } else {
            schema = getDefaultSchema();
        }
        if (schema == null) {
            return false;
        }
        long nameHashCode64 = name.nameHashCode64();
        SchemaObject schemaObject = schema.findTable(nameHashCode64);
        if (schemaObject != null) {
            MySqlCreateTableStatement createTableStmt = (MySqlCreateTableStatement) schemaObject.getStatement();
            if (createTableStmt != null) {
                createTableStmt.setName(to.clone());
                acceptCreateTable(createTableStmt);
            }
            schema.objects.remove(nameHashCode64);
        }
        return true;
    }

    public SchemaObject findTable(SQLExprTableSource x) {
        // TODO
        return null;
    }

    public class MySqlConsoleSchemaVisitor extends MySqlASTVisitorAdapter {

        public boolean visit(SQLDropSequenceStatement x) {
            acceptDropSequence(x);
            return false;
        }

        public boolean visit(SQLCreateSequenceStatement x) {
            acceptCreateSequence(x);
            return false;
        }

        public boolean visit(MySqlCreateTableStatement x) {
            acceptCreateTable(x);
            return false;
        }

        public boolean visit(SQLCreateTableStatement x) {
            acceptCreateTable(x);
            return false;
        }

        public boolean visit(SQLDropTableStatement x) {
            acceptDropTable(x);
            return false;
        }

        public boolean visit(SQLCreateViewStatement x) {
            acceptView(x);
            return false;
        }

        public boolean visit(SQLAlterViewStatement x) {
            acceptView(x);
            return false;
        }

        public boolean visit(SQLCreateIndexStatement x) {
            acceptCreateIndex(x);
            return false;
        }

        public boolean visit(SQLCreateFunctionStatement x) {
            acceptCreateFunction(x);
            return false;
        }

        public boolean visit(SQLAlterTableStatement x) {
            acceptAlterTable(x);
            return false;
        }

        public boolean visit(SQLUseStatement x) {
            String schema = x.getDatabase().getSimpleName();
            setDefaultSchema(schema);
            return false;
        }

        public boolean visit(SQLDropIndexStatement x) {
            acceptDropIndex(x);
            return false;
        }
    }

    public class OracleConsoleSchemaVisitor extends OracleASTVisitorAdapter {

        public boolean visit(SQLDropSequenceStatement x) {
            acceptDropSequence(x);
            return false;
        }

        public boolean visit(SQLCreateSequenceStatement x) {
            acceptCreateSequence(x);
            return false;
        }

        public boolean visit(OracleCreateTableStatement x) {
            visit((SQLCreateTableStatement) x);
            return false;
        }

        public boolean visit(SQLCreateTableStatement x) {
            acceptCreateTable(x);
            return false;
        }

        public boolean visit(SQLDropTableStatement x) {
            acceptDropTable(x);
            return false;
        }

        public boolean visit(SQLCreateViewStatement x) {
            acceptView(x);
            return false;
        }

        public boolean visit(SQLAlterViewStatement x) {
            acceptView(x);
            return false;
        }

        public boolean visit(SQLCreateIndexStatement x) {
            acceptCreateIndex(x);
            return false;
        }

        public boolean visit(SQLCreateFunctionStatement x) {
            acceptCreateFunction(x);
            return false;
        }

        public boolean visit(SQLAlterTableStatement x) {
            acceptAlterTable(x);
            return false;
        }

        public boolean visit(SQLUseStatement x) {
            String schema = x.getDatabase().getSimpleName();
            setDefaultSchema(schema);
            return false;
        }

        public boolean visit(SQLDropIndexStatement x) {
            acceptDropIndex(x);
            return false;
        }
    }

    public class SqlServerConsoleSchemaVisitor extends SQLServerASTVisitorAdapter {

        public SqlServerConsoleSchemaVisitor() {
            this.dbType = DbType.sqlserver;
        }

        public boolean visit(SQLDropSequenceStatement x) {
            acceptDropSequence(x);
            return false;
        }

        public boolean visit(SQLCreateSequenceStatement x) {
            acceptCreateSequence(x);
            return false;
        }

        public boolean visit(OracleCreateTableStatement x) {
            visit((SQLCreateTableStatement) x);
            return false;
        }

        public boolean visit(SQLCreateTableStatement x) {
            acceptCreateTable(x);
            return false;
        }

        public boolean visit(SQLDropTableStatement x) {
            acceptDropTable(x);
            return false;
        }

        public boolean visit(SQLCreateViewStatement x) {
            acceptView(x);
            return false;
        }

        public boolean visit(SQLAlterViewStatement x) {
            acceptView(x);
            return false;
        }

        public boolean visit(SQLCreateIndexStatement x) {
            acceptCreateIndex(x);
            return false;
        }

        public boolean visit(SQLCreateFunctionStatement x) {
            acceptCreateFunction(x);
            return false;
        }

        public boolean visit(SQLAlterTableStatement x) {
            acceptAlterTable(x);
            return false;
        }

        public boolean visit(SQLUseStatement x) {
            String schema = x.getDatabase().getSimpleName();
            setDefaultSchema(schema);
            return false;
        }

        public boolean visit(SQLDropIndexStatement x) {
            acceptDropIndex(x);
            return false;
        }
    }

    public class DefaultConsoleSchemaVisitor extends SQLASTVisitorAdapter {

        public boolean visit(SQLDropSequenceStatement x) {
            acceptDropSequence(x);
            return false;
        }

        public boolean visit(SQLCreateSequenceStatement x) {
            acceptCreateSequence(x);
            return false;
        }

        public boolean visit(SQLCreateTableStatement x) {
            acceptCreateTable(x);
            return false;
        }

        public boolean visit(SQLDropTableStatement x) {
            acceptDropTable(x);
            return false;
        }

        public boolean visit(SQLCreateViewStatement x) {
            acceptView(x);
            return false;
        }

        public boolean visit(SQLAlterViewStatement x) {
            acceptView(x);
            return false;
        }

        public boolean visit(SQLCreateIndexStatement x) {
            acceptCreateIndex(x);
            return false;
        }

        public boolean visit(SQLCreateFunctionStatement x) {
            acceptCreateFunction(x);
            return false;
        }

        public boolean visit(SQLAlterTableStatement x) {
            acceptAlterTable(x);
            return false;
        }

        public boolean visit(SQLDropIndexStatement x) {
            acceptDropIndex(x);
            return false;
        }
    }

    SchemaObject acceptCreateTable(MySqlCreateTableStatement x) {
        SQLExprTableSource like = x.getLike();
        if (like != null) {
            SchemaObject table = findTable((SQLName) like.getExpr());
            if (table != null) {
                MySqlCreateTableStatement stmt = (MySqlCreateTableStatement) table.getStatement();
                MySqlCreateTableStatement stmtCloned = stmt.clone();
                stmtCloned.setName(x.getName().clone());
                acceptCreateTable((SQLCreateTableStatement) stmtCloned);
                return table;
            }
        }
        return acceptCreateTable((SQLCreateTableStatement) x);
    }

    SchemaObject acceptCreateTable(SQLCreateTableStatement x) {
        SQLCreateTableStatement x1 = x.clone();
        String schemaName = x1.getSchema();
        Schema schema = findSchema(schemaName, true);
        SQLSelect select = x1.getSelect();
        if (select != null) {
            select.accept(createResolveVisitor(SchemaResolveVisitor.Option.ResolveAllColumn));
            SQLSelectQueryBlock queryBlock = select.getFirstQueryBlock();
            this.resolve(queryBlock);
            if (queryBlock != null) {
                List<SQLSelectItem> selectList = queryBlock.getSelectList();
                for (SQLSelectItem selectItem : selectList) {
                    SQLExpr selectItemExpr = selectItem.getExpr();
                    if (selectItemExpr instanceof SQLAllColumnExpr || (selectItemExpr instanceof SQLPropertyExpr && ((SQLPropertyExpr) selectItemExpr).getName().equals("*"))) {
                        continue;
                    }
                    SQLColumnDefinition column = null;
                    if (selectItemExpr instanceof SQLName) {
                        final SQLColumnDefinition resolvedColumn = ((SQLName) selectItemExpr).getResolvedColumn();
                        if (resolvedColumn != null) {
                            column = new SQLColumnDefinition();
                            column.setDataType(selectItem.computeDataType());
                            if (DbType.mysql == dbType) {
                                if (resolvedColumn.getDefaultExpr() != null) {
                                    column.setDefaultExpr(resolvedColumn.getDefaultExpr().clone());
                                }
                                if (resolvedColumn.getConstraints().size() > 0) {
                                    for (SQLColumnConstraint constraint : resolvedColumn.getConstraints()) {
                                        column.addConstraint(constraint.clone());
                                    }
                                }
                                if (resolvedColumn.getComment() != null) {
                                    column.setComment(resolvedColumn.getComment());
                                }
                            }
                        }
                    }
                    if (column == null) {
                        column = new SQLColumnDefinition();
                        SQLDataType dataType = null;
                        try {
                            dataType = selectItem.computeDataType();
                        } catch (Throwable ignored) {
                            // ignore
                        }
                        if (dataType != null) {
                            column.setDataType(dataType.clone());
                        }
                    }
                    String name = selectItem.computeAlias();
                    column.setName(name);
                    column.setDbType(dbType);
                    x1.addColumn(column);
                }
                if (x1.getTableElementList().size() > 0) {
                    x1.setSelect(null);
                }
            }
        }
        SQLExprTableSource like = x1.getLike();
        if (like != null) {
            SchemaObject tableObject = null;
            SQLName name = like.getName();
            if (name != null) {
                tableObject = findTable(name);
            }
            SQLCreateTableStatement tableStmt = null;
            if (tableObject != null) {
                SQLStatement stmt = tableObject.getStatement();
                if (stmt instanceof SQLCreateTableStatement) {
                    tableStmt = (SQLCreateTableStatement) stmt;
                }
            }
            if (tableStmt != null) {
                SQLName tableName = x1.getName();
                tableStmt.cloneTo(x1);
                x1.setName(tableName);
                x1.setLike((SQLExprTableSource) null);
            }
        }
        x1.setSchema(null);
        String name = x1.computeName();
        SchemaObject table = schema.findTableOrView(name);
        if (table != null) {
            if (x1.isIfNotExists()) {
                return table;
            }
            LOG.info("replaced table '" + name + "'");
        }
        table = new SchemaObject(schema, name, SchemaObjectType.Table, x1);
        schema.objects.put(table.nameHashCode64(), table);
        return table;
    }

    boolean acceptDropTable(SQLDropTableStatement x) {
        for (SQLExprTableSource table : x.getTableSources()) {
            String schemaName = table.getSchema();
            Schema schema = findSchema(schemaName, false);
            if (schema == null) {
                continue;
            }
            long nameHashCode64 = table.getName().nameHashCode64();
            schema.objects.remove(nameHashCode64);
        }
        return true;
    }

    SchemaObject acceptView(SQLCreateViewStatement x) {
        String schemaName = x.getSchema();
        Schema schema = findSchema(schemaName, true);
        String name = x.computeName();
        SchemaObject view = schema.findTableOrView(name);
        if (view != null) {
            return view;
        }
        SchemaObject object = new SchemaObject(schema, name, SchemaObjectType.View, x.clone());
        long nameHashCode64 = FnvHash.hashCode64(name);
        schema.objects.put(nameHashCode64, object);
        return object;
    }

    boolean acceptView(SQLAlterViewStatement x) {
        String schemaName = x.getSchema();
        Schema schema = findSchema(schemaName, true);
        String name = x.computeName();
        SchemaObject view = schema.findTableOrView(name);
        if (view != null) {
            return false;
        }
        SchemaObject object = new SchemaObject(schema, name, SchemaObjectType.View, x.clone());
        schema.objects.put(object.nameHashCode64(), object);
        return true;
    }

    boolean acceptDropIndex(SQLDropIndexStatement x) {
        if (x.getTableName() == null) {
            return false;
        }
        SQLName table = x.getTableName().getName();
        SchemaObject object = findTable(table);
        if (object != null) {
            SQLCreateTableStatement stmt = (SQLCreateTableStatement) object.getStatement();
            if (stmt != null) {
                stmt.apply(x);
                return true;
            }
        }
        return false;
    }

    boolean acceptCreateIndex(SQLCreateIndexStatement x) {
        String schemaName = x.getSchema();
        Schema schema = findSchema(schemaName, true);
        String name = x.getName().getSimpleName();
        SchemaObject object = new SchemaObject(schema, name, SchemaObjectType.Index, x.clone());
        schema.objects.put(object.nameHashCode64(), object);
        return true;
    }

    boolean acceptCreateFunction(SQLCreateFunctionStatement x) {
        // TODO
        return false;
    }

    boolean acceptAlterTable(SQLAlterTableStatement x) {
        String schemaName = x.getSchema();
        if (dbType == DbType.sqlserver) {
            SQLName tableName = x.getTableSource().getName();
            if (tableName instanceof SQLPropertyExpr) {
                SQLExpr owner = ((SQLPropertyExpr) tableName).getOwner();
                if (owner instanceof SQLPropertyExpr) {
                    schemaName = ((SQLPropertyExpr) owner).getName();
                }
            }
        }
        Schema schema = findSchema(schemaName, true);
        SchemaObject object = schema.findTable(x.nameHashCode64());
        schema.getTables(x.getTableSource());
        if (object != null) {
            SQLCreateTableStatement stmt = (SQLCreateTableStatement) object.getStatement();
            if (stmt != null) {
                stmt.apply(x);
                x.getTableSource().setSchemaObject(object);
                return true;
            }
        }
        return false;
    }

    public boolean acceptCreateSequence(SQLCreateSequenceStatement x) {
        String schemaName = x.getSchema();
        Schema schema = findSchema(schemaName, true);
        String name = x.getName().getSimpleName();
        SchemaObject object = new SchemaObject(schema, name, SchemaObjectType.Sequence);
        schema.objects.put(object.nameHashCode64(), object);
        return false;
    }

    public boolean acceptDropSequence(SQLDropSequenceStatement x) {
        String schemaName = x.getSchema();
        Schema schema = findSchema(schemaName, true);
        long nameHashCode64 = x.getName().nameHashCode64();
        schema.objects.remove(nameHashCode64);
        return false;
    }

    public SQLDataType findFuntionReturnType(long functionNameHashCode) {
        if (functionNameHashCode == FnvHash.Constants.LEN || functionNameHashCode == FnvHash.Constants.LENGTH) {
            return SQLIntegerExpr.DATA_TYPE;
        }
        return null;
    }

    protected String loadDDL(String table) {
        if (table == null) {
            return null;
        }
        table = SQLUtils.normalize(table, schemaDbType);
        if (schemaLoader != null) {
            return schemaLoader.loadDDL(null, null, table);
        }
        return null;
    }

    protected String loadDDL(String schema, String table) {
        if (table == null) {
            return null;
        }
        table = SQLUtils.normalize(table, dbType);
        if (schema != null) {
            schema = SQLUtils.normalize(schema, dbType);
        }
        if (schemaLoader != null) {
            return schemaLoader.loadDDL(null, schema, table);
        }
        return null;
    }

    protected String loadDDL(String catalog, String schema, String table) {
        if (table == null) {
            return null;
        }
        table = SQLUtils.normalize(table, dbType);
        if (schema != null) {
            schema = SQLUtils.normalize(schema, dbType);
        }
        if (catalog != null) {
            catalog = SQLUtils.normalize(catalog, dbType);
        }
        if (schemaLoader != null) {
            return schemaLoader.loadDDL(catalog, schema, table);
        }
        return null;
    }

    public SchemaLoader getSchemaLoader() {
        return schemaLoader;
    }

    public void setSchemaLoader(SchemaLoader schemaLoader) {
        // TODO
    }

    public static interface SchemaLoader {

        String loadDDL(String catalog, String schema, String objectName);
    }
}
