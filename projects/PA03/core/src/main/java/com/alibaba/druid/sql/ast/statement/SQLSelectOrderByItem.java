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

import com.alibaba.druid.sql.ast.*;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;

public final class SQLSelectOrderByItem extends SQLExprImpl implements SQLReplaceable {

    protected SQLExpr expr;

    protected String collate;

    protected SQLExpr opclass;

    protected SQLOrderingSpecification type;

    protected NullsOrderType nullsOrderType;

    protected transient SQLSelectItem resolvedSelectItem;

    public SQLSelectOrderByItem() {
        // TODO
    }

    public SQLSelectOrderByItem(SQLExpr expr) {
        // TODO
    }

    public SQLSelectOrderByItem(SQLExpr expr, SQLOrderingSpecification type) {
        // TODO
    }

    public SQLExpr getExpr() {
        // TODO
        return null;
    }

    public void setExpr(SQLExpr expr) {
        if (expr != null) {
            expr.setParent(this);
        }
        this.expr = expr;
    }

    public String getCollate() {
        return collate;
    }

    public void setCollate(String collate) {
        this.collate = collate;
    }

    public SQLExpr getOpclass() {
        return opclass;
    }

    public void setOpclass(SQLExpr opclass) {
        this.opclass = opclass;
    }

    public SQLOrderingSpecification getType() {
        return this.type;
    }

    public void setType(SQLOrderingSpecification type) {
        this.type = type;
    }

    public NullsOrderType getNullsOrderType() {
        return this.nullsOrderType;
    }

    public void setNullsOrderType(NullsOrderType nullsOrderType) {
        this.nullsOrderType = nullsOrderType;
    }

    protected void accept0(SQLASTVisitor v) {
        if (v.visit(this)) {
            if (expr != null) {
                expr.accept(v);
            }
        }
        v.endVisit(this);
    }

    @Override
    public int hashCode() {
        // TODO
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        SQLSelectOrderByItem other = (SQLSelectOrderByItem) obj;
        if (collate == null) {
            if (other.collate != null) {
                return false;
            }
        } else if (!collate.equals(other.collate)) {
            return false;
        }
        if (expr == null) {
            if (other.expr != null) {
                return false;
            }
        } else if (!expr.equals(other.expr)) {
            return false;
        }
        if (type != other.type) {
            return false;
        }
        return true;
    }

    @Override
    public boolean replace(SQLExpr expr, SQLExpr target) {
        if (this.expr == expr) {
            if (target instanceof SQLIntegerExpr && parent instanceof SQLOrderBy) {
                ((SQLOrderBy) parent).getItems().remove(this);
            }
            this.setExpr(target);
            return true;
        }
        return false;
    }

    public static enum NullsOrderType {

        NullsFirst, NullsLast;

        public String toFormalString() {
            if (NullsFirst.equals(this)) {
                return "NULLS FIRST";
            }
            if (NullsLast.equals(this)) {
                return "NULLS LAST";
            }
            throw new IllegalArgumentException();
        }
    }

    public SQLSelectOrderByItem clone() {
        SQLSelectOrderByItem x = new SQLSelectOrderByItem();
        if (expr != null) {
            x.setExpr(expr.clone());
        }
        x.collate = collate;
        x.type = type;
        x.nullsOrderType = nullsOrderType;
        return x;
    }

    public SQLSelectItem getResolvedSelectItem() {
        return resolvedSelectItem;
    }

    public void setResolvedSelectItem(SQLSelectItem resolvedSelectItem) {
        this.resolvedSelectItem = resolvedSelectItem;
    }

    public boolean isClusterBy() {
        // TODO
        return false;
    }

    public boolean isSortBy() {
        // TODO
        return false;
    }

    public boolean isDistributeBy() {
        // TODO
        return false;
    }
}
