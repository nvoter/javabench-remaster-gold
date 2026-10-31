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
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import java.util.List;

public class SQLDeleteStatement extends SQLStatementImpl implements SQLReplaceable {

    protected SQLWithSubqueryClause with;

    protected SQLTableSource tableSource;

    protected SQLExpr where;

    protected SQLTableSource from;

    protected SQLTableSource using;

    protected boolean only;

    public SQLDeleteStatement() {
        // TODO
    }

    public SQLDeleteStatement(DbType dbType) {
        super(dbType);
        // TODO
    }

    protected void cloneTo(SQLDeleteStatement x) {
        if (headHints != null) {
            for (SQLCommentHint h : headHints) {
                SQLCommentHint h2 = h.clone();
                h2.setParent(x);
                x.headHints.add(h2);
            }
        }
        if (with != null) {
            x.setWith(with.clone());
        }
        if (tableSource != null) {
            x.setTableSource(tableSource.clone());
        }
        if (where != null) {
            x.setWhere(where.clone());
        }
        if (from != null) {
            x.setFrom(from.clone());
        }
        if (using != null) {
            x.setUsing(using.clone());
        }
        x.only = only;
    }

    public SQLDeleteStatement clone() {
        SQLDeleteStatement x = new SQLDeleteStatement();
        cloneTo(x);
        return x;
    }

    public SQLTableSource getTableSource() {
        // TODO
        return null;
    }

    public SQLExprTableSource getExprTableSource() {
        return (SQLExprTableSource) getTableSource();
    }

    public void setTableSource(SQLExpr expr) {
        this.setTableSource(new SQLExprTableSource(expr));
    }

    public void setTableSource(SQLTableSource tableSource) {
        if (tableSource != null) {
            tableSource.setParent(this);
        }
        this.tableSource = tableSource;
    }

    public SQLName getTableName() {
        // TODO
        return null;
    }

    public void setTableName(SQLName tableName) {
        this.setTableSource(new SQLExprTableSource(tableName));
    }

    public void setTableName(String name) {
        setTableName(new SQLIdentifierExpr(name));
    }

    public SQLExpr getWhere() {
        // TODO
        return null;
    }

    public void setWhere(SQLExpr where) {
        if (where != null) {
            where.setParent(this);
        }
        this.where = where;
    }

    public String getAlias() {
        return this.tableSource.getAlias();
    }

    public void setAlias(String alias) {
        this.tableSource.setAlias(alias);
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            if (with != null) {
                with.accept(visitor);
            }
            if (tableSource != null) {
                tableSource.accept(visitor);
            }
            if (where != null) {
                where.accept(visitor);
            }
        }
        visitor.endVisit(this);
    }

    @Override
    public List<SQLObject> getChildren() {
        List<SQLObject> children = new ArrayList<SQLObject>();
        if (with != null) {
            children.add(with);
        }
        children.add(tableSource);
        if (where != null) {
            children.add(where);
        }
        return children;
    }

    public SQLTableSource getFrom() {
        return from;
    }

    public void setFrom(SQLTableSource from) {
        if (from != null) {
            from.setParent(this);
        }
        this.from = from;
    }

    @Override
    public boolean replace(SQLExpr expr, SQLExpr target) {
        if (where == expr) {
            setWhere(target);
            return true;
        }
        return false;
    }

    public boolean isOnly() {
        return only;
    }

    public void setOnly(boolean only) {
        this.only = only;
    }

    public SQLTableSource getUsing() {
        // TODO
        return null;
    }

    public void setUsing(SQLTableSource using) {
        this.using = using;
    }

    public SQLWithSubqueryClause getWith() {
        return with;
    }

    public void setWith(SQLWithSubqueryClause with) {
        if (with != null) {
            with.setParent(this);
        }
        this.with = with;
    }

    public void addCondition(String conditionSql) {
        // TODO
    }

    public void addCondition(SQLExpr expr) {
        // TODO
    }

    public boolean removeCondition(String conditionSql) {
        // TODO
        return false;
    }

    public boolean removeCondition(SQLExpr condition) {
        // TODO
        return false;
    }

    public boolean addWhere(SQLExpr where) {
        // TODO
        return false;
    }
}
