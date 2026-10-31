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
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.Arrays;
import java.util.List;

public class SQLCastExpr extends SQLExprImpl implements SQLObjectWithDataType, SQLReplaceable {

    protected boolean isTry;

    protected SQLExpr expr;

    protected SQLDataType dataType;

    protected SQLExpr format;

    public SQLCastExpr() {
        // TODO
    }

    public SQLCastExpr(SQLExpr expr, SQLDataType dataType) {
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

    public SQLExpr getFormat() {
        return format;
    }

    public void setFormat(SQLExpr format) {
        if (format != null) {
            format.setParent(this);
        }
        this.format = format;
    }

    public SQLDataType getDataType() {
        // TODO
        return null;
    }

    public long dateTypeHashCode() {
        if (this.dataType == null) {
            return 0;
        }
        return this.dataType.nameHashCode64();
    }

    public void setDataType(SQLDataType dataType) {
        if (dataType != null) {
            dataType.setParent(this);
        }
        this.dataType = dataType;
    }

    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            if (this.expr != null) {
                this.expr.accept(visitor);
            }
            if (this.dataType != null) {
                this.dataType.accept(visitor);
            }
        }
        visitor.endVisit(this);
    }

    @Override
    public List getChildren() {
        return Arrays.asList(this.expr, this.dataType);
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

    public SQLDataType computeDataType() {
        return dataType;
    }

    public SQLCastExpr clone() {
        SQLCastExpr x = new SQLCastExpr();
        x.isTry = isTry;
        if (expr != null) {
            x.setExpr(expr.clone());
        }
        if (dataType != null) {
            x.setDataType(dataType.clone());
        }
        return x;
    }

    @Override
    public boolean replace(SQLExpr expr, SQLExpr target) {
        if (this.expr == expr) {
            setExpr(target);
            return true;
        }
        return false;
    }

    public boolean isTry() {
        return isTry;
    }

    public void setTry(boolean aTry) {
        isTry = aTry;
    }

    public String toString() {
        return SQLUtils.toSQLString(this);
    }
}
