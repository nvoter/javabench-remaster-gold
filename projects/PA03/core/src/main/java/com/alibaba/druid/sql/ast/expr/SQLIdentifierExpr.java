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

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.*;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.Collections;
import java.util.List;

public final class SQLIdentifierExpr extends SQLExprImpl implements SQLName, Comparable<SQLIdentifierExpr> {

    protected String name;

    protected long hashCode64;

    private SQLObject resolvedColumn;

    private SQLObject resolvedOwnerObject;

    protected String collate;

    public SQLIdentifierExpr() {
        // TODO
    }

    public SQLIdentifierExpr(String name) {
        // TODO
    }

    public SQLIdentifierExpr(String name, long hash_lower) {
        // TODO
    }

    public String getSimpleName() {
        return name;
    }

    public String getLowerName() {
        if (name == null) {
            return null;
        }
        return name.toLowerCase();
    }

    public String getName() {
        // TODO
        return null;
    }

    public void setName(String name) {
        // TODO
    }

    public String getCollate() {
        return collate;
    }

    public void setCollate(String collate) {
        this.collate = collate;
    }

    public long nameHashCode64() {
        // TODO
        return 0L;
    }

    @Override
    public long hashCode64() {
        // TODO
        return 0L;
    }

    public void output(StringBuilder buf) {
        buf.append(this.name);
    }

    protected void accept0(SQLASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }

    @Override
    public int hashCode() {
        long value = hashCode64();
        return (int) (value ^ (value >>> 32));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SQLPropertyExpr) {
            return ((SQLPropertyExpr) obj).equals(this);
        }
        if (!(obj instanceof SQLIdentifierExpr)) {
            return false;
        }
        SQLIdentifierExpr other = (SQLIdentifierExpr) obj;
        return this.hashCode64() == other.hashCode64();
    }

    public String toString() {
        return this.name;
    }

    public SQLIdentifierExpr clone() {
        SQLIdentifierExpr x = new SQLIdentifierExpr(this.name, hashCode64);
        x.resolvedColumn = resolvedColumn;
        x.resolvedOwnerObject = resolvedOwnerObject;
        if (hint != null) {
            x.hint = hint.clone();
        }
        x.collate = collate;
        return x;
    }

    public SQLIdentifierExpr simplify() {
        String normalized = SQLUtils.normalize(name);
        if (normalized != name) {
            return new SQLIdentifierExpr(normalized, hashCode64);
        }
        return this;
    }

    public String normalizedName() {
        return SQLUtils.normalize(name);
    }

    public SQLColumnDefinition getResolvedColumn() {
        // TODO
        return null;
    }

    public SQLSelectItem getResolvedSelectItem() {
        if (resolvedColumn instanceof SQLSelectItem) {
            return (SQLSelectItem) resolvedColumn;
        }
        return null;
    }

    public SQLObject getResolvedColumnObject() {
        return resolvedColumn;
    }

    public void setResolvedColumn(SQLColumnDefinition resolvedColumn) {
        this.resolvedColumn = resolvedColumn;
    }

    public void setResolvedColumn(SQLObject resolvedColumn) {
        this.resolvedColumn = resolvedColumn;
    }

    public void setResolvedColumn(SQLSelectItem selectItem) {
        this.resolvedColumn = selectItem;
    }

    public SQLTableSource getResolvedTableSource() {
        // TODO
        return null;
    }

    public void setResolvedTableSource(SQLTableSource resolvedTableSource) {
        this.resolvedOwnerObject = resolvedTableSource;
    }

    public SQLObject getResolvedOwnerObject() {
        return resolvedOwnerObject;
    }

    public void setResolvedOwnerObject(SQLObject resolvedOwnerObject) {
        this.resolvedOwnerObject = resolvedOwnerObject;
    }

    public SQLParameter getResolvedParameter() {
        if (resolvedColumn instanceof SQLParameter) {
            return (SQLParameter) this.resolvedColumn;
        }
        return null;
    }

    public void setResolvedParameter(SQLParameter resolvedParameter) {
        this.resolvedColumn = resolvedParameter;
    }

    public SQLDeclareItem getResolvedDeclareItem() {
        if (resolvedColumn instanceof SQLDeclareItem) {
            return (SQLDeclareItem) this.resolvedColumn;
        }
        return null;
    }

    public void setResolvedDeclareItem(SQLDeclareItem resolvedDeclareItem) {
        this.resolvedColumn = resolvedDeclareItem;
    }

    public SQLDataType computeDataType() {
        SQLColumnDefinition resolvedColumn = getResolvedColumn();
        if (resolvedColumn != null) {
            return resolvedColumn.getDataType();
        }
        if (this.resolvedColumn instanceof SQLSelectItem) {
            if (((SQLSelectItem) this.resolvedColumn).getExpr() != this) {
                return ((SQLSelectItem) this.resolvedColumn).computeDataType();
            }
            return null;
        }
        SQLSelectQueryBlock queryBlock = null;
        if (resolvedOwnerObject instanceof SQLSubqueryTableSource) {
            SQLSelect select = ((SQLSubqueryTableSource) resolvedOwnerObject).getSelect();
            queryBlock = select.getFirstQueryBlock();
        } else if (resolvedOwnerObject instanceof SQLUnionQueryTableSource) {
            SQLUnionQuery union = ((SQLUnionQueryTableSource) resolvedOwnerObject).getUnion();
            queryBlock = union.getFirstQueryBlock();
        } else if (resolvedOwnerObject instanceof SQLWithSubqueryClause.Entry) {
            queryBlock = ((SQLWithSubqueryClause.Entry) resolvedOwnerObject).getSubQuery().getFirstQueryBlock();
        }
        if (queryBlock != null) {
            SQLSelectItem selectItem = queryBlock.findSelectItem(nameHashCode64());
            if (selectItem != null) {
                return selectItem.computeDataType();
            }
        }
        for (SQLObject parent = this.parent; parent != null; parent = parent.getParent()) {
            if (parent instanceof SQLCreateFunctionStatement) {
                SQLCreateFunctionStatement createFunction = (SQLCreateFunctionStatement) parent;
                SQLParameter parameter = createFunction.getParameter(this.name);
                return parameter != null ? parameter.computeDataType() : null;
            }
        }
        return null;
    }

    public boolean nameEquals(String name) {
        // TODO
        return false;
    }

    @Override
    public List<SQLObject> getChildren() {
        return Collections.emptyList();
    }

    public static boolean matchIgnoreCase(SQLExpr expr, String name) {
        if (!(expr instanceof SQLIdentifierExpr)) {
            return false;
        }
        SQLIdentifierExpr ident = (SQLIdentifierExpr) expr;
        return ident.getName().equalsIgnoreCase(name);
    }

    @Override
    public int compareTo(SQLIdentifierExpr o) {
        return this.normalizedName().compareTo(o.normalizedName());
    }
}
