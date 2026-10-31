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
package com.alibaba.druid.sql.ast.expr;

import com.alibaba.druid.sql.ast.*;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class SQLInSubQueryExpr extends SQLExprImpl implements Serializable, SQLReplaceable {

    private static final long serialVersionUID = 1L;

    private boolean not;

    private SQLExpr expr;

    public SQLSelect subQuery;

    // for ads query hint
    public SQLCommentHint hint;

    // for clickhouse
    private boolean global;

    public SQLInSubQueryExpr() {
        // TODO
    }

    public SQLInSubQueryExpr(SQLSelect select) {
        // TODO
    }

    public SQLInSubQueryExpr(SQLExpr expr, SQLSelectQueryBlock queryBlock) {
        // TODO
    }

    public SQLInSubQueryExpr clone() {
        SQLInSubQueryExpr x = new SQLInSubQueryExpr();
        x.not = not;
        x.global = global;
        if (expr != null) {
            x.setExpr(expr.clone());
        }
        if (subQuery != null) {
            x.setSubQuery(subQuery.clone());
        }
        return x;
    }

    public boolean isNot() {
        return not;
    }

    public void setNot(boolean not) {
        this.not = not;
    }

    public SQLExpr getExpr() {
        return expr;
    }

    public void setExpr(SQLExpr expr) {
        if (expr != null) {
            expr.setParent(this);
        }
        this.expr = expr;
    }

    public SQLSelect getSubQuery() {
        return this.subQuery;
    }

    public void setSubQuery(SQLSelect x) {
        if (x != null) {
            x.setParent(this);
        }
        this.subQuery = x;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            if (this.expr != null) {
                this.expr.accept(visitor);
            }
            if (this.subQuery != null) {
                this.subQuery.accept(visitor);
            }
        }
        visitor.endVisit(this);
    }

    public List<SQLObject> getChildren() {
        return Arrays.<SQLObject>asList(this.expr, this.subQuery);
    }

    @Override
    public int hashCode() {
        // TODO
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        // TODO
        return false;
    }

    public SQLDataType computeDataType() {
        return SQLBooleanExpr.DATA_TYPE;
    }

    @Override
    public boolean replace(SQLExpr expr, SQLExpr target) {
        if (this.expr == expr) {
            setExpr(target);
            return true;
        }
        return false;
    }

    public SQLCommentHint getHint() {
        return hint;
    }

    public void setHint(SQLCommentHint hint) {
        this.hint = hint;
    }

    public boolean isGlobal() {
        return global;
    }

    public void setGlobal(boolean global) {
        this.global = global;
    }
}
