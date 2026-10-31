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
package com.alibaba.druid.sql.dialect.oracle.ast.expr;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLExprImpl;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.expr.SQLLiteralExpr;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.Collections;
import java.util.List;

public class OracleIntervalExpr extends SQLExprImpl implements SQLLiteralExpr, OracleExpr, SQLReplaceable {

    private SQLExpr value;

    private OracleIntervalType type;

    private SQLExpr precision;

    private Integer factionalSecondsPrecision;

    private OracleIntervalType toType;

    private SQLExpr toFactionalSecondsPrecision;

    public OracleIntervalExpr() {
        // TODO
    }

    @Override
    public OracleIntervalExpr clone() {
        OracleIntervalExpr x = new OracleIntervalExpr();
        if (value != null) {
            x.setValue(value.clone());
        }
        x.type = type;
        x.precision = precision;
        x.factionalSecondsPrecision = factionalSecondsPrecision;
        x.toType = toType;
        x.toFactionalSecondsPrecision = toFactionalSecondsPrecision;
        return x;
    }

    @Override
    public boolean replace(SQLExpr expr, SQLExpr target) {
        if (this.value == expr) {
            setValue(target);
            return true;
        }
        if (this.precision == expr) {
            setPrecision(target);
            return true;
        }
        if (this.toFactionalSecondsPrecision == expr) {
            setToFactionalSecondsPrecision(target);
            return true;
        }
        return false;
    }

    @Override
    public List<SQLObject> getChildren() {
        return Collections.<SQLObject>singletonList(this.value);
    }

    public SQLExpr getValue() {
        return this.value;
    }

    public void setValue(SQLExpr value) {
        if (value != null) {
            value.setParent(this);
        }
        this.value = value;
    }

    public OracleIntervalType getType() {
        return this.type;
    }

    public void setType(OracleIntervalType type) {
        this.type = type;
    }

    public SQLExpr getPrecision() {
        return this.precision;
    }

    public void setPrecision(Integer precision) {
        this.setPrecision(new SQLIntegerExpr(precision));
    }

    public void setPrecision(SQLExpr precision) {
        if (precision != null) {
            precision.setParent(this);
        }
        this.precision = precision;
    }

    public Integer getFactionalSecondsPrecision() {
        return this.factionalSecondsPrecision;
    }

    public void setFactionalSecondsPrecision(Integer factionalSecondsPrecision) {
        this.factionalSecondsPrecision = factionalSecondsPrecision;
    }

    public OracleIntervalType getToType() {
        return this.toType;
    }

    public void setToType(OracleIntervalType toType) {
        this.toType = toType;
    }

    public SQLExpr getToFactionalSecondsPrecision() {
        return this.toFactionalSecondsPrecision;
    }

    public void setToFactionalSecondsPrecision(SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.toFactionalSecondsPrecision = x;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        this.accept0((OracleASTVisitor) visitor);
    }

    @Override
    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, value);
            this.acceptChild(visitor, precision);
            this.acceptChild(visitor, toFactionalSecondsPrecision);
        }
        visitor.endVisit(this);
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
}
