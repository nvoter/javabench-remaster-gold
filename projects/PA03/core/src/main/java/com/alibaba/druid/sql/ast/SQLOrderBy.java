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
package com.alibaba.druid.sql.ast;

import com.alibaba.druid.sql.ast.statement.SQLSelectOrderByItem;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import java.util.List;

public final class SQLOrderBy extends SQLObjectImpl implements SQLReplaceable {

    protected final List<SQLSelectOrderByItem> items = new ArrayList<SQLSelectOrderByItem>();

    // for postgres
    private boolean siblings;

    public SQLOrderBy() {
        // TODO
    }

    public SQLOrderBy(SQLExpr expr) {
        // TODO
    }

    public SQLOrderBy(SQLExpr expr, SQLOrderingSpecification type) {
        // TODO
    }

    public void addItem(SQLSelectOrderByItem item) {
        // TODO
    }

    public void addItem(SQLExpr item) {
        // TODO
    }

    public List<SQLSelectOrderByItem> getItems() {
        // TODO
        return null;
    }

    public boolean isSiblings() {
        return this.siblings;
    }

    public void setSiblings(boolean siblings) {
        this.siblings = siblings;
    }

    protected void accept0(SQLASTVisitor v) {
        if (v.visit(this)) {
            for (int i = 0; i < this.items.size(); i++) {
                final SQLSelectOrderByItem item = this.items.get(i);
                item.accept(v);
            }
        }
        v.endVisit(this);
    }

    @Override
    public boolean equals(Object o) {
        // TODO
        return false;
    }

    @Override
    public int hashCode() {
        // TODO
        return 0;
    }

    public void addItem(SQLExpr expr, SQLOrderingSpecification type) {
        // TODO
    }

    @Override
    public boolean replace(SQLExpr expr, SQLExpr target) {
        // TODO
        return false;
    }

    protected SQLSelectOrderByItem createItem() {
        return new SQLSelectOrderByItem();
    }

    public SQLOrderBy clone() {
        SQLOrderBy x = new SQLOrderBy();
        for (SQLSelectOrderByItem item : items) {
            SQLSelectOrderByItem item1 = item.clone();
            item1.setParent(x);
            x.items.add(item1);
        }
        x.siblings = siblings;
        return x;
    }
}
