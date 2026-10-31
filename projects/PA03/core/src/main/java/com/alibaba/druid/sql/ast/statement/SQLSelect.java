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
package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.*;
import com.alibaba.druid.sql.ast.expr.SQLAllColumnExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleSQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.util.FnvHash;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SQLSelect extends SQLObjectImpl implements SQLDbTypedObject {

    protected SQLWithSubqueryClause withSubQuery;

    protected SQLSelectQuery query;

    protected SQLOrderBy orderBy;

    protected SQLLimit limit;

    protected List<SQLHint> hints;

    protected SQLObject restriction;

    protected boolean forBrowse;

    protected List<String> forXmlOptions;

    protected SQLExpr xmlPath;

    protected SQLExpr rowCount;

    protected SQLExpr offset;

    private SQLHint headHint;

    public SQLSelect() {
        // TODO
    }

    public List<SQLHint> getHints() {
        // TODO
        return null;
    }

    public int getHintsSize() {
        if (hints == null) {
            return 0;
        }
        return hints.size();
    }

    public SQLSelect(SQLSelectQuery query) {
        // TODO
    }

    public SQLWithSubqueryClause getWithSubQuery() {
        return withSubQuery;
    }

    public SQLWithSubqueryClause.Entry findWithSubQueryEntry(String alias) {
        return withSubQuery == null ? null : withSubQuery.findEntry(alias);
    }

    public SQLDataType findSelectItemAndComputeDataType(String ident) {
        if (ident == null) {
            return null;
        }
        long hash = FnvHash.hashCode64(ident);
        return findSelectItemAndComputeDataType(hash);
    }

    public SQLDataType findSelectItemAndComputeDataType(long identHash) {
        SQLSelectQueryBlock queryBlock = getFirstQueryBlock();
        return queryBlock != null ? queryBlock.findSelectItemAndComputeDataType(identHash) : null;
    }

    public void setWithSubQuery(SQLWithSubqueryClause x) {
        // TODO
    }

    public SQLSelectQuery getQuery() {
        // TODO
        return null;
    }

    public void setQuery(SQLSelectQuery query) {
        // TODO
    }

    public SQLSelectQueryBlock getQueryBlock() {
        // TODO
        return null;
    }

    public SQLOrderBy getOrderBy() {
        return this.orderBy;
    }

    public void setOrderBy(SQLOrderBy orderBy) {
        if (orderBy != null) {
            orderBy.setParent(this);
        }
        this.orderBy = orderBy;
    }

    protected void accept0(SQLASTVisitor v) {
        if (v.visit(this)) {
            if (withSubQuery != null) {
                withSubQuery.accept0(v);
            }
            if (this.query != null) {
                this.query.accept(v);
            }
            if (this.restriction != null) {
                this.restriction.accept(v);
            }
            if (this.orderBy != null) {
                this.orderBy.accept(v);
            }
            if (this.hints != null) {
                for (SQLHint hint : hints) {
                    hint.accept(v);
                }
            }
            if (this.offset != null) {
                this.offset.accept(v);
            }
            if (this.rowCount != null) {
                this.rowCount.accept(v);
            }
            if (this.headHint != null) {
                this.headHint.accept(v);
            }
        }
        v.endVisit(this);
    }

    public DbType getDbType() {
        DbType dbType = null;
        SQLObject parent = this.getParent();
        if (parent instanceof SQLStatement) {
            dbType = ((SQLStatement) parent).getDbType();
        }
        if (dbType == null && parent instanceof OracleSQLObject) {
            dbType = DbType.oracle;
        }
        if (dbType == null && query instanceof SQLSelectQueryBlock) {
            dbType = ((SQLSelectQueryBlock) query).dbType;
        }
        return dbType;
    }

    public SQLSelect clone() {
        // TODO
        return null;
    }

    public boolean isSimple() {
        return withSubQuery == null && (hints == null || hints.isEmpty()) && restriction == null && (!forBrowse) && (forXmlOptions == null || forXmlOptions.isEmpty()) && xmlPath == null && rowCount == null && offset == null;
    }

    public SQLObject getRestriction() {
        return this.restriction;
    }

    public void setRestriction(SQLObject restriction) {
        if (restriction != null) {
            restriction.setParent(this);
        }
        this.restriction = restriction;
    }

    public boolean isForBrowse() {
        return forBrowse;
    }

    public void setForBrowse(boolean forBrowse) {
        this.forBrowse = forBrowse;
    }

    public List<String> getForXmlOptions() {
        if (forXmlOptions == null) {
            forXmlOptions = new ArrayList<String>(4);
        }
        return forXmlOptions;
    }

    public int getForXmlOptionsSize() {
        if (forXmlOptions == null) {
            return 0;
        }
        return forXmlOptions.size();
    }

    public SQLExpr getRowCount() {
        return rowCount;
    }

    public void setRowCount(SQLExpr rowCount) {
        if (rowCount != null) {
            rowCount.setParent(this);
        }
        this.rowCount = rowCount;
    }

    public SQLHint getHeadHint() {
        return headHint;
    }

    public void setHeadHint(SQLHint headHint) {
        this.headHint = headHint;
    }

    public SQLExpr getOffset() {
        return offset;
    }

    public void setOffset(SQLExpr offset) {
        if (offset != null) {
            offset.setParent(this);
        }
        this.offset = offset;
    }

    public SQLExpr getXmlPath() {
        return xmlPath;
    }

    public void setXmlPath(SQLExpr xmlPath) {
        if (xmlPath != null) {
            xmlPath.setParent(this);
        }
        this.xmlPath = xmlPath;
    }

    public List<String> computeSelecteListAlias() {
        SQLSelectQueryBlock firstQuery = getFirstQueryBlock();
        if (firstQuery != null) {
            return firstQuery.computeSelecteListAlias();
        }
        return Collections.emptyList();
    }

    public SQLSelectQueryBlock getFirstQueryBlock() {
        // TODO
        return null;
    }

    @Override
    public boolean equals(Object o) {
        // TODO
        return false;
    }

    @Override
    public int hashCode() {
        int result = withSubQuery != null ? withSubQuery.hashCode() : 0;
        result = 31 * result + (query != null ? query.hashCode() : 0);
        result = 31 * result + (orderBy != null ? orderBy.hashCode() : 0);
        result = 31 * result + (limit != null ? limit.hashCode() : 0);
        result = 31 * result + (hints != null ? hints.hashCode() : 0);
        result = 31 * result + (restriction != null ? restriction.hashCode() : 0);
        result = 31 * result + (forBrowse ? 1 : 0);
        result = 31 * result + (forXmlOptions != null ? forXmlOptions.hashCode() : 0);
        result = 31 * result + (xmlPath != null ? xmlPath.hashCode() : 0);
        result = 31 * result + (rowCount != null ? rowCount.hashCode() : 0);
        result = 31 * result + (offset != null ? offset.hashCode() : 0);
        result = 31 * result + (headHint != null ? headHint.hashCode() : 0);
        return result;
    }

    public boolean addWhere(SQLExpr where) {
        if (where == null) {
            return false;
        }
        if (query instanceof SQLSelectQueryBlock) {
            ((SQLSelectQueryBlock) query).addWhere(where);
            return true;
        }
        if (query instanceof SQLUnionQuery) {
            SQLSelectQueryBlock queryBlock = new SQLSelectQueryBlock(getDbType());
            queryBlock.setFrom(new SQLSelect(query), "u");
            queryBlock.addSelectItem(new SQLAllColumnExpr());
            queryBlock.setParent(queryBlock);
            query = queryBlock;
            return true;
        }
        return false;
    }

    public boolean replace(SQLSelectQuery cmp, SQLSelectQuery target) {
        if (cmp == query) {
            setQuery(target);
            return true;
        }
        return false;
    }

    public SQLLimit getLimit() {
        return limit;
    }

    public void setLimit(SQLLimit x) {
        if (x != null) {
            x.setParent(this);
        }
        this.limit = x;
    }
}
