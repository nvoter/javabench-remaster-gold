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
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class SQLBetweenExpr extends SQLExprImpl implements SQLReplaceable, Serializable {

    private static final long serialVersionUID = 1L;

    public SQLExpr testExpr;

    private boolean not;

    public SQLExpr beginExpr;

    public SQLExpr endExpr;

    public SQLBetweenExpr() {
        // TODO
    }

    public SQLBetweenExpr clone() {
        SQLBetweenExpr x = new SQLBetweenExpr();
        if (testExpr != null) {
            x.setTestExpr(testExpr.clone());
        }
        x.not = not;
        if (beginExpr != null) {
            x.setBeginExpr(beginExpr.clone());
        }
        if (endExpr != null) {
            x.setEndExpr(endExpr.clone());
        }
        x.setParenthesized(parenthesized);
        return x;
    }

    public SQLBetweenExpr(SQLExpr testExpr, SQLExpr beginExpr, SQLExpr endExpr) {
        // TODO
    }

    public SQLBetweenExpr(SQLExpr testExpr, boolean not, SQLExpr beginExpr, SQLExpr endExpr) {
        this(testExpr, beginExpr, endExpr);
        // TODO
    }

    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            if (this.testExpr != null) {
                this.testExpr.accept(visitor);
            }
            if (this.beginExpr != null) {
                this.beginExpr.accept(visitor);
            }
            if (this.endExpr != null) {
                this.endExpr.accept(visitor);
            }
        }
        visitor.endVisit(this);
    }

    public List<SQLObject> getChildren() {
        return Arrays.<SQLObject>asList(this.testExpr, beginExpr, this.endExpr);
    }

    public SQLExpr getTestExpr() {
        return this.testExpr;
    }

    public void setTestExpr(SQLExpr testExpr) {
        if (testExpr != null) {
            testExpr.setParent(this);
        }
        this.testExpr = testExpr;
    }

    public boolean isNot() {
        return this.not;
    }

    public void setNot(boolean not) {
        this.not = not;
    }

    public SQLExpr getBeginExpr() {
        return this.beginExpr;
    }

    public void setBeginExpr(SQLExpr beginExpr) {
        if (beginExpr != null) {
            beginExpr.setParent(this);
        }
        this.beginExpr = beginExpr;
    }

    public SQLExpr getEndExpr() {
        // TODO
        return null;
    }

    public void setEndExpr(SQLExpr endExpr) {
        if (endExpr != null) {
            endExpr.setParent(this);
        }
        this.endExpr = endExpr;
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
    public SQLDataType computeDataType() {
        return SQLBooleanExpr.DATA_TYPE;
    }

    @Override
    public boolean replace(SQLExpr expr, SQLExpr target) {
        if (expr == testExpr) {
            setTestExpr(target);
            return true;
        }
        if (expr == beginExpr) {
            setBeginExpr(target);
            return true;
        }
        if (expr == endExpr) {
            setEndExpr(target);
            return true;
        }
        return false;
    }
}
