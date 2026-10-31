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

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLExprImpl;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import java.util.List;

/**
 * AST node for the {@code GROUPING SETS(...)} expression inside a
 * {@code GROUP BY} clause.
 *
 * <p>{@link #hasPrefixComma} records whether the source SQL had a comma
 * immediately before this expression when it appears as a non-first sibling
 * in a {@code GROUP BY} item list. The flag participates in {@link #equals},
 * {@link #hashCode}, and {@link #clone}; two instances that differ only in
 * this flag are considered not equal so AST diff tools can distinguish
 * {@code GROUP BY x, GROUPING SETS (...)} from
 * {@code GROUP BY x GROUPING SETS (...)}.
 */
public class SQLGroupingSetExpr extends SQLExprImpl implements SQLReplaceable {

    private final List<SQLExpr> parameters = new ArrayList<SQLExpr>();

    /**
     * {@code true} when the source SQL had a comma immediately before this
     * {@code GROUPING SETS} sibling in a {@code GROUP BY} clause. Default
     * {@code true} preserves the historically dominant emit shape
     * ({@code GROUP BY x, GROUPING SETS (...)}) for AST consumers that build
     * clauses programmatically without setting this flag.
     */
    private boolean hasPrefixComma = true;

    public SQLGroupingSetExpr clone() {
        // TODO
        return null;
    }

    public boolean isHasPrefixComma() {
        // TODO
        return false;
    }

    public void setHasPrefixComma(boolean hasPrefixComma) {
        // TODO
    }

    public List<SQLExpr> getParameters() {
        return parameters;
    }

    public void addParameter(SQLExpr parameter) {
        // TODO
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, parameters);
        }
        visitor.endVisit(this);
    }

    @Override
    public List getChildren() {
        return this.parameters;
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

    @Override
    public boolean replace(SQLExpr expr, SQLExpr target) {
        for (int i = 0; i < parameters.size(); i++) {
            if (parameters.get(i) == expr) {
                target.setParent(this);
                parameters.set(i, target);
                return true;
            }
        }
        return false;
    }
}
